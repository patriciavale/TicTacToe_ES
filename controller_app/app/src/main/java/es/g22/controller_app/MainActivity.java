package es.g22.controller_app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.controller_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity{

    private TextView deviceIDView;
    private View statusBar;
    private View movClickZone;

    private String deviceID = "";

    private RequestQueue requestQueue;

    private String urlGetID = "http://192.168.160.80:8251/getid";
    private String urlKeepAlive = "http://192.168.160.80:8251/alive";
    private String urlSendData = "http://192.168.160.80:8251/send";

    private String colorError = "#D83737";
    private String colorOk = "#1a7503";

    private Timer keepAliveTimer = new Timer();

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private ArrayList<Reading> data_to_send;
    private float lastX, lastY, lastZ;

    private SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
             if(deviceID == ""){
                 return;
             }
             float xChange = lastX - event.values[0];
             float yChange = lastY - event.values[1];
             float zChange = lastZ - event.values[2];

             lastX = event.values[0];
             lastY = event.values[1];
             lastZ = event.values[2];

             data_to_send.add(new Reading(xChange, yChange, zChange));


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();

        //Get Accelerometer
        if (!getAccelerometer()) {
            deviceIDView.setText("No Accelerometer. Restart app");
            return;
        }

        requestQueue = Volley.newRequestQueue(this);

        getId();

        statusBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceIDView.setText("Getting new ID!");
                getId();
            }
        });

        movClickZone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        //Log.i("Motion", "Finger down");
                        if (!deviceID.equals("")) {
                            data_to_send = new ArrayList<Reading>();
                            movClickZone.setBackgroundColor(Color.parseColor("#000000"));
                            sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        //Log.i("Motion", "Finger up");
                        if (!deviceID.equals("")) {
                            //Log.i("Motion", data_to_send.toString());
                            movClickZone.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            //
                            sendMovement();
                        }
                        return true;
                }
                return false;
            }
        });
    }

    public void initializeViews() {
        deviceIDView = (TextView) findViewById(R.id.device_id);
        statusBar = (View) findViewById(R.id.status);
        movClickZone = (View) findViewById(R.id.movArea);
    }

    public void getId() {
        keepAliveTimer.cancel();
        keepAliveTimer.purge();
        keepAliveTimer = new Timer();
        StringRequest idrequest = new StringRequest(Request.Method.GET, urlGetID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("ERROR")) {
                            deviceIDView.setText("NO ID: Server reported an error");
                            statusBar.setBackgroundColor(Color.parseColor(colorError));
                        }
                        deviceIDView.setText(response);
                        deviceID = response;
                        statusBar.setBackgroundColor(Color.parseColor(colorOk));
                        keepAlive();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        deviceIDView.setText("NO ID: Can't reach server");
                        statusBar.setBackgroundColor(Color.parseColor(colorError));
                    }
                });

        requestQueue.add(idrequest);

    }

    public void keepAlive() {
        //Send packet to keep alive.

        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                StringRequest keepAliveRequest = new StringRequest(Request.Method.POST, urlKeepAlive,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("ERROR")) {
                                    statusBar.setBackgroundColor(Color.parseColor(colorError));
                                    deviceIDView.setText("Keep Alive failed. Click to get new ID");
                                    keepAliveTimer.cancel();
                                    keepAliveTimer.purge();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                statusBar.setBackgroundColor(Color.parseColor(colorError));
                                deviceIDView.setText("Keep Alive failed. Click to get new ID");
                                keepAliveTimer.cancel();
                                keepAliveTimer.purge();
                            }
                        }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", deviceID);
                        return params;
                    }
                };
                requestQueue.add(keepAliveRequest);
            }
        };

        keepAliveTimer.scheduleAtFixedRate(timerTask, 0, 10000);
    }

    public boolean getAccelerometer(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            //We have accelerometer
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            return true;
        } else {
            //No accelerometer found
            return false;
        }
    }

    public void sendMovement(){
        JSONArray mov_json = new JSONArray();
        for (Reading r : data_to_send){
            mov_json.put(r.toJSON());
        }
        JSONObject send_json = new JSONObject();
        try {
            send_json.put("device_id", deviceID);
            send_json.put("data", mov_json);
        } catch (JSONException e) {
            Log.e("SendMovement", "Couldn't format JSON object" + e.toString());
            return;
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlSendData, send_json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SendMov", response.toString());
                        if (response.has("error"))
                            Log.e("SendMovement", "Server returned an error");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SendMovement", "Error connecting to server");
                    }
                });
        requestQueue.add(jsonObjReq);
    }
}
