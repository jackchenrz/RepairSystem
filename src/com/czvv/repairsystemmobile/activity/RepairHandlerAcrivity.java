package com.czvv.repairsystemmobile.activity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android_serialport_api.sample.Application;

import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.adapter.CommonAdapter;
import com.czvv.repairsystemmobile.bean.RepairHandlerBean;
import com.czvv.repairsystemmobile.bean.RepairHandlerBean.RepairHandler;
import com.czvv.repairsystemmobile.bean.RepairInfo;
import com.czvv.repairsystemmobile.bean.RepairShow;
import com.czvv.repairsystemmobile.dao.Reapir_SubmitDao;
import com.czvv.repairsystemmobile.dao.Sys_deptDao;
import com.czvv.repairsystemmobile.utils.Constants;
import com.czvv.repairsystemmobile.utils.GsonUtils;
import com.czvv.repairsystemmobile.utils.HttpUtil;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.czvv.repairsystemmobile.utils.ToastUtils;
import com.czvv.repairsystemmobile.view.PullFlushListView;
import com.czvv.repairsystemmobile.view.PullFlushListView.OnFlushListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RepairHandlerAcrivity extends RepairBaseActivity implements OnClickListener {

	@ViewInject(R.id.lv_content)
	ListView lvContent;
	@ViewInject(R.id.lv_pull)
	PullFlushListView lvPull;
	@ViewInject(R.id.btn_repair)
	Button btnRepair;
	@ViewInject(R.id.btn_upload)
	Button btnUpload;
	@ViewInject(R.id.btnBack)
	Button btnBack;
	@ViewInject(R.id.tv_title)
	TextView tvTitle;
	@ViewInject(R.id.ll_loading)
	LinearLayout llLoading;
	@ViewInject(R.id.progressBar)
	ProgressBar progressBar;
	@ViewInject(R.id.tv_desc_loading)
	TextView tv_desc_loading;
	@ViewInject(R.id.btn_up)
	Button btnUp;
	
	private SharedPreferences sp;
	private String url;
	private Reapir_SubmitDao submitDao;
	private Bitmap bmp;
	private final int FLUSH_UI = 100;
	private final int SAVE_INFO = 101;
	private CommonAdapter<RepairShow> adapter;
	
	private List<RepairShow> repairList = new ArrayList<RepairShow>();
	private Handler handler = new Handler(){

		public void handleMessage(Message msg) {
			final String msgObj = (String) msg.obj;
			
				switch (msg.what) {
				case SAVE_INFO:
					saveRepairHandler(msgObj);
					break;
				case FLUSH_UI:
					if(repairList.size() == 0){
						tv_desc_loading.setText("没有设备处理信息...");
						progressBar.setVisibility(View.GONE);
						return;
					}
					llLoading.setVisibility(View.GONE);
					adapter = new CommonAdapter<RepairShow>(repairList) {

						@Override
						public View getView(int position, View convertView, ViewGroup parent) {
							View view;
							ViewHolder vh;
							if(convertView == null){
								view = getLayoutInflater().inflate(R.layout.list_item_repairhandler, parent,false);
								vh = new ViewHolder(view);
								view.setTag(vh);
							}else{
								view = convertView;
								vh = (ViewHolder) view.getTag();
							}
						    RepairShow pro = repairList.get(position);
							vh.tv_name.setText("设备名称：" + pro.EqptName);
							vh.tv_department.setText("故障发生时间：" + pro.FaultOccu_Time);
							vh.tv_describe.setText("故障描述：" + pro.FaultAppearance);
							Uri imageUri = Uri.parse(pro.imgUrl);
							try {
								BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();  
							      bitmapOptions.inSampleSize = 10; 
								bmp = BitmapFactory.decodeStream(RepairHandlerAcrivity.this.getContentResolver().openInputStream(imageUri), null , bitmapOptions);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
							vh.ivImg.setImageBitmap(bmp);
							return view;
						}
					};
					lvPull.setAdapter(adapter);
					break;

				}
			
			}
		};
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);
		ViewUtils.inject(this);
		submitDao = Reapir_SubmitDao.getInstance(this);
		sp = getSharedPreferences("serviceInfo", MODE_PRIVATE);
		String serverIP = sp.getString("serverIP","");
		String serverPort = sp.getString("serverPort","");
		if(serverIP != null && !"".equals(serverIP) && serverPort != null && !"".equals(serverPort)){
			url = "http://" + serverIP + ":" + serverPort + Constants.SERVICE_PAGE;
		}
		
		initView();
		fillData();
	}
	
	private void initView() {
		btnUpload.setVisibility(View.GONE);
		btnRepair.setVisibility(View.VISIBLE);
		btnRepair.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		lvPull.setVisibility(View.VISIBLE);
		lvContent.setVisibility(View.GONE);
		llLoading.setVisibility(View.VISIBLE);
		tvTitle.setText("设备报修单");
		btnUp.setVisibility(View.VISIBLE);
		btnUp.setOnClickListener(this);
		/**
		 * 下拉刷新
		 */
		lvPull.setOnFlushListener(new OnFlushListener() {
			
			@Override
			public void onReFlush() {
				if (null != bmp && !bmp.isRecycled()){
					bmp.recycle();
					bmp = null;
					}
				llLoading.setVisibility(View.VISIBLE);
				fillData();
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
				RepairShow repairShow2 = repairList.get(position-1);
				Intent intent = new Intent(RepairHandlerAcrivity.this,HandlerDetailActivity.class);
				intent.putExtra("repairShow", repairShow2);
				startActivity(intent);
			}
		});
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_repair:
			startActivity(new Intent(this,RepairInputActivity.class));
			finish();
			break;
		case R.id.btnBack:
			finish();
			break;
		case R.id.btn_up:
			if(sp.getInt("size",0) == 0){
				ToastUtils.showToast(this, "没有需要上传的信息，请先报修");
				return;
			}
			
			startActivity(new Intent(this, UploadActivity.class));
			break;
		}
	}
	
	private void fillData() {
		ThreadUtils.runInBackground(new Runnable() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				if(url == null){
					ThreadUtils.runInMainThread(new Runnable() {
						
						@Override
						public void run() {
							llLoading.setVisibility(View.GONE);
						}
					});
					ToastUtils.showToast(RepairHandlerAcrivity.this, "网络错误");
					return;
				}
				String handlerJsonArray = null;
				try {
					handlerJsonArray = HttpUtil.getJsonArray(url,Constants.SERVICE_GETREPAIR_HANDLE);
				} catch (Exception e) {
					ThreadUtils.runInMainThread(new Runnable() {
						
						@Override
						public void run() {
							llLoading.setVisibility(View.GONE);
							ToastUtils.showToast(RepairHandlerAcrivity.this, "网络错误");
						}
					});
					e.printStackTrace();
				}
				msg.what = SAVE_INFO;
				msg.obj = handlerJsonArray;
				handler.sendMessage(msg);
			}
		});
	}
	
	/**
	 * 保存维修处理信息
	 */
	protected void saveRepairHandler(final String msgObj) {
		if(msgObj != null){
		ThreadUtils.runInBackground(new Runnable() {
			
			@Override
			public void run() {
				RepairHandlerBean repairHandlerBean = GsonUtils.getJsonBean(msgObj, RepairHandlerBean.class);
				List<RepairHandler> repairHandlerList = repairHandlerBean.ds;
				List<RepairInfo> allRepairSubmit = submitDao.getAllRepairSubmit();
				repairList.clear();
				for (RepairHandler repairHandler : repairHandlerList) {
					for (RepairInfo repairInfo : allRepairSubmit) {
						
						if(repairHandler.RepairID.equals(repairInfo.RepairID)&& repairHandler.RepairUserName != null 
								&& repairHandler.RepairFinishTime != null
								&&repairHandler.FaultHandle != null){
							System.out.println(repairHandler.RepairDeptID);
							RepairShow repairShow = new RepairShow();
							repairShow.EqptName = repairInfo.EqptName;
							repairShow.FaultOccu_Time = repairInfo.FaultOccu_Time;
							repairShow.FaultAppearance = repairInfo.FaultAppearance;
							repairShow.RepairUserName = repairHandler.RepairUserName;
							repairShow.RepairFinishTime = repairHandler.RepairFinishTime;
							repairShow.FaultHandle = repairHandler.FaultHandle;
							repairShow.imgUrl = sp.getString(repairInfo.RepairID, "");
							repairShow.RepairDeptID = repairHandler.RepairDeptID;
							
							repairList.add(repairShow);
							repairShow = null;
						}
					}
				}
				
				handler.sendEmptyMessage(FLUSH_UI);
			}
		});}
		
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != bmp && !bmp.isRecycled()){
			bmp.recycle();
			bmp = null;
			}
	}
	@Override
	protected void onPause() {
		super.onPause();
	if (null != bmp && !bmp.isRecycled()){
		bmp.recycle();
			bmp = null;
		}
	}
}
