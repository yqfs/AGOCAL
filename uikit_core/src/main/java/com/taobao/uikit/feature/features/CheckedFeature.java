package com.taobao.uikit.feature.features;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.AdapterCallback;

/**
 * CheckedFeature show check box in the left of the item.
 * 
 * @author jiajing
 * 
 */
public class CheckedFeature extends AbsFeature<ListView> implements
		AdapterCallback {
	private boolean mVisible;
	private ListAdapterProxy mAdapterProxy;

	@Override
	public void constructor(Context context, AttributeSet attrs, int defStyle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHost(ListView host) {
		super.setHost(host);
		host.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

	/**
	 * set whether show the check
	 * 
	 * @param visible
	 */
	public void show(boolean visible) {
		if (mVisible != visible) {
			mVisible = visible;
			if (getHost().getAdapter() instanceof BaseAdapter) {
				((BaseAdapter) this.getHost().getAdapter())
						.notifyDataSetChanged();
			}

		}
	}

	/**
	 * checked all item
	 */
	public void checkedAll() {
		for(int i = 0; i < mAdapterProxy.getCount(); i ++){
			this.getHost().setItemChecked(i, true);
		}
	}

	/**
	 * unchecked all item
	 */
	public void uncheckedAll() {
		for(int i = 0; i < mAdapterProxy.getCount(); i ++){
			this.getHost().setItemChecked(i, false);
		}
	}

	private class CheckableLinearLayout extends LinearLayout implements
			Checkable {
		private CheckedTextView mChecked;

		public CheckableLinearLayout(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
			init();
		}

		public CheckableLinearLayout(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public CheckableLinearLayout(Context context) {
			super(context);
			init();
		}

		private void init() {
			LayoutInflater.from(getHost().getContext()).inflate(
					R.layout.uik_edit_multiple_choice, this, true);
			mChecked = (CheckedTextView) this.findViewById(R.id.checked);
		}

		@Override
		public void setChecked(boolean checked) {
			mChecked.setChecked(checked);
		}

		@Override
		public boolean isChecked() {

			return mChecked.isChecked();
		}

		@Override
		public void toggle() {
			mChecked.toggle();

		}

	}

	private class ListAdapterProxy extends BaseAdapter {
		private ListAdapter mAdapter;

		public ListAdapterProxy(ListAdapter adapter) {
			mAdapter = adapter;
		}

		@Override
		public int getCount() {
			return mAdapter.getCount();
		}

		@Override
		public Object getItem(int position) {
			return mAdapter.getItem(position);
		}

		@Override
		public long getItemId(int position) {
			return mAdapter.getItemId(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new CheckableLinearLayout(getHost().getContext());
			}
			ViewGroup viewGroup = (ViewGroup) convertView;
			if (viewGroup.getChildCount() >= 2) {
				View view = mAdapter.getView(position, viewGroup.getChildAt(1),
						parent);
				if (view != viewGroup.getChildAt(1)) {
					viewGroup.removeViewAt(1);
					viewGroup.addView(view);
				}
			} else {
				View view = mAdapter.getView(position, null, parent);
				viewGroup.addView(view);
			}
			if (viewGroup.getChildCount() > 1) {
				viewGroup.getChildAt(0).setVisibility(
						mVisible ? View.VISIBLE : View.GONE);
			}
			return convertView;
		}

	}

	@Override
	public void beforeSetAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterSetAdapter(ListAdapter adapter) {
		if (!(adapter instanceof ListAdapterProxy)) {
			mAdapterProxy = new ListAdapterProxy(adapter);
			this.getHost().setAdapter(mAdapterProxy);
		}
	}
}
