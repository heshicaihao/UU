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
 * 欢迎 界面
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
	 * 定位
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
		// // 注册定位广播
		// IntentFilter fliter = new IntentFilter(
		// ConnectivityManager.CONNECTIVITY_ACTION);
		// fliter.addAction(GPSLOCATION_BROADCAST_ACTION);
		// registerReceiver(mGPSLocationReceiver, fliter);
		app = (AppContext) getApplication();
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		boolean isNetWork = mLocationManagerProxy
				.isProviderEnabled(LocationManagerProxy.NETWORK_PROVIDER);// 有网络状态
																			// 网络定位
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
			cityName = cityName.replace("市", "");
			String areaName = aMapLocation.getDistrict();
			String address = aMapLocation.getAddress();
			AppConfig.LONGITUDE = aMapLocation.getLongitude();
			AppConfig.LATITUDE = aMapLocation.getLatitude();

			app.saveLongitude(aMapLocation.getLongitude() + "");// 保存经度
			app.saveLatitude(aMapLocation.getLatitude() + "");// 保存维度
			// aMapLocation.getCityCode();

			app.saveLocationCity(cityName);
			app.saveLocationArea(areaName);

			// app.saveSelectCity(cityName);

			// Toast.makeText(StartActivity.this,
			// "城市：" + cityName + "区县 " + areaName, Toast.LENGTH_SHORT)
			// .show();
			// aMapLocation.getCityCode();// 城市编码
			Log.e("Location",
					cityName + areaName + address + aMapLocation.getCityCode());
		}

		/**
		 * if (mListener != null && amapLocation != null) { if (amapLocation !=
		 * null && amapLocation.getAMapException().getErrorCode() == 0) {
		 * mListener.onLocationChanged(amapLocation);// 显示系统小蓝点 } else {
		 * Log.e("AmapErr", "Location ERR:" +
		 * amapLocation.getAMapException().getErrorCode()); } }
		 */
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 移除定位请求
		// mLocationManagerProxy.removeUpdates(mPendingIntent);
		// unregisterReceiver(mGPSLocationReceiver);
		// 销毁定位
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
	// * 激活定位
	// */
	// @Override
	// public void activate(OnLocationChangedListener listener) {
	// mListener = listener;
	// if (mAMapLocationManager == null) {
	// mAMapLocationManager = LocationManagerProxy.getInstance(this);
	// // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
	// // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
	// // 在定位结束后，在合适的生命周期调用destroy()方法
	// // 其中如果间隔时间为-1，则定位只定一次
	// // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
	// mAMapLocationManager.requestLocationData(
	// LocationProviderProxy.AMapNetwork, 60 * 1000, 10, this);
	// }
	// }

	// /**
	// * 停止定位
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
