package com.czvv.repairsystemmobile.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.czvv.repairsystemmobile.Constants;
import com.czvv.repairsystemmobile.bean.AdminInfo;

public class SharedPreferencesUtil {

	private SharedPreferences sp;
	private Editor editor;

	public SharedPreferencesUtil(Context context, String file) {
		sp = context.getSharedPreferences(file, context.MODE_PRIVATE);
		editor = sp.edit();
	}
	
	public void saveStr(String str) {
		editor.putString(Constants.BIRTHDAY, str).commit();
	}
	public String getStr(String str) {
		String string = sp.getString(Constants.BIRTHDAY, "");
		return string;
	}

	/**
	 * 保存登录用户信息
	 */
	public void saveLoginUser(AdminInfo admin) {

		editor.putString(AdminInfo.USERNAME,admin.userName);
		editor.putString(AdminInfo.PASSWORD, admin.password);

		editor.commit();

	}

	public void clearLoginUser() {
		editor.clear();
		editor.commit();
	}

	/**
	 * 获取登录用户信息
	 */
	public AdminInfo getLoginUser() {
		
		AdminInfo admin = new AdminInfo();

		admin.userName = sp.getString(AdminInfo.USERNAME, "");
		admin.password = sp.getString(AdminInfo.PASSWORD, "");

		return admin;
	}

	// 是否第一次运行本应用
	public void setIsFirst(boolean isFirst) {
		editor.putBoolean("isFirst", isFirst);
		editor.commit();
	}

	public boolean getIsFirst() {
		return sp.getBoolean("isFirst", true);
	}
}
