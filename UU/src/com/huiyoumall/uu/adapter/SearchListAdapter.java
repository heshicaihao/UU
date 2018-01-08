package com.huiyoumall.uu.adapter;

import java.util.List;

import android.content.Context;
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
 * 暂时没有用到 适配器
 * 
 * @author ASUS
 * 
 */
public class SearchListAdapter extends BaseAdapter {

	private Context mContext;
	private List<Merchant> mList;
	private LayoutInflater mInflater;
	private AppContext app;

	public SearchListAdapter(Context context, List<Merchant> list) {
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
		app = (AppContext) mContext.getApplicationContext();
	}

	public void setData(List<Merchant> mers) {
		this.mList = mers;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		HolderView holderView = null;
		if (view == null || view.getTag() == null) {
			view = mInflater.inflate(R.layout.item_search_merchant_list, null);
			holderView = new HolderView();
			holderView.vname = (TextView) view.findViewById(R.id.name);
			holderView.vpoint = (TextView) view.findViewById(R.id.point);
			holderView.vshop_price = (TextView) view
					.findViewById(R.id.shop_price);
			holderView.vmarket_price = (TextView) view
					.findViewById(R.id.marker_price);
			holderView.vaddress = (TextView) view.findViewById(R.id.address);
			holderView.vdistance = (TextView) view.findViewById(R.id.distance);
			holderView.vbtn_preference = (TextView) view
					.findViewById(R.id.btn_preference);

			view.setTag(holderView);
		} else {
			holderView = (HolderView) view.getTag();
		}
		Merchant merchant = (Merchant) getItem(position);
		holderView.vname.setText(merchant.getM_name());
		holderView.vpoint.setText(merchant.getGrade() + "分");
		holderView.vshop_price.setText(merchant.getShop_price());
		holderView.vmarket_price.setText(merchant.getMarket_price());
		holderView.vaddress.setText(merchant.getNear_station());
		holderView.vdistance.setText(StringUtils.getDistance(
				app.getLongitude(), app.getLatitude(), merchant.getLon() + "",
				merchant.getLon() + ""));
		holderView.vbtn_preference.setVisibility(View.GONE);

		return view;
	}

	class HolderView {
		TextView vname;
		TextView vpoint;
		TextView vshop_price;
		TextView vmarket_price;
		TextView vaddress;
		TextView vdistance;
		TextView vbtn_preference;
	}

}
