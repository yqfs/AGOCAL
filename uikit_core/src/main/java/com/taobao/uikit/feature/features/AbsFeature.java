package com.taobao.uikit.feature.features;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * 
 * feature base class
 * 
 * @author jiajing
 * @param <T>
 */
public abstract class AbsFeature<T extends View> {
	protected T mHost;

	public AbsFeature() {
		constructor(null, null, 0);
	}

	public void setHost(T host) {
		this.mHost = host;
	}

	public T getHost() {
		return mHost;
	}

	public abstract void constructor(Context context, AttributeSet attrs,
			int defStyle);

}
