package com.huiyoumall.uu.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyoumall.uu.AppConfig;
import com.huiyoumall.uu.ConstantURL;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.widget.MyAlertDialog;

/**
 * 支付帮助 界面
 * 
 * @author ASUS
 * 
 */
public class PayHelpActivity extends BaseActivity implements OnClickListener {

	private TextView vTitle;
	private WebView webview;
	private ImageView vPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_help);
		AppConfig.initWebView(webview, ConstantURL.PAY_HELP_URL);

	}

	@Override
	public void findViewById() {

		vTitle = (TextView) findViewById(R.id.title);
		webview = (WebView) findViewById(R.id.webview);
		vPhone = (ImageView) findViewById(R.id.phone_image);
	}

	@Override
	public void initView() {
		vTitle.setText("支付帮助");
		vPhone.setOnClickListener(this);

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
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.phone_image:
			showPhoneDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * 拨打电话对话框
	 */
	public void showPhoneDialog() {
		final MyAlertDialog dialog = new MyAlertDialog(this).builder()
				.setMsg("400-835-9188")
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
		dialog.setPositiveButton("呼叫", new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone = "4008359188";
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ phone));
				startActivity(intent);
			}
		});
		dialog.show();
	}

}
