package com.czvv.repairsystemmobile.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.base.BaseActivity;
import com.czvv.repairsystemmobile.bean.ToRepairSave;
import com.czvv.repairsystemmobile.bean.DeptInfoBean.DeptInfo;
import com.czvv.repairsystemmobile.bean.ToRepairedBean.ToRepair;
import com.czvv.repairsystemmobile.dao.Sys_deptDao;
import com.czvv.repairsystemmobile.dao.Sys_userDao;
import com.czvv.repairsystemmobile.dao.ToRepairDao;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ToRepairSaveDetail extends BaseActivity {
	
	@ViewInject(R.id.iv_pro)
	ImageView iv_pro;
	@ViewInject(R.id.tv_devicename)
	TextView tv_devicename;
	@ViewInject(R.id.tv_repair_workshop)
	TextView tv_repair_workshop;
	@ViewInject(R.id.tv_repair_people)
	TextView tv_repair_people;
	@ViewInject(R.id.tv_torepairtime)
	TextView tv_torepairtime;
	@ViewInject(R.id.tv_repair_category)
	TextView tv_repair_category;
	@ViewInject(R.id.tv_breakdown)
	TextView tv_breakdown;
	@ViewInject(R.id.tv_repair_type)
	TextView tv_repair_type;
	
	
	private ToRepairDao toRepairDao;
	private List<ToRepairSave> allToRepairSave;
	private int index;
	private final int FLUSH_UI = 100;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			ToRepairSave toRepairSave = allToRepairSave.get(index);
			bitmapUtils.display(iv_pro, toRepair.ImageUrlPath);
			tv_devicename.setText("设备名称：" + toRepair.dept_name);
			tv_repair_workshop.setText("维修部门：" + deptInfo.dept_name);
			tv_repair_people.setText("维修人员：" + toRepairSave.RepairUserName);
			tv_torepairtime.setText("维修完成时间："+ toRepairSave.RepairFinishTime);
			tv_repair_type.setText("故障类型："+ toRepairSave.FaultType);
			tv_repair_category.setText("责任分类："+ toRepairSave.FaultReason);
			tv_breakdown.setText("故障分析：" + toRepairSave.FaultHandle);
		};
	};
	private ToRepair toRepair;
	private BitmapUtils bitmapUtils;
	private Sys_deptDao sys_deptDao;
	private Sys_userDao sys_userDao;
	private SharedPreferences sp;
	private DeptInfo deptInfo;
	@Override
	public int bindLayout() {
		return R.layout.activity_torepair_save_detail;
	}

	@Override
	public void initView(View view) {
		ViewUtils.inject(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		bitmapUtils = new BitmapUtils(mContext);
		sys_userDao = Sys_userDao.getInstance(mContext);
		toRepairDao = ToRepairDao.getInstance(mContext);
		sys_deptDao = Sys_deptDao.getInstance(mContext);
		sp = getSharedPreferences("serviceInfo", MODE_PRIVATE);
		Intent intent = getIntent();
		toRepair = (ToRepair)intent.getSerializableExtra("toRepair");
		ThreadUtils.runInBackground(new Runnable() {
			
			@Override
			public void run() {
				deptInfo = sys_deptDao.getDeptInfo1(sys_userDao.getUserInfo(sp.getString("loginName", "")).dept_id);
				allToRepairSave = toRepairDao.getAllToRepairSave();
				for (int i = 0; i < allToRepairSave.size(); i++) {
					if(toRepair.RepairID.equals(allToRepairSave.get(i).RepairID)){
						index = i;
						handler.sendEmptyMessage(FLUSH_UI);
					}
				}
			}
		});

	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {

	}

}
