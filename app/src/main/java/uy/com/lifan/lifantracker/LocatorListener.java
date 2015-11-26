package uy.com.lifan.lifantracker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by renat on 25/11/2015
 * this class allows to suscribe as listener to the GPS provideor to get the most current location
 */

public class LocatorListener implements LocationListener {
    Location actualLocation;

    public void onLocationChanged(Location location) {

        actualLocation = location;
    }

    public void onProviderDisabled(String provider) {
        //lblEstado.setText("Provider OFF");
    }

    public void onProviderEnabled(String provider) {
        //lblEstado.setText("Provider ON");
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        //lblEstado.setText("Provider Status: " + status);
    }

}
