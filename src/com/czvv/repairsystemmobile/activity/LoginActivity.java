package com.czvv.repairsystemmobile.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.czvv.repairsystemmobile.Constants;
import com.czvv.repairsystemmobile.MApplication;
import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.base.BaseActivity;
import com.czvv.repairsystemmobile.dao.Sys_userDao;
import com.czvv.repairsystemmobile.utils.AlertUtils;
import com.czvv.repairsystemmobile.utils.MD5Utils;
import com.czvv.repairsystemmobile.utils.SharedPreferencesUtil;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.czvv.repairsystemmobile.utils.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LoginActivity extends BaseActivity {

	@ViewInject(R.id.etLoginName)
	EditText etLoginName;
	@ViewInject(R.id.etPassword)
	EditText etPassword;
	@ViewInject(R.id.btnLogin)
	Button btnLogin;

	private Sys_userDao userDao;
	private Activity act;
	private SharedPreferences sp;
	private Dialog loadingDialog;

	@Override
	public int bindLayout() {
		return R.layout.activity_login;
	}

	@Override
	public void initView(View view) {
		ViewUtils.inject(this);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String serverIP = sp.getString("serverIP", "");
				String serverPort = sp.getString("serverPort", "");
				if (serverIP != null && !"".equals(serverIP) && serverPort != null
						&& !"".equals(serverPort)) {
					MApplication.assignData("url", "http://" + serverIP + ":" + serverPort
							+ Constants.SERVICE_PAGE);
				}
				
				final String loginName = etLoginName.getText().toString().trim();
				final String password = MD5Utils.md5Encode(etPassword.getText()
						.toString().trim());
				final String pwd = etPassword.getText()
						.toString().trim();
				if ("".equals(loginName) || "".equals(password)) {
					ToastUtils.showToast(act, "帐号或密码不能为空");
					return;
				}

				loadingDialog = AlertUtils.createLoadingDialog(LoginActivity.this, "正在登陆，请稍后...");
				loadingDialog.show();
				
				ThreadUtils.runInBackground(new Runnable() {

					@Override
					public void run() {
						SystemClock.sleep(1000);
						if(Constants.ADMIN_LOGINNAME.equalsIgnoreCase(loginName) && Constants.ADMIN_PASSWORD.equals(pwd)){
							startActivity(new Intent(LoginActivity.this,AdminActivity.class));
							overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
						}else{
							matchUser(loginName, password);
						}
					}
				});
			}
		});
	}

	@Override
	public void doBusiness(Context mContext) {
		act = this;
		sp = getSharedPreferences("serviceInfo", MODE_PRIVATE);
		sp.edit().putString("loginName", "").commit();
		userDao = Sys_userDao.getInstance(this);
	}

	@Override
	public void resume() {
		etLoginName.setText("");
		etPassword.setText("");
		if(loadingDialog != null){
			loadingDialog.dismiss();
		}
	}

	@Override
	public void destroy() {

	}
	
	protected void matchUser(final String loginName, final String password) {
		
		boolean avaiLogin = userDao.avaiLogin(loginName, password);
		if (avaiLogin) {
			sp.edit().putString("loginName", loginName).commit();
			startActivity(new Intent(act, MainActivity.class));
			overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
		} else {
			loadingDialog.dismiss();
			ToastUtils.showToast(act, "账号或者密码错误");
		}
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
				mApplication.removeAll();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
