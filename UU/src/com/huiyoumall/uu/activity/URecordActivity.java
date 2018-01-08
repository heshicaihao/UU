package com.huiyoumall.uu.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;

/**
 * U币记录 界面
 * 
 * @author ASUS
 * 
 */
public class URecordActivity extends BaseActivity {

	private TextView vTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_u_money);
	}

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
	}

	@Override
	public void initView() {
		vTitle.setText("U币记录");

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
