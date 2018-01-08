package com.huiyoumall.uu.activity;

import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;

/**
 * 首页 地图 界面
 * 
 * @author ASUS
 * 
 */
public class MapLoactionActivity extends BaseActivity implements
		LocationSource, AMapLocationListener, InfoWindowAdapter,
		OnMarkerClickListener, OnItemSelectedListener, OnPoiSearchListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_location);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		initMap();
	}

	private Spinner vSpinner;
	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private PoiSearch.Query query;// Poi查询条件类
	private LatLonPoint lp;// 默认西单广场
	private PoiSearch poiSearch;
	private Marker detailMarker;// 显示Marker的详情
	private PoiOverlay poiOverlay;// poi图层
	private PoiResult poiResult; // poi返回的结果
	private List<PoiItem> poiItems;// poi数据
	private String[] itemCourt = { "羽毛球场", "足球场", "篮球场", "游泳馆", "乒乓球馆" };
	private ProgressDialog progDialog = null;// 搜索时进度条
	private String deepType = "";// poi搜索类型
	private double mLatitude, mLongitude;

	private void initMap() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
			setSelectType();
		}
	}

	@Override
	public void findViewById() {
		// vSpinner = (Spinner) findViewById(R.id.sport_menu);
	}

	@Override
	public void initView() {

	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);// 定位模式为跟随
		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		aMap.setInfoWindowAdapter(this);
		// 计算两点之间的距离
		// AMapUtils.calculateLineDistance(arg0, arg1);

	}

	/**
	 * 设置选择类型
	 */
	private void setSelectType() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, itemCourt);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		vSpinner.setAdapter(adapter);
		vSpinner.setOnItemSelectedListener(this);// 添加spinner选择框监听事件
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 开始进行poi搜索
	 */
	private void doSearchQuery() {
		getProgressDialog("正在搜索中");
		// aMap.setOnMapClickListener(null);// 进行poi搜索时清除掉地图点击事件
		AMapLocation location = mAMapLocationManager
				.getLastKnownLocation(LocationProviderProxy.AMapNetwork);
		query = new PoiSearch.Query(deepType, "", location.getCityCode());// 搜索当前位置的
		query.setLimitDiscount(false);
		query.setLimitGroupbuy(false);
		lp = new LatLonPoint(location.getLatitude(), location.getLongitude());// 默认当前所在位置
		if (lp != null) {
			poiSearch = new PoiSearch(this, query);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.setBound(new SearchBound(lp, 2000, true));//
			// 设置搜索区域为以lp点为圆心，其周围2000米范围
			/*
			 * List<LatLonPoint> list = new ArrayList<LatLonPoint>();
			 * list.add(lp);
			 * list.add(AMapUtil.convertToLatLonPoint(Constants.BEIJING));
			 * poiSearch.setBound(new SearchBound(list));// 设置多边形poi搜索范围
			 */
			poiSearch.searchPOIAsyn();// 异步搜索
		}
	}

	/**
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void activitybackview(View view) {
		super.activitybackview(view);
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
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			mLatitude = amapLocation.getLatitude();
			mLongitude = amapLocation.getLongitude();
			if (amapLocation != null
					&& amapLocation.getAMapException().getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
			} else {
				Log.e("AmapErr", "Location ERR:"
						+ amapLocation.getAMapException().getErrorCode());
				Log.e("AmapErr", "Location ERR:"
						+ amapLocation.getAMapException().getErrorMessage());
			}
		}

	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用destroy()方法
			// 其中如果间隔时间为-1，则定位只定一次
			// 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 60 * 1000, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (poiOverlay != null && poiItems != null && poiItems.size() > 0) {
			detailMarker = marker;
			doSearchPoiDetail(poiItems.get(poiOverlay.getPoiIndex(marker))
					.getPoiId());
		}
		return false;
	}

	/**
	 * 查单个poi详情
	 * 
	 * @param poiId
	 */
	public void doSearchPoiDetail(String poiId) {
		if (poiSearch != null && poiId != null) {
			poiSearch.searchPOIDetailAsyn(poiId);
		}
	}

	@Override
	public View getInfoContents(Marker arg0) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == vSpinner) {
			deepType = itemCourt[position];
		}
		doSearchQuery();// 改变搜索条件，需重新搜索
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		if (parent == vSpinner) {
			deepType = itemCourt[0];
		}
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail result, int rCode) {
		dissmissProgressDialog();// 隐藏对话框
		if (rCode == 0) {
			if (result != null) {// 搜索poi的结果
				if (detailMarker != null) {
					StringBuffer sb = new StringBuffer(result.getSnippet());
					if ((result.getGroupbuys() != null && result.getGroupbuys()
							.size() > 0)
							|| (result.getDiscounts() != null && result
									.getDiscounts().size() > 0)) {

						if (result.getGroupbuys() != null
								&& result.getGroupbuys().size() > 0) {// 取第一条团购信息
							sb.append("\n团购："
									+ result.getGroupbuys().get(0).getDetail());
						}
						if (result.getDiscounts() != null
								&& result.getDiscounts().size() > 0) {// 取第一条优惠信息
							sb.append("\n优惠："
									+ result.getDiscounts().get(0).getDetail());
						}
					} else {

						sb = new StringBuffer("地址：" + result.getSnippet()
								+ "\n电话：" + result.getTel() + "\n类型："
								+ result.getTypeDes());
					}
					// 判断poi搜索是否有深度信息
					if (result.getDeepType() != null) {
						// 如果大家需要深度信息，可以查看下面的代码，现在界面上未显示相关代码
						// sb = getDeepInfo(result, sb);
						detailMarker.setSnippet(sb.toString());
					} else {
						Toast.makeText(MapLoactionActivity.this, "此Poi点没有深度信息",
								Toast.LENGTH_SHORT).show();
					}
				}

			} else {
				Toast.makeText(MapLoactionActivity.this, "对不起，没有搜索到相关数据！",
						Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(MapLoactionActivity.this, "搜索失败,请检查网络连接！",
					Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(MapLoactionActivity.this, "key验证无效！",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(MapLoactionActivity.this, "未知错误，请稍后重试!错误码为" + rCode,
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		dissmissProgressDialog();// 隐藏对话框
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					poiResult = result;
					poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
					if (poiItems != null && poiItems.size() > 0) {
						aMap.clear();// 清理之前的图标
						poiOverlay = new PoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();

						// nextButton.setClickable(true);// 设置下一页可点
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities);
					} else {
						// Toast.makeText(MapLoactionActivity.this,
						// "此Poi点没有深度信息",
						// Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Toast.makeText(MapLoactionActivity.this, "对不起，没有搜索到相关数据！",
						Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(MapLoactionActivity.this, "搜索失败,请检查网络连接！",
					Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(MapLoactionActivity.this, "key验证无效！",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(MapLoactionActivity.this, "未知错误，请稍后重试!错误码为" + rCode,
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * poi没有搜索到数据，返回一些推荐城市的信息
	 */
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "推荐城市\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
					+ cities.get(i).getCityCode() + "城市编码:"
					+ cities.get(i).getAdCode() + "\n";
		}
		Toast.makeText(MapLoactionActivity.this, infomation, Toast.LENGTH_SHORT)
				.show();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
