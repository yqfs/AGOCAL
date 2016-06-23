package com.taobao.uikit.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * 基于Android平台的Handler实现的强大的Timer
 * 支持开始、暂停、重新开始、停止、取消等操作，可取代系统内置的Timer
 * @author 剑白
 * @date 2014年3月23日
 * */
public class HandlerTimer implements  Runnable{

	private Handler handler;
	private long interval;
	private TimerStatus status;
	private Runnable task;
	

	public HandlerTimer(Runnable task){
		this(1000, task);
	}
	
	public HandlerTimer(long interval, Runnable task){
		this(interval, task, new Handler(Looper.getMainLooper()));
	}
	
	public HandlerTimer(long interval, Runnable task, Handler handler){
		if (handler == null || task == null) {
			throw new NullPointerException("handler or task must not be null");
		}
		this.interval = interval;
		this.task = task;
		this.handler = handler;
		this.status = TimerStatus.Waiting;
	}
	
	@Override
	public final  void run() {
		if (status == TimerStatus.Waiting 
			|| status == TimerStatus.Paused
			|| status == TimerStatus.Stopped) {
			return;
		}
		task.run();
        handler.postDelayed(this, interval);
	}

	/**
	 * 启动定时器
	 * */
	public void start(){
        if (this.status != TimerStatus.Running) {
            handler.removeCallbacks(this);
            this.status = TimerStatus.Running;
            handler.postDelayed(this, interval);
        }
    }
	
	/**
	 * 暂停定时器
	 * */
	public void pause(){
		this.status = TimerStatus.Paused;
		handler.removeCallbacks(this);
	}
	
	/**
	 * 启动定时器
	 * */
	public void restart(){
		handler.removeCallbacks(this);
		this.status = TimerStatus.Running;
		handler.postDelayed(this, interval);
	}
	
	/**
	 * 停止定时器
	 * */
	public void stop(){
		status = TimerStatus.Stopped;
		handler.removeCallbacks(this);
	}
	
	/**
	 * 取消定时器
	 * */
	public void cancel(){
		status = TimerStatus.Stopped;
		handler.removeCallbacks(this);
	}

    /**
     * Timer的几种状态
     * */
	enum TimerStatus{
		
		Waiting(0, "待启动"),
		Running(1, "运行中"),
		Paused(-1, "暂停"),
		Stopped(-2, "停止");
		
		private int code;
		private String desc;
		TimerStatus(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		
		public int getCode() {
			return code;
		}
		
		public void setCode(int code) {
			this.code = code;
		}
		
		public String getDesc() {
			return desc;
		}
		
		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
}
