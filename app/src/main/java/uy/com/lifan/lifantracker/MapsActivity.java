package uy.com.lifan.lifantracker;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

import uy.com.lifan.lifantracker.DB.DB;
import uy.com.lifan.lifantracker.DB.Querys;


public class MapsActivity extends FragmentActivity implements LocationListener {

    float[] valuesAcelerometro = new float[3];
    float[] valuesBrujula = new float[3];
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public final static String search = "search";
    public final static String query = "query";
    private boolean is_search;
    private String consulta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        is_search = getIntent().getBooleanExtra(search, false);
        consulta = getIntent().getStringExtra(query);

        setUpMapIfNeeded();

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                LatLng location_vin = locationVIN(marker.getTitle());

                Intent intent = new Intent(MapsActivity.this, RegisterActivity.class);
                intent.putExtra(RegisterActivity.latitud, location_vin.latitude);
                intent.putExtra(RegisterActivity.timeout, false);
                intent.putExtra(RegisterActivity.longitud, location_vin.longitude);
                intent.putExtra(RegisterActivity.VIN, marker.getTitle());
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
        Marker marker;
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
        try {
            ResultSet resultSet;

            if (is_search)
                resultSet = db.select(this.consulta);
            else
                resultSet = db.select(Querys.QRY_LOCATIONS);


            if (resultSet != null) {
                while (resultSet.next()) {
                    locatorLatLong = new LatLng(resultSet.getFloat("latitud"), resultSet.getFloat("longitud"));
                    String color = resultSet.getString("color");

                    int colorNro = R.drawable.car_icon_grey;

                    if (color.compareTo("Blue") == 0)
                        colorNro = R.drawable.car_icon_blue;
                    else if (color.contains("White"))
                        colorNro = R.drawable.car_icon_white;
                    else if (color.compareTo("Red") == 0)
                        colorNro = R.drawable.car_icon_red;
                    else if (color.compareTo("Green") == 0)
                        colorNro = R.drawable.car_icon_green;
                    else if (color.contains("Yellow"))
                        colorNro = R.drawable.car_icon_yellow;
                    else if (color.contains("Black"))
                        colorNro = R.drawable.car_icon_black;
                    else if (color.contains("Silver"))
                        colorNro = R.drawable.car_icon_silver;
                    else if (color.contains("Purple"))
                        colorNro = R.drawable.car_icon_purple;



                    mMap.addMarker(new MarkerOptions()
                            .position(locatorLatLong).flat(true).rotation(210)
                            .title(resultSet.getString("vin"))
                            .snippet(getString(R.string.more_options_marker_message)).position(locatorLatLong).flat(true).icon(BitmapDescriptorFactory.fromResource(colorNro))).setFlat(true);


                    //  marker.setRotation(210);
                    countCars++;
                }

            }
        } catch (Exception ex) {

        }
        Toast toast = Toast.makeText(getApplicationContext(), countCars + " autos encontrados", Toast.LENGTH_LONG);
        toast.show();


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
