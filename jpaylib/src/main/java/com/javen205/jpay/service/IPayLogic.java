package com.javen205.jpay.service;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.javen205.jpay.entity.Order;
import com.javen205.jpay.ui.WebViewActivity;
import com.javen205.jpay.utils.Constants;
import com.javen205.jpay.utils.HttpKit;
import com.javen205.jpay.utils.PayResult;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IPayLogic {
	private static  IPayLogic mIPayLogic;
	private Activity mContext;

	private IPayLogic(Activity context) {
		mContext = context;
	}

	public static IPayLogic getIntance(Activity context){
		if (mIPayLogic == null) {
			synchronized(IPayLogic.class){
				if (mIPayLogic == null) {
					mIPayLogic = new IPayLogic(context);
				}
			}
		}
		return mIPayLogic;
	}

	/**
	 * 获取预付订单
	 * @param order
	 * @return
	 */
	public String WXPay(Order order){
		String body = order.getBody();
		String attach = order.getAttach();
		int total_fee = order.getTotalFee();
		String notify_url = order.getNofityUrl();
		String device_info = order.getDeviceInfo();


		Map<String, String> queryParas = new HashMap<String,String>();
		queryParas.put("body", body);
		queryParas.put("attach", attach);
		queryParas.put("total_fee", total_fee*100+"");
		queryParas.put("notify_url", notify_url);
		queryParas.put("device_info", device_info);

		String result= HttpKit.get(Constants.WXPAY_URL, queryParas);
		return result;
	}

	/**
	 * 获取支付宝App支付订单信息
	 * @return
	 */
	public String getAliPayOrderInfo(Order order){
		String result=HttpKit.get(Constants.ALIPAY_URL);
		return result;
	}

	/**
	 * 获取支付宝Wap支付
	 * @return
	 */
	public void getAliWapPayUrl(Order order){
		StringBuffer sbf=new StringBuffer();
		sbf.append(Constants.ALIWAPPAY_URL)
				.append("?body=").append(order.getBody())
				.append("&subject=").append(order.getSubject())
				.append("&total_amount=").append(order.getTotalFee())
				.append("&passback_params=").append(order.getAttach());

//		startAliWapPay(sbf.toString());
		WebViewActivity.jumpTo(mContext,true,sbf.toString());
	}


	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			PayResult payResult = new PayResult((Map<String, String>) msg.obj);
			System.out.println("alipay call "+payResult.toString());
			String resultStatus = payResult.getResultStatus();
			String memo = payResult.getMemo();
			if (Constants.payListener !=null){
				//返回状态以及详细的描述参考
				// https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.xN1NnL&treeId=204&articleId=105302&docType=1
				Constants.payListener.onPay(-2,resultStatus,memo);
			}
		}
	};

	public void startAliPay(final String orderInfo){
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

	public void startAliWapPay(String from){
//		WebViewActivity.jumpTo(mContext,false,from);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(from));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
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
						   String nonceStr,String timeStamp,String sign){

		IWXAPI api= WXAPIFactory.createWXAPI(mContext, null);
		api.registerApp(appId);

		boolean isPaySupported = api.getWXAppSupportAPI() >= com.tencent.mm.sdk.constants.Build.PAY_SUPPORTED_SDK_INT;
		if (!isPaySupported) {
			Toast.makeText(mContext, "请更新微信客户端", Toast.LENGTH_SHORT).show();
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
		api.sendReq(request);
	}


	/**
	 * 判断微信是否安装
	 * @return
	 */
	public  boolean isWeixinAvilible() {
		return appIsAvilible("com.tencent.mm");
	}

	/**
	 * 判断app是否安装
	 * @param packageName
	 * @return
	 */
	public  boolean appIsAvilible(String packageName) {
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
