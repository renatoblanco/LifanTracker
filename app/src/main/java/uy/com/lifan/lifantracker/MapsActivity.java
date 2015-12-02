package uy.com.lifan.lifantracker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import java.util.Date;

import uy.com.lifan.lifantracker.DB.DB;
import uy.com.lifan.lifantracker.DB.Querys;


public class MapsActivity extends FragmentActivity implements LocationListener {

    float[] valuesAcelerometro = new float[3];
    float[] valuesBrujula = new float[3];
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();


        Button Btnposicionar = (Button) findViewById(R.id.Btnposicionar);

        Btnposicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                Criteria criteria = new Criteria();
                //criteria.setAccuracy(Criteria.ACCURACY_FINE);
                String bestProvider = locationManager.getBestProvider(criteria, false);
                Location location = locationManager.getLastKnownLocation(bestProvider);
                Long time = location.getTime();
                Date d = new Date(time);

                Log.d("", time.toString());

                Log.d("Location", "MAPS" + location.getLatitude() + "" + +location.getLongitude());

                if (location == null)
                    Snackbar.make(v, "location null", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else
                    Snackbar.make(v, d.toString(), Snackbar.LENGTH_INDEFINITE)
                            .setAction("Action", null).show();


                LatLng Pos = new LatLng(location.getLatitude(), location.getLongitude());
                // Double rotacion = GetOrientacion();

                try {

                    DB db = new DB();
                    String insert = String.format(Querys.INRT_LOCATION, "9UK64ED78C77238", location.getLatitude(), location.getLongitude());
                    db.execute(insert);

                } catch (Exception ex) {

                }


                setUpMap();

                Snackbar.make(v, "Vehículo posicionado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            /*    Marker addedMarker = mMap.addMarker(new MarkerOptions()
                        .position(Pos)
                        .title("custom-Lifan Motors Uruguay")
                        .snippet("Pos Actual").position(Pos).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon3)));
                addedMarker.setRotation(210);

                mMap.addMarker(new MarkerOptions()
                        .position(addedMarker.getPosition()));
            */


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
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);

        LatLng LIFAN = new LatLng(-34.707616, -56.503064);
        Marker lifan1;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(LIFAN)      // Sets the center of the map to Lifan
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder


        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LIFAN, 17));


        DB db = new DB();
        try {
            ResultSet resultSet = db.select(Querys.QRY_LOCATIONS);
            if (resultSet != null) {
                while (resultSet.next()) {
                    LIFAN = new LatLng(resultSet.getFloat("latitud"), resultSet.getFloat("longitud"));
                    lifan1 = mMap.addMarker(new MarkerOptions()
                            .position(LIFAN)
                            .title("1-Lifan Motors Uruguay")
                            .snippet(LIFAN.latitude + "" + LIFAN.longitude).position(LIFAN).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon3)));
                    lifan1.setRotation(210);
                    mMap.addMarker(new MarkerOptions()
                            .position(lifan1.getPosition()));

                }
            }
        } catch (Exception ex) {

        }


    }


    public void onLocationChanged(Location location) {
        // Se ha encontrado una nueva localización

    }


    public Double GetOrientacion() {

        String servicio = Context.SENSOR_SERVICE;
        SensorManager sensorManager =
                (SensorManager) getSystemService(servicio);

        Sensor sensorAcelerometro = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Sensor sensorOrientation = sensorManager
                .getDefaultSensor(Sensor.TYPE_ORIENTATION);


        float[] values = new float[3];
        float[] R = new float[9];

        ListenerSensor listenerAceleracion = new ListenerSensor();

        sensorManager.registerListener(listenerAceleracion,
                sensorAcelerometro, SensorManager.SENSOR_DELAY_NORMAL);

        ListenerSensor listenerOrientacion = new ListenerSensor();

        sensorManager.registerListener(listenerOrientacion,
                sensorOrientation, SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.getRotationMatrix(R, null,
                valuesAcelerometro, valuesBrujula);
        sensorManager.getOrientation(R, values);


        return Math.toDegrees(values[0]);
    }

    class ListenerSensor implements SensorEventListener {

        public void onSensorChanged(SensorEvent sensorEvent) {
            if (valuesAcelerometro[0] < 1)
                valuesAcelerometro = sensorEvent.values;
            else if (valuesBrujula[0] < 1)
                valuesBrujula = sensorEvent.values;

        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // La precisión del sensor ha cambiado
        }
    }
}
