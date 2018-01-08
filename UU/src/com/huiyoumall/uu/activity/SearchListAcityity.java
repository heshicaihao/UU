package com.huiyoumall.uu.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.GlobalParams;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseListActivity;
import com.huiyoumall.uu.entity.Merchant;
import com.huiyoumall.uu.entity.MerchantList;
import com.huiyoumall.uu.remote.NetWorkHelper;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.util.StringUtils;
import com.huiyoumall.uu.view.pullview.AbPullToRefreshView;
import com.huiyoumall.uu.widget.EmptyLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 场馆搜索结果的界面
 * 
 * @author ASUS
 * 
 */
public class SearchListAcityity extends BaseListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSearch = getIntent().getStringExtra(SearchMerchantActivity.SEARCH_KEY);
		setContentView(R.layout.activity_search_list);
		findViewById();
		initView();
		// 加载网络数据
		firstLoad();
	}

	private String mSearch;
	private AbPullToRefreshView vPullToRefreshView;
	private ListView vListView;
	private TextView vTitle;
	protected EmptyLayout emptyView;
	private List<Merchant> mMerchants = new ArrayList<Merchant>();
	private SearchListAdapter adapter;

	public void findViewById() {
		vPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		vListView = (ListView) findViewById(R.id.mListView);
		vTitle = (TextView) findViewById(R.id.title);
		emptyView = (EmptyLayout) findViewById(R.id.empter_view);
	}

	public void initView() {
		vTitle.setText("搜索结果");
		// 设置监听器
		vPullToRefreshView.setOnHeaderRefreshListener(this);
		vPullToRefreshView.setOnFooterLoadListener(this);

		// 设置进度条的样式
		vPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		vPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));

		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				GlobalParams.WIN_HEIGHT - 100));
		emptyView.setOnLayoutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				firstLoad();
			}
		});
		adapter = new SearchListAdapter(this, mMerchants);
		vListView.setAdapter(adapter);
	}

	@Override
	public void activitybackview(View view) {
		super.activitybackview(view);
		finish();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 == FIRST_LOAD) {
				if (msg.what == LOAD_SUCCESS) {
					List<Merchant> mList = (List<Merchant>) msg.obj;
					if (mList != null) {
						emptyView.setVisibility(View.GONE);
						vPullToRefreshView.setVisibility(View.VISIBLE);
						mMerchants = mList;
						adapter.setData(mMerchants);

					} else {
						emptyView.setErrorType(EmptyLayout.NODATA);
					}
				} else if (msg.what == LOAD_ERROR) {
					emptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
				}
			}
			// 脚部
			if (msg.arg1 == LOAD_MORE_DATA) {
				vPullToRefreshView.onFooterLoadFinish();
				if (msg.what == LOAD_SUCCESS) {
					List<Merchant> mList = (List<Merchant>) msg.obj;
					if (mList != null) {
						if (mList.size() != 0) {
							mMerchants.addAll(mList);
							adapter.setData(mMerchants);
						}
					}
				}
			}
			// 头部刷新
			if (msg.arg1 == REFRESH_DATA) {
				vPullToRefreshView.onHeaderRefreshFinish();
				if (msg.what == LOAD_SUCCESS) {
					mMerchants.clear();
					MerchantList mlist = (MerchantList) msg.obj;
					if (mlist != null) {
						if (mlist.getmMerchants().size() != 0) {
							mMerchants.addAll(mlist.getmMerchants());
							adapter.setData(mMerchants);
						}
					}
				}
			}
		};
	};

	@Override
	protected void firstLoad() {
		final Message msg = Message.obtain();
		msg.arg1 = FIRST_LOAD;
		vPullToRefreshView.setVisibility(View.GONE);
		if (!NetWorkHelper.isNetworkAvailable(this)) {
			emptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
		} else {
			emptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
			UURemoteApi.searchMerchant(mSearch, 1,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {

							try {
								String result = new String(arg2, "UTF-8");
								msg.what = LOAD_SUCCESS;
								msg.obj = Merchant.getMerchants(result);
							} catch (JSONException e) {
								msg.what = LOAD_ERROR;
							} catch (Exception e) {
								msg.what = LOAD_ERROR;
								e.printStackTrace();
							}
							mHandler.sendMessage(msg);
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							msg.what = LOAD_ERROR;
							mHandler.sendMessage(msg);
						}
					});
		}

	}

	@Override
	protected void loadMore() {
		currentPagte++;
		final Message msg = Message.obtain();
		msg.arg1 = LOAD_MORE_DATA;
		UURemoteApi.searchMerchant(mSearch, 1, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				try {
					msg.obj = Merchant.getMerchants(new String(arg2));
					msg.what = LOAD_SUCCESS;
				} catch (Exception e) {
					msg.what = LOAD_ERROR;
					e.printStackTrace();
				}
				mHandler.sendMessage(msg);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				currentPagte--;
				msg.what = LOAD_ERROR;
				mHandler.sendMessage(msg);
			}
		});

	}

	@Override
	protected void refreshData() {
		currentPagte = 1;
		final Message msg = Message.obtain();
		msg.arg1 = REFRESH_DATA;
		UURemoteApi.searchMerchant("", 2, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				try {
					msg.obj = Merchant.getMerchants(new String(arg2));
					msg.what = LOAD_SUCCESS;
				} catch (Exception e) {
					msg.what = LOAD_ERROR;
					e.printStackTrace();
				}
				mHandler.sendMessage(msg);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				msg.what = LOAD_ERROR;
				mHandler.sendMessage(msg);
			}
		});

	}

	/******************************** 搜索运动适配器 *********************************************/
	/**
	 * 运动项目适配器
	 * 
	 * @author ASUS
	 * 
	 */
	public class SearchListAdapter extends BaseAdapter {

		private List<Merchant> mMerchants;
		private Context context;
		private LayoutInflater mInflater;
		private AppContext app;

		public SearchListAdapter(Context context, List<Merchant> list_) {
			this.mMerchants = list_;
			this.context = context;
			mInflater = LayoutInflater.from(context);
			app = (AppContext) context.getApplicationContext();
		}

		public void setData(List<Merchant> mList) {
			this.mMerchants = mList;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mMerchants.size();
		}

		@Override
		public Object getItem(int position) {
			return mMerchants.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if (convertView == null || convertView.getTag() == null) {
				vh = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.item_search_merchant_list, null);

				vh.nameText = (TextView) convertView.findViewById(R.id.name);
				vh.mdescText = (TextView) convertView
						.findViewById(R.id.address);
				vh.shopPriceText = (TextView) convertView
						.findViewById(R.id.shop_price);
				// vh.classify = (TextView) convertView
				// .findViewById(R.id.classify);
				vh.marketPrice = (TextView) convertView
						.findViewById(R.id.marker_price);
				vh.distanceText = (TextView) convertView
						.findViewById(R.id.distance);
				// vh.merchant_point = (TextView) convertView
				// .findViewById(R.id.point);
				// vh.favorable_text = (TextView) convertView
				// .findViewById(R.id.btn_preference);
				convertView.setTag(vh);

			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			Merchant merchant = (Merchant) getItem(position);

			if (!StringUtils.isEmpty(merchant.getM_name())) {
				vh.nameText.setText(merchant.getM_name());
			}

			if (!StringUtils.isEmpty(merchant.getMdesc())) {
				vh.mdescText.setText("【" + merchant.getMdesc() + "】");
			}

			// if (!StringUtils.isEmpty(merchant.getAddress())) {
			// vh.classify.setText("");
			// }
			if (!StringUtils.isEmpty(merchant.getShop_price())) {
				vh.shopPriceText.setText("¥" + merchant.getShop_price());
			}

			if (!StringUtils.isEmpty(merchant.getMarket_price())) {
				vh.marketPrice.setText("¥" + merchant.getMarket_price());
			}
			// if (!StringUtils.isEmpty(merchant.getPoint())) {
			// vh.merchant_point.setText(merchant.getPoint());
			// }
			// 如果有优惠则显示优惠
			vh.distanceText.setText(StringUtils.getDistance(app.getLongitude(),
					app.getLatitude(), merchant.getLon() + "",
					merchant.getLat() + ""));

			return convertView;

		}

		class ViewHolder {
			TextView nameText;
			TextView mdescText;
			// TextView classify;
			TextView shopPriceText;
			TextView marketPrice;
			TextView distanceText;
			// TextView merchant_point;
			// TextView favorable_text;// 是否有优惠
		}
	}
}
