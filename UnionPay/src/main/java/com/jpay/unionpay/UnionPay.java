package com.jpay.unionpay;

import android.content.Context;
import android.content.Intent;

import com.unionpay.UPPayAssistEx;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 支付步骤说明：
 * 步骤1：从网络获取交易流水号即TN
 * 步骤2：通过银联工具类启动支付插件
 * 步骤3：处理银联手机支付控件返回的支付结果
 */
public class UnionPay {
    private static UnionPay mUnionPay;
    private Context mContext;
    private String mMode;
    private JPay.UnionPayListener mJPayListener;

    //支付失败
    public static final int PAY_ERROR = 0x001;
    //回调参数异常
    public static final int PAY_CALLBACK_ERROR = 0x002;
    //支付参数异常
    public static final int PAY_PARAMETERS_ERROE = 0x003;

    private UnionPay(Context context) {
        mContext = context;
    }

    public static UnionPay getInstance(Context context) {
        if (mUnionPay == null) {
            synchronized (UnionPay.class) {
                if (mUnionPay == null) {
                    mUnionPay = new UnionPay(context.getApplicationContext());
                }
            }
        }
        return mUnionPay;
    }

    /**
     * 启动支付
     * @param mode     "00" - 启动银联正式环境 "01" - 连接银联测试环境
     * @param tn       交易流水号 tn
     * @param listener 支付结果回调
     */
    public void startUnionPay(String mode, String tn, JPay.UnionPayListener listener) {
        this.mJPayListener = listener;
        this.mMode = mode;
        doStartUnionPayPlugin(mContext, tn, mode);
    }

    /**
     * 通过银联工具类启动支付插件
     *
     * @param context 应用上下文
     * @param tn      交易流水号 tn
     * @param mode    "00" - 启动银联正式环境 "01" - 连接银联测试环境
     */
    public void doStartUnionPayPlugin(Context context, String tn, String mode) {
        UPPayAssistEx.startPay(context, null, null, tn, mode);
    }

    /**
     * 处理支付结果回调
     *
     * @param data 支付回调的数据
     */
    public void onUnionPayResult(Intent data) throws JSONException {
        if (data == null) {
            mJPayListener.onPayError(PAY_CALLBACK_ERROR, "callback error,data is null");
            return;
        }
        //支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
        String str = data.getExtras().getString("pay_result");
        if (str.isEmpty()) {
            mJPayListener.onPayError(PAY_CALLBACK_ERROR, "pay_result is null");
            return;
        }
        if (str.equalsIgnoreCase("success")) {
            // 如果想对结果数据验签，可使用下面这段代码，但建议不验签，直接去商户后台查询交易结果
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                JSONObject resultJson = new JSONObject(result);
                String sign = resultJson.getString("sign");
                String dataOrg = resultJson.getString("data");
                // 去商户后台做验签以及查询订单信息
                mJPayListener.onUnionPay(dataOrg, sign, mMode);
            }
        } else if (str.equalsIgnoreCase("fail")) {
            mJPayListener.onPayError(PAY_ERROR, "支付失败");
        } else if (str.equalsIgnoreCase("cancel")) {
            mJPayListener.onPayCancel();
        }
    }
}
