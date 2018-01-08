package com.huiyoumall.uu.activity;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.entity.Order;
import com.huiyoumall.uu.entity.Order.TDeatil;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.util.DateUtil;
import com.huiyoumall.uu.util.StringUtils;
import com.huiyoumall.uu.widget.MyRadioGroup;
import com.huiyoumall.uu.widget.MyRadioGroup.OnCheckedChangeListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class UUPayActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBook = (Order) getIntent().getExtras().getSerializable(
				OrderEnsureActivity.MERCHANT_BOOK);
		mOrder = getIntent().getExtras().getString("orders");
		setContentView(R.layout.activity_pay);
	}

	private LinearLayout tid_order_view1, tid_order_view2, tid_order_view3,
			tid_order_view4;
	private TextView tname_text1, tname_text2, tname_text3, tname_text4;
	private TextView tid_time_text1, tid_time_text2, tid_time_text3,
			tid_time_text4;
	private TextView vTitle;
	private TextView vPay_time_text;
	private TextView order_merchant_name_text;
	private TextView totle_order_price;
	private TextView order_date_text;
	private TextView helper_pay;
	private Button vBtn_pay;
	private MyRadioGroup radio_group;
	private Order mBook;
	private ProgressDialog mDialog;
	private String mOrder;

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

		radio_group = (MyRadioGroup) findViewById(R.id.pay_radiogroup);
		order_merchant_name_text = (TextView) findViewById(R.id.order_merchant_name_text);
		totle_order_price = (TextView) findViewById(R.id.totle_order_price);
		order_date_text = (TextView) findViewById(R.id.order_date_text);
		helper_pay = (TextView) findViewById(R.id.right_btn);
		vPay_time_text = (TextView) findViewById(R.id.pay_time_text);
		vBtn_pay = (Button) findViewById(R.id.btn_pay);
	}

	private void setText(String time) {
		vPay_time_text.setText(Html.fromHtml(time));
	}

	@Override
	public void initView() {
		vTitle.setText("֧��");
		helper_pay.setText("����");
		order_merchant_name_text.setText(mBook.getMerchant_name());
		totle_order_price.setText(mBook.getTotal_price() + "Ԫ");
		order_date_text.setText(DateUtil.getWeekDate(mBook.getDate()));
		vBtn_pay.setOnClickListener(this);
		getOrderDetail();
		mThread.start();
		radio_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(MyRadioGroup group, int checkedId) {
				int radioButtonId = group.getCheckedRadioButtonId();
				payId = radioButtonId;
			}
		});
		helper_pay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HelperUi.startActivity(UUPayActivity.this,
						PayHelpActivity.class);
			}
		});
		vBtn_pay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (payId == 0) {
					Toast.makeText(UUPayActivity.this, "��ѡ��֧����ʽ",
							Toast.LENGTH_SHORT).show();
				} else {
					if (payId == R.id.alipay_radio) {
						mDialog = new ProgressDialog(UUPayActivity.this);
						mDialog.setTitle("��ʾ");
						mDialog.setMessage("֧����...");
						mDialog.show();
						UURemoteApi.uploadAilpay(mOrder,
								new AsyncHttpResponseHandler() {

									@Override
									public void onSuccess(int arg0,
											Header[] arg1, byte[] arg2) {
										mDialog.dismiss();
										String result = new String(arg2);
										if (!StringUtils.isEmpty(result)) {
											Alipay pay = new Alipay(
													UUPayActivity.this);
											// pay.setProduct("ΰ����ë��");
											// pay.setPrice(0.01);
											// pay.setOrder(System
											// .currentTimeMillis() + "");
											pay.pay(result);

											// pay.payTask(result);
										}
									}

									@Override
									public void onFailure(int arg0,
											Header[] arg1, byte[] arg2,
											Throwable arg3) {
										mDialog.dismiss();
										Toast.makeText(UUPayActivity.this,
												"֧���쳣�����Ժ����ԣ�",
												Toast.LENGTH_SHORT).show();
									}

								});
					} else if (payId == R.id.weixinpay_radio) {
						Toast.makeText(UUPayActivity.this, "΢����ʱ��δ����",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

	}

	private void getOrderDetail() {
		List<TDeatil> mDetails = new ArrayList<TDeatil>();
		mDetails.addAll(mBook.gettDeatils());
		List<TDeatil> details = new ArrayList<TDeatil>();
		if (mDetails != null && mDetails.size() > 0) {
			// ÿ�����ڲ�ͬʱ���ļ۸�
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
				if (details.get(k).getTime_quantum().contains(",")) {
					String[] time = details.get(k).getTime_quantum().split(",");
					for (int i = 0; i < time.length; i++) {
						tTime = tTime + time[i] + "\n";
					}
				} else {
					tTime = details.get(k).getTime_quantum();
				}

				switch (k) {
				case 0:
					tid_order_view1.setVisibility(View.VISIBLE);
					tname_text1.setText(tName);
					tid_time_text1.setText(tTime);
					break;
				case 1:
					tid_order_view2.setVisibility(View.VISIBLE);
					tname_text2.setText(tName);
					tid_time_text2.setText(tTime);
					break;
				case 2:
					tid_order_view3.setVisibility(View.VISIBLE);
					tname_text3.setText(tName);
					tid_time_text3.setText(tTime);
					break;
				case 3:
					tid_order_view4.setVisibility(View.VISIBLE);
					tname_text4.setText(tName);
					tid_time_text4.setText(tTime);
					break;

				default:
					break;
				}
			}
		}
	}

	@Override
	public void activitybackview(View view) {
		if (counter != null) {
			counter.cancel();
		}
		super.activitybackview(view);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (counter != null) {
				counter.cancel();
			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private String longTime = "";
	private Counter counter;
	// �����̵߳���ʱ
	private Handler mHander = new Handler() {
		public void handleMessage(Message msg) {
			long result = (Long) msg.obj;
			if (msg.what == 1) {
				if (result != 0) {
					if (StringUtils.isEmpty(longTime)) {
						longTime = new GregorianCalendar().getTimeInMillis()
								+ "";
					}
					long time = Long.parseLong(longTime) + (1000 * 60 * 10);
					long cTime = time - result;
					if (cTime > 0) {
						counter = new Counter(cTime, 1000);
						counter.start();
					} else {
						Toast.makeText(UUPayActivity.this, "�����Ѿ���ʱ",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
	};
	// ��ȡ����ʱ�� ���ɶ���ʱ��
	private Thread mThread = new Thread() {
		public void run() {
			try {
				URL url = new URL("http://www.bjtime.cn");
				URLConnection uc = url.openConnection();
				uc.connect();
				long id = uc.getDate();
				longTime = uc.getDate() + "";
				Message msg = Message.obtain();
				msg.obj = id;
				msg.what = 1;// ���ɶ���ʱ��
				mHander.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	private static final int SECONDS = 60; // ����
	private static final int MINUTES = 60 * 60; // Сʱ
	private long first = 0, twice = 0, third = 0;
	private long mtmp = 0, mtmp2 = 0;

	public class Counter extends CountDownTimer {

		public Counter(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			setText("�����Ѿ����ڣ�������֧����");
			finish();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// ��ȡ��ǰʱ��������
			first = millisUntilFinished / 1000;
			if (first < SECONDS) { // С��һ���� ֻ��ʾ��
				setText("���� " + "<font color='#ff6600'>"
						+ (first < 10 ? "0" + first : first) + "</font>������ɸ���");
			} else if (first < MINUTES) { // ���ڻ����һ���ӣ���С��һСʱ����ʾ����
				twice = first % 60; // ����תΪ����ȡ�࣬����Ϊ��
				mtmp = first / 60; // ������תΪ����
				if (twice == 0) {
					setText("���� " + "<font color='#ff6600'>"
							+ (mtmp < 10 ? "0" + mtmp : mtmp) + "</font>������ɸ���"); // ֻ��ʾ����
				} else {
					setText("���� " + "<font color='#ff6600'>"
							+ (mtmp < 10 ? "0" + mtmp : mtmp)
							+ " </font>��<font color='#ff6600'> "
							+ (twice < 10 ? "0" + twice : twice)
							+ "</font>������ɸ���"); // ��ʾ���Ӻ���
				}
			}
		}
	}

	int payId = 0; // 1 ΢�ţ�2 ֧����

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_pay:
			WinXinPay wxPay = new WinXinPay(UUPayActivity.this);
			wxPay.wxpay();
			break;
		default:
			break;
		}
	}
}
