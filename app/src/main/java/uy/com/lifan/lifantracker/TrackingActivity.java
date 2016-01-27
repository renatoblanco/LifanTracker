package uy.com.lifan.lifantracker;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.ResultSet;
import java.util.ArrayList;

import uy.com.lifan.lifantracker.DB.DB;
import uy.com.lifan.lifantracker.DB.Querys;


public class TrackingActivity extends FragmentActivity implements LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public static final String param_VIN = "param_VIN";
    private String VIN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);


        VIN = getIntent().getStringExtra(param_VIN);

        setUpMapIfNeeded();

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                LatLng location_vin = locationVIN(VIN);

                Intent intent = new Intent(TrackingActivity.this, RegisterActivity.class);
                intent.putExtra(RegisterActivity.latitud, location_vin.latitude);
                intent.putExtra(RegisterActivity.timeout, false);
                intent.putExtra(RegisterActivity.longitud, location_vin.longitude);
                intent.putExtra(RegisterActivity.VIN, VIN);
                startActivity(intent);

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        LatLng locatorLatLong = new LatLng(-34.707616, -56.503064);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(locatorLatLong)      // Sets the center of the map to Lifan
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder


        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locatorLatLong, 17));

        Integer countCars = 0;

        DB db = new DB();

        ArrayList<Marker> markers = new ArrayList<Marker>();
        try {
            ResultSet resultSet;

            String comando = String.format(Querys.QRY_TRACKING_VIN, this.VIN);
            resultSet = db.select(comando);


            if (resultSet != null) {

                while (resultSet.next()) {
                    countCars++;
                    locatorLatLong = new LatLng(resultSet.getFloat("latitud"), resultSet.getFloat("longitud"));

                    Marker mark = mMap.addMarker(new MarkerOptions()
                            .position(locatorLatLong).flat(true).rotation(210)
                            .title(countCars.toString())
                            .snippet(resultSet.getString("created").substring(0, 19)).position(locatorLatLong).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon3)));

                    mark.showInfoWindow();
                    markers.add(mark);

                    //  marker.setRotation(210);


                }
            }
        } catch (Exception ex) {

        }


        Toast toast = Toast.makeText(getApplicationContext(), countCars + " posiciónes encontrada", Toast.LENGTH_LONG);
        toast.show();

    }


    public void onLocationChanged(Location location) {
        // Se ha encontrado una nueva localización

    }


    private LatLng locationVIN(String VIN) {
        DB db = new DB();
        LatLng location = new LatLng(0, 0);
        try {

            String comando = String.format(Querys.QRY_LOCATIONS_VIN, VIN);
            ResultSet resultSet = db.select(comando);


            if (resultSet != null) {
                while (resultSet.next()) {
                    location = new LatLng(resultSet.getFloat("latitud"), resultSet.getFloat("longitud"));

                }
            }
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.exception_message, Toast.LENGTH_SHORT);
            toast.show();
        }
        return location;
    }

}
