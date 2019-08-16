package com.jpay.unionpay;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class JPay {
    private static JPay mJPay;
    private Context mContext;

    private JPay(Context context) {
        mContext = context;
    }

    public static JPay getIntance(Activity context) {
        if (mJPay == null) {
            synchronized (JPay.class) {
                if (mJPay == null) {
                    mJPay = new JPay(context.getApplicationContext());
                }
            }
        }
        return mJPay;
    }

    public interface UnionPayListener {
        //支付成功
        void onPaySuccess();

        //支付失败
        void onPayError(int error_code, String message);

        //支付取消
        void onPayCancel();

        //银联支付结果回调
        void onUnionPay(String dataOrg, String sign, String mode);
    }

    public void toUnionPay(String payParameters, UnionPayListener listener) {
        if (payParameters != null) {
            JSONObject param = null;
            try {
                param = new JSONObject(payParameters);
            } catch (JSONException e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onPayError(UnionPay.PAY_PARAMETERS_ERROE, "参数异常");
                }
                return;
            }
            if (TextUtils.isEmpty(param.optString("mode")) || TextUtils.isEmpty(param.optString("tn"))) {
                if (listener != null) {
                    listener.onPayError(UnionPay.PAY_PARAMETERS_ERROE, "参数异常");
                }
                return;
            }
            toUnionPay(param.optString("mode"), param.optString("tn"), listener);
        } else {
            if (listener != null) {
                listener.onPayError(UnionPay.PAY_PARAMETERS_ERROE, "参数异常");
            }
        }
    }

    public void toUnionPay(String mode, String tn, UnionPayListener listener) {
        if (listener == null) {
            listener.onPayError(UnionPay.PAY_PARAMETERS_ERROE, "参数异常");
            return;
        }
        if (TextUtils.isEmpty(mode)) {
            mode = "00";
        }
        if (TextUtils.isEmpty(tn)) {
            listener.onPayError(UnionPay.PAY_PARAMETERS_ERROE, "参数异常");
            return;
        }
        UnionPay.getInstance(mContext).startUnionPay(mode, tn, listener);
    }

}