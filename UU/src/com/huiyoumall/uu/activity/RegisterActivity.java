package com.huiyoumall.uu.activity;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.util.StringUtils;
import com.huiyoumall.uu.util.Util;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 注册账号 界面
 * 
 * @author ASUS
 * 
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initSMSReceiver();
	}

	private TextView vTitle;
	private TextView vUser_agreement;

	private EditText vInput_phone;
	private EditText vCode;
	private EditText vPassword;
	private EditText vRepead_password;

	private Button vBtn_get_code;
	private Button vBtn_register;

	private ImageView vInput_delete;
	private ImageView vInput_passs_delete;
	private ImageView vInput_repeadpasss_delete;
	private ImageView vCheck;
	private ProgressDialog mdDialog;

	private boolean flag = true;
	private int countSeconds = 60; // 倒数秒数
	private String phoneStr = null;

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		vUser_agreement = (TextView) findViewById(R.id.user_agreement);
		vInput_phone = (EditText) findViewById(R.id.input_phone);
		vPassword = (EditText) findViewById(R.id.password);
		vRepead_password = (EditText) findViewById(R.id.repead_password);
		vCode = (EditText) findViewById(R.id.code);
		vBtn_get_code = (Button) findViewById(R.id.get_code);
		vBtn_register = (Button) findViewById(R.id.btn_register);
		vInput_delete = (ImageView) findViewById(R.id.input_delete);
		vInput_passs_delete = (ImageView) findViewById(R.id.input_passs_delete);
		vInput_repeadpasss_delete = (ImageView) findViewById(R.id.input_repeadpasss_delete);
		vCheck = (ImageView) findViewById(R.id.check);
	}

	@Override
	public void initView() {
		vTitle.setText("注册账号");
		vUser_agreement.setOnClickListener(this);
		vCheck.setOnClickListener(this);
		vInput_delete.setOnClickListener(this);
		vInput_passs_delete.setOnClickListener(this);
		vInput_repeadpasss_delete.setOnClickListener(this);
		vBtn_get_code.setOnClickListener(this);
		vBtn_register.setOnClickListener(this);
		vInput_delete.setVisibility(View.GONE);
		vInput_passs_delete.setVisibility(View.GONE);
		vInput_repeadpasss_delete.setVisibility(View.GONE);
		vInput_phone.addTextChangedListener(new TextWatcher() {
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
					vInput_delete.setVisibility(View.VISIBLE);
				} else {
					vInput_delete.setVisibility(View.GONE);
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
					vInput_passs_delete.setVisibility(View.VISIBLE);
				} else {
					vInput_passs_delete.setVisibility(View.GONE);
				}
			}
		});

		vRepead_password.addTextChangedListener(new TextWatcher() {
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
					vInput_repeadpasss_delete.setVisibility(View.VISIBLE);
				} else {
					vInput_repeadpasss_delete.setVisibility(View.GONE);
				}
			}
		});

	}

	// private Handler mCountHanlder = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// if (countSeconds > 0) {
	// --countSeconds;
	// vBtn_get_code.setText("发送验证码" + "(" + countSeconds + ")");
	// mCountHanlder.sendEmptyMessageDelayed(0, 1000);
	// } else {
	// countSeconds = 60;
	// vBtn_get_code.setText("发送验证码");
	// }
	// }
	// };
	//
	// // 开始倒计时
	// private void startCountBack() {
	// runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// vBtn_get_code.setText(countSeconds + "");
	// mCountHanlder.sendEmptyMessage(0);
	// }
	// });
	// }

	final Handler handler = new Handler() { // handle
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (countSeconds > 0) {
					--countSeconds;
					vBtn_get_code.setText("发送验证码" + "(" + countSeconds + ")");
				} else {
					countSeconds = 60;
					vBtn_get_code.setText("发送验证码");
				}
			}
			super.handleMessage(msg);
		}
	};

	public class MyThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000); // sleep
					Message message = new Message();
					message.what = 1;
					handler1.sendMessage(message);
					if (countSeconds == 0) {
						break;
					}
				} catch (Exception e) {
				}
			}
		}
	}

	// public class MyThread implements Runnable{ // thread
	// @Override
	// public void run(){
	// while(true){
	// try{
	// Thread.sleep(1000); // sleep 1000ms
	// Message message = new Message();
	// message.what = 1;
	// handler.sendMessage(message);
	// }catch (Exception e) {
	// }}}

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

	private static int GET_MESSAGE_TO_REGISTER = 1;
	private static int REGISTER_ = 2;

	private Handler mRegHander = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 == GET_MESSAGE_TO_REGISTER) {
				vBtn_get_code.setEnabled(true);
				if (msg.what == 1) {
					String result = (String) msg.obj;
					if (result.equals("1")) {
						HelperUi.showToastShort(RegisterActivity.this, "发送成功");
						// startCountBack();
						new Thread(new MyThread()).start();
					} else if (result.equals("2")) {
						HelperUi.showToastShort(RegisterActivity.this,
								"手机号已注册，请返回登录");
					} else if (result.equals("3")) {
						HelperUi.showToastShort(RegisterActivity.this,
								"验证码发送失败！");
					}
				} else {
					HelperUi.showToastLong(RegisterActivity.this, "请求服务失败！");
				}
			} else if (msg.arg1 == REGISTER_) {
				dismissProgressDialog();
				if (msg.what == 1) {
					// 界面跳转
					String result = (String) msg.obj;
					if (result.equals("1")) {
						HelperUi.showToastShort(RegisterActivity.this, "注册成功");
						finish();
					} else if (result.equals("2")) {
						HelperUi.showToastShort(RegisterActivity.this, "注册失败");
					} else if (result.equals("0")) {
						HelperUi.showToastShort(RegisterActivity.this,
								"验证码已过期，请重新获取");
					}
				} else {
					HelperUi.showToastShort(RegisterActivity.this, "请求服务失败！");
				}
			}
		};
	};

	private long mExitTime = 0;

	@Override
	public void onClick(View v) {
		int id = v.getId();
		phoneStr = vInput_phone.getText().toString();
		switch (id) {
		case R.id.user_agreement:
			HelperUi.startActivity(RegisterActivity.this, TreatyActivity.class);
			break;
		case R.id.get_code:
			vBtn_get_code.setEnabled(false);
			hideSoftInput(this, vInput_phone);
			if (countSeconds != 60) {
				HelperUi.showToastShort(RegisterActivity.this, "您的验证码已发送!");
				return;
			}
			if (StringUtils.isEmpty(phoneStr)) {
				HelperUi.showToastShort(RegisterActivity.this, "请输入手机号!");
			} else if (!StringUtils.isPhoneNum(phoneStr)) {
				HelperUi.showToastShort(RegisterActivity.this, "您输入的手机号码有误!");
			} else {
				final Message msg = Message.obtain();
				UURemoteApi.GetRegisterCode(phoneStr,
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								msg.arg1 = GET_MESSAGE_TO_REGISTER;
								msg.what = 1;
								msg.obj = new String(arg2);
								mRegHander.sendMessage(msg);
							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								msg.arg1 = GET_MESSAGE_TO_REGISTER;
								msg.what = -1;
								mRegHander.sendMessage(msg);
							}
						});
			}
			break;
		case R.id.btn_register:
			hideSoftInput(this, vCode);
			String code = vCode.getText().toString();
			String pass = vPassword.getText().toString();
			String repeadPass = vRepead_password.getText().toString();
			if (StringUtils.isEmpty(phoneStr)) {
				HelperUi.showToastShort(RegisterActivity.this, "请输入手机号!");
			} else if (StringUtils.isEmpty(code)) {
				HelperUi.showToastShort(RegisterActivity.this, "请输入验证码!");
			} else if (StringUtils.isEmpty(pass)) {
				HelperUi.showToastShort(RegisterActivity.this, "请输入密码!");
			} else if (StringUtils.isEmpty(repeadPass)) {
				HelperUi.showToastShort(RegisterActivity.this, "请重复输入密码!");
			} else if (!StringUtils.isPhoneNum(phoneStr)) {
				HelperUi.showToastShort(RegisterActivity.this, "请输入正确手机号!");
			} else if (!StringUtils.isAllDigit(code)) {
				HelperUi.showToastShort(RegisterActivity.this, "验证码必须为6位数字!");
			} else if (!pass.equals(repeadPass)) {
				HelperUi.showToastShort(RegisterActivity.this, "两次输入密码不一致!");
			} else {
				getProgressDialog("注册中...");
				final Message msg = Message.obtain();
				UURemoteApi.upLoadRegister(phoneStr, pass, code,
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								msg.arg1 = REGISTER_;
								msg.what = 1;
								msg.obj = new String(arg2);
								mRegHander.sendMessage(msg);
							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								msg.arg1 = REGISTER_;
								msg.what = -1;
								mRegHander.sendMessage(msg);
							}
						});
			}
			break;
		case R.id.check:
			if (flag) {
				flag = false;
				vBtn_register.setEnabled(false);
				vCheck.setImageResource(R.drawable.check_2);
			} else {
				flag = true;
				vBtn_register.setEnabled(true);
				vCheck.setImageResource(R.drawable.checked_2);
			}
			break;
		case R.id.input_delete:
			vInput_phone.setText("");
			vInput_delete.setVisibility(View.GONE);
			break;
		case R.id.input_passs_delete:
			vPassword.setText("");
			vInput_passs_delete.setVisibility(View.GONE);
			break;
		case R.id.input_repeadpasss_delete:
			vRepead_password.setText("");
			vInput_repeadpasss_delete.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	/**
	 * 登录参数校验
	 * 
	 * @param phone
	 * @param pwd
	 * @return false || true
	 */
	public boolean LoginCheck() {
		if (!StringUtils.isPhoneNum(vInput_phone.getText().toString())) {
			HelperUi.showToastShort(RegisterActivity.this, "您输入的手机号码有误，请重新输入!");
			return false;
		}
		if (!StringUtils.isAllDigit(vCode.getText().toString())) {
			HelperUi.showToastShort(RegisterActivity.this, "您输入的验证码有误，请重新输入!");
			return false;
		}
		return true;
	}

	// 短信自动填充
	private BroadcastReceiver smsReceiver;
	private IntentFilter filter2;
	private Handler handler1;
	private String strContent;

	public void initSMSReceiver() {
		handler1 = new Handler() {
			public void handleMessage(android.os.Message msg) {
				vCode.setText(strContent);
			};
		};
		filter2 = new IntentFilter();
		filter2.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter2.setPriority(Integer.MAX_VALUE);
		smsReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Object[] objs = (Object[]) intent.getExtras().get("pdus");
				for (Object obj : objs) {
					byte[] pdu = (byte[]) obj;
					SmsMessage sms = SmsMessage.createFromPdu(pdu);
					// 短信的内容
					String message = sms.getMessageBody();
					Log.d("logo", "message     " + message);
					// 短息的手机号。。+86开头？
					String from = sms.getOriginatingAddress();
					Log.d("logo", "from     " + from);
					Time time = new Time();
					time.set(sms.getTimestampMillis());
					String time2 = time.format3339(true);
					Log.d("logo", from + "   " + message + "  " + time2);
					strContent = from + "   " + message;
					handler1.sendEmptyMessage(1);
					if (!TextUtils.isEmpty(from)) {
						String code = Util.patternCode(message);
						if (!TextUtils.isEmpty(code)) {
							strContent = code;
							handler1.sendEmptyMessage(1);
						}
					}
				}
			}
		};
		registerReceiver(smsReceiver, filter2);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(smsReceiver);
	}

}
