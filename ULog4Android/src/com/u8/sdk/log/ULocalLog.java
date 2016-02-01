package com.u8.sdk.log;

import android.util.Log;

/**
 * 
 * Android本地日志输出
 *
 */
public class ULocalLog implements ILog{

	@Override
	public void d(String tag, String msg) {
		Log.d(tag, msg);
	}

	@Override
	public void i(String tag, String msg) {
		Log.i(tag, msg);
	}

	@Override
	public void w(String tag, String msg) {
		Log.w(tag, msg);
	}

	@Override
	public void e(String tag, String msg) {
		Log.e(tag, msg);
	}

	@Override
	public void w(String tag, String msg, Throwable e) {
		Log.w(tag, msg, e);
	}

	@Override
	public void e(String tag, String msg, Throwable e) {
		Log.e(tag, msg, e);
		
	}

	@Override
	public void destory() {

	}

}
