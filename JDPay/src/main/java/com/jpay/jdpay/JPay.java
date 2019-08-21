package com.jpay.jdpay;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

public class JPay {
    private static JPay mJPay;
    private Activity mContext;

    private JPay(Activity context) {
        mContext = context;
    }

    public static JPay getIntance(Activity context) {
        if (mJPay == null) {
            synchronized (JPay.class) {
                if (mJPay == null) {
                    mJPay = new JPay(context);
                }
            }
        }
        return mJPay;
    }

    public interface JDPayListener {
        //支付成功
        void onPaySuccess();

        //支付失败
        void onPayError(String error_code, String message);

        //支付取消
        void onPayCancel();

        //支付结果回调
        void onJdPay(String dataOrg);
    }

    public void toAuthor(String orderId, String merchant,String appId,String signData,String extraInfo, JDPayListener listener) {
        if (listener == null) {
            Log.e("JdPay","参数错误");
            return;
        }
        if (TextUtils.isEmpty(orderId) || TextUtils.isEmpty(merchant) || TextUtils.isEmpty(appId) || TextUtils.isEmpty(signData)) {
            listener.onPayError(JDPay.PAY_PARAMETERS_ERROE, "参数异常");
            return;
        }
        JDPay.getInstance(mContext).author(orderId, merchant,appId,signData,extraInfo, listener);
    }

}