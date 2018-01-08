package com.huiyoumall.uu.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Gson解析 工具类
 * 
 * @author ASUS
 * 
 */
public class GsonTools {
	public GsonTools() {
	}

	public static <T> T getDate(String jsonString, Class<T> cls) {
		T t = null;
		try {
			Gson gson = new Gson();
			t = gson.fromJson(jsonString, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	public static <T> List<T> getListDate(String jsonString, Class<T> cls)
			throws Exception {
		List<T> list = new ArrayList<T>();
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	public static List<String> getListString(String jsoString) {
		List<String> list = new ArrayList<String>();
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsoString, new TypeToken<List<String>>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<Map<String, Object>> getlistMap(String jsonString) {
		List<Map<String, Object>> listMaps = new ArrayList<Map<String, Object>>();
		try {
			Gson gson = new Gson();
			listMaps = gson.fromJson(jsonString,
					new TypeToken<List<Map<String, Object>>>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMaps;
	}
}
