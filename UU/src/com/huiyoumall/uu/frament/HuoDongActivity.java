package com.huiyoumall.uu.frament;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huiyoumall.uu.AppConfig;
import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.activity.HuodongDetailsActivity;
import com.huiyoumall.uu.activity.LoginActivity;
import com.huiyoumall.uu.activity.MerchantDetailsActivity;
import com.huiyoumall.uu.activity.MyHuodongActivity;
import com.huiyoumall.uu.activity.PostEventsActivity;
import com.huiyoumall.uu.adapter.FindFriendsAdapter;
import com.huiyoumall.uu.common.Options;
import com.huiyoumall.uu.entity.Act;
import com.huiyoumall.uu.entity.Activities;
import com.huiyoumall.uu.entity.Sport;
import com.huiyoumall.uu.remote.NetWorkHelper;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.util.DateUtil;
import com.huiyoumall.uu.widget.EmptyLayout;
import com.huiyoumall.uu.widget.SuperViewPagerAdapter;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 活动页
 * 
 * @author ASUS
 * 
 */
public class HuoDongActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.fragment_huodong);
		initView();
	}

	private LinearLayout vHotHuodong_view;
	private LinearLayout vFriendHuodong_view;

	private LinearLayout vPostEvents;
	private LinearLayout vMyHuodong;
	private LinearLayout vMain_view;

	private static RelativeLayout book_lin;

	private SuperViewPagerAdapter pagerAdapter;
	private ViewPager mViewPager;
	private View mPopularActivity;
	private View mFindFriend;
	private static EmptyLayout hot_empter_view;
	private static EmptyLayout friend_empter_view;

	private TextView project_btn;
	private TextView mtheme_btn;
	private TextView sort_btn;

	private LinearLayout vTextLeft_container, vTextMiddle_container,
			vTextRight_container;

	private ImageView proImg;
	private ImageView methImg;
	private ImageView sortImg;

	private static String mSportid;
	private static String mMethem;
	private static String mLatlon;
	private static String mSort;
	private static int page = 1;

	private int selectPos[] = new int[] { 0, 0, 0 };
	private int secindex = 0;
	private PopupWindow mPopupWindow;
	private SectionAdapter mPopAdapter;
	private static ListView hot_list_activity;
	private static ListView find_friends_activity;
	private static List<String> projects = new ArrayList<String>();
	private List<String> methemes = new ArrayList<String>();
	private List<String> sorts = new ArrayList<String>();
	private static HashMap<String, String> sportHm = new HashMap<String, String>();
	private HashMap<String, String> sortHm = new HashMap<String, String>();
	private static List<Activities> activities = new ArrayList<Activities>();

	private static List<Act> acts;
	private static HotActivityAdapter hotActivityAdapter;
	private static FindFriendsAdapter mFindFriendsAdapter;
	private static Context mContext;
	private static boolean isLoad = false;
	private AppContext app;

	/**
	 * sort=1 从近到远 sort=2 价格最高 sort=3价格最低 sort=4人齐最高
	 */

	private static Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				activities.clear();
				activities = (List<Activities>) msg.obj;
				if (activities != null && activities.size() > 0) {
					friend_empter_view.setVisibility(View.GONE);
					mFindFriendsAdapter = new FindFriendsAdapter(mContext,
							activities);
					find_friends_activity.setAdapter(mFindFriendsAdapter);
				} else {
					friend_empter_view.setErrorType(EmptyLayout.NODATA);
				}
			} else {
				friend_empter_view.setErrorType(EmptyLayout.NODATA);
			}
		};
	};

	public static void loadDate() {
		acts = BookActivity.maAds;
		List<Sport> sports = BookActivity.mSports;
		if (sports != null && sports.size() > 0) {
			for (int i = 0; i < sports.size(); i++) {
				projects.add(sports.get(i).getSport_name());
				sportHm.put(sports.get(i).getSport_name(), sports.get(i)
						.getSport_id() + "");
			}
		}

		if (acts != null && acts.size() > 0) {
			hotActivityAdapter.setData(BookActivity.maAds);
			hot_list_activity.setAdapter(hotActivityAdapter);
		} else {
			hot_list_activity.setVisibility(View.GONE);
			book_lin.setVisibility(View.VISIBLE);
		}
		if (!isLoad) {
			load();
		}
	}

	private static void load() {
		isLoad = true;
		if (!NetWorkHelper.isNetworkAvailable(mContext)) {
			friend_empter_view.setErrorType(EmptyLayout.NETWORK_ERROR);
		} else {
			friend_empter_view.setErrorType(EmptyLayout.NETWORK_LOADING);
			final Message msg = Message.obtain();
			mSportid = 20 + "";
			mSort = 2 + "";
			mLatlon = AppConfig.LONGITUDE + "-" + AppConfig.LATITUDE;
			mLatlon = "113.952135 - 22.551719";
			UURemoteApi.FindPartner(mSportid, mLatlon, mSort, mMethem, page,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {

							try {
								String result = new String(arg2, "UTF-8");
								msg.what = 1;
								msg.obj = Activities.parseList(result);
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
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							msg.what = -1;
							friend_empter_view
									.setErrorType(EmptyLayout.NETWORK_ERROR);
							mHandler.sendMessage(msg);
						}
					});
		}
	}

	private void initView() {
		app = (AppContext) getApplicationContext();
		vMyHuodong = (LinearLayout) findViewById(R.id.my_huodong_container);
		vPostEvents = (LinearLayout) findViewById(R.id.post_events_container);
		vMain_view = (LinearLayout) findViewById(R.id.main_view);
		vHotHuodong_view = (LinearLayout) findViewById(R.id.hot_huodong_view);
		vFriendHuodong_view = (LinearLayout) findViewById(R.id.friend_huodong_view);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);

		mPopularActivity = LayoutInflater.from(this).inflate(
				R.layout.view_pupular_activity, null);
		mFindFriend = LayoutInflater.from(this).inflate(
				R.layout.view_find_friends, null);
		vTextLeft_container = (LinearLayout) mFindFriend
				.findViewById(R.id.sectionleft_container);
		vTextMiddle_container = (LinearLayout) mFindFriend
				.findViewById(R.id.sectionmiddle_container);
		vTextRight_container = (LinearLayout) mFindFriend
				.findViewById(R.id.sectionright_container);

		hot_list_activity = (ListView) mPopularActivity
				.findViewById(R.id.hot_list_activity);
		book_lin = (RelativeLayout) mPopularActivity
				.findViewById(R.id.book_lin);

		find_friends_activity = (ListView) mFindFriend
				.findViewById(R.id.find_list_activity);

		friend_empter_view = (EmptyLayout) mFindFriend
				.findViewById(R.id.friend_empter_view);

		Button reserve_btn = (Button) mPopularActivity
				.findViewById(R.id.reserve_btn);

		reserve_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HelperUi.showToastLong(HuoDongActivity.this, "跳转到预订场馆页面");
			}
		});

		project_btn = (TextView) mFindFriend.findViewById(R.id.sectionleft);
		mtheme_btn = (TextView) mFindFriend.findViewById(R.id.sectionmiddle);
		sort_btn = (TextView) mFindFriend.findViewById(R.id.sectionright);

		proImg = (ImageView) mFindFriend.findViewById(R.id.mark1);
		methImg = (ImageView) mFindFriend.findViewById(R.id.mark2);
		sortImg = (ImageView) mFindFriend.findViewById(R.id.mark3);

		vTextLeft_container.setOnClickListener(this);
		vTextMiddle_container.setOnClickListener(this);
		vTextRight_container.setOnClickListener(this);
		// 赋值
		initDate();
		vHotHuodong_view.setSelected(true);
		vFriendHuodong_view.setSelected(false);
		vMyHuodong.setOnClickListener(this);
		vPostEvents.setOnClickListener(this);
		vHotHuodong_view.setOnClickListener(this);
		vFriendHuodong_view.setOnClickListener(this);

		ArrayList<View> views = new ArrayList<View>();
		views.add(mPopularActivity);
		views.add(mFindFriend);
		pagerAdapter = new SuperViewPagerAdapter(views);
		mViewPager.setAdapter(pagerAdapter);

		hotActivityAdapter = new HotActivityAdapter();
		hot_list_activity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (acts != null && acts.size() > 0) {
					Act act = acts.get(position);
					Bundle bundle = new Bundle();
					bundle.putString(MerchantDetailsActivity.M_ID, act.getMid());
					HelperUi.startActivity(HuoDongActivity.this,
							MerchantDetailsActivity.class, bundle);
				}
			}
		});

		find_friends_activity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Activities act = activities.get(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable(HuodongDetailsActivity.HD_DETAILS, act);
				HelperUi.startActivity(HuoDongActivity.this,
						HuodongDetailsActivity.class, bundle);
			}
		});
	}

	private ListView view_list;

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
			mPopAdapter = new SectionAdapter(this, projects,
					selectPos[secindex], secindex);
		} else if (secindex == 1) {
			mPopAdapter = new SectionAdapter(this, methemes,
					selectPos[secindex], secindex);
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
		mPopupWindow.showAsDropDown(project_btn, 0, 0);
		mPopupWindow.update();
	}

	protected void selectSecCheck(int index) {
		project_btn.setTextColor(0xff696969);
		mtheme_btn.setTextColor(0xff696969);
		sort_btn.setTextColor(0xff696969);
		proImg.setImageResource(R.drawable.ic_pull_down_press);
		methImg.setImageResource(R.drawable.ic_pull_down_press);
		sortImg.setImageResource(R.drawable.ic_pull_down_press);
		switch (index) {
		case 0:
			project_btn.setTextColor(this.getResources().getColor(
					R.color.main_color));
			proImg.setImageResource(R.drawable.ic_pull_up_press);
			break;
		case 1:
			mtheme_btn.setTextColor(this.getResources().getColor(
					R.color.main_color));
			methImg.setImageResource(R.drawable.ic_pull_up_press);
			break;
		case 2:
			sort_btn.setTextColor(this.getResources().getColor(
					R.color.main_color));
			sortImg.setImageResource(R.drawable.ic_pull_up_press);
			break;
		}
	}

	private void initDate() {
		methemes.add("随便玩");
		methemes.add("培训");
		methemes.add("比赛");
		methemes.add("教教我");

		sorts.add("从近到远");
		sorts.add("价格最高");
		sorts.add("价格最低");
		sorts.add("人气最高");
		for (int i = 0; i < sorts.size(); i++) {
			sortHm.put(sorts.get(i), (i + 1) + "");
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.my_huodong_container:
			if (app.isLogin) {
				HelperUi.startActivity(this, MyHuodongActivity.class);
			} else {
				HelperUi.startActivity(this, LoginActivity.class);
			}

			break;
		case R.id.post_events_container:
			if (app.isLogin) {
				HelperUi.startActivity(this, PostEventsActivity.class);
			} else {
				HelperUi.startActivity(this, LoginActivity.class);
			}
			break;
		case R.id.hot_huodong_view:
			vHotHuodong_view.setSelected(true);
			vFriendHuodong_view.setSelected(false);
			mViewPager.setCurrentItem(0);
			break;
		case R.id.friend_huodong_view:
			vFriendHuodong_view.setSelected(true);
			vHotHuodong_view.setSelected(false);
			mViewPager.setCurrentItem(1);
			break;
		case R.id.sectionleft_container:
			secindex = 0;
			selectSecCheck(secindex);
			showSectionPop(vMain_view.getWidth(), secindex);
			break;
		case R.id.sectionmiddle_container:
			secindex = 1;
			selectSecCheck(secindex);
			showSectionPop(vMain_view.getWidth(), secindex);
			break;
		case R.id.sectionright_container:
			secindex = 2;
			selectSecCheck(secindex);
			showSectionPop(vMain_view.getWidth(), secindex);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		selectPos[secindex] = position;
		mPopAdapter.notifyDataSetChanged();
		switch (secindex) {
		case 0:
			mPopAdapter = new SectionAdapter(this, projects, position, secindex);
			view_list.setAdapter(mPopAdapter);
			if (position == 0) {
				// project_btn.setText(R.string.shop_list_section_discount);
			} else {
				project_btn.setText(projects.get(position));
			}
			mSportid = sportHm.get(projects.get(position));
			// 加载网络数据
			load();
			break;
		case 1:
			mPopAdapter = new SectionAdapter(this, methemes, position, secindex);
			view_list.setAdapter(mPopAdapter);
			if (position == 0) {
				// mtheme_btn.setText(R.string.shop_list_section_type);
			} else {
				mtheme_btn.setText(methemes.get(position));
			}
			mMethem = methemes.get(position);
			// 加载网络数据
			load();
			break;
		case 2:
			mPopAdapter = new SectionAdapter(this, sorts, position, secindex);
			view_list.setAdapter(mPopAdapter);
			if (position == 0) {
				// sort_btn.setText(R.string.shop_list_section_service);
			} else {
				sort_btn.setText(sorts.get(position));
			}
			// 加载网络数据
			mSort = sortHm.get(sorts.get(position));
			load();
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
						.getColor(R.color.main_color));
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

	class HotActivityAdapter extends BaseAdapter {

		private ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options;
		private List<Act> lists;
		private LayoutInflater mInflater;

		public HotActivityAdapter() {
			lists = new ArrayList<Act>();
			mInflater = LayoutInflater.from(HuoDongActivity.this);
			options = Options.getLargeListOptions();
			imageLoader.init(ImageLoaderConfiguration
					.createDefault(HuoDongActivity.this));
		}

		public void setData(List<Act> list_) {
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
			ViewHolder holder;
			if (convertView == null || convertView.getTag() == null) {
				holder = new ViewHolder();
				convertView = mInflater
						.inflate(R.layout.item_huodong_hot, null);
				holder.hot_houdong_image = (ImageView) convertView
						.findViewById(R.id.hot_houdong_image);
				holder.hot_houdong_name = (TextView) convertView
						.findViewById(R.id.hot_houdong_name);
				holder.hot_houdong_start_time = (TextView) convertView
						.findViewById(R.id.hot_houdong_date);
				holder.shop_price = (TextView) convertView
						.findViewById(R.id.shop_price);
				holder.market_price = (TextView) convertView
						.findViewById(R.id.market_price);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Act act = (Act) getItem(position);
			imageLoader.displayImage(act.getImg(), holder.hot_houdong_image,
					options);
			holder.hot_houdong_name.setText(act.getAct_name());
			holder.hot_houdong_start_time.setText(huodongDate(act.getDate(),
					act.getAct_time()));
			holder.shop_price.setText("¥" + act.getPrice());
			holder.market_price.setText("¥" + act.getT_price());
			return convertView;
		}

		private String huodongDate(String mDate, String mTime) {
			String week = DateUtil.getWeekNumber(mDate, "yyyy-MM-dd");
			String dates = "";
			String[] date = mDate.split("-");
			dates = date[1] + "." + date[2];
			return week + "  " + dates + "  " + mTime;
		}

		class ViewHolder {
			ImageView hot_houdong_image;
			TextView hot_houdong_name;
			TextView hot_houdong_start_time;
			TextView shop_price;
			TextView market_price;
		}
	}
}
