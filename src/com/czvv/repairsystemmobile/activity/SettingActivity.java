package com.czvv.repairsystemmobile.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.czvv.repairsystemmobile.Constants;
import com.czvv.repairsystemmobile.MApplication;
import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.base.BaseActivity;
import com.czvv.repairsystemmobile.utils.AlertUtils;
import com.czvv.repairsystemmobile.utils.MobileUtil;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.czvv.repairsystemmobile.utils.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SettingActivity extends BaseActivity implements
		OnClickListener {

	@ViewInject(R.id.etServerIP)
	EditText etServerIP;
	@ViewInject(R.id.etServerPort)
	EditText etServerPort;
	@ViewInject(R.id.btnBackAdmin)
	ImageButton btnBackAdmin;
	@ViewInject(R.id.btnSaveSetting)
	Button btnSaveSetting;

	@ViewInject(R.id.tv_mac)
	TextView tvMac;
	@ViewInject(R.id.tv_imei)
	TextView tvImei;
	@ViewInject(R.id.tv_version)
	TextView tvVersion;

	private SharedPreferences sp;
	private Activity act;
	private Dialog loadingDialog;

	@Override
	public int bindLayout() {
		return R.layout.activity_setting;
	}

	@Override
	public void initView(View view) {
		ViewUtils.inject(this);
		btnBackAdmin.setOnClickListener(this);
		btnSaveSetting.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		act = this;
		sp = getSharedPreferences("serviceInfo", MODE_PRIVATE);	
		tvImei.setText("IMEI:" + MobileUtil.getIMEI(act));
		tvMac.setText("MAC:" + MobileUtil.getMacAddress(act));
		tvVersion.setText("版本：" + MobileUtil.getVersionName(act));

	}

	@Override
	public void resume() {
		String serverIP = sp.getString("serverIP", "");
		String serverPort = sp.getString("serverPort", "");

		etServerIP.setText(serverIP);
		etServerPort.setText(serverPort);
	}

	@Override
	public void destroy() {
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBackAdmin:
			finish();
			overridePendingTransition(0, R.anim.base_slide_right_out);
			break;
		case R.id.btnSaveSetting:
			loadingDialog = AlertUtils.createLoadingDialog(SettingActivity.this, "正在保存设置，请稍后...");
			loadingDialog.show();
			final String serverIP = etServerIP.getText().toString().trim();
			final String serverPort = etServerPort.getText().toString().trim();
			if (TextUtils.isEmpty(serverIP) || TextUtils.isEmpty(serverPort)) {
				ToastUtils.showToast(act, "请输入IP地址和端口号");
				loadingDialog.dismiss();
				return;
			}
			sp.edit().putString("serverIP", serverIP).commit();
			sp.edit().putString("serverPort", serverPort).commit();
			MApplication.assignData("url", "http://" + serverIP + ":" + serverPort
					+ Constants.SERVICE_PAGE);
			ThreadUtils.runInBackground(new Runnable() {
				
				@Override
				public void run() {
					SystemClock.sleep(1000);
					finish();
					overridePendingTransition(0, R.anim.base_slide_right_out);
				}
			});
			break;
		}
	}
}
