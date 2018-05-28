package com.example.giuseppe.pervasivewater;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.widget.Toast;
import android.content.res.Resources;
import android.location.*;
import android.util.Log;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import java.util.Iterator;
import java.util.Locale;
import android.view.Menu;
import android.view.MenuItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MapsActivity extends AppCompatActivity
        implements
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    private Marker mMarker;
    private static final String TAG = "MapActivity";
    private boolean isInfoWindowShown = false;
    RequestQueue requestQueue;
    private JSONArray result;
    private String data2="";
    private String data1="";
    private double lat5;
    private double long5;
    private String id5;
    Fountain coord5;
    String JsonURL1 = "http://srv1.gabrio.ovh:9998/fountain_list";
    ArrayList<Fountain> coordinate = new ArrayList<Fountain>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //icon actionbar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.drawable.bar);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.maps));

            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Can't find style. Error: ", e);
        }


        mMap = map;


        // Add a marker in Rome and move the camera
       /* LatLng rome1 = new LatLng(41.890251, 12.492373);
        LatLng rome2 = new LatLng(41.8747637, 12.5229818);
        LatLng rome3 = new LatLng(41.8679576, 12.5354582);
        LatLng rome4 = new LatLng(41.867, 12.53545);
        LatLng rome5 = new LatLng(41.1, 44.1);
        coordinate.add(rome1);
        coordinate.add(rome2);
        coordinate.add(rome3);
        coordinate.add(rome4);
        coordinate.add(rome5);*/


        // Creates the Volley request queue
        requestQueue = Volley.newRequestQueue(this);


        // Creating the JsonArrayRequest class called arrayreq, passing the required parameters
        //JsonURL is the URL to be fetched from
        JsonArrayRequest arrayreq = new JsonArrayRequest(JsonURL1,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("JSON","ARR:"+response);

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String latADD = jsonObject.getString("lat");
                                String longADD = jsonObject.getString("lng");
                                String id = jsonObject.getString("id");
                                data2 += latADD +"-" + longADD +"-"+id+ ":";
                            }
                            String[] parts1 = data2.split(":");
                            for (int i=0;i< parts1.length;i++){
                                String[] parts2 = parts1[i].split("-");
                                lat5 = Double.parseDouble(parts2[0]);
                                long5 = Double.parseDouble(parts2[1]);
                                id5 = parts2[2];
                                Log.d("titlenotgenerated",parts2[2]);
                                coord5 = new Fountain(lat5,long5,id5);
                                coordinate.add(coord5);
                            }
                            putMarkers();
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
    }

    //put Markers on the map
    private void putMarkers(){
        float rating = 0;
        ArrayList<PlaceInfo> info = new ArrayList<PlaceInfo>();
        Iterator iter = coordinate.iterator();
        while (iter.hasNext()) {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());
            Fountain  fountain2 = (Fountain) iter.next();
            LatLng latLng2=new LatLng(fountain2.getLat(),fountain2.getLng());
            String id= fountain2.getId();
            try {
                addresses = geocoder.getFromLocation(latLng2.latitude, latLng2.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                Log.d("getFountainID",fountain2.getId());

                info.add(new PlaceInfo(city, address, country, postalCode, latLng2, rating, id));
            }catch(Exception e){
                Log.d("getInfofailed","errore");
                info.add(new PlaceInfo("unknown","unknown","","",latLng2,rating,id));
            }

        }
        moveCamera(coordinate, 14, info);


        //Gps button
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();


        //marker-OnClick
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("IMEERE","CLICKED");
                marker.showInfoWindow();
                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker arg0) {
                // call an activity(xml file)
                String lat1 = Double.toString(arg0.getPosition().latitude);
                String long1 = Double.toString(arg0.getPosition().longitude);
                String position2 = lat1 + "," + long1;
                Intent intent = new Intent(MapsActivity.this, MarkerActivity.class);
                intent.putExtra("activity", position2); //bring info regardind marker, you can add more of putextra calling in different name
                String pass=arg0.getTitle();
                String pass2 = pass.replace("Fontanella: ","");
                intent.putExtra("id",pass2);
                Log.d("fountainIDActivity",pass2);
                startActivity(intent);
            }

        });
    }




    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        mMap.moveCamera(CameraUpdateFactory.zoomBy(2));  //zoom In when I click myLocation Button

        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }


    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    private void moveCamera(ArrayList<Fountain> coordinate, float zoom, ArrayList<PlaceInfo> placeInfo) {
        //Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            if (!mMap.isMyLocationEnabled())
                mMap.setMyLocationEnabled(true);

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (myLocation == null) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                String provider = lm.getBestProvider(criteria, true);
                myLocation = lm.getLastKnownLocation(provider);
            }

            if (myLocation != null) {
                LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, zoom), 1500, null);
            }
        }
        int i = 0;
        mMap.clear();
        Iterator iter = coordinate.iterator();
        while (iter.hasNext()) {
            Fountain font = (Fountain) iter.next();
           LatLng tmp =  new LatLng(font.getLat(),font.getLng());
            Log.d("LOGTAG","iternext:"+tmp);
            if (placeInfo != null) {
                Log.d("PLACEINFO","!=NULL");
                try {

                    String snippet = "Address: " + placeInfo.get(i).getAddress() + "\n" +
                            "Phone Number: " + placeInfo.get(i).getPhoneNumber() + "\n" +
                            "Price Rating: " + placeInfo.get(i).getRating() + "\n";
                    Log.d("getAttributions",placeInfo.get(i).getAttributions());
                    MarkerOptions options = new MarkerOptions()
                            .position(tmp)
                            .title("Fontanella: "+font.getId())
                            .snippet(snippet);
                    //options.title(placeInfo.get(i).getAttributions());
                    mMarker = mMap.addMarker(options);

                } catch (NullPointerException e) {
                    Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage());
                }
            } else {
                   Log.d("PLACEINFO","!NULL");
                mMap.addMarker(new MarkerOptions().position(tmp).title("Fontanella"));
            }
            i++;
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("IMHERE","CLICKED");
                marker.showInfoWindow();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.add_button:
                //launching new activity/screen
                OpenAct();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void OpenAct() {
        String latlongextra = "";
        double lat3=9999;
        double long3=9999;

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            if (!mMap.isMyLocationEnabled())
                mMap.setMyLocationEnabled(true);

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (myLocation == null) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                String provider = lm.getBestProvider(criteria, true);
                myLocation = lm.getLastKnownLocation(provider);
            }

            if (myLocation != null) {
                latlongextra = Double.toString(myLocation.getLatitude()) +","+ Double.toString(myLocation.getLongitude());
                lat3=myLocation.getLatitude();
                long3=myLocation.getLongitude();
            }
        }
        List<Address> addresses = null;
        String address="Could't detect your position";
        if(lat3!=9999 && long3!=9999) {

            Geocoder geocoder;

            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(lat3, long3, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        }
        Intent intent2 = new Intent(MapsActivity.this, Pop_add.class);
        intent2.putExtra("myposition", latlongextra);             //bring info regardind marker, you can add more of putextra calling in different name
        intent2.putExtra("myaddress", address);
        startActivity(intent2);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
}
