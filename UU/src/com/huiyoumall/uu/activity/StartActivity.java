package com.huiyoumall.uu.activity;

import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.LocationSource;
import com.huiyoumall.uu.AppConfig;
import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.GlobalParams;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;

/**
 * ��ӭ ����
 * 
 * @author ASUS
 * 
 */
public class StartActivity extends BaseActivity implements LocationSource,
		AMapLocationListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		GlobalParams.WIN_WIDTH = metrics.widthPixels;
		GlobalParams.WIN_HEIGHT = metrics.heightPixels;
		initLocation();
		new Thread() {
			public void run() {
				try {
					super.sleep(3000);
					HelperUi.startActivity(StartActivity.this,
							MainActivity.class);
					finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * ��λ
	 */
	// private OnLocationChangedListener mListener;
	private LocationManagerProxy mLocationManagerProxy;
	private AppContext app;

	@Override
	public void findViewById() {

	}

	@Override
	public void initView() {
	}

	private void initLocation() {
		// // ע�ᶨλ�㲥
		// IntentFilter fliter = new IntentFilter(
		// ConnectivityManager.CONNECTIVITY_ACTION);
		// fliter.addAction(GPSLOCATION_BROADCAST_ACTION);
		// registerReceiver(mGPSLocationReceiver, fliter);
		app = (AppContext) getApplication();
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		boolean isNetWork = mLocationManagerProxy
				.isProviderEnabled(LocationManagerProxy.NETWORK_PROVIDER);// ������״̬
																			// ���綨λ
		boolean isGps = mLocationManagerProxy
				.isProviderEnabled(LocationManagerProxy.GPS_PROVIDER);
		if (isNetWork) {
			mLocationManagerProxy.setGpsEnable(false);
			mLocationManagerProxy.requestLocationData(
					LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);

		}
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if (aMapLocation != null
				&& aMapLocation.getAMapException().getErrorCode() == 0) {
			String cityName = aMapLocation.getCity();
			cityName = cityName.replace("��", "");
			String areaName = aMapLocation.getDistrict();
			String address = aMapLocation.getAddress();
			AppConfig.LONGITUDE = aMapLocation.getLongitude();
			AppConfig.LATITUDE = aMapLocation.getLatitude();

			app.saveLongitude(aMapLocation.getLongitude() + "");// ���澭��
			app.saveLatitude(aMapLocation.getLatitude() + "");// ����ά��
			// aMapLocation.getCityCode();

			app.saveLocationCity(cityName);
			app.saveLocationArea(areaName);

			// app.saveSelectCity(cityName);

			// Toast.makeText(StartActivity.this,
			// "���У�" + cityName + "���� " + areaName, Toast.LENGTH_SHORT)
			// .show();
			// aMapLocation.getCityCode();// ���б���
			Log.e("Location",
					cityName + areaName + address + aMapLocation.getCityCode());
		}

		/**
		 * if (mListener != null && amapLocation != null) { if (amapLocation !=
		 * null && amapLocation.getAMapException().getErrorCode() == 0) {
		 * mListener.onLocationChanged(amapLocation);// ��ʾϵͳС���� } else {
		 * Log.e("AmapErr", "Location ERR:" +
		 * amapLocation.getAMapException().getErrorCode()); } }
		 */
	}

	@Override
	protected void onPause() {
		super.onPause();
		// �Ƴ���λ����
		// mLocationManagerProxy.removeUpdates(mPendingIntent);
		// unregisterReceiver(mGPSLocationReceiver);
		// ���ٶ�λ
		mLocationManagerProxy.destroy();
		// deactivate();
	}

	@Override
	public void activate(OnLocationChangedListener arg0) {

	}

	@Override
	public void deactivate() {

	}

	// /**
	// * ���λ
	// */
	// @Override
	// public void activate(OnLocationChangedListener listener) {
	// mListener = listener;
	// if (mAMapLocationManager == null) {
	// mAMapLocationManager = LocationManagerProxy.getInstance(this);
	// // �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
	// // ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����removeUpdates()������ȡ����λ����
	// // �ڶ�λ�������ں��ʵ��������ڵ���destroy()����
	// // ����������ʱ��Ϊ-1����λֻ��һ��
	// // �ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������removeUpdates()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
	// mAMapLocationManager.requestLocationData(
	// LocationProviderProxy.AMapNetwork, 60 * 1000, 10, this);
	// }
	// }

	// /**
	// * ֹͣ��λ
	// */
	// @Override
	// public void deactivate() {
	// mListener = null;
	// if (mLocationManagerProxy != null) {
	// mLocationManagerProxy.removeUpdates(this);
	// mLocationManagerProxy.destroy();
	// }
	// mLocationManagerProxy = null;
	// }
}
