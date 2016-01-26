package com.czvv.repairsystemmobile.activity;

import android.content.Context;
import android.os.SystemClock;
import android.view.View;

import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class AboutActivity extends BaseActivity {

	@Override
	public int bindLayout() {
		return R.layout.activity_about;
	}

	@Override
	public void initView(View view) {
		
		ViewUtils.inject(this);

	}
	
	@OnClick(R.id.btn_click)
	public void onClick(){
		 SystemClock.setCurrentTimeMillis(5435454L);
	}
	
	

	@Override
	public void doBusiness(Context mContext) {

	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {

	}

}
