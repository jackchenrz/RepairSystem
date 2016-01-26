package com.czvv.repairsystemmobile.activity;

import java.lang.ref.WeakReference;

import android.app.Activity;
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
import com.czvv.repairsystemmobile.bean.RepairShow;
import com.czvv.repairsystemmobile.dao.Sys_deptDao;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RepairHandlerDetailActivity extends BaseActivity {
	@ViewInject(R.id.iv_pro)
	ImageView ivPro;
	@ViewInject(R.id.tv_devicename)
	TextView tvDeviceName;
	@ViewInject(R.id.tv_repair)
	TextView tvRepair;
	@ViewInject(R.id.tvDetail)
	TextView tvDetail;
	@ViewInject(R.id.tv_repairuser)
	TextView tv_repairuser;
	@ViewInject(R.id.tv_finishtime)
	TextView tv_finishtime;
	@ViewInject(R.id.tvRepairdept)
	TextView tvRepairdept;
	@ViewInject(R.id.tv_handler)
	TextView tv_handler;
	@ViewInject(R.id.btnBack)
	ImageButton btnBack;

	private RepairShow repairShow;
	private Sys_deptDao deptDao;
	private BitmapUtils bitmapUtils;

	@Override
	public int bindLayout() {
		return R.layout.activity_handlerdetail;
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
		repairShow = (RepairShow) intent.getSerializableExtra("repairShow");	
		bitmapUtils.display(ivPro,Environment.getExternalStorageDirectory() + "/.problems/" + repairShow.imgUrl.split("#")[1]);
		tvDeviceName.setText("设备名称：" + repairShow.EqptName);
		tvRepair.setText("故障发生时间：" + repairShow.FaultOccu_Time);
		tvDetail.setText("故障详情：" + repairShow.FaultAppearance);
		tvRepairdept.setText("维修部门："+ deptDao.getDeptInfo1(repairShow.RepairDeptID).dept_name);
		if(repairShow.RepairUserName == null || "".equals(repairShow.RepairUserName)){
			tv_repairuser.setText("维修人：" + "未维修");
		}else{
			tv_repairuser.setText("维修人：" + repairShow.RepairUserName);
		}
		if(repairShow.RepairFinishTime == null || "".equals(repairShow.RepairFinishTime)){
			tv_finishtime.setText("完成时间：" + "未完成");
		}else{
			tv_finishtime.setText("完成时间：" + repairShow.RepairFinishTime);
		}
		if(repairShow.FaultHandle == null || "".equals(repairShow.FaultHandle)){
			tv_handler.setText("处理信息：" + "正在处理");
		}else{
			tv_handler.setText("处理信息：" + repairShow.FaultHandle);
		}
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void destroy() {
	}

}
