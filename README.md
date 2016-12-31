# JPay
微信支付APP支付、支付宝APP支付

> Android微信支付


详细的介绍 参考[Android版-微信APP支付](http://www.jianshu.com/p/febf7c2eea82)

### 客户端使用说明

 1. 修改依赖库`jpaylib`中包名为`com.javen205.jpay.utils` 下的常量类`Constants`的`TESTPAY_URL` 测试中默认的访问路径为
`http://[域名或者IP]:[端口号]/pay/appPay`
 2. 将`demo`的`xxx.wxapi` 改为你申请应用的包名 比如应用的包名为`javen.com`那么就修改为`javen.com.wxapi`
 3. 将`AndroidManifest.xml` 的包名修改为申请应用的包名
 4. 将应用中的`build.gradle`的 `applicationId`修改为申请应用的包名
 5. 测试的时候修改默认的签名key
> 将key复制到app中并修改`buildTypes` 配置如下
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

### 服务端使用说明

1. 开源项目地址[weixin_guide](http://git.oschina.net/javen205/weixin_guide)
2. 开源项目如何下载、如何导入到IDE 参考之前写的文章[微信公众号之项目导入](http://www.jianshu.com/p/ab209e163614)
3. 服务端具体实现在`com.javen.weixin.controller.WeixinPayController.java` 类中的`appPay()`