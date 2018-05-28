package com.example.giuseppe.pervasivewater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.toolbox.StringRequest;

public class Pop_add extends Activity {
    double lat=0;
    double lng=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pop_add);
        Intent intent = getIntent();
        String address = intent.getStringExtra("myaddress"); //get position from MapsActivity
        String latlong = intent.getStringExtra("myposition"); //get position from MapsActivity
        String[] parts2 = latlong.split(",");
        lat = Double.parseDouble(parts2[0]); // latitude
        lng = Double.parseDouble(parts2[1]); // longitude
        Log.d("COORDINATE","popadd:"+lat+" "+lng);

        TextView myTextView = (TextView) findViewById(R.id.myaddress);
        myTextView.setText(address);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width* .9),(int)(height* .5));

        //pop-Up
        Button b3=(Button) findViewById(R.id.delete1);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish this activity
                finish();
            }
        });

        Button b4=(Button) findViewById(R.id.send1);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //post request with the current position lat,lng
                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(Pop_add.this);
                    String URL = "http://srv1.gabrio.ovh:9998/fountain_create";
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("lat", lat);
                    jsonBody.put("lng", lng);
                    final String mRequestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                                return null;
                            }
                        }

                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            String responseString = "";
                            if (response != null) {
                                responseString = String.valueOf(response.statusCode);
                                // can get more details such as response.headers
                            }
                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                        }
                    };

                    requestQueue.add(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //confirmation
                Toast.makeText(getBaseContext(), "Address sent!", Toast.LENGTH_LONG).show();
                //finish this activity
                finish();
                //reload the map with the new marker
                startActivity(new Intent(Pop_add.this,MapsActivity.class));
                overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
            }
        });

    }


}
