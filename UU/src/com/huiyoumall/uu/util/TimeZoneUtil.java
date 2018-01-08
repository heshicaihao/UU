package com.huiyoumall.uu.util;

import java.util.Date;
import java.util.TimeZone;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * @author hsy
 * 
 * @date 2015年25月日
 */
public class TimeZoneUtil {

	/**
	 * 判断用户的设备时区是否为东八区（中国） 2015年25月日
	 * 
	 * @return
	 */
	public static boolean isInEasternEightZones() {
		boolean defaultVaule = true;
		if (TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08"))
			defaultVaule = true;
		else
			defaultVaule = false;
		return defaultVaule;
	}

	/**
	 * 根据不同时区，转换时间 2014年7月31日
	 * 
	 * @param time
	 * @return
	 */
	public static Date transformTime(Date date, TimeZone oldZone,
			TimeZone newZone) {
		Date finalDate = null;
		if (date != null) {
			int timeOffset = oldZone.getOffset(date.getTime())
					- newZone.getOffset(date.getTime());
			finalDate = new Date(date.getTime() - timeOffset);
		}
		return finalDate;
	}

	private static final int SECONDS = 60; // 秒数
	private static final int MINUTES = 60 * 60; // 小时
	private long first = 0, twice = 0, third = 0;
	private long mtmp = 0, mtmp2 = 0;

	public class Counter extends CountDownTimer {

		private TextView textView;

		public Counter(long millisInFuture, long countDownInterval,
				TextView view) {
			super(millisInFuture, countDownInterval);
			this.textView = view;
		}

		@Override
		public void onFinish() {

		}

		private void setText(String str) {
			textView.setText(str);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// 获取当前时间总秒数
			first = millisUntilFinished / 1000;
			if (first < SECONDS) { // 小于一分钟 只显示秒
				setText("00:00:" + (first < 10 ? "0" + first : first));
			} else if (first < MINUTES) { // 大于或等于一分钟，但小于一小时，显示分钟
				twice = first % 60; // 将秒转为分钟取余，余数为秒
				mtmp = first / 60; // 将秒数转为分钟
				if (twice == 0) {
					setText("00:" + (mtmp < 10 ? "0" + mtmp : mtmp) + ":00"); // 只显示分钟
				} else {
					setText("00:" + (mtmp < 10 ? "0" + mtmp : mtmp) + ":"
							+ (twice < 10 ? "0" + twice : twice)); // 显示分钟和秒
				}
			} else {
				twice = first % 3600; // twice为余数 如果为0则小时为整数
				mtmp = first / 3600;
				if (twice == 0) {
					// 只剩下小时
					setText("0" + first / 3600 + ":00:00");
				} else {
					if (twice < SECONDS) { // twice小于60 为秒
						setText((mtmp < 10 ? "0" + mtmp : mtmp) + ":00:"
								+ (twice < 10 ? "0" + twice : twice)); // 显示小时和秒
					} else {
						third = twice % 60; // third为0则剩下分钟 否则还有秒
						mtmp2 = twice / 60;
						if (third == 0) {
							setText((mtmp < 10 ? "0" + mtmp : mtmp) + ":"
									+ (mtmp2 < 10 ? "0" + mtmp2 : mtmp2)
									+ ":00");
						} else {
							setText((mtmp < 10 ? "0" + mtmp : mtmp) + ":"
									+ (mtmp2 < 10 ? "0" + mtmp2 : mtmp2) + ":"
									+ third); // 还有秒
						}
					}
				}
			}
		}
	}
}
