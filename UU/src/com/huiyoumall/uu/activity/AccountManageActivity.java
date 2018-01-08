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
import com.huiyoumall.uu.widget.MyAlertDialog;

/**
 * 账户管理 界面
 * 
 * @author ASUS
 * 
 */
public class AccountManageActivity extends BaseActivity implements
		OnClickListener {

	private TextView vTitle;
	private TextView vBinding_phone_text;
	private LinearLayout vBinding_phone;
	private LinearLayout vAmend_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_manage);
	}

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		vBinding_phone_text = (TextView) findViewById(R.id.binding_phone_text);
		vBinding_phone = (LinearLayout) findViewById(R.id.binding_phone_container);
		vAmend_password = (LinearLayout) findViewById(R.id.amend_password_container);

	}

	@Override
	public void initView() {
		vTitle.setText("账户管理");
		vBinding_phone.setOnClickListener(this);
		vAmend_password.setOnClickListener(this);

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
		case R.id.binding_phone_container:
			final MyAlertDialog dialog = new MyAlertDialog(this).builder()
					.setMsg("您已经绑定手机号码，\n\r确定要绑定新的手机号?")
					.setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					});
			dialog.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View v) {
					HelperUi.startActivity(AccountManageActivity.this,
							BindingPhoneActivity.class);
				}
			});
			dialog.show();

			break;
		case R.id.amend_password_container:
			HelperUi.startActivity(AccountManageActivity.this,
					AmendPasswordActivity.class);

			break;

		default:
			break;
		}
	}

}
