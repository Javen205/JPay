package com.jpay;

import android.app.Activity;
import android.text.TextUtils;

import com.jpay.alipay.Alipay;
import com.jpay.unionpay.UPPay;
import com.jpay.weixin.WeiXinPay;

import org.json.JSONException;
import org.json.JSONObject;


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

    public interface JPayListener {
        //支付成功
        void onPaySuccess();
        //支付失败
        void onPayError(int error_code, String message);
        //支付取消
        void onPayCancel();
        //银联支付结果回调
        void onUUPay(String dataOrg, String sign, String mode);
    }

    public enum PayMode {
        WXPAY, ALIPAY, UUPAY
    }

    public void toPay(PayMode payMode, String payParameters, JPayListener listener) {
        if (payMode.name().equalsIgnoreCase(PayMode.WXPAY.name())) {
            toWxPay(payParameters, listener);
        } else if (payMode.name().equalsIgnoreCase(PayMode.ALIPAY.name())) {
            toAliPay(payParameters, listener);
        } else if (payMode.name().equalsIgnoreCase(PayMode.UUPAY.name())) {
            toUUPay(payParameters,listener);
        }
    }


    public void toWxPay(String payParameters, JPayListener listener) {
        if (payParameters != null) {
            JSONObject param = null;
            try {
                param = new JSONObject(payParameters);
            } catch (JSONException e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
                }
                return;
            }
            if (TextUtils.isEmpty(param.optString("appId")) || TextUtils.isEmpty(param.optString("partnerId"))
                    || TextUtils.isEmpty(param.optString("prepayId")) || TextUtils.isEmpty(param.optString("nonceStr"))
                    || TextUtils.isEmpty(param.optString("timeStamp")) || TextUtils.isEmpty(param.optString("sign"))) {
                if (listener != null) {
                    listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
                }
                return;
            }
            toWxPay(param.optString("appId"),
                    param.optString("partnerId"), param.optString("prepayId"),
                    param.optString("nonceStr"), param.optString("timeStamp"),
                    param.optString("sign"), listener);

        } else {
            if (listener != null) {
                listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
            }
        }
    }

    public void toWxPay(String appId, String partnerId, String prepayId,
                        String nonceStr, String timeStamp, String sign, JPayListener listener) {
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(partnerId)
                || TextUtils.isEmpty(prepayId) || TextUtils.isEmpty(nonceStr)
                || TextUtils.isEmpty(timeStamp) || TextUtils.isEmpty(sign)) {
            if (listener != null) {
                listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
            }
            return;
        }
        WeiXinPay.getInstance(mContext).startWXPay(appId, partnerId, prepayId, nonceStr, timeStamp, sign, listener);
    }

    public void toAliPay(String payParameters, JPayListener listener) {
        if (payParameters != null) {
            if (listener != null) {
                Alipay.getInstance(mContext).startAliPay(payParameters, listener);
            }
        } else {
            if (listener != null) {
                listener.onPayError(Alipay.PAY_PARAMETERS_ERROE, "参数异常");
            }
        }
    }

    public void toUUPay(String payParameters, JPayListener listener) {
        if (payParameters != null) {
            JSONObject param = null;
            try {
                param = new JSONObject(payParameters);
            } catch (JSONException e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onPayError(UPPay.PAY_PARAMETERS_ERROE, "参数异常");
                }
                return;
            }
            if (TextUtils.isEmpty(param.optString("mode")) || TextUtils.isEmpty(param.optString("tn"))){
                if (listener != null) {
                    listener.onPayError(UPPay.PAY_PARAMETERS_ERROE, "参数异常");
                }
                return;
            }
            toUUPay(param.optString("mode"),
                    param.optString("tn"), listener);
        } else {
            if (listener != null) {
                listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
            }
        }
    }

    public void toUUPay(String mode, String tn, JPayListener listener) {
        if (listener == null) {
            listener.onPayError(UPPay.PAY_PARAMETERS_ERROE, "参数异常");
            return;
        }
        if (TextUtils.isEmpty(mode)) {
            mode = "00";
        }
        if (TextUtils.isEmpty(tn)) {
            listener.onPayError(UPPay.PAY_PARAMETERS_ERROE, "参数异常");
            return;
        }
        UPPay.getInstance(mContext).startUPPay(mode, tn, listener);
    }
}
