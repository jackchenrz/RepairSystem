package com.czvv.repairsystemmobile.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.base.BaseActivity;
import com.czvv.repairsystemmobile.utils.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class FeedBackActivity extends BaseActivity {
	@ViewInject(R.id.et_feedback)
	EditText etFeedBack;
	@ViewInject(R.id.et_num)
	EditText etNum;
	@ViewInject(R.id.tv_title)
	TextView tvTitle;
	@ViewInject(R.id.btn_submit)
	TextView btnSubmit;
	@Override
	public int bindLayout() {
		return R.layout.activity_feedback;
	}

	@Override
	public void initView(View view) {
		ViewUtils.inject(this);
		tvTitle.setText("意见反馈");
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String feedBack = etFeedBack.getText().toString().trim();
				String num = etNum.getText().toString().trim();
				if(TextUtils.isEmpty(feedBack) || TextUtils.isEmpty(num)){
					Toast.makeText(FeedBackActivity.this, "请完善信息",Toast.LENGTH_SHORT).show();
					return;
				}
				ToastUtils.mShowToast(FeedBackActivity.this, "已提交");
				finish();
			}
		});
	}

	@Override
	public void doBusiness(Context mContext) {
		
	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {
	}

}
