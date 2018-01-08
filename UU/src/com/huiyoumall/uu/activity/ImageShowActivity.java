package com.huiyoumall.uu.activity;

import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.huiyoumall.uu.R;
import com.huiyoumall.uu.adapter.ImagePagerAdapter;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.entity.MerchantImage;
import com.huiyoumall.uu.widget.ImageShowViewPager;

/**
 * 图片展示界面
 * 
 * @author ASUS
 * 
 */
public class ImageShowActivity extends BaseActivity {
	public static String IMAGES = "images";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImages = (List<MerchantImage>) getIntent().getExtras().get(IMAGES);
		setContentView(R.layout.activity_image_details);
	}

	private ImageShowViewPager image_pager;
	private TextView page_number;
	private ImagePagerAdapter mAdapter;
	private List<MerchantImage> mImages;

	@Override
	public void findViewById() {
		image_pager = (ImageShowViewPager) findViewById(R.id.view_pager);
		page_number = (TextView) findViewById(R.id.img_num);
	}

	@Override
	public void initView() {
		if (mImages != null) {
			page_number.setText("1" + "/" + mImages.size());
			if (mImages.size() != 0) {
				mAdapter = new ImagePagerAdapter(this, mImages);
				image_pager.setAdapter(mAdapter);
			}
			image_pager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					page_number.setText((arg0 + 1) + "/" + mImages.size());
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {

				}
			});
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onClickBack(View view) {
		this.finish();
	}
}
