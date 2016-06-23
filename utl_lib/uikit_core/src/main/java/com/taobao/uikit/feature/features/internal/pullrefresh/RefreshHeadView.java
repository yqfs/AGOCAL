package com.taobao.uikit.feature.features.internal.pullrefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 
 * @author yujf
 * 默认的下拉刷新listview的headView
 */
@SuppressLint("ViewConstructor")
public class RefreshHeadView extends LinearLayout{

	public static final int TYPE_HEADER = 1;
	public static final int TYPE_FOOTER = 2;
	
	public static int STATE_ICON_LEFT_PADDING = 40;
	//public int TIMECOLOR = 0xb89766;
	
	private ImageView mArrow;
	private CustomProgressBar mProgressbar;
	private TextView  mrefreshState;
	private TextView  mLastRefresh;
		
	public RefreshHeadView(Context context, Drawable drawable, View progressBar, View custom)
	{
		super(context);
		init(context,drawable,custom);
	}
	
	/*public void setType(int type)
	{
		mType = type;
	}*/

	private void init(Context context,Drawable drawable,View custom)
	{
		DisplayMetrics dm = new DisplayMetrics();  
		dm = getResources().getDisplayMetrics();
		float screenDensity = dm.density;
        this.setOrientation(LinearLayout.VERTICAL);
		
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        //this.addView(view,params);
        if(custom != null){
        	this.addView(custom,params);
        }
        //RefreshHeadView addView(FrameLayout)
		FrameLayout layout = new FrameLayout(context);
		ViewGroup.LayoutParams logoParams = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		this.addView(layout, logoParams);
		//FrameLayout addView(centerLayout)
        RelativeLayout centerLayout =new RelativeLayout(context);
        centerLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        FrameLayout.LayoutParams centerLayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        centerLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
//        centerLayoutParams.bottomMargin = (int) (30*screenDensity);
//        centerLayoutParams.leftMargin = (int) (100*screenDensity);
        

        layout.addView(centerLayout, centerLayoutParams);
        //centerLayout.setBackgroundColor(Color.GRAY);

        FrameLayout stateIconLayout = new FrameLayout(context);
        RelativeLayout.LayoutParams stateIconParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        stateIconParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        stateIconParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        stateIconParams.topMargin = (int)(32 * screenDensity);
        stateIconParams.bottomMargin = (int)(24 * screenDensity);

        centerLayout.addView(stateIconLayout, stateIconParams);

        //箭头
        ImageView arrow = new ImageView(context);
        FrameLayout.LayoutParams arrow_Params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        arrow_Params.gravity = Gravity.CENTER;
        stateIconLayout.addView(arrow, arrow_Params);
        mArrow = arrow;
        mArrow.setImageDrawable(drawable);

        //菊花
//        View progressBar = progress;
//        if(progressBar==null)
//            progressBar = new ProgressBar(context,null,android.R.attr.progressBarStyleSmall);
        CustomProgressBar progressBar = new CustomProgressBar(context);
        
        FrameLayout.LayoutParams progress_Params = new FrameLayout.LayoutParams((int)(28*screenDensity),(int)(28*screenDensity));
        progress_Params.gravity = Gravity.CENTER_VERTICAL;
        stateIconLayout.addView(progressBar, progress_Params);
//        progressBar.setVisibility(View.GONE);
        mProgressbar = progressBar;
//        mProgressbar.setBackgroundColor(Color.BLACK);

        //stateIconLayout.setBackgroundColor(Color.BLUE);


        RelativeLayout tipLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams tipLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        tipLayoutParams.addRule(RelativeLayout.RIGHT_OF,stateIconLayout.getId());
        tipLayoutParams.leftMargin = (int)(STATE_ICON_LEFT_PADDING*screenDensity);

        tipLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        //tipLayout.setBackgroundColor(Color.YELLOW);



        centerLayout.addView(tipLayout, tipLayoutParams);

        TextView refreshStateText = new TextView(context);
        refreshStateText.setText("下拉刷新");
        refreshStateText.setTextSize(12);
        refreshStateText.setTextColor(0xFF999999);
        refreshStateText.setMaxLines(3);
        FrameLayout.LayoutParams refreshStateTextParams = new FrameLayout.LayoutParams((int)(100*screenDensity),LayoutParams.WRAP_CONTENT);
//        refreshStateTextParams.gravity = Gravity.CENTER_VERTICAL;
        refreshStateTextParams.gravity = Gravity.CENTER;
        tipLayout.addView(refreshStateText, refreshStateTextParams);
        mrefreshState = refreshStateText;
		
	}
	
	public void setProgressBarColor(int color) {
		if(mProgressbar != null)
			mProgressbar.setPaintColor(color);
	}
	
	public void setProgressBarInitState(boolean isShow) {
		mProgressbar.isInitShow(isShow);
	}
	
	public ImageView getArrow() {
		return mArrow;
	}

	public CustomProgressBar getProgressbar() {
		return mProgressbar;
	}

	public TextView getrefreshStateText() {
		return mrefreshState;
	}

	public TextView getLastRefreshText() {
		return mLastRefresh;
	}
	
	public void setPullDownDistance(int distance) {
		mProgressbar.setPullDownDistance(distance);
	}

}
