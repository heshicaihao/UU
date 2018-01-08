package com.huiyoumall.uu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 应用程序配置类，用于保存用户相关信息及设置
 * 
 * @author heshuyaun
 * 
 */
public class AppConfig {
	/** 微信官网申请到的AppId */
	public static final String APP_ID = "wxf2f565574a968187";
	public static final String API_KEY = "412fde4e9c2e2bb619514ecea142e449";
	public static final String MCH_ID = "1233848001";

	/** shareSdk 分享 第三方登录 */
	public static final String APP_KEY = "84e43fc70238";
	public static final String APP_SECRET = "7be46f27e13fbad53a6d68c1beae56e1";

	/**
	 * 保存经纬度
	 */
	public static double LONGITUDE = 0;// 经度
	public static double LATITUDE = 0;// 纬度

	public static final int PAGE_SIZE = 20;// 默认分页大小

	/** UI设计的基准宽度. */
	public static int UI_WIDTH = 720;

	/** UI设计的基准高度. */
	public static int UI_HEIGHT = 1280;

	/** UI设计的密度. */
	public static int UI_DENSITY = 2;

	private final static String APP_CONFIG = "config";
	private Context mContext;
	private static AppConfig appConfig;

	public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";
	// 默认存放图片的路径
	public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "HuiYou"
			+ File.separator + "hy_img" + File.separator;

	// 默认存放文件下载的路径
	public final static String DEFAULT_SAVE_FILE_PATH = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "HuiYou"
			+ File.separator + "download" + File.separator;

	public static AppConfig getAppConfig(Context context) {
		if (appConfig == null) {
			appConfig = new AppConfig();
			appConfig.mContext = context;
		}
		return appConfig;
	}

	/**
	 * 短信广播的action
	 */
	public final static String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	public static final String LOGIN_ACTION = "com.huiyoumall.uu.LOGIN_RECEIVER";

	/**
	 * 加载网页的初始化方法
	 * 
	 * @param webView
	 * @param url
	 */
	public static void initWebView(WebView webView, String url) {
		// 在WebView中加入网页
		webView.loadUrl(url);
		// 在WebView中使用网页的JavaScript
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		// 在WebView中使用网页的超链接，并本框架大开
		webView.setWebViewClient(new WebViewClient());
	}

	/**
	 * 获取Preference设置
	 */
	public static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public String get(String key) {
		Properties props = get();
		return (props != null) ? props.getProperty(key) : null;
	}

	public Properties get() {
		FileInputStream fis = null;
		Properties props = new Properties();
		try {
			// 读取files目录下的config
			// fis = activity.openFileInput(APP_CONFIG);

			// 读取app_config目录下的config
			File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			fis = new FileInputStream(dirConf.getPath() + File.separator
					+ APP_CONFIG);

			props.load(fis);
		} catch (Exception e) {
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return props;
	}

	private void setProps(Properties p) {
		FileOutputStream fos = null;
		try {
			// 把config建在files目录下
			// fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);
			// 把config建在(自定义)app_config的目录下
			File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			File conf = new File(dirConf, APP_CONFIG);
			fos = new FileOutputStream(conf);

			p.store(fos, null);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	public void set(Properties ps) {
		Properties props = get();
		props.putAll(ps);
		setProps(props);
	}

	public void set(String key, String value) {
		Properties props = get();
		props.setProperty(key, value);
		setProps(props);
	}

	public void remove(String... key) {
		Properties props = get();
		for (String k : key)
			props.remove(k);
		setProps(props);
	}

	/**
	 * 获取手机状态栏高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		java.lang.reflect.Field field = null;
		int x = 0;
		int statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
			return statusBarHeight;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusBarHeight;
	}

	public static Bitmap getGradeImg(Context context, String grade) {
		double g = Double.parseDouble(grade);
		Resources resources = context.getResources();
		Bitmap map = null;
		if (g == 0) {
			map = BitmapFactory.decodeResource(resources,
					R.drawable.ic_grade_00);
		} else if (0 < g && g <= 0.5) {
			map = BitmapFactory.decodeResource(resources,
					R.drawable.ic_grade_05);
		} else if (0.5 < g && g <= 1) {
			map = BitmapFactory.decodeResource(resources,
					R.drawable.ic_grade_10);
		} else if (1 < g && g <= 1.5) {
			map = BitmapFactory.decodeResource(resources,
					R.drawable.ic_grade_15);
		} else if (1.5 < g && g <= 2) {
			map = BitmapFactory.decodeResource(resources,
					R.drawable.ic_grade_20);
		} else if (2 < g && g <= 2.5) {
			map = BitmapFactory.decodeResource(resources,
					R.drawable.ic_grade_25);
		} else if (2.5 < g && g <= 3) {
			map = BitmapFactory.decodeResource(resources,
					R.drawable.ic_grade_30);
		} else if (3 < g && g <= 3.5) {
			map = BitmapFactory.decodeResource(resources,
					R.drawable.ic_grade_35);
		} else if (3.5 < g && g <= 4) {
			map = BitmapFactory.decodeResource(resources,
					R.drawable.ic_grade_40);
		} else if (4 < g && g <= 4.5) {
			map = BitmapFactory.decodeResource(resources,
					R.drawable.ic_grade_45);
		} else if (4.5 < g && g <= 5) {
			map = BitmapFactory.decodeResource(resources,
					R.drawable.ic_grade_50);
		}
		return map;
	}
}
