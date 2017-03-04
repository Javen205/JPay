package com.javen205.jpay.weixin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * Created by Javen on 2016/11/20.
 */

public class WxPayCallBackActivity extends Activity implements IWXAPIEventHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        LinearLayout ll = new LinearLayout(this);
        setContentView(ll);

        if(WeiXinPay.getInstance(this)!=null){
            WeiXinPay.getInstance(this).getWXApi().handleIntent(getIntent(), this);
        }else {
            finish();
        }

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if(WeiXinPay.getInstance(this) != null) {
            WeiXinPay.getInstance(this).getWXApi().handleIntent(intent, this);
        }
    }
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        //4、支付结果回调 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5
        if(baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if(WeiXinPay.getInstance(this) != null) {
                if(baseResp.errStr != null) {
                    Log.e("weiXinPay", "errStr=" + baseResp.errStr);
                }
                WeiXinPay.getInstance(this).onResp(baseResp.errCode,baseResp.errStr);
                finish();
            }
        }
    }
}
