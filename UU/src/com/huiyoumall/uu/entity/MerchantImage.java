package com.huiyoumall.uu.entity;

import java.io.Serializable;

/**
 * ����ͼƬ ʵ����
 * 
 * @author ����Ԩ
 * 
 */
public class MerchantImage implements Serializable {
	private int id;// ͼƬid
	private String url;// ͼƬ��ַ
	private int m_id;// ����id
	private String mdesc;// ͼƬ����
	private String type;// ͼƬ����

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getM_id() {
		return m_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setM_id(int m_id) {
		this.m_id = m_id;
	}

	public String getMdesc() {
		return mdesc;
	}

	public void setMdesc(String mdesc) {
		this.mdesc = mdesc;
	}

}
