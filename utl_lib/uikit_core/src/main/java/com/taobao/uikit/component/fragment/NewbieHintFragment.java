package com.taobao.uikit.component.fragment;


import com.taobao.uikit.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * 
 * @author heihei 新手帮助基本fragment，show的时候传入一个需要展示的View和一个GuidFragmentDismissListener就可以
 *
 */
public class NewbieHintFragment extends Fragment implements OnClickListener {

	private ViewGroup mView;
	private GuidFragmentDismissListener mListener;

	public interface GuidFragmentDismissListener{
		void onDismiss();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return mView = (ViewGroup) inflater.inflate(R.layout.uik_newbie_hint_fragment,
				container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mView.setVisibility(View.INVISIBLE);
		super.onCreate(savedInstanceState);
	}

	public void show(View v,GuidFragmentDismissListener listener) {
		if (mView != null) {
			mView.setVisibility(View.VISIBLE);
			mView.setFocusable(true);
			mView.setClickable(true);
			mView.requestFocus();
			mView.setOnClickListener(this);
		}
		
		this.mListener = listener;

		if (v != null) {
			mView.removeAllViews();
			mView.addView(v);
		}
	}

	public boolean onPanelKeyDown() {
		if (mView != null ) {
			return dismisss();
		}
		return false;
	}

	private boolean dismisss() {
		if(mView != null && mView.getVisibility() == View.VISIBLE){
			mView.setVisibility(View.GONE);
			mView.setFocusable(false);
			if(mListener != null){
				mListener.onDismiss();
			}	
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (v == mView) {
			dismisss();
		}
	}
}
