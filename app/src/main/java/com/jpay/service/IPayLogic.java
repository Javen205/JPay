package com.jpay.service;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.jpay.Constants;
import com.jpay.alipay.JPay;
import com.jpay.entity.Order;
import com.jpay.kits.HttpKit;

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

        String result = HttpKit.get(Constants.WX_PAY_URL, queryParas);
        return result;
    }

    /**
     * 获取支付宝App支付订单信息
     *
     * @return
     */
    public String getAliPayOrderInfo(Order order) {
        String result = HttpKit.get(Constants.ALI_PAY_URL);
        return result;
    }

    /**
     * 获取银联支付的tn信息
     *
     * @param order
     * @return
     */
    public String getUnionPayOrderInfo(Order order) {
        return HttpKit.get(Constants.UNION_PAY_URL);
    }

    public String getJDOrderInfo() {
        return HttpKit.get(Constants.JD_PAY_URL);
    }


    public void startAliPay(final String orderInfo) {
        JPay.getIntance(mContext).toAliPay(orderInfo, new JPay.AliPayListener() {
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

        com.jpay.wxpay.JPay.getIntance(mContext).toWxPay(appId, partnerId, prepayId, nonceStr, timeStamp, sign, new com.jpay.wxpay.JPay.WxPayListener() {
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
        });
    }

    public void startUnionPay(String tn) {
        com.jpay.unionpay.JPay.getIntance(mContext).toUnionPay("01", tn, new com.jpay.unionpay.JPay.UnionPayListener() {
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
            public void onUnionPay(String dataOrg, String sign, String mode) {
                Log.d("onUnionPay", "支付成功>需要后台查询订单确认>dataOrg" + dataOrg + " sign>" + sign + " mode>" + mode);
                Toast.makeText(mContext, "支付成功>需要后台查询订单确认>" + dataOrg + " " + mode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void JDAuthor(String orderId, String merchant,String appId,String signData,String extraInfo){
        com.jpay.jdpay.JDPay.getInstance(mContext).author(orderId,merchant,appId,signData,extraInfo, new com.jpay.jdpay.JPay.JDPayListener(){

            @Override
            public void onPaySuccess() {
                Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayError(String error_code, String message) {
                Toast.makeText(mContext, "支付失败>" + error_code + " " + message, Toast.LENGTH_SHORT).show();
                Log.d("onJDPay","支付失败，错误码>" + error_code);
            }

            @Override
            public void onPayCancel() {
                Toast.makeText(mContext, "取消了支付", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onJdPay(String dataOrg) {
                Log.d("onJDPay", "支付结果 dataOrg>" + dataOrg);
            }
        });
    }
}
