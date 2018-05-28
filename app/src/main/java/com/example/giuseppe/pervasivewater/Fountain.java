package com.example.giuseppe.pervasivewater;

import com.google.android.gms.maps.model.LatLng;

public class Fountain {
    public double lat;
    public double lng;
    public String id;
    public Fountain(double lat,double lng, String id){
        this.lat = lat;
        this.lng= lng;
        this.id = id;
    }

    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }

    public String getId() {
        return id;
    }
}
