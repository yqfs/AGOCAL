package com.frame.base.utl.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.frame.base.utl.application.BaseApplication;

/**
 * Created by Frank on 15/12/10.
 */
public class BrandListLayout extends RelativeLayout implements View.OnClickListener {


    public LinearLayout layout;

    public BrandListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        //线性布局Params
        LinearLayout.LayoutParams linearParams = lParams(-1, -1);

        //相对布局Params
        LayoutParams relativeParams = rParams(-1, -1);

        this.setLayoutParams(relativeParams);

        //通用配置
        float screenWidth = BaseApplication.info.getScreenWidth();

        //外层布局
        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        this.addView(layout, relativeParams);

    }

    public static LinearLayout.LayoutParams lParams(float wid, float hei) {
        return new LinearLayout.LayoutParams((int) wid, (int) hei);
    }

    public static LayoutParams rParams(float wid, float hei) {
        return new LayoutParams((int) wid, (int) hei);
    }

    private void temp() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 1:
                break;
        }
    }
}
