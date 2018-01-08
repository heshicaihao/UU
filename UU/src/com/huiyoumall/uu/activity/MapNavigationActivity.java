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
 * ����λ�� ��ͼ ����
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

	// ��ͼ�͵�����Դ
	private MapView mMapView;
	private AMap mAMap;
	private AMapNavi mAMapNavi;

	// ����յ�����
	private NaviLatLng mNaviStart = new NaviLatLng(22.554132184649873,
			113.95393519708274);
	private NaviLatLng mNaviEnd = new NaviLatLng(22.56233588758577,
			113.95638147844262);
	// ����յ��б�
	private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
	private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
	// �滮��·
	private RouteOverLay mRouteOverLay;
	// ���е���·�߹滮�Ƿ�ɹ�
	private boolean mIsFoot = false;
	// �ݳ�����·���Ƿ�滮�ɹ�
	private boolean mIsDrive = false;
	private boolean mIsCalculateRouteSuccess = false;
	// Ĭ�϶�λ�ڳ���
	private LatLng merchIp;// �ɶ��о�γ��
	private Marker locationMarker; // ѡ��ĵ�

	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		vRight_Btn = (TextView) findViewById(R.id.right_btn);

		vTitle_view = (RelativeLayout) findViewById(R.id.title_view);
	}

	public void initView(Bundle savedInstanceState) {

		vTitle.setText("����λ��");
		vRight_Btn.setText("����");
		vRight_Btn.setOnClickListener(this);

		mAMapNavi = AMapNavi.getInstance(this);
		mAMapNavi.setAMapNaviListener(this);
		// ��ʼ������ģ��
		TTSController ttsManager = TTSController.getInstance(this);
		ttsManager.init();
		mAMapNavi.setAMapNaviListener(ttsManager);
		// AMapUtils.calculateLineDistance(arg0, arg1)//��������֮��ľ���
		// ��ʼ������ģ��
		mStartPoints.add(mNaviStart);
		mEndPoints.add(mNaviEnd);
		// ��ʼ����ͼ
		app = (AppContext) getApplicationContext();
		mMapView = (MapView) findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		mAMap = mMapView.getMap();
		mRouteOverLay = new RouteOverLay(mAMap, null);
		// ����markerСͼ��
		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
		// ��ȡ��γ��
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
		// Double.parseDouble(app.getLatitude()));// ��ȡ��ǰ��γ��
		// new NaviLatLng(Double.parseDouble(merchant.getLon()),
		// Double.parseDouble(merchant.getLat()));// ��ȡĿ�ĵؾ�γ��
		calculateFootRoute();// ����·�߹滮
		calculateDriveRoute();// �ݳ�·�߹滮

		mViewPopWindow = new ViewPopupWindow(this, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mViewPopWindow.setText("���е���", "�ݳ�����");
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
		// ɾ������
		AMapNavi.getInstance(this).removeAMapNaviListener(this);
		// ��������˳�ҳ���������ٵ����Ͳ�����Դ
		AMapNavi.getInstance(this).destroy();// ���ٵ���
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
		HelperUi.showToastShort(this, "·���滮����" + arg0);
		mIsCalculateRouteSuccess = false;
	}

	@Override
	public void onCalculateRouteSuccess() {
		AMapNaviPath naviPath = mAMapNavi.getNaviPath();
		if (naviPath == null) {
			return;
		}
		// ��ȡ·���滮��·����ʾ����ͼ��
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

	// ����ݳ�·��
	private void calculateDriveRoute() {
		boolean isSuccess = mAMapNavi.calculateDriveRoute(mStartPoints,
				mEndPoints, null, AMapNavi.DrivingDefault);
		if (isSuccess) {
			mIsDrive = true;
		}
		if (!isSuccess) {
			HelperUi.showToastShort(this, "·�߼���ʧ��,���������");
		}
	}

	// ���㲽��·��
	private void calculateFootRoute() {
		boolean isSuccess = mAMapNavi.calculateWalkRoute(mNaviStart, mNaviEnd);
		if (isSuccess) {
			mIsFoot = true;
		}
		if (!isSuccess) {
			HelperUi.showToastShort(this, "·�߼���ʧ��,���������");
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
	 * �Զ���infowinfow����
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
		// ����·�߹滮
		if (mIsFoot && mIsCalculateRouteSuccess) {
			startGPSNavi();
		} else {
			HelperUi.showToastShort(MapNavigationActivity.this,
					"���Ƚ������Ӧ��·���滮���ٽ��е���");
		}
	}

	@Override
	public void onRightClickListener() {
		// �ݳ�����
		if (mIsDrive && mIsCalculateRouteSuccess) {
			startGPSNavi();
		} else {
			HelperUi.showToastShort(MapNavigationActivity.this,
					"���Ƚ������Ӧ��·���滮���ٽ��е���");
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
