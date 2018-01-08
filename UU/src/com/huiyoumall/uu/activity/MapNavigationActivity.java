package com.huiyoumall.uu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.common.TTSController;
import com.huiyoumall.uu.entity.Merchant;
import com.huiyoumall.uu.util.Util;
import com.huiyoumall.uu.widget.ViewPopupWindow;
import com.huiyoumall.uu.widget.ViewPopupWindow.onViewItemClickListener;

/**
 * 场馆位置 地图 界面
 * 
 * @author ASUS
 * 
 */
public class MapNavigationActivity extends Activity implements
		AMapNaviListener, InfoWindowAdapter, onViewItemClickListener,
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		merchant = (Merchant) getIntent().getExtras().getSerializable(
				MerchantDetailsActivity.M_DETAILS);
		merchant.setLon(22.553514105803135);
		merchant.setLat(113.945971529761);
		setContentView(R.layout.activity_map_naviroute);
		findViewById();
		initView(savedInstanceState);
	}

	private Merchant merchant;
	private AppContext app;
	private ViewPopupWindow mViewPopWindow;
	private RelativeLayout vTitle_view;
	private TextView vTitle, vRight_Btn;

	// 地图和导航资源
	private MapView mMapView;
	private AMap mAMap;
	private AMapNavi mAMapNavi;

	// 起点终点坐标
	private NaviLatLng mNaviStart = new NaviLatLng(22.554132184649873,
			113.95393519708274);
	private NaviLatLng mNaviEnd = new NaviLatLng(22.56233588758577,
			113.95638147844262);
	// 起点终点列表
	private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
	private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
	// 规划线路
	private RouteOverLay mRouteOverLay;
	// 步行导航路线规划是否成功
	private boolean mIsFoot = false;
	// 驾车导航路线是否规划成功
	private boolean mIsDrive = false;
	private boolean mIsCalculateRouteSuccess = false;
	// 默认定位在场馆
	private LatLng merchIp;// 成都市经纬度
	private Marker locationMarker; // 选择的点

	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		vRight_Btn = (TextView) findViewById(R.id.right_btn);

		vTitle_view = (RelativeLayout) findViewById(R.id.title_view);
	}

	public void initView(Bundle savedInstanceState) {

		vTitle.setText("场馆位置");
		vRight_Btn.setText("导航");
		vRight_Btn.setOnClickListener(this);

		mAMapNavi = AMapNavi.getInstance(this);
		mAMapNavi.setAMapNaviListener(this);
		// 初始化语音模块
		TTSController ttsManager = TTSController.getInstance(this);
		ttsManager.init();
		mAMapNavi.setAMapNaviListener(ttsManager);
		// AMapUtils.calculateLineDistance(arg0, arg1)//计算两点之间的距离
		// 初始化语音模块
		mStartPoints.add(mNaviStart);
		mEndPoints.add(mNaviEnd);
		// 初始化地图
		app = (AppContext) getApplicationContext();
		mMapView = (MapView) findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		mAMap = mMapView.getMap();
		mRouteOverLay = new RouteOverLay(mAMap, null);
		// 设置marker小图标
		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
		// 获取经纬度
		merchIp = new LatLng(merchant.getLon(), merchant.getLat());
		MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 0.5f)
				.position(merchIp).title(merchant.getM_name())
				.snippet(merchant.getAddress()).icons(giflist)
				.perspective(true).draggable(true).period(50);
		ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
		markerOptionlst.add(markerOption);
		List<Marker> markerlst = mAMap.addMarkers(markerOptionlst, true);
		locationMarker = markerlst.get(0);
		locationMarker.showInfoWindow();

		// new NaviLatLng(Double.parseDouble(app.getLongitude()),
		// Double.parseDouble(app.getLatitude()));// 获取当前经纬度
		// new NaviLatLng(Double.parseDouble(merchant.getLon()),
		// Double.parseDouble(merchant.getLat()));// 获取目的地经纬度
		calculateFootRoute();// 步行路线规划
		calculateDriveRoute();// 驾车路线规划

		mViewPopWindow = new ViewPopupWindow(this, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mViewPopWindow.setText("步行导航", "驾车导航");
		mViewPopWindow.setonViewItemClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		// 删除监听
		AMapNavi.getInstance(this).removeAMapNaviListener(this);
		// 这是最后退出页，所以销毁导航和播报资源
		AMapNavi.getInstance(this).destroy();// 销毁导航
		TTSController.getInstance(this).stopSpeaking();
		TTSController.getInstance(this).destroy();

	}

	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		HelperUi.showToastShort(this, "路径规划出错" + arg0);
		mIsCalculateRouteSuccess = false;
	}

	@Override
	public void onCalculateRouteSuccess() {
		AMapNaviPath naviPath = mAMapNavi.getNaviPath();
		if (naviPath == null) {
			return;
		}
		// 获取路径规划线路，显示到地图上
		mRouteOverLay.setRouteInfo(naviPath);
		mRouteOverLay.addToMap();
		mIsCalculateRouteSuccess = true;
	}

	@Override
	public void onEndEmulatorNavi() {

	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {

	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {

	}

	@Override
	public void onInitNaviFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviInfoUpdate(NaviInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub

	}

	// 计算驾车路线
	private void calculateDriveRoute() {
		boolean isSuccess = mAMapNavi.calculateDriveRoute(mStartPoints,
				mEndPoints, null, AMapNavi.DrivingDefault);
		if (isSuccess) {
			mIsDrive = true;
		}
		if (!isSuccess) {
			HelperUi.showToastShort(this, "路线计算失败,检查参数情况");
		}
	}

	// 计算步行路线
	private void calculateFootRoute() {
		boolean isSuccess = mAMapNavi.calculateWalkRoute(mNaviStart, mNaviEnd);
		if (isSuccess) {
			mIsFoot = true;
		}
		if (!isSuccess) {
			HelperUi.showToastShort(this, "路线计算失败,检查参数情况");
		}
	}

	private void startGPSNavi() {
		Intent gpsIntent = new Intent(MapNavigationActivity.this,
				UUNaviActivity.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean(Util.ISEMULATOR, false);
		bundle.putInt(Util.ACTIVITYINDEX, Util.SIMPLEROUTENAVI);
		gpsIntent.putExtras(bundle);
		gpsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(gpsIntent);
	}

	@Override
	public View getInfoContents(Marker marker) {
		View infoWindow = getLayoutInflater().inflate(
				R.layout.custom_info_window, null);

		render(marker, infoWindow);
		return infoWindow;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = getLayoutInflater().inflate(
				R.layout.custom_info_window, null);

		render(marker, infoWindow);
		return infoWindow;
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void activitybackview(View view) {
		this.finish();
	}

	@Override
	public void onLeftClickListener() {
		// 步行路线规划
		if (mIsFoot && mIsCalculateRouteSuccess) {
			startGPSNavi();
		} else {
			HelperUi.showToastShort(MapNavigationActivity.this,
					"请先进行相对应的路径规划，再进行导航");
		}
	}

	@Override
	public void onRightClickListener() {
		// 驾车导航
		if (mIsDrive && mIsCalculateRouteSuccess) {
			startGPSNavi();
		} else {
			HelperUi.showToastShort(MapNavigationActivity.this,
					"请先进行相对应的路径规划，再进行导航");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_btn:
			// int height1 = vTitle_view.getHeight()
			// + AppConfig.getStatusBarHeight(MapNavigationActivity.this);
			// mViewPopWindow.showAtLocation(vTitle_view, Gravity.TOP, 0,
			// height1);
			HelperUi.startActivity(MapNavigationActivity.this,
					UUNaviActivity.class);
			break;

		default:
			break;
		}
	}
}
