package com.huawei.courselearningdemo.utils;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import static com.huawei.courselearningdemo.utils.Constant.IAP_PUBLIC_KEY;

public class CipherUtil {
    private static final String TAG = "CipherUtil";

    private static final String SIGN_ALGORITHMS = "SHA256WithRSA";

    private static final String PUBLIC_KEY = IAP_PUBLIC_KEY;

    /**
     * 校验签名信息
     * @param content 结果字符串
     * @param sign 签名字符串
     * @return 是否校验通过
     */
    public static boolean doCheck(String content, String sign) {
        if (TextUtils.isEmpty(PUBLIC_KEY)) {
            Log.e(TAG, "publicKey is null");
            return false;
        }

        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(sign)) {
            Log.e(TAG, "data is error");
            return false;
        }

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(PUBLIC_KEY, Base64.DEFAULT);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));

            return signature.verify(Base64.decode(sign, Base64.DEFAULT));

        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "doCheck NoSuchAlgorithmException" + e);
        } catch (InvalidKeySpecException e) {
            Log.e(TAG, "doCheck InvalidKeySpecException" + e);
        } catch (InvalidKeyException e) {
            Log.e(TAG, "doCheck InvalidKeyException" + e);
        } catch (SignatureException e) {
            Log.e(TAG, "doCheck SignatureException" + e);
        }
        return false;
    }
}
