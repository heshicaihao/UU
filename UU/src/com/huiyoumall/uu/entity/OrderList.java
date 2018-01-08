package com.huiyoumall.uu.entity;

import java.util.ArrayList;
import java.util.List;

import com.huiyoumall.uu.common.GsonTools;
import com.huiyoumall.uu.util.StringUtils;

/**
 * 订单列表 实体类
 * 
 * @author 何树渊
 * 
 */
@SuppressWarnings("serial")
public class OrderList extends Entity {

	private List<Order> list = new ArrayList<Order>();

	public List<Order> getList() {
		return list;
	}

	public void setList(List<Order> list) {
		this.list = list;
	}

	public static OrderList parse(String json) {
		OrderList orderList = null;
		if (!StringUtils.isEmpty(json)) {
			orderList = GsonTools.getDate(json, OrderList.class);
		}
		return orderList;
	}
}
