package com.huiyoumall.uu.frament;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.huiyoumall.uu.GlobalParams;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.activity.JiaolianDetailsActivity;

/**
 * 找教练Fragment
 */
public class FindCoachActiviy extends Activity {

	private LinearLayout vSelect_item;
	private LinearLayout vJiaolian_search;
	private PopupWindow mWindow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_findjl);
		initView();
	}

	private void initView() {
		vSelect_item = (LinearLayout) findViewById(R.id.select_item);
		vJiaolian_search = (LinearLayout) findViewById(R.id.jiaolian_search);
		MyOnclickListener onclickListener = new MyOnclickListener();
		vSelect_item.setOnClickListener(onclickListener);
		vJiaolian_search.setOnClickListener(onclickListener);
		createPopuWindow();
	}

	private class MyOnclickListener implements View.OnClickListener {
		public void onClick(View v) {
			int mID = v.getId();
			if (mID == R.id.select_item) {
				if (mWindow != null) {
					if (mWindow.isShowing()) {
						closePopWindow();
					} else {
						showPopWindow();
					}
				}
			}
			if (mID == R.id.jiaolian_search) {
				HelperUi.startActivity(FindCoachActiviy.this,
						JiaolianDetailsActivity.class);
			}
		}
	};

	private void createPopuWindow() {
		/***************************************************/
		mWindow = new PopupWindow(this);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mView = inflater.inflate(R.layout.view_choose_jiaolian, null);

		LinearLayout ok = (LinearLayout) mView.findViewById(R.id.ok);
		LinearLayout cancel = (LinearLayout) mView.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePopWindow();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePopWindow();
			}
		});
		mWindow.setContentView(mView);
		mWindow.setOutsideTouchable(true);
		mWindow.setFocusable(true);
		mWindow.setWidth(GlobalParams.WIN_WIDTH);
		mWindow.setHeight(GlobalParams.WIN_HEIGHT / 5 * 2);
		mWindow.update();
		// mWindow.setWindowLayoutMode(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT);
		ColorDrawable dw = new ColorDrawable(R.color.white);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		mWindow.setBackgroundDrawable(dw);
		mWindow.setAnimationStyle(R.style.PopupWindowAnimation);
		mWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = FindCoachActiviy.this
						.getWindow().getAttributes();
				params.alpha = 1f;
				FindCoachActiviy.this.getWindow().setAttributes(params);
			}
		});
	}

	/**
	 * 弹出窗口设置背景activity半透明
	 */
	private void showPopWindow() {
		mWindow.showAsDropDown(vSelect_item);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.alpha = 0.7f;
		getWindow().setAttributes(params);
	}

	private void closePopWindow() {
		mWindow.dismiss();
	}
}
