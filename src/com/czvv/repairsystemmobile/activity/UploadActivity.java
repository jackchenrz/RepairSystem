package com.czvv.repairsystemmobile.activity;

import java.io.FileNotFoundException;
import java.io.Flushable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android_serialport_api.sample.Application;

import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.adapter.CommonAdapter;
import com.czvv.repairsystemmobile.bean.RepairInfo;
import com.czvv.repairsystemmobile.dao.Reapir_SubmitDao;
import com.czvv.repairsystemmobile.utils.Constants;
import com.czvv.repairsystemmobile.utils.GsonUtils;
import com.czvv.repairsystemmobile.utils.HttpUtil;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.czvv.repairsystemmobile.utils.ToastUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class UploadActivity extends RepairBaseActivity implements
		OnClickListener {
	@ViewInject(R.id.lv_content)
	ListView lvContent;
	@ViewInject(R.id.btn_repair)
	Button btnRepair;
	@ViewInject(R.id.btn_upload)
	Button btnUpload;
	@ViewInject(R.id.btnBack)
	Button btnBack;
	@ViewInject(R.id.ll_loading)
	LinearLayout llLoading;
	@ViewInject(R.id.progressBar)
	ProgressBar progressBar;
	@ViewInject(R.id.tv_desc_loading)
	TextView tv_desc_loading;

	private ProgressDialog progressDialog;
	private Reapir_SubmitDao repairDao;
	private List<RepairInfo> repairInfoList;
	private String url;
	private CommonAdapter<RepairInfo> adapter;
	private Bitmap bmp;
	private SharedPreferences sp;
	private Editor edit;
	
	private final int FLUSH_UI = 100;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FLUSH_UI:
				edit.putInt("size",repairInfoList.size()).commit();
				adapter = new CommonAdapter<RepairInfo>(repairInfoList) {

					@Override
					public View getView(final int position, View convertView,
							ViewGroup parent) {
						View view;
						ViewHolder vh;
						if (convertView == null) {
							view = getLayoutInflater().inflate(
									R.layout.list_item_problem, parent,false);
							vh = new ViewHolder(view);
							view.setTag(vh);
						} else {
							view = convertView;
							vh = (ViewHolder) view.getTag();
						}
						final RepairInfo pro = repairInfoList.get(position);
						vh.tv_name.setText("设备名称：" + pro.EqptName);
						vh.tv_department.setText("故障发生时间：" + pro.FaultOccu_Time);
						vh.tv_describe.setText("故障描述：" + pro.FaultAppearance);
						String uriString = pro.ImageUrl.split("#")[0];
						Uri imageUri = Uri.parse(uriString);
						try {
							BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();  
						      bitmapOptions.inSampleSize = 10;  
							bmp = BitmapFactory.decodeStream(UploadActivity.this
									.getContentResolver().openInputStream(imageUri) ,null , bitmapOptions);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						vh.ivImg.setImageBitmap(bmp);
						return view;
					}
				};
				lvContent.setAdapter(adapter);
				llLoading.setVisibility(View.GONE);
				break;

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);
		ViewUtils.inject(this);
		sp = getSharedPreferences("serviceInfo", MODE_PRIVATE);
		edit = sp.edit();
		String serverIP = sp.getString("serverIP", "");
		String serverPort = sp.getString("serverPort", "");
		if (serverIP != null && !"".equals(serverIP) && serverPort != null
				&& !"".equals(serverPort)) {
			url = "http://" + serverIP + ":" + serverPort
					+ Constants.SERVICE_PAGE;
		}
		repairDao = Reapir_SubmitDao.getInstance(this);
		btnUpload.setVisibility(View.VISIBLE);
		btnRepair.setVisibility(View.GONE);
		Intent intent = getIntent();
		initView();
		fillData();

	}

	private void initView() {
		llLoading.setVisibility(View.VISIBLE);
		lvContent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				RepairInfo repairInfo = repairInfoList.get(position);
				Intent intent = new Intent(UploadActivity.this,
						DetailActivity.class);
				intent.putExtra("repairInfo", repairInfo);
				startActivity(intent);
			}
		});

		lvContent.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				showAlertDialog(position);
				return true;
			}
		});

		btnUpload.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_upload:
			if (repairInfoList.size() == 0) {
				ToastUtils.showToast(this, "请保存报修信息");
				return;
			}
			uploadInfo();

			break;
		case R.id.btnBack:
			startActivity(new Intent(this, RepairInputActivity.class));
			finish();
			break;
		}
	}

	private void uploadInfo() {
		showProgressDialog("正在上传，请稍后...");
		ThreadUtils.runInBackground(new Runnable() {

			@Override
			public void run() {
				try {
					String jsonArr = "{" + "\"" + "ds" + "\"" + ":[";
					int count = 0;
					for (RepairInfo repairInfo : repairInfoList) {
						edit.putString(repairInfo.RepairID,
								repairInfo.ImageUrl.split("#")[0]).commit();
						System.out.println(repairInfo.ImageUrl);
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
								edit.putInt("size", 0).commit();
								repairInfoList.clear();
								adapter.notifyDataSetChanged();
								ToastUtils.showToast(UploadActivity.this,
										"上传成功");
								progressDialog.dismiss();
							}
						});
					}
				} catch (Exception e) {
					ThreadUtils.runInMainThread(new Runnable() {

						@Override
						public void run() {
							ToastUtils.showToast(UploadActivity.this,
									"上传失败,请检查网络连接");
							progressDialog.dismiss();
						}
					});
					e.printStackTrace();
				}
			}
		});
	}

	private void fillData() {

		ThreadUtils.runInBackground(new Runnable() {

			@Override
			public void run() {
				repairInfoList = repairDao.getRepairInfo(1);
				handler.sendEmptyMessage(FLUSH_UI);
			}
		});
		
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

	private void showAlertDialog(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("是否删除？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				repairDao.delRepairInfo(repairInfoList.get(position).RepairID);
				repairInfoList.remove(position);
				adapter.notifyDataSetChanged();
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != bmp && !bmp.isRecycled()) {
			bmp.recycle();
			bmp = null;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != bmp && !bmp.isRecycled()) {
			bmp.recycle();
			bmp = null;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
	}

	private void showProgressDialog(String msg) {
		if (null == progressDialog) {
			progressDialog = new ProgressDialog(this);
		}
		progressDialog.setMessage(msg);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	}
}
