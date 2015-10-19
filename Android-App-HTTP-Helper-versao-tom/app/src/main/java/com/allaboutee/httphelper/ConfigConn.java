package com.allaboutee.httphelper;


import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class ConfigConn extends ListESP{


    private Button button_SET;
    private EditText editTextSSID, editTextsenha, editTextip, editTextgateway, editTextmask, editTextnome;
    //SharedPreferences sharedPreferences;
    //SharedPreferences.Editor editor;
    //SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_conn);

        editTextSSID = (EditText)findViewById(R.id.eg_ssid);
        editTextsenha = (EditText)findViewById(R.id.eg_senha);
        editTextip = (EditText)findViewById(R.id.eg_ip);
        editTextgateway = (EditText)findViewById(R.id.eg_gateway);
        editTextmask = (EditText)findViewById(R.id.eg_mask);
        editTextnome = (EditText)findViewById(R.id.eg_nome);

        button_SET = (Button)findViewById(R.id.button_SET);
        button_SET.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == button_SET.getId()) {
            // get the pin number
            String parameterValue;
            // get the ip address
            String ssid = editTextSSID.getText().toString().trim();
            // get the port number
            String senha = editTextsenha.getText().toString().trim();
            String nome = editTextnome.getText().toString().trim();
            String nomeWifi = getInfoWifi(2);
            String gateway = editTextgateway.getText().toString().trim();
            String ip = editTextip.getText().toString().trim();
            String mask = editTextmask.getText().toString().trim();
            parameterValue = "ssid="+ssid+"+senha="+senha+"+nome="+nome+"+gateway="+gateway+"+ip="+ip+"+mask="+mask;
            String ipAddress = "192.168.4.1";
            String portNumber = "80";
            // execute HTTP request
            if (ssid.length() > 0 && senha.length() > 0) {
                new HttpRequestAsyncTask(
                        view.getContext(), "="+parameterValue, ipAddress, ":"+portNumber, "/?"
                ).execute();
            }
            editor = sharedPreferences.edit();
            // save the name for the next time the app is used
            //editor.putString(PREF_NAME, nome);
            editor.putString(nomeWifi, nome);
            editor.putString(nome, ip);
            editor.commit(); // save name
        }
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
