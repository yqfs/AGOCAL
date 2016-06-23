package com.taobao.uikit.feature.view;

import android.graphics.Canvas;
import android.view.View;

public interface ViewGroupHelper {
	void measureChild(View child, int parentWidthMeasureSpec,
					  int parentHeightMeasureSpec, int reserve);

	boolean drawChild(Canvas canvas, View child, long drawingTime,
					  int reserve);
}
