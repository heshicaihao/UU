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
		// // ��ȡ�ֻ����ڵؾ�γ��
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
	 * ��õ�ǰapp���е�AppContext
	 * 
	 * @return
	 */
	public static AppContext getInstance() {
		return instance;
	}

	// ��ʼ����������
	private void init() {
		// ��ʼ����������
		AsyncHttpClient client = new AsyncHttpClient();
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
		client.setCookieStore(myCookieStore);
		ApiHttpClient.setHttpClient(client);
		ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));
	}

	/**
	 * ��õ�¼�û�����Ϣ
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
	 * �����¼��Ϣ
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
				setProperty("member_avatar", user.getMember_avatar());// �û�ͷ��-�ļ���
				setProperty("member_truename", user.getMember_truename());
				setProperty("member_sex", user.getMember_sex());
				setProperty("member_birthday", user.getMember_birthday());
				setProperty("member_provinceid", user.getMember_provinceid());// �Ƿ��ס�ҵ���Ϣ
				setProperty("member_cityid", user.getMember_cityid());
				setProperty("member_areaid", user.getMember_areaid());
				setProperty("member_areainfo", user.getMember_areainfo());
			}
		});
	}

	/**
	 * �õ���һ�ε�½
	 * 
	 * @return
	 */
	public String getFirstisLogin() {
		return getProperty("isFirst");
	}

	/**
	 * �����һ�ε�½��¼
	 */
	public void saveFirstisLogin(final String isFirst) {
		setProperties(new Properties() {
			{
				setProperty("isFirst", isFirst);
			}
		});
	}

	/**
	 * �����¼��Ϣ/�˳�
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
	 * ���汻ѡ�еĳ���
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
	 * �õ�ѡ�еĳ���
	 * 
	 * @return
	 */
	public String getSelectCity() {
		return getProperty("cityName");
	}

	/**
	 * �õ���λ�ĳ���
	 * 
	 * @return
	 */
	public String getLocationCity() {
		return getProperty("locationCity");
	}

	/**
	 * �õ���λ������
	 * 
	 * @return
	 */
	public String getLocationArea() {
		return getProperty("locationArea");
	}

	/**
	 * ���汻ѡ�е�����
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
	 * ���涨λ�ĳ���
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
	 * ���涨λ������
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
	 * �����������
	 */
	public void cleanSaveArea() {
		removeProperty("areaName");
	}

	/**
	 * �õ�ѡ�еĳ���
	 * 
	 * @return
	 */
	public String getSelectArea() {
		return getProperty("areaName");
	}

	/**
	 * ���澭��
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
	 * ����ά��
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
	 * �õ�����
	 * 
	 * @return
	 */
	public String getLongitude() {
		return getProperty("longitude");
	}

	/**
	 * �õ�ά��
	 * 
	 * @return
	 */

	public String getLatitude() {
		return getProperty("latitude");
	}

	/**
	 * ��ȡApp��װ����Ϣ
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
	 * ��ȡAppΨһ��ʶ
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
	 * ��ȡcookieʱ��AppConfig.CONF_COOKIE
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
