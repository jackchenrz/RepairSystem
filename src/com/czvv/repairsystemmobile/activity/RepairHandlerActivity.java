package com.czvv.repairsystemmobile.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
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
import com.czvv.repairsystemmobile.bean.RepairHandlerBean;
import com.czvv.repairsystemmobile.bean.RepairHandlerBean.RepairHandler;
import com.czvv.repairsystemmobile.bean.RepairInfo;
import com.czvv.repairsystemmobile.bean.RepairShow;
import com.czvv.repairsystemmobile.dao.Reapir_SubmitDao;
import com.czvv.repairsystemmobile.service.Repair_submitService;
import com.czvv.repairsystemmobile.utils.AlertUtils;
import com.czvv.repairsystemmobile.utils.GsonUtils;
import com.czvv.repairsystemmobile.utils.HttpUtil;
import com.czvv.repairsystemmobile.utils.HttpUtils;
import com.czvv.repairsystemmobile.utils.HttpUtils.WebServiceCallBack;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.czvv.repairsystemmobile.utils.ToastUtils;
import com.czvv.repairsystemmobile.view.PullFlushListView;
import com.czvv.repairsystemmobile.view.PullFlushListView.OnFlushListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RepairHandlerActivity extends BaseActivity implements OnCheckedChangeListener, OnClickListener {
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
	@ViewInject(R.id.btnRepair)
	ImageButton btnRepair;
	@ViewInject(R.id.btnUpload)
	Button btnUpload;
	
	private String url;
	private SharedPreferences sp;
	private List<RepairShow> repairList = new ArrayList<RepairShow>();

	private List<RepairInfo> repairInfoList;

	public static RepairHandlerActivity instance = null;
	private CommonAdapter<RepairShow> adapter;
	private CommonAdapter<RepairInfo> adapter1;
	private BitmapUtils bitmapUtils;
	private Repair_submitService repairDao;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			rgTorepair.setOnCheckedChangeListener(RepairHandlerActivity.this);
			if (rbNo.isChecked()) {
				if (repairInfoList.size() == 0) {
					progressBar.setVisibility(View.GONE);
					tv_desc_loading.setText("没有故障未上传信息");
				} else {
					llLoading.setVisibility(View.GONE);
				}
				setOrUpdateAdapter(repairInfoList);
			} else if (rbYes.isChecked()) {
				
				if (repairList.size() == 0) {
					progressBar.setVisibility(View.GONE);
					tv_desc_loading.setText("没有故障已上传信息");
				} else {
					llLoading.setVisibility(View.GONE);
				}
				setRepairHandlerAdapter(repairList);
				
			}
		};
	};
	private Dialog loadingDialog;
	

	@Override
	public int bindLayout() {
		return R.layout.activity_repairlist;
	}
	
	/**
	 * 设置报修处理页面
	 * @param repairHandlerList
	 */

	protected void setRepairHandlerAdapter(
			final List<RepairShow> repairHandlerList) {
		lvPull.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				return true;
			}
		});
		adapter = new CommonAdapter<RepairShow>(repairHandlerList) {

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
				RepairShow repairShow = repairHandlerList.get(position);
				vh.tv_name.setText("设备名称：" + repairShow.EqptName);
				vh.tv_department
						.setText("故障发生时间：" + repairShow.FaultOccu_Time);
				vh.tv_describe.setText("故障描述：" + repairShow.FaultAppearance);
				bitmapUtils.display(vh.ivImg,Environment.getExternalStorageDirectory() + "/.problems/" + repairShow.imgUrl.split("#")[1]);
				return view;
			}
		};
		lvPull.setAdapter(adapter);
	}

	@Override
	public void initView(View view) {
		ViewUtils.inject(this);
		instance = this;
		rgTorepair.check(R.id.rb_no);
		llLoading.setVisibility(View.VISIBLE);
		btnRepair.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(RepairHandlerActivity.this, RepairInputActivity.class));
				overridePendingTransition(R.anim.base_slide_right_in,R.anim.base_slide_remain);				
			}
		});
		
		lvPull.setOnFlushListener(new OnFlushListener() {
			
			@Override
			public void onReFlush() {
				llLoading.setVisibility(View.VISIBLE);
				doBusiness(RepairHandlerActivity.this);
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
					RepairInfo repairInfo = repairInfoList.get(position - 1);
					Intent intent = new Intent(RepairHandlerActivity.this,
							RepairDetailActivity.class);
					intent.putExtra("repairInfo", repairInfo);
					startActivity(intent);
					overridePendingTransition(R.anim.base_slide_right_in,
							R.anim.base_slide_remain);
				} else if (rbYes.isChecked()) {
					RepairShow repairShow = repairList.get(position - 1);
					Intent intent = new Intent(RepairHandlerActivity.this,
							RepairHandlerDetailActivity.class);
					intent.putExtra("repairShow", repairShow);
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
		repairDao = Reapir_SubmitDao.getInstance(this); 
		sp = getSharedPreferences("serviceInfo", MODE_PRIVATE);
		url = MApplication.gainData("url").toString();
		HttpUtils.callService(url, Constants.SERVICE_NAMESPACE,Constants.SERVICE_GETREPAIR_HANDLE, null,new WebServiceCallBack() {

			@Override
			public void onSucced(SoapObject result) {
				if (result != null) {
					String string = result.getProperty(0).toString();
					
					RepairHandlerBean jsonBean = GsonUtils.getJsonBean(string,RepairHandlerBean.class);
					final List<RepairHandler> repairHandlerList = jsonBean.ds;
					
					ThreadUtils.runInBackground(new Runnable() {
						@Override
						public void run() {
							List<RepairInfo> allRepairSubmit = repairDao.getAllRepairSubmit();
							repairInfoList = repairDao.getRepairInfo(1);
							System.out.println(repairInfoList.size());
							repairList.clear();
							for (RepairHandler repairHandler : repairHandlerList) {
								for (RepairInfo repairInfo : allRepairSubmit) {

									if (repairHandler.RepairID
											.equals(repairInfo.RepairID)
											&& repairHandler.RepairUserName != null
											&& repairHandler.RepairFinishTime != null
											&& repairHandler.FaultHandle != null) {
										RepairShow repairShow = new RepairShow();
										repairShow.EqptName = repairInfo.EqptName;
										repairShow.FaultOccu_Time = repairInfo.FaultOccu_Time;
										repairShow.FaultAppearance = repairInfo.FaultAppearance;
										repairShow.RepairUserName = repairHandler.RepairUserName;
										repairShow.RepairFinishTime = repairHandler.RepairFinishTime;
										repairShow.FaultHandle = repairHandler.FaultHandle;
										repairShow.imgUrl = sp.getString(
												repairInfo.RepairID, "");
										repairShow.RepairDeptID = repairHandler.RepairDeptID;

										repairList.add(repairShow);
										repairShow = null;
									}
								}
							}
							
							handler.sendEmptyMessage(100);
						}
					});
					
				} else {
					ToastUtils.showToast(RepairHandlerActivity.this, "联网失败");
					llLoading.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(String result) {
				ToastUtils.showToast(RepairHandlerActivity.this,"联网失败");
				llLoading.setVisibility(View.GONE);
			}
		});
	}
	

	@Override
	public void resume() {
		doBusiness(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_no:
			btnUpload.setVisibility(View.VISIBLE);
			btnUpload.setOnClickListener(this);
			rbNo.setTextColor(this.getResources().getColor(R.color.white));
			rbNo.setBackgroundColor(this.getResources().getColor(
					R.color.bottomcolor));
			rbYes.setTextColor(this.getResources()
					.getColor(R.color.bottomcolor));
			rbYes.setBackgroundColor(this.getResources().getColor(
					R.color.darkwhite));
			if (repairInfoList.size() == 0) {
				llLoading.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				tv_desc_loading.setText("没有故障未上传信息");
			} else {
				llLoading.setVisibility(View.GONE);
			}
			setOrUpdateAdapter(repairInfoList);
			break;
		case R.id.rb_yes:
			btnUpload.setVisibility(View.GONE);
			rbNo.setTextColor(this.getResources().getColor(R.color.bottomcolor));
			rbNo.setBackgroundColor(this.getResources().getColor(
					R.color.darkwhite));
			rbYes.setTextColor(this.getResources().getColor(R.color.white));
			rbYes.setBackgroundColor(this.getResources().getColor(
					R.color.bottomcolor));
			if (repairList.size() == 0) {
				llLoading.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				tv_desc_loading.setText("没有故障已上传信息");
			} else {
				llLoading.setVisibility(View.GONE);
			}
			setRepairHandlerAdapter(repairList);
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
	protected void setOrUpdateAdapter(final List<RepairInfo> repairInfoList) {
		lvPull.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				showAlertDialog(position - 1);
				return true;
			}
		});

		adapter1 = new CommonAdapter<RepairInfo>(repairInfoList) {

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
				RepairInfo toRepair = repairInfoList.get(position);
				vh.tv_name.setText("设备名称：" + toRepair.EqptName);
				vh.tv_department.setText("故障发生时间：" + toRepair.FaultOccu_Time);
				vh.tv_describe.setText("故障描述：" + toRepair.FaultAppearance);
				
				bitmapUtils.display(vh.ivImg,Environment.getExternalStorageDirectory() + "/.problems/" + toRepair.ImageUrl.split("#")[1]);
				return view;
			}
		};
		lvPull.setAdapter(adapter1);
	}
	@Override
	public void destroy() {

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/* 返回键 */
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			RepairHandlerActivity.this.finish();
			overridePendingTransition(0, R.anim.base_slide_right_out);
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnUpload:
			if (null == repairInfoList || repairInfoList.size() == 0) {
				ToastUtils.showToast(this, "请保存报修信息");
				return;
			}
			uploadInfo();
			break;
		}
	}
	
	private void uploadInfo() {
		loadingDialog = AlertUtils.createLoadingDialog(RepairHandlerActivity.this, "正在上传，请稍后...");
		loadingDialog.show();
		ThreadUtils.runInBackground(new Runnable() {

			@Override
			public void run() {
				if(repairInfoList.size() != 0){
					try {
						String jsonArr = "{" + "\"" + "ds" + "\"" + ":[";
						int count = 0;
						for (RepairInfo repairInfo : repairInfoList) {
							sp.edit().putString(repairInfo.RepairID,
									repairInfo.ImageUrl).commit();
							String str = repairInfo.ImageUrl.split("#")[1]
									.split("\\.")[0];
							HttpUtil.Uploadimg(Constants.FILEPATH + str
									+ ".jpg", url);
							repairDao.editRepairInfo(2, new SimpleDateFormat(
									"yy-MM-dd HH:mm:ss").format(new Date()),
									str, repairInfo.RepairID);
							repairInfo.IsUpload = 2;
							repairInfo.FaultReceiveTime = new SimpleDateFormat(
									"yy-MM-dd HH:mm:ss").format(new Date());
							repairInfo.ImageUrl = str;

							jsonArr += GsonUtils.toJsonArray(repairInfo);
							count++;

							if (count != repairInfoList.size()) {
								jsonArr += ",";
							}
						}
						jsonArr += "]}";

						HttpUtil.toJsonArray(jsonArr, url,
								Constants.SERVICE_RETURNREPAIR_SUBMIT);
						if (count == repairInfoList.size()) {
							ThreadUtils.runInMainThread(new Runnable() {

								@Override
								public void run() {
									repairInfoList.clear();
									adapter1.notifyDataSetChanged();
									ToastUtils.showToast(RepairHandlerActivity.this,
											"上传成功");
									loadingDialog.dismiss();
								}
							});
						}
					} catch (Exception e) {
						ThreadUtils.runInMainThread(new Runnable() {

							@Override
							public void run() {
								ToastUtils.showToast(RepairHandlerActivity.this,
										"上传失败,请检查网络连接");
								loadingDialog.dismiss();
							}
						});
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private void showAlertDialog(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("是否删除？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				repairDao.delRepairInfo(repairInfoList.get(position).RepairID);
				repairInfoList.remove(position);
				adapter1.notifyDataSetChanged();
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

}
