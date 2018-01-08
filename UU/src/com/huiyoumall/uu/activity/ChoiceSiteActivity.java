package com.huiyoumall.uu.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.db.DataHelper;
import com.huiyoumall.uu.entity.CityItem;
import com.huiyoumall.uu.widget.GridViewForScollView;

/**
 * 首页 选择城市 界面
 * 
 * @author ASUS
 * 
 */
public class ChoiceSiteActivity extends BaseActivity {

	private final static String fileName = "city4.json";
	private GridViewForScollView vListView;
	private TextView vTitle;
	private TextView vCity;
	private List<CityItem> mData;
	private DataHelper dh = null;
	private SelectCityAdapter mAdapters;
	private AppContext app;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_choice_site);
	}

	@Override
	public void findViewById() {
		vListView = (GridViewForScollView) findViewById(R.id.select_city_list);
		vTitle = (TextView) findViewById(R.id.title);
		vCity = (TextView) findViewById(R.id.city_text);
	}

	@Override
	public void initView() {
		vTitle.setText("选择城市");

		app = (AppContext) getApplicationContext();
		dh = new DataHelper(this);
		mData = dh.query("parent=0");

		if (app.getLocationCity() != null) {
			vCity.setText(app.getLocationCity());
		} else {
			vCity.setText("深圳市");
		}
		vCity.setSelected(true);
		vCity.setEnabled(true);
		mAdapters = new SelectCityAdapter(this, mData);
		vListView.setAdapter(mAdapters);
		vListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CityItem item = mData.get(position);
				app.saveSelectCity(item.name);
				app.cleanSaveArea();
				finishResult();
			}
		});
	}

	/**
	 * 读取本地文件中JSON字符串
	 * 
	 * @param fileName
	 * @return
	 */
	public String getJson(String fileName) {

		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					getAssets().open(fileName)));
			String line;
			while ((line = bf.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	public void activitybackview(View view) {
		finishResult();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishResult();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void finishResult() {
		setResult(RESULT_OK);
		finish();
	}

	public class SelectCityAdapter extends BaseAdapter {

		private List<CityItem> items;
		private LayoutInflater mInflater;
		private AppContext app;

		/** 是否改变 */

		public SelectCityAdapter(Context context, List<CityItem> datas) {
			this.items = datas;
			this.mInflater = LayoutInflater.from(context);
			this.app = (AppContext) context.getApplicationContext();
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
			if (app.getSelectCity() != null) {
				if (item.name.equals(app.getSelectCity())) {
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

}
