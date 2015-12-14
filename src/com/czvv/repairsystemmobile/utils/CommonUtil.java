package com.czvv.repairsystemmobile.utils;

import java.util.Random;

public class CommonUtil {
	/**
	 * 生成随机码
	 * 
	 * @param length随机码个数
	 * @return
	 */
	public static String getRandomCode(int length) {
		int rand;
		char code;
		String randomcode = "";

		// 生成一定长度的验证码
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			rand = random.nextInt(10000);
			if (rand % 3 == 0) {
				code = (char) ('A' + (rand % 26));
			} else {
				code = (char) ('0' + (rand % 10));
			}
			// 如果是O，则重新生成
			if ("O".equals(code)) {
				i--;
				continue;
			}
			randomcode += code;
		}
		return randomcode;
	}

	/**
	 * 获取字符串的长度，如果有中文，则每个中文字符计为2位
	 * 
	 * @param value
	 *            指定的字符串
	 * @return 字符串的长度
	 */
	public static int getStringLength(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}
}
