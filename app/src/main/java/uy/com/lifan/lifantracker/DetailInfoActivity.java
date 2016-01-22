package uy.com.lifan.lifantracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.sql.ResultSet;

import uy.com.lifan.lifantracker.DB.DB;
import uy.com.lifan.lifantracker.DB.Querys;

public class DetailInfoActivity extends FragmentActivity {

    public static final String VIN = "VIN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        setTitle(R.string.DetailInfoActivity_title);


        ImageButton mapButton = (ImageButton) findViewById(R.id.btn_map);
        LinearLayout pantalla = (LinearLayout) findViewById(R.id.map_container);

        FrameLayout rootContariner = (FrameLayout) findViewById(R.id.root_container);


        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailInfoActivity.this, MapsActivity.class);
                startActivity(intent);

            }
        });

        ImageButton searchButton = (ImageButton) findViewById(R.id.btn_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailInfoActivity.this, SearchActivity.class);
                startActivity(intent);

            }
        });


    }


    public Car datosAuto(String vin) {
        DB db = new DB();//base
        Car car = new Car();
        try {
            String comando = String.format(Querys.QRY_DATOS_VIN, vin, vin, vin);
            ResultSet resultSet = db.select(comando);

            if (resultSet != null) {
                while (resultSet.next()) {

                    car.color = resultSet.getString("color");
                    car.numeroProd = resultSet.getInt("line");
                    car.fechaFab = resultSet.getString("z_enddate");
                    car.modelo = resultSet.getString("modelo");
                    car.engine = resultSet.getString("z_engine");
                    car.proceso = resultSet.getString("proceso");
                }

            }
        } catch (Exception ex) {
            //aca me falta capturar la excpcion
        }

        return car;
    }


}
