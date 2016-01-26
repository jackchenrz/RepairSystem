package com.czvv.repairsystemmobile.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.czvv.repairsystemmobile.MApplication;
import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.base.BaseActivity;
import com.czvv.repairsystemmobile.bean.ToRepairedBean.ToRepair;
import com.czvv.repairsystemmobile.dao.FiveT_InfoDao;
import com.czvv.repairsystemmobile.dao.TechEqptDao;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ToRepairDetail extends BaseActivity {

	@ViewInject(R.id.iv_pro)
	ImageView ivPro;
	@ViewInject(R.id.tv_devicename)
	TextView tvDevicename;
	@ViewInject(R.id.tv_deviceaddress)
	TextView tvDeviceaddress;
	@ViewInject(R.id.tv_useteam)
	TextView tvUseteam;
	@ViewInject(R.id.tv_time)
	TextView tvTime;
	@ViewInject(R.id.tv_problem)
	TextView tvProblem;
	@ViewInject(R.id.tv_status)
	TextView tvStatus;
	@ViewInject(R.id.tv_finishtime)
	TextView tvFinishtime;
	@ViewInject(R.id.tv_deptname)
	TextView tvDeptname;
	@ViewInject(R.id.btn_torepair)
	Button btnTorepair;
	private ToRepair toRepair;
	private BitmapUtils bitmapUtils;
	
	private TechEqptDao techEqptDao;
	private FiveT_InfoDao FiveTInfoDao;
	
	@Override
	public int bindLayout() {
		return R.layout.activity_torepair_detail;
	}

	@Override
	public void initView(View view) {
		ViewUtils.inject(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		bitmapUtils = new BitmapUtils(mContext);
		techEqptDao = TechEqptDao.getInstance(mContext);
		FiveTInfoDao = FiveT_InfoDao.getInstance(mContext);
		
		Intent intent = getIntent();
		toRepair = (ToRepair) intent.getSerializableExtra("toRepair");
		int flag = intent.getIntExtra("flag", 0);
		if(flag == 1){
			btnTorepair.setVisibility(View.VISIBLE);
			btnTorepair.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ToRepairDetail.this,ToRepairInputActivity.class);
					intent.putExtra("toRepair", toRepair);
					startActivity(intent);
					ToRepairDetail.this.finish();
					overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
				}
			});
		}else{
			btnTorepair.setVisibility(View.GONE);
		}
		bitmapUtils.display(ivPro,toRepair.ImageUrlPath);
		tvDevicename.setText("设备名称：" + toRepair.EqptName);
		
		
		if("Tech".equals(MApplication.mRepairType)){
			tvDeviceaddress.setText("设备地点：" + techEqptDao.getTechEqpt(toRepair.EqptID).EqptAddress);
		}else{
			tvDeviceaddress.setText("设备地点：" + FiveTInfoDao.getFiveTEqpt1(toRepair.EqptID).EqptAddress);
		}
		
		tvUseteam.setText("使用部门：" + toRepair.dept_name);
		tvTime.setText("故障发生时间：" + toRepair.FaultOccu_Time);
		tvProblem.setText("故障现象描述：" + toRepair.FaultAppearance);
		tvStatus.setText("故障状态 ："+toRepair.FaultStatus);
		tvFinishtime.setText("处理完成时间 ："+toRepair.RepairFinishTime);
		tvDeptname.setText("维修部门："+toRepair.repair_dept_name);
	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {

	}

}
