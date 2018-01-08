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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.util.Util;

/**
 * ���ֻ����� ����
 * 
 * @author ASUS
 * 
 */
public class BindingPhoneActivity extends BaseActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_binding_phone);

		initSMSReceiver();

	}

	private TextView vTitle;
	private EditText vCode;

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		vCode = (EditText) findViewById(R.id.code);
	}

	@Override
	public void initView() {
		vTitle.setText("���ֻ���");
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.get_code:

			break;
		case R.id.btn_register:

			break;

		default:
			break;
		}
	}

	// �����Զ����
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
					// ���ŵ�����
					String message = sms.getMessageBody();
					Log.d("logo", "message     " + message);
					// ��Ϣ���ֻ��š���+86��ͷ��
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
