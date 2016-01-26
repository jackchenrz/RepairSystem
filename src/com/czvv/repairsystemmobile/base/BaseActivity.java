package com.czvv.repairsystemmobile.base;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import com.czvv.repairsystemmobile.MApplication;
import com.czvv.repairsystemmobile.R;

/**
 * android 系统中的四大组件之一Activity基类
 * 
 * @version 1.0
 * 
 */
public abstract class BaseActivity extends Activity implements IBaseActivity {

	/*** 整个应用Applicaiton **/
	protected MApplication mApplication = null;
	/** 当前Activity的弱引用，防止内存泄露 **/
	private WeakReference<Activity> context = null;
	/** 当前Activity渲染的视图View **/
	private View mContextView = null;
	/** 共通操作 **/
	/** 日志输出标志 **/
	protected final String TAG = this.getClass().getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "BaseActivity-->onCreate()");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置渲染视图View
		mContextView = LayoutInflater.from(this).inflate(bindLayout(), null);
		setContentView(mContextView);

		// 获取应用Application
		mApplication = (MApplication) getApplicationContext();

		// 将当前Activity压入栈
		context = new WeakReference<Activity>(this);
		mApplication.pushTask(context);

		// 初始化控件
		initView(mContextView);

		// 业务操作
		doBusiness(this);

		// 显示VoerFlowMenu
		displayOverflowMenu(getContext());
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "BaseActivity-->onRestart()");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "BaseActivity-->onStart()");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "BaseActivity-->onResume()");
		resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "BaseActivity-->onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "BaseActivity-->onStop()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "BaseActivity-->onDestroy()");

		destroy();
		mApplication.removeTask(context);
	}

	/**
	 * 显示Actionbar菜单图标
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);// 显示
				} catch (Exception e) {
					Log.e(TAG, "onMenuOpened-->" + e.getMessage());
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	/**
	 * 显示OverFlowMenu按钮
	 * 
	 * @param mContext
	 *            上下文Context
	 */
	public void displayOverflowMenu(Context mContext) {
		try {
			ViewConfiguration config = ViewConfiguration.get(mContext);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);// 显示
			}
		} catch (Exception e) {
			Log.e("ActionBar", e.getMessage());
		}
	}

	/**
	 * 获取当前Activity
	 * 
	 * @return
	 */
	protected Activity getContext() {
		if (null != context)
			return context.get();
		else
			return null;
	}

	/**
	 * Actionbar点击返回键关闭事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(0, R.anim.base_slide_right_out);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.base_slide_right_out);
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
