package com.u8.sdk.log;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class Log{
	
	private static Log instance = new Log();
	
	private List<ILog> logPrinters;
	private boolean isInited = false;
	
	private boolean enable = false;
	private String level = ULog.L_DEBUG;
	private boolean local = true;
	private boolean remote = true;
	private int remoteInterval = 1000;
	private String remoteUrl = "";
	
	private Log(){
		logPrinters = new ArrayList<ILog>();
	}

	public static void d(String tag, String msg) {
		try{
		
			if(!ULog.L_DEBUG.equalsIgnoreCase(instance.level)){
				return;
			}
			
			for(ILog printer: instance.logPrinters){
				printer.d(tag, msg);
			}			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

	}

	public static void i(String tag, String msg) {
		try{
			
			if(!ULog.L_DEBUG.equalsIgnoreCase(instance.level) &&
					!ULog.L_INFO.equalsIgnoreCase(instance.level)){
				return;
			}			
			
			for(ILog printer: instance.logPrinters){
				printer.i(tag, msg);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void w(String tag, String msg) {
		try{
			if(ULog.L_ERROR.equalsIgnoreCase(instance.level)){
				return;
			}			
			
			for(ILog printer: instance.logPrinters){
				printer.w(tag, msg);
			}			
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public static void w(String tag, String msg, Throwable e) {
		try{
			
			if(ULog.L_ERROR.equalsIgnoreCase(instance.level)){
				return;
			}			
			
			for(ILog printer: instance.logPrinters){
				printer.w(tag, msg, e);
			}
			
		}catch(Exception e2){
			e2.printStackTrace();
		}
	}


	public static void e(String tag, String msg) {
		try{
			for(ILog printer: instance.logPrinters){
				printer.e(tag, msg);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public static void e(String tag, String msg, Throwable e) {
		try{
			for(ILog printer: instance.logPrinters){
				printer.e(tag, msg, e);
			}
		}catch(Exception e2){
			e2.printStackTrace();
		}
	}


	/**
	 * 在Application的attachBaseContext中调用
	 * @param context
	 */
	public static void init(Context context){
		
		try{
			
			if(instance.isInited){
				return;
			}
			
			instance.parseConfig(context);
			
			instance.logPrinters.clear();
			
			if(!instance.enable){
				android.util.Log.d("ULOG", "the log is not enabled.");
				return;
			}
			
			if(instance.local){
				instance.logPrinters.add(new ULocalLog());
			}
			
			if(instance.remote){
				instance.logPrinters.add(new URemoteLog(instance.remoteUrl, instance.remoteInterval));
			}
			
			Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				
				@Override
				public void uncaughtException(Thread t, final Throwable e) {

					new Thread(new Runnable() {
						
						@Override
						public void run() {
							try{
								URemoteLogPrinter printer = new URemoteLogPrinter();
								printer.printImmediate(instance.remoteUrl, new ULog(ULog.L_ERROR, "Crash", "Application Crashed!!!", e));

							}catch(Exception e){
								e.printStackTrace();
							}finally{
								System.exit(0);
							}

						}
					}).start();
					
					try {
						Thread.sleep(500);
						
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
					
				}
			});
			
			instance.isInited = true;			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}

	
	/**
	 * 在Application的onTerminate中调用销毁
	 */
	public static void destory(){
		
		try{
			if(instance.logPrinters != null){
				for(ILog printer : instance.logPrinters){
					printer.destory();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}


	}
	
	private void parseConfig(Context ctx){
		
		try{
			ApplicationInfo appInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
			if(appInfo != null && appInfo.metaData != null){
				
				if(appInfo.metaData.containsKey("ulog.enable")){
					enable = appInfo.metaData.getBoolean("ulog.enable");
				}
				
				if(appInfo.metaData.containsKey("ulog.level")){
					level = appInfo.metaData.getString("ulog.level");
				}	
				
				if(appInfo.metaData.containsKey("ulog.local")){
					local = appInfo.metaData.getBoolean("ulog.local");
				}	
				
				if(appInfo.metaData.containsKey("ulog.remote")){
					remote = appInfo.metaData.getBoolean("ulog.remote");
				}	
				
				if(appInfo.metaData.containsKey("ulog.remote_interval")){
					remoteInterval = appInfo.metaData.getInt("ulog.remote_interval");
				}
				
				if(appInfo.metaData.containsKey("ulog.remote_url")){
					remoteUrl = appInfo.metaData.getString("ulog.remote_url");
				}				
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}		
	}	
}
