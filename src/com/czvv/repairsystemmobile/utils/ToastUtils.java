package com.czvv.repairsystemmobile.utils;

import android.app.Activity;
import android.widget.Toast;

import com.czvv.repairsystemmobile.view.MyToast;

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
	
	//×Ô¶¨ÒåToast
		public static void mShowToast(final Activity act,final String str){
			if("main".equals(Thread.currentThread().getName())){
				MyToast.makeText(act, str, Toast.LENGTH_SHORT).show();
			}else{
				act.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						MyToast.makeText(act, str, Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
}
