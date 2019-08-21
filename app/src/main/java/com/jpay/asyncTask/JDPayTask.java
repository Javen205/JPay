package com.jpay.asyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jpay.service.IPayLogic;

import org.json.JSONObject;

public class JDPayTask extends AsyncTask<Object, Integer, String> {
	private Activity mContext;
	public JDPayTask(Activity context) {
		this.mContext = context;
	}
	
	@Override
	protected String doInBackground(Object... params) {
		try{
			return  IPayLogic.getIntance(mContext).getJDOrderInfo();
		}catch (Exception e){
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		try {
			if (result!=null) {
				System.out.println("JDPay result>"+result);
				JSONObject data = new JSONObject(result);
				String message = data.getString("message");
				int code = data.getInt("code");

				if(code == 0){
					String orderInfo = data.getString("data");
                    System.out.println("获取到的>>>"+orderInfo);
                    Toast.makeText(mContext, "正在调起支付", Toast.LENGTH_SHORT).show();
                    JSONObject obj = new JSONObject(orderInfo);
                    String appId = obj.optString("appId");
                    String merchant = obj.optString("merchant");
                    String orderId = obj.optString("orderId");
                    String signData = obj.optString("signData");
                    String extraInfo = obj.optString("extraInfo");
					System.out.println("appId>>>"+appId);
					System.out.println("merchant>>>"+merchant);
					System.out.println("orderId>>>"+orderId);
					System.out.println("signData>>>"+signData);
					System.out.println("extraInfo>>>"+extraInfo);
					IPayLogic.getIntance(mContext).JDAuthor(orderId,merchant,appId,signData,extraInfo);
				}else{
		        	Log.d("PAY_GET", "返回错误"+message);
//		        	Toast.makeText(mContext, "返回错误:"+message, Toast.LENGTH_SHORT).show();
				}
			}else {
				System.out.println("get JDPay exception, is null");
			}
		} catch (Exception e) {
			Log.e("PAY_GET", "异常："+e.getMessage());
        	Toast.makeText(mContext, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
		super.onPostExecute(result);
	}

}