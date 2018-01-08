package com.huiyoumall.uu.entity;

import java.io.Serializable;
import java.util.List;

import com.huiyoumall.uu.common.GsonTools;
import com.huiyoumall.uu.util.StringUtils;

/**
 * 场馆列表 实体类
 * 
 * @author 何树渊
 * 
 */
public class MerchantList implements Serializable {

	private List<Merchant> mMerchants;

	public List<Merchant> getmMerchants() {
		return mMerchants;
	}

	public void setmMerchants(List<Merchant> mMerchants) {
		this.mMerchants = mMerchants;
	}

	public static MerchantList parse(String jsonString) throws Exception {
		MerchantList merchants = null;
		if (!StringUtils.isEmpty(jsonString)) {
			merchants = GsonTools.getDate(jsonString, MerchantList.class);
		}
		return merchants;
	}

}
