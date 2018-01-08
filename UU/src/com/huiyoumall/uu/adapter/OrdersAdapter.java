package com.huiyoumall.uu.adapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.activity.OrderEnsureActivity;
import com.huiyoumall.uu.activity.UUPayActivity;
import com.huiyoumall.uu.entity.Order;

/**
 * 订单 适配器
 * 
 * @author ASUS
 * 
 */
public class OrdersAdapter extends BaseAdapter {

	private List<Order> mOrders;
	private Context context;
	private LayoutInflater mInflater;
	private int status;// 状态 0未支付，1支付未消费，2支付已经消费，3取消

	public OrdersAdapter(Context context, List<Order> list_, int status_) {
		this.mOrders = list_;
		this.context = context;
		this.status = status_;
		mInflater = LayoutInflater.from(context);
	}

	public void appendList(List<Order> list) {
		if (!mOrders.containsAll(list) && list != null && list.size() > 0) {
			mOrders.addAll(list);
		}
		notifyDataSetChanged();
	}

	public void clear() {
		mOrders.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mOrders.size();
	}

	@Override
	public Object getItem(int position) {
		return mOrders.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			vh = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_my_order_nopay, null);
			vh.no_pay_lin = (RelativeLayout) convertView
					.findViewById(R.id.no_pay_lin);
			vh.title = (TextView) convertView.findViewById(R.id.title);
			vh.status = (TextView) convertView.findViewById(R.id.status);
			vh.project = (TextView) convertView.findViewById(R.id.project);
			vh.date = (TextView) convertView.findViewById(R.id.date);
			vh.price = (TextView) convertView.findViewById(R.id.price);
			vh.nopey_cancel_text = (TextView) convertView
					.findViewById(R.id.nopey_cancel_text);
			vh.nopay_pay_text = (TextView) convertView
					.findViewById(R.id.nopay_pay_text);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final Order order = (Order) getItem(position);
		vh.title.setText(order.getMerchant_name());
		int status = order.getStatus();
		switch (status) {
		case 0:
			vh.status.setText("未付款");
			vh.no_pay_lin.setVisibility(View.VISIBLE);
			break;
		case 1:
			vh.status.setText("未消费");
			vh.no_pay_lin.setVisibility(View.GONE);
			break;
		case 2:
			vh.status.setText("已消费");
			vh.no_pay_lin.setVisibility(View.GONE);
			break;
		case 3:
			vh.status.setText("已取消");
			vh.no_pay_lin.setVisibility(View.GONE);
			break;
		default:
			break;
		}

		vh.project.setText(order.getSport_name());
		vh.price.setText(order.getTotal_price());
		vh.date.setText(order.getDate());
		vh.nopay_pay_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putSerializable(OrderEnsureActivity.MERCHANT_BOOK, order);
				bundle.putString("orders", order.getOrder_sn());
				HelperUi.startActivity(context, UUPayActivity.class, bundle);
			}
		});
		vh.nopey_cancel_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		return convertView;
	}

	static class ViewHolder {
		RelativeLayout no_pay_lin;
		TextView title;
		TextView status;
		TextView project;
		TextView date;
		TextView price;
		TextView nopey_cancel_text;
		TextView nopay_pay_text;
	}
}
