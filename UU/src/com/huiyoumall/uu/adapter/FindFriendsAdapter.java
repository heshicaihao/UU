package com.huiyoumall.uu.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyoumall.uu.R;
import com.huiyoumall.uu.entity.Activities;
import com.huiyoumall.uu.image.SmartImageView;
import com.huiyoumall.uu.util.DateUtil;
import com.huiyoumall.uu.util.StringUtils;

public class FindFriendsAdapter extends BaseAdapter {

	private Context mContext;
	private List<Activities> activities;
	private LayoutInflater mInflater;

	public FindFriendsAdapter(Context context, List<Activities> list) {
		this.mContext = context;
		this.activities = list;
		mInflater = LayoutInflater.from(context);
	}

	public void setData(List<Activities> list) {
		this.activities = list;
	}

	@Override
	public int getCount() {
		return activities.size();
	}

	@Override
	public Object getItem(int position) {
		return activities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if (convertView == null || convertView.getTag() == null) {
			mHolder = new ViewHolder();
			convertView = mInflater
					.inflate(R.layout.item_my_huodong_list, null);
			mHolder.member_avatar = (SmartImageView) convertView
					.findViewById(R.id.member_avatar);
			mHolder.member_age_text = (TextView) convertView
					.findViewById(R.id.member_age_text);
			mHolder.member_name_text = (TextView) convertView
					.findViewById(R.id.member_name_text);
			mHolder.huodong_sports_img = (ImageView) convertView
					.findViewById(R.id.huodong_sports_img);
			mHolder.houdong_project_text = (TextView) convertView
					.findViewById(R.id.houdong_project_text);
			mHolder.houdong_theme_text = (TextView) convertView
					.findViewById(R.id.houdong_theme_text);
			mHolder.houdong_date_text = (TextView) convertView
					.findViewById(R.id.houdong_date_text);
			mHolder.houdong_merchant_name_text = (TextView) convertView
					.findViewById(R.id.houdong_merchant_name_text);
			mHolder.houdong_price_type_text = (TextView) convertView
					.findViewById(R.id.houdong_price_type_text);
			mHolder.houdong_consume_text = (TextView) convertView
					.findViewById(R.id.houdong_consume_text);
			mHolder.houdong_distance_text = (TextView) convertView
					.findViewById(R.id.houdong_distance_text);
			mHolder.houdong_join_people_num_text = (TextView) convertView
					.findViewById(R.id.houdong_join_people_num_text);
			mHolder.huodong_totle_people_text = (TextView) convertView
					.findViewById(R.id.huodong_totle_people_text);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		Activities activities = (Activities) getItem(position);
		if (!StringUtils.isEmpty(activities.getMember_avatar())) {
			mHolder.member_avatar.setImageUrl(activities.getMember_avatar());
			mHolder.member_avatar.setRatio(2.0f);
		}

		mHolder.member_age_text.setText(activities.getMember_age() + "Ëê");
		if (activities.getMember_sex().equals("ÄÐ")) {
			Drawable sex_img = mContext.getResources().getDrawable(
					R.drawable.ic_male);
			sex_img.setBounds(0, 0, sex_img.getMinimumWidth(),
					sex_img.getMinimumHeight());
			mHolder.member_age_text.setCompoundDrawables(sex_img, null, null,
					null);
		} else {
			Drawable sex_img = mContext.getResources().getDrawable(
					R.drawable.ic_female);
			sex_img.setBounds(0, 0, sex_img.getMinimumWidth(),
					sex_img.getMinimumHeight());
			mHolder.member_age_text.setCompoundDrawables(sex_img, null, null,
					null);
		}
		mHolder.member_name_text.setText(activities.getMember_name());
		mHolder.houdong_project_text.setText(activities.getAct_name());
		mHolder.houdong_theme_text.setText(activities.getAct_methem());
		mHolder.houdong_date_text.setText(huodongDate(activities.getDate(),
				activities.getAct_time()));
		mHolder.houdong_merchant_name_text.setText(activities.getM_name());
		mHolder.houdong_price_type_text.setText(activities.getPrice_type());
		mHolder.houdong_consume_text.setText(activities.getAct_price() + "");
		mHolder.houdong_distance_text.setText(activities.getDistance());
		mHolder.houdong_join_people_num_text.setText(activities.getJoin_num()
				+ "");
		if (activities.getTotle_num() == 0) {
			mHolder.huodong_totle_people_text.setText("²»ÏÞ");
		} else {
			mHolder.huodong_totle_people_text.setText(activities.getTotle_num()
					+ "");
		}

		return convertView;
	}

	private String huodongDate(String mDate, String mTime) {
		String week = DateUtil.getWeekNumber(mDate, "yyyy-MM-dd");
		String dates = "";
		String[] date = mDate.split("-");
		dates = date[1] + "." + date[2];
		return week + "  " + dates + "  " + mTime;
	}

	class ViewHolder {
		SmartImageView member_avatar;
		TextView member_age_text;
		TextView member_name_text;
		ImageView huodong_sports_img;
		TextView houdong_project_text;
		TextView houdong_theme_text;
		TextView houdong_date_text;
		TextView houdong_merchant_name_text;
		TextView houdong_price_type_text;
		TextView houdong_consume_text;
		TextView houdong_distance_text;
		TextView houdong_join_people_num_text;
		TextView huodong_totle_people_text;
	}
}
