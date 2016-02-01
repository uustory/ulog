package com.u8.sdk.log;

public interface ILog {

	public void d(String tag, String msg);
	public void i(String tag, String msg);
	public void w(String tag, String msg);
	public void w(String tag, String msg, Throwable e);
	public void e(String tag, String msg);
	public void e(String tag, String msg, Throwable e);
	
	public void destory();
}
