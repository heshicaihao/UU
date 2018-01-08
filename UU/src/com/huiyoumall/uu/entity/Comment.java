package com.huiyoumall.uu.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huiyoumall.uu.util.StringUtils;

/**
 * 场馆评价 实体类
 * 
 * @author ASUS
 * 
 */
public class Comment extends Entity {

	private String member_id;// 会员id
	private String member_name;// 会员名
	private String mid;// 场馆Id
	private String grade;// 评价分数
	private String sendtime;// 评价时间 long
	private String content;// 评价内容

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static List<Comment> getMerchants(String json) throws JSONException {
		Comment comment = null;
		List<Comment> mList = null;
		if (!StringUtils.isEmpty(json)) {
			mList = new ArrayList<Comment>();
			try {
				JSONArray array = new JSONArray(json);
				for (int i = 0; i < array.length(); i++) {
					JSONObject jo = array.getJSONObject(i);
					comment = new Comment();
					comment.setMember_name(jo.getString("member_name"));
					comment.setSendtime(jo.getString("sendtime"));
					comment.setGrade(jo.getString("grade"));
					comment.setContent(jo.getString("content"));
					mList.add(comment);
				}
			} catch (JSONException e) {
				throw e;
			}
		}
		return mList;
	}

}
