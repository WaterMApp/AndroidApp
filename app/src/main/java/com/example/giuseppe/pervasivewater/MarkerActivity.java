package com.example.giuseppe.pervasivewater;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;
import android.location.Address;
import android.location.Geocoder;
import com.android.volley.toolbox.JsonObjectRequest;
import java.util.List;
import java.util.Locale;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MarkerActivity extends AppCompatActivity {
    // Will show the string "data" that holds the results
    TextView results1;
    TextView results2;
    // URL of object to be parsed
    String JsonURLbaseComments = "http://srv1.gabrio.ovh:9998/fountain_comments/";
    String JsonURLbaseData = "http://srv1.gabrio.ovh:9998/fountain_data/";
    // This string will hold the results
    String data1 = "";
    String data2 = "";
    // Defining the Volley request queue that handles the URL request concurrently
    RequestQueue requestQueue;
    String pass="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);


        //iconActionBar
        ActionBar actionbar= getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.drawable.bar);

        Intent intent = getIntent();
        String activity = intent.getStringExtra("activity");
        String activityID = intent.getStringExtra("id");

        String[] tuple = activity.toString().split(",");
        double lat = Double.parseDouble(tuple[0]);
        double lon = Double.parseDouble(tuple[1]);
        pass=activityID;
        Log.d("activityId",activityID);
        String JsonURLComments = JsonURLbaseComments+activityID;
        String JsonURLData = JsonURLbaseData+activityID;


        Geocoder geocoder;
        List<Address> addresses=null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address;
        try {
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        } catch (Exception e) {
            address = "unknown address";
        }

        TextView myTextView = (TextView) findViewById(R.id.mytextview);
        myTextView.setText("The fontanella selected is " + address);



        // Creates the Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Casts results into the TextView found within the main layout XML with id jsonData
        results1 = (TextView) findViewById(R.id.jsonData1);
        results2 = (TextView) findViewById(R.id.jsonData2);

        // Creating the JsonArrayRequest class called arrayreq, passing the required parameters
        //JsonURL is the URL to be fetched from
        JsonArrayRequest arrayreq = new JsonArrayRequest(JsonURLComments,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                //gets each JSON object within the JSON array
                                JSONObject jsonObject = response.getJSONObject(i);

                                // Retrieves the string labeled "colorName" and "hexValue",
                                // and converts them into javascript objects
                                String name = jsonObject.getString("name");
                                String comment = jsonObject.getString("text");

                                // Adds strings from the current object to the data string
                                //spacing is included at the end to separate the results from
                                //one another
                                data2 += "• Name: " + name +"\n   " + comment + "\n";
                            }
                            // Adds the data string to the TextView "results"
                            results2.setText(data2);
                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
        requestQueue.add(arrayreq);




        // Casts results into the TextView found within the main layout XML with id jsonData
        results1 = (TextView) findViewById(R.id.jsonData1);

        JsonObjectRequest gabreq = new JsonObjectRequest(JsonURLData,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckResponse",response.toString());
               try {
                   String turb = response.getString("turb");
                   String ph = response.getString("ph");
                   String rate = response.getString("temp");
                   String id = response.getString("timestamp");
                   data1 += "ID: " + id +"\nph:" + ph + "\nturb:"+turb+ "\nrate:"+rate;
               }catch (Exception ex){
                   ex.printStackTrace();
                        // Adds strings from the current object to the data string
                        //spacing is included at the end to separate the results from
                        //one another

                    }
                    // Adds the data string to the TextView "results"
                    results1.setText(data1);

            }

        }, null);

        requestQueue.add(gabreq);


        //pop-Up
        Button b=(Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MarkerActivity.this,Pop.class);
                intent.putExtra("myID", pass);
                Log.d("CheckID",pass);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                startActivity(intent);
            }
        });

    }
}
