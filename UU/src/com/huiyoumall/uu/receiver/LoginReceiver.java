package com.huiyoumall.uu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huiyoumall.uu.AppConfig;
import com.huiyoumall.uu.AppContext;

public class LoginReceiver extends BroadcastReceiver {
	private AppContext app;

	public LoginReceiver() {
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		app = AppContext.getInstance();
		boolean strContent = app.isLogin;
		if (intent.getAction().equals(AppConfig.LOGIN_ACTION)) {
			mMessageListener.onReceived(strContent);
			abortBroadcast();
		}

	}

	private static MessageListener mMessageListener;

	// 回调接口
	public interface MessageListener {
		public void onReceived(boolean message);
	}

	public void setOnReceivedMessageListener(MessageListener messageListener) {
		this.mMessageListener = messageListener;
	}

}
