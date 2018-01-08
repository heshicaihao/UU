package com.huiyoumall.uu.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.entity.Activities.ActivityMember;
import com.huiyoumall.uu.image.SmartImageView;
import com.huiyoumall.uu.remote.UURemoteApi;
import com.huiyoumall.uu.util.StringUtils;
import com.huiyoumall.uu.widget.EmptyLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 活动的 个人详情 界面
 * 
 * @author ASUS
 * 
 */
public class HuodongPersonDetailsActivity extends BaseActivity {

	public static String MEMBER_DETAILS = "details";
	public static String IS_MY_BOOK = "is_my_book";
	public static String IS_OVERDUE = "isOerdue";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		member = (ActivityMember) bundle.getSerializable(MEMBER_DETAILS);
		is_my_book = bundle.getBoolean(IS_MY_BOOK);
		is_Overdue = bundle.getBoolean(IS_OVERDUE);
		setContentView(R.layout.activity_huodong_person_details);
	}

	private TextView vTitle;
	private ActivityMember member;
	private boolean is_my_book;// 是否我发起的
	private boolean is_Overdue;// 是否已经逾期
	private EmptyLayout merember_detail_empter_view;
	private SmartImageView member_avatar;
	private TextView member_age_text;
	private TextView member_name_text;
	private TextView member_place_text;
	private TextView member_tel_text;
	private TextView member_signature_text;
	private ListView often_merchant_list;
	private AppContext app;

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		merember_detail_empter_view = (EmptyLayout) findViewById(R.id.merember_detail_empter_view);
		member_avatar = (SmartImageView) findViewById(R.id.member_avatar);
		member_age_text = (TextView) findViewById(R.id.member_age_text);
		member_name_text = (TextView) findViewById(R.id.member_name_text);
		member_place_text = (TextView) findViewById(R.id.member_place_text);
		member_tel_text = (TextView) findViewById(R.id.member_tel_text);
		member_signature_text = (TextView) findViewById(R.id.member_signature_text);
		often_merchant_list = (ListView) findViewById(R.id.often_merchant_list);
	}

	@Override
	public void initView() {
		vTitle.setText("个人详情");
		app = (AppContext) getApplicationContext();
		member_avatar.setImageUrl(member.getMember_avatar());
		member_age_text.setText(member.getMember_age() + "岁");
		if (member.getMember_sex().equals("男")) {
			Drawable sex_img = getResources().getDrawable(R.drawable.ic_male);
			sex_img.setBounds(0, 0, sex_img.getMinimumWidth(),
					sex_img.getMinimumHeight());
			member_age_text.setCompoundDrawables(sex_img, null, null, null);
		} else {
			Drawable sex_img = getResources().getDrawable(R.drawable.ic_female);
			sex_img.setBounds(0, 0, sex_img.getMinimumWidth(),
					sex_img.getMinimumHeight());
			member_age_text.setCompoundDrawables(sex_img, null, null, null);
		}
		member_name_text.setText(member.getMember_name());
		member_place_text.setText(member.getMember_provinceid() + " "
				+ member.getMember_cityid());
		if (is_my_book && !is_Overdue) {
			member_tel_text.setVisibility(View.VISIBLE);
			member_tel_text.setText(member.getTel());
		} else {
			member_tel_text.setVisibility(View.GONE);
		}
		member_signature_text.setText(member.getSignature());
		loadOfentMerchant();
	}

	private List<OftenGoMerchant> merchants;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			merember_detail_empter_view.setVisibility(View.GONE);
			if (msg.what == 1) {
				String str = (String) msg.obj;
				if (!StringUtils.isEmpty(str)) {
					try {
						JSONObject jsonObject = new JSONObject(str);
						JSONArray array = jsonObject.getJSONArray("list");
						if (array.length() > 0) {
							merchants = new ArrayList<OftenGoMerchant>();
							OftenGoMerchant merchant = null;
							for (int i = 0; i < array.length(); i++) {
								JSONObject object = array.getJSONObject(i);
								merchant = new OftenGoMerchant();
								merchant.image = object.getString("image");
								merchant.name = object.getString("name");
								merchant.mid = object.getInt("mid");
								merchant.sportid = object.getInt("sportid");
								merchant.place = object.getString("place");
								merchant.distance = object
										.getString("distance");
								merchants.add(merchant);
							}
							OftenGOMerchantAdapter adapter = new OftenGOMerchantAdapter(
									merchants);
							often_merchant_list.setAdapter(adapter);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		};
	};

	private void loadOfentMerchant() {
		String mLatlon = app.getLongitude() + "-" + app.getLatitude();
		final Message msg = Message.obtain();
		UURemoteApi.loadMyOftenGoMerchant(member.getMember_id(), mLatlon,
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

	class OftenGOMerchantAdapter extends BaseAdapter {

		List<OftenGoMerchant> merchants;
		LayoutInflater mInflater;

		public OftenGOMerchantAdapter(List<OftenGoMerchant> ms) {
			this.merchants = ms;
			this.mInflater = LayoutInflater
					.from(HuodongPersonDetailsActivity.this);
		}

		@Override
		public int getCount() {
			return merchants.size();
		}

		@Override
		public Object getItem(int position) {
			return merchants.get(position);
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
				convertView = mInflater.inflate(
						R.layout.item_member_oftengo_merchant, null);
				mHolderView.merchant_img = (SmartImageView) convertView
						.findViewById(R.id.merchant_img);
				mHolderView.merchant_name = (TextView) convertView
						.findViewById(R.id.merchant_name);
				mHolderView.merchant_place = (TextView) convertView
						.findViewById(R.id.merchant_place);
				mHolderView.merchant_distance = (TextView) convertView
						.findViewById(R.id.merchant_distance);
			} else {
				mHolderView = (HolderView) convertView.getTag();
			}

			OftenGoMerchant merchant = (OftenGoMerchant) getItem(position);
			mHolderView.merchant_img.setImageUrl(merchant.image);
			mHolderView.merchant_name.setText(merchant.name);
			mHolderView.merchant_place.setText(merchant.place);
			mHolderView.merchant_distance.setText(merchant.distance);

			return convertView;
		}

		class HolderView {
			SmartImageView merchant_img;
			TextView merchant_name;
			TextView merchant_place;
			TextView merchant_distance;
		}
	}

	class OftenGoMerchant {
		public int mid;
		public int sportid;
		public String image;
		public String name;
		public String place;
		public String distance;

	}
}
