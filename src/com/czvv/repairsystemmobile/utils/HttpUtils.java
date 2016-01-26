package com.czvv.repairsystemmobile.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.Handler;
import android.os.Message;

public class HttpUtils {
	
	// ����5���̵߳��̳߳�
	private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

	/**
	 * 
	 * @param url WebService��������ַ
	 * @param namespace �����ռ�
	 * @param methodName WebService�ĵ��÷�����
	 * @param properties WebService�Ĳ���
	 * @param webServiceCallBack ���ؽ���ص��ӿ�
	 */
	public static void callService(String url,final String namespace,final String methodName,HashMap<String,String> properties,final WebServiceCallBack webServiceCallBack) {
		// ����HttpTransportSE���󣬴���WebService��������ַ
		final HttpTransportSE httpTransportSE = new HttpTransportSE(url);
		// ����SoapObject����
		SoapObject soapObject = new SoapObject(namespace, methodName);
		
		// SoapObject��Ӳ���
		if (properties != null) {
			for (Iterator<Map.Entry<String, String>> it = properties.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String, String> entry = it.next();
				soapObject.addProperty(entry.getKey(), entry.getValue());
			}
		}

		// ʵ����SoapSerializationEnvelope������WebService��SOAPЭ��İ汾��
		final SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// �����Ƿ���õ���.Net������WebService
		soapEnvelope.setOutputSoapObject(soapObject);
		soapEnvelope.dotNet = true;

		// �������߳������߳�ͨ�ŵ�Handler
		final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what == 0){
					webServiceCallBack.onSucced((SoapObject) msg.obj);
				}else{
					webServiceCallBack.onFailure((String)msg.obj);
				}
			}
		};

		// �����߳�ȥ����WebService
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				Object resultSoapObject = null;
				Message mgs = mHandler.obtainMessage();
				try {
					
					httpTransportSE.call(namespace + methodName, soapEnvelope);
					if (soapEnvelope.getResponse() != null) {
						// ��ȡ��������Ӧ���ص�SoapObject
						resultSoapObject =  soapEnvelope.bodyIn;
					}
					mgs.what = 0;
					mgs.obj = resultSoapObject;
					
				} catch (Exception e) {
					e.printStackTrace();
					mgs.what = 1;
					mgs.obj = e.getMessage();
				} 
				
				// ����ȡ����Ϣ����Handler���͵����߳�
				mHandler.sendMessage(mgs);
			}
		});
	}

	/**
	 * WebService�ص��ӿ�
	 *
	 */
	public interface WebServiceCallBack {
		
		public void onSucced(SoapObject result);
		
		public void onFailure(String result);
	}

}