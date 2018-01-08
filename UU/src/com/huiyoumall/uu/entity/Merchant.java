package com.huiyoumall.uu.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huiyoumall.uu.common.GsonTools;
import com.huiyoumall.uu.util.StringUtils;

/**
 * 场馆 实体类
 * 
 * @author 何树渊
 * 
 */
public class Merchant implements Serializable {
	/**
	 * mid --- id m_name --- 场馆名 cover_image --- 场馆封面 shop_price --- 本店价
	 * market_price --- 市场价 lat --- 经度 lon --- 纬度
	 */

	private String mid;// id
	private String m_name;// 场馆名
	private String address;// 地址
	private String mdesc;// 描述、介绍
	private String shop_price;// 本店价
	private String market_price;// 市场价
	private int view_count;// 浏览次数
	private int reserve_count;// 预定次数
	private String near_station;// 附件公交
	private String con_information;// 联系方式
	private int invoice;// 是否提供发票
	private String park;// 停车信息
	private String sell;// 卖品
	private String other;// 其他
	private double lat;// 经度
	private double lon;// 纬度
	private String cover_image;// 场馆封面
	private String dev_list;// 所拥有的设置
	private String service_list;// 提供的服务
	private String starttime;// 场馆开场时间
	private String endtime;// 场馆结束时间
	private String grade;// 评分
	private String comment_num;// 评论总数
	private int collect;// 场馆收藏 0 没有收藏 ，1 已收藏

	// private string isdiscount;// 是否有优惠
	// private String sport_type;// 运动类别
	// private String sport_name;//运动名称

	private List<Comment> comments;// 三条评论集合
	private String[] images;// 图片集合

	public static List<Merchant> parseList(String jsonString) throws Exception {
		List<Merchant> merchants = null;
		if (!StringUtils.isEmpty(jsonString)) {
			merchants = GsonTools.getListDate(jsonString, Merchant.class);

		}
		return merchants;
	}

	public static Merchant parseEntity(String jsonString) throws Exception {
		Merchant merchant = null;
		if (!StringUtils.isEmpty(jsonString)) {
			merchant = GsonTools.getDate(jsonString, Merchant.class);
		}
		return merchant;
	}

	public static List<Merchant> getMerchants(String json) throws JSONException {
		Merchant merchant = null;
		List<Merchant> mList = null;
		if (!StringUtils.isEmpty(json)) {
			mList = new ArrayList<Merchant>();
			try {
				JSONArray array = new JSONArray(json);
				for (int i = 0; i < array.length(); i++) {
					JSONObject jo = array.getJSONObject(i);
					merchant = new Merchant();
					merchant.setShop_price(jo.getString("shop_price"));
					merchant.setMarket_price(jo.getString("market_price"));
					merchant.setMid(jo.getString("mid"));
					merchant.setM_name(jo.getString("m_name"));
					merchant.setMdesc(jo.getString("mdesc"));
					merchant.setLon(Double.parseDouble(jo.getString("lon")));
					merchant.setLat(Double.parseDouble(jo.getString("lat")));
					mList.add(merchant);
				}
			} catch (JSONException e) {
				throw e;
			}
		}
		return mList;
	}

	/**
	 * 我的收藏
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static List<Merchant> getMyCollectMerchants(String json)
			throws JSONException {
		Merchant merchant = null;
		List<Merchant> mList = null;
		if (!StringUtils.isEmpty(json)) {
			mList = new ArrayList<Merchant>();
			try {
				JSONArray array = new JSONArray(json);
				for (int i = 0; i < array.length(); i++) {
					JSONObject jo = array.getJSONObject(i);
					merchant = new Merchant();
					merchant.setM_name(jo.getString("m_name"));
					merchant.setMid(jo.getString("mid"));
					mList.add(merchant);
				}
			} catch (JSONException e) {
				throw e;
			}
		}
		return mList;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMdesc() {
		return mdesc;
	}

	public void setMdesc(String mdesc) {
		this.mdesc = mdesc;
	}

	public String getShop_price() {
		return shop_price;
	}

	public void setShop_price(String shop_price) {
		this.shop_price = shop_price;
	}

	public String getMarket_price() {
		return market_price;
	}

	public void setMarket_price(String market_price) {
		this.market_price = market_price;
	}

	public int getView_count() {
		return view_count;
	}

	public void setView_count(int view_count) {
		this.view_count = view_count;
	}

	public int getReserve_count() {
		return reserve_count;
	}

	public void setReserve_count(int reserve_count) {
		this.reserve_count = reserve_count;
	}

	public String getNear_station() {
		return near_station;
	}

	public void setNear_station(String near_station) {
		this.near_station = near_station;
	}

	public String getCon_information() {
		return con_information;
	}

	public void setCon_information(String con_information) {
		this.con_information = con_information;
	}

	public int getInvoice() {
		return invoice;
	}

	public void setInvoice(int invoice) {
		this.invoice = invoice;
	}

	public String getPark() {
		return park;
	}

	public void setPark(String park) {
		this.park = park;
	}

	public String getSell() {
		return sell;
	}

	public void setSell(String sell) {
		this.sell = sell;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getCover_image() {
		return cover_image;
	}

	public void setCover_image(String cover_image) {
		this.cover_image = cover_image;
	}

	public String getDev_list() {
		return dev_list;
	}

	public void setDev_list(String dev_list) {
		this.dev_list = dev_list;
	}

	public String getService_list() {
		return service_list;
	}

	public void setService_list(String service_list) {
		this.service_list = service_list;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getComment_num() {
		return comment_num;
	}

	public void setComment_num(String comment_num) {
		this.comment_num = comment_num;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}

	public int getCollect() {
		return collect;
	}

	public void setCollect(int collect) {
		this.collect = collect;
	}
}
