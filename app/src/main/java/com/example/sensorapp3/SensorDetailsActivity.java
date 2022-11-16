package com.example.sensorapp3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView sensorNameTextView;
    private TextView sensorValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        Intent intent = getIntent();
        int type = intent.getIntExtra(SensorActivity.KEY_EXTRA_SENSOR_TYPE, Sensor.TYPE_LIGHT);

        sensorNameTextView = findViewById(R.id.sensor_name);
        sensorValueTextView = findViewById(R.id.sensor_value);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        switch(type) {
            case Sensor.TYPE_LIGHT:
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                sensorNameTextView.setText(getResources().getString(R.string.Light_sensor_label));
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
                sensorNameTextView.setText(getResources().getString(R.string.Humidity_sensor_label));
                break;
            default:
                sensor = null;
                break;
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];
        String str;
        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                str = getResources().getString(R.string.Light_sensor_value, currentValue);
                sensorValueTextView.setText(str);
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                str = getResources().getString(R.string.Humidity_sensor_value, currentValue);
                sensorValueTextView.setText(str);
                break;
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }
}