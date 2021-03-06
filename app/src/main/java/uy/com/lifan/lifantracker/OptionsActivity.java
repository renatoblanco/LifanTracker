package uy.com.lifan.lifantracker;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by renat on 31/01/2016.
 */

public class OptionsActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_options);

        setTitle(R.string.options_title);

        ImageButton mapButton = (ImageButton) findViewById(R.id.btn_map);
        ImageView optionsButton = (ImageView) findViewById(R.id.btn_others);
        ImageButton searchButton = (ImageButton) findViewById(R.id.btn_search);

        ImageView statitics = (ImageView) findViewById(R.id.btn_statistics);

        statitics.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent().setClass(
                        OptionsActivity.this, StatiticsActivity.class);
                startActivity(intent);
            }
        });


        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionsActivity.this, MapsActivity.class);
                startActivity(intent);

            }
        });

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionsActivity.this, OptionsActivity.class);
                startActivity(intent);

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionsActivity.this, SearchActivity.class);
                startActivity(intent);

            }
        });

    }


}

