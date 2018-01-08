package com.huiyoumall.uu.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.activity.ReserveMerchantActivity;
import com.huiyoumall.uu.entity.Merchant;
import com.huiyoumall.uu.image.SmartImageView;
import com.huiyoumall.uu.util.StringUtils;

/**
 * 首页热门球场 适配器
 * 
 * @author ASUS
 * 
 */

public class PopularMerchantAdapter extends BaseAdapter {

	private List<Merchant> mLists;
	private Context context;
	private LayoutInflater mInflater;
	private AppContext app;

	public PopularMerchantAdapter(Context context, List<Merchant> list) {
		this.context = context;
		this.mLists = list;
		this.mInflater = LayoutInflater.from(context);
		app = (AppContext) context.getApplicationContext();
	}

	public void setMerChants(List<Merchant> lists) {
		this.mLists = lists;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView mHolder = null;

		if (convertView == null) {
			mHolder = new HolderView();
			convertView = this.mInflater.inflate(R.layout.item_home_hot, null);
			mHolder.vImgView = (SmartImageView) convertView
					.findViewById(R.id.image_url);
			mHolder.vNameView = (TextView) convertView.findViewById(R.id.name);
			mHolder.vAddressView = (TextView) convertView
					.findViewById(R.id.address);
			mHolder.vDistanceView = (TextView) convertView
					.findViewById(R.id.distance);
			mHolder.vShop_Price = (TextView) convertView
					.findViewById(R.id.shop_price);
			mHolder.vMarket_Price = (TextView) convertView
					.findViewById(R.id.market_price);
			mHolder.vBtnYuding = (Button) convertView
					.findViewById(R.id.btn_yuding);
			convertView.setTag(mHolder);
		} else {
			mHolder = (HolderView) convertView.getTag();
		}

		Merchant court = (Merchant) getItem(position);
		if (!StringUtils.isEmpty(court.getCover_image())) {
			mHolder.vImgView.setImageUrl(court.getCover_image());
			mHolder.vImgView.setRatio(2.0f);
		}
		mHolder.vNameView.setText(court.getM_name());
		mHolder.vDistanceView.setText(StringUtils.getDistance(
				app.getLongitude(), app.getLatitude(), court.getLon() + "",
				court.getLat() + ""));
		if (!StringUtils.isEmpty(court.getAddress())) {
			mHolder.vAddressView.setText("【" + court.getAddress() + "】");
		}

		if (!StringUtils.isEmpty(court.getShop_price())) {
			mHolder.vShop_Price.setText("¥" + court.getShop_price());
		}

		if (!StringUtils.isEmpty(court.getMarket_price())) {
			mHolder.vMarket_Price.setText("¥" + court.getMarket_price());
			mHolder.vMarket_Price.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG);
		}
		mHolder.vBtnYuding.setTag(court);
		mHolder.vBtnYuding.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Merchant m = (Merchant) v.getTag();
				Bundle bundle = new Bundle();
				bundle.putString(ReserveMerchantActivity.MER_ID, m.getMid());
				bundle.putString(ReserveMerchantActivity.MER_NAME,
						m.getM_name());
				HelperUi.startActivity(context, ReserveMerchantActivity.class,
						bundle);
			}
		});
		return convertView;
	}

	class HolderView {
		SmartImageView vImgView;
		TextView vNameView;
		TextView vAddressView;
		TextView vDistanceView;
		TextView vShop_Price;
		TextView vMarket_Price;
		Button vBtnYuding;
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
}
