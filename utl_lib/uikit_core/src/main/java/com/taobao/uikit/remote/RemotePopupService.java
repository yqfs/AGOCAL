package com.taobao.uikit.remote;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;

public abstract class RemotePopupService extends Service{
	
	private Bundle mSavedInstanceState;
	
	protected abstract void onCreatePopup(View parent,IBinder activityToken,
            Bundle savedInstanceState);
	
	@Override
	public IBinder onBind(Intent intent) {
		// Return the interface
		mSavedInstanceState = intent.getExtras();
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		mSavedInstanceState = null;
		return super.onUnbind(intent);
	}
	
	private final IRemoteViewService.Stub mBinder = new IRemoteViewService.Stub() {
		private IBinder mWindowToken;
		private IBinder mActivityToken;
		@Override
		public void setSurface(Surface surface) throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setBounds(int x, int y, int width, int height)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean injectEvent(MotionEvent event) throws RemoteException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void release() throws RemoteException {
			// TODO Auto-generated method stub			
		}
		
		class ParentView extends View {
			public ParentView(Context context) {
				super(context);
			}

			public IBinder getWindowToken() {
				return mWindowToken;
			}
		}

		@Override
		public void setToken(IBinder window, IBinder activity)
				throws RemoteException {
			mWindowToken = window;
			mActivityToken = activity;
			if (mWindowToken != null) {
				onCreatePopup(new ParentView(RemotePopupService.this),
						mActivityToken, mSavedInstanceState);
			}

		}


		@Override
		public Bundle invoke(int resultCode, Bundle resultData)
				throws RemoteException {
			// TODO Auto-generated method stub
			return null;
		}
	};
}
