/**
 * Copyright (c) 2015-2017, javen205  (javendev@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.jpay.weixin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.jpay.JPay.JPayListener;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

/**
 * 微信支付
 * Created by Javen on 2016/11/20.
 */

public class WeiXinPay {
    private static WeiXinPay mWeiXinPay;
    private Context mContext;
    private IWXAPI mIWXAPI;
    private JPayListener mJPayListener;

    //未安装微信或微信版本过低
    public static final int WEIXIN_VERSION_LOW = 0x001;
    //支付参数异常
    public static final int PAY_PARAMETERS_ERROE = 0x002;
    //支付失败
    public static final int PAY_ERROR= 0x003;

    private WeiXinPay(Activity context) {
        mContext = context;
    }

    public static WeiXinPay getInstance(Activity context){
        if (mWeiXinPay == null) {
            synchronized(WeiXinPay.class){
                if (mWeiXinPay == null) {
                    mWeiXinPay = new WeiXinPay(context);
                }
            }
        }
        return mWeiXinPay;
    }

    /**
     * 初始化微信支付接口
     * @param appId
     */
    public void init(String appId){
        mIWXAPI= WXAPIFactory.createWXAPI(mContext, null);
        mIWXAPI.registerApp(appId);
    }

    /**
     * 获取微信接口
     * @return
     */
    public IWXAPI getWXApi() {
        return mIWXAPI;
    }

    /**
     * 调起支付
     * @param appId
     * @param partnerId
     * @param prepayId
     * @param nonceStr
     * @param timeStamp
     * @param sign
     */
    public void startWXPay(String appId,String partnerId,String prepayId,
                           String nonceStr,String timeStamp,String sign,JPayListener listener){
        mJPayListener = listener;
        init(appId);

        if (!checkWx()) {
            if(listener != null) {
                listener.onPayError(WEIXIN_VERSION_LOW,"未安装微信或者微信版本过低");
            }
            return;
        }



        PayReq request = new PayReq();
        request.appId = appId;
        request.partnerId = partnerId;
        request.prepayId= prepayId;
        request.packageValue = "Sign=WXPay";
        request.nonceStr=nonceStr;
        request.timeStamp= timeStamp;
        request.sign= sign;
        mIWXAPI.sendReq(request);
    }


    /**
     * 响应支付回调
     * @param error_code
     * @param message
     */
    public void onResp(int error_code,String message) {
        if(mJPayListener == null) {
            return;
        }
        if(error_code == 0) {
            //支付成功
            mJPayListener.onPaySuccess();
        } else if(error_code == -1) {
            //支付异常
            mJPayListener.onPayError(PAY_ERROR,message);
        } else if(error_code == -2) {
            //支付取消
            mJPayListener.onPayCancel();
        }

        mJPayListener = null;
    }



    //检测微信客户端是否支持微信支付
    private boolean checkWx() {
        return isWeixinAvilible() && mIWXAPI.isWXAppInstalled() && mIWXAPI.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }
    /**
     * 判断微信是否安装
     * @return
     */
    private  boolean isWeixinAvilible() {
        return appIsAvilible("com.tencent.mm");
    }

    /**
     * 判断app是否安装
     * @param packageName
     * @return
     */
    private  boolean appIsAvilible(String packageName) {
        final PackageManager packageManager = mContext.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
