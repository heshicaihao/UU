package com.huiyoumall.uu.frament;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.GlobalParams;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.activity.HuodongDetailsActivity;
import com.huiyoumall.uu.activity.MerchantDetailsActivity;
import com.huiyoumall.uu.activity.MerchantListActivity;
import com.huiyoumall.uu.ad.AdImagePagerAdapter;
import com.huiyoumall.uu.ad.CircleFlowIndicator;
import com.huiyoumall.uu.ad.ViewFlow;
import com.huiyoumall.uu.adapter.FindFriendsAdapter;
import com.huiyoumall.uu.adapter.PopularMerchantAdapter;
import com.huiyoumall.uu.entity.Act;
import com.huiyoumall.uu.entity.Activities;
import com.huiyoumall.uu.entity.HomeDataPk;
import com.huiyoumall.uu.entity.Merchant;
import com.huiyoumall.uu.entity.Sport;
import com.huiyoumall.uu.remote.NetWorkHelper;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.util.ACache;
import com.huiyoumall.uu.widget.EmptyLayout;
import com.huiyoumall.uu.widget.GridViewForScollView;
import com.huiyoumall.uu.widget.ListViewForScrollView;
import com.huiyoumall.uu.widget.MyScrollView;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 首页（预定界面）
 * 
 * @author ASUS
 * 
 */
public class BookActivity extends Activity implements OnClickListener {

	private int[] icons = { R.drawable.ic_bird_view, R.drawable.ic_tennis_view,
			R.drawable.ic_basketball_view, R.drawable.ic_football_view,
			R.drawable.ic_table_tennis_view, R.drawable.ic_fitness_view,
			R.drawable.ic_swimming_view, R.drawable.ic_other_view };

	private ViewFlow mViewFlow;
	private CircleFlowIndicator vFlowIndicator;
	private GridViewForScollView vGridView;
	public static MyScrollView vBook_scrollview;
	private LinearLayout vHot_view;
	private LinearLayout vHuodong_view;
	private EmptyLayout emptyView;
	private ListViewForScrollView vHot_list;
	private ListViewForScrollView vNear_list;
	private PopularMerchantAdapter mHotMerchantAdapter;
	private HomeSportsAdapter homeSportAdapter;
	private List<Merchant> mMerchants = new ArrayList<Merchant>();
	public static List<Sport> mSports = new ArrayList<Sport>();
	public static List<Act> maAds = new ArrayList<Act>();

	// private View mPopularMerchant;
	// private View mNearActivity;
	AppContext app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_home);
		findViewId();
		initView();
		loadData();
		loadNearAct();
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("");
	}

	private void findViewId() {
		mViewFlow = (ViewFlow) findViewById(R.id.viewflow);
		vFlowIndicator = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		vGridView = (GridViewForScollView) findViewById(R.id.gv_home);
		vBook_scrollview = (MyScrollView) findViewById(R.id.book_scrollview);
		vBook_scrollview.smoothScrollTo(0, 20);
		vHot_view = (LinearLayout) findViewById(R.id.hot_view);
		vHuodong_view = (LinearLayout) findViewById(R.id.huodong_view);
		emptyView = (EmptyLayout) findViewById(R.id.home_empter_view);
		vHot_list = (ListViewForScrollView) findViewById(R.id.hot_list);

		// mPopularMerchant = LayoutInflater.from(this).inflate(
		// R.layout.view_pupular_merchant, null);
		// mNearActivity = LayoutInflater.from(this).inflate(
		// R.layout.view_near_activity, null);

		vHot_list = (ListViewForScrollView) findViewById(R.id.hot_list);
		vNear_list = (ListViewForScrollView) findViewById(R.id.near_list);
	}

	private void initView() {
		app = (AppContext) getApplicationContext();
		vHot_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Merchant merchant = mMerchants.get(position);
				Bundle bundle = new Bundle();
				bundle.putString(MerchantDetailsActivity.M_ID,
						merchant.getMid());
				HelperUi.startActivity(BookActivity.this,
						MerchantDetailsActivity.class, bundle);
			}
		});

		homeSportAdapter = new HomeSportsAdapter();
		vGridView.setAdapter(homeSportAdapter);
		vGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(BookActivity.this,
						MerchantListActivity.class);
				intent.putExtra(MerchantListActivity.SPORT,
						mSports.get(position));
				intent.putExtra(MerchantListActivity.SPORT_LIST,
						(Serializable) mSports);
				startActivity(intent);
			}
		});

		emptyView.setOnLayoutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadData();
			}
		});
		vHot_view.setOnClickListener(this);
		vHuodong_view.setOnClickListener(this);

		vHot_view.setSelected(true);
		vHot_view.setEnabled(false);
		vHuodong_view.setSelected(false);
		vHuodong_view.setEnabled(true);
	}

	private void initBanner() {
		mViewFlow.setAdapter(new AdImagePagerAdapter(BookActivity.this, maAds)
				.setInfiniteLoop(true));
		mViewFlow.setmSideBuffer(maAds.size()); // 实际图片张数，
		mViewFlow.setFlowIndicator(vFlowIndicator);
		mViewFlow.setTimeSpan(3000);
		mViewFlow.setSelection(maAds.size() * 1000); // 设置初始位置
		mViewFlow.startAutoFlowTimer(); // 启动自动播放
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			HomeDataPk pks = null;
			if (msg.what == 1) {
				pks = (HomeDataPk) msg.obj;
			} else if (msg.what == -1) {
				pks = (HomeDataPk) getCacheSerializable("indexData");
			}
			if (pks != null) {
				emptyView.setVisibility(View.GONE);
				vBook_scrollview.setVisibility(View.VISIBLE);
				// 存入缓存
				setCacheSerializable("indexData", pks);
				// 广告
				if (pks.getAct() != null && pks.getAct().size() > 0) {
					maAds = pks.getAct();
					initBanner();
				}

				// }
				// 场馆类别
				if (pks.getSports() != null && pks.getSports().size() > 0) {
					mSports = pks.getSports();
					homeSportAdapter.setDatas(mSports);
				}

				// 热门场馆
				if (pks.getMerchant() != null && pks.getMerchant().size() > 0) {
					mMerchants = pks.getMerchant();
					mHotMerchantAdapter = new PopularMerchantAdapter(
							BookActivity.this, mMerchants);
					vHot_list.setAdapter(mHotMerchantAdapter);
					// setListViewHeightBasedOnChildren(vHot_list,
					// mHotMerchantAdapter);

					// ArrayList<View> views = new ArrayList<View>();
					// views.add(mPopularMerchant);
					// views.add(mNearActivity);
					// pagerAdapter = new SuperViewPagerAdapter(views);
					// mViewPager.setAdapter(pagerAdapter);
				}
			} else {
				emptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
			}
		};
	};

	private void loadData() {
		vBook_scrollview.setVisibility(View.GONE);
		if (!NetWorkHelper.isNetworkAvailable(this)) {
			emptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
		} else {
			emptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
			final Message msg = Message.obtain();
			msg.arg1 = 1;
			UURemoteApi.index(new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

					try {
						String result = new String(arg2, "UTF-8");
						HomeDataPk pks = HomeDataPk.parse(result);
						msg.what = 1;
						msg.obj = pks;
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
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					msg.what = -1;
					emptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
					mHandler.sendMessage(msg);
				}
			});
		}
	}

	// 加载附近活动
	private void loadNearAct() {

		String lonlat = app.getLongitude() + "-" + app.getLatitude();
		lonlat = "113.952135-22.551719";
		UURemoteApi.loadNearAct(lonlat, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String result = new String(arg2);
				try {
					final List<Activities> activities = Activities
							.parseList(result);
					if (activities != null && activities.size() > 0) {
						FindFriendsAdapter madapter = new FindFriendsAdapter(
								BookActivity.this, activities);
						vNear_list.setAdapter(madapter);
						vNear_list
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										Activities act = activities
												.get(position);
										Bundle bundle = new Bundle();
										bundle.putSerializable(
												HuodongDetailsActivity.HD_DETAILS,
												act);
										HelperUi.startActivity(
												BookActivity.this,
												HuodongDetailsActivity.class,
												bundle);
									}
								});
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

	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView,
			PopularMerchantAdapter adapter) {
		if (listView == null)
			return;

		if (adapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View listItem = adapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (adapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	private class HomeSportsAdapter extends BaseAdapter {

		private List<Sport> mSports;

		public HomeSportsAdapter() {
			mSports = new ArrayList<Sport>();
		}

		public void setDatas(List<Sport> list) {
			this.mSports = list;
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mSports.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = View.inflate(GlobalParams.CONTEXT,
						R.layout.item_home_grid, null);
			} else {
				view = convertView;
			}

			Sport sport = (Sport) getItem(position);
			ImageView iv = (ImageView) view.findViewById(R.id.changguan_icon);
			TextView tv = (TextView) view.findViewById(R.id.changguan_name);
			tv.setText(sport.getSport_name());
			iv.setImageResource(icons[position]);
			tv.setTag(sport);
			return view;
		}

		@Override
		public Object getItem(int position) {
			return mSports.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.hot_view:
			vHot_view.setSelected(true);
			vHot_view.setEnabled(false);
			vHuodong_view.setSelected(false);
			vHuodong_view.setEnabled(true);
			// mViewPager.setCurrentItem(0);
			vHot_list.setVisibility(View.VISIBLE);
			vNear_list.setVisibility(View.GONE);
			vBook_scrollview.smoothScrollTo(0, 20);
			break;
		case R.id.huodong_view:
			vHot_view.setSelected(false);
			vHot_view.setEnabled(true);
			vHuodong_view.setSelected(true);
			vHuodong_view.setEnabled(false);
			// mViewPager.setCurrentItem(1);
			vHot_list.setVisibility(View.GONE);
			vNear_list.setVisibility(View.VISIBLE);
			vBook_scrollview.smoothScrollTo(0, 20);

		default:
			break;
		}
	}

	/**
	 * 获取Serializable缓存
	 * 
	 * @param key
	 * @return
	 */
	public Object getCacheSerializable(String key) {
		return ACache.get(this).getAsObject(key);
	}

	/**
	 * 缓存Serializable数据
	 * 
	 * @param key
	 * @param value
	 */
	public void setCacheSerializable(String key, Serializable value) {
		ACache.get(this).put(key, value);
	}
}
