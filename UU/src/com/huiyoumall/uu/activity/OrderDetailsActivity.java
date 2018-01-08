package com.huiyoumall.uu.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.entity.Order;
import com.huiyoumall.uu.entity.Order.TDeatil;
import com.huiyoumall.uu.util.DateUtil;

/**
 * 订单详情 界面
 */
public class OrderDetailsActivity extends BaseActivity {

	public static String ORDER_DETAIL = "order_detail";
	public static String ORDER_STATUS = "order_detail";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mOrder = (Order) getIntent().getExtras().getSerializable(ORDER_DETAIL);
		setContentView(R.layout.activity_order_details);
	}

	private RelativeLayout merchant_address_lin;
	private LinearLayout no_pay_lin;
	private Button btn_pay_now;
	private TextView merchant_name;
	private TextView merchant_address;
	private TextView sport_name;
	private TextView book_date;

	private LinearLayout order_view1, order_view2, order_view3, order_view4;
	private TextView tid_time1, tid_time2, tid_time3, tid_time4;
	private TextView tid_name1, tid_name2, tid_name3, tid_name4;
	private TextView tid_price1, tid_price2, tid_price3, tid_price4;

	private TextView order_code;
	private TextView order_numm;
	private TextView order_date;
	private TextView pay_status;
	private TextView order_price;
	private Order mOrder;
	private int order_status;// 订单状态 0未支付，1支付未消费，2支付已经消费，3取消

	@Override
	public void findViewById() {
		merchant_address_lin = (RelativeLayout) findViewById(R.id.merchant_address_lin);
		no_pay_lin = (LinearLayout) findViewById(R.id.no_pay_lin);
		btn_pay_now = (Button) findViewById(R.id.btn_pay_now);
		merchant_name = (TextView) findViewById(R.id.merchant_name);
		merchant_address = (TextView) findViewById(R.id.merchant_address);
		sport_name = (TextView) findViewById(R.id.sport_name);
		book_date = (TextView) findViewById(R.id.book_date);
		order_code = (TextView) findViewById(R.id.order_code);
		order_numm = (TextView) findViewById(R.id.order_numm);
		order_date = (TextView) findViewById(R.id.order_date);
		pay_status = (TextView) findViewById(R.id.pay_status);
		order_price = (TextView) findViewById(R.id.order_price);

		order_view1 = (LinearLayout) findViewById(R.id.order_view1);
		order_view2 = (LinearLayout) findViewById(R.id.order_view2);
		order_view3 = (LinearLayout) findViewById(R.id.order_view3);
		order_view4 = (LinearLayout) findViewById(R.id.order_view4);

		tid_name1 = (TextView) findViewById(R.id.tid_name1);
		tid_name2 = (TextView) findViewById(R.id.tid_name2);
		tid_name3 = (TextView) findViewById(R.id.tid_name3);
		tid_name4 = (TextView) findViewById(R.id.tid_name4);

		tid_time1 = (TextView) findViewById(R.id.tid_time1);
		tid_time2 = (TextView) findViewById(R.id.tid_time2);
		tid_time3 = (TextView) findViewById(R.id.tid_time3);
		tid_time4 = (TextView) findViewById(R.id.tid_time4);

		tid_price1 = (TextView) findViewById(R.id.tid_price1);
		tid_price2 = (TextView) findViewById(R.id.tid_price2);
		tid_price3 = (TextView) findViewById(R.id.tid_price3);
		tid_price4 = (TextView) findViewById(R.id.tid_price4);
	}

	@Override
	public void initView() {
		if (mOrder != null) {
			if (mOrder.getStatus() == 0) {
				no_pay_lin.setVisibility(View.VISIBLE);
			} else {
				no_pay_lin.setVisibility(View.GONE);
			}
			merchant_name.setText(mOrder.getMerchant_name());
			merchant_address.setText(mOrder.getAddress());
			sport_name.setText(mOrder.getSport_name());
			book_date.setText(huodongDate(mOrder.getDate()));
			order_price.setText(mOrder.getTotal_price() + "元");
			order_numm.setText(mOrder.getOrder_sn());
			order_date.setText(mOrder.getCreate_time());
			order_status = mOrder.getStatus();
			switch (order_status) {
			case 0:
				pay_status.setText("未付款");
				break;
			case 1:
				pay_status.setText("未消费");
				break;
			case 2:
				pay_status.setText("已消费");
				break;
			case 3:
				pay_status.setText("已取消");
				break;
			default:
				break;
			}
			getOrderDetail();
		}

		merchant_address_lin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString(MerchantDetailsActivity.M_ID,
						mOrder.getMerchant_id() + "");
				HelperUi.startActivity(OrderDetailsActivity.this,
						MerchantDetailsActivity.class, bundle);
				// OrderDetailsActivity.this.finish();
			}
		});

		btn_pay_now.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putSerializable(
						OrderEnsureActivity.MERCHANT_BOOK,
						(Order) getIntent().getExtras().getSerializable(
								ORDER_DETAIL));
				bundle.putString("orders", mOrder.getOrder_sn());
				HelperUi.startActivity(OrderDetailsActivity.this,
						UUPayActivity.class, bundle);
				OrderDetailsActivity.this.finish();
			}
		});

	}

	private void getOrderDetail() {
		List<TDeatil> mDetails = new ArrayList<TDeatil>();
		mDetails.addAll(mOrder.gettDeatils());
		List<TDeatil> details = new ArrayList<TDeatil>();
		if (mDetails != null && mDetails.size() > 0) {
			// 每个场在不同时间点的价格
			int mTid = 0;
			while (true) {
				for (int i = 0; i < mDetails.size(); i++) {
					TDeatil detail = mDetails.get(i);
					mTid = detail.getTid();
					mDetails.remove(i);
					TDeatil detail2 = new TDeatil();
					detail2 = detail;
					for (int j = 0, len = mDetails.size(); j < len; ++j) {
						if (mTid == mDetails.get(j).getTid()) {
							detail2.setTime_quantum(detail2.getTime_quantum()
									+ "," + mDetails.get(j).getTime_quantum());
							detail2.setT_price(detail2.getT_price() + ","
									+ mDetails.get(j).getT_price());
							mDetails.remove(j);
							--len;
							--j;
						}
					}
					details.add(detail2);
					break;
				}
				if (mDetails.size() == 0) {
					break;
				}
			}
		}

		if (details.size() > 0) {
			for (int k = 0; k < details.size(); k++) {
				String tName = details.get(k).getT_name();
				String tTime = "";
				String tPrice = "";
				if (details.get(k).getTime_quantum().contains(",")) {
					String[] time = details.get(k).getTime_quantum().split(",");
					for (int i = 0; i < time.length; i++) {
						tTime = tTime + time[i] + "\n";
					}
				} else {
					tTime = details.get(k).getTime_quantum();
				}
				if (details.get(k).getT_price().contains(",")) {
					String[] price = details.get(k).getT_price().split(",");
					for (int i = 0; i < price.length; i++) {
						tPrice = tPrice + price[i] + "元" + "\n";
					}
				} else {
					tPrice = details.get(k).getT_price() + "元";
				}

				switch (k) {
				case 0:
					order_view1.setVisibility(View.VISIBLE);
					tid_name1.setText(tName);
					tid_time1.setText(tTime);
					tid_price1.setText(tPrice);
					break;
				case 1:
					order_view2.setVisibility(View.VISIBLE);
					tid_name2.setText(tName);
					tid_time2.setText(tTime);
					tid_price2.setText(tPrice);
					break;
				case 2:
					order_view3.setVisibility(View.VISIBLE);
					tid_name3.setText(tName);
					tid_time3.setText(tTime);
					tid_price3.setText(tPrice);
					break;
				case 3:
					order_view4.setVisibility(View.VISIBLE);
					tid_name4.setText(tName);
					tid_time4.setText(tTime);
					tid_price4.setText(tPrice);
					break;

				default:
					break;
				}
			}
		}
	}

	private String huodongDate(String mDate) {
		String week = DateUtil.getWeekNumber(mDate, "yyyy-MM-dd");
		// String dates = "";
		// String[] date = mDate.split("-");
		// dates = date[1] + "-" + date[2];
		return week + " (" + mDate + ")";
	}

	@Override
	public void activitybackview(View view) {
		super.activitybackview(view);
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
