package com.huiyoumall.uu.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.util.StringUtils;

/**
 * 个性签名 界面
 * 
 * @author ASUS
 * 
 */
public class PersonSignatureActivity extends BaseActivity implements
		OnClickListener {

	private TextView vTitle;
	private TextView vRight_btn;
	private EditText vPerson_signature_edit;
	private String Person_signature;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_signature);
	}

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		vRight_btn = (TextView) findViewById(R.id.right_btn);
		vPerson_signature_edit = (EditText) findViewById(R.id.person_signature_edit);
	}

	@Override
	public void initView() {
		vTitle.setText("个性签名");
		vRight_btn.setText("确定");
		vRight_btn.setOnClickListener(this);

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
		case R.id.right_btn:
			if (!StringUtils.isEmpty(vPerson_signature_edit.getText()
					.toString())) {
				Person_signature = vPerson_signature_edit.getText().toString();
			}
			SharedPreferences mySharedPreferences = getSharedPreferences(
					"Person_Info", this.MODE_PRIVATE);
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putString("Person_signature", Person_signature);
			editor.commit();
			break;

		default:
			break;
		}
	}

}
