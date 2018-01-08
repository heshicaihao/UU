package com.huiyoumall.uu.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.GlobalParams;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.adapter.PopularMerchantAdapter;
import com.huiyoumall.uu.base.BaseListActivity;
import com.huiyoumall.uu.entity.Merchant;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.view.pullview.AbPullToRefreshView;
import com.huiyoumall.uu.widget.EmptyLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 我的收藏界面
 * 
 * @author ASUS
 * 
 */
public class MyCollectActivity extends BaseListActivity {

	private TextView vTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collect);
		findViewById();
		initView();
		firstLoad();
	}

	private EmptyLayout collect_empter_view;
	private AbPullToRefreshView vPullToRefreshView;
	private ListView my_collect_list;
	private AppContext app;
	private List<Merchant> collects = new ArrayList<Merchant>();
	PopularMerchantAdapter mCollectAdapter;

	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		collect_empter_view = (EmptyLayout) findViewById(R.id.collect_empter_view);
		vPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		my_collect_list = (ListView) findViewById(R.id.my_collect_list);
	}

	public void initView() {
		app = (AppContext) getApplicationContext();
		vTitle.setText("我的收藏");
		vPullToRefreshView.setOnHeaderRefreshListener(this);
		vPullToRefreshView.setOnFooterLoadListener(this);

		// 设置进度条的样式
		vPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		vPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));

		collect_empter_view.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, GlobalParams.WIN_HEIGHT - 100));
		collect_empter_view.setOnLayoutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				firstLoad();
			}
		});
		mCollectAdapter = new PopularMerchantAdapter(this, collects);
		my_collect_list.setAdapter(mCollectAdapter);
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

	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			if (msg.arg1 == FIRST_LOAD) {
				if (msg.what == LOAD_SUCCESS) {
					collects = (List<Merchant>) msg.obj;
					if (collects != null && collects.size() > 0) {
						collect_empter_view.setVisibility(View.GONE);
						mCollectAdapter.setMerChants(collects);
					} else {
						collect_empter_view.setErrorType(EmptyLayout.NODATA);
					}
				} else if (msg.what == LOAD_ERROR) {
					collect_empter_view.setErrorType(EmptyLayout.NETWORK_ERROR);
				}
			}
			// 脚部
			if (msg.arg1 == LOAD_MORE_DATA) {
				vPullToRefreshView.onFooterLoadFinish();
				if (msg.what == LOAD_SUCCESS) {
					List<Merchant> mList = (List<Merchant>) msg.obj;
					if (mList != null) {
						if (mList.size() != 0) {
							collects.addAll(mList);
							mCollectAdapter.setMerChants(collects);
						}
					}
				}
			}
			// 头部刷新
			if (msg.arg1 == REFRESH_DATA) {
				vPullToRefreshView.onHeaderRefreshFinish();
				if (msg.what == LOAD_SUCCESS) {
					collects.clear();
					List<Merchant> mList = (List<Merchant>) msg.obj;
					if (mList != null) {
						if (mList.size() != 0) {
							collects.addAll(mList);
							mCollectAdapter.setMerChants(collects);
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
		int memberid = app.member_id;
		memberid = 3;
		String latlon = app.getLongitude() + "-" + app.getLatitude();
		latlon = "113.952135-22.551719";
		UURemoteApi.loadMyCollect(memberid, latlon, currentPagte,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						try {
							String result = new String(arg2, "UTF-8");
							msg.what = LOAD_SUCCESS;
							msg.obj = Merchant.getMyCollectMerchants(result);
						} catch (JSONException e) {
							msg.what = LOAD_ERROR;
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

	@Override
	protected void loadMore() {

		int memberid = app.member_id;
		memberid = 3;
		String latlon = app.getLongitude() + "-" + app.getLatitude();
		latlon = "113.952135-22.551719";

		currentPagte++;
		final Message msg = Message.obtain();
		msg.arg1 = LOAD_MORE_DATA;
		UURemoteApi.loadMyCollect(memberid, latlon, currentPagte,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

						try {
							msg.obj = Merchant
									.getMyCollectMerchants(new String(arg2));
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

		int memberid = app.member_id;
		memberid = 3;
		String latlon = app.getLongitude() + "-" + app.getLatitude();
		latlon = "113.952135-22.551719";

		currentPagte = 1;
		final Message msg = Message.obtain();
		msg.arg1 = REFRESH_DATA;
		UURemoteApi.loadMyCollect(memberid, latlon, currentPagte,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

						try {
							msg.obj = Merchant
									.getMyCollectMerchants(new String(arg2));
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

}
