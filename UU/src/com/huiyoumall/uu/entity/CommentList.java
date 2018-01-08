package com.huiyoumall.uu.entity;

import java.io.Serializable;
import java.util.List;

import com.huiyoumall.uu.common.GsonTools;
import com.huiyoumall.uu.util.StringUtils;

/**
 * 场馆评价列表 实体类
 * 
 * @author ASUS
 * 
 */
public class CommentList implements Serializable {
	private List<Comment> mComments;

	public List<Comment> getmComments() {
		return mComments;
	}

	public void setmComments(List<Comment> mComments) {
		this.mComments = mComments;
	}

	public static CommentList parse(String json) {
		CommentList discussList = null;
		if (!StringUtils.isEmpty(json)) {
			discussList = GsonTools.getDate(json, CommentList.class);
		}
		return discussList;
	}
}
