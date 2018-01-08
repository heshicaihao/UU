package com.huiyoumall.uu.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.huiyoumall.uu.adapter.FindFriendsAdapter;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.entity.Activities;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.widget.EmptyLayout;
import com.huiyoumall.uu.widget.SuperViewPagerAdapter;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 我的活动 界面
 * 
 * @author ASUS
 * 
 */
public class MyHuodongActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_huodong);
		findViewById();
		initView();
	}

	private TextView vTitle;
	private LinearLayout vMyPostEvents_view;
	private LinearLayout vTakePartIn_view;
	private LinearLayout vCompleteHuodong_view;

	private SuperViewPagerAdapter pagerAdapter;
	private ViewPager mViewPager;
	private View mMyPostEvents;
	private View mTakePartIn;
	private View mCompleteHuodong;
	private EmptyLayout my_act_empter_view;
	private ListView my_post_ListView;
	private ListView my_isJoin_ListView;
	private ListView my_isFinish_ListView;

	private List<Activities> act_my_Post_list;
	private List<Activities> act_my_Join_list;
	private List<Activities> act_my_Finish_list;

	AppContext app;

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		my_act_empter_view = (EmptyLayout) findViewById(R.id.my_act_empter_view);
		vMyPostEvents_view = (LinearLayout) findViewById(R.id.my_post_events_view);
		vTakePartIn_view = (LinearLayout) findViewById(R.id.take_part_in_view);
		vCompleteHuodong_view = (LinearLayout) findViewById(R.id.complete_huodong_view);
		mViewPager = (ViewPager) findViewById(R.id.my_act_viewpager);
	}

	@Override
	public void initView() {
		app = (AppContext) getApplicationContext();
		vTitle.setText("我的活动");

		vMyPostEvents_view.setSelected(true);
		vTakePartIn_view.setSelected(false);
		vCompleteHuodong_view.setSelected(false);

		mMyPostEvents = LayoutInflater.from(this).inflate(
				R.layout.fragment_my_post_events_huodong, null);
		mTakePartIn = LayoutInflater.from(this).inflate(
				R.layout.fragment_take_part_in_huodong, null);
		mCompleteHuodong = LayoutInflater.from(this).inflate(
				R.layout.fragment_complete_huodong, null);

		my_post_ListView = (ListView) mMyPostEvents
				.findViewById(R.id.my_post_ListView);
		my_isJoin_ListView = (ListView) mTakePartIn
				.findViewById(R.id.my_isjoin_ListView);
		my_isFinish_ListView = (ListView) mCompleteHuodong
				.findViewById(R.id.my_isfinish_ListView);

		my_post_ListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Activities act = act_my_Post_list.get(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable(HuodongDetailsActivity.HD_DETAILS, act);
				HelperUi.startActivity(MyHuodongActivity.this,
						HuodongDetailsActivity.class, bundle);
			}
		});
		my_isJoin_ListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Activities act = act_my_Join_list.get(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable(HuodongDetailsActivity.HD_DETAILS, act);
				HelperUi.startActivity(MyHuodongActivity.this,
						HuodongDetailsActivity.class, bundle);
			}
		});
		my_isFinish_ListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Activities act = act_my_Finish_list.get(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable(HuodongDetailsActivity.HD_DETAILS, act);
				HelperUi.startActivity(MyHuodongActivity.this,
						HuodongDetailsActivity.class, bundle);
			}
		});

		final ArrayList<View> views = new ArrayList<View>();
		views.add(mMyPostEvents);
		views.add(mTakePartIn);
		views.add(mCompleteHuodong);
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

		vMyPostEvents_view.setOnClickListener(this);
		vTakePartIn_view.setOnClickListener(this);
		vCompleteHuodong_view.setOnClickListener(this);
		loadData();
	}

	private void loadData() {
		int member_id = app.member_id;
		member_id = 3;
		int page = 1;
		String latlon = app.getLongitude() + "-" + app.getLatitude();
		latlon = "113.952135-22.551719";
		UURemoteApi.loadMyPostAct(member_id, page, latlon,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						my_act_empter_view.setVisibility(View.GONE);
						String result = new String(arg2);
						try {
							act_my_Post_list = Activities.parseList(result);
							if (act_my_Post_list != null
									&& act_my_Post_list.size() > 0) {
								FindFriendsAdapter mAdapter = new FindFriendsAdapter(
										MyHuodongActivity.this,
										act_my_Post_list);
								my_post_ListView.setAdapter(mAdapter);
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
		UURemoteApi.loadMyJoinAct(member_id, page, latlon,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						my_act_empter_view.setVisibility(View.GONE);
						String result = new String(arg2);
						try {
							act_my_Join_list = Activities.parseList(result);
							if (act_my_Join_list != null
									&& act_my_Join_list.size() > 0) {
								FindFriendsAdapter mAdapter = new FindFriendsAdapter(
										MyHuodongActivity.this,
										act_my_Join_list);
								my_isJoin_ListView.setAdapter(mAdapter);
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
		UURemoteApi.loadMyFinishAct(member_id, page, latlon,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						my_act_empter_view.setVisibility(View.GONE);
						String result = new String(arg2);
						try {
							act_my_Finish_list = Activities.parseList(result);
							if (act_my_Finish_list != null
									&& act_my_Finish_list.size() > 0) {
								FindFriendsAdapter mAdapter = new FindFriendsAdapter(
										MyHuodongActivity.this,
										act_my_Finish_list);
								my_isFinish_ListView.setAdapter(mAdapter);
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
			vMyPostEvents_view.setSelected(true);
			vTakePartIn_view.setSelected(false);
			vCompleteHuodong_view.setSelected(false);
			break;
		case 1:
			vMyPostEvents_view.setSelected(false);
			vTakePartIn_view.setSelected(true);
			vCompleteHuodong_view.setSelected(false);
			break;
		case 2:
			vMyPostEvents_view.setSelected(false);
			vTakePartIn_view.setSelected(false);
			vCompleteHuodong_view.setSelected(true);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.my_post_events_view:
			change(0);
			mViewPager.setCurrentItem(0);
			break;
		case R.id.take_part_in_view:
			change(1);
			mViewPager.setCurrentItem(1);
			break;
		case R.id.complete_huodong_view:
			change(2);
			mViewPager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}
}
