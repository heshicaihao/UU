package com.huiyoumall.uu.activity;

import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.entity.Order;
import com.huiyoumall.uu.entity.Order.TDeatil;
import com.huiyoumall.uu.remote.NetWorkHelper;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.util.DateUtil;
import com.huiyoumall.uu.util.StringUtils;
import com.huiyoumall.uu.widget.EmptyLayout;
import com.huiyoumall.uu.widget.GridViewForScollView;
import com.huiyoumall.uu.widget.ListViewForScrollView;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 发布活动 界面
 * 
 * @author ASUS
 * 
 */
public class PostEventsActivity extends BaseActivity implements OnClickListener {

	private TextView vTitle;
	private TextView project_text;
	private TextView time_text;
	private TextView place_text;
	private EditText activity_name;
	private EditText people_num_edit;
	private EditText consume_edit;
	private EditText information_edit;

	private TextView methem1, methem2, methem3, methem4;
	private TextView paytype1, paytype2, paytype3;

	private EmptyLayout post_empter_view;

	private RelativeLayout book_lin;
	private Button reserve_btn;
	private Button btn_post_events;

	private ListViewForScrollView dingdan_list;
	private List<Order> datas;

	private String mMethem;// 主题
	private String mPriceType;// 价格类型

	private String mDate;// 活动时间
	private String mTime;// 开始时间
	private String sportId;// 场馆项目
	private String mId;// 场馆id
	private String tId;// 场地id
	private String t_price;// 场地价
	private String address;// 活动地址

	private String name;// 活动名称
	private String people_num;// 活动人数
	private String price;// 活动单价
	private String information;// 活动描述

	private String mIsSelect;// 选中的预订场地
	AppContext app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_events);
	}

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		time_text = (TextView) findViewById(R.id.time_text);
		activity_name = (EditText) findViewById(R.id.activity_name);
		people_num_edit = (EditText) findViewById(R.id.people_num_edit);
		dingdan_list = (ListViewForScrollView) findViewById(R.id.dingdan_list);
		project_text = (TextView) findViewById(R.id.project_text);
		place_text = (TextView) findViewById(R.id.place_text);
		consume_edit = (EditText) findViewById(R.id.consume_edit);
		information_edit = (EditText) findViewById(R.id.information_edit);
		post_empter_view = (EmptyLayout) findViewById(R.id.post_empter_view);

		book_lin = (RelativeLayout) findViewById(R.id.book_lin);
		reserve_btn = (Button) findViewById(R.id.reserve_btn);
		btn_post_events = (Button) findViewById(R.id.btn_post_events);

		methem1 = (TextView) findViewById(R.id.methem1);
		methem2 = (TextView) findViewById(R.id.methem2);
		methem3 = (TextView) findViewById(R.id.methem3);
		methem4 = (TextView) findViewById(R.id.methem4);

		paytype1 = (TextView) findViewById(R.id.paytype1);
		paytype2 = (TextView) findViewById(R.id.paytype2);
		paytype3 = (TextView) findViewById(R.id.paytype3);

		reserve_btn.setOnClickListener(this);
		btn_post_events.setOnClickListener(this);

		methem1.setOnClickListener(this);
		methem2.setOnClickListener(this);
		methem3.setOnClickListener(this);
		methem4.setOnClickListener(this);

		paytype1.setOnClickListener(this);
		paytype2.setOnClickListener(this);
		paytype3.setOnClickListener(this);
	}

	@Override
	public void initView() {
		app = (AppContext) getApplicationContext();
		vTitle.setText("发布活动");
		loadData();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				post_empter_view.setVisibility(View.GONE);
				String json = msg.obj.toString();
				if (StringUtils.isEmpty(json)) {
					book_lin.setVisibility(View.VISIBLE);
				} else {
					try {
						datas = Order.parse(json);
						if (datas != null && datas.size() > 0) {
							Order data = datas.get(0);
							if (data.gettDeatils() != null
									&& data.gettDeatils().size() > 0) {
								mTime = data.gettDeatils().get(0)
										.getTime_quantum();
								tId = data.gettDeatils().get(0).getTid() + "";
								t_price = data.gettDeatils().get(0)
										.getT_price();
							}
							mDate = data.getDate();
							sportId = data.getSoport_id() + "";
							mId = data.getMerchant_id() + "";
							address = data.getAddress();
							mIsSelect = mId + "-" + tId + "-" + mTime;

							project_text.setText(data.getSport_name());
							time_text.setText(huodongDate());
							place_text.setText(address);
							consume_edit.setText(t_price);

							PostEventsAdapter adapter = new PostEventsAdapter(
									PostEventsActivity.this, datas);
							dingdan_list.setAdapter(adapter);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				post_empter_view.setErrorType(EmptyLayout.NETWORK_LOADING);
			}
		};
	};

	private String huodongDate() {
		String week = DateUtil.getWeekNumber(mDate, "yyyy-MM-dd");
		String dates = "";
		String[] date = mDate.split("-");
		dates = date[1] + "." + date[2];
		return week + "  " + dates + "  " + mTime.split("-")[0];
	}

	private void loadData() {
		if (!NetWorkHelper.isNetworkAvailable(this)) {
			post_empter_view.setErrorType(EmptyLayout.NETWORK_ERROR);
		} else {
			post_empter_view.setErrorType(EmptyLayout.NETWORK_LOADING);
			final Message msg = Message.obtain();
			UURemoteApi.uploadOrderToActivity("3",
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							String result = new String(arg2);
							msg.what = 1;
							msg.obj = result;
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

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.reserve_btn:

			break;
		case R.id.btn_post_events:
			name = activity_name.getText().toString();
			people_num = people_num_edit.getText().toString();
			price = consume_edit.getText().toString();
			information = information_edit.getText().toString();
			if (StringUtils.isEmpty(name)) {
				HelperUi.showToastShort(PostEventsActivity.this, "请输入活动名称");
				return;
			}

			StringBuilder sb = new StringBuilder();
			sb.append("{");
			sb.append("\"memberid\":\"" + app.member_id + "\",");
			sb.append("\"name\":\"" + name + "\",");
			sb.append("\"sportid\":\"" + sportId + "\",");
			sb.append("\"mid\":\"" + mId + "\",");
			sb.append("\"tid\":\"" + tId + "\",");
			sb.append("\"t_price\":\"" + t_price + "\",");
			sb.append("\"date\":\"" + mDate + "\",");
			sb.append("\"time\":\"" + mTime.split("-")[0] + "\",");
			sb.append("\"people_num\":\"" + people_num + "\",");
			sb.append("\"methem\":\"" + mMethem + "\",");
			sb.append("\"price_type\":\"" + mPriceType + "\",");
			sb.append("\"price\":\"" + price + "\",");
			sb.append("\"information\":\"" + information + "\"");
			sb.append("}");

			UURemoteApi.releaseActivity(sb.toString(),
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							String result = new String(arg2);
							if (result.equals("1")) {
								HelperUi.showToastLong(PostEventsActivity.this,
										"发布成功");
								activity_name.setText("");
							} else {
								HelperUi.showToastLong(PostEventsActivity.this,
										"发布失败");
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							HelperUi.showToastLong(PostEventsActivity.this,
									"发布失败");
						}

					});

			break;
		case R.id.methem1:
			isMethem(1);
			break;
		case R.id.methem2:
			isMethem(2);
			break;
		case R.id.methem3:
			isMethem(3);
			break;
		case R.id.methem4:
			isMethem(4);
			break;
		case R.id.paytype1:
			isPayType(1);
			break;
		case R.id.paytype2:
			isPayType(2);
			break;
		case R.id.paytype3:
			isPayType(3);
			break;
		default:
			break;
		}
	}

	private void isMethem(int index) {
		methem1.setSelected(false);
		methem2.setSelected(false);
		methem3.setSelected(false);
		methem4.setSelected(false);
		switch (index) {
		case 1:
			methem1.setSelected(true);
			mMethem = methem1.getText().toString();
			break;
		case 2:
			methem2.setSelected(true);
			mMethem = methem2.getText().toString();
			break;
		case 3:
			methem3.setSelected(true);
			mMethem = methem3.getText().toString();
			break;
		case 4:
			methem4.setSelected(true);
			mMethem = methem4.getText().toString();
			break;
		default:
			break;
		}
	}

	private void isPayType(int index) {
		paytype1.setSelected(false);
		paytype2.setSelected(false);
		paytype3.setSelected(false);
		switch (index) {
		case 1:
			paytype1.setSelected(true);
			mPriceType = paytype1.getText().toString();
			break;
		case 2:
			paytype2.setSelected(true);
			mPriceType = paytype2.getText().toString();
			break;
		case 3:
			paytype3.setSelected(true);
			mPriceType = paytype3.getText().toString();
			break;
		default:
			break;
		}
	}

	// static class DataEntity {
	// private String mid;
	// private String m_name;
	// private String address;
	// private String sport_id;
	// private String sport_name;
	// private String date;
	// private List<TDeatil> detail;
	//
	// public String getMid() {
	// return mid;
	// }
	//
	// public void setMid(String mid) {
	// this.mid = mid;
	// }
	//
	// public String getM_name() {
	// return m_name;
	// }
	//
	// public void setM_name(String m_name) {
	// this.m_name = m_name;
	// }
	//
	// public String getAddress() {
	// return address;
	// }
	//
	// public void setAddress(String address) {
	// this.address = address;
	// }
	//
	// public String getSport_id() {
	// return sport_id;
	// }
	//
	// public void setSport_id(String sport_id) {
	// this.sport_id = sport_id;
	// }
	//
	// public String getSport_name() {
	// return sport_name;
	// }
	//
	// public void setSport_name(String sport_name) {
	// this.sport_name = sport_name;
	// }
	//
	// public String getDate() {
	// return date;
	// }
	//
	// public void setDate(String date) {
	// this.date = date;
	// }
	//
	// public List<Detail> getDetail() {
	// return detail;
	// }
	//
	// public void setDetail(List<Detail> detail) {
	// this.detail = detail;
	// }
	//
	// public static List<DataEntity> parse(String jsonString)
	// throws JSONException {
	// List<DataEntity> datas = null;
	// // if (!StringUtils.isEmpty(jsonString)) {
	// // datas = GsonTools.getListDate(jsonString, DataEntity.class);
	// // }
	// // return datas;
	//
	// DataEntity date = null;
	// JSONArray array = new JSONArray(jsonString);
	// if (array.length() > 0) {
	// datas = new ArrayList<DataEntity>();
	// for (int i = 0; i < array.length(); i++) {
	// JSONObject jo = array.getJSONObject(i);
	// date = new DataEntity();
	// date.setMid(jo.getString("mid"));
	// date.setM_name(jo.getString("m_name"));
	// date.setAddress(jo.getString("address"));
	// date.setSport_id(jo.getString("sport_id"));
	// date.setSport_name(jo.getString("sport_name"));
	// date.setDate(jo.getString("date"));
	//
	// JSONArray array2 = new JSONArray(jo.getString("detail"));
	// if (array2.length() > 0) {
	// List<Detail> list = new ArrayList<Detail>();
	// Detail detail = null;
	// for (int j = 0; j < array2.length(); j++) {
	// JSONObject object = array2.getJSONObject(j);
	// detail = new Detail();
	// detail.setMid(jo.getString("mid"));
	// detail.setTid(object.getString("tid"));
	// detail.setT_price(object.getString("t_price"));
	// detail.setTname(object.getString("tname"));
	// detail.setT_time(object.getString("t_time"));
	// list.add(detail);
	// }
	// date.setDetail(list);
	// }
	// datas.add(date);
	// }
	// }
	//
	// return datas;
	// }
	// }

	class PostEventsAdapter extends BaseAdapter {
		private List<Order> lists;
		private Context mContext;

		public PostEventsAdapter(Context context, List<Order> list_) {
			this.mContext = context;
			this.lists = list_;
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HoldView holdView = null;
			if (convertView == null) {
				holdView = new HoldView();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_post_events_list, null);
				holdView.merchant_name_text = (TextView) convertView
						.findViewById(R.id.merchant_name_text);
				holdView.merchant_date_text = (TextView) convertView
						.findViewById(R.id.merchant_date_text);
				holdView.gridView = (GridViewForScollView) convertView
						.findViewById(R.id.gv_home);
				convertView.setTag(holdView);
			} else {
				holdView = (HoldView) convertView.getTag();
			}
			final Order data = (Order) getItem(position);
			holdView.merchant_name_text.setText(data.getMerchant_name());
			holdView.merchant_date_text.setText(data.getDate());
			final List<TDeatil> details = data.gettDeatils();
			if (details != null && details.size() > 0) {
				PostEventsGridViewAdapter gridViewAdapter = new PostEventsGridViewAdapter(
						mContext, details);
				holdView.gridView.setAdapter(gridViewAdapter);
				holdView.gridView
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								mDate = data.getDate();
								sportId = data.getSoport_id() + "";
								mId = data.getMerchant_id() + "";
								address = data.getAddress();
								mTime = details.get(position).getTime_quantum();
								tId = details.get(position).getTid() + "";
								t_price = details.get(position).getT_price();

								mIsSelect = details.get(position).getMid()
										+ "-"
										+ details.get(position).getTid()
										+ "-"
										+ details.get(position)
												.getTime_quantum();

								project_text.setText(data.getSport_name());
								time_text.setText(huodongDate());
								place_text.setText(address);
								consume_edit.setText(t_price);

								activity_name.setText("");
								people_num_edit.setText("");
								information_edit.setText("");
								notifyDataSetChanged();
							}
						});
			}
			return convertView;
		}

		class HoldView {
			TextView merchant_name_text;
			TextView merchant_date_text;
			GridViewForScollView gridView;
		}
	}

	class PostEventsGridViewAdapter extends BaseAdapter {

		List<TDeatil> lists;
		Context context;

		public PostEventsGridViewAdapter(Context context, List<TDeatil> list_) {
			this.lists = list_;
			this.context = context;
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolderGridView hoGridView = null;
			if (convertView == null) {
				hoGridView = new HolderGridView();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_postevents_grid, null);
				hoGridView.tName = (TextView) convertView
						.findViewById(R.id.venue_num_text);
				hoGridView.tTime = (TextView) convertView
						.findViewById(R.id.venue_time_text);
				convertView.setTag(hoGridView);
			} else {
				hoGridView = (HolderGridView) convertView.getTag();
			}
			TDeatil detail = (TDeatil) getItem(position);
			hoGridView.tName.setText(detail.getT_name());
			hoGridView.tTime.setText(detail.getTime_quantum());

			if (mIsSelect.equals(detail.getMid() + "-" + detail.getTid() + "-"
					+ detail.getTime_quantum())) {
				convertView.setBackground(getResources().getDrawable(
						R.color.tab_view_press));
			}
			return convertView;
		}

		class HolderGridView {
			TextView tName;
			TextView tTime;
		}
	}

}
