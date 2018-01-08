package com.huiyoumall.uu.widget;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huiyoumall.uu.AppContext;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.adapter.SelectAreaAdapter;
import com.huiyoumall.uu.db.DataHelper;
import com.huiyoumall.uu.entity.CityItem;
import com.huiyoumall.uu.util.StringUtils;

public class SelectCityPopWindlow extends PopupWindow {
	private View conentView;
	private ProgressBar area_pross;
	private GridViewForScollView areaView;
	private TextView ctView;
	private SelectAreaAdapter areaAdapter;
	private Activity context;
	private final int AREA_TYPE = 2;
	private String city = "";
	private AppContext app;
	private DataHelper dHelper;
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			if (msg.what == AREA_TYPE) {
				area_pross.setVisibility(View.GONE);
				areaView.setVisibility(View.VISIBLE);
				final List<CityItem> areas = (List<CityItem>) msg.obj;
				if (areas != null && areas.size() > 0) {
					areaAdapter.setDatas(areas);
					areaView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							CityItem area = areas.get(position);
							app.saveSelectArea(area.name);
							SelectCityPopWindlow.this.dismiss();
						}
					});
				}
			}
		};
	};

	public SelectCityPopWindlow(final Activity context, final ImageView img,
			final TextView city_text, OnClickListener onClickListener) {
		// ��ʼ������
		dHelper = new DataHelper(context);
		this.app = (AppContext) context.getApplicationContext();
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (conentView == null) {
			conentView = inflater.inflate(R.layout.view_select_city, null);
		}

		LinearLayout change_city = (LinearLayout) conentView
				.findViewById(R.id.change_city);

		areaView = (GridViewForScollView) conentView
				.findViewById(R.id.area_gradview);
		ctView = (TextView) conentView.findViewById(R.id.location_city);

		area_pross = (ProgressBar) conentView.findViewById(R.id.area_pross);
		area_pross.setVisibility(View.VISIBLE);

		change_city.setOnClickListener(onClickListener);

		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		// ����SelectPicPopupWindow��View
		this.setContentView(conentView);
		// ����SelectPicPopupWindow��������Ŀ�
		this.setWidth(w);
		// ����SelectPicPopupWindow��������ĸ�
		this.setHeight(h * 9 / 10);
		// ����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		// this.setOutsideTouchable(true);
		// ˢ��״̬
		this.update();
		this.setWindowLayoutMode(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		// ʵ����һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(R.color.white);
		// ��back���������ط�ʹ����ʧ,������������ܴ���OnDismisslistener �����������ؼ��仯�Ȳ���
		this.setBackgroundDrawable(dw);
		// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		// ����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.PopupWindowAnimation);

		/**
		 * ����dissesʱ���¼�
		 */
		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// if (app.getSelectCity() != null && app.getSelectArea() ==
				// null) {
				// city_text.setText(app.getSelectCity());
				// } else if (app.getSelectCity() != null
				// && app.getSelectArea() != null) {
				// city_text.setText(app.getSelectCity() + "-"
				// + app.getSelectArea());
				// } else {
				// city_text.setText(app.getLocationCity());
				// }

				if (app.getSelectCity() != null && app.getSelectArea() == null) {
					city_text.setText(app.getSelectCity());
				} else if (app.getSelectCity() != null
						&& app.getSelectArea() != null) {
					city_text.setText(app.getSelectCity() + "-"
							+ app.getSelectArea());
				} else {
					if (app.getLocationCity() == null) {
						city_text.setText("����");
						app.saveLocationCity("����");
						app.saveSelectCity("����");
					} else if (app.getLocationCity() != null
							&& app.getLocationArea() != null) {
						city_text.setText(app.getLocationCity() + "-"
								+ app.getLocationArea());

						app.saveSelectCity(app.getLocationCity());
						app.saveSelectArea(app.getLocationArea());
					}
				}

				img.setImageResource(R.drawable.search_city_change);
				WindowManager.LayoutParams params = context.getWindow()
						.getAttributes();
				params.alpha = 1f;
				context.getWindow().setAttributes(params);
				dHelper.Close();
			}
		});
	}

	public void showPopupWindow(View parent) {
		if (app.getSelectCity() != null) {
			ctView.setText(app.getSelectCity());
			city = app.getSelectCity();
		} else {
			ctView.setText(app.getLocationCity());
			city = app.getLocationCity();
		}
		new Thread() {
			@Override
			public void run() {
				CityItem item = null;
				if (StringUtils.isEmpty(city)) {
					item = dHelper.queryEntity("name='����'");
				} else {
					item = dHelper.queryEntity("name='" + city + "'");
				}
				Message msg = Message.obtain();
				msg.what = AREA_TYPE;
				msg.obj = dHelper.query("parent=" + item.id);
				mHandler.sendMessage(msg);
			}
		}.start();
		areaAdapter = new SelectAreaAdapter(context);
		areaView.setAdapter(areaAdapter);
		WindowManager.LayoutParams params = context.getWindow().getAttributes();
		params.alpha = 0.7f;
		context.getWindow().setAttributes(params);
		this.showAsDropDown(parent, parent.getLayoutParams().width, 0);
	}
}
