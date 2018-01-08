package com.huiyoumall.uu.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.huiyoumall.uu.GlobalParams;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.adapter.CommentAdapter;
import com.huiyoumall.uu.base.BaseListActivity;
import com.huiyoumall.uu.entity.Comment;
import com.huiyoumall.uu.remote.NetWorkHelper;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.view.pullview.AbPullToRefreshView;
import com.huiyoumall.uu.widget.EmptyLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 场馆评价 界面
 * 
 * @author ASUS
 * 
 */
public class CommentActivity extends BaseListActivity {

	public static String M_ID = "m_id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mid = getIntent().getStringExtra(M_ID);
		setContentView(R.layout.activity_merchant_discuss);
		ViewUtils.inject(CommentActivity.this);
		initView();
		firstLoad();
	}

	@ViewInject(R.id.title)
	private TextView vTitle;
	@ViewInject(R.id.mPullRefreshView)
	private AbPullToRefreshView mAbPullToRefreshView;
	@ViewInject(R.id.mListView)
	private ListView mListView;
	@ViewInject(R.id.empter_view)
	private EmptyLayout emptyView;
	private String mid;
	private List<Comment> mComments = new ArrayList<Comment>();
	private CommentAdapter maAdapter;

	private void initView() {
		vTitle.setText("场馆评价");
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
		maAdapter = new CommentAdapter(this, mComments);
		mListView.setAdapter(maAdapter);
	}

	@Override
	protected void firstLoad() {
		final Message msg = Message.obtain();
		msg.arg1 = FIRST_LOAD;
		mAbPullToRefreshView.setVisibility(View.GONE);
		if (!NetWorkHelper.isNetworkAvailable(this)) {
			emptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
		} else {
			emptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
			UURemoteApi.LoadComments(mid, currentPagte,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {

							try {
								String result = new String(arg2, "UTF-8");
								List<Comment> cList = Comment
										.getMerchants(result);
								msg.what = LOAD_SUCCESS;
								msg.obj = cList;
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
		UURemoteApi.getPayOrder("", currentPagte,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						try {
							String result = new String(arg2, "UTF-8");
							List<Comment> cList = Comment.getMerchants(result);
							msg.what = LOAD_SUCCESS;
							msg.obj = cList;
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
		UURemoteApi.getPayOrder("", currentPagte,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						try {
							String result = new String(arg2, "UTF-8");
							List<Comment> cList = Comment.getMerchants(result);
							msg.what = LOAD_SUCCESS;
							msg.obj = cList;
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

	private Handler mHandler = new Handler() {
		public synchronized void handleMessage(Message msg) {
			if (msg.arg1 == FIRST_LOAD) {
				if (msg.what == LOAD_SUCCESS) {
					List<Comment> mList = (List<Comment>) msg.obj;
					if (mList != null && mList.size() > 0) {
						emptyView.setVisibility(View.GONE);
						mAbPullToRefreshView.setVisibility(View.VISIBLE);
						mComments.addAll(mList);
						maAdapter.setData(mComments);
						// 存入缓存
						setCacheSerializable("comments" + currentPagte,
								(Serializable) mList);
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
					List<Comment> mList = (List<Comment>) msg.obj;
					if (mList != null && mList.size() > 0)
						mComments.addAll(mList);
					maAdapter.setData(mComments);
				}
			}
			// 头部
			if (msg.arg1 == REFRESH_DATA) {
				mAbPullToRefreshView.onHeaderRefreshFinish();
				if (msg.what == LOAD_SUCCESS) {
					mComments.clear();
					List<Comment> mList = (List<Comment>) msg.obj;
					if (mList != null && mList.size() > 0)
						mComments.addAll(mList);
					maAdapter.setData(mComments);
				}
			}
		};
	};

	public void activitybackview(View view) {
		mComments = null;
		super.activitybackview(view);
	};
}
