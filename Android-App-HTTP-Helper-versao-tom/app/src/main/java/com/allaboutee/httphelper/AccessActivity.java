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

    //private TextView txtestado, txtestado_2;
    private Switch switch_int;

    private Button button_ON, button_OFF, button_back;
    String nome;
    String portNumber = "80";
    Boolean enviou = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);


        switch_int = (Switch) findViewById(R.id.switch_int);
        switch_int.setTextOn("");
        switch_int.setTextOff("");

        button_ON = (Button)findViewById(R.id.button_ON);
        button_OFF = (Button)findViewById(R.id.button_OFF);


        //txtestado = (TextView)findViewById(R.id.estado);
        //txtestado_2 = (TextView)findViewById(R.id.estado_2);


        /*//interruptor
        try {
            new HttpRequestAsyncTask(
                    getApplicationContext(), "=" + "estado", "192.168.1.95", ":" + portNumber, "/?"
            ).execute().get();
            String estado_2 = Globals.getInstance().getData(1);
            System.out.println("estado_2 " + estado_2);
            String[] parts_2 = estado_2.split("_");
            String est_2 = parts_2[1];

            if (est_2.equals("ligado")) {
                txtestado_2.setText("Azul");
                switch_int.setChecked(true);
            }
            else if (est_2.equals("desligado")) {
                txtestado_2.setText("Cinza");
                switch_int.setChecked(false);
            }
            switch_int.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    button_OFF.setEnabled(false);
                    button_ON.setEnabled(false);
                    switch_int.setEnabled(false);
                    if (isChecked) {
                        enviou = enviarHTTP("on", getApplicationContext(), "192.168.1.95");
                        if (enviou) {
                            txtestado_2.setText("Azul");
                        }
                    } else {
                        enviou = enviarHTTP("off", getApplicationContext(), "192.168.1.95");
                        if (enviou) {
                            txtestado_2.setText("Cinza");
                        }

                    }
                }
            });
        }
        catch (Exception e) {
            Log.v(TAG, "erro no envio: "+e);
            txtestado_2.setText("Desativado! Volte ao início e tente novamente!");
        }


        //tomada
        try {
            new HttpRequestAsyncTask(
                    getApplicationContext(), "=" + "estado", "192.168.1.96", ":" + portNumber, "/?"
            ).execute().get();
            String estado = Globals.getInstance().getData(1);
            System.out.println("estado " + estado);
            String[] parts = estado.split("_");
            String est = estado;

            txtestado.setText(est);
            createUI("192.168.1.96");
            setButton(est);
        }
        catch (Exception e){
            Log.v(TAG, "erro no envio: "+e);
            txtestado.setText("Desativado! Volte ao início e tente novamente!");
        }*/

        switch_int.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                button_OFF.setEnabled(false);
                button_ON.setEnabled(false);
                switch_int.setEnabled(false);
                if (isChecked) {
                    enviou = enviarHTTP("on", getApplicationContext(), "192.168.1.95");
                    if (enviou) {
                        //txtestado_2.setText("Azul");
                    }
                } else {
                    enviou = enviarHTTP("off", getApplicationContext(), "192.168.1.95");
                    if (enviou) {
                        //txtestado_2.setText("Cinza");
                    }

                }
            }
        });
        createUI("192.168.1.96");
        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);

    }


    public void setButton(String state) {
        Log.v(TAG, "state: "+state);
        if (state.equals("ligado")) {
            button_ON.setEnabled(false);
            button_OFF.setEnabled(true);
        }
        else {
            button_OFF.setEnabled(false);
            button_ON.setEnabled(true);
        }

    }

    public void createUI(String ip) {
        if (ip.equals("192.168.1.95")) {


        }
        else if(ip.equals("192.168.1.96")) {
            //Nome do dispositivo e botoes
            button_ON.setOnClickListener(this);
            button_OFF.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == button_OFF.getId() || view.getId() == button_ON.getId()) {

            button_OFF.setEnabled(false);
            button_ON.setEnabled(false);
            switch_int.setEnabled(false);

            String parameterValue = "";

            //Rotinas botoes on e off
            if (view.getId() == button_ON.getId()) {
                parameterValue = "on";


            }
            else if (view.getId() == button_OFF.getId()) {
                parameterValue = "off";

            }

            enviou = enviarHTTP(parameterValue, view.getContext(), "192.168.1.96");
            if (enviou) {
                if (parameterValue.equals("on")) {
                    //txtestado.setText("ligado");
                }
                else {
                    //txtestado.setText("desligado");
                }
                //setButton(txtestado.getText().toString());

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
    public boolean enviarHTTP(String parameterValue, Context ctx, String ip) {

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
            button_OFF.setEnabled(true);
            button_ON.setEnabled(true);
            switch_int.setEnabled(true);
            //setButton(txtestado.getText().toString());
            return true;
        }
        catch (Exception e) {
            Toast.makeText(AccessActivity.this,
                    "Comando não enviado! Tente novamente",
                    Toast.LENGTH_LONG).show();
            Log.v(TAG, "2-erro no envio: " + e);
            button_OFF.setEnabled(true);
            button_ON.setEnabled(true);
            switch_int.setEnabled(true);
            //setButton(txtestado.getText().toString());
            return false;
        }
    }
}
