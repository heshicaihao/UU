package com.huiyoumall.uu.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;

/**
 * 消息 界面
 * 
 * @author ASUS
 * 
 */
public class SystemInfoActivity extends BaseActivity {

	private TextView vTitle;
	private TextView vRight_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_info);
	}

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		vRight_btn = (TextView) findViewById(R.id.right_btn);
	}

	@Override
	public void initView() {
		vTitle.setText("消息");
		vRight_btn.setText("管理");

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
