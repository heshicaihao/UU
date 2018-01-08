package com.huiyoumall.uu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HelperUi {

	private static String lastToast = "";
	private static long lastToastTime;

	public void showDiolg() {
	}

	public static void startActivity(Context context, Class<?> activity2,
			Bundle bundle) {
		Intent intent = new Intent(context, activity2);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	public static void startActivity(Context context, Class<?> activity2) {
		Intent intent = new Intent(context, activity2);
		context.startActivity(intent);
	}

	public static void showToastLong(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void showToastLong(Context context, int id) {
		Toast.makeText(context, id, Toast.LENGTH_LONG).show();
	}

	public static void showToastShort(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void showToastShort(Context context, int id) {
		Toast.makeText(context, id, Toast.LENGTH_LONG).show();
	}

	public static void showToast(Context context, String message, int duration,
			int icon, int gravity) {
		if (message != null && !message.equalsIgnoreCase("")) {
			long time = System.currentTimeMillis();
			if (!message.equalsIgnoreCase(lastToast)
					|| Math.abs(time - lastToastTime) > 2000) {
				View view = LayoutInflater.from(context).inflate(
						R.layout.view_toast, null);
				((TextView) view.findViewById(R.id.title_tv)).setText(message);
				if (icon != 0) {
					((ImageView) view.findViewById(R.id.icon_iv))
							.setImageResource(icon);
					((ImageView) view.findViewById(R.id.icon_iv))
							.setVisibility(View.VISIBLE);
				}
				Toast toast = new Toast(context);
				toast.setView(view);
				if (gravity == Gravity.CENTER) {
					toast.setGravity(gravity, 0, 0);
				} else {
					toast.setGravity(gravity, 0, 35);
				}

				toast.setDuration(duration);
				toast.show();
				lastToast = message;
				lastToastTime = System.currentTimeMillis();
			}
		}
	}
}
