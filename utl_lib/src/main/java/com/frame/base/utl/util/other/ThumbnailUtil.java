package com.frame.base.utl.util.other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

public class ThumbnailUtil {

    private final static String tag = "ThumbnailUtil";

    /**
     * 判断是否可以读取到exif内的缩略图.
     *
     * @return
     */
    public static boolean isExistExif(String path) {
        if (path != null) {
            ExifInterface exif = null;
            byte[] byThumb = null;
            try {
                exif = new ExifInterface(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (exif != null) {
                byThumb = exif.getThumbnail();
            }
            return byThumb == null;
        }
        return false;
    }

    /**
     * 读取一张图片exif头内的旋转角度.
     *
     * @return
     */
    public static int readRotationDegree(String path) {
        if (path != null) {
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (exif != null) {
                int degree = getOrientation(exif);
                Log.i(tag, "**getOrientation(exif) = " + degree);
                return degree;
            }
        }
        return -1;
    }

    /**
     * Create a thumbnail bitmap for specified path.
     *
     * @param bFitIn If bFitIn is true, will return a smaller bitmap for fit in bitmap.
     *               Otherwise, will return a bigger bitmap for center crop.
     * @return
     */
    public static Bitmap getThumbnail(String path, int width, int height, boolean bFitIn) {
        Bitmap thumb = null;
        if (path != null) {
            ExifInterface exif = null;
            byte[] byThumb = null;
            try {
                exif = new ExifInterface(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (exif != null) {
                byThumb = exif.getThumbnail();
            } else {
                Log.i(tag, "oh, we can't read exif.");
            }
            if (byThumb == null) {
                thumb = decodeBitmapFromFile(path, width, height, bFitIn);
            } else {
                thumb = decodeBitmapFromByteArray(byThumb, width, height, bFitIn);
            }
            int degrees = getOrientation(exif);

            Log.i(tag, "getOrientation(exif) = " + degrees);

            return rotate(thumb, degrees);
        }
        return null;
    }

    public static void writeOrientation(String path, int orientation) {
        Log.i(tag, "get orientation : " + orientation);
        int temp = ExifInterface.ORIENTATION_UNDEFINED;
        switch (orientation) {
            case 0:
                temp = ExifInterface.ORIENTATION_NORMAL;
                break;
            case 90:
                temp = ExifInterface.ORIENTATION_ROTATE_90;
                break;
            case 180:
                temp = ExifInterface.ORIENTATION_ROTATE_180;
                break;
            case 270:
                temp = ExifInterface.ORIENTATION_ROTATE_270;
                break;
        }

        if (path != null) {
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(path);
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, temp + "");
                try {
                    exif.saveAttributes();
                } catch (IOException e) {
                    Log.e(tag, "cannot save exif", e);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //If bFitIn is true, will return a smaller bitmap for fit in bitmap.
    //Otherwise, will return a bigger bitmap for center crop.
    private static Bitmap decodeBitmapFromFile(String fileName, int minWidth, int minHeight, boolean bFitIn) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, opts);
        setSampleSize(opts, minWidth, minHeight, bFitIn);
        opts.inJustDecodeBounds = false;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bmp = BitmapFactory.decodeFile(fileName, opts);
        return bmp;
    }

    private static Bitmap decodeBitmapFromByteArray(byte[] bytes, int minWidth, int minHeight, boolean bFitIn) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        setSampleSize(opts, minWidth, minHeight, bFitIn);
        opts.inJustDecodeBounds = false;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        return bmp;
    }

    //If bFitIn is true, will return a sample size for fit in bitmap. (Bigger sample size, smaller bitmap)
    //Otherwise, will return a sample size for center crop. (Smaller sample size, bigger bitmap)
    private static void setSampleSize(BitmapFactory.Options opts, int width, int height, boolean bFitin) {
        float x = (float) Math.max(opts.outWidth, opts.outHeight) / Math.max(width, height);
        float y = (float) Math.min(opts.outWidth, opts.outHeight) / Math.min(width, height);
        if (x <= 1 || y <= 1) {
            opts.inSampleSize = 1;
        } else {
            if (bFitin) {
                opts.inSampleSize = x > y ? Math.round(x) : Math.round(y);
            } else {
                opts.inSampleSize = x < y ? Math.round(x) : Math.round(y);
            }
        }
    }

    public static int getOrientation(ExifInterface exif) {
        int degree = 0;
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation TAG values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    // Rotates the bitmap by the specified degree.
    // If a new bitmap is created, the original bitmap is recycled.
    public static Bitmap rotate(Bitmap b, int degree) {
        if (degree != 0 && b != null) {
            Matrix m = new Matrix();
            m.postRotate(degree, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                Log.e("thumbnailUtil.rotate", ex.toString());
            }
        }
        return b;
    }

}

