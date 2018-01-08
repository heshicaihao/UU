package com.huiyoumall.uu.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;

/**
 * U币商城 界面
 * 
 * @author ASUS
 * 
 */
public class UMallActivity extends BaseActivity implements OnClickListener {

	private TextView vTitle;
	private LinearLayout vMy_u_money;
	private LinearLayout vU_rule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_u_mall);
	}

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		vMy_u_money = (LinearLayout) findViewById(R.id.my_u_money_container);
		vU_rule = (LinearLayout) findViewById(R.id.u_rule_container);
	}

	@Override
	public void initView() {
		vTitle.setText("U币商城");
		vMy_u_money.setOnClickListener(this);
		vU_rule.setOnClickListener(this);

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

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.my_u_money_container:
			HelperUi.startActivity(UMallActivity.this, URecordActivity.class);
			break;
		case R.id.u_rule_container:
			HelperUi.startActivity(UMallActivity.this, URuleActivity.class);
			break;

		default:
			break;
		}

	}

}
