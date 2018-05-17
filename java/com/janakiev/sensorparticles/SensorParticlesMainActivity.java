package com.janakiev.sensorparticles;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import simplepath.SimplePathRenderer;

public class SensorParticlesMainActivity extends ActionBarActivity implements SensorEventListener {
    private static final String TAG = "SensorParticlesMainActivity";
    private GLSurfaceView glSurfaceView;
    private SensorManager sensorManager;
    private SimplePathRenderer simplePathRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);

        simplePathRenderer = new SimplePathRenderer(this);
        glSurfaceView.setRenderer(simplePathRenderer);

        setContentView(glSurfaceView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null){
            Toast.makeText(getApplicationContext(), "TYPE_ACCELEROMETER missing",
                    Toast.LENGTH_SHORT).show();
        } else if(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null){
            Toast.makeText(getApplicationContext(), "TYPE_GYROSCOPE missing",
                    Toast.LENGTH_SHORT).show();
        } else if(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null){
            Toast.makeText(getApplicationContext(), "TYPE_MAGNETIC_FIELD missing",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_openg_glcube_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        final float[] values = event.values;

        switch (sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                if(simplePathRenderer != null){
                    glSurfaceView.queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            simplePathRenderer.addAccSensorData(values);
                        }
                    });
                }
                break;
            case Sensor.TYPE_GYROSCOPE:
                if(simplePathRenderer != null){
                    glSurfaceView.queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            simplePathRenderer.addGyroSensorData(values);
                        }
                    });
                }
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                if(simplePathRenderer != null){
                    glSurfaceView.queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            simplePathRenderer.addMagnetSensorData(values);
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
