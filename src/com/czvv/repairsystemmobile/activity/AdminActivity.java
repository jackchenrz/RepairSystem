package com.czvv.repairsystemmobile.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.UriMatcher;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.bean.DeptInfoBean;
import com.czvv.repairsystemmobile.bean.DeptInfoBean.DeptInfo;
import com.czvv.repairsystemmobile.bean.EqptInfoBean;
import com.czvv.repairsystemmobile.bean.EqptInfoBean.EqptInfo;
import com.czvv.repairsystemmobile.bean.FiveTEqptInfoBean;
import com.czvv.repairsystemmobile.bean.FiveTEqptInfoBean.FiveTEqpt;
import com.czvv.repairsystemmobile.bean.TechEqptBean;
import com.czvv.repairsystemmobile.bean.TechEqptBean.TechEqpt;
import com.czvv.repairsystemmobile.bean.UserInfoBean;
import com.czvv.repairsystemmobile.bean.UserInfoBean.UserInfo;
import com.czvv.repairsystemmobile.dao.Eqpt_InfoDao;
import com.czvv.repairsystemmobile.dao.FiveT_InfoDao;
import com.czvv.repairsystemmobile.dao.Sys_deptDao;
import com.czvv.repairsystemmobile.dao.Sys_userDao;
import com.czvv.repairsystemmobile.dao.TechEqptDao;
import com.czvv.repairsystemmobile.service.Eqpt_InfoService;
import com.czvv.repairsystemmobile.service.FiveTService;
import com.czvv.repairsystemmobile.service.Sys_deptService;
import com.czvv.repairsystemmobile.service.Sys_userService;
import com.czvv.repairsystemmobile.service.TechService;
import com.czvv.repairsystemmobile.utils.Constants;
import com.czvv.repairsystemmobile.utils.GsonUtils;
import com.czvv.repairsystemmobile.utils.HttpUtil;
import com.czvv.repairsystemmobile.utils.SharedPreferencesUtil;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.czvv.repairsystemmobile.utils.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AdminActivity extends RepairBaseActivity implements
		OnClickListener {

	@ViewInject(R.id.btnAdminLogout)
	ImageButton btnAdminLogout;
	@ViewInject(R.id.btnAdminSetting)
	Button btnAdminSetting;
	@ViewInject(R.id.btnUpdateUser)
	Button btnUpdateUser;
	@ViewInject(R.id.btnUpdateDevices)
	Button btnUpdateDevices;
	@ViewInject(R.id.btnUpdateDepartment)
	Button btnUpdateDepartment;
	@ViewInject(R.id.btnNone)
	Button btnNone;

	private static final int UPDATEUSER = 100;//ͬ����Ա
	private static final int UPDATEDEVICES = 101;//ͽ���豸
	private static final int UPDATEDEPARTMENT = 102;//ͬ��ά�޲���
	
	private ProgressDialog progressDialog;
	private String url;
	private Activity act;
	
	private Sys_userService userDao;
	private Sys_deptService deptDao;
	private Eqpt_InfoService eqptDao;
	private FiveTService fiveTDao;
	private TechService TechDao;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			final String msgObj = (String) msg.obj;
			switch (msg.what) {
			case UPDATEUSER:
				if (!TextUtils.isEmpty(msgObj)) {
					ThreadUtils.runInBackground(new Runnable() {

						@Override
						public void run() {
							saveUserInfo(msgObj);
						}
					});
				} else {
					showAlertDialog("��������ʧ�ܣ������ԣ�");
				}
				break;
			case UPDATEDEPARTMENT:
				if (!TextUtils.isEmpty(msgObj)) {
					ThreadUtils.runInBackground(new Runnable() {

						@Override
						public void run() {
							saveDeptInfo(msgObj);
						}
					});
				} else {
					showAlertDialog("��������ʧ�ܣ������ԣ�");
				}
				break;
			case UPDATEDEVICES:
				if (!TextUtils.isEmpty(msgObj)) {
					ThreadUtils.runInBackground(new Runnable() {

						@Override
						public void run() {
							saveEqptInfo(msgObj);
						}
					});

				} else {
					showAlertDialog("��������ʧ�ܣ������ԣ�");
				}
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		ViewUtils.inject(this);
		act = this;

		Intent intent = getIntent();
		String serverIP = intent.getStringExtra("serverIP");
		String serverPort = intent.getStringExtra("serverPort");
		if (serverIP != null && !"".equals(serverIP) && serverPort != null&& !"".equals(serverPort)) {
			url = "http://" + serverIP + ":" + serverPort+ Constants.SERVICE_PAGE;
		}
		userDao = Sys_userDao.getInstance(act);
		deptDao = Sys_deptDao.getInstance(act);
		eqptDao = Eqpt_InfoDao.getInstance(act);
		fiveTDao = FiveT_InfoDao.getInstance(act);
		TechDao = TechEqptDao.getInstance(act);

		btnAdminLogout.setOnClickListener(this);
		btnAdminSetting.setOnClickListener(this);
		btnUpdateUser.setOnClickListener(this);
		btnUpdateDevices.setOnClickListener(this);
		btnUpdateDepartment.setOnClickListener(this);
		btnNone.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAdminLogout:
			SharedPreferencesUtil spUtil = new SharedPreferencesUtil(this,Constants.ADMIN_INFO);
			spUtil.clearLoginUser();
			startActivity(new Intent(AdminActivity.this, LoginActivity.class));
			AdminActivity.this.finish();
			break;

		case R.id.btnAdminSetting:
			startActivity(new Intent(AdminActivity.this, SettingActivity.class));
			AdminActivity.this.finish();
			break;
		case R.id.btnUpdateUser:
			updateUser();
			break;
		case R.id.btnUpdateDevices:
			updateDevices();
			break;
		case R.id.btnUpdateDepartment:
			updateDepartment();
			break;
		case R.id.btnNone:

			break;
		}
	}

	private void updateDepartment() {
		showProgressDialog("����ͬ�����ݣ����Ժ�...");
		ThreadUtils.runInBackground(new Runnable() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				if (url == null) {
					progressDialog.dismiss();
					ToastUtils.showToast(AdminActivity.this,"������IP��˿ڴ���,����������");
					startActivity(new Intent(AdminActivity.this,SettingActivity.class));
					return;
				}
				String deptJsonArray = null;
				try {
					deptJsonArray = HttpUtil.getJsonArray(url,Constants.SERVICE_GETSYS_DEPT);
				} catch (Exception e) {
					progressDialog.dismiss();
					ToastUtils.showToast(AdminActivity.this,"������IP��˿ڴ���,����������");
					startActivity(new Intent(AdminActivity.this,SettingActivity.class));
					finish();
					e.printStackTrace();
				}
				if (deptJsonArray == null) {
					progressDialog.dismiss();
					ToastUtils.showToast(AdminActivity.this,"������IP��˿ڴ���,����������");
					startActivity(new Intent(AdminActivity.this,SettingActivity.class));
					finish();
					return;
				}
				msg.what = UPDATEDEPARTMENT;
				msg.obj = deptJsonArray;
				handler.sendMessage(msg);
			}
		});
	}

	private void updateDevices() {
		showProgressDialog("����ͬ�����ݣ����Ժ�...");
		ThreadUtils.runInBackground(new Runnable() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				if (url == null) {
					progressDialog.dismiss();
					ToastUtils.showToast(AdminActivity.this,"������IP��˿ڴ���,����������");
					startActivity(new Intent(AdminActivity.this,SettingActivity.class));
					return;
				}
				String fivetEqptJsonArray = null;
				String eqptJsonArray = null;
				String techJsonArray = null;
				try {
					fivetEqptJsonArray = HttpUtil.getJsonArray(url,Constants.SERVICE_GETFIVET_EQPT);
					eqptJsonArray = HttpUtil.getJsonArray(url,Constants.SERVICE_GETEQPT_INFO);
					techJsonArray = HttpUtil.getJsonArray(url,Constants.SERVICE_GETTECH_EQPT);
				} catch (Exception e) {
					progressDialog.dismiss();
					ToastUtils.showToast(AdminActivity.this,"������IP��˿ڴ���,����������");
					startActivity(new Intent(AdminActivity.this,SettingActivity.class));
					finish();
					e.printStackTrace();
				}
				String jsonArray = fivetEqptJsonArray + "&" + eqptJsonArray+ "&" + techJsonArray;
				if (fivetEqptJsonArray == null || eqptJsonArray == null|| techJsonArray == null) {
					progressDialog.dismiss();
					ToastUtils.showToast(AdminActivity.this,"������IP��˿ڴ���,����������");
					startActivity(new Intent(AdminActivity.this,SettingActivity.class));
					finish();
					return;
				}
				msg.what = UPDATEDEVICES;
				msg.obj = jsonArray;
				handler.sendMessage(msg);
			}
		});
	}

	private void updateUser() {
		showProgressDialog("����ͬ�����ݣ����Ժ�...");

		ThreadUtils.runInBackground(new Runnable() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				if (url == null) {
					progressDialog.dismiss();
					ToastUtils.showToast(AdminActivity.this,"������IP��˿ڴ���,����������");
					startActivity(new Intent(AdminActivity.this,SettingActivity.class));
					return;
				}

				String userJsonArray = null;
				try {
					userJsonArray = HttpUtil.getJsonArray(url,Constants.SERVICE_GETSYSUSER);
				} catch (Exception e) {
					progressDialog.dismiss();
					ToastUtils.showToast(AdminActivity.this,"������IP��˿ڴ���,����������");
					startActivity(new Intent(AdminActivity.this,SettingActivity.class));
					finish();
					e.printStackTrace();
				}

				if (userJsonArray == null) {
					progressDialog.dismiss();
					ToastUtils.showToast(AdminActivity.this,"������IP��˿ڴ���,����������");
					startActivity(new Intent(AdminActivity.this,SettingActivity.class));
					finish();
					return;
				}
				msg.what = UPDATEUSER;
				msg.obj = userJsonArray;
				handler.sendMessage(msg);
			}
		});
	}

	/**
	 * �����豸��Ϣ
	 * 
	 * @param msgObj
	 */
	protected void saveEqptInfo(String msgObj) {
		String[] jsonArr = msgObj.split("&");
		int addCount = 0;
		fiveTDao.deleteAllFiveTEqpt();
		FiveTEqptInfoBean fiveTEqptInfoBean = GsonUtils.getJsonBean(jsonArr[0],FiveTEqptInfoBean.class);
		List<FiveTEqpt> fiveTEqptList = fiveTEqptInfoBean.ds;
		ThreadUtils.runInMainThread(new Runnable() {
			@Override
			public void run() {
				progressDialog.setMessage("����ͬ���а��豸�����Ժ�...");
			}
		});
		for (FiveTEqpt fiveTEqpt : fiveTEqptList) {
			fiveTDao.addFiveTEqpt(fiveTEqpt);
			addCount++;
		}

		ThreadUtils.runInMainThread(new Runnable() {
			@Override
			public void run() {
				progressDialog.setMessage("����ͬ����е�豸�����Ժ�...");
			}
		});

		eqptDao.deleteAllEqptInfo();
		EqptInfoBean eqptInfoBean = GsonUtils.getJsonBean(jsonArr[1],EqptInfoBean.class);
		List<EqptInfo> eqptInfoList = eqptInfoBean.ds;
		for (EqptInfo eqptInfo : eqptInfoList) {
			eqptDao.addEqptInfo(eqptInfo);
			addCount++;
		}

		TechDao.deleteAllTechEqpt();
		TechEqptBean techEqptBean = GsonUtils.getJsonBean(jsonArr[2],TechEqptBean.class);
		List<TechEqpt> techEqptList = techEqptBean.ds;
		for (TechEqpt techEqpt : techEqptList) {
			TechDao.addTechEqpt(techEqpt);
			addCount++;
		}

		if (addCount == fiveTEqptList.size() + eqptInfoList.size()+ techEqptList.size()) {
			progressDialog.dismiss();
			ThreadUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					showAlertDialog("�豸ͬ���ɹ���");
				}
			});

		} else {
			ThreadUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					showAlertDialog("ͬ��ȫ���豸ʧ�ܣ�������ͬ��...");
				}
			});
		}
	}

	/**
	 * ����ά�޲�����Ϣ
	 * 
	 * @param msgObj
	 */
	protected void saveDeptInfo(String msgObj) {
		deptDao.deleteAllDept();
		int addCount = 0;
		DeptInfoBean deptInfoBean = GsonUtils.getJsonBean(msgObj,DeptInfoBean.class);
		List<DeptInfo> deptInfoList = deptInfoBean.ds;
		for (DeptInfo deptInfo : deptInfoList) {
			deptDao.addDept(deptInfo);
			addCount++;
		}
		if (addCount == deptInfoList.size()) {
			progressDialog.dismiss();
			ThreadUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					showAlertDialog("ά�޲���ͬ���ɹ���");
				}
			});

		} else {
			ThreadUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					showAlertDialog("ͬ��ȫ��ά�޲���ʧ�ܣ�������ͬ��...");
				}
			});
		}
	}

	/**
	 * �����û���Ϣ
	 * 
	 * @param msgObj
	 */
	protected void saveUserInfo(String msgObj) {
		userDao.deleteAllUserLog();
		int addCount = 0;
		UserInfoBean userInfoBean = GsonUtils.getJsonBean(msgObj,UserInfoBean.class);
		List<UserInfo> userInfoList = userInfoBean.ds;
		for (UserInfo userInfo : userInfoList) {
			userDao.addUserLog(userInfo);
			addCount++;
		}
		if (addCount == userInfoList.size()) {
			progressDialog.dismiss();
			ThreadUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					showAlertDialog("��Աͬ���ɹ���");
				}
			});
		} else {
			ThreadUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					showAlertDialog("ͬ��ȫ����Աʧ�ܣ�������ͬ��...");
				}
			});
		}
	}

	private void showAlertDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��ʾ");
		builder.setMessage(msg);
		builder.setPositiveButton("ȷ��", null);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
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
