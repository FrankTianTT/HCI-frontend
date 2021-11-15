package com.huawei.courselearningdemo.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.entity.InAppPurchaseData;
import com.huawei.hms.iap.entity.OrderStatusCode;
import com.huawei.hms.iap.entity.ProductInfo;
import com.huawei.hms.iap.entity.PurchaseResultInfo;
import com.huawei.hms.support.api.client.Status;

import org.json.JSONException;

import butterknife.BindView;
import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.ui.activity.MainActivity;
import com.huawei.courselearningdemo.utils.CipherUtil;
import com.huawei.courselearningdemo.utils.Constant;
import com.huawei.courselearningdemo.utils.LogUtil;
import com.huawei.courselearningdemo.utils.RechargeState;
import com.huawei.courselearningdemo.utils.StringUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;
import com.huawei.courselearningdemo.viewmodel.RechargeViewModel;
import com.huawei.courselearningdemo.viewmodel.SharedViewModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends BaseFragment {
    private SharedViewModel sharedViewModel;
    private RechargeViewModel rechargeViewModel;
    private BaseFragment lastOneFragment = null;
    private FragmentManager fragmentManager = null;
    @BindView(R.id.user_profile)
    protected CircleImageView profileImageView;
    @BindView(R.id.login_tv)
    protected TextView loginTv;
    @BindView(R.id.user_name)
    protected TextView userNameTv;
    @BindView(R.id.user_balance)
    protected TextView userBalanceTv;
    @BindView(R.id.user_info_group)
    protected Group userInfoGroup;
    @BindView(R.id.account_recharge_btn)
    protected Button rechargeButton;
    @BindView(R.id.account_logout_btn)
    protected Button logoutButton;
    private MainActivity mainActivity;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_account;
    }

    @Override
    protected void initView() {
        initUnLogInView();
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    protected void initViewModel() {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        rechargeViewModel = new ViewModelProvider(this).get(RechargeViewModel.class);
    }

    @Override
    protected void initObserver() {
        sharedViewModel.getUid().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String uid) {
                if(uid != null) {
                    // 一旦检测到用户登录，就进行初始化：检测支付环境、检测是否有漏单未发货情况
                    rechargeViewModel.init(Iap.getIapClient(mainActivity), uid);
                    rechargeViewModel.checkOwedPurchases();
                }
            }
        });
        sharedViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (!TextUtils.isEmpty(user.getUname())) {
                    initLoggedInView();
                    userNameTv.setText(user.getUname());
                }
                if(user.getBalance() != null)
                    // 当调用setText()方法时，如果传入int型是不会被当成内容而是resourceID来使用
                    userBalanceTv.setText(String.valueOf(user.getBalance()));
                if (StringUtil.hasText(user.getPicture()))
                    Glide.with(AccountFragment.this).load(user.getPicture()).into(profileImageView);
            }
        });
        rechargeViewModel.getRechargeStateData().observe(this, new Observer<RechargeState>() {
            @Override
            public void onChanged(RechargeState rechargeState) {
                if(rechargeState.equals(RechargeState.INITIALIZED)){
                    Status resolutionStatus = rechargeViewModel.getResolutionStatus();
                    try {
                        // 打开华为应用内支付的支付页面
                        resolutionStatus.startResolutionForResult(mainActivity, Constant.REQUEST_CODE_BUY);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }else if(rechargeState.equals(RechargeState.SUCCEED)){
                    // 拉取最新的用户信息 - 触发界面显示的余额变化
                    sharedViewModel.reloadUserDataFromServer();
                }
            }
        });
    }

    @Override
    protected void initListener() {
        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.signIn();
            }
        });
        rechargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RechargeState state = rechargeViewModel.getRechargeStateData().getValue();
                ProductInfo productInfo = rechargeViewModel.getProductInfo();
                if(!rechargeViewModel.isEnvReady()){
                    if(state.equals(RechargeState.ENV_ERROR))
                        ToastUtil.showShortToast("网络异常，请稍后重试！");
                    else if(state.equals(RechargeState.UNSUPPORTED))
                        ToastUtil.showShortToast("非常抱歉，您当前登录的华为账号所在的服务地不在华为应用内支付服务支持结算的国家或地区中！");
                    else
                        ToastUtil.showShortToast("正在检查支付环境...");
                    rechargeViewModel.queryIsEnvReady(); // 再次尝试检测支付环境
                }else{
                    if (productInfo == null){
                        if(state.equals(RechargeState.PRODUCT_ERROR))
                            ToastUtil.showShortToast("商品项加载失败，请稍后重试..");
                        else
                            ToastUtil.showShortToast("正在加载商品信息..");
                        rechargeViewModel.queryProduct(); // 再次尝试加载商品信息
                    } else {
                        showPurchaseDialog(productInfo);
                    }
                }
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.signOut();
//                sharedViewModel.cancelAuthorization();
                initUnLogInView();
            }
        });
    }

    private void initUnLogInView(){
        loginTv.setVisibility(View.VISIBLE);
        userInfoGroup.setVisibility(View.GONE);
        logoutButton.setVisibility(View.GONE);
        rechargeButton.setVisibility(View.GONE);
        profileImageView.setImageResource(R.drawable.account_null);
    }

    private void initLoggedInView(){
        loginTv.setVisibility(View.GONE);
        userInfoGroup.setVisibility(View.VISIBLE);
        logoutButton.setVisibility(View.VISIBLE);
        rechargeButton.setVisibility(View.VISIBLE);
    }

    private void showPurchaseDialog(ProductInfo productInfo){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity());
        dialogBuilder.setTitle("购买 "+productInfo.getProductName());
        dialogBuilder.setMessage("确认支付 "+ productInfo.getPrice()+"？");
        dialogBuilder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rechargeViewModel.createPurchaseIntent();
                        LogUtil.d("ttt", "3");

                    }
                });
        dialogBuilder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showShortToast("取消支付");
                    }
                });
        // 显示对话框
        dialogBuilder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i(this, "onActivityResult 开始处理");
        if (data == null) {
            LogUtil.e(this, "onActivityResult data is null");
            return;
        }
        if (requestCode == Constant.REQUEST_CODE_BUY) {
            // 华为应用内支付结果处理
            // 调用parsePurchaseResultInfoFromIntent方法解析支付结果数据
            PurchaseResultInfo purchaseResultInfo = Iap.getIapClient(mainActivity).parsePurchaseResultInfoFromIntent(data);
            LogUtil.i(purchaseResultInfo, "order state code: "+purchaseResultInfo.getReturnCode());
            switch (purchaseResultInfo.getReturnCode()) {
                case OrderStatusCode.ORDER_STATE_CANCEL:
                    // 用户取消
                    break;
                case OrderStatusCode.ORDER_STATE_FAILED:
                case OrderStatusCode.ORDER_PRODUCT_OWNED:
                    // 当状态为支付失败或产品已订购时，需要检查是否存在未发货商品（消耗型商品可能需要补单）
                    ToastUtil.showShortToast("上一笔钻石充值订单可能存在漏单情况，正在检查...");
                    rechargeViewModel.checkOwedPurchases();
                    break;
                case OrderStatusCode.ORDER_STATE_SUCCESS:
                    // 支付成功
                    String inAppPurchaseData = purchaseResultInfo.getInAppPurchaseData();
                    String inAppPurchaseDataSignature = purchaseResultInfo.getInAppDataSignature();
                    // 若验签成功，则进行发货
                    if(CipherUtil.doCheck(inAppPurchaseData, inAppPurchaseDataSignature)) {
                        try {
                            InAppPurchaseData inAppPurchaseDataBean = new InAppPurchaseData(inAppPurchaseData);
                            if(inAppPurchaseDataBean.getProductId().equals(Constant.PRODUCT_ID_FOR_10_DIAMONDS)){
                                rechargeViewModel.recharge10Diamonds(inAppPurchaseDataBean.getOrderID(), inAppPurchaseDataBean.getPurchaseToken());
                            }
                        } catch (JSONException e) {
                            LogUtil.e("checkOwedPurchases", "InAppPurchaseData JSON parse error");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
