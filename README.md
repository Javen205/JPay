

# JPay

对微信App支付、支付宝App支付的二次封装,对外提供一个相对简单的接口以及支付结果的回调


[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/javendev/maven/JPay/images/download.svg)](https://bintray.com/javendev/maven/JPay/_latestVersion)
[![QQ](http://pub.idqqimg.com/wpa/images/group.png)](https://jq.qq.com/?_wv=1027&k=47koFFR)


GitHub:https://github.com/Javen205/JPay
oschina:http://git.oschina.net/javen205/JPay

[版本更新记录](https://github.com/Javen205/JPay/wiki/%E7%89%88%E6%9C%AC%E6%9B%B4%E6%96%B0%E8%AE%B0%E5%BD%95)


**使用方法**

### 1、引入

```
compile 'com.javen205.jpay:jpaysdk:latest.release.here'
```

### 2. Android Manifest配置

##### 2.1权限声明

```
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

##### 2.2注册activity

`application`节点添加如下类容
```
 <!-- 微信支付 -->
        <activity
            android:name="com.javen205.jpay.weixin.WXPayEntryActivity"
            android:configChanges="orientation|keyboardHidden|navigation|screenSize"
            android:launchMode="singleTop"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />
        <activity-alias
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:targetActivity="com.javen205.jpay.weixin.WXPayEntryActivity" />
        <!-- 微信支付 end -->


        <!-- alipay sdk begin -->

        <activity
            android:name="com.alipay.sdk.app.H5PayActivity"
            android:configChanges="orientation|keyboardHidden|navigation|screenSize"
            android:exported="false"
            android:screenOrientation="behind"
            android:windowSoftInputMode="adjustResize|stateHidden" >
        </activity>
        <activity
            android:name="com.alipay.sdk.app.H5AuthActivity"
            android:configChanges="orientation|keyboardHidden|navigation"
            android:exported="false"
            android:screenOrientation="behind"
            android:windowSoftInputMode="adjustResize|stateHidden" >
        </activity>

        <!-- alipay sdk end -->
```

### 3. 发起支付

##### 3.1 微信支付


```
JPay.getIntance(mContext).toPay(JPay.PayMode.WXPAY, payParameters, new JPay.JPayListener() {
			@Override
			public void onPaySuccess() {
				Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show()
			}

			@Override
			public void onPayError(int error_code, String message) {
				Toast.makeText(mContext, "支付失败>"+error_code+" "+ message, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPayCancel() {
				Toast.makeText(mContext, "取消了支付", Toast.LENGTH_SHORT).show();
			}
		});
```
`payParameters` 为JSON字符串格式如下：
```
{
  "appId": "",
  "partnerId": "",
  "prepayId": "",
  "sign": "",
  "nonceStr" : "",
  "timeStamp": ""
}
```

或者

```
JPay.getIntance(mContext).toWxPay(appId, partnerId, prepayId, nonceStr, timeStamp, sign, new JPay.JPayListener() {
			@Override
			public void onPaySuccess() {
				Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPayError(int error_code, String message) {
				Toast.makeText(mContext, "支付失败>"+error_code+" "+ message, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPayCancel() {
				Toast.makeText(mContext, "取消了支付", Toast.LENGTH_SHORT).show();
			}
		});
```
##### 3.2 支付宝支付

```
JPay.getIntance(mContext).toPay(JPay.PayMode.ALIPAY, orderInfo, new JPay.JPayListener() {
			@Override
			public void onPaySuccess() {
				Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPayError(int error_code, String message) {
				Toast.makeText(mContext, "支付失败>"+error_code+" "+ message, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPayCancel() {
				Toast.makeText(mContext, "取消了支付", Toast.LENGTH_SHORT).show();
			}
		});
```

或者

```
Alipay.getInstance(mContext).startAliPay(orderInfo, new JPay.JPayListener() {
			@Override
			public void onPaySuccess() {

			}

			@Override
			public void onPayError(int error_code, String message) {

			}

			@Override
			public void onPayCancel() {

			}
		});
```

### 4.案例的使用


> appId以及相关的key我们都从服务端获取

#### 4.1 客户端使用说明
 1. 将`AndroidManifest.xml` 的包名修改为申请应用的包名
 2. 将应用中的`build.gradle`的 `applicationId`修改为申请应用的包名
 3. 测试的时候修改默认的签名key

> 将key复制到项目的根目录(app)中并修改`buildTypes` 配置如下

```
 signingConfigs {
        release {
            storeFile file("wxkey")
            storePassword '123456'
            keyAlias '1'
            keyPassword '123456'
        }
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
        debug {
            signingConfig signingConfigs.release
        }
    }
```



#### 4.2 服务端使用说明

1. 开源项目地址[weixin_guide](http://git.oschina.net/javen205/weixin_guide)
2. 开源项目如何下载、如何导入到IDE 参考之前写的文章[微信公众号之项目导入](http://blog.csdn.net/zyw_java/article/details/61415051)
3. 微信支付服务端具体实现在`com.javen.weixin.controller.WeixinPayController.java` 类中的`appPay()`
4. 支付宝支付服务端具体实现在`com.javen.alipay.AliPayController.java` 类中的`appPay()`


#### 4.3 参考资料

微信、支付宝APP支付详细介绍参考资料 [博客地址](http://blog.csdn.net/zyw_java)

[10分钟搭建属于自己的ngork服务器，实现内网穿透](http://blog.csdn.net/zyw_java/article/details/70341106)
[Android版-微信APP支付](http://blog.csdn.net/zyw_java/article/details/54024232)

[Android版-支付宝APP支付](http://blog.csdn.net/zyw_java/article/details/54024238)

[支付宝Wap支付你了解多少？](http://blog.csdn.net/zyw_java/article/details/54024253)

**安利**

[微信公众号开发：订阅号、服务号](http://git.oschina.net/javen205/weixin_guide)

[AndroidStudio多渠道打包](http://blog.csdn.net/zyw_java/article/details/61420290)

[Android依赖管理与私服搭建](http://blog.csdn.net/zyw_java/article/details/60336030)

[Android Studio 上传aar(Library)到JCenter](http://blog.csdn.net/zyw_java/article/details/60336189)
