package com.czvv.repairsystemmobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android_serialport_api.sample.ExitApplication;

import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.bean.AdminInfo;
import com.czvv.repairsystemmobile.utils.Constants;
import com.czvv.repairsystemmobile.utils.MD5Utils;
import com.czvv.repairsystemmobile.utils.SharedPreferencesUtil;
import com.czvv.repairsystemmobile.utils.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LoginAdminActivity extends RepairBaseActivity implements
		OnClickListener {

	@ViewInject(R.id.etLoginName)
	EditText etLoginName;
	@ViewInject(R.id.etPassword)
	EditText etPassword;

	@ViewInject(R.id.btnBackUserLogin)
	ImageButton btnBackUserLogin;
	@ViewInject(R.id.btnLogin)
	ImageButton btnLogin;

	private AdminInfo loginAdmin;
	private Activity act;
	private ProgressDialog progressDialog;
	private final int IS_FINISH = 1;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int isLogin = msg.arg1;
			if (isLogin == 1) {
				Intent intent = new Intent();
				intent.setClass(LoginAdminActivity.this, AdminActivity.class);
				startActivity(intent);
				finish();
			} else {
				ToastUtils.showToast(act, "帐号或密码错误");
			}

			if (msg.what == IS_FINISH) {
				progressDialog.dismiss();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_admin);
		ViewUtils.inject(this);
		btnBackUserLogin.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		loginAdmin = new AdminInfo();
		act = this;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBackUserLogin:
			Intent intent = new Intent();
			intent.setClass(LoginAdminActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.btnLogin:
			String loginName = etLoginName.getText().toString().trim().toUpperCase();
			String password = MD5Utils.md5Encode(etPassword.getText()
					.toString().trim());

			if ("".equals(loginName) || "".equals(password)) {
				ToastUtils.showToast(act, "帐号或密码不能为空");
				return;
			}

			loginAdmin.userName = loginName;
			loginAdmin.password = password;
			new Thread(new LoginThread()).start();
			showDialg("正在登录...");
			break;
		}
	}

	private class LoginThread implements Runnable {

		@Override
		public void run() {

			boolean isLogin = false;
			try {
				if (loginAdmin.userName.equals(Constants.ADMIN_LOGINNAME)
						&& loginAdmin.password.equals(MD5Utils
								.md5Encode(Constants.ADMIN_PASSWORD))) {
					isLogin = true;
					AdminInfo adminInfo = new AdminInfo();
					adminInfo.userName = Constants.ADMIN_LOGINNAME;
					adminInfo.password = Constants.ADMIN_PASSWORD;
					// 登录成功，将用户信息存到本地
					SharedPreferencesUtil spUtil = new SharedPreferencesUtil(
							LoginAdminActivity.this, Constants.ADMIN_INFO);
					spUtil.saveLoginUser(adminInfo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Message message = Message.obtain();
			message.arg1 = isLogin ? 1 : 0;
			message.what = IS_FINISH;
			handler.sendMessage(message);
		}
	}

	private void showDialg(String msg) {

		if (null == progressDialog) {
			progressDialog = new ProgressDialog(this);
		}
		progressDialog.setMessage(msg);
		progressDialog.show();
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出应用",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				final SharedPreferencesUtil spUtil = new SharedPreferencesUtil(
						this, Constants.ADMIN_INFO);
				spUtil.clearLoginUser();
				ExitApplication.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
