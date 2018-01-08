package com.huiyoumall.uu.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.huiyoumall.uu.adapter.CommentAdapter;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.common.Options;
import com.huiyoumall.uu.db.DataHelper;
import com.huiyoumall.uu.entity.Comment;
import com.huiyoumall.uu.entity.Merchant;
import com.huiyoumall.uu.entity.MerchantImage;
import com.huiyoumall.uu.remote.NetWorkHelper;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.util.StringUtils;
import com.huiyoumall.uu.widget.EmptyLayout;
import com.huiyoumall.uu.widget.ListViewForScrollView;
import com.huiyoumall.uu.widget.MyScrollView;
import com.huiyoumall.uu.widget.MyScrollView.OnScrollListener;
import com.huiyoumall.uu.widget.SharePopupWindow;
import com.huiyoumall.uu.widget.SharePopupWindow.onShareItemClickListener;
import com.huiyoumall.uu.widget.ViewPopupWindow;
import com.huiyoumall.uu.widget.ViewPopupWindow.onViewItemClickListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 场馆详情 界面
 * 
 * @author ASUS
 * 
 */
public class MerchantDetailsActivity extends BaseActivity implements
		OnScrollListener, onShareItemClickListener, onViewItemClickListener,
		PlatformActionListener, Callback {

	public static String M_ID = "m_id";
	public static String M_DETAILS = "merchant";

	public static int LOAD_SUCCESS = 0;
	public static int LOAD_CANCEL = 1;
	public static int LOAD_ERROR = 2;

	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		// mErchant = (Merchant) getIntent().getExtras()
		// .getSerializable(M_DETAILS);
		mMid = getIntent().getExtras().getString(M_ID);
		setContentView(R.layout.activity_merchant_details);
		// 初始化ShareSDK
		ShareSDK.initSDK(this);
		loadMerchand();

	}

	private MyScrollView vCourt_details_scroll;
	private LinearLayout vCourt_title_down;
	private RelativeLayout vTitle_view;
	private RelativeLayout vMerchant_comment;
	private RelativeLayout vMerchant__phone;
	private RelativeLayout vShop_details_address;
	private RelativeLayout vMore_discuss;
	private LinearLayout vOther_view, vSell_view, vPark_view, vStation_view;
	private FrameLayout vCourt_details_frame;

	private ImageView vmercant_image;
	private ImageView vDev_equipment_img;
	private ImageView vService_equipment_img;
	private ImageView vDetails_share;
	private ImageView vGrade_img;
	private ImageView vCollect_btn;

	private Button vShop_btn;
	private Button vShop_btn_down;
	private TextView vMerchant_name;
	private TextView vMerchant_phone_txt;
	private TextView vMerchant_address_txt;
	private TextView vDev_text;
	private TextView vService_text;
	private TextView vGrade_txt;
	private TextView vNo_discuss;
	private TextView vTitle;

	// private TextView vGrade_txt;

	private TextView vOther_text, vSell_text, vInvoice_text, vPark_text,
			vStation_text;

	private TextView vReserve_count;

	private TextView vView_count;
	private TextView vIntroduction;
	private TextView vShop_price;
	private TextView vShop_price_down;
	private TextView vMarket_price_down;
	private TextView vMarket_price;
	private TextView vBusiness_hours;
	private TextView vComment_num;
	private Merchant mErchant;
	private EmptyLayout vEmptyLayout;
	private ListViewForScrollView vCommemtList;
	private List<Comment> mComments = new ArrayList<Comment>();// 场馆评价mComments
	private List<MerchantImage> merchantImages;// 场馆图片

	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private SharePopupWindow mPopupWindow;
	private ViewPopupWindow mViewPopWindow;
	private String mMid;
	private CommentAdapter commentAdapter;
	private ProgressDialog mDialog;
	private AppContext app;

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		vCourt_details_scroll = (MyScrollView) findViewById(R.id.merchant_details_scroll);
		vCourt_title_down = (LinearLayout) findViewById(R.id.court_title_down);
		vCourt_details_frame = (FrameLayout) findViewById(R.id.court_details_frame);
		vmercant_image = (ImageView) findViewById(R.id.mercant_image);

		vDetails_share = (ImageView) findViewById(R.id.details_share);
		vCollect_btn = (ImageView) findViewById(R.id.collect_btn);
		vShop_btn = (Button) findViewById(R.id.shop_btn);
		vShop_btn_down = (Button) findViewById(R.id.shop_btn_down);
		vMerchant_name = (TextView) findViewById(R.id.name);
		vMerchant_phone_txt = (TextView) findViewById(R.id.merchant_phone_txt);
		vTitle_view = (RelativeLayout) findViewById(R.id.title_view);
		vMerchant_comment = (RelativeLayout) findViewById(R.id.merchant_comment);
		vMerchant__phone = (RelativeLayout) findViewById(R.id.merchant_details_phone);
		vShop_details_address = (RelativeLayout) findViewById(R.id.shop_details_address);

		vOther_view = (LinearLayout) findViewById(R.id.other_view);
		vSell_view = (LinearLayout) findViewById(R.id.sell_view);
		vPark_view = (LinearLayout) findViewById(R.id.park_view);
		vStation_view = (LinearLayout) findViewById(R.id.station_view);
		vNo_discuss = (TextView) findViewById(R.id.no_discuss);

		vMerchant_address_txt = (TextView) findViewById(R.id.shop_adress);
		vEmptyLayout = (EmptyLayout) findViewById(R.id.empter_view);
		vCommemtList = (ListViewForScrollView) findViewById(R.id.comment_list);
		vShop_price = (TextView) findViewById(R.id.shop_price);
		vShop_price_down = (TextView) findViewById(R.id.shop_price_down);
		vMarket_price = (TextView) findViewById(R.id.market_price);
		vMarket_price_down = (TextView) findViewById(R.id.market_price_down);
		vReserve_count = (TextView) findViewById(R.id.reserve_count);
		vView_count = (TextView) findViewById(R.id.view_count);
		vIntroduction = (TextView) findViewById(R.id.mer_introduction);
		vBusiness_hours = (TextView) findViewById(R.id.business_hours);
		vDev_equipment_img = (ImageView) findViewById(R.id.dev_equipment_img);
		vDev_text = (TextView) findViewById(R.id.dev_text);
		vService_equipment_img = (ImageView) findViewById(R.id.service_equipment_img);
		vService_text = (TextView) findViewById(R.id.service_text);
		vComment_num = (TextView) findViewById(R.id.comment_num_text);
		vGrade_txt = (TextView) findViewById(R.id.grade_txt);
		vGrade_img = (ImageView) findViewById(R.id.grade_img);

		vOther_text = (TextView) findViewById(R.id.other_text);
		vSell_text = (TextView) findViewById(R.id.sell_text);
		vInvoice_text = (TextView) findViewById(R.id.invoice_text);
		vPark_text = (TextView) findViewById(R.id.park_text);
		vStation_text = (TextView) findViewById(R.id.near_station_text);
		vMore_discuss = (RelativeLayout) findViewById(R.id.more_discuss);

		vCourt_details_scroll.setOnScrollListener(this);
		vmercant_image.setOnClickListener(myOnClickListener);
		vShop_btn.setOnClickListener(myOnClickListener);
		vShop_btn_down.setOnClickListener(myOnClickListener);
		vMerchant_comment.setOnClickListener(myOnClickListener);
		vMerchant__phone.setOnClickListener(myOnClickListener);
		vShop_details_address.setOnClickListener(myOnClickListener);
		vDev_equipment_img.setOnClickListener(myOnClickListener);
		vService_equipment_img.setOnClickListener(myOnClickListener);
		vMore_discuss.setOnClickListener(myOnClickListener);
		vDetails_share.setOnClickListener(myOnClickListener);
		vCollect_btn.setOnClickListener(myOnClickListener);

	}

	@Override
	public void initView() {
		vTitle.setText("场馆详情");
		app = (AppContext) getApplicationContext();
		options = Options.getLargeListOptions();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		// if (mErchant != null) {
		// vMerchant_name.setText(mErchant.getM_name());
		// vShop_price.setText("$" + mErchant.getShop_price());
		// vMarket_price.setText("￥" + mErchant.getMarket_price());
		// // vmercant_image.setImageUrl(mErchant.getCover_image());
		//
		// vMarket_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		// imageLoader.displayImage(mErchant.getCover_image(), vmercant_image,
		// options);
		//
		// } else if (mMid != null) {
		// int mid = Integer.parseInt(mMid);
		// // 根据mid加载场馆详情
		// }
		mPopupWindow = new SharePopupWindow(this, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindow.setOnShareItemClickListener(this);

		mViewPopWindow = new ViewPopupWindow(this, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mViewPopWindow.setText("取消", "呼叫");
		mViewPopWindow.setonViewItemClickListener(this);

		commentAdapter = new CommentAdapter(this, mComments);
		vCommemtList.setAdapter(commentAdapter);
	}

	// .............................请求场馆收藏数据...................................................
	private Handler mHandlerCollect = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mDialog.dismiss();
			if (msg.what == 1) {
				String result = (String) msg.obj;
				int rid = Integer.parseInt(result);
				if (rid == 0) {
					Toast.makeText(getApplicationContext(), "收藏成功",
							Toast.LENGTH_SHORT).show();
					// 界面刷新 按钮变为收藏状态的
					mErchant.setCollect(1);
					setIcon(mErchant.getCollect());

				} else if (rid == 1) {
					Toast.makeText(getApplicationContext(), "取消成功",
							Toast.LENGTH_SHORT).show();
					// 界面刷新 按钮变为没有收藏状态
					mErchant.setCollect(0);
					setIcon(mErchant.getCollect());
				} else if (rid == 2) {
					Toast.makeText(getApplicationContext(), "操作失败，稍后再试",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "PHP攻城狮，操作失误",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "操作失败，稍后再试",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	/**
	 * 设置收藏图标的背景
	 * 
	 * @param a
	 */
	public void setIcon(int a) {
		if (a == 1) {
			vCollect_btn.setBackground(getResources().getDrawable(
					R.drawable.btn_collectt_press));
		} else if (a == 0) {
			vCollect_btn.setBackground(getResources().getDrawable(
					R.drawable.btn_collect));
		} else {
			Toast.makeText(getApplicationContext(), "Android攻城狮，操作失误",
					Toast.LENGTH_SHORT).show();
		}

	}

	private void collectMerchant() {
		mDialog.show();
		if (!NetWorkHelper.isNetworkAvailable(this)) {
			return;
		} else {
			UURemoteApi.collectMerchant(mMid, app.getisLoginUser()
					.getMember_id(), new AsyncHttpResponseHandler() {
				Message msg = Message.obtain();

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					String result = new String(arg2);
					msg.what = 1;
					msg.obj = result;
					mHandlerCollect.sendMessage(msg);
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					msg.what = -1;
					mHandlerCollect.sendMessage(msg);
				}
			});
		}
	}

	// .............................请求场馆详情数据...................................................

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == LOAD_SUCCESS) {
				Merchant merchant = (Merchant) msg.obj;
				mErchant = merchant;
				if (mErchant.getCollect() == 0) {
					vCollect_btn.setBackground(getResources().getDrawable(
							R.drawable.btn_collect));
				} else {
					vCollect_btn.setBackground(getResources().getDrawable(
							R.drawable.btn_collectt_press));
				}
				if (merchant != null) {
					vEmptyLayout.setVisibility(View.GONE);
					vCourt_details_scroll.setVisibility(View.VISIBLE);
					if (merchant.getComments() != null
							&& merchant.getComments().size() > 0) {
						mComments = merchant.getComments();

						// 当没有评价是隐藏评价列表和查看更多评价
						// 并显示没有评价
						if (mComments.size() != 0) {
							vNo_discuss.setVisibility(View.GONE);
							vCommemtList.setVisibility(View.VISIBLE);
							vMore_discuss.setVisibility(View.VISIBLE);
						} else {
							vNo_discuss.setVisibility(View.VISIBLE);
							vCommemtList.setVisibility(View.GONE);
							vMore_discuss.setVisibility(View.GONE);
						}
						commentAdapter.setData(mComments);
					}
					if (merchant.getImages() != null
							&& merchant.getImages().length > 0) {
						String[] images = merchant.getImages();
						MerchantImage mi = null;
						merchantImages = new ArrayList<MerchantImage>();
						for (int i = 0; i < images.length; i++) {
							mi = new MerchantImage();
							mi.setUrl(images[i]);
							merchantImages.add(mi);
						}
					}
					if (StringUtils.isEmpty(merchant.getCover_image())) {
						MerchantImage mImg = new MerchantImage();
						mImg.setUrl(merchant.getCover_image());
						merchantImages.add(mImg);
					}

					// 插入本地数据库
					DataHelper dh = new DataHelper(MerchantDetailsActivity.this);
					dh.insertbrowse(merchant);
					// 场馆名称
					vMerchant_name.setText(merchant.getM_name());
					// 本场价格
					vShop_price.setText("¥" + merchant.getShop_price());
					vShop_price_down.setText("¥" + merchant.getShop_price());
					// 市场价格
					vMarket_price.setText("¥" + merchant.getMarket_price());
					vMarket_price_down
							.setText("¥" + merchant.getMarket_price());
					vMarket_price.getPaint().setFlags(
							Paint.STRIKE_THRU_TEXT_FLAG);
					vMarket_price_down.getPaint().setFlags(
							Paint.STRIKE_THRU_TEXT_FLAG);
					// 封面图
					imageLoader.displayImage(mErchant.getCover_image(),
							vmercant_image, options);
					// 预定数
					vReserve_count.setText(merchant.getReserve_count() + "");
					// 浏览数
					vView_count.setText(merchant.getView_count() + "");
					// 场地介绍
					vIntroduction.setText(merchant.getMdesc());
					// 场馆电话
					vMerchant_phone_txt.setText(merchant.getCon_information());
					// 场馆详细地址
					vMerchant_address_txt.setText(merchant.getAddress());
					// 营业时间
					vBusiness_hours.setText(merchant.getStarttime() + "~~"
							+ merchant.getEndtime());
					vComment_num.setText(merchant.getComment_num() + "人评价");

					// 运动设施 先判断
					imageLoader.displayImage(merchantImages.get(0).getUrl(),
							vDev_equipment_img, options);
					// 服务设施
					imageLoader.displayImage(merchantImages.get(0).getUrl(),
							vService_equipment_img, options);

					if (!StringUtils.isEmpty(merchant.getDev_list())) {
						String devStr = merchant.getDev_list();
						devStr = devStr.substring(0, devStr.length() - 1);
						devStr = devStr.replace(",", "  |  ");
						vDev_text.setText(devStr);
					}
					if (!StringUtils.isEmpty(merchant.getService_list())) {
						String serStr = merchant.getService_list();
						serStr = serStr.substring(0, serStr.length() - 1);
						serStr = serStr.replace(",", "  |  ");
						vService_text.setText(serStr);
					}
					if (!StringUtils.isEmpty(merchant.getOther())) {
						vOther_text.setText(merchant.getOther());
					} else {
						vOther_view.setVisibility(View.GONE);
					}

					if (!StringUtils.isEmpty(merchant.getSell())) {
						vSell_text.setText(merchant.getSell());
					} else {
						vSell_view.setVisibility(View.GONE);
					}

					if (merchant.getInvoice() == 0) {
						vInvoice_text.setText("无发票");
					} else {
						vInvoice_text.setText("有发票");
					}

					if (!StringUtils.isEmpty(merchant.getPark())) {
						vPark_text.setText(merchant.getPark());
					} else {
						vPark_view.setVisibility(View.GONE);
					}

					if (!StringUtils.isEmpty(merchant.getNear_station())) {
						vStation_text.setText(merchant.getNear_station());
					} else {
						vStation_view.setVisibility(View.GONE);
					}
					vGrade_img.setImageBitmap(AppConfig.getGradeImg(
							MerchantDetailsActivity.this, merchant.getGrade()));
					// 评分值
					vGrade_txt.setText(merchant.getGrade() + "");

					vCourt_details_scroll.smoothScrollTo(0, 0);

				}
			} else {
				vEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
			}
		};
	};

	private void loadMerchand() {
		if (!NetWorkHelper.isNetworkAvailable(this)) {
			vEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
		} else {
			vEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
			UURemoteApi.loadMerchant(mMid, new AsyncHttpResponseHandler() {
				Message msg = Message.obtain();

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					String result = new String(arg2);
					try {
						Merchant m = Merchant.parseEntity(result);
						msg.what = LOAD_SUCCESS;
						msg.obj = m;
					} catch (Exception e) {
						vEmptyLayout
								.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
						msg.what = LOAD_ERROR;
						e.printStackTrace();
					}
					mHandler.sendMessage(msg);
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					vEmptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
					msg.what = LOAD_ERROR;
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
	protected void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}

	@Override
	public void onScroll(int scrollY) {
		if (scrollY >= vCourt_details_frame.getHeight()) {
			vCourt_title_down.setVisibility(View.VISIBLE);
		} else {
			vCourt_title_down.setVisibility(View.GONE);
		}
	}

	private OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mercant_image:
				changeImageShow();
				break;
			case R.id.shop_btn:
				Bundle bundle = new Bundle();
				bundle.putString(ReserveMerchantActivity.MER_ID, mMid);
				bundle.putString(ReserveMerchantActivity.MER_NAME,
						mErchant.getM_name());
				HelperUi.startActivity(MerchantDetailsActivity.this,
						ReserveMerchantActivity.class, bundle);
				break;
			case R.id.shop_btn_down:
				Bundle bundle2 = new Bundle();
				bundle2.putString(ReserveMerchantActivity.MER_ID, mMid);
				bundle2.putString(ReserveMerchantActivity.MER_NAME,
						mErchant.getM_name());
				HelperUi.startActivity(MerchantDetailsActivity.this,
						ReserveMerchantActivity.class, bundle2);
				break;
			case R.id.merchant_comment:
				Intent intent = new Intent(MerchantDetailsActivity.this,
						CommentActivity.class);
				intent.putExtra(CommentActivity.M_ID, mErchant.getMid());
				startActivity(intent);
				break;
			case R.id.merchant_details_phone:
				int height1 = vTitle_view.getHeight()
						+ AppConfig
								.getStatusBarHeight(MerchantDetailsActivity.this);
				;
				mViewPopWindow.showAtLocation(vTitle_view, Gravity.TOP, 0,
						height1);

				break;
			case R.id.shop_details_address:
				Bundle bundle3 = new Bundle();
				bundle3.putSerializable(M_DETAILS, mErchant);
				HelperUi.startActivity(MerchantDetailsActivity.this,
						MapNavigationActivity.class, bundle3);
				break;
			case R.id.dev_equipment_img:
				changeImageShow();
				break;
			case R.id.service_equipment_img:
				changeImageShow();
				break;
			case R.id.more_discuss:
				HelperUi.startActivity(MerchantDetailsActivity.this,
						CommentActivity.class);
				break;
			case R.id.details_share:
				int height = vTitle_view.getHeight()
						+ AppConfig
								.getStatusBarHeight(MerchantDetailsActivity.this);
				mPopupWindow
						.showAtLocation(vTitle_view, Gravity.TOP, 0, height);
				break;
			case R.id.collect_btn:
				mDialog = new ProgressDialog(MerchantDetailsActivity.this);
				mDialog.setTitle("提示");
				mDialog.setMessage("处理中...");
				collectMerchant();
				break;
			default:
				break;
			}
		}
	};

	private void changeImageShow() {
		Bundle bundle = new Bundle();
		bundle.putSerializable(ImageShowActivity.IMAGES,
				(Serializable) merchantImages);
		HelperUi.startActivity(MerchantDetailsActivity.this,
				ImageShowActivity.class, bundle);
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
	public void onWechatButtonClick() {
		Wechat.ShareParams sp = new Wechat.ShareParams();
		sp.title = "优优运动俱乐部";
		sp.text = "和你一起分享运动的快乐";
		sp.shareType = Platform.SHARE_TEXT;
		sp.url = "http://www.huiyoumall.cn/";
		sp.imageUrl = mErchant.getCover_image();
		Platform wc = ShareSDK.getPlatform(MerchantDetailsActivity.this,
				Wechat.NAME);
		wc.setPlatformActionListener(MerchantDetailsActivity.this);
		wc.share(sp);
	}

	@Override
	public void onWechatmomentButtonClick() {
		WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
		sp.title = "优优运动俱乐部";
		sp.text = "和你一起分享运动的快乐";
		sp.shareType = Platform.SHARE_TEXT;
		sp.url = "http://www.huiyoumall.cn/";
		sp.imageUrl = mErchant.getCover_image();
		Platform wm = ShareSDK.getPlatform(MerchantDetailsActivity.this,
				WechatMoments.NAME);
		wm.setPlatformActionListener(MerchantDetailsActivity.this);
		wm.share(sp);
	}

	@Override
	public void onQqButtonClick() {
		QQ.ShareParams sp = new QQ.ShareParams();
		sp.title = "羽毛球馆";
		// sp.titleUrl = "和你一起运动分享生活快乐"; // 标题的超链接
		sp.text = "和你一起运动分享生活快乐";
		// sp.imageUrl = "http://www.someserver.com/测试图片网络地址.jpg";

		Platform qq = ShareSDK.getPlatform(MerchantDetailsActivity.this,
				QQ.NAME);
		qq.setPlatformActionListener(MerchantDetailsActivity.this); // 设置分享事件回调
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
		Platform sina = ShareSDK.getPlatform(MerchantDetailsActivity.this,
				SinaWeibo.NAME);
		// 设置分享监听
		sina.setPlatformActionListener(MerchantDetailsActivity.this);
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
			Toast.makeText(MerchantDetailsActivity.this, text,
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

	@Override
	public void onLeftClickListener() {

	}

	@Override
	public void onRightClickListener() {
		String phone = vMerchant_phone_txt.getText().toString().trim();
		if (!StringUtils.isEmpty(phone)) {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ phone));
			MerchantDetailsActivity.this.startActivity(intent);// 内部类
		}
	}
}
