package com.taobao.uikit.remote;


import  android.view.Surface;
import 	android.view.MotionEvent;


// Interface declaration
interface IRemoteViewService {
     void setSurface(in Surface surface);
     void setBounds(in int x,in int y,in int width,in int height);
     boolean injectEvent(in MotionEvent event);
     void release();
     void setToken(in IBinder window,in IBinder appWindow);
     Bundle invoke(in int resultCode, in Bundle param);
}