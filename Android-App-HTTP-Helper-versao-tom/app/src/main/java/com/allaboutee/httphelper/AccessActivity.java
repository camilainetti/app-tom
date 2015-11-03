package com.allaboutee.httphelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AccessActivity extends ListESP {

    private TextView nome_escolhido;
    private Button button_ON,button_OFF;
    String nome;

    private static final String TAG = "AccessActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        nome = intent.getStringExtra(ListESP.EXTRA_MESSAGE);
        String nome_ssid = sharedPreferences.getString(nome+"getSSID", "");
        String ssid_home = sharedPreferences.getString(nome_ssid+"getHomessid", "");
        Log.v(TAG, "home_ssid:" + ssid_home + "::");
        //ConectarESP.conectar(getApplicationContext(), ssid_home);
        setContentView(R.layout.activity_access);
        nome_escolhido= (TextView)findViewById(R.id.nome_escolhido);
        nome_escolhido.setText(nome);
        button_ON = (Button)findViewById(R.id.button_ON);
        button_OFF = (Button)findViewById(R.id.button_OFF);
        button_ON.setOnClickListener(this);
        button_OFF.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        String parameterValue;
        if (view.getId() == button_ON.getId()) {
            parameterValue = "on";
        } else {
            parameterValue = "off";
        }
        String ipAddress = sharedPreferences.getString(nome, "");
        String portNumber = "80";
        Log.v(TAG, "ip server:"+ipAddress+"nome:"+nome+"::");

        // execute HTTP request
        new HttpRequestAsyncTask(
                view.getContext(), "=" + parameterValue, ipAddress, ":" + portNumber, "/?pin"
        ).execute();
    }
}

