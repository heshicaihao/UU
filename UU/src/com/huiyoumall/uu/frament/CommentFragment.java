package com.huiyoumall.uu.frament;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huiyoumall.uu.R;

/**
 * 有问题待完善。
 * 
 * @author ASUS
 * 
 */
public class CommentFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_order_unpaid, container,
				false);
		return view;
	}
}
