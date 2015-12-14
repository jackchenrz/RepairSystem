package com.czvv.repairsystemmobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import com.czvv.repairsystemmobile.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class WelcomeActivity extends Activity {
	@ViewInject(R.id.tv_version_name)
	TextView tvVersionName;
	@ViewInject(R.id.tv_progress)
	TextView tv_progress;
	private int versionCode;
	private Activity act;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		ViewUtils.inject(this);
		act = this;
		new Thread() {
			public void run() {
				SystemClock.sleep(2000);
				Intent intent = new Intent(WelcomeActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			};
		}.start();

		PackageManager packageManager = getPackageManager();
		try {
			// packageInfo 就是对应清单文件中的所有信息
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			String versionName = packageInfo.versionName;
			versionCode = packageInfo.versionCode;
			tvVersionName.setText("版本：" + versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		// chekUpdate();
	}

	

}
