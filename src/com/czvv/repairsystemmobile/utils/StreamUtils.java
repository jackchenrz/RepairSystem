package com.czvv.repairsystemmobile.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class StreamUtils {
	/**
	 * ���������е�����ת�����ַ���
	 * @param is
	 * @return
	 */
	public static String convertStream2Str(InputStream is){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len = -1;
		byte[] buffer = new byte[1024];
		try {
			while((len = is.read(buffer)) != -1){
				baos.write(buffer, 0, len);
			}
			
			return new String(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
