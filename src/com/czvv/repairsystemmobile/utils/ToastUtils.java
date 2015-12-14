package com.czvv.repairsystemmobile.utils;

import android.app.Activity;
import android.widget.Toast;

public class ToastUtils {
	public static void showToast(final Activity act,final String str){
		if("main".equals(Thread.currentThread().getName())){
			Toast.makeText(act, str, 0).show();
		}else{
			act.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(act, str, 0).show();
				}
			});
		}
	}
}
