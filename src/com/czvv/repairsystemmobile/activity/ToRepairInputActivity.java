package com.czvv.repairsystemmobile.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.czvv.repairsystemmobile.Constants;
import com.czvv.repairsystemmobile.MApplication;
import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.adapter.CommonAdapter;
import com.czvv.repairsystemmobile.base.BaseActivity;
import com.czvv.repairsystemmobile.bean.DeptInfoBean.DeptInfo;
import com.czvv.repairsystemmobile.bean.FiveTProbTypeBean;
import com.czvv.repairsystemmobile.bean.FiveTProbTypeBean.FiveTProbType;
import com.czvv.repairsystemmobile.bean.ToRepairSave;
import com.czvv.repairsystemmobile.bean.ToRepairedBean.ToRepair;
import com.czvv.repairsystemmobile.dao.Sys_deptDao;
import com.czvv.repairsystemmobile.dao.Sys_userDao;
import com.czvv.repairsystemmobile.dao.ToRepairDao;
import com.czvv.repairsystemmobile.utils.AlertUtils;
import com.czvv.repairsystemmobile.utils.DensityUtil;
import com.czvv.repairsystemmobile.utils.GsonUtils;
import com.czvv.repairsystemmobile.utils.HttpUtils;
import com.czvv.repairsystemmobile.utils.HttpUtils.WebServiceCallBack;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.czvv.repairsystemmobile.utils.ToastUtils;
import com.czvv.repairsystemmobile.view.DatePickerPopWindow;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ToRepairInputActivity extends BaseActivity implements
		OnClickListener {

	@ViewInject(R.id.et_repair_workshop)
	EditText et_repair_workshop;
	@ViewInject(R.id.et_repair_people)
	EditText et_repair_people;
	@ViewInject(R.id.et_torepairtime)
	EditText et_torepairtime;
	@ViewInject(R.id.et_repair_type)
	EditText et_repair_type;
	@ViewInject(R.id.et_repair_category)
	EditText et_repair_category;
	@ViewInject(R.id.et_breakdown)
	EditText et_breakdown;
	@ViewInject(R.id.btn_torepairtime)
	Button btn_torepairtime;
	@ViewInject(R.id.btn_repair_type)
	Button btn_repair_type;
	@ViewInject(R.id.btn_repair_category)
	Button btn_repair_category;
	@ViewInject(R.id.btn_save)
	Button btn_save;
	@ViewInject(R.id.btn_cancel)
	Button btn_cancel;

	private Sys_userDao sys_userDao;
	private Sys_deptDao sys_deptDao;
	private ToRepairDao toRepairDao;
	private SharedPreferences sp;
	private String url;
	private CommonAdapter<Object> adapter;
	private List<String> repairTypeList = new ArrayList<String>();
	private List<String> repairCategoryList = new ArrayList<String>();
	private String str;
	private ToRepair toRepair;
	private Dialog loadingDialog;
	private List<FiveTProbType> FiveTProbTypeList;
	private String FiveTProbType;
	private String FiveTSysName;

	@Override
	public int bindLayout() {
		return R.layout.activity_torepair;
	}

	@Override
	public void initView(View view) {
		ViewUtils.inject(this);
		btn_torepairtime.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {

		sys_userDao = Sys_userDao.getInstance(mContext);
		sys_deptDao = Sys_deptDao.getInstance(mContext);
		toRepairDao = ToRepairDao.getInstance(mContext);
		sp = getSharedPreferences("serviceInfo", MODE_PRIVATE);

		Intent intent = getIntent();
		toRepair = (ToRepair) intent.getSerializableExtra("toRepair");

		DeptInfo deptInfo = sys_deptDao.getDeptInfo1(sys_userDao.getUserInfo(sp
				.getString("loginName", "")).dept_id);
		et_repair_workshop.setText(deptInfo.dept_name);
		String serverIP = sp.getString("serverIP", "");
		String serverPort = sp.getString("serverPort", "");
		if (serverIP != null && !"".equals(serverIP) && serverPort != null
				&& !"".equals(serverPort)) {
			url = "http://" + serverIP + ":" + serverPort
					+ Constants.SERVICE_PAGE;
		}

		if ("Tech".equals(MApplication.mRepairType)) {
			btn_repair_type.setOnClickListener(this);
			repairCategoryList.add("使用");
			repairCategoryList.add("维修");
			repairCategoryList.add("厂家");

			repairTypeList.add("机械原因");
			repairTypeList.add("电器原因");
			repairTypeList.add("机电综合");

			btn_repair_category.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					popWindow(repairCategoryList, et_repair_category, null);
				}
			});
		} else if ("5T".equals(MApplication.mRepairType)) {

			HttpUtils.callService(url, Constants.SERVICE_NAMESPACE,Constants.SERVICE_GETFIVET_EQPT_PROB_TYPE, null,new WebServiceCallBack() {

				@Override
				public void onSucced(SoapObject result) {
					btn_repair_type
							.setOnClickListener(ToRepairInputActivity.this);
					if (result != null) {
						String string = result.getProperty(0)
								.toString();
						FiveTProbTypeBean jsonBean = GsonUtils
								.getJsonBean(string,
										FiveTProbTypeBean.class);
						FiveTProbTypeList = jsonBean.ds;
					} else {
						ToastUtils.showToast(
								ToRepairInputActivity.this, "联网失败");
					}
				}

				@Override
				public void onFailure(String result) {
					ToastUtils.showToast(ToRepairInputActivity.this,
							"联网失败");
				}
			});
		}

	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_torepairtime:
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			DatePickerPopWindow popWindow = new DatePickerPopWindow(
					ToRepairInputActivity.this, df.format(date));
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.alpha = 0.8f;
			getWindow().setAttributes(lp);
			popWindow.setAnimationStyle(R.style.AnimationPreview);
			popWindow.showAtLocation(et_torepairtime, Gravity.CENTER, 0, 0);
			popWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
			popWindow.setOutsideTouchable(true);
			popWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					WindowManager.LayoutParams lp = getWindow().getAttributes();
					lp.alpha = 1f;
					getWindow().setAttributes(lp);
					System.out.println(sp.getString(Constants.BIRTHDAY, ""));
					et_torepairtime.setText(sp
							.getString(Constants.BIRTHDAY, ""));
				}
			});

			break;

		case R.id.btn_repair_type:
			if ("Tech".equals(MApplication.mRepairType)) {
				popWindow(repairTypeList, et_repair_type, null);
			} else if ("5T".equals(MApplication.mRepairType)) {
				popWindow(FiveTProbTypeList, et_repair_type, et_repair_category);
			}
			break;
		case R.id.btn_save:
			loadingDialog = AlertUtils.createLoadingDialog(
					ToRepairInputActivity.this, "正在保存，请稍后...");
			loadingDialog.show();
			
			ToRepairHandlerAcrivity.instance.finish();
			final String repairFinishTime = et_torepairtime.getText()
					.toString().trim();
			final String repairUserName = et_repair_people.getText().toString()
					.trim();
			final String faultHandle = et_breakdown.getText().toString().trim();
			final String faultType = et_repair_type.getText().toString().trim();
			final String faultReason = et_repair_category.getText().toString()
					.trim();
			/**
			 * 保存维修故障信息
			 */
			if ("Tech".equals(MApplication.mRepairType)) {

				if (TextUtils.isEmpty(faultType)
						|| TextUtils.isEmpty(repairFinishTime)
						|| TextUtils.isEmpty(faultReason)
						|| TextUtils.isEmpty(repairUserName)
						|| TextUtils.isEmpty(faultHandle)) {
					loadingDialog.dismiss();
					ToastUtils.showToast(this, "请完善相关信息");
					return;
				}
				ThreadUtils.runInBackground(new Runnable() {
					
					@Override
					public void run() {
						toRepairDao.delToRepair(toRepair.RepairID);
						ToRepairSave toRepairSave = new ToRepairSave();
						toRepairSave.RepairID = toRepair.RepairID;
						toRepairSave.RepairDeptID = toRepair.RepairDeptID;
						toRepairSave.FaultType = faultType;
						toRepairSave.FaultReason = faultReason;
						toRepairSave.RepairUserName = repairUserName;
						toRepairSave.FaultHandle = faultHandle;
						toRepairSave.RepairFinishTime = repairFinishTime;
						toRepairSave.ProbType = "";
						toRepairSave.ProbSysName = "";
						toRepairDao.addTorepair(toRepairSave);
						loadingDialog.dismiss();
						Intent intent = new Intent(ToRepairInputActivity.this,
								ToRepairSaveListActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.base_slide_right_in,
								R.anim.base_slide_remain);
						ToRepairInputActivity.this.finish();
					}
				});
			}else if("5T".equals(MApplication.mRepairType)){
				
				
				
				ThreadUtils.runInBackground(new Runnable() {
					
					@Override
					public void run() {
						toRepairDao.delToRepair(toRepair.RepairID);
						ToRepairSave toRepairSave = new ToRepairSave();
						toRepairSave.RepairID = toRepair.RepairID;
						toRepairSave.RepairDeptID = toRepair.RepairDeptID;
						toRepairSave.RepairUserName = repairUserName;
						toRepairSave.FaultHandle = faultHandle;
						toRepairSave.RepairFinishTime = repairFinishTime;
						toRepairSave.FaultType = faultType;
						toRepairSave.FaultReason = faultReason;
						toRepairSave.ProbType = FiveTProbType;
						toRepairSave.ProbSysName = FiveTSysName;
						toRepairDao.addTorepair(toRepairSave);
						loadingDialog.dismiss();
						Intent intent = new Intent(ToRepairInputActivity.this,
								ToRepairSaveListActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.base_slide_right_in,
								R.anim.base_slide_remain);
						ToRepairInputActivity.this.finish();
					}
				});
			}

			break;
		case R.id.btn_cancel:
			finish();
			break;

		}
	}

	/**
	 * 自定义布局的popupWindow
	 * 
	 * @param anchor
	 */
	@SuppressLint("NewApi")
	private void popWindow(final List list, final EditText anchor,
			final EditText tv) {
		LayoutInflater inflater = LayoutInflater.from(this);// 获取一个填充器
		View view = inflater.inflate(R.layout.popup_sel, null);// 填充我们自定义的布局

		Display display = getWindowManager().getDefaultDisplay();// 得到当前屏幕的显示器对象
		Point size = new Point();// 创建一个Point点对象用来接收屏幕尺寸信息
		display.getSize(size);// Point点对象接收当前设备屏幕尺寸信息
		int width = size.x;// 从Point点对象中获取屏幕的宽度(单位像素)
		int height = size.y;// 从Point点对象中获取屏幕的高度(单位像素)
		// 创建一个PopupWindow对象，第二个参数是设置宽度的，用刚刚获取到的屏幕宽度乘以2/3，2*width/3取该屏幕的2/3宽度，从而在任何设备中都可以适配，高度则包裹内容即可，最后一个参数是设置得到焦点
		final PopupWindow popWindow = new PopupWindow(view, 4 * width / 5,
				1 * height / 3, true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());// 设置PopupWindow的背景为一个空的Drawable对象，如果不设置这个，那么PopupWindow弹出后就无法退出了
		popWindow.setOutsideTouchable(true);// 设置是否点击PopupWindow外退出PopupWindow
		popWindow.setAnimationStyle(R.style.AnimationPreview);
		WindowManager.LayoutParams params = getWindow().getAttributes();// 创建当前界面的一个参数对象
		params.alpha = 0.8f;// 设置参数的透明度为0.8，透明度取值为0~1，1为完全不透明，0为完全透明，因为android中默认的屏幕颜色都是纯黑色的，所以如果设置为1，那么背景将都是黑色，设置为0，背景显示我们的当前界面
		getWindow().setAttributes(params);// 把该参数对象设置进当前界面中
		popWindow.setOnDismissListener(new OnDismissListener() {// 设置PopupWindow退出监听器
					@Override
					public void onDismiss() {// 如果PopupWindow消失了，即退出了，那么触发该事件，然后把当前界面的透明度设置为不透明
						WindowManager.LayoutParams params = getWindow()
								.getAttributes();
						params.alpha = 1.0f;// 设置为不透明，即恢复原来的界面
						getWindow().setAttributes(params);
					}
				});
		// 第一个参数为父View对象，即PopupWindow所在的父控件对象，第二个参数为它的重心，后面两个分别为x轴和y轴的偏移量
		 popWindow.showAtLocation(anchor, Gravity.CENTER, 0, 0);

		ListView lv_content = (ListView) view.findViewById(R.id.lv_content);

		adapter = new CommonAdapter<Object>(list) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				TextView tv = new TextView(ToRepairInputActivity.this);
				tv.setText(list.get(position).toString());
				tv.setTextSize(DensityUtil.dip2px(ToRepairInputActivity.this,
						14));
				return tv;
			}
		};

		lv_content.setAdapter(adapter);

		lv_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String string = list.get(position).toString();
				String[] strings = string.split(" ");
				if (strings.length == 1) {
					anchor.setText(string);
				} else {
					anchor.setText(strings[strings.length - 2]);
					if (tv != null) {
						tv.setText(strings[strings.length - 1]);
					}
					FiveTProbType = strings[0];
					FiveTSysName = strings[strings.length - 3];
				}
				popWindow.dismiss();
			}
		});
	}

}
