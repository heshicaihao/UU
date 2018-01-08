package com.huiyoumall.uu.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.entity.Order;
import com.huiyoumall.uu.entity.Order.TDeatil;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.util.DateUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 订单确定 界面
 * 
 * @author ASUS
 * 
 */
public class OrderEnsureActivity extends BaseActivity {

	public static String MERCHANT_BOOK = "merchant_book";// 场预订

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBook = (Order) getIntent().getExtras().getSerializable(MERCHANT_BOOK);
		setContentView(R.layout.activity_order_ensure);
	}

	private TextView vTitle;
	private TextView vSprot_name;
	private LinearLayout tid_order_view1, tid_order_view2, tid_order_view3,
			tid_order_view4;
	private TextView tname_text1, tname_text2, tname_text3, tname_text4;
	private TextView tid_time_text1, tid_time_text2, tid_time_text3,
			tid_time_text4;
	private TextView tid_price_text1, tid_price_text2, tid_price_text3,
			tid_price_text4;
	private TextView vMname_text;
	private TextView order_totle_price;
	private TextView order_date_text;
	private TextView order_account;

	private Button btn_order;
	private List<TDeatil> mDetails = new ArrayList<TDeatil>();
	private Order mBook;
	private AppContext app;
	private String mOrderJson;
	private ProgressDialog mDialog;

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		tid_order_view1 = (LinearLayout) findViewById(R.id.tid_order_view1);
		tid_order_view2 = (LinearLayout) findViewById(R.id.tid_order_view2);
		tid_order_view3 = (LinearLayout) findViewById(R.id.tid_order_view3);
		tid_order_view4 = (LinearLayout) findViewById(R.id.tid_order_view4);

		tname_text1 = (TextView) findViewById(R.id.tname_text1);
		tname_text2 = (TextView) findViewById(R.id.tname_text2);
		tname_text3 = (TextView) findViewById(R.id.tname_text3);
		tname_text4 = (TextView) findViewById(R.id.tname_text4);

		tid_time_text1 = (TextView) findViewById(R.id.tid_time_text1);
		tid_time_text2 = (TextView) findViewById(R.id.tid_time_text2);
		tid_time_text3 = (TextView) findViewById(R.id.tid_time_text3);
		tid_time_text4 = (TextView) findViewById(R.id.tid_time_text4);

		tid_price_text1 = (TextView) findViewById(R.id.tid_price_text1);
		tid_price_text2 = (TextView) findViewById(R.id.tid_price_text2);
		tid_price_text3 = (TextView) findViewById(R.id.tid_price_text3);
		tid_price_text4 = (TextView) findViewById(R.id.tid_price_text4);

		vSprot_name = (TextView) findViewById(R.id.sprot_name);
		vMname_text = (TextView) findViewById(R.id.m_name_text);
		order_date_text = (TextView) findViewById(R.id.order_date_text);
		order_totle_price = (TextView) findViewById(R.id.order_totle_price);
		order_account = (TextView) findViewById(R.id.order_account);
		btn_order = (Button) findViewById(R.id.btn_order);
	}

	@Override
	public void initView() {
		vTitle.setText("确认订单");
		app = (AppContext) getApplicationContext();
		vSprot_name.setText(mBook.getSport_name());
		vMname_text.setText(mBook.getMerchant_name());
		order_date_text.setText(DateUtil.getWeekDate(mBook.getDate()));
		order_account.setText(app.getisLoginUser().getMember_name());
		double totlePrice = 0;
		List<TDeatil> details = new ArrayList<TDeatil>();
		mDetails.addAll(mBook.gettDeatils());
		if (mDetails.size() > 0) {
			// 把订单信息转换为服务器需要的json数据
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			sb.append("\"uid\":\"" + 3 + "\",\"mid\":\"" + 32
					+ "\",\"sprotid\":\"" + 20 + "\",\"date\":\""
					+ mBook.getDate() + "\",\"detail\":[");
			if (mDetails != null && mDetails.size() > 0) {
				for (int i = 0; i < mDetails.size(); i++) {
					sb.append("{\"tid\":\""
							+ mDetails.get(i).getTid()
							+ "\",\"field\":\""
							+ ReserveMerchantActivity.timeField.get(mDetails
									.get(i).getTime_quantum())
							+ "\",\"time_quantum\":\""
							+ mDetails.get(i).getTime_quantum()
							+ "\",\"price\":\"" + mDetails.get(i).getT_price()
							+ "\"},");
				}
			}
			// 删除最后一个逗号
			sb.deleteCharAt(sb.length() - 1);
			sb.append("]}");
			mOrderJson = sb.toString();

			// 先取出总价格
			for (int i = 0; i < mDetails.size(); i++) {
				totlePrice += Double.parseDouble(mDetails.get(i).getT_price());
			}
			order_totle_price.setText(totlePrice + "元");
			// 每个场在不同时间点的价格
			int mTid = 0;
			while (true) {
				for (int i = 0; i < mDetails.size(); i++) {
					TDeatil detail = new TDeatil();
					detail = mDetails.get(i);
					mTid = detail.getTid();
					mDetails.remove(i);
					for (int j = 0, len = mDetails.size(); j < len; ++j) {

						if (mTid == mDetails.get(j).getTid()) {
							detail.setTime_quantum(detail.getTime_quantum()
									+ "," + mDetails.get(j).getTime_quantum());
							detail.setT_price(detail.getT_price() + ","
									+ mDetails.get(j).getT_price());
							mDetails.remove(j);
							--len;
							--j;
						}
					}
					details.add(detail);
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
					tid_order_view1.setVisibility(View.VISIBLE);
					tname_text1.setText(tName);
					tid_time_text1.setText(tTime);
					tid_price_text1.setText(tPrice);
					break;
				case 1:
					tid_order_view2.setVisibility(View.VISIBLE);
					tname_text2.setText(tName);
					tid_time_text2.setText(tTime);
					tid_price_text2.setText(tPrice);
					break;
				case 2:
					tid_order_view3.setVisibility(View.VISIBLE);
					tname_text3.setText(tName);
					tid_time_text3.setText(tTime);
					tid_price_text3.setText(tPrice);
					break;
				case 3:
					tid_order_view4.setVisibility(View.VISIBLE);
					tname_text4.setText(tName);
					tid_time_text4.setText(tTime);
					tid_price_text4.setText(tPrice);
					break;

				default:
					break;
				}
			}
		}

		btn_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog = new ProgressDialog(OrderEnsureActivity.this);
				mDialog.setTitle("提示");
				mDialog.setMessage("提交中....");
				mDialog.show();
				System.out.println(mOrderJson);
				final Message msg = Message.obtain();
				UURemoteApi.uploadOrderStr(mOrderJson,
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								msg.obj = new String(arg2);
								msg.what = 1;
								mHandler.sendMessage(msg);
							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								msg.what = -1;
								mHandler.sendMessage(msg);
							}

						});
			}
		});
	}

	private String mOrder;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mDialog.dismiss();
			if (msg.what == 1) {
				String result = (String) msg.obj;
				// 切换
				if (!result.equals("0")) {
					mOrder = result;
					Bundle bundle = new Bundle();
					bundle.putSerializable(
							OrderEnsureActivity.MERCHANT_BOOK,
							(Order) getIntent().getExtras().getSerializable(
									MERCHANT_BOOK));
					bundle.putString("orders", mOrder);
					HelperUi.startActivity(OrderEnsureActivity.this,
							UUPayActivity.class, bundle);
					OrderEnsureActivity.this.finish();
				}
			} else {
				HelperUi.showToastShort(OrderEnsureActivity.this, "订单生成失败！");
			}
		}
	};

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
