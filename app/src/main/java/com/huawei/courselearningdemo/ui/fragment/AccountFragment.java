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
import com.huawei.courselearningdemo.ui.activity.ContentViewActivity;
import com.huawei.courselearningdemo.ui.activity.StarActivity;
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
    @BindView(R.id.star_btn)
    protected Button starButton;
    private MainActivity mainActivity;
    protected StarFragment sStarFragment;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_account;
    }

    public StarFragment getStarFragement(){
        if(this.sStarFragment==null){
            this.sStarFragment=new StarFragment();
        }
        return this.sStarFragment;
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
                    // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
                    // ?????????setText()????????????????????????int?????????????????????????????????resourceID?????????
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
                        // ??????????????????????????????????????????
                        resolutionStatus.startResolutionForResult(mainActivity, Constant.REQUEST_CODE_BUY);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }else if(rechargeState.equals(RechargeState.SUCCEED)){
                    // ??????????????????????????? - ?????????????????????????????????
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
                        ToastUtil.showShortToast("?????????????????????????????????");
                    else if(state.equals(RechargeState.UNSUPPORTED))
                        ToastUtil.showShortToast("????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
                    else
                        ToastUtil.showShortToast("????????????????????????...");
                    rechargeViewModel.queryIsEnvReady(); // ??????????????????????????????
                }else{
                    if (productInfo == null){
                        if(state.equals(RechargeState.PRODUCT_ERROR))
                            ToastUtil.showShortToast("???????????????????????????????????????..");
                        else
                            ToastUtil.showShortToast("????????????????????????..");
                        rechargeViewModel.queryProduct(); // ??????????????????????????????
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
        starButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //StarFragment star=getStarFragement();
                Intent intent = new Intent(getActivity(), StarActivity.class);
                // ???course???????????????intent?????????????????????????????????
                startActivity(intent);
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
        dialogBuilder.setTitle("?????? "+productInfo.getProductName());
        dialogBuilder.setMessage("???????????? "+ productInfo.getPrice()+"???");
        dialogBuilder.setPositiveButton("??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rechargeViewModel.createPurchaseIntent();
                        LogUtil.d("ttt", "3");

                    }
                });
        dialogBuilder.setNegativeButton("??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showShortToast("????????????");
                    }
                });
        // ???????????????
        dialogBuilder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i(this, "onActivityResult ????????????");
        if (data == null) {
            LogUtil.e(this, "onActivityResult data is null");
            return;
        }
        if (requestCode == Constant.REQUEST_CODE_BUY) {
            // ?????????????????????????????????
            // ??????parsePurchaseResultInfoFromIntent??????????????????????????????
            PurchaseResultInfo purchaseResultInfo = Iap.getIapClient(mainActivity).parsePurchaseResultInfoFromIntent(data);
            LogUtil.i(purchaseResultInfo, "order state code: "+purchaseResultInfo.getReturnCode());
            switch (purchaseResultInfo.getReturnCode()) {
                case OrderStatusCode.ORDER_STATE_CANCEL:
                    // ????????????
                    break;
                case OrderStatusCode.ORDER_STATE_FAILED:
                case OrderStatusCode.ORDER_PRODUCT_OWNED:
                    // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    ToastUtil.showShortToast("??????????????????????????????????????????????????????????????????...");
                    rechargeViewModel.checkOwedPurchases();
                    break;
                case OrderStatusCode.ORDER_STATE_SUCCESS:
                    // ????????????
                    String inAppPurchaseData = purchaseResultInfo.getInAppPurchaseData();
                    String inAppPurchaseDataSignature = purchaseResultInfo.getInAppDataSignature();
                    // ?????????????????????????????????
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
