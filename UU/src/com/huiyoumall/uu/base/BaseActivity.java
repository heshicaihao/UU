package com.huiyoumall.uu.base;

import java.io.Serializable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.huiyoumall.uu.util.ACache;
import com.huiyoumall.uu.util.StringUtils;

public abstract class BaseActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		mContext = this;
	}

	ProgressDialog progress;
	public Activity mActivity;
	private Context mContext;

	/**
	 * fragment����
	 */
	protected FragmentManager mFragmentManager;

	/**
	 * ��ȡ�ؼ�id
	 */
	public abstract void findViewById();

	public void activitybackview(View view) {
		this.finish();
	}

	/**
	 * ��ʼ������
	 */
	public abstract void initView();

	public void replaceFragment(Fragment fragment) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
		ft.replace(android.R.id.tabcontent, fragment);
		ft.commit();
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		mFragmentManager = getSupportFragmentManager();
		findViewById();
		initView();
	}

	/**
	 * ���û������ݣ�key,value��
	 */
	public void setCacheStr(String key, String value) {
		if (!StringUtils.isEmpty(value)) {
			ACache.get(this).put(key, value);
		}
	}

	/**
	 * ��ʾ������
	 */
	protected void showProgressDialog() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (progress == null) {
					progress = new ProgressDialog(mContext);
					progress.setMessage("���ڼ���,���Ժ�...");
					progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progress.setCancelable(true);
				}
				progress.show();
			}
		});
	}

	/**
	 * �õ�һ��������
	 * 
	 * @param msg
	 * @return
	 */
	public ProgressDialog getProgressDialog(String msg) {
		progress = new ProgressDialog(this);
		// progressDialog.setIndeterminate(true);
		progress.setMessage(msg);
		progress.setCancelable(true);
		return progress;
	}

	/**
	 * ���ؽ�����
	 */
	protected void dismissProgressDialog() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (progress != null) {
					progress.dismiss();
				}
			}
		});
	}

	/**
	 * �������뷨
	 * 
	 * @param context
	 * @param achor
	 */
	public static void hideSoftInput(Context context, View achor) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(achor.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * ��ȡ�������ݸ���key
	 */
	public String getCacheStr(String key) {
		return ACache.get(this).getAsString(key);
	}

	/**
	 * ��ȡSerializable����
	 * 
	 * @param key
	 * @return
	 */
	public Object getCacheSerializable(String key) {
		return ACache.get(this).getAsObject(key);
	}

	/**
	 * ����Serializable����
	 * 
	 * @param key
	 * @param value
	 */
	public void setCacheSerializable(String key, Serializable value) {
		ACache.get(this).put(key, value);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
