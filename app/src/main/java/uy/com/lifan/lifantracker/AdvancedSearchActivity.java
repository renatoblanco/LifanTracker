package uy.com.lifan.lifantracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import uy.com.lifan.lifantracker.DB.Querys;

public class AdvancedSearchActivity extends AppCompatActivity {


    private static final String LOG_TAG = "Advanced search";
    private EditText VIN;
    private EditText motor;
    private View mProgressView;
    private View searchView;
    private Spinner modelSpinner;
    private Spinner colorSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);
        this.setTitle(R.string.searchAdvancedActivity_title);

        mProgressView = findViewById(R.id.search_progress);
        searchView = findViewById(R.id.search_relative_layout);

        ImageButton scanButton = (ImageButton) findViewById(R.id.button);
        Button searchButton = (Button) findViewById(R.id.btn_find);
        VIN = (EditText) findViewById(R.id.VIN);
        motor = (EditText) findViewById(R.id.engine);
        modelSpinner = (Spinner) findViewById(R.id.spinner_models);
        colorSpinner = (Spinner) findViewById(R.id.spinner_colors);



        searchButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {


                                                Intent intent = new Intent(AdvancedSearchActivity.this, MapsActivity.class);
                                                intent.putExtra(MapsActivity.search, true);
                                                String qry = String.format(Querys.QRY_ADVANCED_SEARCH, VIN.getText(), motor.getText(), colorSpinner.getSelectedItem().toString(), modelSpinner.getSelectedItem().toString());
                                                intent.putExtra(MapsActivity.query, qry);
                                                startActivity(intent);

                                            }

                                        }
        );


        ImageButton mapButton = (ImageButton) findViewById(R.id.btn_map);

        mapButton.setOnClickListener(new View.OnClickListener()

                                     {
                                         @Override
                                         public void onClick(View view) {
                                             Intent intent = new Intent(AdvancedSearchActivity.this, MapsActivity.class);
                                             startActivity(intent);
                                         }
                                     }

        );

        ImageButton search_tool_Button = (ImageButton) findViewById(R.id.btn_search);

        search_tool_Button.setOnClickListener(new View.OnClickListener()

                                              {
                                                  @Override
                                                  public void onClick(View view) {
                                                      Intent intent = new Intent(AdvancedSearchActivity.this, AdvancedSearchActivity.class);
                                                      startActivity(intent);

                                                  }
                                              }

        );


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


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            searchView.setVisibility(show ? View.GONE : View.VISIBLE);
            searchView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    searchView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            searchView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}
