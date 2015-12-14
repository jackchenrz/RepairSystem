package com.czvv.repairsystemmobile.utils;

import android.content.Context;
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
}
