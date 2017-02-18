package com.javen205.testlib;

import android.app.Activity;
import android.os.Bundle;

import com.javen205.jpay.IPay;
import com.javen205.jpay.entity.Order;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Order order = new Order();


        IPay.getIntance(this).toWxPay(order, new IPay.IPayListener() {
            @Override
            public void onPay(int i, String s, String s1) {

            }
        });
    }
}
