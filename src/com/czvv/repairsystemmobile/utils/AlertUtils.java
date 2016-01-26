
package com.czvv.repairsystemmobile.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.czvv.repairsystemmobile.R;

/**
 * �Ի��򹤾���
 */
public class AlertUtils {
	
	
	public static Dialog createLoadingDialog(Context context, String msg) {  
		  
        LayoutInflater inflater = LayoutInflater.from(context);  
        View v = inflater.inflate(R.layout.loading_dialog, null);// �õ�����view  
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// ���ز���  
        // main.xml�е�ImageView  
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);  
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// ��ʾ����  
        // ���ض���  
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(  
                context, R.anim.load_animation);  
        // ʹ��ImageView��ʾ����  
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);  
        tipTextView.setText(msg);// ���ü�����Ϣ  
  
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// �����Զ�����ʽdialog  
  
        loadingDialog.setCancelable(false);// �������á����ؼ���ȡ��  
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(  
                LinearLayout.LayoutParams.MATCH_PARENT,  
                LinearLayout.LayoutParams.MATCH_PARENT));// ���ò���  
        return loadingDialog;  
  
    }  
	private static ProgressDialog mProgressDialog;
	
	/**
	 * ��ʾProgressDialog
	 * @param context ������
	 * @param message ��Ϣ
	 */
	public static void loading(Context context, String message){
		
		loading(context,message,true);
	}
	
	/**
	 * ��ʾProgressDialog
	 * @param context ������
	 * @param message ��Ϣ
	 */
	public static void loading(Context context, String message,final ILoadingOnKeyListener listener){
		
		loading(context,message,true,listener);
	}
	
	/**
	 * ��ʾProgressDialog
	 * @param context ������
	 * @param message ��Ϣ
	 * @param cancelable �Ƿ����ȡ��
	 */
	public static void loading(Context context, String message,boolean cancelable){
		
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage(message);
			mProgressDialog.setCancelable(cancelable);
		}
		if(mProgressDialog.isShowing()){mProgressDialog.cancel();mProgressDialog.dismiss();}
		mProgressDialog.show();
	}
	
	/**
	 * ��ʾProgressDialog
	 * @param context ������
	 * @param message ��Ϣ
	 */
	public static void loading(Context context, String message,boolean cancelable ,final ILoadingOnKeyListener listener){
		
		if(mProgressDialog == null){
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage(message);
			mProgressDialog.setCancelable(cancelable);
		}
		
		if(mProgressDialog.isShowing()){mProgressDialog.cancel();mProgressDialog.dismiss();}
		
		if(null != listener)
		{
			mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
	            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
	            	listener.onKey(dialog, keyCode, event);
	                return false;
	            }
	        });
		}else{
			mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
	            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
	                if (keyCode == KeyEvent.KEYCODE_BACK) {
	                	mProgressDialog.dismiss();
	                }
	                return false;
	            }
	        });
		}
		
		mProgressDialog.show();
	}
	
	/**
	 * �жϼ��ضԻ����Ƿ����ڼ���
	 * @return �Ƿ�
	 */
	public static boolean isLoading(){
		
		if(null != mProgressDialog){
			return mProgressDialog.isShowing();
		}else{
			return false;
		}
	}
	
	/**
	 * �ر�ProgressDialog
	 */
	public static void closeLoading(){
		if(mProgressDialog != null){
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
	
	/**
	 * ����ProgressDialog������Ϣ
	 * @param message ��Ϣ
	 */
	public static void updateProgressText(String message){
		if(mProgressDialog == null ) return ;
		
		if(mProgressDialog.isShowing()){
			mProgressDialog.setMessage(message);
		}
	}
	
    /**
     * �����Ի���
     * @param title �Ի������
     * @param msg  �Ի�������
     * @param okBtnListenner ȷ����ť����¼�
     * @param cancelBtnListenner ȡ����ť����¼�
     */
    public static AlertDialog dialog(Context context,String msg) {
    	return dialog(context,"",msg);
    }
    
    /**
     * �����Ի���
     * @param title �Ի������
     * @param msg  �Ի�������
     * @param okBtnListenner ȷ����ť����¼�
     * @param cancelBtnListenner ȡ����ť����¼�
     */
    public static AlertDialog dialog(Context context,String title,String msg) {
    	return dialog(context,title,msg,null);
    }
    
    /**
     * �����Ի���
     * @param title �Ի������
     * @param msg  �Ի�������
     * @param okBtnListenner ȷ����ť����¼�
     * @param cancelBtnListenner ȡ����ť����¼�
     */
    public static AlertDialog dialog(Context context,String title,String msg,OnClickListener okBtnListenner) {
    	return dialog(context,title,msg,okBtnListenner,null);
    }
    
    /**
     * �����Ի���
     * @param title �Ի������
     * @param msg  �Ի�������
     * @param okBtnListenner ȷ����ť����¼�
     * @param cancelBtnListenner ȡ����ť����¼�
     */
    public static AlertDialog dialog(Context context,String title,String msg,OnClickListener okBtnListenner,OnClickListener cancelBtnListenner) {
    	return dialog(context,null,title,msg,okBtnListenner,cancelBtnListenner);
    }
    
    /**
     * �����Ի���
     * @param title �Ի������
     * @param msg  �Ի�������
     * @param okBtnListenner ȷ����ť����¼�
     * @param cancelBtnListenner ȡ����ť����¼�
     */
    public static AlertDialog dialog(Context context,Drawable icon,String title,String msg) {
    	return dialog(context,icon,title,msg,null);
    }
    
    /**
     * �����Ի���
     * @param title �Ի������
     * @param msg  �Ի�������
     * @param okBtnListenner ȷ����ť����¼�
     * @param cancelBtnListenner ȡ����ť����¼�
     */
    public static AlertDialog dialog(Context context,Drawable icon,String title,String msg,OnClickListener okBtnListenner) {
    	return dialog(context,icon,title,msg,okBtnListenner,null);
    }
	
    /**
     * �����Ի���
     * @param icon  ����ͼ��
     * @param title �Ի������
     * @param msg  �Ի�������
     * @param okBtnListenner ȷ����ť����¼�
     * @param cancelBtnListenner ȡ����ť����¼�
     */
    public static AlertDialog dialog(Context context,Drawable icon,String title,String msg, OnClickListener okBtnListenner,OnClickListener cancelBtnListenner) {
        Builder dialogBuilder = new AlertDialog.Builder(context);
        if(null != icon){
        	dialogBuilder.setIcon(icon);
        }
        if(StringUtils.isNoBlankAndNoNull(title)){
            dialogBuilder.setTitle(title);
        }
        dialogBuilder.setMessage(msg);
        if(null != okBtnListenner){
        	dialogBuilder.setPositiveButton(android.R.string.ok, okBtnListenner);
        }
        if(null != cancelBtnListenner){
        	dialogBuilder.setNegativeButton(android.R.string.cancel, cancelBtnListenner);
        }
        dialogBuilder.create();
        return dialogBuilder.show();
    }
    
    /**
     * ����һ���Զ��岼�ֶԻ���
     * @param context ������
     * @param view �Զ��岼��View
     */
	public static AlertDialog dialog(Context context,View view) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		return builder.show();
	}
	
    /**
     * ����һ���Զ��岼�ֶԻ���
     * @param context ������
     * @param resId �Զ��岼��View��Ӧ��layout id
     */
	public static AlertDialog dialog(Context context,int resId) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(resId, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		return builder.show();
	}
    
	 /**
     * ����Pop���ڣ��������Ƿ��������ط��رմ��ڣ�
     * @param context ��������������
     * @param anchor ����pop����Ŀؼ�
     * @param viewId pop���ڽ���layout
     * @param xoff ����Xƫ����
     * @param yoff ����Yƫ����
     * @param outSideTouchable ��������ط��Ƿ�رմ���
     */
    public static PopupWindow popwindow(Context context,View anchor,int viewId,int xoff,int yoff,boolean outSideTouchable){
        ViewGroup menuView = (ViewGroup) LayoutInflater.from(context).inflate(viewId, null);
        PopupWindow pw = new PopupWindow(menuView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setTouchable(outSideTouchable);
        pw.setFocusable(outSideTouchable);
        pw.setOutsideTouchable(outSideTouchable);
//        pw.showAsDropDown(anchor, xoff, yoff);
        pw.showAtLocation(anchor, Gravity.CENTER, xoff, yoff);
        pw.update();
        return pw;
    }
    
    
    /**
     * ����Pop���ڣ��������Ƿ��������ط��رմ��ڣ�
     * @param anchor ����pop����Ŀؼ�
     * @param popView pop���ڽ���
     * @param xoff ����Xƫ����
     * @param yoff ����Yƫ����
     * @param outSideTouchable ��������ط��Ƿ�رմ���
     */
    public static PopupWindow popwindow(View anchor,View popView,int xoff,int yoff,boolean outSideTouchable){
        PopupWindow pw = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(outSideTouchable);
        pw.showAsDropDown(anchor, xoff, yoff);
        pw.update();
        
        return pw;
    }    
    /**
     * Loading�����Ի���
     */
    public interface ILoadingOnKeyListener{
    	 public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event);
    }
}
