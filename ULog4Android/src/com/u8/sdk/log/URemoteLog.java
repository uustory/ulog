package com.u8.sdk.log;

/**
 * 
 * 远程日志实现
 *
 */
public class URemoteLog implements ILog{
	
	private URemoteLogPrinter printer;
	
	public URemoteLog(String url, int interval){
		printer = new URemoteLogPrinter(url, interval);
	}
	
	@Override
	public void d(String tag, String msg) {
		printer.print(new ULog(ULog.L_DEBUG, tag, msg));
	}

	@Override
	public void i(String tag, String msg) {
		printer.print(new ULog(ULog.L_INFO, tag, msg));
	}

	@Override
	public void w(String tag, String msg) {
		printer.print(new ULog(ULog.L_WARN, tag, msg));
	}

	@Override
	public void w(String tag, String msg, Throwable e) {
		printer.print(new ULog(ULog.L_WARN, tag, msg, e));
	}

	@Override
	public void e(String tag, String msg) {
		printer.print(new ULog(ULog.L_ERROR, tag, msg));
	}

	@Override
	public void e(String tag, String msg, Throwable e) {
		printer.print(new ULog(ULog.L_ERROR, tag, msg, e));
	}

	@Override
	public void destory() {
		printer.stop();
	}

}
