# ULog

游戏在发布到Android上时，在调试apk的时候，往往我们都是通过Logcat等工具来根据日志来进行问题的定位和排查，
但是很多时候真机连不上logcat，还有logcat日志过滤不太好用，日志容易被顶掉。

所以，那些使用[U8SDK统一渠道SDK接入框架](http://www.uustory.com)的同学，反馈比较多的问题之一就是：调试困难

于是，我们就开发了一个简单的远程日志打印的功能，旨在让Android Apk的调试可以简单直观一点。

在原有Log的基础上，我们加上了远程日志打印，我们暂且称之为ulog吧

ulog包括两部分

ULog4Android ：一个简单的Android Library工程，简单封装了下日志，增加远程日志发送
uconsole.py: 一个python写的无比简单的日志接收服务器，用于接收日志


###使用说明：

1、日志服务器启动和配置

没有python的，可以安装下python。安装完成之后，需要安装web.py 这个python库。安装好之后，直接运行：

python uconsole.py 就能启动服务器了，默认监听的端口是8080，如果该端口被占用了，那么可以这样运行，来指定端口：

python uconsole.py 8082 

这样，端口就监听在了8082上了

你可以浏览器中，打开localhost:8082/ 日志将会显示在网页上


2、客户端ULog4Android配置

直接将ULog4Android/bin/ulog4android.jar拷贝到Android工程中的libs目录中

在AndroidManifest.xml中的meta-data中，添加部分日志参数：

<meta-data android:name="ulog.enable" android:value="true" />  <!--是否开启日志，关闭之后，不会输出到logcat也不会输出到远程-->
<meta-data android:name="ulog.level" android:value="DEBUG" />   <!--日志级别(DEBUG|INFO|WARNING|ERROR)-->
<meta-data android:name="ulog.local" android:value="true" />    <!--是否在logcat中打印-->
<meta-data android:name="ulog.remote" android:value="true" />   <!--是否远程打印-->
<meta-data android:name="ulog.remote_interval" android:value="500" />   <!--远程打印时，日志上报间隔，单位毫秒-->
<meta-data android:name="ulog.remote_url" android:value="http://192.168.18.9:8080/" />  <!--远程日志服务器地址，就是uconsole监听的地址-->


3、客户端ULog4Android使用

日志调用类：com.u8.sdk.log.Log

3.1 调用日志之前，需要先初始化
```
调用Log.init()；一般推荐在Application的onCreate或者attachBaseContext中调用
```
3.2 调用销毁 
```
在应用退出的时候，调用Log.destory()接口，进行销毁
```

3.3 日志调用
```
Log.d(String tag, String msg)
Log.i(String tag, String msg)
Log.w(String tag, String msg)
Log.w(String tag, String msg, Throwable e)
Log.e(String tag, String msg)
Log.e(String tag, String msg, Throwable e)
```

4、运行截图

[运行图片](images/rmlog.png)







