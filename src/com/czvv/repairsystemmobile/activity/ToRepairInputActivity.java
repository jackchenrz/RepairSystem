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
			repairCategoryList.add("ʹ��");
			repairCategoryList.add("ά��");
			repairCategoryList.add("����");

			repairTypeList.add("��еԭ��");
			repairTypeList.add("����ԭ��");
			repairTypeList.add("�����ۺ�");

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
								ToRepairInputActivity.this, "����ʧ��");
					}
				}

				@Override
				public void onFailure(String result) {
					ToastUtils.showToast(ToRepairInputActivity.this,
							"����ʧ��");
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
					ToRepairInputActivity.this, "���ڱ��棬���Ժ�...");
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
			 * ����ά�޹�����Ϣ
			 */
			if ("Tech".equals(MApplication.mRepairType)) {

				if (TextUtils.isEmpty(faultType)
						|| TextUtils.isEmpty(repairFinishTime)
						|| TextUtils.isEmpty(faultReason)
						|| TextUtils.isEmpty(repairUserName)
						|| TextUtils.isEmpty(faultHandle)) {
					loadingDialog.dismiss();
					ToastUtils.showToast(this, "�����������Ϣ");
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
	 * �Զ��岼�ֵ�popupWindow
	 * 
	 * @param anchor
	 */
	@SuppressLint("NewApi")
	private void popWindow(final List list, final EditText anchor,
			final EditText tv) {
		LayoutInflater inflater = LayoutInflater.from(this);// ��ȡһ�������
		View view = inflater.inflate(R.layout.popup_sel, null);// ��������Զ���Ĳ���

		Display display = getWindowManager().getDefaultDisplay();// �õ���ǰ��Ļ����ʾ������
		Point size = new Point();// ����һ��Point���������������Ļ�ߴ���Ϣ
		display.getSize(size);// Point�������յ�ǰ�豸��Ļ�ߴ���Ϣ
		int width = size.x;// ��Point������л�ȡ��Ļ�Ŀ��(��λ����)
		int height = size.y;// ��Point������л�ȡ��Ļ�ĸ߶�(��λ����)
		// ����һ��PopupWindow���󣬵ڶ������������ÿ�ȵģ��øոջ�ȡ������Ļ��ȳ���2/3��2*width/3ȡ����Ļ��2/3��ȣ��Ӷ����κ��豸�ж��������䣬�߶���������ݼ��ɣ����һ�����������õõ�����
		final PopupWindow popWindow = new PopupWindow(view, 4 * width / 5,
				1 * height / 3, true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());// ����PopupWindow�ı���Ϊһ���յ�Drawable��������������������ôPopupWindow��������޷��˳���
		popWindow.setOutsideTouchable(true);// �����Ƿ���PopupWindow���˳�PopupWindow
		popWindow.setAnimationStyle(R.style.AnimationPreview);
		WindowManager.LayoutParams params = getWindow().getAttributes();// ������ǰ�����һ����������
		params.alpha = 0.8f;// ���ò�����͸����Ϊ0.8��͸����ȡֵΪ0~1��1Ϊ��ȫ��͸����0Ϊ��ȫ͸������Ϊandroid��Ĭ�ϵ���Ļ��ɫ���Ǵ���ɫ�ģ������������Ϊ1����ô���������Ǻ�ɫ������Ϊ0��������ʾ���ǵĵ�ǰ����
		getWindow().setAttributes(params);// �Ѹò����������ý���ǰ������
		popWindow.setOnDismissListener(new OnDismissListener() {// ����PopupWindow�˳�������
					@Override
					public void onDismiss() {// ���PopupWindow��ʧ�ˣ����˳��ˣ���ô�������¼���Ȼ��ѵ�ǰ�����͸��������Ϊ��͸��
						WindowManager.LayoutParams params = getWindow()
								.getAttributes();
						params.alpha = 1.0f;// ����Ϊ��͸�������ָ�ԭ���Ľ���
						getWindow().setAttributes(params);
					}
				});
		// ��һ������Ϊ��View���󣬼�PopupWindow���ڵĸ��ؼ����󣬵ڶ�������Ϊ�������ģ����������ֱ�Ϊx���y���ƫ����
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
