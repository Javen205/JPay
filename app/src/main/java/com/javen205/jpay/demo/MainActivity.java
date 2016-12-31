package com.javen205.jpay.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.javen205.jpay.IPay;
import com.javen205.jpay.entity.Order;

import xxxxx.com.R;


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
        IPay.getIntance(this).toPay(IPay.PayMode.WXPAY,order,new IPay.IPayListener() {


            @Override
            public void onPay(int wxcode, String alicode, String message) {
                System.out.println("回调过来的状态》"+wxcode+" message>"+((message!=null && message.isEmpty())?"":message));
                if (wxcode == 0){
                    //显示充值成功的页面和需要的操作
                }
                if (wxcode == -1){
                    //错误
                }
                if (wxcode == -2){
                    //用户取消
                }
                Toast.makeText(MainActivity.this, "回调过来的状态》" + wxcode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void testAliPay(View view){
        Toast.makeText(this, "支付宝测试", Toast.LENGTH_SHORT).show();

        Order order = new Order();
        order.setBody("会员充值中心");
        order.setParaTradeNo(System.currentTimeMillis()+"");
        order.setTotalFee(20);
        order.setAttach("json");//附加参数
        order.setNofityUrl("http://www.xxxx.com");//支付成功服务端回调通知的地址

        IPay.getIntance(MainActivity.this).toPay(IPay.PayMode.ALIPAY,order,new IPay.IPayListener() {

            @Override
            public void onPay(int wxcode, String alicode, String message) {
                System.out.println("回调过来的状态》"+alicode+" message>"+((message!=null && message.isEmpty())?"":message));
                Toast.makeText(MainActivity.this, "回调过来的状态》" + alicode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void testAliWapPay(View view){
//        WebViewActivity.jumpTo(this,true,"http://www.baidu.com");
//        WebViewActivity.jumpTo(this,false,"文本测试");
        Toast.makeText(this, "支付宝Wap测试", Toast.LENGTH_SHORT).show();

        Order order = new Order();
        order.setBody("会员充值中心");
        order.setSubject("会员充值中心");
        order.setParaTradeNo(System.currentTimeMillis()+"");
        order.setTotalFee(20);
        order.setAttach("json");//附加参数
        order.setNofityUrl("http://www.xxxx.com");//支付成功服务端回调通知的地址

        IPay.getIntance(MainActivity.this).toPay(IPay.PayMode.ALIWAPPAY,order,new IPay.IPayListener() {

            @Override
            public void onPay(int wxcode, String alicode, String message) {
                System.out.println("回调过来的状态》"+alicode+" message>"+((message!=null && message.isEmpty())?"":message));
                Toast.makeText(MainActivity.this, "回调过来的状态》" + alicode, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
