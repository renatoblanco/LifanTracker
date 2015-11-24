package uy.com.lifan.lifantracker;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.barcode.Barcode;

import uy.com.lifan.lifantracker.DB.DB;
import uy.com.lifan.lifantracker.DB.Querys;
import uy.com.lifan.lifantracker.barcodereader.BarcodeCaptureActivity;

public class ScanActivity extends AppCompatActivity implements LocationListener {

    //barcode references
    private String barcodeValue;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String LOG_TAG = "Scan VIN";
    boolean isGPSEnabled = false;
    boolean errorLocation = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        ImageButton cameraButton = (ImageButton) findViewById(R.id.button);


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ScanActivity.this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            /*    if(errorLocation)
                        Snackbar.make(view, "No se puede obtener la ubicacion del GPS", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                errorLocation=false;
*/
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

                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    String bestProvider = locationManager.getBestProvider(criteria, false);
                    Location location = locationManager.getLastKnownLocation(bestProvider);


                    LatLng Pos = new LatLng(location.getLatitude(), location.getLongitude());

                    try {

                        DB db = new DB();
                        String insert = String.format(Querys.INRT_LOCATION, barcodeValue, location.getLatitude(), location.getLongitude());
                        db.execute(insert);


                    } catch (Exception ex) {

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
        // Se ha encontrado una nueva localización

    }
}
