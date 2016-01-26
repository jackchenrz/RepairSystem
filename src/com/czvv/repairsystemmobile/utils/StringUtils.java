package com.czvv.repairsystemmobile.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * �ַ���������
 */
public class StringUtils {

	/**
	 * ��ȡUUID
	 * @return 32UUIDСд�ַ���
	 */
	public static String gainUUID(){
		String strUUID = UUID.randomUUID().toString();
		strUUID = strUUID.replaceAll("-", "").toLowerCase();
		return strUUID;
	}
	
	/**
	 * �ж��ַ����Ƿ�ǿշ�null
	 * @param strParm ��Ҫ�жϵ��ַ���
	 * @return ���
	 */
    public static boolean isNoBlankAndNoNull(String strParm)
    {
      return ! ( (strParm == null) || (strParm.equals("")));
    }
    
    /**
     * ����ת���ַ���
     * @param is ������
     * @return
     * @throws Exception
     */
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    /**
     * ���ļ�ת���ַ���
     * @param file �ļ�
     * @return
     * @throws Exception
     */
    public static String getStringFromFile(File file) throws Exception {
        FileInputStream fin = new FileInputStream(file);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }
	
}
