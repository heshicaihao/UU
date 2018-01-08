package com.huiyoumall.uu.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huiyoumall.uu.R;
import com.huiyoumall.uu.common.Options;
import com.huiyoumall.uu.entity.MerchantImage;
import com.huiyoumall.uu.widget.ImageShowViewPager;
import com.huiyoumall.uu.widget.TouchImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 图片展示的PagerAdapter 适配器
 */
public class ImagePagerAdapter extends PagerAdapter {
	Context context;
	List<MerchantImage> mImages;
	LayoutInflater inflater = null;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	// view内控件
	TouchImageView full_image;
	// TextView progress_text;
	ProgressBar progress;
	TextView retry;

	public ImagePagerAdapter(Context context, List<MerchantImage> mImages_) {
		this.context = context;
		this.mImages = mImages_;
		inflater = LayoutInflater.from(context);
		options = Options.getLargeListOptions();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	/** 动态加载数据 */
	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
		((ImageShowViewPager) container).mCurrentView = ((TouchImageView) ((View) object)
				.findViewById(R.id.full_image));
	}

	@Override
	public int getCount() {
		return mImages == null ? 0 : mImages.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@SuppressWarnings("static-access")
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = inflater.from(context).inflate(
				R.layout.view_imageshow_details, null);
		full_image = (TouchImageView) view.findViewById(R.id.full_image);
		// progress_text = (TextView) view.findViewById(R.id.progress_text);
		progress = (ProgressBar) view.findViewById(R.id.progress);
		retry = (TextView) view.findViewById(R.id.retry);// 加载失败
		// progress_text.setText(String.valueOf(position));
		progress.setVisibility(View.GONE);
		retry.setVisibility(View.GONE);
		imageLoader.displayImage(mImages.get(position).getUrl(), full_image,
				options, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						view.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						view.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						view.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						view.setVisibility(View.VISIBLE);
					}
				});
		// imageLoader.displayImage(imgsUrls.get(position), full_image, options,
		// new ImageLoadingListener() {
		//
		// @Override
		// public void onLoadingStarted(String imageUri, View view) {
		// progress.setVisibility(View.VISIBLE);
		// // progress_text.setVisibility(View.VISIBLE);
		// full_image.setVisibility(View.GONE);
		// retry.setVisibility(View.GONE);
		// }
		//
		// @Override
		// public void onLoadingFailed(String imageUri, View view,
		// FailReason failReason) {
		// progress.setVisibility(View.GONE);
		// // progress_text.setVisibility(View.GONE);
		// full_image.setVisibility(View.GONE);
		// retry.setVisibility(View.VISIBLE);
		// }
		//
		// @Override
		// public void onLoadingComplete(String imageUri, View view,
		// Bitmap loadedImage) {
		// progress.setVisibility(View.GONE);
		// // progress_text.setVisibility(View.GONE);
		// full_image.setVisibility(View.VISIBLE);
		// // full_image.setImageBitmap(loadedImage);
		// retry.setVisibility(View.GONE);
		// }
		//
		// @Override
		// public void onLoadingCancelled(String imageUri, View view) {
		// progress.setVisibility(View.GONE);
		// // progress_text.setVisibility(View.GONE);
		// full_image.setVisibility(View.GONE);
		// retry.setVisibility(View.VISIBLE);
		// }
		// });
		((ViewPager) container).addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}
}
