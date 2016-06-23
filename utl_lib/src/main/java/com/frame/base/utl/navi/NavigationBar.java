package com.frame.base.utl.navi;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.base.utl.R;
import com.frame.base.utl.application.BaseApplication;
import com.frame.base.utl.jump.PanelForm;
import com.frame.base.utl.jump.PanelManager;

/**
 * 导航条布局，动态加入到导航栏的 Activity 之中
 *
 * @author YANGQIYUN on 2015/11/18
 */
public class NavigationBar extends LinearLayout implements OnClickListener {

  public static NavigationBarItem[] navigationBarItems = null;

  private static int currentPosition = -1;

  private InnerAdapter adapter;

  public NavigationBar(Context context) {
    super(context);
    init(context);
  }

  public NavigationBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.navigation_bar, this);
    adapter = new InnerAdapter();
  }

  /**
   * 更新导航栏信息
   *
   * @param dstid 目标导航页的页面id
   */
  public void onResume(int dstid) {
    currentPosition = findPanelPosition(dstid);
    setAdapter(adapter);
  }

  /**
   * 设置 adapter，更新导航栏信息
   */
  private void setAdapter(Adapter<ViewHolder> adapter) {
    if (adapter == null || adapter.getItemCount() == 0) {
      return;
    }

    removeAllViews();
    for (int i = 0; i < adapter.getItemCount(); i++) {
      ViewHolder vh = adapter.onCreateViewHolder();
      adapter.onBindViewHolder(vh, i);
      LayoutParams lp =
//          new LayoutParams(BaseApplication.info.getNaviItemWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
              new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1);
      addView(vh.itemView, lp);
    }
  }

  @Override
  public void onClick(View v) {
    int p = (Integer) v.getTag();
    // 如果在当前页面点击当前页面，不做任何操作
    if (p == currentPosition) {
      return;
    }
    PanelManager.getInstance().switchPanel(navigationBarItems[p].panelId, null, null);
  }

  /**
   * 根据index获取导航栏项
   */
  public NavigationBarItem getItemByIndex(int index) {
    if (index < 0 || index > navigationBarItems.length - 1) {
      return null;
    } else {
      return navigationBarItems[index];
    }
  }

  /**
   * 根据PanelId获取导航栏项
   */
  public NavigationBarItem getItemByPanelId(int index) {
    for (NavigationBarItem item : navigationBarItems) {
      if (item.panelId == index) {
        return item;
      }
    }

    return null;
  }

  /**
   * @param dstid panel id
   * @return 如果该页面不是导航页，返回 -1；否则就是该导航页的索引，索引大于 0
   */
  public static int findPanelPosition(int dstid) {
    for (int i = 0; i < navigationBarItems.length; i++) {
      NavigationBarItem item = navigationBarItems[i];
      if (item.locked && dstid == item.panelId) {
        return i;
      }
    }

    return -1;
  }

  private class InnerViewHolder extends ViewHolder {

    public ImageView naviImageView;
    public TextView naviTextView;
    public ImageView pointImgView;
    public TextView infoPointNum;
    public ImageView infoImage;

    public InnerViewHolder(View itemView) {
      super(itemView);
      naviImageView = (ImageView) itemView.findViewById(R.id.tv_navi_image);
      naviTextView = (TextView) itemView.findViewById(R.id.tv_navi_text);
      pointImgView = (ImageView) itemView.findViewById(R.id.info_point);
      infoPointNum = (TextView) itemView.findViewById(R.id.info_num);
      infoImage = (ImageView) itemView.findViewById(R.id.info_image);
    }
  }

  private class InnerAdapter extends Adapter {

    @Override
    public ViewHolder onCreateViewHolder() {
      View convertView = LayoutInflater.from(BaseApplication.getContext()).inflate(R.layout.navigation_bar_item, null);
      if (convertView == null) {
        return null;
      }
      return new InnerViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      if (!(holder instanceof InnerViewHolder) || navigationBarItems[position] == null) {
        return;
      }

      NavigationBarItem naviItem = navigationBarItems[position];
      InnerViewHolder newHolder = (InnerViewHolder) holder;

      newHolder.itemView.setTag(position);
      newHolder.itemView.setOnClickListener(NavigationBar.this);

      newHolder.naviTextView.setText(naviItem.title);

      if (currentPosition == position) {
        newHolder.naviImageView.setBackgroundResource(naviItem.pressIconImgId);
        newHolder.naviTextView.setTextColor(naviItem.selectedColor);
      } else {
        newHolder.naviImageView.setBackgroundResource(naviItem.norIconImgId);
        newHolder.naviTextView.setTextColor(naviItem.unSelectedColor);
      }

      int infoNum = naviItem.infoNum;
      int defaulImgId = naviItem.specialImgId;

      if (defaulImgId == -1) {
        // 显示小红点
        newHolder.pointImgView.setVisibility(View.VISIBLE);
        newHolder.infoPointNum.setVisibility(View.GONE);
      } else if (defaulImgId > 0) {
        // 显示特定图片
        newHolder.pointImgView.setVisibility(View.GONE);
        newHolder.infoPointNum.setVisibility(View.GONE);
        newHolder.infoImage.setVisibility(View.VISIBLE);
        newHolder.infoImage.setImageResource(defaulImgId);
      } else {
        // 显示数字
        newHolder.infoImage.setVisibility(View.GONE);

        // 消息数量分情况处理
        if (infoNum <= 0) {
          newHolder.pointImgView.setVisibility(View.GONE);
          newHolder.infoPointNum.setVisibility(View.GONE);
        } else if (infoNum < 100) {
          newHolder.pointImgView.setVisibility(View.GONE);
          newHolder.infoPointNum.setVisibility(View.VISIBLE);
          newHolder.infoPointNum.setText(String.valueOf(infoNum));
        } else {
          // 超过100只显示99
          newHolder.pointImgView.setVisibility(View.GONE);
          newHolder.infoPointNum.setVisibility(View.VISIBLE);
          newHolder.infoPointNum.setText(String.valueOf(99));
        }
      }
    }

    @Override
    public int getItemCount() {
      return navigationBarItems.length;
    }
  }

  /**
   * 参照 RecyclerView 的 Adapter 写的 Adapter，用于动态加入布局
   */
  private static abstract class Adapter<VH extends ViewHolder> {

    public abstract VH onCreateViewHolder();

    public abstract void onBindViewHolder(VH holder, int position);

    public abstract int getItemCount();

  }

  /**
   * 参照 RecyclerView 的 ViewHolder 写的 ViewHolder
   */
  private static abstract class ViewHolder {

    public final View itemView;

    public ViewHolder(View itemView) {
      if (itemView == null) {
        throw new IllegalArgumentException("itemView may not be null");
      }
      this.itemView = itemView;
    }
  }
}
