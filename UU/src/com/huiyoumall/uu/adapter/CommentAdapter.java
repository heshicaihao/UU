package com.huiyoumall.uu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyoumall.uu.R;
import com.huiyoumall.uu.entity.Comment;
import com.huiyoumall.uu.util.DateUtil;

/**
 * ∆¿¬€   ≈‰∆˜
 * 
 * @author ASUS
 * 
 */
public class CommentAdapter extends BaseAdapter {

	private List<Comment> mComments;
	private Context context;
	private LayoutInflater mInflater;

	public CommentAdapter(Context context, List<Comment> list_) {
		this.mComments = list_;
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	public void setData(List<Comment> mlist) {
		this.mComments = mlist;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mComments.size();
	}

	@Override
	public Object getItem(int position) {
		return mComments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			vh = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_merchant_evaluate,
					null);

			vh.nameText = (TextView) convertView
					.findViewById(R.id.evaluate_user_value_text);
			vh.dateText = (TextView) convertView
					.findViewById(R.id.evaluate_date_text);
			vh.contentText = (TextView) convertView
					.findViewById(R.id.evaluate_content);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();

		}

		Comment comm = (Comment) getItem(position);
		vh.nameText.setText(comm.getMember_name());
		vh.dateText.setText(DateUtil.getStringByFormat(comm.getSendtime()));
		vh.contentText.setText(comm.getContent());

		return convertView;
	}

	class ViewHolder {
		TextView nameText;
		TextView dateText;
		TextView contentText;
	}

}
