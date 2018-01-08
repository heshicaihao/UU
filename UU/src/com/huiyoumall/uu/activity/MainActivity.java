package com.huiyoumall.uu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.common.SystemBarTintManager;
import com.huiyoumall.uu.frament.BookActivity;
import com.huiyoumall.uu.frament.FindCoachActiviy;
import com.huiyoumall.uu.frament.HuoDongActivity;
import com.huiyoumall.uu.frament.MiActivity;
import com.huiyoumall.uu.widget.SelectCityPopWindlow;

/**
 * Main 界面
 * 
 * @author ASUS
 * 
 */
@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 设定状态栏的颜色，当版本大于4.4时起作用
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			// 此处可以重新指定状态栏颜色
			tintManager.setStatusBarTintResource(R.color.main_color);
		}
		findViewById();
		initView();
		// 创建状态栏的管理实例
		loction();
	}

	private AppContext app;
	private SelectCityPopWindlow mCityPopWindlow;

	/**
	 * 定位
	 */
	public static final String GPSLOCATION_BROADCAST_ACTION = "com.location.apis.gpslocationdemo.broadcast";

	private static final int CHANGE_CITY = 1;
	private LinearLayout vHome, vOther;
	private LinearLayout vSelect_city_show;
	private RelativeLayout vSearch_court;
	private TextView vSelect_city_txt;
	private ImageView vSearch_city_img;
	private TextView mTitle;
	private LinearLayout vWeb_map;

	private LinearLayout bookLayout, activityLayout, coachLayout, mineLayout;
	private ImageButton tab_book_img, tab_activity_img, tab_coach_img,
			tab_mine_img;
	private TextView tab_book_text, tab_activity_text, tab_coach_text,
			tab_mine_text;

	private List<View> list = new ArrayList<View>();// 相当于数据源
	private View view = null;
	private View view1 = null;
	private View view2 = null;
	private View view3 = null;
	private View view4 = null;
	private ViewPager mViewPager;
	private PagerAdapter pagerAdapter = null;

	public void findViewById() {
		mViewPager = (ViewPager) findViewById(R.id.main_viewpager);

		bookLayout = (LinearLayout) findViewById(R.id.tab_book);
		activityLayout = (LinearLayout) findViewById(R.id.tab_activity);
		coachLayout = (LinearLayout) findViewById(R.id.tab_coach);
		mineLayout = (LinearLayout) findViewById(R.id.tab_mine);

		tab_book_img = (ImageButton) findViewById(R.id.tab_book_img);
		tab_activity_img = (ImageButton) findViewById(R.id.tab_activity_img);
		tab_coach_img = (ImageButton) findViewById(R.id.tab_coach_img);
		tab_mine_img = (ImageButton) findViewById(R.id.tab_mine_img);

		tab_book_text = (TextView) findViewById(R.id.tab_book_text);
		tab_activity_text = (TextView) findViewById(R.id.tab_activity_text);
		tab_coach_text = (TextView) findViewById(R.id.tab_coach_text);
		tab_mine_text = (TextView) findViewById(R.id.tab_mine_text);

		vHome = (LinearLayout) findViewById(R.id.home);
		vOther = (LinearLayout) findViewById(R.id.other);
		vSelect_city_show = (LinearLayout) findViewById(R.id.select_city_show);
		vSelect_city_txt = (TextView) findViewById(R.id.select_city_txt);
		vSearch_city_img = (ImageView) findViewById(R.id.Search_city_img);
		vSearch_court = (RelativeLayout) findViewById(R.id.search_lin);
		vWeb_map = (LinearLayout) findViewById(R.id.web_map);
		mTitle = (TextView) findViewById(R.id.title);
		MyOnclickListener mOnclickListener = new MyOnclickListener();
		vSelect_city_show.setOnClickListener(mOnclickListener);
		vWeb_map.setOnClickListener(mOnclickListener);
		vSearch_court.setOnClickListener(mOnclickListener);

		bookLayout.setOnClickListener(mOnclickListener);
		activityLayout.setOnClickListener(mOnclickListener);
		coachLayout.setOnClickListener(mOnclickListener);
		mineLayout.setOnClickListener(mOnclickListener);
	}

	public void initView() {
		createView();
		app = (AppContext) getApplicationContext();
		mCityPopWindlow = new SelectCityPopWindlow(MainActivity.this,
				vSearch_city_img, vSelect_city_txt, chooseCityoncListener);
		pagerAdapter = new PagerAdapter() {
			// 判断再次添加的view和之前的view 是否是同一个view
			// arg0新添加的view，arg1之前的
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			// 返回数据源长度
			@Override
			public int getCount() {
				return list.size();
			}

			// 销毁被滑动走的view
			/**
			 * ViewGroup 代表了我们的viewpager 相当于activitygroup当中的view容器， 添加之前先移除。
			 * position 第几条数据 Object 被移出的view
			 * */
			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// 移除view
				container.removeView(list.get(position));
			}

			/**
			 * instantiateItem viewpager要现实的view ViewGroup viewpager position
			 * 第几条数据
			 * */
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				// 获取view添加到容器当中，并返回
				View v = list.get(position);
				container.addView(v);
				return v;
			}
		};
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int flag = (Integer) list.get((arg0)).getTag();
				setTab(flag);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		mViewPager.setAdapter(pagerAdapter);
	}

	// 把viewpager里面要显示的view实例化出来，并且把相关的view添加到一个list当中
	@SuppressWarnings("deprecation")
	private void createView() {
		view = this.getLocalActivityManager()
				.startActivity("book", new Intent(this, BookActivity.class))
				.getDecorView();
		view.setTag(0);
		list.add(view);
		view1 = this
				.getLocalActivityManager()
				.startActivity("huodong",
						new Intent(this, HuoDongActivity.class)).getDecorView();
		view1.setTag(1);
		list.add(view1);
		view2 = this
				.getLocalActivityManager()
				.startActivity("coach",
						new Intent(this, FindCoachActiviy.class))
				.getDecorView();
		view2.setTag(2);
		list.add(view2);
		view3 = this.getLocalActivityManager()
				.startActivity("mi", new Intent(this, MiActivity.class))
				.getDecorView();
		view3.setTag(3);
		list.add(view3);
	}

	private void setSeceted(int i) {
		// setTab(i);
		mViewPager.setCurrentItem(i);
	}

	private void setTab(int i) {

		// 设置图片为亮色
		resetImage();
		// 切换内容区域
		switch (i) {
		case 0:
			tab_book_img.setImageResource(R.drawable.tab_book_press);
			tab_book_text.setTextColor(getResources().getColor(
					R.color.main_color));
			setVisibleOrGoneTitle(0, null);

			break;
		case 1:
			tab_activity_img.setImageResource(R.drawable.tab_activity_press);
			tab_activity_text.setTextColor(getResources().getColor(
					R.color.main_color));
			setVisibleOrGoneTitle(1, "活动");
			HuoDongActivity.loadDate();
			break;
		case 2:
			tab_coach_img.setImageResource(R.drawable.tab_coach_press);
			tab_coach_text.setTextColor(getResources().getColor(
					R.color.main_color));
			setVisibleOrGoneTitle(2, "教练");
			break;
		case 3:
			tab_mine_img.setImageResource(R.drawable.tab_mine_press);
			tab_mine_text.setTextColor(getResources().getColor(
					R.color.main_color));
			setVisibleOrGoneTitle(3, "我");
			break;
		}
	}

	private void resetImage() {

		tab_book_img.setImageResource(R.drawable.tab_book_normal);
		tab_activity_img.setImageResource(R.drawable.tab_activity_normal);
		tab_coach_img.setImageResource(R.drawable.tab_coach_normal);
		tab_mine_img.setImageResource(R.drawable.tab_mine_normal);
		tab_book_text.setTextColor(getResources().getColor(
				R.color.text_home_color));
		tab_activity_text.setTextColor(getResources().getColor(
				R.color.text_home_color));
		tab_coach_text.setTextColor(getResources().getColor(
				R.color.text_home_color));
		tab_mine_text.setTextColor(getResources().getColor(
				R.color.text_home_color));
	}

	private void loction() {
		if (app.getSelectCity() != null && app.getSelectArea() == null) {
			vSelect_city_txt.setText(app.getSelectCity());
		} else if (app.getSelectCity() != null && app.getSelectArea() != null) {
			vSelect_city_txt.setText(app.getSelectCity() + "-"
					+ app.getSelectArea());
		} else {
			if (app.getLocationCity() == null) {
				vSelect_city_txt.setText("深圳");
				app.saveLocationCity("深圳");
				app.saveSelectCity("深圳");
			} else if (app.getLocationCity() != null
					&& app.getLocationArea() != null) {
				vSelect_city_txt.setText(app.getLocationCity() + "-"
						+ app.getLocationArea());

				app.saveSelectCity(app.getLocationCity());
				app.saveSelectArea(app.getLocationArea());
			}
		}
	}

	private long mExitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - mExitTime > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void setVisibleOrGoneTitle(int type, String title) {
		if (type == 0) {
			vHome.setVisibility(View.VISIBLE);
			vOther.setVisibility(View.GONE);
		} else {
			vOther.setVisibility(View.VISIBLE);
			vHome.setVisibility(View.GONE);
			mTitle.setText(title);
		}
	}

	private class MyOnclickListener implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.select_city_show:
				if (mCityPopWindlow.isShowing()) {
					mCityPopWindlow.dismiss();
					vSearch_city_img
							.setImageResource(R.drawable.search_city_change);
				} else {
					mCityPopWindlow.showPopupWindow(vSelect_city_show);
					vSearch_city_img
							.setImageResource(R.drawable.ic_pull_up_normal);
				}
				break;
			case R.id.search_lin:
				HelperUi.startActivity(MainActivity.this,
						SearchMerchantActivity.class);
				break;
			case R.id.web_map:
				HelperUi.startActivity(MainActivity.this,
						MapLocationActivity2.class);
				break;

			case R.id.tab_book:
				setSeceted(0);
				break;
			case R.id.tab_activity:
				setSeceted(1);
				break;
			case R.id.tab_coach:
				setSeceted(2);
				break;
			case R.id.tab_mine:
				setSeceted(3);
				break;
			default:
				break;
			}
		}
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	private OnClickListener chooseCityoncListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this,
					ChoiceSiteActivity.class);
			startActivityForResult(intent, CHANGE_CITY);
			mCityPopWindlow.dismiss();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			loction();
			break;
		default:
			break;
		}
	}
}
