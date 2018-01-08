package com.huiyoumall.uu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.huiyoumall.uu.AppConfig;
import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.entity.Activities;
import com.huiyoumall.uu.entity.Activities.ActivityMember;
import com.huiyoumall.uu.image.SmartImageView;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.util.DateUtil;
import com.huiyoumall.uu.util.StringUtils;
import com.huiyoumall.uu.widget.EmptyLayout;
import com.huiyoumall.uu.widget.GridViewForScollView;
import com.huiyoumall.uu.widget.SharePopupWindow;
import com.huiyoumall.uu.widget.SharePopupWindow.onShareItemClickListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 活动详情 界面
 * 
 * @author ASUS
 * 
 */
public class HuodongDetailsActivity extends BaseActivity implements
		OnClickListener, onShareItemClickListener, PlatformActionListener,
		Callback {

	public static String HD_DETAILS = "details";
	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;
	boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivitie = (Activities) getIntent().getExtras().get(HD_DETAILS);
		app = (AppContext) getApplicationContext();
		setContentView(R.layout.activity_huodong_details);
	}

	private SharePopupWindow mPopupWindow;

	private TextView vTitle;
	private TextView vHuodong_information;
	private TextView vUnfold_value;

	private ImageView vDetails_share;
	private ImageView collect_btn;

	private RelativeLayout vUnfold_tbn;
	private RelativeLayout vTitle_view;

	private Activities mActivitie;
	private AppContext app;

	private EmptyLayout detail_empter_view;
	private LinearLayout my_huodong_view;
	private LinearLayout apply_view;
	private SmartImageView member_avatar;
	private TextView join_num;
	private TextView member_age_text;
	private TextView member_name;
	private TextView huodong_count_text;
	private TextView huodong_name_text;
	private TextView huodong_project_text;
	private TextView huodong_date_text;
	private TextView huodong_merchant_text;
	private TextView huodong_people_num_text;
	private TextView huodong_theme_text;
	private TextView huodong_price_type_text;
	private TextView huodong_consume_text;
	private Button json_btn;
	private int mActId;
	private boolean isJoin;// 是否参加活动
	private boolean isOverdue;// 活动是否已经逾期
	private boolean isMyBook;// 是否是我发起的活动
	private GridViewForScollView huodong_join_gridview, huodong_apply_gridview;
	private List<ActivityMember> joinMembers;// 参加人员
	private List<ActivityMember> applyMembers;// 报名人员
	private ProgressDialog mDialog;

	@Override
	public void findViewById() {
		detail_empter_view = (EmptyLayout) findViewById(R.id.detail_empter_view);
		vTitle = (TextView) findViewById(R.id.title);
		vUnfold_value = (TextView) findViewById(R.id.unfold_value_text);
		vDetails_share = (ImageView) findViewById(R.id.details_share);
		collect_btn = (ImageView) findViewById(R.id.collect_btn);
		vTitle_view = (RelativeLayout) findViewById(R.id.title_view);
		vUnfold_tbn = (RelativeLayout) findViewById(R.id.unfold_tbn);

		huodong_join_gridview = (GridViewForScollView) findViewById(R.id.huodong_join_gridview);
		huodong_apply_gridview = (GridViewForScollView) findViewById(R.id.huodong_apply_gridview);

		my_huodong_view = (LinearLayout) findViewById(R.id.my_huodong_view);
		apply_view = (LinearLayout) findViewById(R.id.apply_view);
		join_num = (TextView) findViewById(R.id.join_num);
		member_avatar = (SmartImageView) findViewById(R.id.huodong_member_avatar);
		member_age_text = (TextView) findViewById(R.id.member_age_text);
		member_name = (TextView) findViewById(R.id.member_name);
		huodong_count_text = (TextView) findViewById(R.id.huodong_count_text);
		huodong_name_text = (TextView) findViewById(R.id.huodong_name_text);
		huodong_project_text = (TextView) findViewById(R.id.huodong_project_text);
		huodong_date_text = (TextView) findViewById(R.id.huodong_date_text);
		huodong_merchant_text = (TextView) findViewById(R.id.huodong_merchant_text);
		huodong_people_num_text = (TextView) findViewById(R.id.huodong_people_num_text);
		huodong_theme_text = (TextView) findViewById(R.id.huodong_theme_text);
		huodong_price_type_text = (TextView) findViewById(R.id.huodong_price_type_text);
		huodong_consume_text = (TextView) findViewById(R.id.huodong_consume_text);
		vHuodong_information = (TextView) findViewById(R.id.huodong_information_text);
		json_btn = (Button) findViewById(R.id.json_btn);
		collect_btn.setVisibility(View.GONE);
	}

	@Override
	public void initView() {
		vTitle.setText("活动详情");
		mActId = mActivitie.getMember_id();
		member_avatar.setImageUrl(mActivitie.getMember_avatar());
		member_age_text.setText(mActivitie.getMember_age() + " 岁");
		StringUtils.setSixIcon(member_age_text, mActivitie.getMember_sex(),
				this);
		// if (mActivitie.getMember_sex().equals("男")) {
		// Drawable sex_img = getResources().getDrawable(R.drawable.ic_male);
		// sex_img.setBounds(0, 0, sex_img.getMinimumWidth(),
		// sex_img.getMinimumHeight());
		// member_age_text.setCompoundDrawables(sex_img, null, null, null);
		// } else {
		// Drawable sex_img = getResources().getDrawable(R.drawable.ic_female);
		// sex_img.setBounds(0, 0, sex_img.getMinimumWidth(),
		// sex_img.getMinimumHeight());
		// member_age_text.setCompoundDrawables(sex_img, null, null, null);
		// }

		member_name.setText(mActivitie.getMember_name());
		join_num.setText(mActivitie.getJoin_num() + "");
		huodong_count_text.setText("活动次数");
		huodong_name_text.setText(mActivitie.getAct_name());
		huodong_project_text.setText(mActivitie.getSport_name());
		huodong_date_text.setText(huodongDate(mActivitie.getDate(),
				mActivitie.getAct_time()));
		huodong_merchant_text.setText(mActivitie.getM_name());
		if (mActivitie.getTotle_num() == 0) {
			huodong_people_num_text.setText("不限");
		} else {
			huodong_people_num_text.setText(mActivitie.getTotle_num() + "人");
		}
		huodong_theme_text.setText(mActivitie.getAct_methem());
		huodong_price_type_text.setText(mActivitie.getPrice_type());
		huodong_consume_text.setText(mActivitie.getAct_price() + "");
		if (app.isLogin) {
			if (app.member_id == mActivitie.getMember_id()) {
				// 此活动是我发起的
				my_huodong_view.setVisibility(View.GONE);
				apply_view.setVisibility(View.VISIBLE);
				isMyBook = true;
			} else {
				my_huodong_view.setVisibility(View.VISIBLE);
				apply_view.setVisibility(View.GONE);
				isMyBook = false;
			}
		}

		mPopupWindow = new SharePopupWindow(this, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindow.setOnShareItemClickListener(this);

		vUnfold_tbn.setOnClickListener(this);
		vDetails_share.setOnClickListener(this);
		json_btn.setOnClickListener(this);
		loadDetail();
	}

	private String act_num = "";
	private String act_information = "";
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			detail_empter_view.setVisibility(View.GONE);
			if (msg.what == 1) {
				String jsonStr = (String) msg.obj;
				try {
					JSONObject json = new JSONObject(jsonStr);
					act_num = json.getString("act_num");
					act_information = json.getString("act_information");
					isJoin = json.getBoolean("isjoin");
					isOverdue = json.getBoolean("isoverdue");
					if (isOverdue) {
						my_huodong_view.setVisibility(View.VISIBLE);
						json_btn.setText("活动已经逾期了");
						json_btn.setEnabled(false);
					}
					if (isJoin) {
						my_huodong_view.setVisibility(View.GONE);
					} else {
						my_huodong_view.setVisibility(View.VISIBLE);
					}
					join_num.setText(act_num);
					if (act_information.length() > 120) {
						vHuodong_information.setText(act_information.substring(
								0, 120).toString());
						vUnfold_value.setVisibility(View.VISIBLE);

					} else {
						vHuodong_information.setText(act_information);
						vUnfold_value.setVisibility(View.GONE);
					}

					JSONArray joinArray = new JSONArray(json.getString("join"));
					if (joinArray.length() > 0) {
						ActivityMember joinentity = null;
						joinMembers = new ArrayList<ActivityMember>();
						for (int j = 0; j < joinArray.length(); j++) {
							JSONObject joinJson = joinArray.getJSONObject(j);
							joinentity = new ActivityMember();
							joinentity.setMember_id(joinJson
									.getInt("member_id"));
							joinentity.setMember_avatar(joinJson
									.getString("member_avatar"));
							joinentity.setMember_name(joinJson
									.getString("member_name"));
							joinentity.setMember_sex(joinJson
									.getString("member_sex"));
							joinentity.setTel(joinJson.getString("tel"));
							joinentity.setSignature(joinJson
									.getString("signature"));
							joinentity.setMember_age(joinJson
									.getInt("member_age"));
							joinentity.setMember_provinceid(joinJson
									.getString("member_provinceid"));
							joinentity.setMember_cityid(joinJson
									.getString("member_cityid"));
							joinMembers.add(joinentity);
						}
						// 设置gridview 横向滑动
						int size = joinMembers.size();
						int length = 100;
						DisplayMetrics dm = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(dm);
						float density = dm.density;
						int gridviewWidth = (int) (size * (length + 4) * density);
						int itemWidth = (int) (length * density);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								gridviewWidth,
								LinearLayout.LayoutParams.FILL_PARENT);
						huodong_join_gridview.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
						huodong_join_gridview.setColumnWidth(itemWidth); // 设置列表项宽
						huodong_join_gridview.setHorizontalSpacing(5); // 设置列表项水平间距
						huodong_join_gridview
								.setStretchMode(GridView.NO_STRETCH);
						huodong_join_gridview.setNumColumns(size); // 设置列数量=列表集合数
						huodong_join_gridview.setSelector(new ColorDrawable(
								Color.TRANSPARENT));// 设置透明色
						HuodongMemberAdapter joinAdapter = new HuodongMemberAdapter(
								joinMembers);
						huodong_join_gridview.setAdapter(joinAdapter);
						huodong_join_gridview
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										ActivityMember act = joinMembers
												.get(position);
										Bundle bundle = new Bundle();
										bundle.putSerializable(
												HuodongPersonDetailsActivity.MEMBER_DETAILS,
												act);
										bundle.putBoolean(
												HuodongPersonDetailsActivity.IS_MY_BOOK,
												isMyBook);
										bundle.putBoolean(
												HuodongPersonDetailsActivity.IS_OVERDUE,
												isOverdue);
										HelperUi.startActivity(
												HuodongDetailsActivity.this,
												HuodongPersonDetailsActivity.class,
												bundle);
									}
								});
					}

					JSONArray applyArray = new JSONArray(
							json.getString("apply"));
					if (applyArray.length() > 0) {
						ActivityMember applyentity = null;
						applyMembers = new ArrayList<ActivityMember>();
						for (int i = 0; i < applyArray.length(); i++) {
							applyentity = new ActivityMember();
							JSONObject applyJson = applyArray.getJSONObject(i);
							applyentity = new ActivityMember();
							applyentity.setMember_id(applyJson
									.getInt("member_id"));
							applyentity.setMember_avatar(applyJson
									.getString("member_avatar"));
							applyentity.setMember_name(applyJson
									.getString("member_name"));
							applyentity.setMember_sex(applyJson
									.getString("member_sex"));
							applyentity.setTel(applyJson.getString("tel"));
							applyentity.setSignature(applyJson
									.getString("signature"));
							applyentity.setMember_age(applyJson
									.getInt("member_age"));
							applyentity.setMember_provinceid(applyJson
									.getString("member_provinceid"));
							applyentity.setMember_cityid(applyJson
									.getString("member_cityid"));
							applyMembers.add(applyentity);
						}

						// 设置gridview 横向滑动
						int size = applyMembers.size();
						int length = 100;
						DisplayMetrics dm = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(dm);
						float density = dm.density;
						int gridviewWidth = (int) (size * (length + 4) * density);
						int itemWidth = (int) (length * density);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								gridviewWidth,
								LinearLayout.LayoutParams.FILL_PARENT);
						huodong_apply_gridview.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
						huodong_apply_gridview.setColumnWidth(itemWidth); // 设置列表项宽
						huodong_apply_gridview.setHorizontalSpacing(5); // 设置列表项水平间距
						huodong_apply_gridview
								.setStretchMode(GridView.NO_STRETCH);
						huodong_apply_gridview.setNumColumns(size); // 设置列数量=列表集合数
						huodong_apply_gridview.setSelector(new ColorDrawable(
								Color.TRANSPARENT));// 设置透明色
						HuodongMemberAdapter applyAdapter = new HuodongMemberAdapter(
								applyMembers);
						huodong_apply_gridview.setAdapter(applyAdapter);
						huodong_apply_gridview
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										ActivityMember act = applyMembers
												.get(position);
										Bundle bundle = new Bundle();
										bundle.putSerializable(
												HuodongPersonDetailsActivity.MEMBER_DETAILS,
												act);
										bundle.putBoolean(
												HuodongPersonDetailsActivity.IS_MY_BOOK,
												isMyBook);
										bundle.putBoolean(
												HuodongPersonDetailsActivity.IS_OVERDUE,
												isOverdue);
										HelperUi.startActivity(
												HuodongDetailsActivity.this,
												HuodongPersonDetailsActivity.class,
												bundle);
									}
								});

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	};

	private void loadDetail() {
		final Message msg = Message.obtain();
		mActId = 34;
		UURemoteApi.loadHuoDongDetail(mActId, 3,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						String result = new String(arg2);
						msg.what = 1;
						msg.obj = result;
						mHandler.sendMessage(msg);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						msg.what = -1;
						mHandler.sendMessage(msg);
					}
				});
	}

	private String huodongDate(String mDate, String mTime) {
		String week = DateUtil.getWeekNumber(mDate, "yyyy-MM-dd");
		String dates = "";
		String[] date = mDate.split("-");
		dates = date[1] + "." + date[2];
		return week + "  " + dates + "  " + mTime;
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
		case R.id.unfold_tbn:
			if (flag) {
				vHuodong_information.setText(act_information.substring(0, 120));
				flag = false;
				vUnfold_value.setText("展开");
			} else {
				vHuodong_information.setText(act_information);
				flag = true;
				vUnfold_value.setText("收起");
			}
			break;
		case R.id.details_share:
			int height = vTitle_view.getHeight()
					+ AppConfig.getStatusBarHeight(HuodongDetailsActivity.this);
			mPopupWindow.showAtLocation(vTitle_view, Gravity.TOP, 0, height);
			break;
		case R.id.json_btn:
			// 报名参加活动
			mDialog = new ProgressDialog(HuodongDetailsActivity.this);
			mDialog.setTitle("提示");
			mDialog.setMessage("参与中...");
			mDialog.show();
			UURemoteApi.loadJoinHuodong(mActivitie.getAct_id(), app.member_id,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							String result = new String(arg2);
							if (result.equals("1")) {
								HelperUi.showToastShort(
										HuodongDetailsActivity.this, "恭喜！您已经参加"
												+ mActivitie.getAct_name()
												+ "活动");
								my_huodong_view.setVisibility(View.GONE);
							} else {
								HelperUi.showToastShort(
										HuodongDetailsActivity.this, "参加活动失败！");
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							HelperUi.showToastShort(
									HuodongDetailsActivity.this, "请求服务失败！");
						}
					});
			break;

		default:
			break;
		}
	}

	@Override
	public void onWechatButtonClick() {
		Wechat.ShareParams sp = new Wechat.ShareParams();
		sp.title = "优优运动俱乐部";
		sp.text = "和你一起分享运动的快乐";
		sp.shareType = Platform.SHARE_TEXT;
		sp.url = "http://www.huiyoumall.cn/";
		// sp.imageUrl = mErchant.getCover_image();
		Platform wc = ShareSDK.getPlatform(HuodongDetailsActivity.this,
				Wechat.NAME);
		wc.setPlatformActionListener(HuodongDetailsActivity.this);
		wc.share(sp);
	}

	@Override
	public void onWechatmomentButtonClick() {
		WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
		sp.title = "优优运动俱乐部";
		sp.text = "和你一起分享运动的快乐";
		sp.shareType = Platform.SHARE_TEXT;
		sp.url = "http://www.huiyoumall.cn/";
		// sp.imageUrl = mErchant.getCover_image();
		Platform wm = ShareSDK.getPlatform(HuodongDetailsActivity.this,
				WechatMoments.NAME);
		wm.setPlatformActionListener(HuodongDetailsActivity.this);
		wm.share(sp);
	}

	@Override
	public void onQqButtonClick() {
		QQ.ShareParams sp = new QQ.ShareParams();
		sp.title = "羽毛球馆";
		// sp.titleUrl = "和你一起运动分享生活快乐"; // 标题的超链接
		sp.text = "和你一起运动分享生活快乐";
		// sp.imageUrl = "http://www.someserver.com/测试图片网络地址.jpg";

		Platform qq = ShareSDK
				.getPlatform(HuodongDetailsActivity.this, QQ.NAME);
		qq.setPlatformActionListener(HuodongDetailsActivity.this); // 设置分享事件回调
		// 执行图文分享
		qq.share(sp);
	}

	@Override
	public void onQzoneButtonClick() {
		// QZone.ShareParams sp = new QZone.ShareParams();
		// sp.title = "测试Title";
		// sp.titleUrl = "http://www.baidu.com"; // 标题的超链接
		// sp.text = "Text文本内容 http://www.baidu.com";
		// sp.imageUrl =
		// "http://365ccyx.com/templates/ccyx/images/pic/logo.png";
		// sp.site = "sharesdk";
		// sp.siteUrl = "http://sharesdk.cn";
		// Platform qzone = ShareSDK.getPlatform(MerchantDetailsActivity.this,
		// QZone.NAME);
		// qzone.setPlatformActionListener(MerchantDetailsActivity.this); //
		// 设置分享事件回调
		// // 执行图文分享
		// qzone.share(sp);
	}

	@Override
	public void onSinaweiboButtonClick() {
		SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
		// 设置分享内容
		sp.text = "羽毛球馆,和你一起运动分享生活快乐";
		sp.imageUrl = "http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg";

		// 初始化新浪分享平台
		Platform sina = ShareSDK.getPlatform(HuodongDetailsActivity.this,
				SinaWeibo.NAME);
		// 设置分享监听
		sina.setPlatformActionListener(HuodongDetailsActivity.this);
		// 执行分享
		sina.share(sp);
	}

	@Override
	public void onCancel(Platform platform, int action) {
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> arg2) {
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		t.printStackTrace();

		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_TOAST: {
			String text = String.valueOf(msg.obj);
			Toast.makeText(HuodongDetailsActivity.this, text,
					Toast.LENGTH_SHORT).show();
		}
			break;
		case MSG_ACTION_CCALLBACK: {
			switch (msg.arg1) {
			case 1: { // 成功提示, successful notification
				showNotification(2000, getString(R.string.share_completed));
			}
				break;
			case 2: { // 失败提示, fail notification
				String expName = msg.obj.getClass().getSimpleName();
				if ("WechatClientNotExistException".equals(expName)
						|| "WechatTimelineNotSupportedException"
								.equals(expName)) {
					showNotification(2000,
							getString(R.string.wechat_client_inavailable));
				} else if ("GooglePlusClientNotExistException".equals(expName)) {
					showNotification(2000,
							getString(R.string.google_plus_client_inavailable));
				} else if ("QQClientNotExistException".equals(expName)) {
					showNotification(2000,
							getString(R.string.qq_client_inavailable));
				} else {
					showNotification(2000, getString(R.string.share_failed));
				}
			}
				break;
			case 3: { // 取消提示, cancel notification
				showNotification(2000, getString(R.string.share_canceled));
			}
				break;
			}
		}
			break;
		case MSG_CANCEL_NOTIFY: {
			NotificationManager nm = (NotificationManager) msg.obj;
			if (nm != null) {
				nm.cancel(msg.arg1);
			}
		}
			break;
		}
		return false;
	}

	// 在状态栏提示分享操作,the notification on the status bar
	@SuppressWarnings("deprecation")
	private void showNotification(long cancelTime, String text) {
		try {
			Context app = getApplicationContext();
			NotificationManager nm = (NotificationManager) app
					.getSystemService(Context.NOTIFICATION_SERVICE);
			final int id = Integer.MAX_VALUE / 13 + 1;
			nm.cancel(id);

			long when = System.currentTimeMillis();
			Notification notification = new Notification(R.drawable.logo, text,
					when);
			PendingIntent pi = PendingIntent.getActivity(app, 0, new Intent(),
					0);
			notification.setLatestEventInfo(app, "分享", text, pi);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			nm.notify(id, notification);

			if (cancelTime > 0) {
				Message msg = new Message();
				msg.what = MSG_CANCEL_NOTIFY;
				msg.obj = nm;
				msg.arg1 = id;
				UIHandler.sendMessageDelayed(msg, cancelTime, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class HuodongMemberAdapter extends BaseAdapter {

		private List<ActivityMember> members;
		private LayoutInflater mInflater;

		public HuodongMemberAdapter(List<ActivityMember> members_) {
			this.members = members_;
			mInflater = LayoutInflater.from(HuodongDetailsActivity.this);
		}

		@Override
		public int getCount() {
			return members.size();
		}

		@Override
		public Object getItem(int position) {
			return members.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolderView mHolderView = null;
			if (convertView == null || convertView.getTag() == null) {
				mHolderView = new HolderView();
				convertView = mInflater.inflate(R.layout.item_huodong_person,
						null);
				mHolderView.member_avatar = (SmartImageView) convertView
						.findViewById(R.id.member_avatar);
				mHolderView.member_name = (TextView) convertView
						.findViewById(R.id.member_name);
				convertView.setTag(mHolderView);
			} else {
				mHolderView = (HolderView) convertView.getTag();
			}
			ActivityMember member = (ActivityMember) getItem(position);
			mHolderView.member_avatar.setImageUrl(member.getMember_avatar());
			mHolderView.member_name.setText(member.getMember_name());
			return convertView;
		}

		class HolderView {
			SmartImageView member_avatar;
			TextView member_name;
		}

	}
}
