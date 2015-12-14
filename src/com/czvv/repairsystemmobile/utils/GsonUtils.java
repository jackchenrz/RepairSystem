package com.czvv.repairsystemmobile.utils;

import com.google.gson.Gson;

public class GsonUtils {

	/**
	 * json转成对象
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	public static <T> T getJsonBean(String jsonString, Class<T> cls) {
		T t = new Gson().fromJson(jsonString, cls);
		return t;
	}
	
	/**
	 * 将对象转成json
	 * @param t
	 * @return
	 */
	public static <T> String toJsonArray(T t){
		return new Gson().toJson(t);
	}
}
