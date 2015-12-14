package com.czvv.repairsystemmobile.activity;

import android.app.Activity;
import android_serialport_api.sample.ExitApplication;

public class RepairBaseActivity extends Activity {
	// private boolean startispressed = false;
	// private boolean poundispressed = false;
	// private boolean downispressed = false;

	@Override
	protected void onStart() {
		super.onStart();

		ExitApplication.getInstance().addActivity(this);
	}

	/*
	 * ***********************注释监听按键*******************************************
	 * 
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键 return true; } else
	 * if (keyCode == KeyEvent.KEYCODE_HOME) { //
	 * 由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow() } else if (keyCode ==
	 * KeyEvent.KEYCODE_CALL) { // 监控/拦截/屏蔽call键 return true; } else if (keyCode
	 * == KeyEvent.KEYCODE_STAR) {// * startispressed = true; } else if (keyCode
	 * == KeyEvent.KEYCODE_POUND) {// # poundispressed = true; } else if
	 * (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) { downispressed = true; if
	 * (startispressed && poundispressed && downispressed) { // finish();// 退出应用
	 * // System.exit(0); final SharedPreferencesUtil spUtil = new
	 * SharedPreferencesUtil(this, Constants.USER_INFO); UserInfo nowUser =
	 * spUtil.getLoginUser(); if (nowUser != null) { if
	 * (nowUser.getLoginName().equals(Constants.ADMIN_LOGINNAME)) {
	 * AlertDialog.Builder builder = new AlertDialog.Builder(this);
	 * builder.setTitle("提示"); builder.setMessage("确定要退出系统吗？");
	 * builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * spUtil.clearLoginUser(); ExitApplication.getInstance().exit(); } });
	 * builder.setNegativeButton("取消", null); AlertDialog alertDialog =
	 * builder.create(); alertDialog.show(); } } } }
	 * 
	 * return super.onKeyDown(keyCode, event); }
	 * 
	 * @Override public boolean onKeyUp(int keyCode, KeyEvent event) {
	 * 
	 * if (keyCode == KeyEvent.KEYCODE_STAR) { startispressed = false; } else if
	 * (keyCode == KeyEvent.KEYCODE_POUND) { poundispressed = false; } else if
	 * (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) { downispressed = false; }
	 * 
	 * return super.onKeyUp(keyCode, event);
	 * 
	 * }
	 * 
	 * // 拦截/屏蔽系统Home键 public void onAttachedToWindow() {
	 * this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
	 * super.onAttachedToWindow(); }
	 */
}
