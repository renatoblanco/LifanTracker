package uy.com.lifan.lifantracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.Date;

import uy.com.lifan.lifantracker.DB.DB;
import uy.com.lifan.lifantracker.DB.Querys;
import uy.com.lifan.lifantracker.Util.VINUtil;
import uy.com.lifan.lifantracker.barcodereader.BarcodeCaptureActivity;

public class ScanActivity extends AppCompatActivity implements LocationListener {


    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String LOG_TAG = "Scan VIN";

    //Geolocation referentes
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    boolean isGPSEnabled = false;
    boolean errorLocation = false;
    float accuracy = 0;
    LocationManager locationManager;
    Location actualLocation;
    LocatorListener locationlistener;

    //barcode references
    private String barcodeValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        setTitle(R.string.scanActivity_title);

        locationlistener = new LocatorListener();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);
        locationlistener.actualLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        ImageButton cameraButton = (ImageButton) findViewById(R.id.button);
        Button manualButton = (Button) findViewById(R.id.manual);

        manualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   Intent intent = new Intent(ScanActivity.this, MapsActivity.class);
                //   startActivity(intent);

            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ScanActivity.this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

                errorLocation = false;
                startActivityForResult(intent, RC_BARCODE_CAPTURE);

            }
        });

        ImageButton mapButton = (ImageButton) findViewById(R.id.btn_map);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScanActivity.this, MapsActivity.class);
                startActivity(intent);

            }
        });

        ImageButton searchButton = (ImageButton) findViewById(R.id.btn_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScanActivity.this, SearchActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    barcodeValue = barcode.displayValue;

                    View layout = (CoordinatorLayout) findViewById(R.id
                            .main_scan_layout);

                    VINUtil validateVIN = new VINUtil();
                    if ((barcodeValue.length() >= 17) && (validateVIN.isValid(barcodeValue.substring(0, 17)))) {

                        Location location = locationlistener.actualLocation;
                        Date d = new Date(location.getTime());
                        java.sql.Timestamp sq = new java.sql.Timestamp(d.getTime());

                        LatLng Pos = new LatLng(location.getLatitude(), location.getLongitude());
                        Log.d("Location", d.toString() + " " + location.getLatitude() + "" + +location.getLongitude());
                        try {

                            DB db = new DB();
                            String insert = String.format(Querys.INRT_LOCATION, barcodeValue.substring(0, 17), location.getLatitude(), location.getLongitude(), sq);
                            db.execute(insert);

                            Snackbar.make(layout, R.string.vehiculos_posicionado_ok, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            errorLocation = false;

                            Intent intent = new Intent(ScanActivity.this, RegisterActivity.class);
                            intent.putExtra(RegisterActivity.latitud, location.getLatitude());
                            intent.putExtra(RegisterActivity.VIN, barcodeValue.substring(0, 17));
                            startActivity(intent);

                        } catch (Exception ex) {

                        }
                    } else {
                        Snackbar.make(layout, "El VIN no es Valido, reintente", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).setActionTextColor(Color.RED).show();
                        errorLocation = false;

                    }

                } else {

                    Log.d(LOG_TAG, "No barcode captured, intent data is null");
                }
            } else {
                Log.d(LOG_TAG, "No barcode captured, intent data is null");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


    public void onLocationChanged(Location location) {
        Log.d("Location", "on location change Scan" + location.getTime() + " " + location.getLatitude() + "  " + +location.getLongitude());
        actualLocation = location;
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderDisabled(String provider) {
    }


    @Override
    protected void onStop() {
        super.onStop();
        //  locationManager.removeUpdates(locationlistener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);
    }
}
