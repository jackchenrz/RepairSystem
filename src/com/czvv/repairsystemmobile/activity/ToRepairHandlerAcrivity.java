package com.czvv.repairsystemmobile.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.czvv.repairsystemmobile.Constants;
import com.czvv.repairsystemmobile.MApplication;
import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.adapter.CommonAdapter;
import com.czvv.repairsystemmobile.base.BaseActivity;
import com.czvv.repairsystemmobile.bean.ToRepairSave;
import com.czvv.repairsystemmobile.bean.ToRepairedBean;
import com.czvv.repairsystemmobile.bean.ToRepairedBean.ToRepair;
import com.czvv.repairsystemmobile.dao.Sys_userDao;
import com.czvv.repairsystemmobile.dao.ToRepairDao;
import com.czvv.repairsystemmobile.utils.GsonUtils;
import com.czvv.repairsystemmobile.utils.HttpUtils;
import com.czvv.repairsystemmobile.utils.HttpUtils.WebServiceCallBack;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.czvv.repairsystemmobile.utils.ToastUtils;
import com.czvv.repairsystemmobile.view.PullFlushListView;
import com.czvv.repairsystemmobile.view.PullFlushListView.OnFlushListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ToRepairHandlerAcrivity extends BaseActivity implements
		OnCheckedChangeListener {

	@ViewInject(R.id.lv_pull)
	PullFlushListView lvPull;
	@ViewInject(R.id.ll_loading)
	LinearLayout llLoading;
	@ViewInject(R.id.progressBar)
	ProgressBar progressBar;
	@ViewInject(R.id.tv_desc_loading)
	TextView tv_desc_loading;
	@ViewInject(R.id.rg_torepair)
	RadioGroup rgTorepair;
	@ViewInject(R.id.rb_no)
	RadioButton rbNo;
	@ViewInject(R.id.rb_yes)
	RadioButton rbYes;
	@ViewInject(R.id.btnLook)
	ImageButton btnLook;

	private String url;
	private SharedPreferences sp;
	private Sys_userDao userDao;
	private ToRepairDao toRepairDao;
	private List<ToRepair> toRepairListNO;
	private List<ToRepair> toRepairListYES;
	private List<ToRepairSave> allToRepairSave;
	 public static ToRepairHandlerAcrivity instance = null;

	private CommonAdapter<ToRepair> adapter;
	private BitmapUtils bitmapUtils;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			rgTorepair.setOnCheckedChangeListener(ToRepairHandlerAcrivity.this);
			if (rbNo.isChecked()) {
				if (toRepairListNO.size() == 0) {
					progressBar.setVisibility(View.GONE);
					tv_desc_loading.setText("没有故障未处理信息");
				} else {
					llLoading.setVisibility(View.GONE);
				}
				setOrUpdateAdapter(toRepairListNO);
			} else if (rbYes.isChecked()) {
				if (toRepairListYES.size() == 0) {
					progressBar.setVisibility(View.GONE);
					tv_desc_loading.setText("没有故障已处理信息");
				} else {
					llLoading.setVisibility(View.GONE);
				}
				setOrUpdateAdapter(toRepairListYES);
			}

			btnLook.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ToRepairHandlerAcrivity.this,
							ToRepairSaveListActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.base_slide_right_in,
							R.anim.base_slide_remain);
				}
			});
		};
	};

	@Override
	public int bindLayout() {
		return R.layout.activity_torepairlist;
	}

	@Override
	public void initView(View view) {
		ViewUtils.inject(this);
		instance = this;
		rgTorepair.check(R.id.rb_no);
		llLoading.setVisibility(View.VISIBLE);
		/**
		 * 下拉刷新
		 */
		lvPull.setOnFlushListener(new OnFlushListener() {

			@Override
			public void onReFlush() {
				llLoading.setVisibility(View.VISIBLE);
				doBusiness(ToRepairHandlerAcrivity.this);
				lvPull.flushFinish();
			}

			@Override
			public void onLoadingMore() {
			}
		});

		lvPull.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (rbNo.isChecked()) {
					ToRepair toRepair = toRepairListNO.get(position - 1);
					Intent intent = new Intent(ToRepairHandlerAcrivity.this,
							ToRepairDetail.class);
					intent.putExtra("toRepair", toRepair);
					intent.putExtra("flag", 1);
					startActivity(intent);
					overridePendingTransition(R.anim.base_slide_right_in,
							R.anim.base_slide_remain);
				} else if (rbYes.isChecked()) {
					ToRepair toRepair = toRepairListYES.get(position - 1);
					Intent intent = new Intent(ToRepairHandlerAcrivity.this,
							ToRepairDetail.class);
					intent.putExtra("toRepair", toRepair);
					intent.putExtra("flag", 0);
					startActivity(intent);
					overridePendingTransition(R.anim.base_slide_right_in,
							R.anim.base_slide_remain);
				}
			}
		});
	}

	@Override
	public void doBusiness(Context mContext) {
		bitmapUtils = new BitmapUtils(mContext);
		userDao = Sys_userDao.getInstance(this);
		toRepairDao = ToRepairDao.getInstance(this);
		sp = getSharedPreferences("serviceInfo", MODE_PRIVATE);
		String serverIP = sp.getString("serverIP", "");
		String serverPort = sp.getString("serverPort", "");
		if (serverIP != null && !"".equals(serverIP) && serverPort != null
				&& !"".equals(serverPort)) {
			url = "http://" + serverIP + ":" + serverPort
					+ Constants.SERVICE_PAGE;
		}

		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("DeptID",
				userDao.getUserInfo(sp.getString("loginName", "")).dept_id);
		if ("Tech".equals(MApplication.mRepairType)) {
			HttpUtils.callService(url, Constants.SERVICE_NAMESPACE,Constants.SERVICE_GETTECHREPAIRINGLIST, properties,new WebServiceCallBack() {

				@Override
				public void onSucced(SoapObject result) {
					if (result != null) {
						String string = result.getProperty(0).toString();
						ToRepairedBean jsonBean = GsonUtils.getJsonBean(string,ToRepairedBean.class);
						toRepairListNO = jsonBean.ds;
						getRepairedList(Constants.SERVICE_GETTECHREPAIREDLIST);
					} else {
						ToastUtils.showToast(ToRepairHandlerAcrivity.this, "联网失败");
						llLoading.setVisibility(View.GONE);
					}
				}

				@Override
				public void onFailure(String result) {
					ToastUtils.showToast(ToRepairHandlerAcrivity.this,"联网失败");
					llLoading.setVisibility(View.GONE);
				}
			});
		} else if ("5T".equals(MApplication.mRepairType)) {
			HttpUtils.callService(url, Constants.SERVICE_NAMESPACE,Constants.SERVICE_GET5TREPAIRINGLIST, properties,new WebServiceCallBack() {

				@Override
				public void onSucced(SoapObject result) {
					if (result != null) {
						String string = result.getProperty(0)
								.toString();
						ToRepairedBean jsonBean = GsonUtils
								.getJsonBean(string,
										ToRepairedBean.class);
						toRepairListNO = jsonBean.ds;
						getRepairedList(Constants.SERVICE_GET5TREPAIREDLIST);
					} else {
						ToastUtils.showToast(
								ToRepairHandlerAcrivity.this, "联网失败");
						llLoading.setVisibility(View.GONE);
					}
				}

				@Override
				public void onFailure(String result) {
					ToastUtils.showToast(ToRepairHandlerAcrivity.this,
							"联网失败");
					llLoading.setVisibility(View.GONE);
				}
			});
		}
	}

	protected void getRepairedList(String methodName) {
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("DeptID",
				userDao.getUserInfo(sp.getString("loginName", "")).dept_id);
		HttpUtils.callService(url, Constants.SERVICE_NAMESPACE, methodName,properties, new WebServiceCallBack() {

			@Override
			public void onSucced(SoapObject result) {
				if (result != null) {
					String string = result.getProperty(0).toString();
					ToRepairedBean jsonBean = GsonUtils.getJsonBean(
							string, ToRepairedBean.class);
					toRepairListYES = jsonBean.ds;
					ThreadUtils.runInBackground(new Runnable() {

						@Override
						public void run() {
							allToRepairSave = toRepairDao.getAllToRepairSave();
							if (null != toRepairListNO&& toRepairListNO.size() != 0) {
								if (null != allToRepairSave) {
									for (ToRepairSave toRepairSave : allToRepairSave) {

										for (Iterator<ToRepair> it = toRepairListNO.iterator(); it.hasNext();) {
											ToRepair s = it.next();
											if (s.RepairID.equals(toRepairSave.RepairID)) {
												it.remove();
											}
										}
									}
								}
							}
							handler.sendEmptyMessage(100);
						}
					});

				} else {
					ToastUtils.showToast(ToRepairHandlerAcrivity.this,"联网失败");
					llLoading.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(String result) {
				ToastUtils.showToast(ToRepairHandlerAcrivity.this,"联网失败");
				llLoading.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void resume() {
		doBusiness(this);
	}

	@Override
	public void destroy() {
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_no:
			rbNo.setTextColor(this.getResources().getColor(R.color.white));
			rbNo.setBackgroundColor(this.getResources().getColor(
					R.color.bottomcolor));
			rbYes.setTextColor(this.getResources()
					.getColor(R.color.bottomcolor));
			rbYes.setBackgroundColor(this.getResources().getColor(
					R.color.darkwhite));
			if (toRepairListNO.size() == 0) {
				llLoading.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				tv_desc_loading.setText("没有故障未处理信息");
			} else {
				llLoading.setVisibility(View.GONE);
			}
			setOrUpdateAdapter(toRepairListNO);
			break;
		case R.id.rb_yes:
			rbNo.setTextColor(this.getResources().getColor(R.color.bottomcolor));
			rbNo.setBackgroundColor(this.getResources().getColor(
					R.color.darkwhite));
			rbYes.setTextColor(this.getResources().getColor(R.color.white));
			rbYes.setBackgroundColor(this.getResources().getColor(
					R.color.bottomcolor));
			if (toRepairListYES.size() == 0) {
				llLoading.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				tv_desc_loading.setText("没有故障已处理信息");
			} else {
				llLoading.setVisibility(View.GONE);
			}
			setOrUpdateAdapter(toRepairListYES);
			break;
		}
	}

	static class ViewHolder {
		@ViewInject(R.id.tv_name)
		TextView tv_name;
		@ViewInject(R.id.tv_department)
		TextView tv_department;
		@ViewInject(R.id.tv_describe)
		TextView tv_describe;
		@ViewInject(R.id.iv_img)
		ImageView ivImg;

		public ViewHolder(View view) {
			ViewUtils.inject(this, view);
		}
	}

	protected void setOrUpdateAdapter(final List<ToRepair> toRepairList) {

			adapter = new CommonAdapter<ToRepair>(toRepairList) {

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View view;
					ViewHolder vh;
					if (convertView == null) {
						view = getLayoutInflater()
								.inflate(R.layout.list_item_repairhandler,
										parent, false);
						vh = new ViewHolder(view);
						view.setTag(vh);
					} else {
						view = convertView;
						vh = (ViewHolder) view.getTag();
					}
					ToRepair toRepair = toRepairList.get(position);
					vh.tv_name.setText("设备名称：" + toRepair.EqptName);
					vh.tv_department.setText("故障发生时间："
							+ toRepair.FaultOccu_Time);
					vh.tv_describe.setText("故障描述：" + toRepair.FaultAppearance);
					System.out.println(toRepair.ImageUrlPath);
					bitmapUtils.display(vh.ivImg, toRepair.ImageUrlPath);
					return view;
				}
			};
			lvPull.setAdapter(adapter);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/* 返回键 */
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ToRepairHandlerAcrivity.this.finish();
			overridePendingTransition(0, R.anim.base_slide_right_out);
		}
		return false;
	}

}
