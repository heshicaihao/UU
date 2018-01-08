package com.huiyoumall.uu.entity;

import java.io.Serializable;

/**
 * 首页 城市选择 城市的实体类
 * 
 * @author ASUS
 * 
 */
public class CityItem implements Serializable {

	public int id;// 城市ID 或区域ID
	public String name;// 城市名称 或区域名称
	public int type;// 类型 城市 还是区域
	public int parent;// 城市id
}
