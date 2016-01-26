package com.czvv.repairsystemmobile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.base.BaseActivity;
import com.czvv.repairsystemmobile.bean.RepairInfo;
import com.czvv.repairsystemmobile.dao.Sys_deptDao;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RepairDetailActivity extends BaseActivity {

	@ViewInject(R.id.iv_pro)
	ImageView ivPro;
	@ViewInject(R.id.tv_devicename)
	TextView tvDeviceName;
	@ViewInject(R.id.tv_repair)
	TextView tvRepair;
	@ViewInject(R.id.tv_detail)
	TextView tvDetail;
	@ViewInject(R.id.tv_repaireqpt)
	TextView tv_repaireqpt;
	@ViewInject(R.id.btnBack)
	ImageButton btnBack;
	private RepairInfo repairInfo;
	private Sys_deptDao deptDao;
	private BitmapUtils bitmapUtils;

	@Override
	public int bindLayout() {
		return R.layout.activity_detail;
	}

	@Override
	public void initView(View view) {
		ViewUtils.inject(this);	
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, R.anim.base_slide_right_out);
			}
		});
	}

	@Override
	public void doBusiness(Context mContext) {
		bitmapUtils = new BitmapUtils(mContext);
		deptDao = Sys_deptDao.getInstance(this);

		Intent intent = getIntent();
		repairInfo = (RepairInfo) intent.getSerializableExtra("repairInfo");
		bitmapUtils.display(ivPro,Environment.getExternalStorageDirectory() + "/.problems/" + repairInfo.ImageUrl.split("#")[1]);
		tvDeviceName.setText("设备名称：" + repairInfo.EqptName);
		tvRepair.setText("故障发生时间：" + repairInfo.FaultOccu_Time);
		tvDetail.setText("故障详情：" + repairInfo.FaultAppearance);
		tv_repaireqpt.setText("维修部门："+ deptDao.getDeptInfo1(repairInfo.RepairDeptID).dept_name);
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void destroy() {
	}

}
