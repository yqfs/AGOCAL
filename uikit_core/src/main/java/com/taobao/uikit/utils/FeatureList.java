package com.taobao.uikit.utils;

import com.taobao.uikit.R;
import com.taobao.uikit.feature.features.AbsFeature;
import com.taobao.uikit.feature.features.FeatureFactory;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FeatureList<T extends View> extends
		ArrayList<AbsFeature<? super T>> implements
		Comparator<AbsFeature<? super T>>, IFeatureList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5539018560951385305L;

	private T mHost;

	public FeatureList(T host) {
		mHost = host;
	}

	@Override
	public boolean add(AbsFeature<? super T> object) {
		for (AbsFeature<? super T> feature : this) {
			if (TextUtils.equals(feature.getClass().getName(), object
					.getClass().getName())) {
				throw new RuntimeException(feature.getClass().getName()
						+ " already add to this view");
			}
		}

		boolean ret = super.add(object);

		Collections.sort(this, this);
		return ret;
	}

	@Override
	public int compare(AbsFeature<? super T> lhs, AbsFeature<? super T> rhs) {
		return FeatureFactory
				.getFeaturePriority(lhs.getClass().getSimpleName())
				- FeatureFactory.getFeaturePriority(rhs.getClass()
						.getSimpleName());
	}

	public void init(Context context, AttributeSet attrs, int defStyle) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.FeatureNameSpace);
		if (typedArray != null) {
            ArrayList<AbsFeature<? super T>> features = FeatureFactory.creator(mHost.getContext(), typedArray);

			for (AbsFeature<? super T> feature : features) {
				addFeature(feature);
				feature.constructor(context, attrs, defStyle);
			}

			typedArray.recycle();
		}
	}

	@Override
	public boolean addFeature(AbsFeature<? super T> feature) {
		if (feature != null) {
			feature.setHost(mHost);
			return this.add(feature);
		}
		return false;
	}

	@Override
	public AbsFeature<? super T> findFeature(Class<? extends AbsFeature<? super T>> clazz) {
		for (AbsFeature<? super T> feature : this) {
			if (feature.getClass() == clazz) {
				return feature;
			}
		}
		return null;
	}

	@Override
	public boolean removeFeature(Class<? extends AbsFeature<? super T>> clazz) {
		for (AbsFeature<? super T> feature : this) {
			if (feature.getClass() == clazz) {
				return this.remove(feature);
			}
		}
		return false;
	}

	@Override
	public void clearFeatures() {
		this.clear();
		mHost.requestLayout();
	}
}
