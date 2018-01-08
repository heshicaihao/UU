package com.huiyoumall.uu.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huiyoumall.uu.activity.ReserveMerchantActivity;
import com.huiyoumall.uu.entity.Order.TDeatil;

/**
 * 场馆预订 服务端数据json解析 实体类
 * 
 * @author ASUS
 * 
 */
public class Reserve {

	private String[] site;
	private String time_quantum;
	private List<Sport> sports;
	private List<List<TDeatil>> detail;

	public String[] getSite() {
		return site;
	}

	public void setSite(String[] site) {
		this.site = site;
	}

	public String getTime_quantum() {
		return time_quantum;
	}

	public void setTime_quantum(String time_quantum) {
		this.time_quantum = time_quantum;
	}

	public List<List<TDeatil>> getDetail() {
		return detail;
	}

	public void setDetail(List<List<TDeatil>> detail) {
		this.detail = detail;
	}

	public List<Sport> getSports() {
		return sports;
	}

	public void setSports(List<Sport> sports) {
		this.sports = sports;
	}

	public static Reserve parseEntity(String jsonString) {
		Reserve reserve = new Reserve();
		// if (!StringUtils.isEmpty(jsonString)) {
		// reserve = GsonTools.getDate(jsonString, Reserve.class);
		// }
		try {
			JSONObject jb = new JSONObject(jsonString);
			// 运动项目
			// JSONArray sprotArray = new JSONArray(jb.get("sprot").toString());
			// List<Sport> sp = new ArrayList<Sport>();
			// for (int i = 0; i < sprotArray.length(); i++) {
			// JSONObject jsb = sprotArray.getJSONObject(i);
			// Sport s = new Sport();
			// s.setSport_id(jsb.getInt("sportid"));
			// s.setSport_name(jsb.getString("sport"));
			// sp.add(s);
			// }
			// site解析
			JSONArray arraysite = new JSONArray(jb.get("site").toString());
			String[] s = new String[arraysite.length()];
			for (int i = 0; i < arraysite.length(); i++) {
				s[i] = (String) arraysite.get(i);
			}
			reserve.setSite(s);

			// 时间点解析
			reserve.setTime_quantum(jb.get("time_quantum").toString());
			// JSONArray arraytime = new JSONArray(jb.get("time_quantum")
			// .toString());
			// String[] t = new String[arraytime.length()];
			// for (int i = 0; i < arraytime.length(); i++) {
			// t[i] = (String) arraytime.get(i);
			// }
			// reserve.setTime_quantum(t);

			// 定价详情解析
			JSONArray arraydetail = new JSONArray(jb.get("detail").toString());
			TDeatil detail = null;
			List<TDeatil> details = null;
			List<List<TDeatil>> detailLists = new ArrayList<List<TDeatil>>();
			for (int i = 0; i < arraydetail.length(); i++) {
				JSONArray js = new JSONArray(arraydetail.get(i).toString());
				details = new ArrayList<TDeatil>();
				for (int j = 0; j < js.length(); j++) {
					JSONObject jsb = js.getJSONObject(j);
					detail = new TDeatil();
					detail.setTid(jsb.getInt("tid"));
					detail.setT_name(jsb.getString("name"));
					detail.setIs_res(jsb.getInt("is_res"));
					detail.setTime_quantum(ReserveMerchantActivity.time.get(jsb
							.getString("time_quantum")));
					detail.setT_price(jsb.getString("price"));
					details.add(detail);
				}
				detailLists.add(details);
			}
			reserve.setDetail(detailLists);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return reserve;
	}
}
