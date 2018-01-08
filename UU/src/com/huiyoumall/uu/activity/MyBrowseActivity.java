package com.huiyoumall.uu.activity;

import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.adapter.SearchListAdapter;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.db.DataHelper;
import com.huiyoumall.uu.entity.Merchant;

/**
 * 我的浏览 界面
 * 
 * @author ASUS
 * 
 */
public class MyBrowseActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_browse);
	}

	private TextView vTitle;
	private TextView right_btn;
	private ListView browse_listview;
	private RelativeLayout browse_empty;
	private SearchListAdapter browseAdapter;
	private DataHelper dh;
	private List<Merchant> mlist;

	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		right_btn = (TextView) findViewById(R.id.right_btn);
		browse_listview = (ListView) findViewById(R.id.browse_listview);
		browse_empty = (RelativeLayout) findViewById(R.id.browse_none);

	}

	public void initView() {
		vTitle.setText("我的浏览");
		right_btn.setText("清除");
		dh = new DataHelper(this);
		mlist = dh.querybrowse();
		if (mlist != null && mlist.size() > 0) {
			browse_empty.setVisibility(View.GONE);
			browseAdapter = new SearchListAdapter(this, mlist);
			browse_listview.setAdapter(browseAdapter);
		}
		browse_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Merchant merchant = mlist.get(position);
				Bundle bundle = new Bundle();
				bundle.putString(MerchantDetailsActivity.M_ID,
						merchant.getMid());
				HelperUi.startActivity(MyBrowseActivity.this,
						MerchantDetailsActivity.class, bundle);
			}
		});

		right_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mlist != null && mlist.size() > 0) {
					if (dh.deleteBrowse() > 0) {
						mlist.clear();
						browseAdapter.setData(mlist);
					}
				}
			}
		});
	}

	@Override
	public void activitybackview(View view) {
		super.activitybackview(view);
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
