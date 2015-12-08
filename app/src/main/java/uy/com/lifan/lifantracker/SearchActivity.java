package uy.com.lifan.lifantracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import uy.com.lifan.lifantracker.Util.VINUtil;
import uy.com.lifan.lifantracker.barcodereader.BarcodeCaptureActivity;

public class SearchActivity extends AppCompatActivity {


    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String LOG_TAG = "Scan VIN";
    EditText VIN;


    //barcode references
    private String barcodeValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        ImageButton scanButton = (ImageButton) findViewById(R.id.button);
        Button searchButton = (Button) findViewById(R.id.btn_find);
        VIN = (EditText) findViewById(R.id.VIN);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   Intent intent = new Intent(ScanActivity.this, MapsActivity.class);
                //   startActivity(intent);

            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SearchActivity.this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);

            }
        });

        ImageButton mapButton = (ImageButton) findViewById(R.id.btn_map);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, MapsActivity.class);
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

                        this.VIN.setText(barcodeValue.substring(0, 17));
                    }
                } else {
                    View layout = (CoordinatorLayout) findViewById(R.id
                            .search_coordinator_layout);
                    Snackbar.make(layout, "El VIN no es Valido, reintente", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).setActionTextColor(Color.RED).show();
                }

            } else {
                Log.d(LOG_TAG, "No barcode captured, intent data is null");
            }

        } else

        {
            super.onActivityResult(requestCode, resultCode, data);
        }

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
