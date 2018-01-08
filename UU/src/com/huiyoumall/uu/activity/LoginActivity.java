package com.huiyoumall.uu.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.entity.User;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.util.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 登陆 界面
 * 
 * @author ASUS
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText vAccount, vPassword;
	private Button vBtnLogin;
	private ProgressDialog dialog;
	private AppContext app;
	private TextView vRegister, vfindPassword;
	private TextView vTitle;
	private ImageView input_account_delete, input_pass_delete;

	// private Handler mHandler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// dialog.dismiss();
	// if (msg.what == 1) {
	// Toast.makeText(LoginActivity.this, msg.obj.toString(),
	// Toast.LENGTH_SHORT).show();
	// }
	// };
	// };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public void findViewById() {
		vAccount = (EditText) findViewById(R.id.account);
		vPassword = (EditText) findViewById(R.id.pass);
		vBtnLogin = (Button) findViewById(R.id.btn_login);
		vRegister = (TextView) findViewById(R.id.register);
		vfindPassword = (TextView) findViewById(R.id.find_password);
		vTitle = (TextView) findViewById(R.id.title);
		input_account_delete = (ImageView) findViewById(R.id.input_account_delete);
		input_pass_delete = (ImageView) findViewById(R.id.input_pass_delete);
	}

	@Override
	public void initView() {
		vTitle.setText("登陆");
		app = (AppContext) getApplicationContext();
		vBtnLogin.setOnClickListener(this);
		vRegister.setOnClickListener(this);
		vfindPassword.setOnClickListener(this);
		dialog = new ProgressDialog(this);
		dialog.setTitle("登录中...");
		dialog.setCancelable(true);
		input_account_delete.setOnClickListener(this);
		input_pass_delete.setOnClickListener(this);
		input_account_delete.setVisibility(View.GONE);
		input_pass_delete.setVisibility(View.GONE);

		vAccount.addTextChangedListener(new TextWatcher() {
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
					input_account_delete.setVisibility(View.VISIBLE);
				} else {
					input_account_delete.setVisibility(View.GONE);
				}
			}
		});

		vPassword.addTextChangedListener(new TextWatcher() {
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
					input_pass_delete.setVisibility(View.VISIBLE);
				} else {
					input_pass_delete.setVisibility(View.GONE);
				}
			}
		});
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				String result = (String) msg.obj;
				if (result.equals("1")) {
					HelperUi.showToastLong(LoginActivity.this, "密码错误");
				} else if (result.equals("2")) {
					HelperUi.showToastLong(LoginActivity.this, "用户不存在");
				} else if (result.equals("3")) {
					HelperUi.showToastLong(LoginActivity.this, "未知错误");
				} else {
					User user = new User();
					try {
						JSONObject json = new JSONObject(result);
						user.setMember_id(json.getInt("member_id"));
						user.setMember_name(json.getString("member_name"));
						user.setMember_avatar(json.getString("member_avatar"));
						user.setMember_time(json.getString("member_time"));
						user.setMember_old_login_time(json
								.getString("member_old_login_time"));
						user.setMember_truename(json
								.getString("member_truename"));
						user.setMember_sex(json.getString("member_sex"));
						user.setMember_birthday(json
								.getString("member_birthday"));
						user.setMember_provinceid(json
								.getString("member_provinceid"));
						user.setMember_cityid(json.getString("member_cityid"));
						user.setMember_areaid(json.getString("member_areaid"));
						user.setMember_areainfo(json
								.getString("member_areainfo"));

						app.saveUserInfo(user);
						LoginActivity.this.finish();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} else {
				HelperUi.showToastLong(LoginActivity.this, "登录失败");
			}
		};
	};

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_login) {
			String userName = vAccount.getText().toString();
			String passWord = vPassword.getText().toString();
			if (StringUtils.isEmpty(userName)) {
				HelperUi.showToastLong(LoginActivity.this, "请输入手机号");
			} else if (StringUtils.isEmpty(passWord)) {
				HelperUi.showToastLong(LoginActivity.this, "请输入密码");
			} else if (!StringUtils.isPhoneNum(userName)) {
				HelperUi.showToastLong(LoginActivity.this, "您输入的手机号码有误!");
			} else {
				dialog.show();
				final Message msg = Message.obtain();
				UURemoteApi.login(userName, passWord,
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								dialog.dismiss();
								try {
									String sendString = new String(arg2,
											"UTF-8");
									msg.what = 1;
									msg.obj = sendString;
									mHandler.sendMessage(msg);
									// 如果登录成功
									/*
									 * User user = new User();
									 * app.saveUserInfo(user);
									 * LoginActivity.this.finish();
									 */
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								msg.what = -1;
								dialog.dismiss();
								mHandler.sendMessage(msg);

							}
						});
			}

		}
		if (v.getId() == R.id.register) {
			HelperUi.startActivity(LoginActivity.this, RegisterActivity.class);
		}
		if (v.getId() == R.id.find_password) {
			HelperUi.startActivity(LoginActivity.this,
					FindPasswordActivity.class);
		}
		if (v.getId() == R.id.input_account_delete) {
			vAccount.setText("");
			input_account_delete.setVisibility(View.GONE);
		}
		if (v.getId() == R.id.input_pass_delete) {
			vPassword.setText("");
			input_pass_delete.setVisibility(View.GONE);
		}
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
