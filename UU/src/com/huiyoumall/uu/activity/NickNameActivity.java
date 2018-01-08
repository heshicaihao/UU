package com.huiyoumall.uu.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.util.StringUtils;

/**
 * 个人信息编辑 昵称 界面
 * 
 * @author ASUS
 * 
 */
public class NickNameActivity extends BaseActivity implements OnClickListener {

	private TextView vTitle;
	private TextView vRight_btn;
	private ImageView vDelete;
	private EditText vNick_name;
	private String NickName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nick_name);
	}

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		vRight_btn = (TextView) findViewById(R.id.right_btn);
		vDelete = (ImageView) findViewById(R.id.input_account_delete);
		vNick_name = (EditText) findViewById(R.id.nick_name_edit);
	}

	@Override
	public void initView() {
		vTitle.setText("昵称");
		vRight_btn.setText("确定");
		vRight_btn.setOnClickListener(this);
		vDelete.setOnClickListener(this);

		vNick_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() > 0) {
					vDelete.setVisibility(View.VISIBLE);
				} else {
					vDelete.setVisibility(View.GONE);
				}
			}
		});

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
			if (!StringUtils.isEmpty(vNick_name.getText().toString())) {
				NickName = vNick_name.getText().toString();
			}
			SharedPreferences mySharedPreferences = getSharedPreferences(
					"Person_Info", this.MODE_PRIVATE);
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putString("NickName", NickName);
			editor.commit();
			break;
		case R.id.input_account_delete:
			vNick_name.setText("");
			vDelete.setVisibility(View.GONE);

			break;
		default:
			break;
		}
	}

}
