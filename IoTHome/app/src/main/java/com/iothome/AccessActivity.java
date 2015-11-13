package com.iothome;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class AccessActivity extends MainActivity {

    private static final String TAG = "AccessActivity";

    private TextView txtestado, txtestado_2;
    private Switch switch_int;

    private Button button_ON, button_OFF;
    String nome;
    String portNumber = "80";
    Boolean enviou = false;


    private static boolean busy = false;
    private static String last_action = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        switch_int = (Switch) findViewById(R.id.switch_int);
        switch_int.setTextOn("");
        switch_int.setTextOff("");

        button_ON = (Button)findViewById(R.id.button_ON);
        button_OFF = (Button)findViewById(R.id.button_OFF);


        txtestado = (TextView)findViewById(R.id.estado);
        txtestado_2 = (TextView)findViewById(R.id.estado_2);

        button_ON.setEnabled(false);
        button_OFF.setEnabled(false);
        String parameterValue = "estado";
        sendSocket(parameterValue);
        while (Globals.getInstance().getData(1).equals("")) {}
        //String estado = Globals.getInstance().getData(1);
        String estado = Globals.getInstance().getData(1);
        Log.v(TAG, "estado "+estado);
        txtestado.setText(estado);
        createUI("192.168.1.96");
        setButton(estado);

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
        Log.v(TAG, "AccessActivity.last_actionbutton" + AccessActivity.last_action);
        if (!AccessActivity.busy) {
            if ((button_OFF.isEnabled() && !AccessActivity.last_action.equals("off")) ||
                    (button_ON.isEnabled() && !AccessActivity.last_action.equals("on"))) {

                if (view.getId() == button_OFF.getId() || view.getId() == button_ON.getId()) {
                    Log.v(TAG, "aqui");
                    AccessActivity.busy = true;
                    button_OFF.setEnabled(false);
                    button_ON.setEnabled(false);
                    switch_int.setEnabled(false);
                    String parameterValue = "";

                    //Rotinas botoes on e off
                    if (view.getId() == button_ON.getId()) {
                        parameterValue = "on";


                    } else if (view.getId() == button_OFF.getId()) {
                        parameterValue = "off";

                    }
                    sendSocket(parameterValue);
                    //enviou = enviarHTTP(parameterValue, view.getContext(), "192.168.1.96");


                    if (parameterValue.equals("on")) {
                        AccessActivity.last_action = "on";
                        txtestado.setText("ligado");
                    } else {
                        AccessActivity.last_action = "off";
                        txtestado.setText("desligado");
                    }
                    setButton(txtestado.getText().toString());

                    AccessActivity.busy = false;

                }
            }
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
            ).execute();//.get() para esperar
            Toast.makeText(AccessActivity.this,
                    "Comando enviado!",
                    Toast.LENGTH_LONG).show();
            button_OFF.setEnabled(true);
            button_ON.setEnabled(true);
            switch_int.setEnabled(true);
            AccessActivity.busy = false;
            setButton(txtestado.getText().toString());

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
            AccessActivity.busy = false;
            setButton(txtestado.getText().toString());

            return false;
        }

    }
}
