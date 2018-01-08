package com.huiyoumall.uu.ad;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.activity.MerchantDetailsActivity;
import com.huiyoumall.uu.common.Options;
import com.huiyoumall.uu.entity.Act;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 首页广告 图片适配器
 */
public class AdImagePagerAdapter extends BaseAdapter {

	private Context context;
	private List<Act> mAds;
	private int size;
	private boolean isInfiniteLoop;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	public AdImagePagerAdapter(Context context, List<Act> mAds) {
		this.context = context;
		this.mAds = mAds;
		if (mAds != null) {
			this.size = mAds.size();
		}
		isInfiniteLoop = false;
		options = Options.getLargeListOptions();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : mAds.size();
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = holder.imageView = new ImageView(context);
			holder.imageView
					.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
			holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		imageLoader.displayImage((String) this.mAds.get(getPosition(position))
				.getImg(), holder.imageView, options);

		holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/*
				 * if (TextUtils.isEmpty(url)) {
				 * holder.imageView.setEnabled(false); return; }
				 */
				// Bundle bundle = new Bundle();
				//
				// bundle.putString("url", url);
				// bundle.putString("title", title);
				// Intent intent = new Intent(context, BaseWebActivity.class);
				// intent.putExtras(bundle);
				//
				// context.startActivity(intent);
				Toast.makeText(context, "点击了第" + getPosition(position) + "张图片",
						0).show();

				Bundle bundle = new Bundle();
				bundle.putString(MerchantDetailsActivity.M_ID,
						mAds.get(getPosition(position)).getMid());
				HelperUi.startActivity(context, MerchantDetailsActivity.class,
						bundle);
			}
		});

		return view;
	}

	private static class ViewHolder {

		ImageView imageView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public AdImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

}
