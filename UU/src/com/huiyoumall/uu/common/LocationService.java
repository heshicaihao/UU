package com.huiyoumall.uu.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.widget.Toast;

import com.huiyoumall.uu.util.StringUtils;

/**
 * 暂时没有用到 Location位置定位服务类
 * 
 * @author heshuyuan
 * 
 */
public class LocationService {

	private final String TAG = "LocationServiceUtils";
	private String mapUriStr = "http://maps.google.cn/maps/api/geocode/json?latlng={0},{1}&sensor=true&language=zh-CN";
	private LocationManager mLocManager = null;
	private Location mLocation;
	public double mLatiTude;
	public double mLongTude;
	private Criteria mCriteria;
	private Context context;

	public LocationService(Context context) {
		this.context = context;
		try {
			mCriteria = new Criteria();
			mCriteria.setAccuracy(Criteria.ACCURACY_FINE); // 设置为最大精度
			mCriteria.setAltitudeRequired(false); // 不要求海拔信息
			mCriteria.setBearingRequired(false); // 不要求方位信息
			mCriteria.setCostAllowed(false); // 允许付费
			mCriteria.setPowerRequirement(Criteria.POWER_LOW); // 对电量的要求低
		} catch (Exception e) {
		}

	}

	/**
	 * 判断是否启动定位服务
	 * 
	 * @param context
	 * @return
	 */
	public boolean oPenLocService() {
		boolean isGps = false;
		boolean isNetWork = false;
		if (context != null) {
			mLocManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
			if (mLocManager != null) {
				isGps = mLocManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER);
				isNetWork = mLocManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			}
			if (isGps || isNetWork) {
				String last = mLocManager.getBestProvider(mCriteria, true);
				mLocation = mLocManager.getLastKnownLocation(last);
				if (mLocation != null) {
					mLocManager.requestLocationUpdates(last, 10000, 0,
							locatListener);
					mLatiTude = mLocation.getLatitude();
					mLongTude = mLocation.getLongitude();
					return true;
				}
			}
		}
		return false;
	}

	private LocationListener locatListener = new LocationListener() {

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
		public void onLocationChanged(Location location) {
			if (location != null) {
				mLatiTude = location.getLatitude();
				mLongTude = location.getLongitude();
			}
		}
	};

	/**
	 * 跳转定位服务界面
	 * 
	 * @param context
	 *            全局信息接口
	 */
	public void gotoLocServiceSettings(Context context) {
		final Intent intent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 跳转WIFI服务界面
	 * 
	 * @param context
	 *            全局信息接口
	 */
	public void gotoWifiServiceSettings(Context context) {
		final Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public void clear() {
		mLocManager = null;
	}

	private HttpResponse httpResponse = null;
	private HttpEntity httpEntity = null;
	private String result = "";
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			result = (String) msg.obj;
			if (!StringUtils.isEmpty(result)) {
				Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "定位失败", Toast.LENGTH_SHORT).show();
			}
		};
	};
	public Thread thread = new Thread() {
		public void run() {
			Message msg = Message.obtain();
			String urlStr = MessageFormat.format(mapUriStr, mLatiTude,
					mLongTude);
			HttpGet httpGet = new HttpGet(urlStr);
			HttpClient httpClient = new DefaultHttpClient();// 生成一个http客户端
			try {
				httpResponse = httpClient.execute(httpGet);
				httpEntity = httpResponse.getEntity();// 获取相应内容
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(httpEntity.getContent()));

				result = "";
				String line = "";
				while ((line = reader.readLine()) != null) {
					result += line;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			msg.obj = result;
			mHandler.sendMessage(msg);
		};
	};
}
