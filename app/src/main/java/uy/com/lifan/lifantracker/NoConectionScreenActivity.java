package uy.com.lifan.lifantracker;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by renat on 04/01/2016.
 */

public class NoConectionScreenActivity extends Activity {

    public static final String error_network = "error_network";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.no_conectioin_screen);

        ImageView image = (ImageView) findViewById(R.id.error_image);

        String network_error = getIntent().getStringExtra(error_network);

        if (network_error.compareTo("LTE") == 0)

            image.setImageIcon(R.drawable.cast_ic_notification_0);

        Button btnRetry = (Button) findViewById(R.id.retry);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent().setClass(
                        NoConectionScreenActivity.this, SplashScreenActivity.class);
                startActivity(mainIntent);
            }
        });


    }

}

