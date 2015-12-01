package uy.com.lifan.lifantracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by renat on 27/11/2015.
 */
public class ActivityToolbar extends AppCompatActivity {

    private Toolbar toolbar;
    //layout referentes
    public int activityLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        ImageButton searchButton = (ImageButton) findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent intent = new Intent(ActivityToolbar.this, MapsActivity.class);
                // startActivity(intent);

            }
        });

    }



}
