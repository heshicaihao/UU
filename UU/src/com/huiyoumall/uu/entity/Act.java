package com.huiyoumall.uu.entity;

import java.io.Serializable;

/**
 * ��ҳ ���Ż ���λ��
 * 
 * @author ����Ԩ
 * 
 */
public class Act implements Serializable {
	private String act_id;// �id
	private String act_name;// �����
	private String mid;// ����id
	private String m_name;// ��������
	private String img;
	private String price;// ��۸�
	private String t_price;
	private String num;// �����
	private String date;// �����
	private String act_time;// �ʱ���

	public String getAct_id() {
		return act_id;
	}

	public void setAct_id(String act_id) {
		this.act_id = act_id;
	}

	public String getAct_name() {
		return act_name;
	}

	public void setAct_name(String act_name) {
		this.act_name = act_name;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getM_name() {
		return m_name;
	}

	public void setM_name(String m_name) {
		this.m_name = m_name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getT_price() {
		return t_price;
	}

	public void setT_price(String t_price) {
		this.t_price = t_price;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAct_time() {
		return act_time;
	}

	public void setAct_time(String act_time) {
		this.act_time = act_time;
	}

}
