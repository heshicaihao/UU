package com.huiyoumall.uu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyoumall.uu.R;
import com.huiyoumall.uu.entity.CityItem;

/**
 * 首页选择城市 适配器
 * 
 * @author ASUS
 * 
 */
public class ChooseItemAdapter extends BaseAdapter {

	private List<CityItem> items;
	private Context context;
	private LayoutInflater mInflater;

	/** 是否改变 */

	public ChooseItemAdapter(Context context) {
		this.items = new ArrayList<CityItem>();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
	}

	public void setDatas(List<CityItem> list) {
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
			convertView = this.mInflater.inflate(R.layout.item_item_choose,
					null);
			mHolderView.item_text_view = (TextView) convertView
					.findViewById(R.id.item_text_view);
			convertView.setTag(mHolderView);
		} else {
			mHolderView = (HolderView) convertView.getTag();
		}
		CityItem item = (CityItem) getItem(position);
		mHolderView.item_text_view.setText(item.name);
		mHolderView.item_text_view.setTag(item);
		return convertView;
	}

	public class HolderView {
		public TextView item_text_view;
	}

}
