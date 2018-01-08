package com.huiyoumall.uu.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.util.Util;

/**
 * 找回密码的界面
 * 
 * @author ASUS
 * 
 */
public class FindPasswordActivity extends BaseActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password);

		initSMSReceiver();

	}

	private LinearLayout get_code_view;
	private LinearLayout input_pass_view;
	private TextView vTitle;
	private Button vBtnNext;
	private EditText vCode;

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		vBtnNext = (Button) findViewById(R.id.btn_next);
		get_code_view = (LinearLayout) findViewById(R.id.get_code_view);
		input_pass_view = (LinearLayout) findViewById(R.id.input_pass_view);
		vCode = (EditText) findViewById(R.id.code);
	}

	@Override
	public void initView() {
		vTitle.setText("找回密码");
		vBtnNext.setOnClickListener(this);
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
		case R.id.btn_next:
			HelperUi.startActivity(FindPasswordActivity.this,
					SettingNewPassWordActivity.class);

			break;

		default:
			break;
		}
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
