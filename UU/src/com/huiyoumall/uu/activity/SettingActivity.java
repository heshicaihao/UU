package com.huiyoumall.uu.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.image.WebImageCache;
import com.huiyoumall.uu.util.StringUtils;
import com.huiyoumall.uu.widget.MyAlertDialog;

/**
 * 设置 界面
 * 
 * @author ASUS
 * 
 */
public class SettingActivity extends BaseActivity implements OnClickListener {

	private Button vLoginOut;
	private AppContext app;
	private TextView vTitle;

	private LinearLayout vAccount_manage;
	private LinearLayout vAbout_uu;
	private LinearLayout vSuggestion;
	private LinearLayout vClear_cache;

	TextView cache_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
	}

	@Override
	public void findViewById() {

		vTitle = (TextView) findViewById(R.id.title);
		vLoginOut = (Button) findViewById(R.id.btn_logout);
		vAccount_manage = (LinearLayout) findViewById(R.id.account_manage_container);
		vAbout_uu = (LinearLayout) findViewById(R.id.about_uu_container);
		vSuggestion = (LinearLayout) findViewById(R.id.suggestion_container);
		vClear_cache = (LinearLayout) findViewById(R.id.clear_cache_container);
		cache_text = (TextView) findViewById(R.id.cache_text);

		vLoginOut.setOnClickListener(this);
		vAccount_manage.setOnClickListener(this);
		vAbout_uu.setOnClickListener(this);
		vSuggestion.setOnClickListener(this);
		vClear_cache.setOnClickListener(this);

	}

	@Override
	public void initView() {
		vTitle.setText("设置");
		try {
			String cache = WebImageCache.getTotalCacheSize(this);
			if (!StringUtils.isEmpty(cache)) {
				cache_text.setText(cache);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		app = (AppContext) getApplicationContext();
		if (!app.isLogin) {
			vLoginOut.setText("登录");
		} else {
			vLoginOut.setText("退出登录");
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

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.account_manage_container:
			HelperUi.startActivity(SettingActivity.this,
					AccountManageActivity.class);
			break;
		case R.id.about_uu_container:
			HelperUi.startActivity(SettingActivity.this, AboutUUActivity.class);
			break;
		case R.id.suggestion_container:
			HelperUi.startActivity(SettingActivity.this,
					SuggestionActivity.class);
			break;
		case R.id.clear_cache_container:
			showClearCacheDialog();
			break;
		case R.id.btn_logout:
			if (app.isLogin) {
				app.cleanisLoginInfo();
				SettingActivity.this.finish();
				vLoginOut.setText("登录");
			} else {
				HelperUi.startActivity(SettingActivity.this,
						LoginActivity.class);
			}

			break;
		default:
			break;
		}

	}

	/**
	 * 清理缓存的对话框
	 */
	public void showClearCacheDialog() {
		final MyAlertDialog dialog = new MyAlertDialog(this).builder()
				.setMsg("是否清理缓存")
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
		dialog.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				WebImageCache.clearAllCache(SettingActivity.this);
				try {
					String cache = WebImageCache
							.getTotalCacheSize(SettingActivity.this);
					if (!StringUtils.isEmpty(cache)) {
						cache_text.setText(cache);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		dialog.show();
	}
}
