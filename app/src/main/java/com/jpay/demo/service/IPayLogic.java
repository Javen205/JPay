package com.jpay.demo.service;

import android.app.Activity;
import android.widget.Toast;

import com.jpay.JPay;
import com.jpay.demo.entity.Order;
import com.jpay.demo.utils.Constants;
import com.jpay.demo.utils.HttpKit;

import java.util.HashMap;
import java.util.Map;


public class IPayLogic {
    private static IPayLogic mIPayLogic;
    private Activity mContext;

    private IPayLogic(Activity context) {
        mContext = context;
    }

    public static IPayLogic getIntance(Activity context) {
        if (mIPayLogic == null) {
            synchronized (IPayLogic.class) {
                if (mIPayLogic == null) {
                    mIPayLogic = new IPayLogic(context);
                }
            }
        }
        return mIPayLogic;
    }

    /**
     * 获取预付订单
     *
     * @param order
     * @return
     */
    public String WXPay(Order order) {
        String body = order.getBody();
        String attach = order.getAttach();
        int total_fee = order.getTotalFee();
        String notify_url = order.getNofityUrl();
        String device_info = order.getDeviceInfo();


        Map<String, String> queryParas = new HashMap<String, String>();
        queryParas.put("body", body);
        queryParas.put("attach", attach);
        queryParas.put("total_fee", total_fee * 100 + "");
        queryParas.put("notify_url", notify_url);
        queryParas.put("device_info", device_info);

        String result = HttpKit.get(Constants.WXPAY_URL, queryParas);
        return result;
    }

    /**
     * 获取支付宝App支付订单信息
     *
     * @return
     */
    public String getAliPayOrderInfo(Order order) {
        String result = HttpKit.get(Constants.ALIPAY_URL);
        return result;
    }

    /**
     * 获取银联支付的tn信息
     * @param order
     * @return
     */
    public String getUPPayOrderInfo(Order order) {
        String result = HttpKit.get(Constants.UPPAY_URL);
        return result;
    }


    public void startAliPay(final String orderInfo) {
        JPay.getIntance(mContext).toPay(JPay.PayMode.ALIPAY, orderInfo, new JPay.JPayListener() {
            @Override
            public void onPaySuccess() {
                Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayError(int error_code, String message) {
                Toast.makeText(mContext, "支付失败>" + error_code + " " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayCancel() {
                Toast.makeText(mContext, "取消了支付", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUUPay(String dataOrg, String sign, String mode) {

            }
        });
    }


    /**
     * 调起支付
     *
     * @param appId
     * @param partnerId
     * @param prepayId
     * @param nonceStr
     * @param timeStamp
     * @param sign
     */
    public void startWXPay(String appId, String partnerId, String prepayId,
                           String nonceStr, String timeStamp, String sign) {

        JPay.getIntance(mContext).toWxPay(appId, partnerId, prepayId, nonceStr, timeStamp, sign, new JPay.JPayListener() {
            @Override
            public void onPaySuccess() {
                Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayError(int error_code, String message) {
                Toast.makeText(mContext, "支付失败>" + error_code + " " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayCancel() {
                Toast.makeText(mContext, "取消了支付", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUUPay(String dataOrg, String sign, String mode) {

            }
        });
    }

    public void startUPPay(String tn) {

        JPay.getIntance(mContext).toUUPay("01",tn, new JPay.JPayListener() {
            @Override
            public void onPaySuccess() {
                Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayError(int error_code, String message) {
                Toast.makeText(mContext, "支付失败>" + error_code + " " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayCancel() {
                Toast.makeText(mContext, "取消了支付", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUUPay(String dataOrg, String sign, String mode) {
                Toast.makeText(mContext, "支付成功>需要后台查询订单确认>"+dataOrg+" "+mode, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
