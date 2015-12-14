package com.czvv.repairsystemmobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android_serialport_api.sample.Application;
import android_serialport_api.sample.ExitApplication;

import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.dao.Sys_userDao;
import com.czvv.repairsystemmobile.utils.Constants;
import com.czvv.repairsystemmobile.utils.MD5Utils;
import com.czvv.repairsystemmobile.utils.SharedPreferencesUtil;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.czvv.repairsystemmobile.utils.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LoginActivity extends RepairBaseActivity {

	@ViewInject(R.id.etLoginName)
	EditText etLoginName;
	@ViewInject(R.id.etPassword)
	EditText etPassword;
	@ViewInject(R.id.btn_Admin_Login)
	ImageButton btnAdminLogin;
	@ViewInject(R.id.btnLogin)
	ImageButton btnLogin;

	private Sys_userDao userDao;
	private Activity act;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ViewUtils.inject(this);
		act = this;
		sp = getSharedPreferences("serviceInfo", MODE_PRIVATE);
		sp.edit().putString("loginName", "").commit();
		userDao = Sys_userDao.getInstance(this);

		init();
	}

	private void init() {
		btnAdminLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, LoginAdminActivity.class);
				startActivity(intent);
			}
		});

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String loginName = etLoginName.getText().toString().trim();
				String password = MD5Utils.md5Encode(etPassword.getText().toString().trim());
				if ("".equals(loginName) || "".equals(password)) {
					ToastUtils.showToast(act, "帐号或密码不能为空");
					return;
				}

				matchUser(loginName, password);
				// startActivity(new Intent(act,SelectDevActivity.class));
			}
		});
	}

	protected void matchUser(final String loginName, final String password) {
		ThreadUtils.runInBackground(new Runnable() {

			@Override
			public void run() {
				boolean avaiLogin = userDao.avaiLogin(loginName, password);
				if (avaiLogin) {
					sp.edit().putString("loginName", loginName).commit();
					startActivity(new Intent(act, SelectDevActivity.class));
				} else {
					ToastUtils.showToast(act, "账号或者密码错误");
				}
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
		etLoginName.setText("");
		etPassword.setText("");
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
