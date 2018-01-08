package com.huiyoumall.uu.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.entity.Merchant;
import com.huiyoumall.uu.entity.Sport;
import com.huiyoumall.uu.frament.BookActivity;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MapLocationActivity2 extends Activity implements
		OnMarkerClickListener, OnInfoWindowClickListener, OnMapLoadedListener,
		InfoWindowAdapter, OnMapClickListener, LocationSource,
		AMapLocationListener, OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_location);
		mapView = (MapView) findViewById(R.id.map);
		vBtn_location = (ImageView) findViewById(R.id.id_btn_location);
		mapView.onCreate(savedInstanceState); // 此方法必须重写
		init();
	}

	private MarkerOptions markerOption;
	private AMap aMap;
	private MapView mapView;
	private Marker mMarker;
	private LocationManagerProxy aMapManager;
	private OnLocationChangedListener mListener;
	private UiSettings mUiSettings;
	private AMapLocation aLocation;
	private boolean isFirst = true;
	private List<Merchant> mMerchants = new ArrayList<Merchant>();
	private List<Sport> mSports;
	private Sport mSport;

	private List<LatLng> latLngs = new ArrayList<LatLng>();
	private ListView mListView;
	private ImageView vImg_down;
	private ImageView vBtn_location;
	private TextView vSport_menu;
	private ProgressDialog dialog;
	private boolean isShow;

	private void init() {
		dialog = new ProgressDialog(this);
		dialog.setMessage("加载中...");
		mListView = (ListView) findViewById(R.id.listView);
		vImg_down = (ImageView) findViewById(R.id.img_down);
		vSport_menu = (TextView) findViewById(R.id.sport_menu);
		mSports = BookActivity.mSports;
		if (mSports != null && mSports.size() > 0) {
			mSport = mSports.get(0);
			SportMenuAdapter mAdapter = new SportMenuAdapter(mSports);
			mListView.setAdapter(mAdapter);
			vSport_menu.setText(mSport.getSport_name());
			loadMerchants();
		}
		vImg_down.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShow) {
					mListView.setVisibility(View.GONE);
					isShow = false;
				} else {
					mListView.setVisibility(View.VISIBLE);
					isShow = true;
				}
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mSport = mSports.get(position);
				vSport_menu.setText(mSport.getSport_name());
				if (isShow) {
					mListView.setVisibility(View.GONE);
					isShow = false;
				} else {
					mListView.setVisibility(View.VISIBLE);
					isShow = true;
				}
				loadMerchants();
			}
		});

		if (aMap == null) {
			aMap = mapView.getMap();
			mUiSettings = aMap.getUiSettings();
			// 自定义系统定位小蓝点
			MyLocationStyle myLocationStyle = new MyLocationStyle();
			myLocationStyle.myLocationIcon(BitmapDescriptorFactory
					.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
			myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
			myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
			myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
			aMap.setMyLocationStyle(myLocationStyle);
			aMap.setMyLocationRotateAngle(180);
			aMap.setLocationSource(this);// 设置定位监听
			mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
			aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
			aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
			mUiSettings.setTiltGesturesEnabled(false);// 设置地图是否可以倾斜
			mUiSettings.setScaleControlsEnabled(true);// 设置地图默认的比例尺是否显示
			mUiSettings.setZoomControlsEnabled(false);

			setUpMap();
		}
	}

	// 地球上计算任意两点的距离
	public void myDistance() {
		LatLng lng1 = new LatLng(113.93317937850906, 22.537005849692);
		LatLng lng2 = new LatLng(113.945971529761, 22.553514105803135);
		float f = AMapUtils.calculateLineDistance(lng2, lng1);
		String s2 = (f / 1000) + "";
	}

	private void setUpMap() {
		aMap.setOnMapClickListener(this);
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		vBtn_location.setOnClickListener(this);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			dialog.dismiss();
			// if (msg.what == 1) {
			// List<Merchant> merchants = (List<Merchant>) msg.obj;
			// if (merchants.size() > 0) {
			// addMarkersToMap(merchants);
			// }
			// } else if (msg.what == -1) {
			//
			// }
			setdata(mMerchants, mSport.getSport_name());
			addMarkersToMap(mMerchants);
		};
	};

	private void setdata(List<Merchant> list, String name) {
		Merchant merchant = null;
		merchant = new Merchant();
		merchant.setLon(113.92550495351794);
		merchant.setLat(22.547483268260606);
		merchant.setAddress("南山区玉泉路");
		merchant.setM_name(name);
		list.add(merchant);

		merchant = new Merchant();
		merchant.setLon(113.92649200643542);
		merchant.setLat(22.547800347769776);
		merchant.setAddress("南山区南头大山路");
		merchant.setM_name(name);
		list.add(merchant);

		merchant = new Merchant();
		merchant.setLon(113.92733958448413);
		merchant.setLat(22.548062928686885);
		merchant.setAddress("南山区艺园路");
		merchant.setM_name(name);
		list.add(merchant);

		merchant = new Merchant();
		merchant.setLon(113.92814961160663);
		merchant.setLat(22.546784699896453);
		merchant.setAddress("南山区艺园路54号");
		merchant.setM_name(name);
		list.add(merchant);

		merchant = new Merchant();
		merchant.setLon(113.9291527577782);
		merchant.setLat(22.545387552559802);
		merchant.setAddress("南山区宝泉路口");
		merchant.setM_name(name);
		list.add(merchant);
	}

	// 加载运动项目
	private void loadMerchants() {
		dialog.show();
		UURemoteApi.loadSports("", "", "", new AsyncHttpResponseHandler() {

			Message msg = Message.obtain();

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String result = new String(arg2);
				try {
					List<Merchant> mList = Merchant.parseList(result);
					msg.what = 1;
					msg.obj = mList;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
				}
				mHandler.sendMessage(msg);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				msg.what = -1;
				mHandler.sendMessage(msg);
			}
		});
	}

	/**
	 * 往地图上添加mrkers
	 */
	private void addMarkersToMap(List<Merchant> list) {
		ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
		MarkerOptions options = null;
		for (int i = 0; i < list.size(); i++) {
			options = new MarkerOptions();
			Merchant mer = list.get(i);

			latLngs.add(new LatLng(mer.getLat(), mer.getLon()));

			options.title(mer.getM_name());
			options.snippet(mer.getAddress());
			options.anchor(0.5f, 0.5f);
			options.position(new LatLng(mer.getLat(), mer.getLon()));
			if (mSport.getSport_id() == 19) {
				options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.ic_marker_bad)));
			} else if (mSport.getSport_id() == 20) {
				options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.ic_marker_tennis)));
			} else if (mSport.getSport_id() == 21) {
				options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.ic_marker_basketball)));
			} else if (mSport.getSport_id() == 22) {
				options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.ic_marker_football)));
			} else if (mSport.getSport_id() == 23) {
				options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.ic_marker_fitness)));
			} else if (mSport.getSport_id() == 24) {
				options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.ic_marker_swimming)));
			} else if (mSport.getSport_id() == 25) {
				options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.ic_marker_yoga)));
			} else {
			}
			options.perspective(true);
			options.draggable(true);
			options.period(50);
			options.setFlat(true);
			markerOptionlst.add(options);
		}
		aMap.addMarkers(markerOptionlst, true);
	}

	/**
	 * 对marker标注点点击响应事件
	 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		HelperUi.showToastShort(MapLocationActivity2.this,
				"你点击的是" + marker.getTitle());
		mMarker = marker;
		return false;
	}

	/**
	 * 监听点击infowindow窗口事件回调
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {
		HelperUi.showToastShort(MapLocationActivity2.this, "你点击了infoWindow窗口"
				+ marker.getTitle());
		HelperUi.showToastShort(MapLocationActivity2.this, "当前地图可视区域内Marker数量:"
				+ aMap.getMapScreenMarkers().size());
		if (marker.isInfoWindowShown()) {
			marker.hideInfoWindow();
		}
	}

	/**
	 * 监听amap地图加载成功事件回调
	 */
	@Override
	public void onMapLoaded() {
		// LatLngBounds bounds = new LatLngBounds.Builder()
		// .include(Constants.XIAN).include(Constants.CHENGDU)
		// .include(latlng).build();
		// aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
	}

	@Override
	public View getInfoContents(Marker marker) {
		View infoContent = getLayoutInflater().inflate(
				R.layout.custom_info_window, null);
		render(marker, infoContent);
		return infoContent;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = getLayoutInflater().inflate(
				R.layout.custom_info_window, null);

		render(marker, infoWindow);
		return infoWindow;
	}

	@Override
	public void onMapClick(LatLng latLng) {
		if (mMarker != null) {
			mMarker.hideInfoWindow();
		}
	}

	/**
	 * 自定义infowinfow窗口
	 */
	public void render(Marker marker, View view) {
		((ImageView) view.findViewById(R.id.badge))
				.setImageResource(R.drawable.ic_distance);
		ImageView imageView = (ImageView) view.findViewById(R.id.badge);
		imageView.setImageResource(R.drawable.ic_distance);
		String title = marker.getTitle();
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		if (title != null) {
			SpannableString titleText = new SpannableString(title);
			titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
					titleText.length(), 0);
			titleUi.setTextSize(15);
			titleUi.setText(titleText);

		} else {
			titleUi.setText("");
		}
		String snippet = marker.getSnippet();
		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
		if (snippet != null) {
			SpannableString snippetText = new SpannableString(snippet);
			snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
					snippetText.length(), 0);
			snippetUi.setTextSize(20);
			snippetUi.setText(snippetText);
		} else {
			snippetUi.setText("");
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isShow) {
				mListView.setVisibility(View.GONE);
				isShow = false;
			} else {
				finish();
			}
		}
		return false;
	}

	public void onbackOnclick(View view) {
		this.finish();
	}

	class SportMenuAdapter extends BaseAdapter {

		private List<Sport> mSports;
		private LayoutInflater mInflater;

		public SportMenuAdapter(List<Sport> list) {
			this.mSports = list;
			mInflater = LayoutInflater.from(MapLocationActivity2.this);
		}

		@Override
		public int getCount() {
			return mSports.size();
		}

		@Override
		public Object getItem(int position) {
			return mSports.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView text = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_map_menu, null);
			}
			text = (TextView) convertView.findViewById(R.id.menuitem);
			text.setText(mSports.get(position).getSport_name());
			text.setTag(mSports.get(position).getSport_id());
			return convertView;
		}
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (aMapManager == null) {
			aMapManager = LocationManagerProxy.getInstance(this);
			/*
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true
			 */
			// Location API定位采用GPS和网络混合定位方式，时间最短是2000毫秒
			aMapManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	@Override
	public void deactivate() {
		mListener = null;
		if (aMapManager != null) {
			aMapManager.removeUpdates(this);
			aMapManager.destory();
		}
		aMapManager = null;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (aLocation != null) {
			this.aLocation = aLocation;
			if (mListener != null)
				mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			if (isFirst) {
				isFirst = false;
				aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(
						aLocation.getLatitude(), aLocation.getLongitude())));
				CameraUpdateFactory.zoomTo(16);
				// MarkerOptions markerOption = new MarkerOptions();
				// markerOption.position(new LatLng(aLocation.getLatitude(),
				// aLocation.getLongitude()));
				// markerOption.title("深圳市").snippet("深圳市：34.341568, 108.940174");
				// markerOption.draggable(true);
				// Marker marker = aMap.addMarker(markerOption);
				// marker.setObject("11");// 这里可以存储用户数据
			}
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.id_btn_location:
			if (aLocation != null) {
				aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(
						aLocation.getLatitude(), aLocation.getLongitude())));
			}
			break;
		default:
			break;
		}
	}
}
