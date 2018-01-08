package com.huiyoumall.uu.entity;

import java.io.Serializable;
import java.util.List;

import com.huiyoumall.uu.common.GsonTools;
import com.huiyoumall.uu.util.StringUtils;

/**
 * ��ҳ���ݰ� ʵ����
 * 
 * @author ASUS
 * 
 */
public class HomeDataPk implements Serializable {
	private List<Act> act;// ���5
	private List<Sport> sports;// �˶���Ŀ8
	private List<Merchant> merchant;// ���ų���10

	public static HomeDataPk parse(String jsonString) throws Exception {
		HomeDataPk dataPks = null;
		if (!StringUtils.isEmpty(jsonString)) {
			dataPks = GsonTools.getDate(jsonString, HomeDataPk.class);
		}
		return dataPks;
	}

	public List<Act> getAct() {
		return act;
	}

	public void setAct(List<Act> act) {
		this.act = act;
	}

	public List<Sport> getSports() {
		return sports;
	}

	public void setSports(List<Sport> sports) {
		this.sports = sports;
	}

	public List<Merchant> getMerchant() {
		return merchant;
	}

	public void setMerchant(List<Merchant> merchant) {
		this.merchant = merchant;
	}

}
