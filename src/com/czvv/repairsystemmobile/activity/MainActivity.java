package com.czvv.repairsystemmobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.adapter.ComFragmentAdapter;
import com.czvv.repairsystemmobile.base.BaseFragment;
import com.czvv.repairsystemmobile.fragment.MineFragment;
import com.czvv.repairsystemmobile.fragment.RepairFragment;
import com.czvv.repairsystemmobile.fragment.ToRepairFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MainActivity extends FragmentActivity implements OnCheckedChangeListener {
	
	@ViewInject(R.id.view_pager)
	ViewPager viewPager;
	@ViewInject(R.id.rg_main)
	RadioGroup rgMain;
	@ViewInject(R.id.rb_repair)
	RadioButton rbUpdate;
	@ViewInject(R.id.rb_torepair)
	RadioButton rbBusiness;
	@ViewInject(R.id.rb_mine)
	RadioButton rbMine;
	
	private List<BaseFragment> pagerList;
	private ComFragmentAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		doBusiness(this);
	}
	
	/**
	 * pagerList
	 * @return
	 */
	public List<BaseFragment> getPageList(){
		return pagerList;
	}

	private void doBusiness(Context mContext) {
		rgMain.setOnCheckedChangeListener(this);
		rgMain.check(R.id.rb_repair);
		pagerList = new ArrayList<BaseFragment>();
		pagerList.add(new RepairFragment());
		pagerList.add(new ToRepairFragment());
		pagerList.add(new MineFragment());
		adapter = new ComFragmentAdapter(getSupportFragmentManager(),pagerList);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				if(arg0 == 0){
					rgMain.check(R.id.rb_repair);
				}else if(arg0 == 1){
					rgMain.check(R.id.rb_torepair);
				}else if(arg0 == 2){
					rgMain.check(R.id.rb_mine);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int index = 0;
		
		switch (checkedId) {
		case R.id.rb_repair:
			index = 0;
			rbUpdate.setTextColor(this.getResources().getColor(R.color.bottomcolor));
			rbBusiness.setTextColor(this.getResources().getColor(R.color.darkblack));
			rbMine.setTextColor(this.getResources().getColor(R.color.darkblack));
			break;
		case R.id.rb_torepair:
			index = 1;
			rbBusiness.setTextColor(this.getResources().getColor(R.color.bottomcolor));
			rbUpdate.setTextColor(this.getResources().getColor(R.color.darkblack));
			rbMine.setTextColor(this.getResources().getColor(R.color.darkblack));
			break;
		case R.id.rb_mine:
			index = 2;
			rbMine.setTextColor(this.getResources().getColor(R.color.bottomcolor));
			rbBusiness.setTextColor(this.getResources().getColor(R.color.darkblack));
			rbUpdate.setTextColor(this.getResources().getColor(R.color.darkblack));
			break;
		}
		viewPager.setCurrentItem(index);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/* ·µ»Ø¼ü */
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MainActivity.this.finish();
			overridePendingTransition(0, R.anim.base_slide_right_out);
		}
		return false;
	}

}
