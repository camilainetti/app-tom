package com.iothome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class AccessActivity extends MainActivity {

    private static final String TAG = "AccessActivity";

    private ListView listDevices;
    private ImageButton button_devices, button_confs, button_other;

    private static boolean busy = false;
    private static String last_action = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        //lista de devices
        listDevices = (ListView)findViewById(R.id.listDevices);

        //menu inferior
        button_devices = (ImageButton)findViewById(R.id.button_devices);
        button_devices.setOnClickListener(this);
        button_confs = (ImageButton)findViewById(R.id.button_confs);
        button_confs.setOnClickListener(this);
        button_other = (ImageButton)findViewById(R.id.button_other);
        button_other.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == button_confs.getId()) {
            Intent intentconf = new Intent(this, ConfigConn.class);
            startActivity(intentconf);
        }

        else if (view.getId() == button_devices.getId()) {
            Toast.makeText(AccessActivity.this,
                    "Devices sendo exibidos",
                    Toast.LENGTH_LONG).show();
        }
    }
}
