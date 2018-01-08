package com.huiyoumall.uu.frament;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.activity.EditPersonalInfoActivity;
import com.huiyoumall.uu.activity.LoginActivity;
import com.huiyoumall.uu.activity.MyBrowseActivity;
import com.huiyoumall.uu.activity.MyCoachActivity;
import com.huiyoumall.uu.activity.MyCollectActivity;
import com.huiyoumall.uu.activity.MyCouponActivity;
import com.huiyoumall.uu.activity.MyHuodongActivity;
import com.huiyoumall.uu.activity.MyOrderActivity;
import com.huiyoumall.uu.activity.SettingActivity;
import com.huiyoumall.uu.activity.SystemInfoActivity;
import com.huiyoumall.uu.activity.UMallActivity;
import com.huiyoumall.uu.widget.MyAlertDialog;

/**
 * 我 Frament
 * 
 * @author ASUS
 * 
 */
public class MiActivity extends Activity implements View.OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mi);
		initView();
	}

	private AppContext app;
	private LinearLayout vPersen_center_login;
	private LinearLayout vPerson_center_nulogin;
	private LinearLayout vMyOrder;
	private LinearLayout vMyHuodong;
	private LinearLayout vMyJiaolian;
	private LinearLayout vMyCoupon;
	private LinearLayout vUMall;
	private LinearLayout vMyBrowse;
	private LinearLayout vMyCollect;
	private LinearLayout vSetingShare;
	private LinearLayout vTel_me;
	private LinearLayout vSystemInfo;
	private LinearLayout vSetting_container;

	private ImageView vMan_image;
	private boolean isLogin = false;

	private void initView() {
		vPersen_center_login = (LinearLayout) findViewById(R.id.persen_center_login);
		vPerson_center_nulogin = (LinearLayout) findViewById(R.id.persen_center_unlogin);
		vMyOrder = (LinearLayout) findViewById(R.id.my_order_container);
		vMyHuodong = (LinearLayout) findViewById(R.id.my_huodong_container);
		vMyJiaolian = (LinearLayout) findViewById(R.id.my_coach_container);
		vMyCoupon = (LinearLayout) findViewById(R.id.my_coupon_container);
		vUMall = (LinearLayout) findViewById(R.id.my_u_mall_container);
		vMyBrowse = (LinearLayout) findViewById(R.id.my_browse_container);
		vMyCollect = (LinearLayout) findViewById(R.id.my_collect_container);
		vSetingShare = (LinearLayout) findViewById(R.id.seting_share_container);
		vTel_me = (LinearLayout) findViewById(R.id.tel_me_container);
		vSystemInfo = (LinearLayout) findViewById(R.id.system_info_container);
		vSetting_container = (LinearLayout) findViewById(R.id.setting_container);

		vMan_image = (ImageView) findViewById(R.id.man_image);
		vPerson_center_nulogin.setOnClickListener(this);
		vMyOrder.setOnClickListener(this);
		vMyHuodong.setOnClickListener(this);
		vMyJiaolian.setOnClickListener(this);
		vMyCoupon.setOnClickListener(this);
		vUMall.setOnClickListener(this);
		vMyBrowse.setOnClickListener(this);
		vMyCollect.setOnClickListener(this);
		vSetingShare.setOnClickListener(this);
		vTel_me.setOnClickListener(this);
		vSystemInfo.setOnClickListener(this);
		vSetting_container.setOnClickListener(this);
		vPersen_center_login.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		app = (AppContext) getApplicationContext();
		isLogin = app.isLogin;
		if (isLogin) {
			vPerson_center_nulogin.setVisibility(View.GONE);
			vPersen_center_login.setVisibility(View.VISIBLE);
		} else {
			vPerson_center_nulogin.setVisibility(View.VISIBLE);
			vPersen_center_login.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.persen_center_unlogin:
			HelperUi.startActivity(this, LoginActivity.class);
			break;
		case R.id.persen_center_login:
			HelperUi.startActivity(this, EditPersonalInfoActivity.class);
			break;
		case R.id.my_order_container:
			if (isLogin) {
				HelperUi.startActivity(this, MyOrderActivity.class);
			} else {
				HelperUi.startActivity(this, LoginActivity.class);
			}

			break;
		case R.id.my_huodong_container:
			if (isLogin) {
				HelperUi.startActivity(this, MyHuodongActivity.class);
			} else {
				HelperUi.startActivity(this, LoginActivity.class);
			}

			break;
		case R.id.my_coach_container:
			if (isLogin) {
				HelperUi.startActivity(this, MyCoachActivity.class);
			} else {
				HelperUi.startActivity(this, LoginActivity.class);
			}

			break;

		case R.id.my_coupon_container:
			if (isLogin) {
				HelperUi.startActivity(this, MyCouponActivity.class);
			} else {
				HelperUi.startActivity(this, LoginActivity.class);
			}

			break;
		case R.id.my_u_mall_container:
			if (isLogin) {
				HelperUi.startActivity(this, UMallActivity.class);
			} else {
				HelperUi.startActivity(this, LoginActivity.class);
			}

			break;
		case R.id.my_browse_container:
			HelperUi.startActivity(this, MyBrowseActivity.class);
			break;
		case R.id.my_collect_container:
			if (isLogin) {
				HelperUi.startActivity(this, MyCollectActivity.class);
			} else {
				HelperUi.startActivity(this, LoginActivity.class);
			}

			break;
		case R.id.seting_share_container:
			break;

		case R.id.tel_me_container:
			showPhoneDialog();
			break;

		case R.id.system_info_container:
			HelperUi.startActivity(this, SystemInfoActivity.class);
			break;

		case R.id.setting_container:
			HelperUi.startActivity(this, SettingActivity.class);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			Bitmap bitmap = (Bitmap) data.getParcelableExtra("bitmap");
			vMan_image.setImageBitmap(bitmap);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
