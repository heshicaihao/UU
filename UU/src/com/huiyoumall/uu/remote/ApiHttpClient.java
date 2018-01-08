package com.huiyoumall.uu.remote;

import java.util.Locale;

import org.apache.http.client.params.ClientPNames;

import android.content.Context;
import android.util.Log;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.util.TLog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ApiHttpClient {

	public final static String HOST = "192.168.1.133";
	private static String API_URL = "http://192.168.1.133/huiyoumall/shop/index.php?%s";
	public static final String DELETE = "DELETE";
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static AsyncHttpClient client;

	public ApiHttpClient() {
	}

	public static AsyncHttpClient getHttpClient() {
		return client;
	}

	public static void cancelAll(Context context) {
		client.cancelRequests(context, true);
	}

	public static void clearUserCookies(Context context) {
		// (new HttpClientCookieStore(context)).a();
	}

	public static void delete(String partUrl, AsyncHttpResponseHandler handler) {
		client.delete(getAbsoluteApiUrl(partUrl), handler);
		log(new StringBuilder("DELETE ").append(partUrl).toString());
	}

	public static void get(String partUrl, AsyncHttpResponseHandler handler) {
		client.get(getAbsoluteApiUrl(partUrl), handler);
		log(new StringBuilder("GET ").append(partUrl).toString());
	}

	public synchronized static void get(String partUrl, RequestParams params,
			AsyncHttpResponseHandler handler) {
		client.get(getAbsoluteApiUrl(partUrl), params, handler);
		log(new StringBuilder("GET ").append(partUrl).append("&")
				.append(params).toString());
	}

	public static String getAbsoluteApiUrl(String partUrl) {
		String url = String.format(API_URL, partUrl);// http://192.168.1.114/huiyou/shop/index.php?act=phoneinterface&op=index
		Log.d("BASE_CLIENT", "request:" + url);// http://192.168.1.114/huiyoumall/shop/index.php?act=phoneinterface&op=index
		return url;
	}

	// public static String getApiUrl() {
	// return API_URL;
	// }

	public static void getDirect(String url, AsyncHttpResponseHandler handler) {
		client.get(url, handler);
		log(new StringBuilder("GET ").append(url).toString());
	}

	public static void log(String log) {
		Log.d("BaseApi", log);
		TLog.log("Test", log);
	}

	public static void post(String partUrl, AsyncHttpResponseHandler handler) {
		client.post(getAbsoluteApiUrl(partUrl), handler);
		log(new StringBuilder("POST ").append(partUrl).toString());
	}

	public static void post(String partUrl, RequestParams params,
			AsyncHttpResponseHandler handler) {
		client.post(getAbsoluteApiUrl(partUrl), params, handler);
		log(new StringBuilder("POST ").append(partUrl).append("&")
				.append(params).toString());
	}

	public static void postDirect(String url, RequestParams params,
			AsyncHttpResponseHandler handler) {
		client.post(url, params, handler);
		log(new StringBuilder("POST ").append(url).append("&").append(params)
				.toString());
	}

	public static void put(String partUrl, AsyncHttpResponseHandler handler) {
		client.put(getAbsoluteApiUrl(partUrl), handler);
		log(new StringBuilder("PUT ").append(partUrl).toString());
	}

	public static void put(String partUrl, RequestParams params,
			AsyncHttpResponseHandler handler) {
		client.put(getAbsoluteApiUrl(partUrl), params, handler);
		log(new StringBuilder("PUT ").append(partUrl).append("&")
				.append(params).toString());
	}

	public static void setApiUrl(String apiUrl) {
		API_URL = apiUrl;
	}

	public static void setHttpClient(AsyncHttpClient c) {
		client = c;
		client.addHeader("Accept-Language", Locale.getDefault().toString());
		client.addHeader("Host", HOST);
		client.addHeader("Connection", "Keep-Alive");
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

		setUserAgent(ApiClientHelper.getUserAgent(AppContext.getInstance()));
	}

	public static void setUserAgent(String userAgent) {
		client.setUserAgent(userAgent);
	}

	public static void setCookie(String cookie) {
		client.addHeader("Cookie", cookie);
	}

	private static String appCookie;

	public static void cleanCookie() {
		appCookie = "";
	}

	public static String getCookie(AppContext appContext) {
		if (appCookie == null || appCookie == "") {
			appCookie = appContext.getProperty("cookie");
		}
		return appCookie;
	}

}
