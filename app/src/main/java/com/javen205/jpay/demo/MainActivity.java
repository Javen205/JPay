package com.javen205.jpay.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.javen205.jpay.demo.asyncTask.AliPayTask;
import com.javen205.jpay.demo.asyncTask.WXPayTask;
import com.javen205.jpay.demo.entity.Order;

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


}
