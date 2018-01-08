package com.huiyoumall.uu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.common.TTSController;
import com.huiyoumall.uu.util.Util;

/**
 * 
 * ���� ����
 * 
 * */
public class UUNaviActivity extends Activity implements AMapNaviViewListener {
	// ����View
	private AMapNaviView mAmapAMapNaviView;
	// �Ƿ�Ϊģ�⵼��
	private boolean mIsEmulatorNavi = true;
	// ��¼���ĸ�ҳ����ת�����������ؼ�
	private int mCode = -1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uunavi);
		Bundle bundle = getIntent().getExtras();
		processBundle(bundle);
		init(savedInstanceState);

	}

	private void processBundle(Bundle bundle) {
		if (bundle != null) {
			mIsEmulatorNavi = bundle.getBoolean(Util.ISEMULATOR, true);
			mCode = bundle.getInt(Util.ACTIVITYINDEX);
		}
	}

	/**
	 * ��ʼ��
	 * 
	 * @param savedInstanceState
	 */
	private void init(Bundle savedInstanceState) {
		mAmapAMapNaviView = (AMapNaviView) findViewById(R.id.simplenavimap);
		mAmapAMapNaviView.onCreate(savedInstanceState);
		mAmapAMapNaviView.setAMapNaviViewListener(this);
		TTSController.getInstance(this).startSpeaking();
		if (mIsEmulatorNavi) {
			// ����ģ���ٶ�
			AMapNavi.getInstance(this).setEmulatorNaviSpeed(100);
			// ����ģ�⵼��
			AMapNavi.getInstance(this).startNavi(AMapNavi.EmulatorNaviMode);

		} else {
			// ����ʵʱ����
			AMapNavi.getInstance(this).startNavi(AMapNavi.GPSNaviMode);
		}
	}

	// -----------------------------��������ص��¼�------------------------
	/**
	 * �������淵�ذ�ť����
	 * */
	@Override
	public void onNaviCancel() {
		Intent intent = new Intent(UUNaviActivity.this,
				MerchantDetailsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		finish();
	}

	@Override
	public void onNaviSetting() {

	}

	@Override
	public void onNaviMapMode(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviTurnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNextRoadClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScanViewButtonClick() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * ���ؼ������¼�
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mCode == Util.SIMPLEROUTENAVI) {
				Intent intent = new Intent(UUNaviActivity.this,
						MerchantDetailsActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();

			} else if (mCode == Util.SIMPLEGPSNAVI) {
				Intent intent = new Intent(UUNaviActivity.this,
						MapNavigationActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	// ------------------------------�������ڷ���---------------------------
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mAmapAMapNaviView.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		super.onResume();
		mAmapAMapNaviView.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		mAmapAMapNaviView.onPause();
		AMapNavi.getInstance(this).stopNavi();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mAmapAMapNaviView.onDestroy();

		TTSController.getInstance(this).stopSpeaking();

	}

	@Override
	public void onLockMap(boolean arg0) {

	}

}
