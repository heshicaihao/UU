package com.huiyoumall.uu.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;

/**
 * ���������� ����
 * 
 * @author ASUS
 * 
 */
public class SettingNewPassWordActivity extends BaseActivity {

	private TextView vTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_new_password);
	}

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
	}

	@Override
	public void initView() {
		vTitle.setText("����������");
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
