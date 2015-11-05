package com.allaboutee.httphelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class AccessActivity extends ListESP {

    private static final String TAG = "AccessActivity";

    private TextView nome_escolhido, txtestado, interruptor, txtestado_int;
    private Switch switcher;

    private Button button_ON, button_OFF, button_back;
    String nome;
    String portNumber = "80";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        //Nome do dispositivo e botoes
        nome_escolhido= (TextView)findViewById(R.id.nome_escolhido);
        txtestado = (TextView)findViewById(R.id.estado);

        button_ON = (Button)findViewById(R.id.button_ON);
        button_ON.setOnClickListener(this);

        button_OFF = (Button)findViewById(R.id.button_OFF);
        button_OFF.setOnClickListener(this);

        interruptor = (TextView)findViewById(R.id.interruptor);

        txtestado_int = (TextView)findViewById(R.id.estado_int);

        switcher = (Switch)findViewById(R.id.switcha);

        try {
            new HttpRequestAsyncTask(
                    getApplicationContext(), "=" + "estado", "192.168.1.96", ":" + portNumber, "/?"
            ).execute().get();
        }
        catch (Exception e){
            Log.v(TAG, "erro no envio!");
        }

        String estado = Globals.getInstance().getData(1);

        String[] parts = estado.split("_");
        String est = parts[1];

        System.out.println("Nozeeees " + est);
        txtestado.setText(est);

        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

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

        if (view.getId() == button_OFF.getId() || view.getId() == button_ON.getId()) {
            String ipAddress = sharedPreferences.getString(nome, "");
            String portNumber = "80";
            Log.v(TAG, "ip server:" + ipAddress + "nome:" + nome + "::");

            // execute HTTP request
            new HttpRequestAsyncTask(
                    view.getContext(), "=" + parameterValue, "192.168.1.96", ":" + portNumber, "/?pin"
            ).execute();

            Toast.makeText(AccessActivity.this,
                    "Comando enviado!",
                    Toast.LENGTH_LONG).show();
        }

        //Rotina botao voltar
        else if (view.getId() == button_back.getId()) {
            Intent intentvoltar = new Intent(this, ListESP.class);
            intentvoltar.putExtra(EXTRA_MESSAGE2, nome);
            startActivity(intentvoltar);
        }
    }
}
