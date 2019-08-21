package com.jpay.jdpay;

import android.app.Activity;
import android.content.Intent;

import com.jdpaysdk.author.JDPayAuthor;

import org.json.JSONException;
import org.json.JSONObject;

public class JDPay {
    private static JDPay mJDPay;
    private Activity mContext;
    private String mMode;
    private JPay.JDPayListener mJPayListener;

    //支付失败
    public static final String PAY_ERROR = "JPay001";
    //回调参数异常
    public static final String PAY_CALLBACK_ERROR = "JPay002";
    //支付参数异常
    public static final String PAY_PARAMETERS_ERROE = "JPay003";

    private JDPay(Activity context) {
        mContext = context;
    }

    public static JDPay getInstance(Activity context) {
        if (mJDPay == null) {
            synchronized (JDPay.class) {
                if (mJDPay == null) {
                    mJDPay = new JDPay(context);
                }
            }
        }
        return mJDPay;
    }

    public  void author(String orderId, String merchant,String appId,String signData,String extraInfo, JPay.JDPayListener listener){
        this.mJPayListener = listener;
        JDPayAuthor jdPayAuthor = new JDPayAuthor();
        jdPayAuthor.author(mContext,orderId,merchant,appId,signData,extraInfo);
    }

    public  void openAccount(String merchant,String appkey,String biztype,String bizParam, JPay.JDPayListener listener){
        this.mJPayListener = listener;
        JDPayAuthor jdPayAuthor = new JDPayAuthor();
        jdPayAuthor.openAccount(mContext,merchant,appkey,biztype,bizParam);
    }

    /**
     * 处理支付结果回调
     *
     * @param data 支付回调的数据
     */
    public void onJDPayResult(Intent data) throws JSONException {
        if (data == null) {
            mJPayListener.onPayError(PAY_CALLBACK_ERROR, "callback error,data is null");
            return;
        }
        String str = data.getExtras().getString(JDPayAuthor.JDPAY_RESULT);
        JSONObject obj = new JSONObject(str);
        String payStatus = obj.optString("payStatus");
        String errorCode = obj.optString("errorCode");
        if (!errorCode.equals("0000") || payStatus.equals("JDP_PAY_FAIL")){
            mJPayListener.onPayError(errorCode,"");
        }else {
            mJPayListener.onJdPay(str);
        }
    }
}
