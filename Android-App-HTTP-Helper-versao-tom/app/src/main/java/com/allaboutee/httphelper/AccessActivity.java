package com.allaboutee.httphelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class AccessActivity extends ListESP {

    private TextView nome_escolhido;
    private Button button_ON,button_OFF;
    String portNumber = "80";

    private static final String TAG = "AccessActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String nome = intent.getStringExtra(ListESP.EXTRA_MESSAGE);
        String nome_ssid = sharedPreferences.getString(nome + "getSSID", "");
        String ssid_home = sharedPreferences.getString(nome_ssid + "getHomessid", "");
        Log.v(TAG, "home_ssid:" + ssid_home + "::");

        ConectarESP.conectar(getApplicationContext(), ssid_home);

        setContentView(R.layout.activity_access);

        button_ON = (Button)findViewById(R.id.button_ON);
        button_OFF = (Button)findViewById(R.id.button_OFF);
        button_ON.setOnClickListener(this);
        button_OFF.setOnClickListener(this);
        Toast.makeText(AccessActivity.this,
                "Conectando a sua rede! Aguarde...",
                Toast.LENGTH_LONG).show();
        String wifi_atual = getInfoWifi(2).replaceAll("\"", "");
        while(!wifi_atual.equals(ssid_home)) {
            wifi_atual = getInfoWifi(2).replaceAll("\"", "");
        }
        Toast.makeText(AccessActivity.this,
                "Conectado em: "+ssid_home,
                Toast.LENGTH_LONG).show();

        String ipAddress = sharedPreferences.getString(nome, "");

//        new HttpRequestAsyncTask(
//                getApplicationContext(), "=" + "estado", ipAddress, ":" + portNumber, "/?"
//        ).execute();

//        Toast.makeText(AccessActivity.this,
//                "Aguarde...",
//                Toast.LENGTH_LONG).show();
//        Log.v(TAG, "aqui");
//        while(Globals.getInstance().getData(0).equals("false")){
//        }
//        Log.v(TAG, "aqui2");
        String estado = "";//Globals.getInstance().getData(1);
        nome_escolhido= (TextView)findViewById(R.id.nome_escolhido);
        nome_escolhido.setText(nome + " está " + estado);

    }
    @Override
    public void onClick(View view) {
        Intent intent = getIntent();
        String nome = intent.getStringExtra(ListESP.EXTRA_MESSAGE);
        String nome_ssid = sharedPreferences.getString(nome + "getSSID", "");
        String ssid_home = sharedPreferences.getString(nome_ssid + "getHomessid", "");

        String parameterValue;
        if (view.getId() == button_ON.getId()) {
            parameterValue = "on";
        } else {
            parameterValue = "off";
        }
        String ipAddress = sharedPreferences.getString(nome, "");
        Log.v(TAG, "ip server:" + ipAddress + "nome:" + nome + "::");

        // execute HTTP request
//        new HttpRequestAsyncTask(
//                view.getContext(), "=" + parameterValue, ipAddress, ":" + portNumber, "/?pin"
//        ).execute();

    }
}

