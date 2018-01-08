package com.huiyoumall.uu;

import java.io.File;

import org.kymjs.kjframe.http.KJAsyncTask;
import org.kymjs.kjframe.utils.FileUtils;
import org.kymjs.kjframe.utils.PreferenceHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.huiyoumall.uu.activity.MainActivity;

public class AppStartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 防止第三方跳转时出现双实例
		Activity aty = AppManager.getActivity(MainActivity.class);
		if (aty != null && !aty.isFinishing()) {
			finish();
		}
		// SystemTool.gc(this); //针对性能好的手机使用，加快应用相应速度
		final View view = View.inflate(this, R.layout.activity_start, null);
		setContentView(view);
		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
		aa.setDuration(800);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
	}

	/**
	 * 跳转到...
	 */
	private void redirectTo() {
		// 有服务开启服务
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		int cacheVersion = PreferenceHelper.readInt(this, "first_install",
				"first_install", -1);
		// int currentVersion = TDevice.getVersionCode();
		// if (cacheVersion < currentVersion) {
		// PreferenceHelper.write(this, "first_install", "first_install",
		// currentVersion);
		// cleanImageCache();
		// }
	}

	private void cleanImageCache() {
		final File folder = FileUtils.getSaveFolder("HuiYou/imagecache");
		KJAsyncTask.execute(new Runnable() {
			@Override
			public void run() {
				for (File file : folder.listFiles()) {
					file.delete();
				}
			}
		});
	}
}
