package com.taobao.uikit.remote;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.Surface.OutOfResourcesException;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;

public abstract class RemoteViewService extends Service
{

    private static final String TAG = "RemoteViewService";

    private Bundle mSavedInstanceState;

    private ResultReceiver mResultReceiver;

    /**
     * create view for the service
     */
    protected abstract View onCreateView(LayoutInflater inflater, ViewGroup container, IBinder appWindow, Bundle savedInstanceState);

    protected Bundle invoke(int resultCode, Bundle param)
    {
        return null;
    }

    protected void sendResult(int resultCode, Bundle resultData)
    {
        mResultReceiver.send(resultCode, resultData);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.d(TAG, "onBind" + intent.toString());
        mSavedInstanceState = intent.getExtras();
        mResultReceiver = intent.getParcelableExtra(RemoteView.KEY_RESULT_LISTENER);
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        mSavedInstanceState = null;
        mResultReceiver = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    private final IRemoteViewService.Stub mBinder = new IRemoteViewService.Stub()
    {
        private Container mView = null;

        private Surface mSurface;

        private int mX;

        private int mY;

        private int mWidth;

        private int mHeight;

        private WindowManager mWm;

        private WindowManager.LayoutParams mParams;

        private IBinder mWindowToken;

        private IBinder mAppWindowToken;

        private static final int MSG_VIEW_ADD = 100;

        private static final int MSG_VIEW_REMOVE = 200;

        private static final int MSG_VIEW_UPDATE = 300;

        private static final int MSG_VIEW_TOUCH_EVENT = 400;

        private Handler mHandler;

        @Override
        public void setBounds(int x, int y, int width, int height) throws RemoteException
        {
            mX = x;
            mY = y;

            if (mParams != null && (width != mWidth || height != mHeight))
            {
                mParams.width = width;
                mParams.height = height;
                if (isMainThread())
                {
                    updateViewLayout();
                }
                else
                {
                    initHandler();
                    mHandler.sendEmptyMessage(MSG_VIEW_UPDATE);
                }
            }
            mWidth = width;
            mHeight = height;
        }

        @Override
        public void setSurface(Surface surface) throws RemoteException
        {
            mSurface = surface;
            initViewWhenReady();
            if (surface == null)
            {
                release();
            }
        }

        @Override
        public boolean injectEvent(MotionEvent event) throws RemoteException
        {
            if (isMainThread())
            {
                return dispatchTouchEvent(event);
            }
            else
            {
                Message msg = Message.obtain();
                msg.what = MSG_VIEW_TOUCH_EVENT;
                msg.obj = event;
                initHandler();
                return mHandler.sendMessage(msg);
            }
        }

        private void initViewWhenReady()
        {
            if (mSurface == null)
            {
                return;
            }

            if (mWindowToken == null)
            {
                return;
            }
            if (isMainThread())
            {
                addView();
            }
            else
            {
                initHandler();
                mHandler.sendEmptyMessage(MSG_VIEW_ADD);
            }
        }

        private void initHandler()
        {
            if (mHandler == null)
            {
                mHandler = new Handler(RemoteViewService.this.getMainLooper())
                {
                    public void handleMessage(Message msg)
                    {
                        if (msg.what == MSG_VIEW_ADD)
                        {
                            addView();
                        }
                        else if (msg.what == MSG_VIEW_TOUCH_EVENT)
                        {
                            dispatchTouchEvent((MotionEvent) msg.obj);
                        }
                        else if (msg.what == MSG_VIEW_REMOVE)
                        {
                            removeView();
                        }
                        else if (msg.what == MSG_VIEW_UPDATE)
                        {
                            updateViewLayout();
                        }
                    }
                };
            }
        }

        private void updateViewLayout()
        {
            if (null != mWm && null != mView)
            {
                mWm.updateViewLayout(mView, mParams);
            }
        }

        private boolean dispatchTouchEvent(MotionEvent event)
        {
            return null != mView && mView.dispatchTouchEvent(event);
        }

        private void removeView()
        {
            if (null == mWm)
            {
                mWm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            }

            if (null != mView)
            {
                mWm.removeView(mView);
                mView = null;
            }
        }

        private void addView()
        {
            removeView();

            if (null == mWm)
            {
                mWm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            }

            Container container = new Container(RemoteViewService.this);

            View view = onCreateView(LayoutInflater.from(RemoteViewService.this), container, mAppWindowToken, mSavedInstanceState);

            container.addView(view);

            mParams = new WindowManager.LayoutParams();

            mParams.type = LayoutParams.TYPE_APPLICATION_PANEL;
            mParams.format = PixelFormat.RGBA_8888;

            mParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
            //| LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

            mParams.gravity = Gravity.LEFT | Gravity.TOP;
            mParams.x = mX;
            mParams.y = mY;
            mParams.width = mWidth;
            mParams.height = mHeight;
            mParams.token = mWindowToken;

            mView = container;
            mWm.addView(container, mParams);
        }

        class Container extends FrameLayout
        {

            public Container(Context context)
            {
                super(context);
            }

            @Override
            public void draw(Canvas canvas)
            {
                if (null == mSurface)
                {
                    return;
                }

                Canvas canvasS = null;
                try
                {
                    canvasS = mSurface.lockCanvas(new Rect(0, 0, this.getWidth(), this.getHeight()));
                }
                catch (OutOfResourcesException e)
                {
                    canvasS = null;
                }
                catch (IllegalArgumentException e1)
                {
                    canvasS = null;
                }
                catch (IllegalStateException e2)
                {
                    canvasS = null;
                }
                if (canvasS == null)
                {
                    return;
                }

                super.draw(canvasS);
                mSurface.unlockCanvasAndPost(canvasS);
            }
        }

        @Override
        public void release()
        {
            mSurface = null;

            if (mWm == null)
            {
                return;
            }

            if (isMainThread())
            {
                removeView();
            }
            else
            {
                initHandler();
                mHandler.sendEmptyMessage(MSG_VIEW_REMOVE);
            }
        }

        @Override
        public void setToken(IBinder window, IBinder appWindow) throws RemoteException
        {
            mWindowToken = window;
            mAppWindowToken = appWindow;
            initViewWhenReady();
        }


        @Override
        public Bundle invoke(int resultCode, Bundle param) throws RemoteException
        {
            return invoke(resultCode, param);
        }

    };

    private boolean isMainThread()
    {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }
}
