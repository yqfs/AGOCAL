package com.frame.base.utl.mvp.presenter;

/**
 * Created by Administrator on 2016/6/20.
 */
public abstract class BasePresenter<T> {

    public T mView;

    public void attach(T mView){
        this.mView = mView;
    }
    public void dettach(){
        mView = null;
    }
}
