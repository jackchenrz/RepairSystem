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
	 * 服务器得到json
	 * @param url
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
	public static String getJsonArray(String url, String methodName)
			throws Exception {

		String jsonArray = null;
		// 根据命名空间和方法得到SoapObject对象
		SoapObject soapObject = new SoapObject(Constants.SERVICE_NAMESPACE,
				methodName);
		SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		// 将soapObject对象设置为envelop对象，传出消息
		envelop.dotNet = true;
		envelop.setOutputSoapObject(soapObject);
		HttpTransportSE httpSE = new HttpTransportSE(url);
		// 开始调用远程方法
		httpSE.call(Constants.SERVICE_NAMESPACE + methodName, envelop);
		// 得到远程方法返回的SOAP对象
		SoapObject resultObj = (SoapObject) envelop.bodyIn;
		// 得到服务器传回的数据
		jsonArray = resultObj.getProperty(0).toString();
		return jsonArray;
	}

	/**
	 * 上传json到服务器
	 * @param jsonArr
	 * @param url
	 * @param methodName
	 * @throws Exception
	 */
	public static void toJsonArray(String jsonArr, String url, String methodName)
			throws Exception {

		SoapObject soapObject = new SoapObject(Constants.SERVICE_NAMESPACE,
				methodName);

		soapObject.addProperty("strJson", jsonArr); // 参数

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
	 * 上传图片到服务器
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

		String uploadBuffer = new String(Base64.encode(baos.toByteArray())); // 进行Base64编码

		connectWebService(substring, url, uploadBuffer);

		fis.close();

	}

	public static void connectWebService(String substring, String url,
			String imageBuffer) throws Exception {

		// 以下就是 调用过程了，不明白的话 请看相关webservice文档

		SoapObject soapObject = new SoapObject(Constants.SERVICE_NAMESPACE,
				Constants.SERVICE_UPLOADIMAGE);

		soapObject.addProperty("FileName", substring); // 参数1 图片名

		soapObject.addProperty("image", imageBuffer); // 参数2 图片字符串

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
