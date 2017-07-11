package com.jpay.network.asyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jpay.entity.Order;
import com.jpay.service.IPayLogic;

import org.json.JSONObject;


public class AliPay extends AsyncTask<Object, Integer, String> {
	private Activity mContext;
	public AliPay(Activity context) {
		this.mContext = context;
	}
	
	@Override
	protected String doInBackground(Object... params) {
		return  IPayLogic.getIntance(mContext).getAliPayOrderInfo((Order)params[0]);
	}
	
	@Override
	protected void onPostExecute(String result) {
		try {
			if (result!=null) {
				System.out.println("AliPay result>"+result);
				JSONObject data = new JSONObject(result);
				String message = data.getString("message");
				int code = data.getInt("code");

				if(code == 0){
					String orderInfo = data.getString("data");

					Toast.makeText(mContext, "正在调起支付", Toast.LENGTH_SHORT).show();
					IPayLogic.getIntance(mContext).startAliPay(orderInfo);

				}else{

		        	Log.d("PAY_GET", "返回错误"+message);
//		        	Toast.makeText(mContext, "返回错误:"+message, Toast.LENGTH_SHORT).show();
				}
			}else {
				System.out.println("get  AliPay exception, is null");
			}
		} catch (Exception e) {
			Log.e("PAY_GET", "异常："+e.getMessage());
        	Toast.makeText(mContext, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
		super.onPostExecute(result);
	}

}
