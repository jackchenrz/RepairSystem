package com.czvv.repairsystemmobile.utils;

import java.util.Random;

public class CommonUtil {
	/**
	 * ���������
	 * 
	 * @param length��������
	 * @return
	 */
	public static String getRandomCode(int length) {
		int rand;
		char code;
		String randomcode = "";

		// ����һ�����ȵ���֤��
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			rand = random.nextInt(10000);
			if (rand % 3 == 0) {
				code = (char) ('A' + (rand % 26));
			} else {
				code = (char) ('0' + (rand % 10));
			}
			// �����O������������
			if ("O".equals(code)) {
				i--;
				continue;
			}
			randomcode += code;
		}
		return randomcode;
	}

	/**
	 * ��ȡ�ַ����ĳ��ȣ���������ģ���ÿ�������ַ���Ϊ2λ
	 * 
	 * @param value
	 *            ָ�����ַ���
	 * @return �ַ����ĳ���
	 */
	public static int getStringLength(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* ��ȡ�ֶ�ֵ�ĳ��ȣ�����������ַ�����ÿ�������ַ�����Ϊ2������Ϊ1 */
		for (int i = 0; i < value.length(); i++) {
			/* ��ȡһ���ַ� */
			String temp = value.substring(i, i + 1);
			/* �ж��Ƿ�Ϊ�����ַ� */
			if (temp.matches(chinese)) {
				/* �����ַ�����Ϊ2 */
				valueLength += 2;
			} else {
				/* �����ַ�����Ϊ1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}
}
