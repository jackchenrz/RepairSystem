package com.czvv.repairsystemmobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.czvv.repairsystemmobile.activity.LoginActivity;

public class MApplication extends Application {

	/**�����ṩ����Ӧ���������ڵ�Context**/
	private static Context instance;
	public static String mRepairType = "";
	/**����Ӧ��ȫ�ֿɷ������ݼ���**/
	private static Map<String, Object> gloableData = new HashMap<String, Object>();
	private final Stack<WeakReference<Activity>> activitys = new Stack<WeakReference<Activity>>();
	private static final String LOG_DIR = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/documentsystem/log/";
	private static final String LOG_NAME = getCurrentDateString() + ".txt";
	/**
	 * �����ṩApplication Context
	 * @return
	 */
	public static Context gainContext() {
		return instance;
	}

	public void onCreate() {
		super.onCreate();
		instance = this;
//		Thread.setDefaultUncaughtExceptionHandler(handler);
	}
	UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
		 
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
        	writeErrorLog(ex);
			Intent intent = new Intent();
			intent.setClass(instance, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			instance.startActivity(intent);
			android.os.Process.killProcess(android.os.Process.myPid());
        }
	};

	/**
	 * ��Application�������ݣ����������5����
	 * @param strKey �������Key
	 * @param strValue ���ݶ���
	 */
	public static void assignData(String strKey, Object strValue) {
//		if (gloableData.size() > 8) {
//			throw new RuntimeException("�������������");
//		}
		gloableData.put(strKey, strValue);
	}

	/**
	 * ��Applcaiton��ȡ����
	 * @param strKey �������Key
	 * @return ��ӦKey�����ݶ���
	 */
	public static Object gainData(String strKey) {
		return gloableData.get(strKey);
	}
	
	/*
	 * ��Application���Ƴ�����
	 */
	public static void removeData(String key){
		if(gloableData.containsKey(key)) gloableData.remove(key);
	}

	/**
	 * ��Activityѹ��Applicationջ
	 * @param task ��Ҫѹ��ջ��Activity����
	 */
	public  void pushTask(WeakReference<Activity> task) {
		activitys.push(task);
	}

	/**
	 * �������Activity�����ջ���Ƴ�
	 * @param task
	 */
	public  void removeTask(WeakReference<Activity> task) {
		activitys.remove(task);
	}

	/**
	 * ����ָ��λ�ô�ջ���Ƴ�Activity
	 * @param taskIndex Activityջ����
	 */
	public  void removeTask(int taskIndex) {
		if (activitys.size() > taskIndex)
			activitys.remove(taskIndex);
	}

	/**
	 * ��ջ��Activity�Ƴ���ջ��
	 */
	public  void removeToTop() {
		int end = activitys.size();
		int start = 1;
		for (int i = end - 1; i >= start; i--) {
			if (!activitys.get(i).get().isFinishing()) {     
				activitys.get(i).get().finish(); 
		    }
		}
	}

	/**
	 * �Ƴ�ȫ������������Ӧ���˳���
	 */
	public  void removeAll() {
		//finish���е�Activity
		for (WeakReference<Activity> task : activitys) {
			if (!task.get().isFinishing()) {     
				task.get().finish(); 
		    }  
		}
	}
	
	
	/**
     * ��ӡ������־
     * 
     * @param ex
     */
    protected void writeErrorLog(Throwable ex) {
         FileOutputStream fileOutputStream = null;
        PrintStream printStream = null;
        try {
        	File dir = new File(LOG_DIR);
        	if (!dir.exists()) {
        		dir.mkdirs();
        	}
        	File file = new File(dir, LOG_NAME);
        	if (!file.exists()) {
        		file.createNewFile();
        	}
        	fileOutputStream = new FileOutputStream(file, true);
            printStream = new PrintStream(fileOutputStream);

			// ��д���ֻ�����Ϣ
			Class<?> clazz = Class.forName("android.os.Build");
			Field[] fields = clazz.getFields();// ������е���Ա����
			for (Field field : fields) {
				String name = field.getName(); // ��ó�Ա����������
				Object value = field.get(null); // ��ó�Ա������ֵ
				printStream.println(name+" : "+value); // ����Ա��������Ϣ��д����־�ļ�
			}
			printStream.println("=============����һ���ָ���=====================");
            ex.printStackTrace(printStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (printStream != null) {
                	printStream.flush();
                    printStream.close();
                }
                if(fileOutputStream != null){
                	 fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * ��ȡ��ǰ����
     * 
     * @return    
     */
    private static String getCurrentDateString() {
        String result = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        Date nowDate = new Date();
        result = sdf.format(nowDate);
        return result;
    }
	/*******************************************Application�д�ŵ�Activity������ѹջ/��ջ��API��������*****************************************/
}
