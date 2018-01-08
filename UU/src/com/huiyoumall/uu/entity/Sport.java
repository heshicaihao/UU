package com.huiyoumall.uu.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.huiyoumall.uu.common.GsonTools;
import com.huiyoumall.uu.util.StringUtils;

/**
 * �˶���Ŀ ʵ����
 * 
 * @author ����Ԩ
 * 
 */
public class Sport implements Serializable {

	/**
	 * sport_id --- id sport_name --- �˶���Ŀ�� sport_logo --- logoͼ
	 */
	private int sport_id;// id
	private String sport_name;// �˶���Ŀ��

	public int getSport_id() {
		return sport_id;
	}

	public void setSport_id(int sport_id) {
		this.sport_id = sport_id;
	}

	public String getSport_name() {
		return sport_name;
	}

	public void setSport_name(String sport_name) {
		this.sport_name = sport_name;
	}

	public static List<Sport> parse(String jsonString) throws Exception {
		List<Sport> sports = null;
		if (!StringUtils.isEmpty(jsonString)) {
			sports = GsonTools.getListDate(jsonString, Sport.class);
		}
		return sports;
	}

	/**
	 * ���˶��б��������ת��Ϊ����ķ���
	 * 
	 * @param list
	 * @return
	 */
	public static String[] getSportsName(List<Sport> list) {

		List<String> sportNames = new ArrayList<String>();

		for (int i = 0; i < list.size(); i++) {
			Sport sport = list.get(i);
			String sportName = sport.getSport_name();
			sportNames.add(sportName);
		}
		final int size = list.size();
		String[] arr = (String[]) sportNames.toArray(new String[size]);

		return arr;
	}

}
