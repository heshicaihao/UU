package com.huiyoumall.uu;

import java.util.Properties;
import java.util.UUID;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.huiyoumall.uu.base.BaseApplication;
import com.huiyoumall.uu.common.GetCityJsonToDb;
import com.huiyoumall.uu.db.DataHelper;
import com.huiyoumall.uu.entity.User;
import com.huiyoumall.uu.remote.ApiHttpClient;
import com.huiyoumall.uu.util.StringUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

public class AppContext extends BaseApplication {

	private static AppContext instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		if (getFirstisLogin() == null) {
			DataHelper dh = new DataHelper(this);
			dh.insert(GetCityJsonToDb.getDatas(this));
			saveFirstisLogin("true");
		}
		GlobalParams.CONTEXT = this;
		init();
		// // 获取手机所在地经纬度
		// LocationService location = new LocationService(this);
		// if (location.oPenLocService()) {
		// saveLongitude(location.mLongTude + "");
		// saveLatitude(location.mLatiTude + "");
		// }
	}

	public int member_id;
	public String member_name;
	public boolean isLogin;

	/**
	 * 获得当前app运行的AppContext
	 * 
	 * @return
	 */
	public static AppContext getInstance() {
		return instance;
	}

	// 初始化网络请求
	private void init() {
		// 初始化网络请求
		AsyncHttpClient client = new AsyncHttpClient();
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
		client.setCookieStore(myCookieStore);
		ApiHttpClient.setHttpClient(client);
		ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));
	}

	/**
	 * 获得登录用户的信息
	 * 
	 * @return
	 */
	public User getisLoginUser() {

		User user = new User();
		user.setMember_id(StringUtils.toInt(getProperty("member_id"), 0));
		user.setMember_name(getProperty("member_name"));
		user.setMember_avatar(getProperty("member_avatar"));
		user.setMember_truename(getProperty("member_truename"));
		user.setMember_sex(getProperty("member_sex"));
		user.setMember_birthday(getProperty("member_birthday"));
		user.setMember_provinceid(getProperty("member_provinceid"));
		user.setMember_cityid(getProperty("member_cityid"));
		user.setMember_areaid(getProperty("member_areaid"));
		user.setMember_areainfo(getProperty("member_areainfo"));
		return user;
	}

	/**
	 * 保存登录信息
	 * 
	 * @param username
	 * @param pwd
	 */
	public void saveUserInfo(final User user) {
		this.member_id = user.getMember_id();
		this.member_name = user.getMember_name();
		this.isLogin = true;
		setProperties(new Properties() {
			{
				setProperty("member_id", String.valueOf(user.getMember_id()));
				setProperty("member_name", user.getMember_name());
				setProperty("member_avatar", user.getMember_avatar());// 用户头像-文件名
				setProperty("member_truename", user.getMember_truename());
				setProperty("member_sex", user.getMember_sex());
				setProperty("member_birthday", user.getMember_birthday());
				setProperty("member_provinceid", user.getMember_provinceid());// 是否记住我的信息
				setProperty("member_cityid", user.getMember_cityid());
				setProperty("member_areaid", user.getMember_areaid());
				setProperty("member_areainfo", user.getMember_areainfo());
			}
		});
	}

	/**
	 * 得到第一次登陆
	 * 
	 * @return
	 */
	public String getFirstisLogin() {
		return getProperty("isFirst");
	}

	/**
	 * 保存第一次登陆记录
	 */
	public void saveFirstisLogin(final String isFirst) {
		setProperties(new Properties() {
			{
				setProperty("isFirst", isFirst);
			}
		});
	}

	/**
	 * 清除登录信息/退出
	 */
	public void cleanisLoginInfo() {
		this.member_id = 0;
		this.member_name = "";
		this.isLogin = false;
		removeProperty("member_id", "member_name", "member_avatar",
				"member_truename", "member_sex", "member_birthday",
				"member_provinceid", "member_cityid", "member_areaid",
				"member_areainfo");
	}

	/**
	 * 保存被选中的城市
	 * 
	 * @param cityName
	 */
	public void saveSelectCity(final String cityName) {
		setProperties(new Properties() {
			{
				setProperty("cityName", cityName);
			}
		});
	}

	/**
	 * 得到选中的城市
	 * 
	 * @return
	 */
	public String getSelectCity() {
		return getProperty("cityName");
	}

	/**
	 * 得到定位的城市
	 * 
	 * @return
	 */
	public String getLocationCity() {
		return getProperty("locationCity");
	}

	/**
	 * 得到定位的区域
	 * 
	 * @return
	 */
	public String getLocationArea() {
		return getProperty("locationArea");
	}

	/**
	 * 保存被选中的区域
	 * 
	 * @param cityName
	 */
	public void saveSelectArea(final String areaName) {
		setProperties(new Properties() {
			{
				setProperty("areaName", areaName);
			}
		});
	}

	/**
	 * 保存定位的城市
	 * 
	 * @param cityName
	 */
	public void saveLocationCity(final String location) {
		setProperties(new Properties() {
			{
				setProperty("locationCity", location);
			}
		});
	}

	/**
	 * 保存定位的区域
	 * 
	 * @param cityName
	 */
	public void saveLocationArea(final String location) {
		setProperties(new Properties() {
			{
				setProperty("locationArea", location);
			}
		});
	}

	/**
	 * 清除保存区域
	 */
	public void cleanSaveArea() {
		removeProperty("areaName");
	}

	/**
	 * 得到选中的城市
	 * 
	 * @return
	 */
	public String getSelectArea() {
		return getProperty("areaName");
	}

	/**
	 * 保存经度
	 * 
	 * @param cityName
	 */
	public void saveLongitude(final String longitude) {
		setProperties(new Properties() {
			{
				setProperty("longitude", longitude);
			}
		});
	}

	/**
	 * 保存维度
	 * 
	 * @param cityName
	 */
	public void saveLatitude(final String latitude) {
		setProperties(new Properties() {
			{
				setProperty("latitude", latitude);
			}
		});
	}

	/**
	 * 得到经度
	 * 
	 * @return
	 */
	public String getLongitude() {
		return getProperty("longitude");
	}

	/**
	 * 得到维度
	 * 
	 * @return
	 */

	public String getLatitude() {
		return getProperty("latitude");
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	/**
	 * 获取App唯一标识
	 * 
	 * @return
	 */
	public String getAppId() {
		String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
		if (StringUtils.isEmpty(uniqueID)) {
			uniqueID = UUID.randomUUID().toString();
			setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
		}
		return uniqueID;
	}

	/**
	 * 获取cookie时传AppConfig.CONF_COOKIE
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		String res = AppConfig.getAppConfig(this).get(key);
		return res;
	}

	public void removeProperty(String... key) {
		AppConfig.getAppConfig(this).remove(key);
	}

	public void setProperties(Properties ps) {
		AppConfig.getAppConfig(this).set(ps);
	}

	public Properties getProperties() {
		return AppConfig.getAppConfig(this).get();
	}

	public void setProperty(String key, String value) {
		AppConfig.getAppConfig(this).set(key, value);
	}
}
