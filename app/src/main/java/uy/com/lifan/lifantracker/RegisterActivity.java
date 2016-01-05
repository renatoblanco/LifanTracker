package uy.com.lifan.lifantracker;

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

import java.sql.ResultSet;
import java.util.Timer;
import java.util.TimerTask;

import uy.com.lifan.lifantracker.DB.DB;
import uy.com.lifan.lifantracker.DB.Querys;

public class RegisterActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String latitud = "latitud";
    public static final String longitud = "longitud";
    public static final String VIN = "VIN";
    // Set the duration of the splash screen
    private static final long SCREEN_DELAY = 5000;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(R.string.registerActivity_title);
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

        Car esteAuto = datosAuto(VIN);

        LatLng actualPos = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(actualPos).title(VIN).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon3)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(actualPos));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(actualPos)      // Sets the center of the map to Lifan
                .zoom(20)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder


        mMap.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(actualPos, 17));

        TextView txtVIN = (TextView) findViewById(R.id.VIN);
        txtVIN.setText(VIN);

        TextView engine = (TextView) findViewById(R.id.engine);
        engine.setText(esteAuto.engine);

        TextView modelo = (TextView) findViewById(R.id.modelo);
        modelo.setText(esteAuto.modelo);

        TextView color = (TextView) findViewById(R.id.color);
        color.setText(esteAuto.color);

        TextView proceso = (TextView) findViewById(R.id.proceso);
        proceso.setText(esteAuto.proceso);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                Intent mainIntent = new Intent().setClass(
                        RegisterActivity.this, ScanActivity.class);
                startActivity(mainIntent);
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, SCREEN_DELAY);


    }

    public Car datosAuto(String vin) {
        DB db = new DB();//base
        Car car = new Car();
        try {
            String comando = String.format(Querys.QRY_DATOS_VIN, vin, vin);
            ResultSet resultSet = db.select(comando);

            if (resultSet != null) {
                while (resultSet.next()) {

                    car.color = resultSet.getString("color");
                    car.numeroProd = resultSet.getInt("line");
                    car.fechaFab = resultSet.getString("z_enddate");
                    car.modelo = resultSet.getString("modelo");
                    car.engine = resultSet.getString("z_engine");
                    car.proceso = resultSet.getString("proceso");
                }

            }
        } catch (Exception ex) {
            //aca me falta capturar la excpcion
        }

        return car;
    }


}
