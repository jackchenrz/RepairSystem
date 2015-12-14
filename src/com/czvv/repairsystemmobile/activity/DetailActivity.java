package com.czvv.repairsystemmobile.activity;

import java.io.FileNotFoundException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.bean.RepairInfo;
import com.czvv.repairsystemmobile.dao.Sys_deptDao;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class DetailActivity extends RepairBaseActivity {

	@ViewInject(R.id.iv_pro)
	ImageView ivPro;
	@ViewInject(R.id.tv_devicename)
	TextView tvDeviceName;
	@ViewInject(R.id.tv_repair)
	TextView tvRepair;
	@ViewInject(R.id.tv_detail)
	TextView tvDetail;
	@ViewInject(R.id.tv_repaireqpt)
	TextView tv_repaireqpt;
	@ViewInject(R.id.btnBack)
	TextView btnBack;
	private RepairInfo repairInfo;
	private Bitmap bmp;
	private Sys_deptDao deptDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		ViewUtils.inject(this);
		deptDao = Sys_deptDao.getInstance(this);

		Intent intent = getIntent();
		repairInfo = (RepairInfo) intent.getSerializableExtra("repairInfo");
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		fillData();
	}

	private void fillData() {
		try {
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();  
		      bitmapOptions.inSampleSize = 4; 
			bmp = BitmapFactory.decodeStream(DetailActivity.this
					.getContentResolver().openInputStream(
							Uri.parse(repairInfo.ImageUrl)),null , bitmapOptions);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ivPro.setImageBitmap(bmp);
		tvDeviceName.setText("�豸���ƣ�" + repairInfo.EqptName);
		tvRepair.setText("���Ϸ���ʱ�䣺" + repairInfo.FaultOccu_Time);
		tvDetail.setText("�������飺" + repairInfo.FaultAppearance);
		tv_repaireqpt.setText("ά�޲��ţ�"+ deptDao.getDeptInfo1(repairInfo.RepairDeptID).dept_name);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != bmp && !bmp.isRecycled()) {
			bmp.recycle();
			bmp = null;
		}
	}

}
