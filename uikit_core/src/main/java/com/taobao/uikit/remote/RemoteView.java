package com.taobao.uikit.remote;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.TypedArray;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.ViewGroup;

import com.taobao.uikit.R;

/**
 * Remote View support showing view from remote process or other bundles,
 * 
 * @author jiajing
 * 
 */
public class RemoteView extends ViewGroup {
	static final String TAG = "RemoteView";
	static final String KEYEVENT_KEY = "keyevent";
	static final int KEYEVENT_CODE = 100;
	static final String KEY_RESULT_LISTENER = "com.taobao.uikit.remote.KEY_RESULT_LISTENER";
	
	private static final boolean DEBUG = true;

	private TextureView mTextureView;
	private Activity mActivity;
	private int mWidth;
	private int mHeight;
	private Surface mSurface;
	private int mLastVisibility;
	private String mAction;
	private Intent mIntent;
	private IRemoteViewService mRemoteViewService = sEmptyRemoteViewService;

	private static IRemoteViewService sEmptyRemoteViewService = new IRemoteViewService.Stub() {

		@Override
		public boolean injectEvent(MotionEvent event) throws RemoteException {
			return false;
		}

		@Override
		public void setSurface(Surface surface) throws RemoteException {
			// do nothing
		}

		@Override
		public void setBounds(int x, int y, int width, int height)
				throws RemoteException {
			// do nothing
		}

		@Override
		public void release() throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setToken(IBinder window, IBinder appWindow)
				throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public Bundle invoke(int resultCode, Bundle resultData)
				throws RemoteException {
			// TODO Auto-generated method stub
			return null;
		}

	};

	public RemoteView(Context context) {
		this(context, null);
	}

	public RemoteView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RemoteView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		while (context instanceof ContextWrapper) {
			if (context instanceof Activity) {
				mActivity = (Activity) context;
				break;
			}
			context = ((ContextWrapper) context).getBaseContext();
		}
		if (mActivity == null) {
			throw new IllegalStateException(
					"The RemoteView's Context is not an Activity.");
		}

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.RemoteView, defStyle, 0);
		mAction = a.getString(R.styleable.RemoteView_action);

		if (!TextUtils.isEmpty(mAction)) {
			setIntent(new Intent(mAction));
		}

		initView();
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		// TODO Auto-generated method stub
		super.onWindowVisibilityChanged(visibility);

	}

	private void initView() {
		mTextureView = new TextureView(this.getContext());
		mTextureView
				.setSurfaceTextureListener(new ActivityViewSurfaceTextureListener());
		addView(mTextureView);
		mLastVisibility = getVisibility();
	}

	private void attachToService() {

		if (mRemoteViewService != sEmptyRemoteViewService) {
			return;
		}
		
		if(mMyResultReceiver == null){
			mMyResultReceiver = new MyResultReceiver(this,new Handler());
		}
		
		mIntent.putExtra(KEY_RESULT_LISTENER, mMyResultReceiver);
		mActivity.bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);
	}

	/**
	 * set the intent for attach to remote view
	 * 
	 * @param intent
	 */
	public void setIntent(Intent intent) {
		mIntent = intent;
		attachToService();
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mRemoteViewService = IRemoteViewService.Stub.asInterface(service);
			setToken();
			setBounds();
			attachToSurfaceWhenReady();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			release();
		}

	};

	private void setBounds() {
		if (mRemoteViewService == sEmptyRemoteViewService) {
			return;
		}

		int[] loc = new int[2];
		getLocationOnScreen(loc);
		try {
			mRemoteViewService.setBounds(loc[0], loc[1], mWidth, mHeight);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mTextureView.layout(0, 0, r - l, b - t);
		setBounds();
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);

		if (mSurface != null) {
			try {

				if (visibility == View.GONE) {
					mRemoteViewService.setSurface(null);
				} else if (mLastVisibility == View.GONE) {
					// Don't change surface when going between View.VISIBLE and
					// View.INVISIBLE.
					mRemoteViewService.setSurface(mSurface);
				}
			} catch (RemoteException e) {
				throw new RuntimeException(
						"ActivityView: Unable to set surface of ActivityContainer. "
								+ e);
			}
		}
		mLastVisibility = visibility;
	}

	private boolean injectInputEvent(MotionEvent event) {
		try {
			return mRemoteViewService != null
					&& mRemoteViewService.injectEvent(event);
		} catch (RemoteException e) {
			return false;
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return injectInputEvent(event) || super.onTouchEvent(event);
	}

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		if ((event.getSource() & InputDevice.SOURCE_CLASS_POINTER) == InputDevice.SOURCE_CLASS_POINTER) {
			if (injectInputEvent(event)) {
				return true;
			}
		}
		return super.onGenericMotionEvent(event);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}

	/**
	 * release resource
	 */
	public void release() {
		if (mRemoteViewService == sEmptyRemoteViewService) {
			return;
		}

		try {
			mRemoteViewService.release();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		mRemoteViewService = sEmptyRemoteViewService;
		mActivity.unbindService(mConnection);

		if (mSurface != null) {
			mSurface.release();
			mSurface = null;
		}

		mTextureView.setSurfaceTextureListener(null);

	}

	private void attachToSurfaceWhenReady() {
		if (mRemoteViewService == sEmptyRemoteViewService) {
			return;
		}

		final SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
		if (surfaceTexture == null || mSurface != null) {
			return;
		}

		mSurface = new Surface(surfaceTexture);

		try {
			mRemoteViewService.setSurface(mSurface);
		} catch (RemoteException e) {
			mSurface.release();
			mSurface = null;
			throw new RuntimeException(
					"ActivityView: Unable to create ActivityContainer. " + e);
		}

	}

	private void setToken() {
		if (mRemoteViewService == sEmptyRemoteViewService) {
			return;
		}

		IBinder binder = this.getWindowToken();
		if (binder == null) {
			return;
		}
		Class<?> clazz = mActivity.getClass();
		Method m = null;
		try {
			m = clazz.getMethod("getActivityToken");
		} catch (NoSuchMethodException e) {
			m = null;
			e.printStackTrace();
		}
		IBinder token = null;
		if (m != null) {
			try {
				token = (IBinder) m.invoke(mActivity);
			} catch (IllegalAccessException e) {
				token = null;
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				token = null;
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				token = null;
				e.printStackTrace();
			}
		}

		try {
			mRemoteViewService.setToken(binder, token);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		attachToService();
		this.setToken();
		// attachToSurfaceWhenReady();
	}

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		this.release();
	}

	private class ActivityViewSurfaceTextureListener implements
			SurfaceTextureListener {
		@Override
		public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture,
				int width, int height) {
			if (DEBUG)
				Log.d(TAG, "onSurfaceTextureAvailable: width=" + width
						+ " height=" + height);
			mWidth = width;
			mHeight = height;

			setBounds();
			attachToSurfaceWhenReady();
		}

		@Override
		public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture,
				int width, int height) {

			if (DEBUG)
				Log.d(TAG, "onSurfaceTextureSizeChanged: w=" + width + " h="
						+ height);
		}

		@Override
		public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
			if (DEBUG)
				Log.d(TAG, "onSurfaceTextureDestroyed");
			if (mSurface != null) {
				mSurface.release();
				mSurface = null;
			}
			// release();

			return true;
		}

		@Override
		public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
			// Log.d(TAG, "onSurfaceTextureUpdated");
		}

	}

	public Bundle invoke(int resultCode, Bundle param) {
		if (mRemoteViewService == sEmptyRemoteViewService) {
			return null;
		}
		
		try {
			return mRemoteViewService.invoke(resultCode, param);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public interface ResultHandler {
		void onReceiveResult(int resultCode, Bundle resultData);
	}

	private ResultHandler mResultHandler;
	private MyResultReceiver mMyResultReceiver;
	
	public void setResultHandler(ResultHandler resultHandler) {
		mResultHandler = resultHandler;
	}

	private static class MyResultReceiver extends ResultReceiver {
		private RemoteView mRemote;

		public MyResultReceiver(RemoteView remote, Handler handler) {
			super(handler);
			mRemote = remote;
		}

		protected void onReceiveResult(int resultCode, Bundle resultData) {
			if (mRemote != null && mRemote.mResultHandler != null) {
				mRemote.mResultHandler.onReceiveResult(resultCode, resultData);
			}
		}
	}
}
