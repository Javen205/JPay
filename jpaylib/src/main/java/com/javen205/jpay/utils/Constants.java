package com.javen205.jpay.utils;


import com.javen205.jpay.IPay;

public class Constants {
	public static String APP_ID="";//不能删除 回调的WXPayEntryActivity中需要使用
	public static IPay.IPayListener payListener;


	//服务端获取预付订单的接口访问地址
	public static final String WXPAY_URL="http://192.168.111.194:8080/pay/appPay";
	public static final String ALIPAY_URL="http://192.168.111.194:8080/alipay/appPay";
	public static final String ALIWAPPAY_URL="http://192.168.111.194:8080/alipay/wapPay";
}
