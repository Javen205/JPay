package com.jpay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.jpay.JPay.JPayListener;
import com.jpay.weixin.WeiXinPay;

import java.util.Map;

/**
 * Created by Javen on 2016/11/21.
 */

public class Alipay {
    private static Alipay mAliPay;
    private Activity mContext;
    private JPayListener mJPayListener;

    //支付失败
    public static final int PAY_ERROR = 0x001;
    //支付网络连接出错
    public static final int PAY_NETWORK_ERROR = 0x002;
    //支付结果解析错误
    public static final int RESULT_ERROR = 0x003;
    //正在处理中
    public static final int PAY_DEALING = 0x004;
    //其它支付错误
    public static final int PAY_OTHER_ERROR = 0x006;
    //支付参数异常
    public static final int PAY_PARAMETERS_ERROE = 0x007;


    private Alipay(Activity context) {
        mContext = context;
    }

    public static Alipay getInstance(Activity context){
        if (mAliPay == null) {
            synchronized(WeiXinPay.class){
                if (mAliPay == null) {
                    mAliPay = new Alipay(context);
                }
            }
        }
        return mAliPay;
    }


    public void startAliPay(final String orderInfo,JPayListener listener){
        mJPayListener = listener;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(mContext);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @SuppressWarnings("unchecked")
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);

            Log.e("aliPay call ",payResult.toString());

            String resultStatus = payResult.getResultStatus();
            if (mJPayListener ==null){
                return;
            }
            // https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.xN1NnL&treeId=204&articleId=105302&docType=1
            if(payResult == null) {
                mJPayListener.onPayError(RESULT_ERROR,"结果解析错误");
                return;
            }
            if(TextUtils.equals(resultStatus, "9000")) {
                //支付成功
                mJPayListener.onPaySuccess();
            } else if(TextUtils.equals(resultStatus, "8000")) {
                //正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                mJPayListener.onPayError(PAY_DEALING,"正在处理结果中");
            } else if(TextUtils.equals(resultStatus, "6001")) {
                //支付取消
                mJPayListener.onPayCancel();
            } else if(TextUtils.equals(resultStatus, "6002")) {
                //网络连接出错
                mJPayListener.onPayError(PAY_NETWORK_ERROR,"网络连接出错");
            } else if(TextUtils.equals(resultStatus, "4000")) {
                //支付错误
                mJPayListener.onPayError(PAY_ERROR,"订单支付失败");
            }else {
                mJPayListener.onPayError(PAY_OTHER_ERROR,resultStatus);
            }
        }
    };
}
