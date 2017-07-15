package com.jpay.demo.asyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jpay.demo.entity.Order;
import com.jpay.demo.service.IPayLogic;

import org.json.JSONObject;

public class WXPayTask extends AsyncTask<Object, Integer, String> {
	private Activity mContext;
	public WXPayTask(Activity context) {
		this.mContext = context;
	}
	
	@Override
	protected String doInBackground(Object... params) {
		try{
			return  IPayLogic.getIntance(mContext).WXPay((Order)params[0]);
		}catch (Exception e){
		}
		return null;

	}
	
	@Override
	protected void onPostExecute(String result) {
		try {
			if (result!=null) {
				System.out.println("TestPayPrepay result>"+result);
				JSONObject data = new JSONObject(result);
				if(!data.has("code")){
					String sign = data.getString("sign");
					String timestamp = data.getString("timestamp");
					String noncestr = data.getString("noncestr");
					String partnerid = data.getString("partnerid");
					String prepayid = data.getString("prepayid");
					String appid = data.getString("appid");
					Toast.makeText(mContext, "正在调起支付", Toast.LENGTH_SHORT).show();

					IPayLogic.getIntance(mContext).startWXPay(appid, partnerid, prepayid, noncestr, timestamp, sign);
				}else{
					String message = data.getString("message");
		        	Log.d("PAY_GET", "返回错误"+message);
		        	Toast.makeText(mContext, "返回错误:"+message, Toast.LENGTH_SHORT).show();
				}
			}else {
				System.out.println("get  prepayid exception, is null");
			}
		} catch (Exception e) {
			Log.e("PAY_GET", "异常："+e.getMessage());
        	Toast.makeText(mContext, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
		super.onPostExecute(result);
	}

}
