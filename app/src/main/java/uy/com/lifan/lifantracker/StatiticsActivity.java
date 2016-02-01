package uy.com.lifan.lifantracker;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;

import uy.com.lifan.lifantracker.DB.DB;
import uy.com.lifan.lifantracker.DB.Querys;

/**
 * Created by renat on 31/01/2016.
 */

public class StatiticsActivity extends Activity {
    public Integer stock = 0;
    public Integer stock_gps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_statitics);

        setTitle(R.string.options_title);

        ImageButton mapButton = (ImageButton) findViewById(R.id.btn_map);
        ImageView optionsButton = (ImageView) findViewById(R.id.btn_others);
        ImageButton searchButton = (ImageButton) findViewById(R.id.btn_search);


        TextView cant_stock = (TextView) findViewById(R.id.textView5);
        TextView cant_gps = (TextView) findViewById(R.id.textView6);

        TextView porcentaje = (TextView) findViewById(R.id.txt_porcentaje);

        statitics();


        cant_stock.setText(this.stock.toString());
        cant_gps.setText(this.stock_gps.toString());

        porcentaje.setText((this.stock_gps / this.stock) * 100 + "%");

        ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mProgress.setVisibility(View.VISIBLE);
        mProgress.setProgress((this.stock_gps / this.stock) * 100);


        ImageView statitics = (ImageView) findViewById(R.id.btn_statistics);

        statitics.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent().setClass(
                        StatiticsActivity.this, StatiticsActivity.class);
                startActivity(intent);
            }
        });


        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatiticsActivity.this, MapsActivity.class);
                startActivity(intent);

            }
        });

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatiticsActivity.this, StatiticsActivity.class);
                startActivity(intent);

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatiticsActivity.this, SearchActivity.class);
                startActivity(intent);

            }
        });

    }


    public void statitics() {
        DB db = new DB();//base
        Car car = new Car();
        try {
            ResultSet resultSet = db.select(Querys.QRY_INVENTORY_STOCK);

            if (resultSet != null) {
                while (resultSet.next()) {

                    this.stock = resultSet.getInt(1);
                }

            }
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.exception_message, Toast.LENGTH_SHORT);
            toast.show();
        }

        try {
            ResultSet resultSet = db.select(Querys.QRY_LOCATIONS_COUNT);

            if (resultSet != null) {
                while (resultSet.next()) {

                    this.stock_gps = resultSet.getInt(1);
                }

            }
        } catch (Exception ex) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.exception_message, Toast.LENGTH_SHORT);
            toast.show();
        }


    }


}

