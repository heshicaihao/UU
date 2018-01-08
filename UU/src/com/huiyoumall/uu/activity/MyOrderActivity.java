package com.huiyoumall.uu.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.adapter.OrdersAdapter;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.entity.Order;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.widget.EmptyLayout;
import com.huiyoumall.uu.widget.SuperViewPagerAdapter;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 我的订单
 * 
 * @author ASUS
 * 
 */
public class MyOrderActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order);
		findViewById();
		initView();
	}

	private TextView vTitle;
	private TextView right_btn;
	private LinearLayout vNot_pay_lin_view;
	private LinearLayout vIs_pay_lin_view;
	private LinearLayout vCompant_pay_lin_view;

	private SuperViewPagerAdapter pagerAdapter;
	private ViewPager mViewPager;
	private View mNot_pay_view;
	private View mIs_pay_view;
	private View mMy_compant_view;
	private EmptyLayout my_order_empter_view;
	private ListView my_Not_pay_lv;
	private ListView my_Is_pay_lv;
	private ListView my_Compant_lv;

	private List<Order> act_not_order_list;
	private List<Order> act_is_order_list;
	private List<Order> act_compant_list;

	AppContext app;

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		right_btn = (TextView) findViewById(R.id.right_btn);
		my_order_empter_view = (EmptyLayout) findViewById(R.id.my_order_empter_view);

		vNot_pay_lin_view = (LinearLayout) findViewById(R.id.not_pay_lin_view);
		vIs_pay_lin_view = (LinearLayout) findViewById(R.id.is_pay_lin_view);
		vCompant_pay_lin_view = (LinearLayout) findViewById(R.id.complete_pay_view);

		mViewPager = (ViewPager) findViewById(R.id.my_order_viewpager);
	}

	@Override
	public void initView() {
		app = (AppContext) getApplicationContext();
		vTitle.setText("我的订单");
		right_btn.setText("帮助");
		vNot_pay_lin_view.setSelected(true);
		vIs_pay_lin_view.setSelected(false);
		vCompant_pay_lin_view.setSelected(false);

		mNot_pay_view = LayoutInflater.from(this).inflate(
				R.layout.fragment_my_order, null);
		mIs_pay_view = LayoutInflater.from(this).inflate(
				R.layout.fragment_my_order, null);
		mMy_compant_view = LayoutInflater.from(this).inflate(
				R.layout.fragment_my_order, null);

		my_Not_pay_lv = (ListView) mNot_pay_view
				.findViewById(R.id.my_order_list);
		my_Is_pay_lv = (ListView) mIs_pay_view.findViewById(R.id.my_order_list);
		my_Compant_lv = (ListView) mMy_compant_view
				.findViewById(R.id.my_order_list);

		my_Not_pay_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Order order = act_not_order_list.get(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable(OrderDetailsActivity.ORDER_DETAIL, order);
				HelperUi.startActivity(MyOrderActivity.this,
						OrderDetailsActivity.class, bundle);

			}
		});
		my_Is_pay_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Order order = act_is_order_list.get(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable(OrderDetailsActivity.ORDER_DETAIL, order);
				HelperUi.startActivity(MyOrderActivity.this,
						OrderDetailsActivity.class, bundle);
			}
		});
		my_Compant_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Order order = act_compant_list.get(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable(OrderDetailsActivity.ORDER_DETAIL, order);
				HelperUi.startActivity(MyOrderActivity.this,
						OrderDetailsActivity.class, bundle);
			}
		});

		final ArrayList<View> views = new ArrayList<View>();
		views.add(mNot_pay_view);
		views.add(mIs_pay_view);
		views.add(mMy_compant_view);
		pagerAdapter = new SuperViewPagerAdapter(views);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				change(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		vNot_pay_lin_view.setOnClickListener(this);
		vIs_pay_lin_view.setOnClickListener(this);
		vCompant_pay_lin_view.setOnClickListener(this);
		right_btn.setOnClickListener(this);
		loadData();
	}

	private void loadData() {
		int member_id = app.member_id;
		member_id = 3;
		UURemoteApi.loadMyOrderNotPay(member_id,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						my_order_empter_view.setVisibility(View.GONE);
						String result = new String(arg2);
						try {
							act_not_order_list = Order.parseList(result);
							if (act_not_order_list != null
									&& act_not_order_list.size() > 0) {
								OrdersAdapter mAdapter = new OrdersAdapter(
										MyOrderActivity.this,
										act_not_order_list, 1);
								my_Not_pay_lv.setAdapter(mAdapter);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {

					}

				});
		UURemoteApi.loadMyOrderIsPay(member_id, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				my_order_empter_view.setVisibility(View.GONE);
				String result = new String(arg2);
				try {
					act_is_order_list = Order.parseList(result);
					if (act_is_order_list != null
							&& act_is_order_list.size() > 0) {
						OrdersAdapter mAdapter = new OrdersAdapter(
								MyOrderActivity.this, act_is_order_list, 2);
						my_Is_pay_lv.setAdapter(mAdapter);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}

		});

		UURemoteApi.loadMyOrderCancelPay(member_id,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						my_order_empter_view.setVisibility(View.GONE);
						String result = new String(arg2);
						try {
							act_compant_list = Order.parseList(result);
							if (act_compant_list != null
									&& act_compant_list.size() > 0) {
								OrdersAdapter mAdapter = new OrdersAdapter(
										MyOrderActivity.this, act_compant_list,
										3);
								my_Compant_lv.setAdapter(mAdapter);

							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {

					}

				});
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

	private void change(int index) {
		switch (index) {
		case 0:
			vNot_pay_lin_view.setSelected(true);
			vIs_pay_lin_view.setSelected(false);
			vCompant_pay_lin_view.setSelected(false);
			break;
		case 1:
			vNot_pay_lin_view.setSelected(false);
			vIs_pay_lin_view.setSelected(true);
			vCompant_pay_lin_view.setSelected(false);
			break;
		case 2:
			vNot_pay_lin_view.setSelected(false);
			vIs_pay_lin_view.setSelected(false);
			vCompant_pay_lin_view.setSelected(true);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.not_pay_lin_view:
			change(0);
			mViewPager.setCurrentItem(0);
			break;
		case R.id.is_pay_lin_view:
			change(1);
			mViewPager.setCurrentItem(1);
			break;
		case R.id.complete_pay_view:
			change(2);
			mViewPager.setCurrentItem(2);
			break;
		case R.id.right_btn:
			HelperUi.startActivity(MyOrderActivity.this, PayHelpActivity.class);
			break;
		default:
			break;
		}
	}
}
