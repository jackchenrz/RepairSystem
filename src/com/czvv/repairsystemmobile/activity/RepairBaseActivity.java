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
	 * ***********************ע�ͼ�������*******************************************
	 * 
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (keyCode == KeyEvent.KEYCODE_BACK) { // ���/����/���η��ؼ� return true; } else
	 * if (keyCode == KeyEvent.KEYCODE_HOME) { //
	 * ����Home��Ϊϵͳ�����˴����ܲ�����Ҫ��дonAttachedToWindow() } else if (keyCode ==
	 * KeyEvent.KEYCODE_CALL) { // ���/����/����call�� return true; } else if (keyCode
	 * == KeyEvent.KEYCODE_STAR) {// * startispressed = true; } else if (keyCode
	 * == KeyEvent.KEYCODE_POUND) {// # poundispressed = true; } else if
	 * (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) { downispressed = true; if
	 * (startispressed && poundispressed && downispressed) { // finish();// �˳�Ӧ��
	 * // System.exit(0); final SharedPreferencesUtil spUtil = new
	 * SharedPreferencesUtil(this, Constants.USER_INFO); UserInfo nowUser =
	 * spUtil.getLoginUser(); if (nowUser != null) { if
	 * (nowUser.getLoginName().equals(Constants.ADMIN_LOGINNAME)) {
	 * AlertDialog.Builder builder = new AlertDialog.Builder(this);
	 * builder.setTitle("��ʾ"); builder.setMessage("ȷ��Ҫ�˳�ϵͳ��");
	 * builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * spUtil.clearLoginUser(); ExitApplication.getInstance().exit(); } });
	 * builder.setNegativeButton("ȡ��", null); AlertDialog alertDialog =
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
	 * // ����/����ϵͳHome�� public void onAttachedToWindow() {
	 * this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
	 * super.onAttachedToWindow(); }
	 */
}
