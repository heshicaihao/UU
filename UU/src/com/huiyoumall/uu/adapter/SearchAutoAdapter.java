package com.huiyoumall.uu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyoumall.uu.R;

/**
 * ËÑË÷¼ÇÂ¼ ÊÊÅäÆ÷
 * 
 * @author ASUS
 * 
 */
public class SearchAutoAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> mObjects;

	public SearchAutoAdapter(Context context, List<String> listStrings) {
		this.mContext = context;
		mObjects = listStrings;
	}

	public void setObjects(List<String> oList) {
		this.mObjects = oList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public Object getItem(int position) {
		return mObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AutoHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_seach, parent, false);
			holder = new AutoHolder();
			holder.content = (TextView) convertView
					.findViewById(R.id.auto_content);
			convertView.setTag(holder);
		} else {
			holder = (AutoHolder) convertView.getTag();
		}

		holder.content.setText(getItem(position).toString());
		holder.content.setTag(getItem(position).toString());
		return convertView;
	}

	private class AutoHolder {
		TextView content;
	}
}
