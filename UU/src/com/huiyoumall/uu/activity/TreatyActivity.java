package com.huiyoumall.uu.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.huiyoumall.uu.AppConfig;
import com.huiyoumall.uu.ConstantURL;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;

/**
 * UU用户协定 界面
 * 
 * @author ASUS
 * 
 */
public class TreatyActivity extends BaseActivity {

	private TextView vTitle;

	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_treaty);
		AppConfig.initWebView(webview, ConstantURL.TREATY_URL);
	}

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		webview = (WebView) findViewById(R.id.webview);
	}

	@Override
	public void initView() {
		vTitle.setText("UU用户协定");

	}

	@Override
	public void activitybackview(View view) {
		super.activitybackview(view);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
