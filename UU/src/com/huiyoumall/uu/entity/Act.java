package com.huiyoumall.uu.entity;

import java.io.Serializable;

/**
 * 首页 热门活动 广告位置
 * 
 * @author 何树渊
 * 
 */
public class Act implements Serializable {
	private String act_id;// 活动id
	private String act_name;// 活动名称
	private String mid;// 场馆id
	private String m_name;// 场馆名称
	private String img;
	private String price;// 活动价格
	private String t_price;
	private String num;// 活动人数
	private String date;// 活动日期
	private String act_time;// 活动时间点

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
