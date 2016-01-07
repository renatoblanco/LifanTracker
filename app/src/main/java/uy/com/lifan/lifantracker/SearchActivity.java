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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.barcode.Barcode;

import java.sql.ResultSet;

import uy.com.lifan.lifantracker.DB.DB;
import uy.com.lifan.lifantracker.DB.Querys;
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
        this.setTitle(R.string.searchActivity_title);


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


                    VINUtil validateVIN = new VINUtil();
                    if ((barcodeValue.length() >= 17) && (validateVIN.isValid(barcodeValue.substring(0, 17)))) {

                        this.VIN.setText(barcodeValue.substring(0, 17));
                        LatLng location_vin = locationVIN(barcodeValue.substring(0, 17));

                        //si location_vin ,longitud y latitud ==0 el auto no tiene pos GPS
                        //hay que buscarlo en el stock y si esta, entonces mostrarlo en ekl galpon. Calidad, PDI...etc

                        Intent intent = new Intent(SearchActivity.this, RegisterActivity.class);
                        intent.putExtra(RegisterActivity.latitud, location_vin.latitude);
                        intent.putExtra(RegisterActivity.longitud, location_vin.longitude);
                        intent.putExtra(RegisterActivity.VIN, barcodeValue.substring(0, 17));
                        startActivity(intent);


                    } else {

                        View layout = (CoordinatorLayout) findViewById(R.id
                                .search_coordinator_layout);
                        this.VIN.setText("");
                        Snackbar.make(layout, "El VIN no es Valido, reintente", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).setActionTextColor(Color.RED).show();
                    }
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

        }
        return location;
    }


}
