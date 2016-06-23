package com.taobao.uikit.component;

import com.taobao.uikit.R;
import com.taobao.uikit.feature.view.TView;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xiekang.xiekang on 2014/10/13.
 */
public class GifView extends TView
{

    //fix 4.1.2 version bug
    private static final int SUBTLE_PARAM = 1;

    private Movie mMovie;

    private long mMovieStart;

    private int mMovieWidth;

    private int mMovieHeight;

    private int mCanvasWidth;

    private int mCanvasHeight;

    private boolean mIsPlaying;

    private boolean mIsPlayed;

    private boolean mAutoPlay;

    private int mGifResId;

    private String mGifFilePath;

    private OnPlayEndListener mEndListener;

    public interface OnPlayEndListener
    {

        void OnPlayEnd();
    }

    public GifView(Context context)
    {
        this(context, null);
    }

    public GifView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public GifView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GifView);
        if (null != a)
        {
            mAutoPlay = a.getBoolean(R.styleable.GifView_uik_auto_play, false);
            mGifResId = a.getResourceId(R.styleable.GifView_uik_gif_src, 0);
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (mMovie != null)
        {
            canvas.save();
            fixCanvas(canvas);
            if (mAutoPlay)
            {
                playMovie(canvas);
                invalidate();
            }
            else
            {
                if (mIsPlaying)
                {
                    if (playMovie(canvas))
                    {
                        mIsPlaying = false;
                        mIsPlayed = true;
                    }
                    invalidate();
                }
                else
                {
                    if (mIsPlayed)
                    {
                        mMovie.setTime(mMovie.duration() - SUBTLE_PARAM);
                    }
                    else
                    {
                        mMovie.setTime(0);
                    }
                    mMovie.draw(canvas, 0, 0);

                    if (mIsPlayed && null != mEndListener)
                    {
                        mEndListener.OnPlayEnd();
                    }
                }
            }
            canvas.restore();
        }
    }

    private void fixCanvas(Canvas canvas)
    {
        int dwidth = mMovieWidth;
        int dheight = mMovieHeight;

        int vwidth = mCanvasWidth;
        int vheight = mCanvasHeight;
        float scale;
        float dx = 0;
        float dy = 0;

        if (dwidth * vheight > vwidth * dheight)
        {
            scale = (float) vheight / (float) dheight;
            dx = (vwidth - dwidth * scale) * 0.5f;
        }
        else
        {
            scale = (float) vwidth / (float) dwidth;
            dy = (vheight - dheight * scale) * 0.5f;
        }

        canvas.translate((int) (dx + 0.5f), (int) (dy + 0.5f));
        canvas.scale(scale, scale);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int w;
        int h;

        if (mMovie == null)
        {
            mMovieWidth = -1;
            mMovieHeight = -1;
            w = h = 0;
        }
        else
        {
            w = mMovieWidth;
            h = mMovieHeight;
            if (w <= 0)
            {
                w = 1;
            }
            if (h <= 0)
            {
                h = 1;
            }
        }

        mCanvasWidth = resolveSize(w, widthMeasureSpec);
        mCanvasHeight = resolveSize(h, heightMeasureSpec);

        setMeasuredDimension(mCanvasWidth, mCanvasHeight);
    }

    @Override protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        decode();
    }

    @Override protected void onDetachedFromWindow()
    {
        mMovie = null;
        super.onDetachedFromWindow();
    }

    private boolean playMovie(Canvas canvas)
    {
        long now = AnimationUtils.currentAnimationTimeMillis();
        if (mMovieStart == 0)
        {
            mMovieStart = now;
        }
        int duration = mMovie.duration() - SUBTLE_PARAM;
        if (duration == 0)
        {
            duration = 1000;
        }

        boolean ret = false;
        int relTime = (int) (now - mMovieStart);
        if (relTime >= duration)
        {
            relTime = duration;
            mMovieStart = 0;
            ret = true;
        }
        mMovie.setTime(relTime);
        mMovie.draw(canvas, 0, 0);
        return ret;
    }

    public void setGifResource(int resId)
    {
        if (resId != 0 && resId != mGifResId)
        {
            mGifResId = resId;
            mGifFilePath = null;
            decode();
        }
    }

    public void setGifFilePath(final String path)
    {
        if (null != path && !TextUtils.equals(path, mGifFilePath))
        {
            mGifFilePath = path;
            mGifResId = 0;
            decode();
        }
    }

    private void decode()
    {
        InputStream is = null;
        try
        {
            if (0 != mGifResId)
            {
                is = getResources().openRawResource(mGifResId);
            }
            else if (!TextUtils.isEmpty(mGifFilePath))
            {
                is = new BufferedInputStream(new FileInputStream(mGifFilePath));
                if (is.markSupported())
                {
                    is.mark(is.available());
                }
            }

            if (null != is)
            {
                mMovie = Movie.decodeStream(is);
                if (mMovie != null)
                {
                    mMovieWidth = mMovie.width();
                    mMovieHeight = mMovie.height();
                }
                requestLayout();
                invalidate();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != is)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void play()
    {
        mMovieStart = 0;
        mIsPlaying = true;
        mIsPlayed = false;
        mAutoPlay = false;
        invalidate();
    }

    public void autoPlay()
    {
        mAutoPlay = true;
        invalidate();
    }

    public void stop()
    {
        mIsPlaying = false;
        mAutoPlay = false;
        mIsPlayed = false;
        invalidate();
    }

    public void setPlayEndListener(OnPlayEndListener endListener)
    {
        mEndListener = endListener;
    }
}
