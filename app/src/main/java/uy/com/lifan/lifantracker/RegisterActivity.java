package uy.com.lifan.lifantracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegisterActivity extends FragmentActivity implements OnMapReadyCallback, View.OnTouchListener {

    public static final String latitud = "latitud";
    public static final String longitud = "longitud";
    public static final String timeout = "timeout";
    public static final String VIN = "VIN";
    // Set the duration of the splash screen
    private static final long SCREEN_DELAY = 2000;
    private GoogleMap mMap;
    Timer timer = new Timer();
    private boolean is_first = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(R.string.registerActivity_title);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        ImageView detailButton = (ImageView) findViewById(R.id.detail);


        ImageButton mapButton = (ImageButton) findViewById(R.id.btn_map);
        LinearLayout pantalla = (LinearLayout) findViewById(R.id.map_container);

        FrameLayout rootContariner = (FrameLayout) findViewById(R.id.root_container);

        rootContariner.setOnTouchListener(this);
        pantalla.setOnTouchListener(this);


        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MapsActivity.class);
                startActivity(intent);

            }
        });

        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this, DetailInfoActivity.class);
                intent.putExtra(RegisterActivity.VIN, getIntent().getStringExtra(VIN));
                startActivity(intent);

            }
        });

        ImageButton searchButton = (ImageButton) findViewById(R.id.btn_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, SearchActivity.class);
                startActivity(intent);

            }
        });

        ImageButton optionsButton = (ImageButton) findViewById(R.id.btn_others);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, OptionsActivity.class);
                startActivity(intent);

            }
        });


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

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                timer.cancel();

            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                timer.cancel();
            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (is_first)
                    is_first = false;
                else
                    timer.cancel();
            }
        });
        // Add a marker in Lifan and move the camera

        double latitud = getIntent().getDoubleExtra(this.latitud, -34.707616);
        double longitud = getIntent().getDoubleExtra(this.longitud, -56.503064);
        String VIN = getIntent().getStringExtra(this.VIN);

        Car esteAuto = datosAuto(VIN);

        LatLng actualPos = new LatLng(latitud, longitud);

        mMap.addMarker(new MarkerOptions().position(actualPos).title(VIN).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon3)).flat(true));


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

        TextView fechaprod = (TextView) findViewById(R.id.fechaprod);
        fechaprod.setText(esteAuto.fechaFab);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                Intent mainIntent = new Intent().setClass(
                        RegisterActivity.this, ScanActivity.class);
                startActivity(mainIntent);
                finish();
            }
        };

        if (getIntent().getBooleanExtra("timeout", true))
            timer.schedule(task, SCREEN_DELAY);

    }

    public Car datosAuto(String vin) {
        DB db = new DB();//base
        Car car = new Car();
        try {
            String comando = String.format(Querys.QRY_DATOS_VIN, vin, vin, vin);
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
            Toast toast = Toast.makeText(getApplicationContext(), R.string.exception_message, Toast.LENGTH_SHORT);
            toast.show();
        }

        return car;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        timer.cancel();
        return false;
    }
}
