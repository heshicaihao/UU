package com.huiyoumall.uu.base;

import java.io.Serializable;

import android.app.Activity;
import android.view.View;

import com.huiyoumall.uu.util.ACache;
import com.huiyoumall.uu.view.pullview.AbPullToRefreshView;
import com.huiyoumall.uu.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.huiyoumall.uu.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;

public abstract class BaseListActivity extends Activity implements
		OnHeaderRefreshListener, OnFooterLoadListener {

	public int currentPagte = 1;

	protected int type;
	public static int FIRST_LOAD = 1;
	public static int LOAD_MORE_DATA = 2;
	public static int REFRESH_DATA = 3;

	public static int LOAD_SUCCESS = 1;
	public static int LOAD_ERROR = -1;

	@Override
	public void onFooterLoad(AbPullToRefreshView view) {
		loadMore();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
		refreshData();
	}

	protected abstract void firstLoad();

	protected abstract void loadMore();

	protected abstract void refreshData();

	/**
	 * 获取Serializable缓存
	 * 
	 * @param key
	 * @return
	 */
	public Object getCacheSerializable(String key) {
		return ACache.get(this).getAsObject(key);
	}

	/**
	 * 缓存Serializable数据
	 * 
	 * @param key
	 * @param value
	 */
	public void setCacheSerializable(String key, Serializable value) {
		ACache.get(this).put(key, value);
	}

	public void activitybackview(View view) {
		this.finish();
	}
}
