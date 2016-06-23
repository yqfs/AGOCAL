package com.frame.base.utl.jump;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 页面跳转链对象，用于记录当前页的上一个页面的相关信息
 *
 * @author WilliamChik on 15/7/18.
 */
public class JumpRefer implements Parcelable {

  // 页面名称
  public String page;
  // 控件名称
  public String ctrlName;
  // 事件参数
  public String args;
  // 附加信息, key:value
  public String attachment;

  public static final Creator<JumpRefer> CREATOR = new Creator<JumpRefer>() {

    @Override
    public JumpRefer createFromParcel(Parcel source) {
      JumpRefer jumpRefer = new JumpRefer();
      jumpRefer.page = source.readString();
      jumpRefer.ctrlName = source.readString();
      jumpRefer.args = source.readString();
      jumpRefer.attachment = source.readString();
      return jumpRefer;
    }

    @Override
    public JumpRefer[] newArray(int size) {
      return new JumpRefer[0];
    }
  };

  public JumpRefer() {
    page = PageContext.PAGE;
  }

  /**
   * 构造函数
   */
  public JumpRefer(String ctrlName, String args, String attachment) {
    this.page = PageContext.PAGE;
    this.ctrlName = ctrlName;
    this.args = args;
    this.attachment = attachment;
  }

  /**
   * 构造函数
   */
  public JumpRefer(String ctrlName) {
    this.page = PageContext.PAGE;
    this.ctrlName = ctrlName;
  }

  /**
   * 构造函数
   */
  public JumpRefer(String pageName, String ctrlName, String args, String attachment) {
    this.page = pageName;
    this.ctrlName = ctrlName;
    this.args = args;
    this.attachment = attachment;
  }

  /**
   * 获取自己的新的jumpRefer
   *
   * @param bundle 启动新的activity带入的bundle参数
   */
  public static void setMyJumpRefer(String ctrlName, String args, String attachment, Bundle bundle) {
    JumpRefer jumpRefer = new JumpRefer();
    jumpRefer.ctrlName = ctrlName;
    jumpRefer.args = args;
    jumpRefer.attachment = attachment;

    bundle.putParcelable("jumpRefer", jumpRefer);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(page);
    dest.writeString(ctrlName);
    dest.writeString(args);
    dest.writeString(attachment);
  }
}
