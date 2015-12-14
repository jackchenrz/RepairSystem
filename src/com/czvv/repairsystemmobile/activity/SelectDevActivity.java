package com.czvv.repairsystemmobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android_serialport_api.sample.Application;

import com.czvv.repairsystemmobile.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SelectDevActivity extends RepairBaseActivity implements
		OnClickListener {

	@ViewInject(R.id.btn_sel_techeqpt)
	Button btnSelTecheqpt;
	@ViewInject(R.id.btn_sel_fiveteqpt)
	Button btnSelFiveteqpt;
	@ViewInject(R.id.btnLogout)
	Button btnLogout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_selectdev);
		ViewUtils.inject(this);
		btnSelTecheqpt.setOnClickListener(this);
		btnSelFiveteqpt.setOnClickListener(this);
		btnLogout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sel_techeqpt:
			Application.mRepairType = "Tech";
			startActivity(new Intent(this, RepairHandlerAcrivity.class));
			break;
		case R.id.btn_sel_fiveteqpt:
			Application.mRepairType = "5T";
			startActivity(new Intent(this, RepairHandlerAcrivity.class));
			break;
		case R.id.btnLogout:
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			break;
		}
	}
}
