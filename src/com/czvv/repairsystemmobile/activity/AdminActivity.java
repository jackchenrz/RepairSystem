package com.czvv.repairsystemmobile.activity;

import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.czvv.repairsystemmobile.Constants;
import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.base.BaseActivity;
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
import com.czvv.repairsystemmobile.utils.AlertUtils;
import com.czvv.repairsystemmobile.utils.GsonUtils;
import com.czvv.repairsystemmobile.utils.HttpUtils;
import com.czvv.repairsystemmobile.utils.HttpUtils.WebServiceCallBack;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.czvv.repairsystemmobile.utils.ToastUtils;
import com.czvv.repairsystemmobile.view.MyProgressBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AdminActivity extends BaseActivity implements
		OnClickListener {

	@ViewInject(R.id.btnAdminSetting)
	ImageButton btnAdminSetting;
	@ViewInject(R.id.btnUpdateUser)
	Button btnUpdateUser;
	@ViewInject(R.id.btnUpdateDevices)
	Button btnUpdateDevices;
	@ViewInject(R.id.btnUpdateDepartment)
	Button btnUpdateDepartment;
	@ViewInject(R.id.btnNone)
	Button btnNone;

	private static final int PRO_USER = 101;//同步维修部门
	private static final int PRO_DEVICES = 102;//同步维修部门
	private static final int PRO_DEPARTMENT= 103;//同步维修部门
	
	private String url;
	private Activity act;
	private SharedPreferences sp;
	
	private Sys_userService userDao;
	private Sys_deptService deptDao;
	private Eqpt_InfoService eqptDao;
	private FiveTService fiveTDao;
	private TechService TechDao;

	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PRO_USER:
				tipTextView.setText("正在更新");
				pro.setProgress(addCount*100/userInfoList.size());
				tvPro.setText(addCount + "/" + userInfoList.size());
				break;
			case PRO_DEPARTMENT:
				tipTextView.setText("正在更新");
				pro.setProgress(addCount*100/deptInfoList.size());
				tvPro.setText(addCount + "/" + deptInfoList.size());
				break;
			case PRO_DEVICES:
				tipTextView.setText("正在更新");
				pro.setProgress(addCount*100/(fiveTEqptList.size() + eqptInfoList.size()+ techEqptList.size()));
				tvPro.setText(addCount + "/" + (fiveTEqptList.size() + eqptInfoList.size()+ techEqptList.size()));
				break;
			}
		};
	};
	private MyProgressBar pro;
	private TextView tvPro;
	private Dialog downloadDialog;
	private int i;
	private int addCount;
	private List<UserInfo> userInfoList;
	private TextView tipTextView;
	private List<DeptInfo> deptInfoList;
	private List<FiveTEqpt> fiveTEqptList;
	private List<EqptInfo> eqptInfoList;
	private List<TechEqpt> techEqptList;
	@Override
	public int bindLayout() {
		return R.layout.activity_admin;
	}

	@Override
	public void initView(View view) {
		ViewUtils.inject(this);		
	}

	@Override
	public void doBusiness(Context mContext) {
		act = this;
		sp = getSharedPreferences("serviceInfo", MODE_PRIVATE);	
		String serverIP = sp.getString("serverIP","");
		String serverPort = sp.getString("serverPort","");
		System.out.println(serverIP + serverPort);
		if (serverIP != null && !"".equals(serverIP) && serverPort != null&& !"".equals(serverPort)) {
			url = "http://" + serverIP + ":" + serverPort+ Constants.SERVICE_PAGE;
		}
		userDao = Sys_userDao.getInstance(act);
		deptDao = Sys_deptDao.getInstance(act);
		eqptDao = Eqpt_InfoDao.getInstance(act);
		fiveTDao = FiveT_InfoDao.getInstance(act);
		TechDao = TechEqptDao.getInstance(act);

		btnAdminSetting.setOnClickListener(this);
		btnUpdateUser.setOnClickListener(this);
		btnUpdateDevices.setOnClickListener(this);
		btnUpdateDepartment.setOnClickListener(this);
		btnNone.setOnClickListener(this);		
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

		case R.id.btnAdminSetting:
			startActivity(new Intent(AdminActivity.this, SettingActivity.class));
			overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
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

	/**
	 * 更新维修部门
	 */
	private void updateDepartment() {
		downloadDialog = showDownloadDialog(this, "正在准备更新...", "0/0", i);
		downloadDialog.show();
		if (url == null) {
			downloadDialog.dismiss();
			ToastUtils.showToast(AdminActivity.this,"服务器IP与端口错误,请设置连接");
			startActivity(new Intent(AdminActivity.this,SettingActivity.class));
			overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
			return;
		}
		
		HttpUtils.callService(url, Constants.SERVICE_NAMESPACE, Constants.SERVICE_GETSYS_DEPT, null, new WebServiceCallBack() {
			
			@Override
			public void onSucced(SoapObject result) {
				if (result != null) {
					final String string = result.getProperty(0).toString();
						ThreadUtils.runInBackground(new Runnable() {

							@Override
							public void run() {
								saveDeptInfo(string);
							}
						});
				} else {
					ToastUtils.showToast(AdminActivity.this, "联网失败");
					downloadDialog.dismiss();
				}
			}
			@Override
			public void onFailure(String result) {
				ToastUtils.showToast(AdminActivity.this, "联网失败");
				downloadDialog.dismiss();
			}
		});
	}

	/**
	 * 更新设备信息
	 */
	private void updateDevices() {
		downloadDialog = showDownloadDialog(this, "正在准备更新...", "0/0", i);
		downloadDialog.show();
		
		HttpUtils.callService(url, Constants.SERVICE_NAMESPACE, Constants.SERVICE_GETFIVET_EQPT, null, new WebServiceCallBack() {
			
			@Override
			public void onSucced(SoapObject result) {
				if (result != null) {
					String string = result.getProperty(0).toString();
					getEqpt(string + "&");
				} else {
					ToastUtils.showToast(AdminActivity.this, "联网失败");
					downloadDialog.dismiss();
				}
			}
			@Override
			public void onFailure(String result) {
				ToastUtils.showToast(AdminActivity.this, "联网失败");
				downloadDialog.dismiss();
			}
		});
	}
	private void getEqpt(final String str) {
		HttpUtils.callService(url, Constants.SERVICE_NAMESPACE,Constants.SERVICE_GETEQPT_INFO, null, new WebServiceCallBack() {
			
			@Override
			public void onSucced(SoapObject result) {
				if (result != null) {
					String string = result.getProperty(0).toString();
					getTech(str + string + "&");
				} else {
					ToastUtils.showToast(AdminActivity.this, "联网失败");
					downloadDialog.dismiss();
				}
			}
			@Override
			public void onFailure(String result) {
				ToastUtils.showToast(AdminActivity.this, "联网失败");
				downloadDialog.dismiss();
			}
		});
	}

	protected void getTech(final String str) {
		HttpUtils.callService(url, Constants.SERVICE_NAMESPACE,Constants.SERVICE_GETTECH_EQPT, null, new WebServiceCallBack() {
			
			@Override
			public void onSucced(SoapObject result) {
				if (result != null) {
					final String string = result.getProperty(0).toString();
					ThreadUtils.runInBackground(new Runnable() {

						@Override
						public void run() {
							saveEqptInfo(str + string);
						}
					});
				} else {
					ToastUtils.showToast(AdminActivity.this, "联网失败");
					downloadDialog.dismiss();
				}
			}
			@Override
			public void onFailure(String result) {
				ToastUtils.showToast(AdminActivity.this, "联网失败");
				downloadDialog.dismiss();
			}
		});
	}

	/**
	 * 更新用户信息
	 */
	private void updateUser() {
		downloadDialog = showDownloadDialog(this, "正在准备更新...", "0/0", i);
		downloadDialog.show();
		HttpUtils.callService(url, Constants.SERVICE_NAMESPACE,Constants.SERVICE_GETSYSUSER, null, new WebServiceCallBack() {
			
			@Override
			public void onSucced(SoapObject result) {
				if (result != null) {
					final String string = result.getProperty(0).toString();
					ThreadUtils.runInBackground(new Runnable() {

						@Override
						public void run() {
							saveUserInfo(string);
						}
					});
				} else {
					ToastUtils.showToast(AdminActivity.this, "联网失败");
					downloadDialog.dismiss();
				}
			}
			@Override
			public void onFailure(String result) {
				ToastUtils.showToast(AdminActivity.this, "联网失败");
				downloadDialog.dismiss();
			}
		});
	}

	/**
	 * 保存设备信息
	 * 
	 * @param msgObj
	 */
	protected void saveEqptInfo(String msgObj) {
		String[] jsonArr = msgObj.split("&");
		addCount = 0;
		fiveTDao.deleteAllFiveTEqpt();
		FiveTEqptInfoBean fiveTEqptInfoBean = GsonUtils.getJsonBean(jsonArr[0],FiveTEqptInfoBean.class);
		fiveTEqptList = fiveTEqptInfoBean.ds;
		TechDao.deleteAllTechEqpt();
		TechEqptBean techEqptBean = GsonUtils.getJsonBean(jsonArr[2],TechEqptBean.class);
		techEqptList = techEqptBean.ds;
		eqptDao.deleteAllEqptInfo();
		EqptInfoBean eqptInfoBean = GsonUtils.getJsonBean(jsonArr[1],EqptInfoBean.class);
		eqptInfoList = eqptInfoBean.ds;
		for (FiveTEqpt fiveTEqpt : fiveTEqptList) {
			fiveTDao.addFiveTEqpt(fiveTEqpt);
			addCount++;
			handler.sendEmptyMessage(PRO_DEVICES);
		}

		for (EqptInfo eqptInfo : eqptInfoList) {
			eqptDao.addEqptInfo(eqptInfo);
			addCount++;
			handler.sendEmptyMessage(PRO_DEVICES);
		}
		
		for (TechEqpt techEqpt : techEqptList) {
			TechDao.addTechEqpt(techEqpt);
			addCount++;
			handler.sendEmptyMessage(PRO_DEVICES);
		}
		if (addCount == fiveTEqptList.size() + eqptInfoList.size()+ techEqptList.size()) {
			downloadDialog.dismiss();
			ThreadUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					AlertUtils.dialog(act, "提示", "设备同步成功！", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
				}
			});

		} else {
			ThreadUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					AlertUtils.dialog(act, "提示", "同步全部设备失败，请重新同步...", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
				}
			});
		}
	}

	/**
	 * 保存维修部门信息
	 * 
	 * @param msgObj
	 */
	protected void saveDeptInfo(String msgObj) {
		deptDao.deleteAllDept();
		addCount = 0;
		DeptInfoBean deptInfoBean = GsonUtils.getJsonBean(msgObj,DeptInfoBean.class);
		deptInfoList = deptInfoBean.ds;
		for (DeptInfo deptInfo : deptInfoList) {
			deptDao.addDept(deptInfo);
			addCount++;
			handler.sendEmptyMessage(PRO_DEPARTMENT);
		}
		if (addCount == deptInfoList.size()) {
			downloadDialog.dismiss();
			ThreadUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					AlertUtils.dialog(act, "提示", "维修部门同步成功！", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
				}
			});

		} else {
			ThreadUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					AlertUtils.dialog(act, "提示", "同步全部维修部门失败，请重新同步...", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
				}
			});
		}
	}

	/**
	 * 保存用户信息
	 * 
	 * @param msgObj
	 */
	protected void saveUserInfo(String msgObj) {
		userDao.deleteAllUserLog();
		addCount = 0;
		UserInfoBean userInfoBean = GsonUtils.getJsonBean(msgObj,UserInfoBean.class);
		userInfoList = userInfoBean.ds;
		for (UserInfo userInfo : userInfoList) {
			userDao.addUserLog(userInfo);
			addCount++;
			handler.sendEmptyMessage(PRO_USER);
		}
		if (addCount == userInfoList.size()) {
			downloadDialog.dismiss();
			ThreadUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					AlertUtils.dialog(act, "提示", "人员同步成功！", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
				}
			});
		} else {
			ThreadUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					AlertUtils.dialog(act, "提示", "同步全部人员失败，请重新同步...", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
				}
			});
		}
	}

	private Dialog showDownloadDialog(Context context, String msg, String text, int progress) {  
		  
        LayoutInflater inflater = LayoutInflater.from(context);  
        View v = inflater.inflate(R.layout.download_dialog, null);// 得到加载view  
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局  
        pro = (MyProgressBar) v.findViewById(R.id.pro);  
        tvPro = (TextView) v.findViewById(R.id.tv_pro);  
        tipTextView = (TextView) v.findViewById(R.id.tipTextView);
        tipTextView.setText(msg);// 设置加载信息  
        tvPro.setText(text);//设置下载进度提示
        pro.setProgress(progress);
        pro.setMax(100);
        Dialog downloadDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog  
  
        downloadDialog.setCancelable(false);// 不可以用“返回键”取消  
        downloadDialog.setContentView(layout, new LinearLayout.LayoutParams(  
                LinearLayout.LayoutParams.MATCH_PARENT,  
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局  
        return downloadDialog;  
    }  
}
