package com.taobao.uikit.feature.callback;

import android.graphics.Rect;

public interface FocusCallback {
	void beforeOnFocusChanged(boolean gainFocus, int direction,
							  Rect previouslyFocusedRect);

	void afterOnFocusChanged(boolean gainFocus, int direction,
							 Rect previouslyFocusedRect);

	void beforeOnWindowFocusChanged(boolean hasWindowFocus);

	void afterOnWindowFocusChanged(boolean hasWindowFocus);
}
