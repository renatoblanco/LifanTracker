package uy.com.lifan.lifantracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;

import uy.com.lifan.lifantracker.DB.DB;
import uy.com.lifan.lifantracker.DB.Querys;
import uy.com.lifan.lifantracker.Util.Util;

public class DetailInfoActivity extends FragmentActivity {

    public static final String VIN = "VIN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        setTitle(R.string.DetailInfoActivity_title);


        ImageButton mapButton = (ImageButton) findViewById(R.id.btn_map);
        LinearLayout pantalla = (LinearLayout) findViewById(R.id.map_container);

        ImageView tracking = (ImageView) findViewById(R.id.tracking);

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

        tracking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent().setClass(
                        DetailInfoActivity.this, TrackingActivity.class);
                intent.putExtra("param_VIN", getIntent().getStringExtra(VIN));
                startActivity(intent);
            }
        });

        String VIN = getIntent().getStringExtra(this.VIN);

        Car esteAuto = datosAuto(VIN);

        TextView txtVIN = (TextView) findViewById(R.id.VIN);
        txtVIN.setText(VIN);

        TextView engine = (TextView) findViewById(R.id.engine);
        engine.setText(esteAuto.engine);

        TextView color = (TextView) findViewById(R.id.color);
        color.setText(esteAuto.color);

        TextView proceso = (TextView) findViewById(R.id.proceso);
        proceso.setText(esteAuto.proceso);


        TextView fechaprod = (TextView) findViewById(R.id.fechaprod);
        fechaprod.setText(Util.getAnioVIN(VIN));

        TextView lote = (TextView) findViewById(R.id.txt_lote);
        lote.setText(esteAuto.lote);

        TextView key = (TextView) findViewById(R.id.txt_key);
        key.setText(esteAuto.lifan_keycode);

        TextView factura = (TextView) findViewById(R.id.txt_factura);
        factura.setText(esteAuto.invoice);

        TextView txt_numeroprod = (TextView) findViewById(R.id.txrt_nroprod);
        txt_numeroprod.setText(esteAuto.numeroProd.toString());


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
                    car.lote = resultSet.getString("z_shipment");
                    car.lifan_keycode = resultSet.getString("lifan_keycode");
                    car.invoice = resultSet.getString("invoice");

                }

            }
        } catch (Exception ex) {

            Toast toast = Toast.makeText(getApplicationContext(), R.string.exception_message, Toast.LENGTH_SHORT);
            toast.show();
        }

        return car;
    }


}
