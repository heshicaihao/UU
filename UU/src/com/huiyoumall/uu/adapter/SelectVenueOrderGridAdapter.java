package com.huiyoumall.uu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyoumall.uu.R;
import com.huiyoumall.uu.entity.Order.TDeatil;

/**
 * 选择场馆订单 适配器
 * 
 * @author ASUS
 * 
 */
public class SelectVenueOrderGridAdapter extends BaseAdapter {
	private List<TDeatil> items;
	private Context context;
	private LayoutInflater mInflater;

	/** 是否改变 */

	public SelectVenueOrderGridAdapter(Context context, List<TDeatil> list) {
		this.items = list;
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
	}

	public void setDatas(List<TDeatil> list) {
		this.items = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView mHolderView = null;
		if (convertView == null) {
			mHolderView = new HolderView();
			convertView = this.mInflater.inflate(R.layout.item_choose_order,
					null);
			mHolderView.court_time = (TextView) convertView
					.findViewById(R.id.court_time);
			mHolderView.court_num = (TextView) convertView
					.findViewById(R.id.court_num);
			convertView.setTag(mHolderView);
		} else {
			mHolderView = (HolderView) convertView.getTag();
		}
		TDeatil item = (TDeatil) getItem(position);
		mHolderView.court_time.setText(item.getTime_quantum());
		mHolderView.court_num.setText(item.getT_name());

		mHolderView.court_num.setTag(item);
		return convertView;
	}

	class HolderView {
		TextView court_time;
		TextView court_num;
	}

	// private String getSiteTime(String time_quantum) {
	// int index = Integer.parseInt(time_quantum.split("_")[1]);
	// String time = "";
	// switch (index) {
	// case 0:
	// time = "8:00-9:00";
	// break;
	// case 1:
	// time = "9:00-10:00";
	// break;
	// case 2:
	// time = "10:00-11:00";
	// break;
	// case 3:
	// time = "11:00-12:00";
	// break;
	// case 4:
	// time = "12:00-13:00";
	// break;
	// case 5:
	// time = "13:00-14:00";
	// break;
	// case 6:
	// time = "14:00-15:00";
	// break;
	// case 7:
	// time = "15:00-16:00";
	// break;
	// case 8:
	// time = "16:00-17:00";
	// break;
	// case 9:
	// time = "17:00-18:00";
	// break;
	// case 10:
	// time = "18:00-19:00";
	// break;
	// case 11:
	// time = "19:00-20:00";
	// break;
	// case 12:
	// time = "20:00-21:00";
	// break;
	// case 13:
	// time = "21:00-22:00";
	// break;
	// case 14:
	// time = "22:00-23:00";
	// break;
	// case 15:
	// time = "23:00-0:00";
	// break;
	// default:
	// break;
	// }
	// return time;
	// }
}
