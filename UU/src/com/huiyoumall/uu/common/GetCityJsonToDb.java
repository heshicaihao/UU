package com.huiyoumall.uu.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huiyoumall.uu.entity.CityItem;

/**
 * ��ȡ����Ŀ�е�json��������List ������
 * 
 * @author ASUS
 * 
 */
public class GetCityJsonToDb {

	private final static String fileName = "city4.json";

	/**
	 * ��ȡ�����ļ���JSON�ַ���
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getJson(Context context, String fileName) {

		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					context.getAssets().open(fileName), "UTF-8"));
			String line;
			while ((line = bf.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	public static List<CityItem> getDatas(Context context) {
		String jsonString = getJson(context, fileName);
		Gson gson = new Gson();
		return gson.fromJson(jsonString, new TypeToken<List<CityItem>>() {
		}.getType());
	}

	public static String getDatas(Context context, String fileName) {
		String jsonString = getJson(context, fileName);
		return jsonString;
	}
}
