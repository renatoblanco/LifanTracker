package uy.com.lifan.lifantracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import uy.com.lifan.lifantracker.DB.Querys;

public class AdvancedSearchActivity extends AppCompatActivity {


    private static final String LOG_TAG = "Advanced search";
    private EditText VIN;
    private EditText motor;
    private EditText lote;
    private EditText proceso;
    private View mProgressView;
    private View searchView;
    private Spinner modelSpinner;
    private Spinner colorSpinner;

    private EditText fromDateEtxt;
    private EditText toDateEtxt;

    Calendar newCalendar = Calendar.getInstance();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    private DatePickerDialog fromDatePickerDialog;

    private DatePickerDialog toDatePickerDialog;


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
        lote = (EditText) findViewById(R.id.lote_text);
        proceso = (EditText) findViewById(R.id.procesoText);
        modelSpinner = (Spinner) findViewById(R.id.spinner_models);
        colorSpinner = (Spinner) findViewById(R.id.spinner_colors);

        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
        toDateEtxt = (EditText) findViewById(R.id.etxt_todate);

        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        fromDateEtxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showFrom();
                fromDatePickerDialog.show();
            }
        });

        toDateEtxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showTo();
                toDatePickerDialog.show();

            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String finalQuery = Querys.QRY_ADVANCED_SEARCH;
                                                if (VIN.getText().length() > 0)
                                                    finalQuery = finalQuery.concat(String.format(Querys.QRY_ADVANCED_SEARCH_VIN, VIN.getText()));
                                                if (lote.getText().length() > 0)
                                                    finalQuery = finalQuery.concat(String.format(Querys.QRY_ADVANCED_SEARCH_LOTE, lote.getText()));
                                                if (proceso.getText().length() > 0)
                                                    finalQuery = finalQuery.concat(String.format(Querys.QRY_ADVANCED_SEARCH_PROCESO, proceso.getText()));
                                                if (motor.getText().length() > 0)
                                                    finalQuery = finalQuery.concat(String.format(Querys.QRY_ADVANCED_SEARCH_MOTOR, motor.getText()));
                                                if (colorSpinner.getSelectedItemId() > 0)
                                                    finalQuery = finalQuery.concat(String.format(Querys.QRY_ADVANCED_SEARCH_COLOR, colorSpinner.getSelectedItem().toString()));
                                                if (modelSpinner.getSelectedItemId() > 0)
                                                    finalQuery = finalQuery.concat(String.format(Querys.QRY_ADVANCED_SEARCH_MODELO, modelSpinner.getSelectedItem().toString()));
                                                if (fromDateEtxt.getText().toString().compareTo("") != 0)
                                                    finalQuery = finalQuery.concat(String.format(Querys.QRY_ADVANCED_SEARCH_DESDE, fromDateEtxt.getText().toString()));
                                                if (toDateEtxt.getText().toString().compareTo("") != 0)
                                                    finalQuery = finalQuery.concat(String.format(Querys.QRY_ADVANCED_SEARCH_HASTA, toDateEtxt.getText().toString()));


                                                Intent intent = new Intent(AdvancedSearchActivity.this, MapsActivity.class);
                                                intent.putExtra(MapsActivity.search, true);
                                                intent.putExtra(MapsActivity.query, finalQuery);
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

    private void showFrom() {

        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }


    private void showTo() {

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }
}
