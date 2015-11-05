package com.allaboutee.httphelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.regex.Pattern;

public class AccessActivity extends ListESP {

    private static final String TAG = "AccessActivity";

    private TextView txtestado, txtestado_2;
    private Switch switch_int;

    private Button button_ON, button_OFF, button_back;
    String nome;
    String portNumber = "80";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        //Nome do dispositivo e botoes
        txtestado = (TextView)findViewById(R.id.estado);

        button_ON = (Button)findViewById(R.id.button_ON);
        button_ON.setOnClickListener(this);

        button_OFF = (Button)findViewById(R.id.button_OFF);
        button_OFF.setOnClickListener(this);

        txtestado_2 = (TextView)findViewById(R.id.estado_2);

        switch_int = (Switch) findViewById(R.id.switch_int);

        try {
            new HttpRequestAsyncTask(
                    getApplicationContext(), "=" + "estado", "192.168.1.95", ":" + portNumber, "/?"
            ).execute().get();
        }
        catch (Exception e){
            Log.v(TAG, "erro no envio!");
        }

        String estado_2 = Globals.getInstance().getData(1);
        System.out.println("estado_2 " + estado_2);
        String[] parts_2 = estado_2.split("_");
        String est_2 = parts_2[1];

        System.out.println("Nozeeees 2" + est_2);
        txtestado_2.setText(est_2);

        try {
            new HttpRequestAsyncTask(
                    getApplicationContext(), "=" + "estado", "192.168.1.96", ":" + portNumber, "/?"
            ).execute().get();
        }
        catch (Exception e){
            Log.v(TAG, "erro no envio!");
        }


        String estado = Globals.getInstance().getData(1);
        System.out.println("estado " + estado);
        String[] parts = estado.split("_");
        String est = parts[1];

        System.out.println("Nozeeees  " + est);
        txtestado.setText(est);




        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);


        switch_int.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enviarHTTP("on", getApplicationContext(), "192.168.1.95");
                    txtestado_2.setText("on");
                } else {
                    enviarHTTP("off", getApplicationContext(), "192.168.1.95");
                    txtestado_2.setText("off");
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == button_OFF.getId() || view.getId() == button_ON.getId()) {
            String parameterValue = "";

            //Rotinas botoes on e off
            if (view.getId() == button_ON.getId()) {
                parameterValue = "on";
                txtestado.setText("on");
            }
            else if (view.getId() == button_OFF.getId()) {
                parameterValue = "off";
                txtestado.setText("off");
            }

            enviarHTTP(parameterValue, view.getContext(), "192.168.1.96");

        }


        //Rotina botao voltar
        else if (view.getId() == button_back.getId()) {
            Intent intentvoltar = new Intent(this, ListESP.class);
            intentvoltar.putExtra(EXTRA_MESSAGE2, nome);
            startActivity(intentvoltar);
        }
    }
    //Envia comando HTTP GET para o ESP
    public void enviarHTTP(String parameterValue, Context ctx, String ip) {
        String ipAddress = sharedPreferences.getString(nome, "");
        String portNumber = "80";
        Log.v(TAG, "ip server:" + ipAddress + "nome:" + nome + "::");

        // execute HTTP request
        new HttpRequestAsyncTask(
                ctx, "=" + parameterValue, ip, ":" + portNumber, "/?pin"
        ).execute();

        Toast.makeText(AccessActivity.this,
                "Comando enviado!",
                Toast.LENGTH_LONG).show();
    }
}
