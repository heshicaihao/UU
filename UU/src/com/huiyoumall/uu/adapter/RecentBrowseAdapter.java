package com.huiyoumall.uu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.entity.Merchant;
import com.huiyoumall.uu.image.SmartImageView;
import com.huiyoumall.uu.util.StringUtils;

/**
 * ×î½üä¯ÀÀ ÊÊÅäÆ÷
 * 
 * @author ASUS
 * 
 */

public class RecentBrowseAdapter extends BaseAdapter {

	private List<Merchant> mLists;
	private Context context;
	private LayoutInflater mInflater;
	private AppContext app;

	public RecentBrowseAdapter(Context context, ArrayList<Merchant> lists) {
		this.context = context;
		this.mLists = lists;
		this.mInflater = LayoutInflater.from(context);
		app = (AppContext) context.getApplicationContext();
	}

	public void addCourtData(ArrayList<Merchant> lists) {
		this.mLists = lists;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mLists.size();
	}

	@Override
	public Object getItem(int position) {
		return mLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView mHolder = null;
		if (convertView == null) {
			mHolder = new HolderView();
			convertView = this.mInflater.inflate(R.layout.item_home_hot, null);
			mHolder.vImgView = (SmartImageView) convertView
					.findViewById(R.id.image_url);
			mHolder.vNameView = (TextView) convertView
					.findViewById(R.id.changguan_name);
			mHolder.vAddressView = (TextView) convertView
					.findViewById(R.id.cg_search);
			mHolder.vDistanceView = (TextView) convertView
					.findViewById(R.id.changguan_name);
			mHolder.vNewPrice = (TextView) convertView
					.findViewById(R.id.changguan_name);
			mHolder.vOldPrice = (TextView) convertView
					.findViewById(R.id.changguan_name);

			convertView.setTag(mHolder);
		} else {
			mHolder = (HolderView) convertView.getTag();
		}
		Merchant court = (Merchant) getItem(position);
		mHolder.vImgView.setImageUrl(court.getCover_image());
		mHolder.vNameView.setText(court.getM_name());
		mHolder.vAddressView.setText(court.getAddress());
		mHolder.vDistanceView.setText(StringUtils.getDistance(
				app.getLongitude(), app.getLatitude(), court.getLon() + "",
				court.getLat() + ""));
		mHolder.vNewPrice.setText(court.getShop_price());
		mHolder.vOldPrice.setText(court.getMarket_price());
		return convertView;
	}

	class HolderView {
		SmartImageView vImgView;
		TextView vNameView;
		TextView vAddressView;
		TextView vDistanceView;
		TextView vNewPrice;
		TextView vOldPrice;
		Button vBtnYuding;
	}

}
