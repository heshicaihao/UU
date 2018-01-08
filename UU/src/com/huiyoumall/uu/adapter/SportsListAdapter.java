package com.huiyoumall.uu.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.entity.Merchant;
import com.huiyoumall.uu.util.StringUtils;

/**
 * 运动列表 适配器
 * 
 * @author ASUS
 * 
 */
public class SportsListAdapter extends BaseAdapter {

	private List<Merchant> mMerchants;
	private Context context;
	private LayoutInflater mInflater;
	private AppContext app;

	public SportsListAdapter(Context context, List<Merchant> list_) {
		this.mMerchants = list_;
		this.context = context;
		mInflater = LayoutInflater.from(context);
		app = (AppContext) context.getApplicationContext();
	}

	public void setData(List<Merchant> mList) {
		this.mMerchants = mList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mMerchants.size();
	}

	@Override
	public Object getItem(int position) {
		return mMerchants.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			vh = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_venue_list, null);

			vh.nameText = (TextView) convertView
					.findViewById(R.id.merchant_name);
			vh.addressText = (TextView) convertView
					.findViewById(R.id.merchant_address);
			vh.shopPriceText = (TextView) convertView
					.findViewById(R.id.merchant_shop_price);
			vh.marketPrice = (TextView) convertView
					.findViewById(R.id.merchant_mark_price);
			vh.distanceText = (TextView) convertView
					.findViewById(R.id.merchant_distance);
			vh.merchant_point = (TextView) convertView
					.findViewById(R.id.merchant_point);
			vh.favorable_text = (TextView) convertView
					.findViewById(R.id.favorable_text);
			convertView.setTag(vh);

		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		Merchant merchant = (Merchant) getItem(position);

		if (!StringUtils.isEmpty(merchant.getM_name())) {
			vh.nameText.setText(merchant.getM_name());
		}

		if (!StringUtils.isEmpty(merchant.getAddress())) {
			vh.addressText.setText("【" + merchant.getAddress() + "】");
		}

		if (!StringUtils.isEmpty(merchant.getShop_price())) {
			vh.shopPriceText.setText("¥" + merchant.getShop_price());
		}

		if (!StringUtils.isEmpty(merchant.getMarket_price())) {
			vh.marketPrice.setText("¥" + merchant.getMarket_price());
			vh.marketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}
		if (!StringUtils.isEmpty(merchant.getGrade())) {
			vh.merchant_point.setText(merchant.getGrade());
		}
		// 如果有优惠则显示优惠
		vh.distanceText.setText(StringUtils.getDistance(app.getLongitude(),
				app.getLatitude(), merchant.getLon() + "", merchant.getLat()
						+ ""));

		return convertView;

	}

	class ViewHolder {
		TextView nameText;
		TextView addressText;
		TextView shopPriceText;
		TextView marketPrice;
		TextView distanceText;
		TextView merchant_point;
		TextView favorable_text;// 是否有优惠
	}
}
