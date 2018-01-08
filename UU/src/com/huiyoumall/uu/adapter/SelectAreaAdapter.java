package com.huiyoumall.uu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.entity.CityItem;

/**
 * ��ҳѡ���� ������
 * 
 * @author ASUS
 * 
 */
public class SelectAreaAdapter extends BaseAdapter {

	private List<CityItem> items;
	private Context context;
	private LayoutInflater mInflater;
	private AppContext app;

	/** �Ƿ�ı� */

	public SelectAreaAdapter(Context context) {
		items = new ArrayList<CityItem>();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.app = (AppContext) context.getApplicationContext();
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
		if (convertView == null || convertView.getTag() == null) {
			mHolderView = new HolderView();
			convertView = this.mInflater.inflate(R.layout.item_area, null);
			mHolderView.cityView = (TextView) convertView
					.findViewById(R.id.city_item);
			convertView.setTag(mHolderView);
		} else {
			mHolderView = (HolderView) convertView.getTag();
		}

		CityItem item = (CityItem) getItem(position);
		mHolderView.cityView.setText(item.name);
		if (app.getSelectArea() != null) {
			if (item.name.equals(app.getSelectArea())) {
				mHolderView.cityView.setSelected(true);
				mHolderView.cityView.setEnabled(true);
			}
		}
		mHolderView.cityView.setTag(item);
		return convertView;
	}

	class HolderView {
		TextView cityView;
	}

}
