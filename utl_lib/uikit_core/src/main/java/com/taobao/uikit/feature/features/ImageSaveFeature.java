/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taobao.uikit.feature.features;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.ImageSaveCallback;
import com.taobao.uikit.feature.callback.TouchEventCallback;


/** 
 * @ClassName: ImageSaveFeature 
 * @Description: New Feature to make TUrlImageView pop up specific menu when long clicked.
 * @author "weina.mawn<weina.mawn@alibaba-inc.com>"
 * @date 2014年10月13日 下午12:08:46  *  
 */

public class ImageSaveFeature extends AbsFeature<ImageView> implements ImageSaveCallback, TouchEventCallback{

	private ImageView mOriginView;
	private Context mContext;
    
    private Dialog mDialog;
    
    private PointF mStartPoint = new PointF();

    //private PointF mMidPoint = new PointF();
    
    /**Boolean flag to indicate whether to perform long click action
     * (in case users are zooming image view or long press it using more than two fingers)*/
    private boolean mActive = false;
    
    /**Choices of image save dialog.*/
    private HashMap<String, ImageSaveFeatureCallback> mChoices = new HashMap<String, ImageSaveFeatureCallback> ();
    
    public interface ImageSaveFeatureCallback {
    	void doMethod(ImageView imageView);
    }

	@Override
	public void setHost(ImageView host) {
		super.setHost(host);
		mOriginView = host;
		mContext = host.getContext();
		if(mContext!=null){
			mChoices.put(mContext.getResources().getString(R.string.uik_save_image),
	    			new ImageSaveFeatureCallback() {
				
				@Override
				public void doMethod(ImageView imageView) {
					saveImageView(imageView);
				}
			});
		}
	}
    
	@Override
	public void beforePerformLongClick() {
		if(mActive){
			showDialog();
		}
	}

	@Override
	public void afterperformLongClick() {

	}

	@Override
	public void beforeOnTouchEvent(MotionEvent event) {
		switch(event.getAction() & MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN:
			mStartPoint.set(event.getX(), event.getY());
			mActive = true;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			mActive = false;
			break;
		case MotionEvent.ACTION_MOVE:
			{
				float dxx = event.getX() - mStartPoint.x;
				float dyy = event.getY() - mStartPoint.y;
				if(Math.abs(dxx) > 10.f || Math.abs(dyy) > 10.0f) {
					mActive = false;
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void afterOnTouchEvent(MotionEvent event) {
		
	}

	@Override
	public void beforeDispatchTouchEvent(MotionEvent event) {
		
	}

	@Override
	public void afterDispatchTouchEvent(MotionEvent event) {
		
	}
	
	@Override
	public void constructor(Context context, AttributeSet attrs, int defStyle) {
		
	}

	/**
	 * Add a choice to Dialog of ImageView.
	 * @param title
	 * @param callback
	 * */
	public void addDialogChoice(String title, ImageSaveFeatureCallback callback){
		mChoices.put(title, callback);
	}
	
	/**
	 * Show a dialog when image view is long clicked.
	 * */
	@SuppressLint("InflateParams") private void showDialog() {
		if(!(mChoices.size()>0)){//there is no choice
			return;
		}
		
		//new dialog 
		mDialog = new Dialog(mContext, R.style.uik_imagesavedialog);
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout = (LinearLayout)inflater.inflate(R.layout.uik_image_save_dialog, null);
       
        Iterator<String> it = mChoices.keySet().iterator();
        
        while(it.hasNext()){
        	TextView textView = (TextView)(inflater.inflate(R.layout.uik_image_save_choice, linearLayout,false));
        	String title = it.next();
        	textView.setText(title);
        	final ImageSaveFeatureCallback callback = mChoices.get(title);
        	textView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					callback.doMethod(mOriginView);
					if (mDialog != null && mDialog.isShowing()) {
						mDialog.dismiss();
						mDialog = null;
					}
				}
			});
        	linearLayout.addView(textView);
        	
        	if(it.hasNext()){
        		View divider = inflater.inflate(R.layout.uik_choice_divider, linearLayout, false);
        		linearLayout.addView(divider);
        	}
        }

        mDialog.setContentView(linearLayout);
        mDialog.show();
    }
	

	private void saveImageView(ImageView imageView) {

		Drawable dr = imageView.getDrawable();
		if(dr == null){
			dr = imageView.getBackground();
			if(dr == null){
				Toast.makeText(mContext.getApplicationContext(), getStringResource(R.string.uik_save_image_fail_get), Toast.LENGTH_SHORT).show();
				return;
			}
		}

		Bitmap bm = Bitmap.createBitmap((dr.getIntrinsicWidth()==-1)?imageView.getWidth():dr.getIntrinsicWidth(),
				(dr.getIntrinsicHeight()==-1)?imageView.getHeight():dr.getIntrinsicHeight(), Bitmap.Config.RGB_565);
		
		Canvas canvas = new Canvas(bm);
		dr.draw(canvas);
		
		if(null == bm) {
			Toast.makeText(mContext.getApplicationContext(),getStringResource(R.string.uik_save_image_fail_get), Toast.LENGTH_SHORT).show();
			return;
		}

		String fileName = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES).toString() + 
				"/" + dr.toString().hashCode() + ".png";
		
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
			bm.compress(Bitmap.CompressFormat.PNG, 100, out);

			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri uri = Uri.fromFile(new File(fileName));
			intent.setData(uri);
			mContext.sendBroadcast(intent);
			Toast.makeText(mContext.getApplicationContext(),getStringResource(R.string.uik_save_image_success),Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(mContext.getApplicationContext(),getStringResource(R.string.uik_save_image_fail_full),Toast.LENGTH_SHORT).show();
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getStringResource(int id) {
		return mContext.getResources().getString(id);
	}

}