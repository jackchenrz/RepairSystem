package com.czvv.repairsystemmobile.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;

import com.czvv.repairsystemmobile.MApplication;
import com.czvv.repairsystemmobile.R;
import com.czvv.repairsystemmobile.activity.AboutActivity;
import com.czvv.repairsystemmobile.activity.FeedBackActivity;
import com.czvv.repairsystemmobile.base.BaseFragment;
import com.czvv.repairsystemmobile.utils.AlertUtils;
import com.czvv.repairsystemmobile.utils.ThreadUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MineFragment extends BaseFragment implements OnClickListener {
	
	
	@ViewInject(R.id.rl_checkversion)
	RelativeLayout rlCheckversion;
	@ViewInject(R.id.rl_feedback)
	RelativeLayout rlFeedback;
	@ViewInject(R.id.rl_about)
	RelativeLayout rlAbout;
	@ViewInject(R.id.rl_logout)
	RelativeLayout rlLogout;
	protected MApplication mApplication = null;
	
	private final int VERSION = 100;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			loadingDialog.dismiss();
			AlertUtils.dialog(getActivity(), "温馨提示", "当前版本已是最新版本！",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		};
	};
	private Dialog loadingDialog;
	@Override
	public View initView() {
		View view = View.inflate(getActivity(), R.layout.fragment_mine, null);
		ViewUtils.inject(this,view);
		rlCheckversion.setOnClickListener(this);
		rlFeedback.setOnClickListener(this);
		rlAbout.setOnClickListener(this);
		rlLogout.setOnClickListener(this);
		return view;
	}

	@Override
	public void initData() {
		mApplication = (MApplication) getActivity().getApplicationContext();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_checkversion:
			loadingDialog = AlertUtils.createLoadingDialog(getActivity(), "正在检查，请稍后...");
			loadingDialog.show();
			ThreadUtils.runInBackground(new Runnable() {
				
				@Override
				public void run() {
					SystemClock.sleep(2000);
					handler.sendEmptyMessage(VERSION);
				}
			});
			break;
		case R.id.rl_feedback:
			Intent intent = new Intent(getActivity(),FeedBackActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
			break;
		case R.id.rl_about:
			Intent intent1 = new Intent(getActivity(),AboutActivity.class);
			startActivity(intent1);
			getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
			break;
		case R.id.rl_logout:
			popWindow();
			break;
		}
	}
	
	/**
	 * 自定义布局的popupWindow
	 */
	@SuppressLint("NewApi")
	private void popWindow(){
        LayoutInflater inflater = LayoutInflater.from(getActivity());//获取一个填充器
        View view = inflater.inflate(R.layout.popup_out, null);//填充我们自定义的布局
        
        LinearLayout llLogout = (LinearLayout) view.findViewById(R.id.ll_logout);
        LinearLayout llClose = (LinearLayout) view.findViewById(R.id.ll_close);
        
        llLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
        
        llClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
				mApplication.removeAll();
			}
		});
        
        Display display = getActivity().getWindowManager().getDefaultDisplay();//得到当前屏幕的显示器对象
        Point size = new Point();//创建一个Point点对象用来接收屏幕尺寸信息
        display.getSize(size);//Point点对象接收当前设备屏幕尺寸信息
        int width = size.x;//从Point点对象中获取屏幕的宽度(单位像素)
        int height = size.y;//从Point点对象中获取屏幕的高度(单位像素)
        //创建一个PopupWindow对象，第二个参数是设置宽度的，用刚刚获取到的屏幕宽度乘以2/3，取该屏幕的2/3宽度，从而在任何设备中都可以适配，高度则包裹内容即可，最后一个参数是设置得到焦点
        PopupWindow popWindow = new PopupWindow(view, 2*width/3, LayoutParams.WRAP_CONTENT, true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());//设置PopupWindow的背景为一个空的Drawable对象，如果不设置这个，那么PopupWindow弹出后就无法退出了
        popWindow.setOutsideTouchable(true);//设置是否点击PopupWindow外退出PopupWindow
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();//创建当前界面的一个参数对象
        params.alpha = 0.5f;//设置参数的透明度为0.8，透明度取值为0~1，1为完全不透明，0为完全透明，因为android中默认的屏幕颜色都是纯黑色的，所以如果设置为1，那么背景将都是黑色，设置为0，背景显示我们的当前界面
        getActivity().getWindow().setAttributes(params);//把该参数对象设置进当前界面中
        popWindow.setOnDismissListener(new OnDismissListener() {//设置PopupWindow退出监听器
            @Override
            public void onDismiss() {//如果PopupWindow消失了，即退出了，那么触发该事件，然后把当前界面的透明度设置为不透明
                WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.alpha = 1.0f;//设置为不透明，即恢复原来的界面
                getActivity().getWindow().setAttributes(params);
            }
        });
        //第一个参数为父View对象，即PopupWindow所在的父控件对象，第二个参数为它的重心，后面两个分别为x轴和y轴的偏移量
        popWindow.showAtLocation(inflater.inflate(R.layout.activity_main, null), Gravity.CENTER, 0, 0);
 
    }
}
