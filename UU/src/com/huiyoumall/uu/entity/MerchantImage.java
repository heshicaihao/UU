package com.huiyoumall.uu.entity;

import java.io.Serializable;

/**
 * ≥°π›Õº∆¨  µÃÂ¿‡
 * 
 * @author ∫Œ ˜‘®
 * 
 */
public class MerchantImage implements Serializable {
	private int id;// Õº∆¨id
	private String url;// Õº∆¨µÿ÷∑
	private int m_id;// ≥°π›id
	private String mdesc;// Õº∆¨√Ë ˆ
	private String type;// Õº∆¨¿‡–Õ

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
