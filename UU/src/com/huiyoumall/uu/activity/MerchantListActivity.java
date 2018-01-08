package com.huiyoumall.uu.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.GlobalParams;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.adapter.SportsListAdapter;
import com.huiyoumall.uu.base.BaseListActivity;
import com.huiyoumall.uu.db.DataHelper;
import com.huiyoumall.uu.entity.CityItem;
import com.huiyoumall.uu.entity.Merchant;
import com.huiyoumall.uu.entity.Sport;
import com.huiyoumall.uu.remote.NetWorkHelper;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.view.pullview.AbPullToRefreshView;
import com.huiyoumall.uu.widget.EmptyLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 场馆列表 界面
 * 
 * @author ASUS
 * 
 */
public class MerchantListActivity extends BaseListActivity implements
		OnItemClickListener {
	public static String SPORT = "sport";
	public static String SPORT_LIST = "sport_list";
	public static String[] sorts = { "默认排序", "可预订", "从近到远", "价格最低", "人气最好" };
	public static String[] areas;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSports = (List<Sport>) getIntent().getSerializableExtra(SPORT_LIST);
		mSport = (Sport) getIntent().getSerializableExtra(SPORT);
		setContentView(R.layout.activity_merchant_list);
		findViewById();
		initView();
		// 初始化弹出框
		initData();
		// 加载网络数据
		firstLoad();
	}

	private TextView vTitle;
	private TextView vTextLeft, vTextMiddle, vTextRight;
	private LinearLayout vTextLeft_container, vTextMiddle_container,
			vTextRight_container;
	private ImageView leftImg;
	private ImageView middleImg;
	private ImageView rightImg;
	// private ListView vListView1, vListView2, vListView3;
	private ListView vmainListView;

	protected AbPullToRefreshView mAbPullToRefreshView;
	protected EmptyLayout emptyView;

	private SportsListAdapter msPortsAdapter;
	private AppContext app;
	private PopupWindow mPopupWindow;
	private SectionAdapter mPopAdapter;
	private ListView view_list;
	private LinearLayout vMerchant_list_view;
	private DataHelper dHelper;
	private List<Sport> mSports;
	private List<Merchant> mMerchants = new ArrayList<Merchant>();
	private Sport mSport;
	private String mArea;// 选择的区域
	private String mSort;
	private List<String> mSprotStrs;

	public void findViewById() {
		vMerchant_list_view = (LinearLayout) findViewById(R.id.merchant_list_view);
		vTitle = (TextView) findViewById(R.id.title);
		vTextLeft = (TextView) findViewById(R.id.sectionleft);
		vTextMiddle = (TextView) findViewById(R.id.sectionmiddle);
		vTextRight = (TextView) findViewById(R.id.sectionright);

		vTextLeft_container = (LinearLayout) findViewById(R.id.sectionleft_container);
		vTextMiddle_container = (LinearLayout) findViewById(R.id.sectionmiddle_container);
		vTextRight_container = (LinearLayout) findViewById(R.id.sectionright_container);

		leftImg = (ImageView) findViewById(R.id.mark1);
		middleImg = (ImageView) findViewById(R.id.mark2);
		rightImg = (ImageView) findViewById(R.id.mark3);

		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		emptyView = (EmptyLayout) findViewById(R.id.empter_view);

		vmainListView = (ListView) findViewById(R.id.mListView);
		MyOnclickListener onclickListener = new MyOnclickListener();
		// vTextLeft.setOnClickListener(onclickListener);
		// vTextMiddle.setOnClickListener(onclickListener);
		// vTextRight.setOnClickListener(onclickListener);

		vTextLeft_container.setOnClickListener(onclickListener);
		vTextMiddle_container.setOnClickListener(onclickListener);
		vTextRight_container.setOnClickListener(onclickListener);
	}

	public void initView() {
		app = (AppContext) getApplicationContext();
		vTitle.setText("场馆列表");
		vTextLeft.setText("运动类型");
		vTextMiddle.setText("所有区域");
		vTextRight.setText("默认排序");

		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);

		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));

		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				GlobalParams.WIN_HEIGHT - 100));
		emptyView.setOnLayoutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				firstLoad();
			}
		});
	}

	private void showSectionPop(int width, int secindex) {
		View layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.view_popup_category, null);
		view_list = (ListView) layout.findViewById(R.id.view_list);
		view_list.setOnItemClickListener(this);
		mPopupWindow = new PopupWindow(layout, width,
				LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setWindowLayoutMode(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		ColorDrawable dw = new ColorDrawable(R.color.white);
		mPopupWindow.setBackgroundDrawable(dw);
		mPopupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
		mPopupWindow.setOutsideTouchable(true);

		if (secindex == 0) {
			mPopAdapter = new SectionAdapter(this, mSprotStrs,
					selectPos[secindex], secindex);
		} else if (secindex == 1) {
			mPopAdapter = new SectionAdapter(this, areas, selectPos[secindex],
					secindex);
		} else if (secindex == 2) {
			mPopAdapter = new SectionAdapter(this, sorts, selectPos[secindex],
					secindex);
		}

		view_list.setAdapter(mPopAdapter);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				selectSecCheck(4);
			}
		});
		mPopupWindow.showAsDropDown(vTextLeft, 0, 0);
		mPopupWindow.update();
	}

	protected void selectSecCheck(int index) {
		vTextLeft.setTextColor(0xff696969);
		vTextMiddle.setTextColor(0xff696969);
		vTextRight.setTextColor(0xff696969);
		leftImg.setImageResource(R.drawable.ic_pull_down_press);
		middleImg.setImageResource(R.drawable.ic_pull_down_press);
		rightImg.setImageResource(R.drawable.ic_pull_down_press);
		switch (index) {
		case 0:
			vTextLeft.setTextColor(getResources().getColor(R.color.main_color));
			leftImg.setImageResource(R.drawable.ic_pull_up_press);
			break;
		case 1:
			vTextMiddle.setTextColor(getResources()
					.getColor(R.color.main_color));
			middleImg.setImageResource(R.drawable.ic_pull_up_press);
			break;
		case 2:
			vTextRight
					.setTextColor(getResources().getColor(R.color.main_color));
			rightImg.setImageResource(R.drawable.ic_pull_up_press);
			break;
		}
	}

	private void initData() {
		// 初始化弹出框
		dHelper = new DataHelper(this);
		if (app.getSelectCity() != null) {
			CityItem cityItem = dHelper.queryEntity("name='"
					+ app.getSelectCity() + "'");
			List<CityItem> list = dHelper.query("parent=" + cityItem.id);
			if (list != null && list.size() > 0) {
				areas = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					areas[i] = list.get(i).name;
				}
			} else {
				areas = new String[] {};
			}
		} else {
			areas = new String[] {};
		}

		if (mSports != null && mSports.size() > 0) {
			mSprotStrs = new ArrayList<String>();
			for (int i = 0; i < mSports.size(); i++) {
				mSprotStrs.add(i, mSports.get(i).getSport_name());
			}
		}

		msPortsAdapter = new SportsListAdapter(this, mMerchants);
		vmainListView.setAdapter(msPortsAdapter);
	}

	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			if (msg.arg1 == FIRST_LOAD) {
				if (msg.what == LOAD_SUCCESS) {
					List<Merchant> mList = (List<Merchant>) msg.obj;
					if (mList != null) {
						emptyView.setVisibility(View.GONE);
						mAbPullToRefreshView.setVisibility(View.VISIBLE);
						// //存入缓存
						// setCacheSerializable("merchantList" + currentPagte,
						// mList);
						mMerchants = mList;
						msPortsAdapter.setData(mMerchants);

					} else {
						emptyView.setErrorType(EmptyLayout.NODATA);
					}
				} else if (msg.what == LOAD_ERROR) {
					emptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
				}
			}
			// 脚部
			if (msg.arg1 == LOAD_MORE_DATA) {
				mAbPullToRefreshView.onFooterLoadFinish();
				if (msg.what == LOAD_SUCCESS) {
					List<Merchant> mList = (List<Merchant>) msg.obj;
					if (mList != null) {
						if (mList.size() != 0) {
							mMerchants.addAll(mList);
							msPortsAdapter.setData(mMerchants);
						}
					}
				}
			}
			// 头部刷新
			if (msg.arg1 == REFRESH_DATA) {
				mAbPullToRefreshView.onHeaderRefreshFinish();
				if (msg.what == LOAD_SUCCESS) {
					mMerchants.clear();
					List<Merchant> mList = (List<Merchant>) msg.obj;
					if (mList != null) {
						if (mList.size() != 0) {
							mMerchants.addAll(mList);
							msPortsAdapter.setData(mMerchants);
						}
					}
				}
			}
		};
	};

	// 首次进入界面加载
	@Override
	protected void firstLoad() {
		final Message msg = Message.obtain();
		msg.arg1 = FIRST_LOAD;
		mAbPullToRefreshView.setVisibility(View.GONE);
		if (!NetWorkHelper.isNetworkAvailable(this)) {
			emptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
		} else {
			emptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
			UURemoteApi.loadSports(mSport.getSport_id(), 2,
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

	// 上滑加载更多
	@Override
	protected void loadMore() {
		currentPagte++;
		final Message msg = Message.obtain();
		msg.arg1 = LOAD_MORE_DATA;
		UURemoteApi.loadSports(mSport.getSport_id(), 2,
				new AsyncHttpResponseHandler() {

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

	// 下拉刷新
	@Override
	protected void refreshData() {
		currentPagte = 1;
		final Message msg = Message.obtain();
		msg.arg1 = REFRESH_DATA;
		UURemoteApi.loadSports(mSport.getSport_id(), 2,
				new AsyncHttpResponseHandler() {

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

	private class MyOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int mId = v.getId();
			switch (mId) {
			case R.id.sectionleft_container:
				secindex = 0;
				selectSecCheck(secindex);
				showSectionPop(vMerchant_list_view.getWidth(), secindex);
				break;
			case R.id.sectionmiddle_container:
				secindex = 1;
				selectSecCheck(secindex);
				showSectionPop(vMerchant_list_view.getWidth(), secindex);
				break;
			case R.id.sectionright_container:
				secindex = 2;
				selectSecCheck(secindex);
				showSectionPop(vMerchant_list_view.getWidth(), secindex);
				break;
			default:
				break;
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MerchantListActivity.this.finish();
		}
		return false;
	}

	private int selectPos[] = new int[] { 0, 0, 0 };
	private int secindex = 0;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		selectPos[secindex] = position;
		mPopAdapter.notifyDataSetChanged();
		switch (secindex) {
		case 0:
			mPopAdapter = new SectionAdapter(this, mSprotStrs, position,
					secindex);
			view_list.setAdapter(mPopAdapter);
			vTextLeft.setText(mSprotStrs.get(position));

			// 加载网络数据
			firstLoad();
			break;
		case 1:
			mPopAdapter = new SectionAdapter(this, areas, position, secindex);
			view_list.setAdapter(mPopAdapter);
			vTextMiddle.setText(areas[position]);
			mArea = areas[position];
			// 加载网络数据
			firstLoad();
			break;
		case 2:
			mPopAdapter = new SectionAdapter(this, sorts, position, secindex);
			view_list.setAdapter(mPopAdapter);
			vTextRight.setText(sorts[position]);
			mSort = sorts[position];
			// 加载网络数据
			firstLoad();
			break;
		}
		mPopupWindow.dismiss();
	}

	public class SectionAdapter extends BaseAdapter {

		private Context context;
		private List<String> itemList;
		private int selectid;
		private int secindex;

		public SectionAdapter(Context context, List<String> item, int selectid,
				int secindex) {
			this.context = context;
			this.itemList = item;
			this.selectid = selectid;
			this.secindex = secindex;
		}

		public SectionAdapter(Context context, String[] items, int selectid,
				int secindex) {
			this.context = context;
			this.selectid = selectid;
			this.secindex = secindex;
			itemList = new ArrayList<String>();
			for (int i = 0; i < items.length; i++) {
				itemList.add(i, items[i]);
			}
		}

		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DataList data = null;
			if (convertView == null) {
				data = new DataList();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.view_secect_huodong_item, null);
				data.mText = (TextView) convertView.findViewById(R.id.name);
				data.mImage = (ImageView) convertView
						.findViewById(R.id.checkimg);
				convertView.setTag(data);
			} else {
				data = (DataList) convertView.getTag();
			}
			data.mText.setText(itemList.get(position));
			if (selectid == position) {
				data.mImage.setVisibility(View.VISIBLE);
				convertView.setBackgroundResource(R.color.tab_view_press);
				data.mText.setTextColor(getApplication().getResources()
						.getColor(R.color.main_color));//
				// data.mText.setTextColor(0xff1398a7);
			} else {
				data.mImage.setVisibility(View.INVISIBLE);
				convertView.setBackgroundResource(R.color.white);
				data.mText.setTextColor(Color.BLACK);
			}
			if (secindex == 2) {
				//
			}
			return convertView;
		}

		public void freshdata() {
			this.notifyDataSetChanged();
		}

		private class DataList {
			public TextView mText;
			public ImageView mImage;
		}
	}

}
