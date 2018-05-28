package com.example.giuseppe.pervasivewater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

public class Pop extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pop_window);
      Intent intent = getIntent();
       final String activityID = intent.getStringExtra("myID");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width* .9),(int)(height* .5));

        //pop-Up
        Button b1=(Button) findViewById(R.id.delete);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button b2=(Button) findViewById(R.id.send);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting nickname and comment from EditText
                final EditText edit1 =  (EditText) findViewById(R.id.editText2);
                String comment="";
                comment = (String) edit1.getText().toString();
                Log.d("comment",comment);
                final EditText edit2 =  (EditText) findViewById(R.id.editText3);
                String nick="";
                nick = (String) edit2.getText().toString();
                Log.d("nick",nick);


                //Post request to the server
                //with the nickname and comment
                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(Pop.this);
                    String URL = "http://srv1.gabrio.ovh:9998/fountain_comments/"+activityID;
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("name", nick);
                    jsonBody.put("text", comment);
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
                //display the success
                Toast.makeText(getBaseContext(), "Comment sent!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }


}
