package com.huiyoumall.uu.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.huiyoumall.uu.util.StringUtils;

/**
 * 获取活动实体类
 * 
 * @author ASUS
 * 
 */
public class Activities implements Serializable {
	private String distance;// 距离
	private String act_name; // 活动名称
	private String act_methem;// 活动主题
	private int act_id; // 活动ID
	private int sportid;// 运动项目id
	private String sport_name;// 运动项目
	private int member_id; // 用户ID
	private String member_name;// 用户名
	private String member_sex; // 用户性别
	private int member_age;// 用户年龄
	private String member_avatar; // 用户头像
	private String price_type; // 费用类型
	private String m_name;// 场馆名
	private String img; // 活动图片
	private int join_num; // 活动已参加人数
	private int totle_num;// 活动总人数
	private String date; // 日期
	private String act_price;// 价格
	private String act_time;// 开始时间
	private String act_num;// 已参与活动数
	private String isJoin;// 是否参加

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getAct_name() {
		return act_name;
	}

	public void setAct_name(String act_name) {
		this.act_name = act_name;
	}

	public String getAct_methem() {
		return act_methem;
	}

	public void setAct_methem(String act_methem) {
		this.act_methem = act_methem;
	}

	public int getAct_id() {
		return act_id;
	}

	public void setAct_id(int act_id) {
		this.act_id = act_id;
	}

	public int getSportid() {
		return sportid;
	}

	public void setSportid(int sportid) {
		this.sportid = sportid;
	}

	public String getSport_name() {
		return sport_name;
	}

	public void setSport_name(String sport_name) {
		this.sport_name = sport_name;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}

	public String getMember_sex() {
		return member_sex;
	}

	public void setMember_sex(String member_sex) {
		this.member_sex = member_sex;
	}

	public int getMember_age() {
		return member_age;
	}

	public void setMember_age(int member_age) {
		this.member_age = member_age;
	}

	public String getMember_avatar() {
		return member_avatar;
	}

	public void setMember_avatar(String member_avatar) {
		this.member_avatar = member_avatar;
	}

	public String getPrice_type() {
		return price_type;
	}

	public void setPrice_type(String price_type) {
		this.price_type = price_type;
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

	public int getJoin_num() {
		return join_num;
	}

	public void setJoin_num(int join_num) {
		this.join_num = join_num;
	}

	public int getTotle_num() {
		return totle_num;
	}

	public void setTotle_num(int totle_num) {
		this.totle_num = totle_num;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAct_price() {
		return act_price;
	}

	public void setAct_price(String act_price) {
		this.act_price = act_price;
	}

	public String getAct_time() {
		return act_time;
	}

	public void setAct_time(String act_time) {
		this.act_time = act_time;
	}

	public String getAct_num() {
		return act_num;
	}

	public void setAct_num(String act_num) {
		this.act_num = act_num;
	}

	public String getIsJoin() {
		return isJoin;
	}

	public void setIsJoin(String isJoin) {
		this.isJoin = isJoin;
	}

	public static List<Activities> parseList(String jsonString)
			throws Exception {
		List<Activities> activitys = null;
		Activities activities = null;
		JSONObject json = new JSONObject(jsonString);
		if (!StringUtils.isEmpty(jsonString)) {
			JSONArray array = new JSONArray(json.getString("list"));
			activitys = new ArrayList<Activities>();
			for (int i = 0; i < array.length(); i++) {
				activities = new Activities();
				JSONObject jsonObject = array.getJSONObject(i);
				activities.setAct_id(jsonObject.getInt("act_id"));
				activities.setDistance(jsonObject.getString("distance"));
				activities.setAct_name(jsonObject.getString("act_name"));
				activities.setAct_methem(jsonObject.getString("act_methem"));
				activities.setSportid(jsonObject.getInt("sportid"));
				activities.setSport_name(jsonObject.getString("sport_name"));
				activities.setMember_id(jsonObject.getInt("member_id"));
				activities.setMember_name(jsonObject.getString("member_name"));
				activities.setMember_sex(jsonObject.getString("member_sex"));
				activities.setMember_age(jsonObject.getInt("member_age"));
				activities.setMember_avatar(jsonObject
						.getString("member_avatar"));
				activities.setPrice_type(jsonObject.getString("price_type"));
				activities.setM_name(jsonObject.getString("m_name"));
				activities.setImg(jsonObject.getString("img"));
				activities.setJoin_num(jsonObject.getInt("join_num"));
				activities.setTotle_num(jsonObject.getInt("totle_num"));
				activities.setDate(jsonObject.getString("date"));
				activities.setAct_price(jsonObject.getString("act_price"));
				activities.setAct_time(jsonObject.getString("act_time"));
				activitys.add(activities);
			}
		}
		return activitys;
	}

	// 活动人员详情
	public static class ActivityMember implements Serializable {
		private int member_id;// 用户id
		private String member_avatar;// 用户头像
		private String member_name;// 用户名
		private String member_sex;// 性别
		private String tel;// 联系方式
		private int member_age;// 年龄
		private String member_provinceid;// 省
		private String member_cityid;// 市
		private String signature;// 个性签名
		private List<Merchant> merchants;// 常去场馆列表

		public int getMember_id() {
			return member_id;
		}

		public void setMember_id(int member_id) {
			this.member_id = member_id;
		}

		public String getMember_avatar() {
			return member_avatar;
		}

		public void setMember_avatar(String member_avatar) {
			this.member_avatar = member_avatar;
		}

		public String getMember_name() {
			return member_name;
		}

		public void setMember_name(String member_name) {
			this.member_name = member_name;
		}

		public String getMember_sex() {
			return member_sex;
		}

		public void setMember_sex(String member_sex) {
			this.member_sex = member_sex;
		}

		public String getTel() {
			return tel;
		}

		public void setTel(String tel) {
			this.tel = tel;
		}

		public int getMember_age() {
			return member_age;
		}

		public void setMember_age(int member_age) {
			this.member_age = member_age;
		}

		public String getMember_provinceid() {
			return member_provinceid;
		}

		public void setMember_provinceid(String member_provinceid) {
			this.member_provinceid = member_provinceid;
		}

		public String getMember_cityid() {
			return member_cityid;
		}

		public void setMember_cityid(String member_cityid) {
			this.member_cityid = member_cityid;
		}

		public String getSignature() {
			return signature;
		}

		public void setSignature(String signature) {
			this.signature = signature;
		}

		public List<Merchant> getMerchants() {
			return merchants;
		}

		public void setMerchants(List<Merchant> merchants) {
			this.merchants = merchants;
		}

	}

}
