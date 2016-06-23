package com.frame.base.utl.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

public class Rotation3DAnimation extends Animation {
    public static final int ROTATE_Y = 0;
    public static final int ROTATE_X = 1;

    int centerX, centerY, duration, rotate, angel;
    boolean isZoomin;
    Camera camera = new Camera();

    public Rotation3DAnimation() {
        duration = 400;
        rotate = ROTATE_Y;
        angel = 180; //
        isZoomin = false;
    }

    /**
     * @param duration
     * @param rotate
     * @param angel
     * @param isZoomin
     */
    public Rotation3DAnimation(int duration, int rotate, int angel, boolean isZoomin) {
        this.duration = duration;
        this.rotate = rotate;
        this.angel = angel;
        this.isZoomin = isZoomin;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        centerX = width / 2;
        centerY = height / 2;
        setDuration(duration);
        //setFillAfter(true);
        setFillBefore(true);
        setInterpolator(new DecelerateInterpolator());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final Matrix matrix = t.getMatrix();
        int direction = -1;
        camera.save();
        if (!isZoomin) {
            interpolatedTime = 1 - interpolatedTime;
            direction = 1;
        }

        if (rotate == ROTATE_Y) {
            camera.rotateY(angel * interpolatedTime * direction); 
        } else if (rotate == ROTATE_X) {
            camera.rotateX(angel * interpolatedTime * direction); 
        }

        camera.getMatrix(matrix);
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
        camera.restore();
    }

}
