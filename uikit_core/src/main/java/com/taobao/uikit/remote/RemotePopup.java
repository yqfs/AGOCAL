package com.taobao.uikit.remote;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

public class RemotePopup {
	private Activity mActivity;
	private View mParent;
	private IRemoteViewService mRemoteViewService;

	public RemotePopup(Context context) {
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
	}

	public void show(Intent intent, View parent) {
		mParent = parent;
		mActivity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mRemoteViewService = IRemoteViewService.Stub.asInterface(service);

			try {
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
				mRemoteViewService.setToken(mParent.getWindowToken(), token);
			} catch (RemoteException e) {
				e.printStackTrace();
			} finally {
				mActivity.unbindService(mConnection);
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

	};
}
