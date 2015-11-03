package com.allaboutee.httphelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AccessActivity extends ListESP {

    private static final String TAG = "AccessActivity";

    private TextView nome_escolhido;
    private Button button_ON, button_OFF, button_back;
    String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        //Recebe nome do dispositivo escolhido na tela principal
        nome = intent.getStringExtra(ListESP.EXTRA_MESSAGE);

        //Recebe nome da rede e senha ja configurados
        String nome_ssid = sharedPreferences.getString(nome+"getSSID", "");
        String ssid_home = sharedPreferences.getString(nome_ssid+"getHomessid", "");
        Log.v(TAG, "home_ssid:" + ssid_home + "::");

        //ConectarESP.conectar(getApplicationContext(), ssid_home);

        setContentView(R.layout.activity_access);

        //Nome do dispositivo e botoes
        nome_escolhido= (TextView)findViewById(R.id.nome_escolhido);
        nome_escolhido.setText(nome);

        button_ON = (Button)findViewById(R.id.button_ON);
        button_ON.setOnClickListener(this);

        button_OFF = (Button)findViewById(R.id.button_OFF);
        button_OFF.setOnClickListener(this);

        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String parameterValue = "";

        //Rotinas botoes on e off
        if (view.getId() == button_ON.getId())
            parameterValue = "on";
        if (view.getId() == button_OFF.getId())
            parameterValue = "off";

        if (view.getId() == button_OFF.getId() || view.getId() == button_ON.getId()) {
            String ipAddress = sharedPreferences.getString(nome, "");
            String portNumber = "80";
            Log.v(TAG, "ip server:" + ipAddress + "nome:" + nome + "::");

            // execute HTTP request
            new HttpRequestAsyncTask(
                    view.getContext(), "=" + parameterValue, ipAddress, ":" + portNumber, "/?pin"
            ).execute();
        }

        //Rotina botao voltar
        if(view.getId() == button_back.getId()) {
            Intent intentvoltar = new Intent(this, ListESP.class);
            intentvoltar.putExtra(EXTRA_MESSAGE2, nome);
            startActivity(intentvoltar);

        }
    }
}
