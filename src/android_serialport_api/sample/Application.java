/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package android_serialport_api.sample;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class Application extends android.app.Application {
	private static Application instance;
	public static String mRepairType = "";
    private static final String LOG_DIR = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/repairsystem/log/";
    private static final String LOG_NAME = getCurrentDateString() + ".txt";

	public static Application getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		instance = this;
		super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(handler);
	}
	
	UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
		 
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            writeErrorLog(ex);
            android.os.Process.killProcess(android.os.Process.myPid());
//            Intent intent = new Intent(getApplicationContext(),
//                    CollapseActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            //exit();
        }
    };
	
	
	/**
     * ��ӡ������־
     * 
     * @param ex
     */
    protected void writeErrorLog(Throwable ex) {
        String info = null;
        ByteArrayOutputStream baos = null;
        PrintStream printStream = null;
        try {
            baos = new ByteArrayOutputStream();
            printStream = new PrintStream(baos);

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
            byte[] data = baos.toByteArray();
            info = new String(data);
            data = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (printStream != null) {
                    printStream.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("example", "������Ϣ\n" + info);
        File dir = new File(LOG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, LOG_NAME);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(info.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
 
    
}
