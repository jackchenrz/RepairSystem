package com.czvv.repairsystemmobile.utils;

public class BooleanAndintUtil {

	/**
	 * Boolean����ת��int��
	 * @param b
	 * @return
	 */
	public static int Boolean2int(Boolean b) {
		int i = 0;
		if (b) {
			i = 1;
		}
		return i;
	}

	/**
	 * int��ת��Boolean��
	 * @param i
	 * @return
	 */
	public static boolean int2Boolean(int i) {
		boolean b = false;
		if (i == 1) {
			b = true;
		}
		return b;
	}
}
