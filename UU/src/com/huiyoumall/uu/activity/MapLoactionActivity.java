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
 * ��ҳ ��ͼ ����
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
		mapView.onCreate(savedInstanceState);// �˷���������д
		initMap();
	}

	private Spinner vSpinner;
	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private PoiSearch.Query query;// Poi��ѯ������
	private LatLonPoint lp;// Ĭ�������㳡
	private PoiSearch poiSearch;
	private Marker detailMarker;// ��ʾMarker������
	private PoiOverlay poiOverlay;// poiͼ��
	private PoiResult poiResult; // poi���صĽ��
	private List<PoiItem> poiItems;// poi����
	private String[] itemCourt = { "��ë��", "����", "����", "��Ӿ��", "ƹ�����" };
	private ProgressDialog progDialog = null;// ����ʱ������
	private String deepType = "";// poi��������
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
	 * ����һЩamap������
	 */
	private void setUpMap() {
		// �Զ���ϵͳ��λС����
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// ����С�����ͼ��
		myLocationStyle.strokeColor(Color.BLACK);// ����Բ�εı߿���ɫ
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// ����Բ�ε������ɫ
		// myLocationStyle.anchor(int,int)//����С�����ê��
		myLocationStyle.strokeWidth(0.1f);// ����Բ�εı߿��ϸ
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// ���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
		// ���ö�λ������Ϊ��λģʽ �������ɶ�λ��������ͼ������������ת����
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);// ��λģʽΪ����
		aMap.setOnMarkerClickListener(this);// ��ӵ��marker�����¼�
		aMap.setInfoWindowAdapter(this);
		// ��������֮��ľ���
		// AMapUtils.calculateLineDistance(arg0, arg1);

	}

	/**
	 * ����ѡ������
	 */
	private void setSelectType() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, itemCourt);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		vSpinner.setAdapter(adapter);
		vSpinner.setOnItemSelectedListener(this);// ���spinnerѡ�������¼�
	}

	/**
	 * ���ؽ��ȿ�
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * ��ʼ����poi����
	 */
	private void doSearchQuery() {
		getProgressDialog("����������");
		// aMap.setOnMapClickListener(null);// ����poi����ʱ�������ͼ����¼�
		AMapLocation location = mAMapLocationManager
				.getLastKnownLocation(LocationProviderProxy.AMapNetwork);
		query = new PoiSearch.Query(deepType, "", location.getCityCode());// ������ǰλ�õ�
		query.setLimitDiscount(false);
		query.setLimitGroupbuy(false);
		lp = new LatLonPoint(location.getLatitude(), location.getLongitude());// Ĭ�ϵ�ǰ����λ��
		if (lp != null) {
			poiSearch = new PoiSearch(this, query);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.setBound(new SearchBound(lp, 2000, true));//
			// ������������Ϊ��lp��ΪԲ�ģ�����Χ2000�׷�Χ
			/*
			 * List<LatLonPoint> list = new ArrayList<LatLonPoint>();
			 * list.add(lp);
			 * list.add(AMapUtil.convertToLatLonPoint(Constants.BEIJING));
			 * poiSearch.setBound(new SearchBound(list));// ���ö����poi������Χ
			 */
			poiSearch.searchPOIAsyn();// �첽����
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
				mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
			} else {
				Log.e("AmapErr", "Location ERR:"
						+ amapLocation.getAMapException().getErrorCode());
				Log.e("AmapErr", "Location ERR:"
						+ amapLocation.getAMapException().getErrorMessage());
			}
		}

	}

	/**
	 * ���λ
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
			// ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����removeUpdates()������ȡ����λ����
			// �ڶ�λ�������ں��ʵ��������ڵ���destroy()����
			// ����������ʱ��Ϊ-1����λֻ��һ��
			// �ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������removeUpdates()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 60 * 1000, 10, this);
		}
	}

	/**
	 * ֹͣ��λ
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
	 * �鵥��poi����
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
		doSearchQuery();// �ı���������������������
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		if (parent == vSpinner) {
			deepType = itemCourt[0];
		}
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail result, int rCode) {
		dissmissProgressDialog();// ���ضԻ���
		if (rCode == 0) {
			if (result != null) {// ����poi�Ľ��
				if (detailMarker != null) {
					StringBuffer sb = new StringBuffer(result.getSnippet());
					if ((result.getGroupbuys() != null && result.getGroupbuys()
							.size() > 0)
							|| (result.getDiscounts() != null && result
									.getDiscounts().size() > 0)) {

						if (result.getGroupbuys() != null
								&& result.getGroupbuys().size() > 0) {// ȡ��һ���Ź���Ϣ
							sb.append("\n�Ź���"
									+ result.getGroupbuys().get(0).getDetail());
						}
						if (result.getDiscounts() != null
								&& result.getDiscounts().size() > 0) {// ȡ��һ���Ż���Ϣ
							sb.append("\n�Żݣ�"
									+ result.getDiscounts().get(0).getDetail());
						}
					} else {

						sb = new StringBuffer("��ַ��" + result.getSnippet()
								+ "\n�绰��" + result.getTel() + "\n���ͣ�"
								+ result.getTypeDes());
					}
					// �ж�poi�����Ƿ��������Ϣ
					if (result.getDeepType() != null) {
						// ��������Ҫ�����Ϣ�����Բ鿴����Ĵ��룬���ڽ�����δ��ʾ��ش���
						// sb = getDeepInfo(result, sb);
						detailMarker.setSnippet(sb.toString());
					} else {
						Toast.makeText(MapLoactionActivity.this, "��Poi��û�������Ϣ",
								Toast.LENGTH_SHORT).show();
					}
				}

			} else {
				Toast.makeText(MapLoactionActivity.this, "�Բ���û��������������ݣ�",
						Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(MapLoactionActivity.this, "����ʧ��,�����������ӣ�",
					Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(MapLoactionActivity.this, "key��֤��Ч��",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(MapLoactionActivity.this, "δ֪�������Ժ�����!������Ϊ" + rCode,
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		dissmissProgressDialog();// ���ضԻ���
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// ����poi�Ľ��
				if (result.getQuery().equals(query)) {// �Ƿ���ͬһ��
					poiResult = result;
					poiItems = poiResult.getPois();// ȡ�õ�һҳ��poiitem���ݣ�ҳ��������0��ʼ
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// ����������poiitem����ʱ���᷵�غ��������ؼ��ֵĳ�����Ϣ
					if (poiItems != null && poiItems.size() > 0) {
						aMap.clear();// ����֮ǰ��ͼ��
						poiOverlay = new PoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();

						// nextButton.setClickable(true);// ������һҳ�ɵ�
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities);
					} else {
						// Toast.makeText(MapLoactionActivity.this,
						// "��Poi��û�������Ϣ",
						// Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Toast.makeText(MapLoactionActivity.this, "�Բ���û��������������ݣ�",
						Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(MapLoactionActivity.this, "����ʧ��,�����������ӣ�",
					Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(MapLoactionActivity.this, "key��֤��Ч��",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(MapLoactionActivity.this, "δ֪�������Ժ�����!������Ϊ" + rCode,
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * poiû�����������ݣ�����һЩ�Ƽ����е���Ϣ
	 */
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "�Ƽ�����\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "��������:" + cities.get(i).getCityName() + "��������:"
					+ cities.get(i).getCityCode() + "���б���:"
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
