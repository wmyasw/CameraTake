# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# http client
-keep class org.apache.http.** {*; }
#gson
#如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
# 如果使用了Gson之类的工具要使被它解析的JavaBean类即实体类不被混淆。
-keep class com.matrix.app.entity.json.** { *; }
-keep class com.matrix.appsdk.network.model.** { *; }
#mangrovetreelibray
#######


#-keepclassmembers class * {
#    @com.j256.ormlite.field.DatabaseField*;
##    @com.j256.ormlite.table.DatabaseTable*;
#}

#OrmLite uses reflection
#第三方Tab
-keep class com.flyco.tablayout.** {
   *;
}
#--------------------------------------------#
#Warning:library class android.webkit.WebView depends on program class android.net.http.SslCertificate

-dontwarn android.net.http.**
-keep class android.net.http.** { *;}
#Warning:library class org.apache.http.conn.ssl.SSLSocketFactory depends on program class org.apache.http.conn.scheme.HostNameResolver
-dontwarn org.apache.http.**
-keep class org.apache.http.** { *;}

#-dontwarn  com.jdjt.mangrovetreelibray.**
-keep class com.android.dx.** { *;}
-keep interface com.android.dx.** { *;}
-keep class com.google.dexmaker.** { *;}
-keepnames class com.google.dexmaker$* {
    public <fields>;
    public <methods>;
}
-keepnames class com.android.dx$* {
    public <fields>;
    public <methods>;
}
#-keep class com.jdjt.mangrovetreelibray.ioc.** { *; }
#-keep class com.jdjt.mangrovetreelibray.utils.** { *; }
#-keep class com.jdjt.mangrovetreelibray.views.** { *; }
#-keep class  com.jdjt.mangrove.http.** {*;}
#-keepnames class com.jdjt.mangrove$* {
#    public <fields>;
#    public <methods>;
#}
#-keepclassmembers class * extends android.app.Application {
#    public void *();
#}
#-keepclassmembers class com.jdjt.mangrovetreelibray.ioc.net$IocHttp {
#    public <fields>;
#    public <methods>;
#}
#-keepclassmembers class com.jdjt.mangrovetreelibray.ioc.net.** {
#    public <fields>;
#    public <methods>;
#}

#-keep class **$$InView{*;}

# # -------------------------------------------
# #  ######## 内部类混淆配置  ##########
# # -------------------------------------------


-keep @com.jdjt.mangrovetreelibray.ioc.annotation.NotProguard class * {*;}
-keep public class * {
    @com.jdjt.mangrovetreelibray.ioc.annotation.NotProguard <fields>;
}
-keepclassmembers class * {
    @com.jdjt.mangrovetreelibray.ioc.annotation.NotProguard <methods>;
    public static <fields>;
#    public static class *;
}


#不混淆android-async-http(这里的与你用的httpClient框架决定)
-keep class com.loopj.android.http.**{*;}

 #不混淆org.apache.http.legacy.jar
 -dontwarn android.net.compatibility.**
 -dontwarn android.net.http.**
 -dontwarn com.android.internal.http.multipart.**
 -dontwarn org.apache.commons.**
 -dontwarn org.apache.http.**
 -keep class android.net.compatibility.**{*;}
 -keep class android.net.http.**{*;}
 -keep class com.android.internal.http.multipart.**{*;}
 -keep class org.apache.commons.**{*;}
 -keep class org.apache.http.**{*;}

#-keep class com.jeremyfeinstein.slidingmenu.lib.** { *; }
#-keep interface com.jeremyfeinstein.slidingmenu.lib.** { *; }
# 地图模块
-keep class com.fengmap.** { *; }
-keep class com.fengmap.android.**{*;}
#-keepclassmembers class com.fengmap.drpeng.** {
#     *;
#}
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
#SweetAlert 混淆
-keep class cn.pedant.SweetAlert.Rotate3dAnimation {
    public <init>(...);
 }

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }
 #*******************微信支付************
 #-libraryjars libs/libammsdk.jar
 #-keep class com.tencent.** { *;}
 #-keep interface cn.sharesdk.**{*;}
 #-keep class cn.sharesdk.**{*;}
 #-keep class com.mob.** {*;}

# -keep class cn.sharesdk.**{*;}
# -keep class com.sina.**{*;}
 #-keep class **.R$* {*;}
# -keep class **.R{*;}
# -keep class com.mob.**{*;}
# -dontwarn com.mob.**
# -dontwarn cn.sharesdk.**
# -dontwarn **.R$*

 #********************支付宝*************
 #-libraryjars libs/alipaySingle-20160825.jar
 #-dontwarn android.net.**
# -keep class android.net.SSLCertificateSocketFactory{*;}
# -keep class com.alipay.android.app.IAlixPay{*;}
# -keep class com.alipay.android.app.IAlixPay$Stub{*;}
 #-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
# -keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
 #-keep class com.alipay.sdk.app.PayTask{ public *;}
# -keep class com.alipay.sdk.app.AuthTask{ public *;}


#微信
#-keep class com.tencent.mm.sdk.** {*;}

# ********************Rxjava Retrofit Okhttp3 adapter之中的类*************
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-keep class okhttp3.Request.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Gson
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
#自己调用Gson的类
-keep class com.jdjt.mangrove.domain.** {*;}

#********************Picasso******************
-dontwarn com.squareup.okhttp.**

#retrofit
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# webview + js,保留跟 javascript相关的属性
-keepattributes *JavascriptInterface*
# keep 使用 webview 的类
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
    public void *(android.webkit.webView, jav.lang.String);
}

#保留JavascriptInterface中的方法
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#okio
-dontwarn okio.**

-keep class okio.**{*;}
#okgo
-dontwarn com.lzy.okgo.**
-keep class com.lzy.okgo.**{*;}

#okrx
-dontwarn com.lzy.okrx.**
-keep class com.lzy.okrx.**{*;}

#okrx2
-dontwarn com.lzy.okrx2.**
-keep class com.lzy.okrx2.**{*;}

#okserver
-dontwarn com.lzy.okserver.**
-keep class com.lzy.okserver.**{*;}