package com.frame.base.utl.util.other;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;

import com.frame.base.utl.application.BaseApplication;

import java.util.List;

public class SensorHelper {

  public static final int SENSOR_SHAKE = 10;
  private static Vibrator vibrator;
  // private JFBClainModule clainModel;
  private SensorManager sensorManager;
  private Handler handler;
  private long shakeTime = 0;
  private SensorEventListener sensorEventListener = new SensorEventListener() {

    @Override
    public void onSensorChanged(SensorEvent event) {
      try {
        // 传感器信息改变时执行该方法
        float[] values = event.values;
        float x = values[0]; // x轴方向的重力加速度，向右为正
        float y = values[1]; // y轴方向的重力加速度，向前为正
        float z = values[2]; // z轴方向的重力加速度，向上为正
        // Log.i(TAG, "x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y +
        // "；z轴方向的重力加速度" +
        // z);
        // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
        int medumValue = 11;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
        if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {

          if ((System.currentTimeMillis() - shakeTime) > 5000) {
            shakeTime = System.currentTimeMillis();
            // 震动200 毫秒
            vibrator.vibrate(200);
            shakeTime = System.currentTimeMillis();
            Message msg = new Message();
            msg.what = SENSOR_SHAKE;
            handler.sendMessage(msg);
          }

        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
  };

  public SensorHelper(Handler handler) {
    this.handler = handler;
    sensorManager = (SensorManager) BaseApplication.getContext().getSystemService(Context.SENSOR_SERVICE);
    vibrator = (Vibrator) BaseApplication.getContext().getSystemService(Context.VIBRATOR_SERVICE);
  }

  public void registerSensorListener() {
    if (sensorManager != null) {
      sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                                     SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  public void unRegisterSensorListener() {
    if (sensorManager != null) {
      sensorManager.unregisterListener(sensorEventListener);
    }
  }

  public boolean hasSensorDevice() {

    List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
    if (sensors != null) {
      return sensors.size() != 0;

    }

    return false;
  }

  public void stopSensor() {

  }
}
