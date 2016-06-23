package com.jpw.agocal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jpw.agocal.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * text+text+go
 * Created by Administrator on 2016/6/23.
 */
public class ListItemTextView extends LinearLayout {

    @Bind(R.id.txt_tag_item)
    TextView txtTagItem;
    @Bind(R.id.txt_content_item)
    TextView txtContentItem;

    public ListItemTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public ListItemTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ListItemTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_textview_item, this);
        ButterKnife.bind(this ,this);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ListItemTextView, defStyleAttr, 0);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.ListItemTextView_tagTextSize:
                    txtTagItem.setTextSize(a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics())));
                    break;
                case R.styleable.ListItemTextView_tagText:
                    String mTitleText = a.getString(attr);
                    if(mTitleText != null){
                        txtTagItem.setText(mTitleText);
                    }
                    break;
                case R.styleable.ListItemTextView_tagTextColor:
                    txtTagItem.setTextColor(a.getColor(attr, Color.BLACK));
                    break;
                case R.styleable.ListItemTextView_textSize:
                    txtContentItem.setTextSize(a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics())));
                    break;
                case R.styleable.ListItemTextView_text:
                   String mTitleTextContent = a.getString(attr);
                    if(mTitleTextContent != null){
                        txtContentItem.setText(mTitleTextContent);
                    }
                    break;
                case R.styleable.ListItemTextView_textColor:
                    txtContentItem.setTextColor(a.getColor(attr, Color.BLACK));
                    break;
            }
        }
        a.recycle();
    }
}
