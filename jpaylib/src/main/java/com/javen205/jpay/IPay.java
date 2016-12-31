package com.javen205.jpay;

import android.app.Activity;
import android.widget.Toast;

import com.javen205.jpay.asyncTask.AliPay;
import com.javen205.jpay.asyncTask.WXPayPrepay;
import com.javen205.jpay.entity.Order;
import com.javen205.jpay.service.IPayLogic;
import com.javen205.jpay.utils.Constants;


public class IPay {
	private static  IPay mIPay;
	private Activity mContext;

	private IPay(Activity context) {
		mContext = context;
	}



	public static IPay getIntance(Activity context){
		if (mIPay == null) {
			synchronized(IPay.class){
				if (mIPay == null) {
					mIPay = new IPay(context);
				}
			}
		}
		return mIPay;
	}

	public interface IPayListener{
		void onPay(int wxcode, String alicode, String message);
	}

	public enum PayMode{
		WXPAY,ALIPAY,ALIWAPPAY
	}

	public void toPay(PayMode payMode, Order order, IPayListener listener){
		if (payMode.name().equalsIgnoreCase(PayMode.WXPAY.name())) {
			toWxPay(order, listener);
		}else if (payMode.name().equalsIgnoreCase(PayMode.ALIPAY.name())) {
			toAliPay(order, listener);
		}else if (payMode.name().equalsIgnoreCase(PayMode.ALIWAPPAY.name())) {
			toAliWapPay(order, listener);
		}
	}


	public void toWxPay(Order order,IPayListener listener){
		if (order != null) {
			if (IPayLogic.getIntance(mContext).isWeixinAvilible()) {
				Constants.payListener = listener;
				new WXPayPrepay(mContext).execute(order);
			}else {
				Toast.makeText(mContext, "未安装微信", Toast.LENGTH_LONG).show();
			}
		}else {
			Toast.makeText(mContext, "参数异常 order is null", Toast.LENGTH_LONG).show();
		}
	}

	public void toAliPay(Order order,IPayListener listener){
		if (order != null) {
			Constants.payListener = listener;
			new AliPay(mContext).execute(order);
		}else {
			Toast.makeText(mContext, "参数异常 order is null", Toast.LENGTH_LONG).show();
		}
	}


	public void toAliWapPay(Order order,IPayListener listener){
		if (order != null) {
			Constants.payListener = listener;
			IPayLogic.getIntance(mContext).getAliWapPayUrl(order);

		}else {
			Toast.makeText(mContext, "参数异常 order is null", Toast.LENGTH_LONG).show();
		}
	}
}
