package com.huiyoumall.uu.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.adapter.SearchAutoAdapter;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.util.StringUtils;
import com.huiyoumall.uu.widget.GridViewForScollView;

/**
 * 首页 场馆搜索 界面
 * 
 * @author ASUS
 * 
 */
public class SearchMerchantActivity extends BaseActivity implements
		OnClickListener {

	public static final String SEARCH_HISTORY = "search_history";
	public static final String SEARCH_KEY = "content";
	private ListView mAutoListView;
	private Button mSearchButtoon;
	private LinearLayout vClear_his_search;
	private EditText mAutoEdit;
	private SearchAutoAdapter mSearchAutoAdapter;
	private List<String> mHisSearchs = new ArrayList<String>();
	private GridViewForScollView vGridMarks;

	public List<MMark> datas = new ArrayList<MMark>();

	public List<MMark> getDatas() {

		datas.add(new MMark("羽毛球场"));
		datas.add(new MMark("足球场"));
		datas.add(new MMark("篮球场"));
		datas.add(new MMark("游泳馆"));
		datas.add(new MMark("乒乓球馆"));
		datas.add(new MMark("健身房"));
		datas.add(new MMark("壁球"));
		return datas;

	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_merchant_search);

	}

	@Override
	public void findViewById() {
		vGridMarks = (GridViewForScollView) findViewById(R.id.mark_grid);
		vClear_his_search = (LinearLayout) findViewById(R.id.clear_his_search);
	}

	@Override
	public void initView() {

		MarkAdapter markAdapter = new MarkAdapter(this, getDatas());
		// markAdapter.setDatas(getDatas());
		vGridMarks.setAdapter(markAdapter);

		initSearchHistory();
		mSearchAutoAdapter = new SearchAutoAdapter(this, mHisSearchs);
		mAutoListView = (ListView) findViewById(R.id.auto_listview);
		mAutoListView.setAdapter(mSearchAutoAdapter);
		if (mHisSearchs.size() > 0) {
			vClear_his_search.setVisibility(View.VISIBLE);
		} else {
			vClear_his_search.setVisibility(View.GONE);
		}
		mAutoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				String data = mSearchAutoAdapter.getItem(position).toString();
				Intent intent = new Intent(SearchMerchantActivity.this,
						SearchListAcityity.class);
				intent.putExtra(SEARCH_KEY, data);
				startActivity(intent);
			}
		});

		mSearchButtoon = (Button) findViewById(R.id.search_button);
		mSearchButtoon.setOnClickListener(this);
		vClear_his_search.setOnClickListener(this);
		mAutoEdit = (EditText) findViewById(R.id.auto_edit);
	}

	public void onclickBack(View view) {
		this.finish();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.search_button) {// 搜索按钮

			Intent intent = new Intent(SearchMerchantActivity.this,
					SearchListAcityity.class);
			String con = mAutoEdit.getText().toString().trim();
			intent.putExtra(SEARCH_KEY, con);
			startActivity(intent);
			//
			saveSearchHistory();
			initSearchHistory();
			mSearchAutoAdapter.setObjects(mHisSearchs);

		}
		if (id == R.id.clear_his_search) {
			clearSearchHis();
		}
	}

	/*
	 * 保存搜索记录
	 */
	private void saveSearchHistory() {
		String text = mAutoEdit.getText().toString().trim();
		if (text.length() < 1) {
			return;
		}
		SharedPreferences sp = getSharedPreferences(SEARCH_HISTORY, 0);
		String longhistory = sp.getString(SEARCH_HISTORY, "");
		String[] tmpHistory = longhistory.split(",");
		ArrayList<String> history = new ArrayList<String>(
				Arrays.asList(tmpHistory));
		if (history.size() > 0) {
			int i;
			for (i = 0; i < history.size(); i++) {
				if (text.equals(history.get(i))) {
					history.remove(i);
					break;
				}
			}
			history.add(0, text);
		}
		if (history.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < history.size(); i++) {
				sb.append(history.get(i) + ",");
			}
			sp.edit().putString(SEARCH_HISTORY, sb.toString()).commit();
		} else {
			sp.edit().putString(SEARCH_HISTORY, text + ",").commit();
		}
	}

	/**
	 * 读取历史搜索记录
	 */
	public void initSearchHistory() {
		SharedPreferences sp = getSharedPreferences(
				SearchMerchantActivity.SEARCH_HISTORY, 0);
		String longhistory = sp.getString(
				SearchMerchantActivity.SEARCH_HISTORY, "");
		String[] hisArrays = longhistory.split(",");
		if (hisArrays.length == 1) {
			if (StringUtils.isEmpty(hisArrays[0])) {
				return;
			}
		}
		mHisSearchs.clear();
		for (int i = 0; i < hisArrays.length; i++) {
			mHisSearchs.add(hisArrays[i]);
		}
		if (mHisSearchs.size() > 0) {
			vClear_his_search.setVisibility(View.VISIBLE);
		} else {
			vClear_his_search.setVisibility(View.GONE);
		}
	}

	private void clearSearchHis() {
		SharedPreferences sp = getSharedPreferences(SEARCH_HISTORY, 0);
		sp.edit().clear().commit();
		mHisSearchs.clear();
		mSearchAutoAdapter.setObjects(mHisSearchs);
		vClear_his_search.setVisibility(View.GONE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		datas = null;
	}

	/**
	 * 热门标签的适配器
	 * 
	 * @author ASUS
	 * 
	 */
	public class MarkAdapter extends BaseAdapter {

		private List<MMark> marks;
		private Context context;
		private AppContext app;
		private LayoutInflater mInflater;

		/** 是否改变 */
		public MarkAdapter(Context context, List<MMark> list) {
			marks = list;
			this.context = context;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return marks.size();
		}

		@Override
		public Object getItem(int position) {
			return marks.get(position);
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
				convertView = mInflater.inflate(R.layout.item_mark, null);
				mHolderView.markText = (TextView) convertView
						.findViewById(R.id.mark_text);
				convertView.setTag(mHolderView);
			} else {
				mHolderView = (HolderView) convertView.getTag();
			}

			MMark mMark = (MMark) getItem(position);
			mHolderView.markText.setText(mMark.getMark());
			mHolderView.markText.setTag(mMark.getMark());
			mHolderView.markText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String mark = (String) v.getTag();
					Intent intent = new Intent(SearchMerchantActivity.this,
							SearchListAcityity.class);
					intent.putExtra(SEARCH_KEY, mark);
					startActivity(intent);
				}
			});
			return convertView;
		}

		class HolderView {
			TextView markText;
		}
	}

	public class MMark {
		String mark;

		public MMark() {
		}

		public MMark(String mark) {
			this.mark = mark;
		}

		public String getMark() {
			return mark;
		}

		public void setMark(String mark) {
			this.mark = mark;
		}
	}

}
