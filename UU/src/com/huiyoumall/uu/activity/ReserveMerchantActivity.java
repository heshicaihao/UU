package com.huiyoumall.uu.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.adapter.SelectVenueOrderGridAdapter;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.entity.Order;
import com.huiyoumall.uu.entity.Order.TDeatil;
import com.huiyoumall.uu.entity.Reserve;
import com.huiyoumall.uu.remote.NetWorkHelper;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.util.StringUtils;
import com.huiyoumall.uu.widget.CustomScrollView;
import com.huiyoumall.uu.widget.EmptyLayout;
import com.huiyoumall.uu.widget.HScrollView;
import com.huiyoumall.uu.widget.HorizontalListView;
import com.huiyoumall.uu.widget.OnScrollChangedListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 场次选择 界面
 * 
 * @author ASUS
 * 
 */
public class ReserveMerchantActivity extends BaseActivity implements
		OnScrollChangedListener, OnClickListener {

	public static final String MER_ID = "m_id";
	public static final String MER_NAME = "m_name";

	public static final String SELECT_DATE = "sele_date";
	public static final String SELECT_PRICE = "sele_price";

	public static final String SPORT_ID = "sport_id";
	public static final String SPORT_NAME = "sprot_name";
	public static final Map<String, String> time = new HashMap<String, String>();
	public static final Map<String, String> timeField = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mId = getIntent().getExtras().getString(MER_ID);
		mName = getIntent().getExtras().getString(MER_NAME);
		app = (AppContext) getApplicationContext();
		setContentView(R.layout.activity_reserve_merchant);
	}

	private CustomScrollView view = null;
	private HorizontalListView vSelectDateview;
	private HScrollView vHorizon;
	// private LinearLayout vHscoll_lin;
	private LinearLayout vPrice_horizon;
	private LinearLayout view_content;
	private ScrollView vScroll;
	private LinearLayout vVscorll_lin;
	private LinearLayout vBtn_pay;
	private EmptyLayout vEmpter_view;
	private AppContext app;

	private GridView vOrder_gridView;
	// private GridView vEnue_price_grid;
	// private VenuePriceAdapter mPriceAdapter;

	private TextView order_price;
	private TextView vTitle, vRight_Btn;
	private String mPrice = 0 + "";
	private String mId;
	private String mName;
	private Reserve mReserve;
	private String[] mSite;
	private String mTime;// 场馆开业时间 8:00-9:00
	private String mSelectDate;
	private String mSelectWeek;
	private int isSelectDateId = 0;
	private List<List<TDeatil>> mDetails;
	private SelectDateAdaper selectDateAdaper;

	private LinearLayout hide_lin;
	private SelectVenueOrderGridAdapter mOrderAdapter;
	private List<TDeatil> pricesOrder = Collections
			.synchronizedList(new LinkedList<TDeatil>());

	// 场馆预订时间点
	private String[] vsiteTimes = null;
	// 场地编号
	private List<SiteNum> mSiteNums;

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		vRight_Btn = (TextView) findViewById(R.id.right_btn);

		vSelectDateview = (HorizontalListView) findViewById(R.id.horizn_date_list);
		view_content = (LinearLayout) findViewById(R.id.view_content);
		view = (CustomScrollView) findViewById(R.id.view_scoll);
		vHorizon = (HScrollView) findViewById(R.id.horizontal);
		// vHscoll_lin = (LinearLayout) findViewById(R.id.hscoll_lin);
		vOrder_gridView = (GridView) findViewById(R.id.order_view);
		// vEnue_price_grid = (GridView) findViewById(R.id.venue_price_grid);
		vScroll = (ScrollView) findViewById(R.id.vscroll);
		vVscorll_lin = (LinearLayout) findViewById(R.id.vscorll_lin);
		vPrice_horizon = (LinearLayout) findViewById(R.id.price_horizon);
		hide_lin = (LinearLayout) findViewById(R.id.hide_lin);
		order_price = (TextView) findViewById(R.id.order_price);
		vBtn_pay = (LinearLayout) findViewById(R.id.btn_pay);
		vEmpter_view = (EmptyLayout) findViewById(R.id.empter_view);
		view.setListener(this);
		vBtn_pay.setOnClickListener(this);
	}

	@Override
	public void initView() {
		time.put("field_0", "8:00-9:00");
		time.put("field_1", "9:00-10:00");
		time.put("field_2", "10:00-11:00");
		time.put("field_3", "11:00-12:00");
		time.put("field_4", "12:00-13:00");
		time.put("field_5", "13:00-14:00");
		time.put("field_6", "14:00-15:00");
		time.put("field_7", "15:00-16:00");
		time.put("field_8", "16:00-17:00");
		time.put("field_9", "17:00-18:00");
		time.put("field_10", "18:00-19:00");
		time.put("field_11", "19:00-20:00");
		time.put("field_12", "20:00-21:00");
		time.put("field_13", "21:00-22:00");
		time.put("field_14", "22:00-23:00");
		time.put("field_15", "23:00-0:00");

		timeField.put("8:00-9:00", "field_0");
		timeField.put("9:00-10:00", "field_1");
		timeField.put("10:00-11:00", "field_2");
		timeField.put("11:00-12:00", "field_3");
		timeField.put("12:00-13:00", "field_4");
		timeField.put("13:00-14:00", "field_5");
		timeField.put("14:00-15:00", "field_6");
		timeField.put("15:00-16:00", "field_7");
		timeField.put("16:00-17:00", "field_8");
		timeField.put("17:00-18:00", "field_9");
		timeField.put("18:00-19:00", "field_10");
		timeField.put("19:00-20:00", "field_11");
		timeField.put("20:00-21:00", "field_12");
		timeField.put("21:00-22:00", "field_13");
		timeField.put("22:00-23:00", "field_14");
		timeField.put("23:00-0:00", "field_15");

		mSelectDate = StringUtils.getData().get(0).getDate2();
		mSelectWeek = "今日";
		vTitle.setText("场次选择");
		vRight_Btn.setText("订单");
		selectWeekDate();
		mOrderAdapter = new SelectVenueOrderGridAdapter(this, pricesOrder);
		vOrder_gridView.setAdapter(mOrderAdapter);
		vRight_Btn.setOnClickListener(this);
		loadReserve();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				mReserve = (Reserve) msg.obj;
			} else if (msg.what == -1) {
				return;
			}
			if (mReserve != null) {
				vEmpter_view.setVisibility(View.GONE);
				view_content.setVisibility(View.VISIBLE);
				mSite = mReserve.getSite();
				mTime = mReserve.getTime_quantum();
				mDetails = mReserve.getDetail();
				// 数据绑定到界面
				setData();
			}
		};
	};

	private void loadReserve() {
		vEmpter_view.setVisibility(View.VISIBLE);
		view_content.setVisibility(View.GONE);
		if (!NetWorkHelper.isNetworkAvailable(this)) {
			vEmpter_view.setErrorType(EmptyLayout.NETWORK_ERROR);
		} else {
			vEmpter_view.setErrorType(EmptyLayout.NETWORK_LOADING);
			final Message msg = Message.obtain();
			UURemoteApi.loadReserve("38", mSelectDate,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							try {
								String result = new String(arg2, "UTF-8");
								Reserve res = Reserve.parseEntity(result);
								msg.what = 1;
								msg.obj = res;
							} catch (UnsupportedEncodingException e) {
								msg.what = -1;
								e.printStackTrace();
							} catch (Exception e) {
								msg.what = -1;
								e.printStackTrace();
							}
							mHandler.sendMessage(msg);
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							msg.what = -1;
							vEmpter_view
									.setErrorType(EmptyLayout.NETWORK_ERROR);
							mHandler.sendMessage(msg);
						}
					});
		}
	}

	private void setData() {
		// 如果已经选择日期则先移除控件的view
		vHorizon.removeAllViews();
		vVscorll_lin.removeAllViews();
		vPrice_horizon.removeAllViews();
		if (pricesOrder.size() != 0) {
			pricesOrder.clear();
			mOrderAdapter.setDatas(pricesOrder);
			hide_lin.setVisibility(View.VISIBLE);
			vOrder_gridView.setVisibility(View.GONE);
		}
		if (mSite != null) {
			selectSite();
		}
		selectTime();
		if (mDetails != null) {
			selectPrice();
		}
	}

	// 处理可选择时间
	private void selectWeekDate() {
		if (StringUtils.getData() != null) {
			selectDateAdaper = new SelectDateAdaper();
			vSelectDateview.setAdapter(selectDateAdaper);
		}
		vSelectDateview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				VenueWeekdate date = (VenueWeekdate) view.getTag();
				isSelectDateId = date.getId();
				selectDateAdaper.notifyDataSetChanged();
				mSelectDate = date.getDate2();
				mSelectWeek = date.getToday();
				order_price.setText("选择场地");
				mPrice = 0 + "";
				loadReserve();
			}
		});
	}

	// 场次号
	private void selectSite() {
		LinearLayout hLayout = new LinearLayout(this);
		hLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		params.setMargins(1, 0, 0, 0);
		hLayout.setLayoutParams(params);
		hLayout.setVerticalGravity(Gravity.CENTER);
		TextView numText = null;
		mSiteNums = new ArrayList<SiteNum>();
		for (int i = 0; i < mSite.length; i++) {
			SiteNum sn = new SiteNum();
			sn.id = Integer.parseInt(mSite[i].split("-")[0]);
			sn.siteNum = mSite[i].split("-")[1];
			mSiteNums.add(sn);

			numText = new TextView(this);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			layoutParams.setMargins(1, 0, 0, 0);
			numText.setLayoutParams(layoutParams);
			numText.setPadding(10, 0, 10, 0);
			numText.setWidth(130);
			numText.setBackground(getResources().getDrawable(R.color.white));
			numText.setTextColor(getResources().getColor(
					R.color.text_home_color));
			numText.setTextSize(10.0f);
			numText.setGravity(Gravity.CENTER);
			numText.setText(mSite[i].split("-")[1]);
			hLayout.addView(numText);
		}
		vHorizon.addView(hLayout);
	}

	// 处理场地预订时间点
	private void selectTime() {
		if (!StringUtils.isEmpty(mTime)) {
			String startTime = mTime.split("-")[0];
			// String endTime = mTime.split("-")[1];
			if (startTime.equals("8:00")) {
				String[] time = { "8:00", "9:00", "10:00", "11:00", "12:00",
						"13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
						"19:00", "20:00", "21:00", "22:00", "23:00", "0:00" };
				vsiteTimes = time;
			} else if (startTime.equals("9:00")) {
				String[] time = { "9:00", "10:00", "11:00", "12:00", "13:00",
						"14:00", "15:00", "16:00", "17:00", "18:00", "19:00",
						"20:00", "21:00", "22:00", "23:00", "0:00" };
				vsiteTimes = time;
			}
		}

		TextView textTime = null;
		if (vsiteTimes != null) {
			for (int i = 0; i < vsiteTimes.length; i++) {

				textTime = new TextView(this);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(0, 0, 0, 1);
				textTime.setLayoutParams(layoutParams);
				textTime.setBackground(getResources()
						.getDrawable(R.color.white));
				textTime.setTextColor(getResources().getColor(
						R.color.text_home_color));
				textTime.setTextSize(12.0f);
				textTime.setWidth(130);
				textTime.setHeight(85);
				textTime.setGravity(Gravity.TOP | Gravity.RIGHT);
				textTime.setText(vsiteTimes[i]);
				vVscorll_lin.addView(textTime);
			}
		}
	}

	// 处理场地选择的价格
	private void selectPrice() {
		LinearLayout visLayout = null;
		if (mDetails.size() > 0) {
			for (int i = 0; i < mDetails.size(); i++) {
				visLayout = new LinearLayout(this);
				visLayout.setOrientation(LinearLayout.VERTICAL);
				visLayout.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.MATCH_PARENT));

				visLayout.setVerticalGravity(Gravity.CENTER);

				List<TDeatil> details = mDetails.get(i);
				if (details.size() != 0) {
					for (int j = 0; j < details.size(); j++) {
						TDeatil detail = details.get(j);
						final TextView text = new TextView(this);
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						layoutParams.setMargins(1, 1, 0, 0);
						text.setLayoutParams(layoutParams);
						if (detail.getIs_res() == 0) {// 不可预订
							text.setBackgroundColor(getResources().getColor(
									R.color.already_booked));
							text.setEnabled(false);
						} else {
							text.setBackground(getResources().getDrawable(
									R.drawable.price_item_bg_gree));
						}
						text.setTextColor(getResources()
								.getColor(R.color.white));
						text.setTextSize(14.0f);
						text.setPadding(10, 10, 10, 10);
						text.setWidth(130);
						text.setHeight(85);
						text.setGravity(Gravity.CENTER);
						text.setText("¥" + detail.getT_price());
						text.setTag(detail);
						text.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								TDeatil det = (TDeatil) v.getTag();
								if (det.getSelect() == 0) {
									if (pricesOrder.size() < 4) {
										pricesOrder.add(det);
										mPrice = (Double.parseDouble(det
												.getT_price()) + Double
												.parseDouble(mPrice))
												+ "";
										order_price.setText(mPrice + "元  提交订单");
										mOrderAdapter.setDatas(pricesOrder);
										det.setSelect(1);
										text.setBackground(getResources()
												.getDrawable(
														R.drawable.price_item_bg_blue));
									} else {
										Toast.makeText(
												ReserveMerchantActivity.this,
												"你选的场次太多了，请分两次下单",
												Toast.LENGTH_SHORT).show();
									}

								} else {
									text.setBackground(getResources()
											.getDrawable(
													R.drawable.price_item_bg_gree));
									det.setSelect(0);
									if (pricesOrder.contains(det)) {
										pricesOrder.remove(det);
										mPrice = (Double.parseDouble(mPrice) - Double
												.parseDouble(det.getT_price()))
												+ "";
										if (Double.parseDouble(mPrice) == 0) {
											order_price.setText("选择场地");
										} else {
											order_price.setText(mPrice
													+ "元  提交订单");
										}
										mOrderAdapter.setDatas(pricesOrder);
									}
								}
								if (pricesOrder.size() == 0) {
									hide_lin.setVisibility(View.VISIBLE);
									vOrder_gridView.setVisibility(View.GONE);
								} else {
									hide_lin.setVisibility(View.GONE);
									vOrder_gridView.setVisibility(View.VISIBLE);
								}

								// Toast.makeText(ChooseCourtActivity.this,
								// ve.vEnueId, Toast.LENGTH_SHORT).show();
							}
						});
						visLayout.addView(text);
					}
				}
				vPrice_horizon.addView(visLayout);
			}
		}
	}

	@Override
	public void onScrollChanged(int x, int y, int oldx, int oldy) {
		vHorizon.scrollTo(x, x);
		vScroll.scrollTo(x, y);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_pay:
			// Intent intent = new Intent(ReserveMerchantActivity.this,
			// OrderEnsureActivity.class);
			// intent.putExtra(SELECT_PRICE, (Serializable) pricesOrder);
			// intent.putExtra(SELECT_DATE, mSelectDate);
			// intent.putExtra(MER_ID, mId);
			// intent.putExtra(MER_NAME, mName);
			// intent.putExtra(SPORT_ID, "sprotid");
			// intent.putExtra(SPORT_NAME, "sprotname");
			// startActivity(intent);

			if (!app.isLogin) {
				HelperUi.startActivity(ReserveMerchantActivity.this,
						LoginActivity.class);
			} else {
				Bundle bundle = new Bundle();
				Order book = new Order();
				book.settDeatils(pricesOrder);
				book.setDate(mSelectDate);
				book.setMerchant_id(Integer.parseInt(mId));
				book.setMerchant_name(mName);
				book.setSoport_id(22);
				book.setSport_name("运动项目名");
				book.setTotal_price(mPrice);
				bundle.putSerializable(OrderEnsureActivity.MERCHANT_BOOK, book);
				HelperUi.startActivity(ReserveMerchantActivity.this,
						OrderEnsureActivity.class, bundle);
			}

			break;
		case R.id.right_btn:
			HelperUi.startActivity(ReserveMerchantActivity.this,
					MyOrderActivity.class);
			break;

		default:
			break;
		}
	}

	class SelectDateAdaper extends BaseAdapter {

		@Override
		public int getCount() {
			return StringUtils.getData().size();
		}

		@Override
		public Object getItem(int position) {
			return StringUtils.getData().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(ReserveMerchantActivity.this)
					.inflate(R.layout.item_choose_time, parent, false);
			TextView today02 = (TextView) convertView.findViewById(R.id.today);
			TextView date02 = (TextView) convertView.findViewById(R.id.date);
			VenueWeekdate entity = (VenueWeekdate) getItem(position);
			today02.setText(entity.getToday());
			date02.setText(entity.getDate1());
			convertView.setTag(entity);
			if (isSelectDateId == entity.getId()) {
				convertView.setBackground(getResources().getDrawable(
						R.drawable.bg_select_site_date));
			} else {
				convertView.setBackgroundResource(R.color.white);
			}
			return convertView;
		}

	}

	// 场馆可选择时间
	public static class VenueWeekdate {
		private int id;
		private String today;
		private String date1;
		private String date2;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getToday() {
			return today;
		}

		public void setToday(String today) {
			this.today = today;
		}

		public String getDate1() {
			return date1;
		}

		public void setDate1(String date1) {
			this.date1 = date1;
		}

		public String getDate2() {
			return date2;
		}

		public void setDate2(String date2) {
			this.date2 = date2;
		}

	}

	// 场馆编号
	public class SiteNum {
		public int id;
		public String siteNum;
	}

}
