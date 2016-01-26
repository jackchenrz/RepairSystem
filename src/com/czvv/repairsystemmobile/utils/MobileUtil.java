package com.czvv.repairsystemmobile.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class MobileUtil {
	/**
	 * ��ȡ�ֻ�MAC��ַ
	 */
	public static String getMacAddress(Context context) {
		String result = "";
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		result = wifiInfo.getMacAddress();
		return result;
	}

	/**
	 * ��ȡ�ֻ�IMEI
	 */
	public static String getIMEI(Context context) {
		String result = "";
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		result = tm.getDeviceId();
		return result;
	}
	
	public static String getVersionName(Context context) {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String version = packInfo.versionName;
		return version;
	}
	
	/**
	 * �ж��Ƿ�������
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connect = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connect == null)
			return false;
		NetworkInfo netinfo = connect.getActiveNetworkInfo();
		if (netinfo == null)
			return false;
		if (netinfo.isConnected())
			return true;
		return false;
	}
}
