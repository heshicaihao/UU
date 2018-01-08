package com.huiyoumall.uu.remote;

import com.huiyoumall.uu.AppContext;

public class ApiClientHelper {

	/**
	 * �������ķ�������ݵ�userAgent
	 * 
	 * @param appContext
	 * @return
	 */
	public static String getUserAgent(AppContext appContext) {
		StringBuilder ua = new StringBuilder("UU.NET");
		ua.append('/' + appContext.getPackageInfo().versionName + '_'
				+ appContext.getPackageInfo().versionCode);// app�汾��Ϣ
		ua.append("/Android");// �ֻ�ϵͳƽ̨
		ua.append("/" + android.os.Build.VERSION.RELEASE);// �ֻ�ϵͳ�汾
		ua.append("/" + android.os.Build.MODEL); // �ֻ��ͺ�
		ua.append("/" + appContext.getAppId());// �ͻ���Ψһ��ʶ
		return ua.toString();
	}
}
