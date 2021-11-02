package com.huawei.courselearningdemo.service;

import android.text.TextUtils;
import android.util.Log;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

import java.util.Arrays;

import com.huawei.courselearningdemo.repository.UserLocalRepository;
import com.huawei.courselearningdemo.model.ResultVO;
import com.huawei.courselearningdemo.network.RetrofitClient;
import com.huawei.courselearningdemo.utils.LogUtil;
import com.huawei.courselearningdemo.utils.StringUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPushService extends HmsMessageService {
    private static final String TAG = "MyPushService";

    /**
     * Android 10.0及以上版本的设备上，调用getToken方法直接返回Token。如果当次调用失败会缓存申请，之后会自动重试申请，成功后则以onNewToken方法返回。
     * 低于Android 10.0的设备上，调用getToken方法返回为空，确保推送服务开通的情况下，Token值以onNewToken方法返回。
     * 此处覆写了onNewToken方法，将接收到的非空的Token值发送给服务器。
     */
    @Override
    public void onNewToken(String token) {
        LogUtil.i(TAG, "received refresh token:" + token);
        // send the token to your app server.
        if (!TextUtils.isEmpty(token)) {
            // This method callback must be completed in 10 seconds. Otherwise, you need to start a new Job for callback processing.
            refreshedTokenToServer(token);
        }
    }

    private void refreshedTokenToServer(String token) {
        if(UserLocalRepository.getUser() == null || !StringUtil.hasText(UserLocalRepository.getUser().getUid())) return;
        Call<ResultVO<String>> refreshUserTokenCall = RetrofitClient.userDao.refreshUserToken(UserLocalRepository.getUser().getUid(), token);
        refreshUserTokenCall.enqueue(new Callback<ResultVO<String>>() {
            @Override
            public void onResponse(Call call, Response response) {
                LogUtil.i("refreshUserToken", "success");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                LogUtil.i("refreshUserToken", "fail");
            }
        });
    }

    /**
     * 该方法用于接收透传消息方法
     * @param message RemoteMessage
     */
    @Override
    public void onMessageReceived(RemoteMessage message) {
        LogUtil.i(TAG, "onMessageReceived is called");
        if (message == null) {
            LogUtil.e(TAG, "Received message entity is null!");
            return;
        }


        LogUtil.i(TAG, "getCollapseKey: " + message.getCollapseKey()
                + "\n getData: " + message.getData()
                + "\n getFrom: " + message.getFrom()
                + "\n getTo: " + message.getTo()
                + "\n getMessageId: " + message.getMessageId()
                + "\n getOriginalUrgency: " + message.getOriginalUrgency()
                + "\n getUrgency: " + message.getUrgency()
                + "\n getSendTime: " + message.getSentTime()
                + "\n getMessageType: " + message.getMessageType()
                + "\n getTtl: " + message.getTtl());



        RemoteMessage.Notification notification = message.getNotification();
        if (notification != null) {
            LogUtil.i(TAG, "\n getImageUrl: " + notification.getImageUrl()
                    + "\n getTitle: " + notification.getTitle()
                    + "\n getTitleLocalizationKey: " + notification.getTitleLocalizationKey()
                    + "\n getTitleLocalizationArgs: " + Arrays.toString(notification.getTitleLocalizationArgs())
                    + "\n getBody: " + notification.getBody()
                    + "\n getBodyLocalizationKey: " + notification.getBodyLocalizationKey()
                    + "\n getBodyLocalizationArgs: " + Arrays.toString(notification.getBodyLocalizationArgs())
                    + "\n getIcon: " + notification.getIcon()
                    + "\n getSound: " + notification.getSound()
                    + "\n getTag: " + notification.getTag()
                    + "\n getColor: " + notification.getColor()
                    + "\n getClickAction: " + notification.getClickAction()
                    + "\n getChannelId: " + notification.getChannelId()
                    + "\n getLink: " + notification.getLink()
                    + "\n getNotifyId: " + notification.getNotifyId());
        }
    }

    /**
     * 申请Token失败时会回调该方法。
     */
    @Override
    public void onTokenError(Exception e) {
        super.onTokenError(e);
        Log.e(TAG, "onTokenError! error info: " +e.getMessage() + " caused by: "+ e.getCause());
    }
}
