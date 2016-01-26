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
			AlertUtils.dialog(getActivity(), "��ܰ��ʾ", "��ǰ�汾�������°汾��",new DialogInterface.OnClickListener() {
				
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
			loadingDialog = AlertUtils.createLoadingDialog(getActivity(), "���ڼ�飬���Ժ�...");
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
	 * �Զ��岼�ֵ�popupWindow
	 */
	@SuppressLint("NewApi")
	private void popWindow(){
        LayoutInflater inflater = LayoutInflater.from(getActivity());//��ȡһ�������
        View view = inflater.inflate(R.layout.popup_out, null);//��������Զ���Ĳ���
        
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
        
        Display display = getActivity().getWindowManager().getDefaultDisplay();//�õ���ǰ��Ļ����ʾ������
        Point size = new Point();//����һ��Point���������������Ļ�ߴ���Ϣ
        display.getSize(size);//Point�������յ�ǰ�豸��Ļ�ߴ���Ϣ
        int width = size.x;//��Point������л�ȡ��Ļ�Ŀ��(��λ����)
        int height = size.y;//��Point������л�ȡ��Ļ�ĸ߶�(��λ����)
        //����һ��PopupWindow���󣬵ڶ������������ÿ�ȵģ��øոջ�ȡ������Ļ��ȳ���2/3��ȡ����Ļ��2/3��ȣ��Ӷ����κ��豸�ж��������䣬�߶���������ݼ��ɣ����һ�����������õõ�����
        PopupWindow popWindow = new PopupWindow(view, 2*width/3, LayoutParams.WRAP_CONTENT, true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());//����PopupWindow�ı���Ϊһ���յ�Drawable��������������������ôPopupWindow��������޷��˳���
        popWindow.setOutsideTouchable(true);//�����Ƿ���PopupWindow���˳�PopupWindow
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();//������ǰ�����һ����������
        params.alpha = 0.5f;//���ò�����͸����Ϊ0.8��͸����ȡֵΪ0~1��1Ϊ��ȫ��͸����0Ϊ��ȫ͸������Ϊandroid��Ĭ�ϵ���Ļ��ɫ���Ǵ���ɫ�ģ������������Ϊ1����ô���������Ǻ�ɫ������Ϊ0��������ʾ���ǵĵ�ǰ����
        getActivity().getWindow().setAttributes(params);//�Ѹò����������ý���ǰ������
        popWindow.setOnDismissListener(new OnDismissListener() {//����PopupWindow�˳�������
            @Override
            public void onDismiss() {//���PopupWindow��ʧ�ˣ����˳��ˣ���ô�������¼���Ȼ��ѵ�ǰ�����͸��������Ϊ��͸��
                WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.alpha = 1.0f;//����Ϊ��͸�������ָ�ԭ���Ľ���
                getActivity().getWindow().setAttributes(params);
            }
        });
        //��һ������Ϊ��View���󣬼�PopupWindow���ڵĸ��ؼ����󣬵ڶ�������Ϊ�������ģ����������ֱ�Ϊx���y���ƫ����
        popWindow.showAtLocation(inflater.inflate(R.layout.activity_main, null), Gravity.CENTER, 0, 0);
 
    }
}
