package com.huawei.courselearningdemo.viewmodel;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.iap.IapApiException;
import com.huawei.hms.iap.entity.ConsumeOwnedPurchaseReq;
import com.huawei.hms.iap.entity.ConsumeOwnedPurchaseResult;
import com.huawei.hms.iap.entity.InAppPurchaseData;
import com.huawei.hms.iap.entity.IsEnvReadyResult;
import com.huawei.hms.iap.entity.OrderStatusCode;
import com.huawei.hms.iap.entity.OwnedPurchasesReq;
import com.huawei.hms.iap.entity.OwnedPurchasesResult;
import com.huawei.hms.iap.entity.ProductInfo;
import com.huawei.hms.iap.entity.ProductInfoReq;
import com.huawei.hms.iap.entity.PurchaseIntentReq;
import com.huawei.hms.iap.entity.PurchaseIntentResult;
import com.huawei.hms.support.api.client.Status;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huawei.hms.iap.IapClient;
import com.huawei.hms.iap.entity.ProductInfoResult;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import com.huawei.courselearningdemo.model.RechargeOrder;
import com.huawei.courselearningdemo.model.ResultVO;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.network.RetrofitClient;
import com.huawei.courselearningdemo.utils.CipherUtil;
import com.huawei.courselearningdemo.utils.Constant;
import com.huawei.courselearningdemo.utils.LogUtil;
import com.huawei.courselearningdemo.utils.RechargeState;
import com.huawei.courselearningdemo.utils.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RechargeViewModel extends ViewModel {
    private IapClient client;
    private String uid = null;
    private boolean envReady = false;
    private ProductInfo productInfo = null;
    private final MutableLiveData<RechargeState> rechargeStateData = new MutableLiveData<>();
    // 用于拉起华为IAP收银台页面的status
    private Status resolutionStatus = null;

    public void init(IapClient client, String uid){
        this.client = client;
        this.uid = uid;
        // 一旦设定了IapClient 就开始判断是否支持应用内支付
        queryIsEnvReady();
    }

    public void resetEnvReady(){
        envReady = false;
    }

    public boolean isEnvReady(){
        return envReady;
    }

    public ProductInfo getProductInfo(){
        return productInfo;
    }

    public LiveData<RechargeState> getRechargeStateData() {
        return rechargeStateData;
    }

    public Status getResolutionStatus(){
        return resolutionStatus;
    }

    // 判断当前环境是否支持应用内支付
    public void queryIsEnvReady(){
        rechargeStateData.setValue(RechargeState.INITIALIZING);
        Task<IsEnvReadyResult> task = client.isEnvReady();
        task.addOnSuccessListener(new OnSuccessListener<IsEnvReadyResult>() {
            @Override
            public void onSuccess(IsEnvReadyResult result) {
                envReady = true;
                // 如果环境支持，则加载商品信息
                queryProduct();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                envReady = false;
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    if (apiException.getStatus().getStatusCode() == OrderStatusCode.ORDER_ACCOUNT_AREA_NOT_SUPPORTED) {
                        // 用户当前登录的华为帐号所在的服务地不在华为IAP支持结算的国家或地区中
                        rechargeStateData.setValue(RechargeState.UNSUPPORTED);
                    }else
                        rechargeStateData.setValue(RechargeState.ENV_ERROR);
                    LogUtil.e(task, "queryIsEnvReady fail status code: "+apiException.getStatus().getStatusCode());
                }else {
                    LogUtil.e(task, "queryIsEnvReady fail failure: "+e.getMessage() + "  caused by: "+e.getCause());
                    rechargeStateData.setValue(RechargeState.ENV_ERROR);
                }
            }
        });
    }

    // 查询商品
    public void queryProduct() {
        List<String> productIdList = new ArrayList<String>();
        productIdList.add(Constant.PRODUCT_ID_FOR_10_DIAMONDS);
        ProductInfoReq req = new ProductInfoReq();
        // priceType: 0：消耗型商品
        req.setPriceType(0);
        req.setProductIds(productIdList);
        Task<ProductInfoResult> task = client.obtainProductInfo(req);
        task.addOnSuccessListener(new OnSuccessListener<ProductInfoResult>() {
            @Override
            public void onSuccess(ProductInfoResult result) {
                if(result.getProductInfoList()==null || result.getProductInfoList().size()==0) {
                    LogUtil.e(task, "getProductInfoList null or empty!");
                    rechargeStateData.setValue(RechargeState.PRODUCT_ERROR);
                    productInfo = null;
                }
                else{
                    // 获取接口请求成功时返回的商品详情信息
                    productInfo = result.getProductInfoList().get(0);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                productInfo = null;
                rechargeStateData.setValue(RechargeState.PRODUCT_ERROR);
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    LogUtil.e("getProductInfoList", "fail status code: "+apiException.getStatus().getStatusCode() + " "+ apiException.getMessage());
                } else {
                    LogUtil.e("getProductInfoList", "fail failure: "+e.getMessage() + "  caused by: "+e.getCause());
                }
            }
        });
    }

    public void createPurchaseIntent(){
        // 构造一个PurchaseIntentReq对象
        PurchaseIntentReq req = new PurchaseIntentReq();
        // 通过createPurchaseIntent接口购买的商品必须是您在AppGallery Connect网站配置的商品。
        req.setProductId(Constant.PRODUCT_ID_FOR_10_DIAMONDS);
        // 消耗型商品
        req.setPriceType(0);
        req.setDeveloperPayload("test");
        // 调用createPurchaseIntent接口创建托管商品订单
        Task<PurchaseIntentResult> task = client.createPurchaseIntent(req);
        task.addOnSuccessListener(new OnSuccessListener<PurchaseIntentResult>() {
            @Override
            public void onSuccess(PurchaseIntentResult result) {
                if(result == null || result.getStatus() == null) {
                    LogUtil.e(task, "createPurchaseIntent result or status is null!");
                }else {
                    LogUtil.i("createPurchaseIntent", "success");
                    Status status = result.getStatus();
                    if(status.hasResolution()) {
                        resolutionStatus = status;
                        rechargeStateData.setValue(RechargeState.INITIALIZED);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    LogUtil.e("createPurchaseIntent", "fail status code: "+apiException.getStatus().getStatusCode());
                } else {
                    LogUtil.e("createPurchaseIntent","fail failure: "+e.getMessage() + "  caused by: "+e.getCause());
                }
            }
        });
    }

    public void recharge10Diamonds(String orderId, String purchaseToken){
        LogUtil.i("开始检查订单对应的钻石是否已发货，未发货则进行发货", "orderId: "+orderId);
        rechargeStateData.setValue(RechargeState.PURCHASED);
        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setUid(uid);
        rechargeOrder.setOrderId(orderId);
        rechargeOrder.setValue(10);
        // 调用应用服务器的recharge接口，若传入的订单id在数据库内已存在，则钻石已经发货过了，否则重新发货
        Call<ResultVO<User>> rechargeCall = RetrofitClient.getUserDao().recharge(rechargeOrder);
        rechargeCall.enqueue(new Callback<ResultVO<User>>() {
            @Override
            public void onResponse(Call<ResultVO<User>> call, Response<ResultVO<User>> response) {
                if(response.body() == null || response.body().getCode() == null)
                    LogUtil.e("rechargeCall", "response Null Error!");
                else if(response.body().getCode().equals(Constant.REQUEST_SUCCESS)) {
                    LogUtil.i("rechargeCall success", response.body().getMsg());
                    ToastUtil.showShortToast(response.body().getMsg());
                    // 发货成功
                    rechargeStateData.setValue(RechargeState.SUCCEED);
                    // 调用消耗型商品确认交易接口
                    consumeOwnedPurchase(purchaseToken);
                }
                else {
                    LogUtil.e("rechargeCall", response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<ResultVO<User>> call, Throwable t) {
                ToastUtil.showShortToast("网络错误，请稍后重试");
            }
        });
    }

    // （消耗型）商品发货成功后执行消耗操作，通知华为IAP服务器更新对应的状态，使得该消耗型商品可再次购买
    private void consumeOwnedPurchase(String purchaseToken){
        // 构造ConsumeOwnedPurchaseReq对象
        ConsumeOwnedPurchaseReq req = new ConsumeOwnedPurchaseReq();
        req.setPurchaseToken(purchaseToken);
        // 调用consumeOwnedPurchase接口进行消耗
        Task<ConsumeOwnedPurchaseResult> task = client.consumeOwnedPurchase(req);
        task.addOnSuccessListener(new OnSuccessListener<ConsumeOwnedPurchaseResult>() {
            @Override
            public void onSuccess(ConsumeOwnedPurchaseResult result) {
                // 获取接口请求结果
                LogUtil.d("consumeOwnedPurchase", "success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    LogUtil.e("consumeOwnedPurchase", "fail status code: "+apiException.getStatus().getStatusCode());
                } else {
                    LogUtil.e("consumeOwnedPurchase",  "checkOwedPurchases fail failure: "+e.getMessage() + "  caused by: "+e.getCause());
                }
            }
        });
    }

    // 检查消耗型商品是否漏单，若漏单则进行补单操作
    public void checkOwedPurchases(){
        // 构造一个OwnedPurchasesReq对象
        OwnedPurchasesReq ownedPurchasesReq = new OwnedPurchasesReq();
        // priceType: 0：消耗型商品
        ownedPurchasesReq.setPriceType(0);
        // 调用obtainOwnedPurchases接口获取所有已购但未发货的消耗型商品
        Task<OwnedPurchasesResult> task = client.obtainOwnedPurchases(ownedPurchasesReq);
        task.addOnSuccessListener(new OnSuccessListener<OwnedPurchasesResult>() {
            @Override
            public void onSuccess(OwnedPurchasesResult result) {
                // 获取接口请求成功的结果
                if (result != null && result.getInAppPurchaseDataList() != null) {
                    for (int i = 0; i < result.getInAppPurchaseDataList().size(); i++) {
                        String inAppPurchaseData = result.getInAppPurchaseDataList().get(i);
                        String inAppSignature = result.getInAppSignature().get(i);
                        // 使用应用的IAP公钥验证inAppPurchaseData的签名数据
                        // 如果验签成功，确认每个商品的购买状态。确认商品已支付后，检查此前是否已发过货，未发货则进行发货操作。发货成功后执行消耗操作
                        if(CipherUtil.doCheck(inAppPurchaseData, inAppSignature)) {
                            try {
                                LogUtil.i("检查是否漏单","第"+(i+1)+"个商品验签成功");
                                InAppPurchaseData inAppPurchaseDataBean = new InAppPurchaseData(inAppPurchaseData);
                                // 只需要检查purchaseState为0（已购买）的交易项
                                if(inAppPurchaseDataBean.getPurchaseState()==0 && inAppPurchaseDataBean.getProductId().equals(Constant.PRODUCT_ID_FOR_10_DIAMONDS)){
                                    recharge10Diamonds(inAppPurchaseDataBean.getOrderID(), inAppPurchaseDataBean.getPurchaseToken());
                                }
                            } catch (JSONException e) {
                                LogUtil.e("checkOwedPurchases", "InAppPurchaseData JSON parse error");
                            }
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    LogUtil.e("checkOwedPurchases", "fail status code: "+apiException.getStatus().getStatusCode());
                } else {
                    LogUtil.e("checkOwedPurchases",  "checkOwedPurchases fail failure: "+e.getMessage() + "  caused by: "+e.getCause());
                }
            }
        });
    }
}
