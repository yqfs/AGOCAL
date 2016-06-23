package com.taobao.uikit.feature.features.internal.pullrefresh;

import android.view.View;

public interface IViewEdgeJudge {

	boolean hasArrivedTopEdge();
	
	boolean hasArrivedBottomEdge();
	
	void setHeadView(View View);
	
	void setFooterView(View view);
	
	void keepTop();
	
	void keepBottom();

}
