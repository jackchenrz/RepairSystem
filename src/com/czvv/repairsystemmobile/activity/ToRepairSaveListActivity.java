package com.czvv.repairsystemmobile.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.czvv.repairsystemmobile.Constants;
import com.czvv.repairsystemmobile.MApplication;
import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.activity.ToRepairHandlerAcrivity.ViewHolder;
import com.czvv.repairsystemmobile.adapter.CommonAdapter;
import com.czvv.repairsystemmobile.base.BaseActivity;
import com.czvv.repairsystemmobile.bean.ToRepairSave;
import com.czvv.repairsystemmobile.bean.ToRepairedBean;
import com.czvv.repairsystemmobile.bean.ToRepairedBean.ToRepair;
import com.czvv.repairsystemmobile.dao.Sys_userDao;
import com.czvv.repairsystemmobile.dao.ToRepairDao;
import com.czvv.repairsystemmobile.utils.AlertUtils;
import com.czvv.repairsystemmobile.utils.GsonUtils;
import com.czvv.repairsystemmobile.utils.HttpUtils;
import com.czvv.repairsystemmobile.utils.HttpUtils.WebServiceCallBack;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.czvv.repairsystemmobile.utils.ToastUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ToRepairSaveListActivity extends BaseActivity {
	@ViewInject(R.id.lv_pull)
	ListView lvPull;
	@ViewInject(R.id.btn_up)
	Button btn_up;
	
	private CommonAdapter<ToRepair> adapter;
	private List<ToRepair> list;
	private List<ToRepairSave> allToRepairSave;
	private BitmapUtils bitmapUtils;
	private Dialog loadingDialog;
	private SharedPreferences sp;
	private String url;
	private ToRepairDao toRepairDao;
	private Sys_userDao userDao;
	private String jsonArr;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case 100:
				loadingDialog.dismiss();
				setOrUpdateAdapter();
				btn_up.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						loadingDialog = AlertUtils.createLoadingDialog(ToRepairSaveListActivity.this, "正在上传，请稍后...");
						loadingDialog.show();
						getData();
					}
				});
				break;
			case 101:
				HashMap<String, String> properties = new HashMap<String, String>();
				properties.put("strJson", jsonArr);
				HttpUtils.callService(url,Constants.SERVICE_NAMESPACE, Constants.SERVICE_RETURNREPAIR_HANDLE, properties, new WebServiceCallBack() {
					
					@Override
					public void onSucced(SoapObject result) {
						loadingDialog.dismiss();
						
						ToastUtils.showToast(ToRepairSaveListActivity.this, "上传成功");
						list.clear();
						toRepairDao.deleteAllToRepairSave();
						adapter.notifyDataSetChanged();
						ToRepairHandlerAcrivity.instance.finish();
						Intent intent = new Intent(ToRepairSaveListActivity.this,ToRepairHandlerAcrivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
						ToRepairSaveListActivity.this.finish();
					}
					
					@Override
					public void onFailure(String result) {
						loadingDialog.dismiss();
						ToastUtils.showToast(ToRepairSaveListActivity.this, "联网失败");
					}
				});
				break;
			}
			
		};
	};
	
	@Override
	public int bindLayout() {
		return R.layout.activity_torepair_savelist;
	}

	protected void setOrUpdateAdapter() {
		
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}else{
			adapter = new CommonAdapter<ToRepair>(list) {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View view;
					ViewHolder vh;
					if (convertView == null) {
						view = getLayoutInflater().inflate(
								R.layout.list_item_repairhandler, parent,
								false);
						vh = new ViewHolder(view);
						view.setTag(vh);
					} else {
						view = convertView;
						vh = (ViewHolder) view.getTag();
					}
					ToRepair toRepair = list.get(position);
					vh.tv_name.setText("设备名称：" + toRepair.EqptName);
					vh.tv_department
					.setText("故障发生时间：" + toRepair.FaultOccu_Time);
					vh.tv_describe.setText("故障描述：" + toRepair.FaultAppearance);
					bitmapUtils.display(vh.ivImg,toRepair.ImageUrlPath);
					return view;
				}
			};		
			lvPull.setAdapter(adapter);		
			
		}
	}

	@Override
	public void initView(View view) {
		ViewUtils.inject(this);	
	}

	@Override
	public void doBusiness(Context mContext) {
		loadingDialog = AlertUtils.createLoadingDialog(ToRepairSaveListActivity.this, "正在加载，请稍后...");
		loadingDialog.show();
		toRepairDao = ToRepairDao.getInstance(mContext);
		userDao = Sys_userDao.getInstance(this);
		bitmapUtils = new BitmapUtils(mContext);
		sp = getSharedPreferences("serviceInfo", MODE_PRIVATE);
		String serverIP = sp.getString("serverIP", "");
		String serverPort = sp.getString("serverPort", "");
		if (serverIP != null && !"".equals(serverIP) && serverPort != null
				&& !"".equals(serverPort)) {
			url = "http://" + serverIP + ":" + serverPort
					+ Constants.SERVICE_PAGE;
		}
		
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("DeptID", userDao.getUserInfo(sp.getString("loginName", "")).dept_id);
		if("Tech".equals(MApplication.mRepairType)){
			HttpUtils.callService(url, Constants.SERVICE_NAMESPACE, Constants.SERVICE_GETTECHREPAIRINGLIST, properties , new WebServiceCallBack() {
				
				@Override
				public void onSucced(SoapObject result) {
					if(result != null){
						String string = result.getProperty(0).toString();
							ToRepairedBean jsonBean = GsonUtils.getJsonBean(string, ToRepairedBean.class);
							list = jsonBean.ds;
							getListData();
					}else{
						loadingDialog.dismiss();
						ToastUtils.showToast(ToRepairSaveListActivity.this, "联网失败");
					}
				}
				
				@Override
				public void onFailure(String result) {
					loadingDialog.dismiss();
					ToastUtils.showToast(ToRepairSaveListActivity.this, "联网失败");
				}
			});
		}else if("5T".equals(MApplication.mRepairType)){
			HttpUtils.callService(url, Constants.SERVICE_NAMESPACE, Constants.SERVICE_GET5TREPAIRINGLIST, properties , new WebServiceCallBack() {
				
				@Override
				public void onSucced(SoapObject result) {
					if(result != null){
						String string = result.getProperty(0).toString();
						ToRepairedBean jsonBean = GsonUtils.getJsonBean(string, ToRepairedBean.class);
						list = jsonBean.ds;
						getListData();
					}else{
						loadingDialog.dismiss();
						ToastUtils.showToast(ToRepairSaveListActivity.this, "联网失败");
					}
				}
				
				@Override
				public void onFailure(String result) {
					loadingDialog.dismiss();
					ToastUtils.showToast(ToRepairSaveListActivity.this, "联网失败");
				}
			});
		}
		lvPull.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ToRepair toRepair = list.get(position);
				Intent intent = new Intent(ToRepairSaveListActivity.this,ToRepairSaveDetail.class);
				intent.putExtra("toRepair", toRepair);
				startActivity(intent);
			}
		});
		
		lvPull.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				showAlertDialog(position);
				
				return true;
			}
		});
	}
	private boolean flag = false;
	protected void getListData() {
		ThreadUtils.runInBackground(new Runnable() {

			@Override
			public void run() {
				allToRepairSave = toRepairDao.getAllToRepairSave();
				if(null == allToRepairSave || allToRepairSave.size() == 0){
					list.clear();
					loadingDialog.dismiss();
					return;
				}
				for(Iterator<ToRepair> it = list.iterator();it.hasNext();){
					ToRepair s = it.next();
					for (ToRepairSave repairInfo : allToRepairSave) {
						if(s.RepairID.equals(repairInfo.RepairID)){
							flag = true;
						}
					}
					
					if(!flag){
						it.remove();
					}
				}
				handler.sendEmptyMessage(100);
			}
		});
		
	}
	
	protected void getData() {
		ThreadUtils.runInBackground(new Runnable() {

			@Override
			public void run() {
				allToRepairSave = toRepairDao.getAllToRepairSave();
				if(allToRepairSave.size() != 0){
					jsonArr = "{" + "\"" + "ds" + "\"" + ":[";
					int count = 0;
					for (ToRepairSave repairInfo : allToRepairSave) {
						for(Iterator<ToRepair> it = list.iterator();it.hasNext();){
							ToRepair s = it.next();
							if(s.RepairID.equals(repairInfo.RepairID)){
								jsonArr += GsonUtils.toJsonArray(repairInfo);
							}
						}
						count++;
						
						if (count != allToRepairSave.size()) {
							jsonArr += ",";
						}
					}
					jsonArr += "]}";
					handler.sendEmptyMessage(101);
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
	
	private void showAlertDialog(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("是否删除？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				toRepairDao.delToRepair(list.get(position).RepairID);
				list.remove(position);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	/* 返回键 */
	if (keyCode == KeyEvent.KEYCODE_BACK) {
		ToRepairSaveListActivity.this.finish();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}
	return false;
	}
}
