package meisteam.pf.post.ch.metaldetector;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import meisteam.pf.post.ch.metaldetector.R;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final int SCAN_QR_CODE_REQUEST_CODE = 0;

    Intent intent;

    private SensorManager sensorManager;
    private Sensor magnetSensor;
    private TextView magnetTextView;
    private ProgressBar magnetProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initalizeMagnetView();
        initalizeProgressBar();
    }

    private void initalizeMagnetView() {
        magnetTextView = (TextView) findViewById(R.id.magnetProgressView);
    }

    private void initalizeProgressBar() {
        magnetProgressBar = (ProgressBar) findViewById(R.id.magnetProgressBar);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        magnetSensor = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
        sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);
        magnetProgressBar.setMax((int) magnetSensor.getMaximumRange());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add("Log");
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, SCAN_QR_CODE_REQUEST_CODE);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] mag = event.values;
        double value = Math.sqrt(mag[0] * mag[0] + mag[1] * mag[1] + mag[2] * mag[2]);

        magnetTextView.setText("Actual measured value: " + ((int) value) * 50000 + " of " + View.MEASURED_SIZE_MASK);
        magnetProgressBar.setProgress((int) value);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.println(Log.INFO, "onAccuracyChanged", "Sensor accuracy changed.");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SCAN_QR_CODE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String logMsg = intent.getStringExtra("SCAN_RESULT");
                try {
                    log(logMsg);
                } catch (JSONException e) {
                    Log.println(Log.ERROR, "Logger", "Could not convert Json!");
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void log(String qrCode) throws JSONException {
        Intent intent = new Intent("ch.appquest.intent.LOG");

        if (getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty()) {
            Toast.makeText(this, "Logbook App not installed", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("task", "Metalldetektor");
        jsonMessage.put("solution", qrCode);
        String message = jsonMessage.toString();

        intent.putExtra("ch.appquest.logmessage", message);

        startActivity(intent);
    }
}