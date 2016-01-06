package uy.com.lifan.lifantracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Window;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by renat on 31/10/2015.
 */

public class SplashScreenActivity extends Activity {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 3000;
    private String error_network = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash_screen);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                if (is_conected()) {
                    Intent mainIntent = new Intent().setClass(
                            SplashScreenActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                } else {
                    Intent mainIntent = new Intent().setClass(
                            SplashScreenActivity.this, NoConectionScreenActivity.class);

                    mainIntent.putExtra(NoConectionScreenActivity.error_network, error_network);

                    startActivity(mainIntent);
                }

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);


    }


    public boolean is_conected() {

        boolean conected = false;

        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {

            URL url = null;
            try {
                url = new URL("http://192.168.2.2:8080/admin");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                conected = false;
                error_network = "VPN";
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection)
                        url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                error_network = "VPN";
            }
            connection.setRequestProperty("User-Agent", "yourAgent");
            connection.setRequestProperty("Connection", "close");
            connection.setConnectTimeout(1000);

            try {
                connection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (connection.getResponseCode() == 200) {
                    conected = true;
                } else {
                    conected = false;
                    error_network = "VPN";
                }
            } catch (IOException e) {
                e.printStackTrace();
                conected = false;
                error_network = "VPN";
            }


        } else {

            conected = false;
            error_network = "LTE";

        }
        return conected;

    }

}

