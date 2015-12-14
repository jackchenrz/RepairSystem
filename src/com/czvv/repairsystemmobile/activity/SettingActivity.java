package com.czvv.repairsystemmobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.utils.MobileUtil;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.czvv.repairsystemmobile.utils.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SettingActivity extends RepairBaseActivity implements
		OnClickListener {

	@ViewInject(R.id.etServerIP)
	EditText etServerIP;
	@ViewInject(R.id.etServerPort)
	EditText etServerPort;
	@ViewInject(R.id.btnBackAdmin)
	Button btnBackAdmin;
	@ViewInject(R.id.btnSaveSetting)
	Button btnSaveSetting;
	@ViewInject(R.id.btnConnect)
	Button btnConnect;

	@ViewInject(R.id.tv_mac)
	TextView tvMac;
	@ViewInject(R.id.tv_imei)
	TextView tvImei;
	@ViewInject(R.id.tv_version)
	TextView tvVersion;

	private SharedPreferences sp;
	private Activity act;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		act = this;
		sp = getSharedPreferences("serviceInfo", MODE_PRIVATE);
		ViewUtils.inject(this);
		btnBackAdmin.setOnClickListener(this);
		btnSaveSetting.setOnClickListener(this);
		btnConnect.setOnClickListener(this);
		fillData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBackAdmin:
			finish();
			break;
		case R.id.btnSaveSetting:
			showProgressDialog("正在保存设置，请稍后...");
			final String serverIP = etServerIP.getText().toString().trim();
			final String serverPort = etServerPort.getText().toString().trim();
			if (TextUtils.isEmpty(serverIP) || TextUtils.isEmpty(serverPort)) {
				ToastUtils.showToast(act, "请输入IP地址和端口号");
			}
			sp.edit().putString("serverIP", serverIP).commit();
			sp.edit().putString("serverPort", serverPort).commit();
			ThreadUtils.runInBackground(new Runnable() {
				
				@Override
				public void run() {
					SystemClock.sleep(2000);
					progressDialog.dismiss();
					Intent intent = new Intent(act, AdminActivity.class);
					intent.putExtra("serverIP", serverIP);
					intent.putExtra("serverPort", serverPort);
					startActivity(intent);
					finish();
				}
			});
			break;
		case R.id.btnConnect:
			ThreadUtils.runInBackground(new Runnable() {

				@Override
				public void run() {
					if (isNetworkAvailable(act)) {
						String serverIP = etServerIP.getText().toString().trim();
						String serverPort = etServerPort.getText().toString().trim();
						if (TextUtils.isEmpty(serverIP) || TextUtils.isEmpty(serverPort)) {
							ToastUtils.showToast(act, "请输入IP地址和端口号");
						}
						sp.edit().putString("serverIP", serverIP).commit();
						sp.edit().putString("serverPort", serverPort).commit();
						ToastUtils.showToast(act, "网络开启，请确认服务器地址与端口，点击完成");
					} else {
						ToastUtils.showToast(act, "请先开启网络");
					}
				}
			});
			break;
		}
	}

	private void fillData() {

		tvImei.setText("IMEI:" + MobileUtil.getIMEI(act));
		tvMac.setText("MAC:" + MobileUtil.getMacAddress(act));
		tvVersion.setText("版本：" + getVersionName());

	}

	/**
	 * 判断是否开启网络
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

	@Override
	protected void onStart() {

		String serverIP = sp.getString("serverIP", "");
		String serverPort = sp.getString("serverPort", "");

		etServerIP.setText(serverIP);
		etServerPort.setText(serverPort);
		super.onStart();
	}

	private String getVersionName() {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String version = packInfo.versionName;
		return version;
	}
	
	private void showProgressDialog(String msg) {
		if (null == progressDialog) {
			progressDialog = new ProgressDialog(this);
		}
		progressDialog.setMessage(msg);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	}


}
