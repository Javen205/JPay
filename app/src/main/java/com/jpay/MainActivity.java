package com.jpay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.jpay.asyncTask.AliPayTask;
import com.jpay.asyncTask.WXPayTask;
import com.jpay.entity.Order;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);
        //支付宝APP支付可以使用沙箱环境测试 https://auth.alipay.com/login/ant_sso_index.htm?goto=https%3A%2F%2Fopenhome.alipay.com%2Fplatform%2FappDaily.htm%3Ftab%3Dtool
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
}
