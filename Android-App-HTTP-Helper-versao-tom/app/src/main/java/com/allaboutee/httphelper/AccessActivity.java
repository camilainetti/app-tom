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
        txtestado = (TextView)findViewById(R.id.estado); //tomada

        button_ON = (Button)findViewById(R.id.button_ON);
        button_ON.setOnClickListener(this);

        button_OFF = (Button)findViewById(R.id.button_OFF);
        button_OFF.setOnClickListener(this);

        txtestado_2 = (TextView)findViewById(R.id.estado_2); //interruptor

        switch_int = (Switch) findViewById(R.id.switch_int);
        switch_int.setTextOn("");
        switch_int.setTextOff("");

        //interruptor
        /*try {
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
        String est_2 = parts_2[1];*/
        String est_2 = "ligado";

        if (est_2.equals("ligado")) {
            txtestado_2.setText("Azul");
            switch_int.setChecked(true);
        }
        else if (est_2.equals("desligado")) {
            txtestado_2.setText("Cinza");
            switch_int.setChecked(false);
        }

        //tomada
        /*try {
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
        String est = parts[1];*/
        String est = "ligado";

        txtestado.setText(est);
        if (est.equals("ligado")) {
            button_ON.setEnabled(false);
        }
        else if (est.equals("desligado")) {
            button_OFF.setEnabled(false);
        }

        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);


        switch_int.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                
                String estado_ligado = "";

                if (button_ON.isEnabled()){
                    estado_ligado = "on";
                    button_ON.setEnabled(false);
                }

                if (button_OFF.isEnabled()){
                    estado_ligado = "off";
                    button_OFF.setEnabled(false);
                }

                switch_int.setClickable(false);

                if (isChecked) {
                    enviarHTTP("on", getApplicationContext(), "192.168.1.95");
                    txtestado_2.setText("Azul");
                } else {
                    enviarHTTP("off", getApplicationContext(), "192.168.1.95");
                    txtestado_2.setText("Cinza");

                }

                if (estado_ligado.equals("on")){
                    button_ON.setEnabled(true);
                }
                if (estado_ligado.equals("off")){
                    button_OFF.setEnabled(true);
                }

                switch_int.setClickable(true);
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
                enviarHTTP(parameterValue, view.getContext(), "192.168.1.96");
                txtestado.setText("ligado");
                button_ON.setEnabled(false);
                button_OFF.setEnabled(true);
            }
            else if (view.getId() == button_OFF.getId()) {
                parameterValue = "off";
                enviarHTTP(parameterValue, view.getContext(), "192.168.1.96");
                txtestado.setText("desligado");
                button_ON.setEnabled(true);
                button_OFF.setEnabled(false);

            }

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

        switch_int.setVisibility(View.GONE);

        String portNumber = "80";
        Log.v(TAG, "ip server:" + ip + "::" + "parameterValue" + parameterValue);

        // execute HTTP request
        try {
            new HttpRequestAsyncTask(
                    ctx, "=" + parameterValue, ip, ":" + portNumber, "/?pin"
            ).execute().get();
            Toast.makeText(AccessActivity.this,
                    "Comando enviado!",
                    Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(AccessActivity.this,
                    "Comando não enviado! Tente novamente",
                    Toast.LENGTH_LONG).show();
        }
        switch_int.setVisibility(View.VISIBLE);
    }
}
