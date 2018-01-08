package com.huiyoumall.uu.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huiyoumall.uu.util.StringUtils;

/**
 * 订单 实体类
 * 
 * @author 何树渊
 * 
 */
public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String order_sn;// 订单号
	private int merchant_id;// 场馆ID
	private String merchant_name; // 场馆名称
	private String address;// 场馆地址
	private int soport_id;// 运动项目ID
	private String sport_name;// 运动项目名称
	private String total_price;// 总价
	private String date;// 使用日期
	private int status;// 订单状态 0未支付，1支付未消费，2支付已经消费，3取消
	private String create_time;// 下单时间
	private String order_info;// 订单详情
	private List<TDeatil> tDeatils;// 场地集合

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public int getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(int merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getMerchant_name() {
		return merchant_name;
	}

	public void setMerchant_name(String merchant_name) {
		this.merchant_name = merchant_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getSoport_id() {
		return soport_id;
	}

	public void setSoport_id(int soport_id) {
		this.soport_id = soport_id;
	}

	public String getSport_name() {
		return sport_name;
	}

	public void setSport_name(String sport_name) {
		this.sport_name = sport_name;
	}

	public String getTotal_price() {
		return total_price;
	}

	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getOrder_info() {
		return order_info;
	}

	public void setOrder_info(String order_info) {
		this.order_info = order_info;
	}

	public List<TDeatil> gettDeatils() {
		return tDeatils;
	}

	public void settDeatils(List<TDeatil> tDeatils) {
		this.tDeatils = tDeatils;
	}

	public static List<Order> parse(String jsonString) throws JSONException {
		List<Order> datas = null;
		// if (!StringUtils.isEmpty(jsonString)) {
		// datas = GsonTools.getListDate(jsonString, DataEntity.class);
		// }
		// return datas;

		Order date = null;
		JSONArray array = new JSONArray(jsonString);
		if (array.length() > 0) {
			datas = new ArrayList<Order>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject jo = array.getJSONObject(i);
				date = new Order();
				date.setMerchant_id(jo.getInt("mid"));
				date.setMerchant_name(jo.getString("m_name"));
				date.setAddress(jo.getString("address"));
				date.setSoport_id(jo.getInt("sport_id"));
				date.setSport_name(jo.getString("sport_name"));
				date.setDate(jo.getString("date"));

				JSONArray array2 = new JSONArray(jo.getString("detail"));
				if (array2.length() > 0) {
					List<TDeatil> list = new ArrayList<TDeatil>();
					TDeatil detail = null;
					for (int j = 0; j < array2.length(); j++) {
						JSONObject object = array2.getJSONObject(j);
						detail = new TDeatil();
						detail.setMid(jo.getInt("mid"));
						detail.setTid(object.getInt("tid"));
						detail.setT_price(object.getString("t_price"));
						detail.setT_name(object.getString("tname"));
						detail.setTime_quantum(object.getString("t_time"));
						list.add(detail);
					}
					date.settDeatils(list);
				}
				datas.add(date);
			}
		}

		return datas;
	}

	public static List<Order> parseList(String jsonString) throws Exception {
		List<Order> orders = null;
		Order order = null;
		JSONObject json = new JSONObject(jsonString);
		if (!StringUtils.isEmpty(jsonString)) {
			JSONArray array = new JSONArray(json.getString("list"));
			orders = new ArrayList<Order>();
			for (int i = 0; i < array.length(); i++) {
				order = new Order();
				JSONObject jsonObject = array.getJSONObject(i);
				order.setOrder_sn(jsonObject.getString("order_sn"));
				order.setMerchant_id(jsonObject.getInt("merchant_id"));
				order.setMerchant_name(jsonObject.getString("merchant_name"));
				order.setAddress(jsonObject.getString("address"));
				order.setSoport_id(jsonObject.getInt("sport_id"));
				order.setSport_name(jsonObject.getString("sport_name"));
				order.setTotal_price(jsonObject.getString("total_price"));
				order.setStatus(jsonObject.getInt("status"));
				order.setDate(jsonObject.getString("date"));
				order.setCreate_time(jsonObject.getString("create_time"));
				JSONArray detArray = new JSONArray(
						jsonObject.getString("order_info"));
				TDeatil deatil = null;
				List<TDeatil> tds = new ArrayList<Order.TDeatil>();
				for (int j = 0; j < detArray.length(); j++) {
					JSONObject object = detArray.getJSONObject(j);
					deatil = new TDeatil();
					deatil.setTid(object.getInt("tid"));
					deatil.setT_name(object.getString("t_name"));
					deatil.setTime_quantum(object.getString("time_quantum"));
					deatil.setT_price(object.getString("price"));
					tds.add(deatil);
				}
				order.settDeatils(tds);
				orders.add(order);
			}
		}
		return orders;
	}

	public static class TDeatil implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int mid;// 场馆id
		private int tid;// 场地ID
		private String t_name;// 场地名称
		private int is_res;// 是否可以预订
		private String time_quantum;// 时间段
		private String t_price;// 单价
		private int select;// 场地是否被选择

		public int getTid() {
			return tid;
		}

		public void setTid(int tid) {
			this.tid = tid;
		}

		public int getMid() {
			return mid;
		}

		public void setMid(int mid) {
			this.mid = mid;
		}

		public String getT_name() {
			return t_name;
		}

		public void setT_name(String t_name) {
			this.t_name = t_name;
		}

		public int getIs_res() {
			return is_res;
		}

		public void setIs_res(int is_res) {
			this.is_res = is_res;
		}

		public String getTime_quantum() {
			return time_quantum;
		}

		public void setTime_quantum(String time_quantum) {
			this.time_quantum = time_quantum;
		}

		public String getT_price() {
			return t_price;
		}

		public void setT_price(String t_price) {
			this.t_price = t_price;
		}

		public int getSelect() {
			return select;
		}

		public void setSelect(int select) {
			this.select = select;
		}
	}
}
