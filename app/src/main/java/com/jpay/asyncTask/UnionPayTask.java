package com.jpay.asyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jpay.entity.Order;
import com.jpay.service.IPayLogic;

import org.json.JSONObject;

public class UnionPayTask extends AsyncTask<Object, Integer, String> {
	private Activity mContext;
	public UnionPayTask(Activity context) {
		this.mContext = context;
	}
	
	@Override
	protected String doInBackground(Object... params) {
		try{
			return  IPayLogic.getIntance(mContext).getUnionPayOrderInfo((Order)params[0]);
		}catch (Exception e){
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		try {
			if (result!=null) {
				System.out.println("UnionPay result>"+result);
				JSONObject data = new JSONObject(result);
				String message = data.getString("message");
				int code = data.getInt("code");

				if(code == 0){
					String orderInfo = data.getString("data");
                    System.out.println("获取到的tn>>>"+orderInfo);
                    Toast.makeText(mContext, "正在调起支付", Toast.LENGTH_SHORT).show();
					IPayLogic.getIntance(mContext).startUnionPay(orderInfo);
				}else{
		        	Log.d("PAY_GET", "返回错误"+message);
//		        	Toast.makeText(mContext, "返回错误:"+message, Toast.LENGTH_SHORT).show();
				}
			}else {
				System.out.println("get  UnionPay exception, is null");
			}
		} catch (Exception e) {
			Log.e("PAY_GET", "异常："+e.getMessage());
        	Toast.makeText(mContext, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
		super.onPostExecute(result);
	}

}