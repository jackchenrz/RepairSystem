package com.czvv.repairsystemmobile.utils;

import android.os.Handler;
import android.os.Looper;

public class ThreadUtils {
	// 如果handler创建，会自动关联主线程
	public static Handler handler = new Handler(Looper.getMainLooper());

	/**
	 * 子线程执行
	 */
	public static void runInBackground(Runnable runnable) {
		new Thread(runnable).start();
	}
	
	/**
	 * 主线程执行
	 */
	public static void runInMainThread(Runnable runnable) {
		handler.post(runnable);
	}
}
