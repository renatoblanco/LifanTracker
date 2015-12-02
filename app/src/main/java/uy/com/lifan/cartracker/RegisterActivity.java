package uy.com.lifan.cartracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

import uy.com.lifan.lifantracker.R;
import uy.com.lifan.lifantracker.ScanActivity;

public class RegisterActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static final String latitud = "latitud";
    public static final String longitud = "longitud";
    public static final String VIN = "VIN";

    // Set the duration of the splash screen
    private static final long SCREEN_DELAY = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Add a marker in Lifan and move the camera

        double latitud = getIntent().getDoubleExtra(this.latitud, -34.707616);
        double longitud = getIntent().getDoubleExtra(this.longitud, -56.503064);
        String VIN = getIntent().getStringExtra(this.VIN);

        LatLng lifan = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(lifan).title(VIN).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon3)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lifan));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(lifan)      // Sets the center of the map to Lifan
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder


        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lifan, 17));

        TextView txtVIN = (TextView) findViewById(R.id.VIN);
        txtVIN.setText(VIN);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Start the next activity
                Intent mainIntent = new Intent().setClass(
                        RegisterActivity.this, ScanActivity.class);
                startActivity(mainIntent);
                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };


        Timer timer = new Timer();
        timer.schedule(task, SCREEN_DELAY);


    }
}
