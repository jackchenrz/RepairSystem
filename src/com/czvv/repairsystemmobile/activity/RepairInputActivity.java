package com.czvv.repairsystemmobile.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;
import android_serialport_api.sample.Application;

import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.bean.EqptInfoBean.EqptInfo;
import com.czvv.repairsystemmobile.bean.FiveTEqptInfoBean.FiveTEqpt;
import com.czvv.repairsystemmobile.bean.RepairInfo;
import com.czvv.repairsystemmobile.dao.Eqpt_InfoDao;
import com.czvv.repairsystemmobile.dao.FiveT_InfoDao;
import com.czvv.repairsystemmobile.dao.Reapir_SubmitDao;
import com.czvv.repairsystemmobile.dao.Sys_deptDao;
import com.czvv.repairsystemmobile.dao.Sys_userDao;
import com.czvv.repairsystemmobile.dao.TechEqptDao;
import com.czvv.repairsystemmobile.service.Eqpt_InfoService;
import com.czvv.repairsystemmobile.service.FiveTService;
import com.czvv.repairsystemmobile.service.Repair_submitService;
import com.czvv.repairsystemmobile.service.Sys_deptService;
import com.czvv.repairsystemmobile.service.Sys_userService;
import com.czvv.repairsystemmobile.service.TechService;
import com.czvv.repairsystemmobile.utils.Constants;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.czvv.repairsystemmobile.view.DatePickerPopWindow;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RepairInputActivity extends RepairBaseActivity implements
		OnClickListener {

	@ViewInject(R.id.btnBack)
	Button btnBack;
	@ViewInject(R.id.et_devicename)
	EditText etDevicename;
	@ViewInject(R.id.btn_devicename)
	Button btnDevicename;
	@ViewInject(R.id.et_repairdepartment)
	EditText etRepairdepartment;
	@ViewInject(R.id.btn_repairdepartment)
	Button btnRepairdepartment;
	@ViewInject(R.id.et_repairtime)
	EditText etRepairtime;
	@ViewInject(R.id.btn_photograph)
	Button btnPhotograph;
	@ViewInject(R.id.iv_selimg)
	ImageView ivSelimg;
	@ViewInject(R.id.et_breakdown)
	EditText etBreakdown;
	@ViewInject(R.id.btn_save)
	Button btn_save;
	@ViewInject(R.id.btn_cancel)
	Button btn_cancel;

	private Uri uri;
	private String name;
	private Bitmap bmp;

	private static final int CAMERA = 55;
	private Repair_submitService repairDao;
	private Sys_userService userDao;
	private Eqpt_InfoService eqptDao;
	private FiveTService fivetDao;
	private TechService techDao;
	private Sys_deptService deptDao;
	private int flag = 0;
	private SharedPreferences sp;
	private ProgressDialog progressDialog;
	private final int FLUSH = 100;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FLUSH:
				progressDialog.dismiss();
				Intent intent4 = new Intent(RepairInputActivity.this, UploadActivity.class);
				startActivity(intent4);
				finish();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repair_input);
		ViewUtils.inject(this);
		sp = getSharedPreferences("serviceInfo", MODE_PRIVATE);
		repairDao = Reapir_SubmitDao.getInstance(this);
		userDao = Sys_userDao.getInstance(this);
		fivetDao = FiveT_InfoDao.getInstance(this);
		eqptDao = Eqpt_InfoDao.getInstance(this);
		techDao = TechEqptDao.getInstance(this);
		deptDao = Sys_deptDao.getInstance(this);

		btnBack.setOnClickListener(this);
		btnDevicename.setOnClickListener(this);
		btnRepairdepartment.setOnClickListener(this);
		btnPhotograph.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		etRepairtime.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			startActivity(new Intent(this, RepairHandlerAcrivity.class));
			finish();
			break;
		case R.id.btn_devicename:
			Intent intent2 = new Intent(this, SearchActivity.class);
			flag = 0;
			intent2.putExtra("flag", flag);
			startActivityForResult(intent2, Constants.DEVICENAME);

			break;
		case R.id.btn_repairdepartment:
			Intent intent1 = new Intent(this, SearchActivity.class);
			flag = 1;
			intent1.putExtra("flag", flag);
			startActivityForResult(intent1, Constants.REPAIRDEPARTMENTLIST);
			break;
		case R.id.btn_photograph:
			photoGraph();
			break;
		case R.id.btn_save:
			final String deviceName = etDevicename.getText().toString().trim();
			final String repairdepartment = etRepairdepartment.getText()
					.toString().trim();
			final String breakdown = etBreakdown.getText().toString().trim();
			final String repairtime = etRepairtime.getText().toString().trim();

			if (uri == null || TextUtils.isEmpty(deviceName)
					|| TextUtils.isEmpty(repairdepartment)
					|| TextUtils.isEmpty(breakdown)
					|| TextUtils.isEmpty(breakdown)) {
				Toast.makeText(this, "保存失败，请完善信息", Toast.LENGTH_SHORT).show();
				return;
			}
			final String imgUri = uri + "#" + name;
			saveRepairInfo(deviceName, repairdepartment, breakdown, repairtime,
					imgUri);
			break;
		case R.id.btn_cancel:
			startActivity(new Intent(this, RepairHandlerAcrivity.class));
			finish();
			break;
		case R.id.et_repairtime:
			Date date=new Date();
			SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");
			DatePickerPopWindow popWindow=new DatePickerPopWindow(RepairInputActivity.this,df.format(date));
			WindowManager.LayoutParams lp=getWindow().getAttributes();
			lp.alpha=0.5f;
			getWindow().setAttributes(lp);
			popWindow.showAtLocation(etRepairtime, Gravity.CENTER, 0, 0);
			popWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
			popWindow.setOutsideTouchable(true);
			popWindow.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					WindowManager.LayoutParams lp=getWindow().getAttributes();
					lp.alpha=1f;
					getWindow().setAttributes(lp);
					System.out.println(sp.getString(Constants.BIRTHDAY, ""));
					etRepairtime.setText(sp.getString(Constants.BIRTHDAY, ""));
				}
			});
			
			break;

		}
	}

	private void photoGraph() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File dir = new File(Constants.FILEPATH);
				new DateFormat();
				name = DateFormat.format("yyyyMMdd_hhmmss",
						Calendar.getInstance(Locale.CHINA))
						+ ".jpg";
				if (!dir.exists())
					dir.mkdirs();

				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				File f = new File(dir, name);
				Uri u = Uri.fromFile(f);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
				startActivityForResult(intent, CAMERA);
			} catch (ActivityNotFoundException e) {
			}
		} else {
		}
	}

	private void saveRepairInfo(final String deviceName,
			final String repairdepartment, final String breakdown,
			final String repairtime, final String imgUri) {
		showProgressDialog("正在保存，请稍后...");
		final RepairInfo repairInfo = new RepairInfo();
		ThreadUtils.runInBackground(new Runnable() {

			@Override
			public void run() {

				repairInfo.RepairID = UUID.randomUUID().toString();
				repairInfo.RepairType = Application.mRepairType;
				if (Application.mRepairType.equals("5T")) {
					FiveTEqpt fiveTEqpt = fivetDao.getFiveTEqpt(deviceName);
					repairInfo.EqptID = fiveTEqpt.EqptID;
					repairInfo.EqptName = fiveTEqpt.EqptAddress;
					repairInfo.EqptType = fiveTEqpt.EqptType;
					repairInfo.ProbeStation = fiveTEqpt.ProbeStation;
					repairInfo.Specification = "";
					repairInfo.Manufacturer = "";
				} else if (Application.mRepairType.equals("Tech")) {
					EqptInfo eqptInfo = eqptDao.getEqptInfo(deviceName);
					repairInfo.EqptID = techDao
							.getTechEqptID(eqptInfo.EqptInfoID);
					repairInfo.EqptName = eqptInfo.EqptName;
					repairInfo.EqptType = "";
					repairInfo.ProbeStation = "";
					repairInfo.Specification = eqptInfo.EqptSpecif;
					repairInfo.Manufacturer = eqptInfo.Manufacturer;
				}
				repairInfo.FaultStatus = "未处理";
				repairInfo.UserID = userDao
						.getUserInfo(sp.getString("loginName", "")).user_id;
				repairInfo.UserDeptID = userDao
						.getUserInfo(sp.getString("loginName", "")).dept_id;
				repairInfo.FaultOccu_Time = repairtime;
				repairInfo.FaultAppearance = breakdown;
				repairInfo.IsUpload = 1;
				repairInfo.ImageUrl = imgUri;
				repairInfo.CreateDate = new SimpleDateFormat("HH:mm")
						.format(new Date());
				repairInfo.LastUpdateDate = new SimpleDateFormat("HH:mm")
						.format(new Date());
				repairInfo.IsStop = false;
				repairInfo.StopTime = "";
				repairInfo.StopHours = 0;
				repairInfo.StopMinutes = 0;
				repairInfo.RepairDeptID = deptDao
						.getDeptInfo(repairdepartment).dept_id;
				repairDao.addRepairSubmit(repairInfo);
				handler.sendEmptyMessageDelayed(FLUSH, 1000);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (resultCode == Constants.DEVICENAME) {
				String devicename = data.getStringExtra("devicename");
				etDevicename.setText(devicename);
			}
			if (resultCode == Constants.REPAIRDEPARTMENTLIST) {
				String repairdepartment = data
						.getStringExtra("repairdepartment");
				etRepairdepartment.setText(repairdepartment);
			}
		}
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CAMERA:
				File f = new File(Constants.FILEPATH + name);

				try {
					uri = Uri.parse(android.provider.MediaStore.Images.Media
							.insertImage(getContentResolver(),
									f.getAbsolutePath(), null, null));
					BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();  
				      bitmapOptions.inSampleSize = 10; 
					bmp = BitmapFactory.decodeStream(RepairInputActivity.this
							.getContentResolver().openInputStream(uri),null , bitmapOptions);
					ivSelimg.setImageBitmap(bmp);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != bmp && !bmp.isRecycled()) {
			bmp.recycle();
			bmp = null;
		}
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
