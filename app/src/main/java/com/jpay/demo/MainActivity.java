package com.jpay.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.jpay.demo.asyncTask.AliPayTask;
import com.jpay.demo.asyncTask.UPPayTask;
import com.jpay.demo.asyncTask.WXPayTask;
import com.jpay.demo.entity.Order;
import com.jpay.unionpay.UPPay;

import org.json.JSONException;

import mayihuijia.com.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);
        //支付宝APP支付可以使用沙箱环境测试 https://openhome.alipay.com/platform/appDaily.htm?tab=tool
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
    }

    public void testWxPay(View view){
        Toast.makeText(this, "测试", Toast.LENGTH_SHORT).show();

        Order order = new Order();
        order.setBody("会员充值中心");
        order.setParaTradeNo(System.currentTimeMillis()+"");
        order.setTotalFee(20);
        order.setAttach("json");//附加参数
        order.setNofityUrl("http://www.baidu.com");//支付成功服务端回调通知的地址
        order.setDeviceInfo("");

        new WXPayTask(this).execute(order);
    }

    public void testAliPay(View view){
        Toast.makeText(this, "支付宝测试", Toast.LENGTH_SHORT).show();

        Order order = new Order();
        order.setBody("会员充值中心");
        order.setParaTradeNo(System.currentTimeMillis()+"");
        order.setTotalFee(20);
        order.setAttach("json");//附加参数
        order.setNofityUrl("http://www.xxxx.com");//支付成功服务端回调通知的地址

        new AliPayTask(this).execute(order);
    }

    public void testUPPPay(View view){
        Toast.makeText(this, "银联测试", Toast.LENGTH_SHORT).show();

        Order order = new Order();
        order.setBody("会员充值中心");
        order.setParaTradeNo(System.currentTimeMillis()+"");
        order.setTotalFee(20);
        order.setAttach("json");//附加参数
        order.setNofityUrl("http://www.xxxx.com");//支付成功服务端回调通知的地址

        new UPPayTask(this).execute(order);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            UPPay.getInstance(this).onUUPayResult(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
