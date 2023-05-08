package com.example.second_project_11345.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.example.second_project_11345.Interfaces.TiltCallback;

public class TiltDetector {
    private final Sensor sensor;
    private final SensorManager sensorManager;
    private final TiltCallback tiltCallback;
    private long timestamp = 0L;
    private SensorEventListener sensorEventListener;

    public TiltDetector(Context context, TiltCallback tiltCallback) {
        this.sensorManager = (SensorManager)context.getSystemService("sensor");
        this.sensor = this.sensorManager.getDefaultSensor(1);
        this.tiltCallback = tiltCallback;
        this.initEventListener();
    }

    private void initEventListener() {
        this.sensorEventListener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                float y = event.values[0];
                TiltDetector.this.calculateChange(y);
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    private void calculateChange(float y) {
        if (System.currentTimeMillis() - this.timestamp > 500L) {
            this.timestamp = System.currentTimeMillis();
            if ((double)y > 4.0) {
                if (this.tiltCallback != null) {
                    this.tiltCallback.turnLeft();
                }
            } else if ((double)y < -4.0 && this.tiltCallback != null) {
                this.tiltCallback.turnRight();
            }
        }

    }

    public void start() {
        this.sensorManager.registerListener(this.sensorEventListener, this.sensor, 3);
    }

    public void stop() {
        this.sensorManager.unregisterListener(this.sensorEventListener, this.sensor);
    }
}
