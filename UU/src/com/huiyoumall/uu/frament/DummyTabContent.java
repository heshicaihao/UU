package com.huiyoumall.uu.frament;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

/**
 * 重新了TabContentFactory 用于订单管理
 * 
 * @author ASUS
 * 
 */
public class DummyTabContent implements TabContentFactory {
	private Context mContext;

	public DummyTabContent(Context context) {
		mContext = context;
	}

	@Override
	public View createTabContent(String tag) {
		View v = new View(mContext);
		return v;
	}
}
