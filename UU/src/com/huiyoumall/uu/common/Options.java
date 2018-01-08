package com.huiyoumall.uu.common;

import android.graphics.Bitmap;

import com.huiyoumall.uu.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * ͼƬ���洦���� ������
 * 
 * @author ASUS
 * 
 */
public class Options {
	/** �б����õ���ͼƬ�������� */
	public static DisplayImageOptions getLargeListOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				// // ����ͼƬ�������ڼ���ʾ��ͼƬ
				// .showImageOnLoading(R.drawable.small_image_holder_listpage)
				// // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				// .showImageForEmptyUri(R.drawable.small_image_holder_listpage)
				// // ����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
				// .showImageOnFail(R.drawable.small_image_holder_listpage)
				.cacheInMemory(true)
				// �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.cacheOnDisc(true)
				// �������ص�ͼƬ�Ƿ񻺴���SD����
				.considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				// ����ͼƬ����εı��뷽ʽ��ʾ
				.bitmapConfig(Bitmap.Config.RGB_565)
				// ����ͼƬ�Ľ�������
				// .decodingOptions(android.graphics.BitmapFactory.Options
				// decodingOptions)//����ͼƬ�Ľ�������
				.considerExifParams(true)
				// ����ͼƬ����ǰ���ӳ�
				// .delayBeforeLoading(int delayInMillis)//int
				// delayInMillisΪ�����õ��ӳ�ʱ��
				// ����ͼƬ���뻺��ǰ����bitmap��������
				// ��preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)
				// ����ͼƬ������ǰ�Ƿ����ã���λ
				// .displayer(new RoundedBitmapDisplayer(20))//�Ƿ�����ΪԲ�ǣ�����Ϊ����
				.showImageForEmptyUri(R.drawable.image_empty_large_bg)
				.showImageOnFail(R.drawable.image_empty_large_bg)
				.displayer(new FadeInBitmapDisplayer(100))// ����
				.build();
		return options;
	}

	public static DisplayImageOptions getListOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				// // ����ͼƬ�������ڼ���ʾ��ͼƬ
				// .showImageOnLoading(R.drawable.small_image_holder_listpage)
				// // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				// .showImageForEmptyUri(R.drawable.small_image_holder_listpage)
				// // ����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
				// .showImageOnFail(R.drawable.small_image_holder_listpage)
				.cacheInMemory(true)
				// �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.cacheOnDisc(true)
				// �������ص�ͼƬ�Ƿ񻺴���SD����
				.considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				// ����ͼƬ����εı��뷽ʽ��ʾ
				.bitmapConfig(Bitmap.Config.RGB_565)
				// ����ͼƬ�Ľ�������
				// .decodingOptions(android.graphics.BitmapFactory.Options
				// decodingOptions)//����ͼƬ�Ľ�������
				.considerExifParams(true)
				// ����ͼƬ����ǰ���ӳ�
				// .delayBeforeLoading(int delayInMillis)//int
				// delayInMillisΪ�����õ��ӳ�ʱ��
				// ����ͼƬ���뻺��ǰ����bitmap��������
				// ��preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)
				// ����ͼƬ������ǰ�Ƿ����ã���λ
				// .displayer(new RoundedBitmapDisplayer(20))//�Ƿ�����ΪԲ�ǣ�����Ϊ����
				.showImageForEmptyUri(R.drawable.image_empty_bg)
				.showImageOnFail(R.drawable.image_empty_bg)
				.displayer(new FadeInBitmapDisplayer(100))// ����
				.build();
		return options;
	}
}