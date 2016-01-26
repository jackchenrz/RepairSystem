package com.czvv.repairsystemmobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.czvv.repairsystemmobile.Constants;
import com.czvv.repairsystemmobile.MApplication;
import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.adapter.CommonAdapter;
import com.czvv.repairsystemmobile.base.BaseActivity;
import com.czvv.repairsystemmobile.bean.DeptInfoBean.DeptInfo;
import com.czvv.repairsystemmobile.bean.EqptInfoBean.EqptInfo;
import com.czvv.repairsystemmobile.bean.FiveTEqptInfoBean.FiveTEqpt;
import com.czvv.repairsystemmobile.dao.Eqpt_InfoDao;
import com.czvv.repairsystemmobile.dao.FiveT_InfoDao;
import com.czvv.repairsystemmobile.dao.Sys_deptDao;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SearchActivity extends BaseActivity {

	@ViewInject(R.id.tv_search)
	TextView tvSearch;
	@ViewInject(R.id.btnBack)
	ImageButton btnBack;
	@ViewInject(R.id.et_search)
	AutoCompleteTextView etSearch;
	@ViewInject(R.id.lv_search)
	ListView lvSearch;
	@ViewInject(R.id.tv_title)
	TextView tv_title;
	@ViewInject(R.id.btn_ok)
	Button btnOk;

	private Eqpt_InfoDao eqptDao;
	private FiveT_InfoDao fiveTDao;
	private Sys_deptDao deptDao;

	private List<EqptInfo> allEqptInfoList;
	private List<FiveTEqpt> allFiveTEqptList;
	private List<DeptInfo> allDeptList;

	private List<String> list = new ArrayList<String>();
	private final int FILL_DEPT = 100;
	private final int FILL_TECH = 101;
	private final int FILL_5T = 102;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FILL_DEPT:
				if (allDeptList.size() != 0) {

					lvSearch.setAdapter(new CommonAdapter<DeptInfo>(allDeptList) {

						@Override
						public View getView(int position, View convertView,
								ViewGroup parent) {

							TextView view;
							if (convertView == null) {
								view = new TextView(SearchActivity.this);
							} else {
								view = (TextView) convertView;
							}
							view.setText(allDeptList.get(position).dept_name);
							view.setTextSize(18);
							return view;
						}
					});
				}
				break;
			case FILL_TECH:
				if (allEqptInfoList.size() != 0) {
					tv_title.setText("机械设备");

					lvSearch.setAdapter(new CommonAdapter<EqptInfo>(allEqptInfoList) {

						@Override
						public View getView(int position, View convertView,
								ViewGroup parent) {

							TextView view;
							if (convertView == null) {
								view = new TextView(SearchActivity.this);
							} else {
								view = (TextView) convertView;
							}
							view.setText(allEqptInfoList.get(position).EqptName);
							view.setTextSize(18);
							return view;
						}
					});
				}
				break;
			case FILL_5T:
				if (allFiveTEqptList.size() != 0) {

					tv_title.setText("行安设备");
					lvSearch.setAdapter(new CommonAdapter<FiveTEqpt>(allFiveTEqptList) {

						@Override
						public View getView(int position, View convertView,
								ViewGroup parent) {
							TextView view;
							if (convertView == null) {
								view = new TextView(SearchActivity.this);
							} else {
								view = (TextView) convertView;
							}
							view.setText(allFiveTEqptList.get(position).EqptAddress);
							view.setTextSize(18);
							return view;
						}
					});
				}
				break;
			}
		};
	};


	private void init(final int flag) {
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String searchContent = etSearch.getText().toString().trim();
				if (flag == 0 && MApplication.mRepairType.equals("Tech")) {
					Intent intent1 = new Intent(SearchActivity.this,
							RepairInputActivity.class);
					intent1.putExtra("devicename", searchContent);
					setResult(Constants.DEVICENAME, intent1);
					finish();
					overridePendingTransition(0, R.anim.base_slide_right_out);

				} else if (flag == 0 && MApplication.mRepairType.equals("5T")) {
					Intent intent1 = new Intent(SearchActivity.this,
							RepairInputActivity.class);
					intent1.putExtra("devicename", searchContent);
					setResult(Constants.DEVICENAME, intent1);
					finish();
					overridePendingTransition(0, R.anim.base_slide_right_out);
				} else if (flag == 1) {
					Intent intent1 = new Intent(SearchActivity.this,
							RepairInputActivity.class);
					intent1.putExtra("repairdepartment", searchContent);
					setResult(Constants.REPAIRDEPARTMENTLIST, intent1);
					finish();
					overridePendingTransition(0, R.anim.base_slide_right_out);
				}
			}
		});

		if (flag == 0) {
			tvSearch.setText("设备名称 ：");
			fillDevNameData();
		}

		if (flag == 1) {
			tv_title.setText("维修部门");
			tvSearch.setText("维修部门 ：");
			fillDeptData();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.search_item, list);
		etSearch.setThreshold(1);
		etSearch.setAdapter(adapter);

		lvSearch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (flag == 0 && MApplication.mRepairType.equals("Tech")) {
					etSearch.setText(allEqptInfoList.get(position).EqptName);
				} else if (flag == 0 && MApplication.mRepairType.equals("5T")) {
					etSearch.setText(allFiveTEqptList.get(position).EqptAddress);
				} else if (flag == 1) {
					etSearch.setText(allDeptList.get(position).dept_name);
				}
			}
		});
	}

	private void fillDeptData() {
		ThreadUtils.runInBackground(new Runnable() {

			@Override
			public void run() {
				allDeptList = deptDao.getAllDeptList(1);
				list.clear();
				for (DeptInfo deptInfo : allDeptList) {
					list.add(deptInfo.dept_name);
				}
				
				handler.sendEmptyMessage(FILL_DEPT);
			}
		});
	}

	private void fillDevNameData() {
		ThreadUtils.runInBackground(new Runnable() {

			@Override
			public void run() {
				allEqptInfoList = eqptDao.getAllEqptInfoList();

				allFiveTEqptList = fiveTDao.getAllFiveTEqptList();

				if (MApplication.mRepairType.equals("Tech")) {
					list.clear();
					for (EqptInfo eqptInfo : allEqptInfoList) {
						list.add(eqptInfo.EqptName);
					}
					handler.sendEmptyMessage(FILL_TECH);
				} else if (MApplication.mRepairType.equals("5T")) {
					list.clear();
					for (FiveTEqpt fiveTEqpt : allFiveTEqptList) {
						list.add(fiveTEqpt.EqptAddress);
					}
					handler.sendEmptyMessage(FILL_5T);
				}
			}
		});

	}

	@Override
	public int bindLayout() {
		return R.layout.activity_serach;
	}

	@Override
	public void initView(View view) {
		ViewUtils.inject(this);		
	}

	@Override
	public void doBusiness(Context mContext) {
		eqptDao = Eqpt_InfoDao.getInstance(this);
		fiveTDao = FiveT_InfoDao.getInstance(this);
		deptDao = Sys_deptDao.getInstance(this);

		Intent intent = getIntent();
		final int flag = intent.getIntExtra("flag", 0);
		init(flag);		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void destroy() {
		
	}
}
