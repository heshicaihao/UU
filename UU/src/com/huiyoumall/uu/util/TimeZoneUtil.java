package com.huiyoumall.uu.util;

import java.util.Date;
import java.util.TimeZone;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * @author hsy
 * 
 * @date 2015��25����
 */
public class TimeZoneUtil {

	/**
	 * �ж��û����豸ʱ���Ƿ�Ϊ���������й��� 2015��25����
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
	 * ���ݲ�ͬʱ����ת��ʱ�� 2014��7��31��
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

	private static final int SECONDS = 60; // ����
	private static final int MINUTES = 60 * 60; // Сʱ
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
			// ��ȡ��ǰʱ��������
			first = millisUntilFinished / 1000;
			if (first < SECONDS) { // С��һ���� ֻ��ʾ��
				setText("00:00:" + (first < 10 ? "0" + first : first));
			} else if (first < MINUTES) { // ���ڻ����һ���ӣ���С��һСʱ����ʾ����
				twice = first % 60; // ����תΪ����ȡ�࣬����Ϊ��
				mtmp = first / 60; // ������תΪ����
				if (twice == 0) {
					setText("00:" + (mtmp < 10 ? "0" + mtmp : mtmp) + ":00"); // ֻ��ʾ����
				} else {
					setText("00:" + (mtmp < 10 ? "0" + mtmp : mtmp) + ":"
							+ (twice < 10 ? "0" + twice : twice)); // ��ʾ���Ӻ���
				}
			} else {
				twice = first % 3600; // twiceΪ���� ���Ϊ0��СʱΪ����
				mtmp = first / 3600;
				if (twice == 0) {
					// ֻʣ��Сʱ
					setText("0" + first / 3600 + ":00:00");
				} else {
					if (twice < SECONDS) { // twiceС��60 Ϊ��
						setText((mtmp < 10 ? "0" + mtmp : mtmp) + ":00:"
								+ (twice < 10 ? "0" + twice : twice)); // ��ʾСʱ����
					} else {
						third = twice % 60; // thirdΪ0��ʣ�·��� ��������
						mtmp2 = twice / 60;
						if (third == 0) {
							setText((mtmp < 10 ? "0" + mtmp : mtmp) + ":"
									+ (mtmp2 < 10 ? "0" + mtmp2 : mtmp2)
									+ ":00");
						} else {
							setText((mtmp < 10 ? "0" + mtmp : mtmp) + ":"
									+ (mtmp2 < 10 ? "0" + mtmp2 : mtmp2) + ":"
									+ third); // ������
						}
					}
				}
			}
		}
	}
}
