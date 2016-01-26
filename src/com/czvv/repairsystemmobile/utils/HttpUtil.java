package com.czvv.repairsystemmobile.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.czvv.repairsystemmobile.Constants;

public class HttpUtil {

	/**
	 * �������õ�json
	 * @param url
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
	public static String getJsonArray(String url, String methodName)
			throws Exception {

		String jsonArray = null;
		// ���������ռ�ͷ����õ�SoapObject����
		SoapObject soapObject = new SoapObject(Constants.SERVICE_NAMESPACE,
				methodName);
		SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		// ��soapObject��������Ϊenvelop���󣬴�����Ϣ
		envelop.dotNet = true;
		envelop.setOutputSoapObject(soapObject);
		HttpTransportSE httpSE = new HttpTransportSE(url);
		// ��ʼ����Զ�̷���
		httpSE.call(Constants.SERVICE_NAMESPACE + methodName, envelop);
		// �õ�Զ�̷������ص�SOAP����
		SoapObject resultObj = (SoapObject) envelop.bodyIn;
		// �õ����������ص�����
		jsonArray = resultObj.getProperty(0).toString();
		return jsonArray;
	}

	/**
	 * �ϴ�json��������
	 * @param jsonArr
	 * @param url
	 * @param methodName
	 * @throws Exception
	 */
	public static void toJsonArray(String jsonArr, String url, String methodName)
			throws Exception {

		SoapObject soapObject = new SoapObject(Constants.SERVICE_NAMESPACE,
				methodName);

		soapObject.addProperty("strJson", jsonArr); // ����

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(

		SoapEnvelope.VER11);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(soapObject);

		HttpTransportSE httpTranstation = new HttpTransportSE(url);

		httpTranstation
				.call(Constants.SERVICE_NAMESPACE + methodName, envelope);
		Object resultObj = envelope.bodyIn;
	}

	
	/**
	 * �ϴ�ͼƬ��������
	 * @param fileName
	 * @param url
	 * @throws Exception
	 */
	public static void Uploadimg(String fileName, String url) throws Exception {

		String substring = fileName.substring(fileName.lastIndexOf("/") + 1,
				fileName.lastIndexOf("."));

		FileInputStream fis = new FileInputStream(fileName);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];

		int count = 0;

		while ((count = fis.read(buffer)) >= 0) {

			baos.write(buffer, 0, count);

		}

		String uploadBuffer = new String(Base64.encode(baos.toByteArray())); // ����Base64����

		connectWebService(substring, url, uploadBuffer);

		fis.close();

	}

	public static void connectWebService(String substring, String url,
			String imageBuffer) throws Exception {

		// ���¾��� ���ù����ˣ������׵Ļ� �뿴���webservice�ĵ�

		SoapObject soapObject = new SoapObject(Constants.SERVICE_NAMESPACE,
				Constants.SERVICE_UPLOADIMAGE);

		soapObject.addProperty("FileName", substring); // ����1 ͼƬ��

		soapObject.addProperty("image", imageBuffer); // ����2 ͼƬ�ַ���

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(

		SoapEnvelope.VER11);

		envelope.dotNet = true;

		envelope.setOutputSoapObject(soapObject);

		HttpTransportSE httpTranstation = new HttpTransportSE(url);

		httpTranstation.call(Constants.SERVICE_NAMESPACE
				+ Constants.SERVICE_UPLOADIMAGE, envelope);
		SoapObject resultObj = (SoapObject) envelope.bodyIn;
	}

}
