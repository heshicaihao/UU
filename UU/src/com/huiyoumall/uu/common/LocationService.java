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
 * ��ʱû���õ� Locationλ�ö�λ������
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
			mCriteria.setAccuracy(Criteria.ACCURACY_FINE); // ����Ϊ��󾫶�
			mCriteria.setAltitudeRequired(false); // ��Ҫ�󺣰���Ϣ
			mCriteria.setBearingRequired(false); // ��Ҫ��λ��Ϣ
			mCriteria.setCostAllowed(false); // ������
			mCriteria.setPowerRequirement(Criteria.POWER_LOW); // �Ե�����Ҫ���
		} catch (Exception e) {
		}

	}

	/**
	 * �ж��Ƿ�������λ����
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
	 * ��ת��λ�������
	 * 
	 * @param context
	 *            ȫ����Ϣ�ӿ�
	 */
	public void gotoLocServiceSettings(Context context) {
		final Intent intent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * ��תWIFI�������
	 * 
	 * @param context
	 *            ȫ����Ϣ�ӿ�
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
				Toast.makeText(context, "��λʧ��", Toast.LENGTH_SHORT).show();
			}
		};
	};
	public Thread thread = new Thread() {
		public void run() {
			Message msg = Message.obtain();
			String urlStr = MessageFormat.format(mapUriStr, mLatiTude,
					mLongTude);
			HttpGet httpGet = new HttpGet(urlStr);
			HttpClient httpClient = new DefaultHttpClient();// ����һ��http�ͻ���
			try {
				httpResponse = httpClient.execute(httpGet);
				httpEntity = httpResponse.getEntity();// ��ȡ��Ӧ����
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
