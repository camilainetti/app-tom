package com.allaboutee.httphelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class ConfigConn extends HomeActivity{

    public final static String PREF_NAME = "PREF_NAME";
    private Button button_SET;
    private EditText editTextSSID, editTextsenha, editTextnome;
    //SharedPreferences sharedPreferences;
    //SharedPreferences.Editor editor;
    //SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_conn);

        editTextSSID = (EditText)findViewById(R.id.eg_ssid);
        editTextsenha = (EditText)findViewById(R.id.eg_senha);
        editTextnome = (EditText)findViewById(R.id.eg_nome);

        button_SET = (Button)findViewById(R.id.button_SET);
        button_SET.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == button_SET.getId()) {
            // get the pin number
            String parameterValue = "";
            // get the ip address
            String ssid = editTextSSID.getText().toString().trim();
            // get the port number
            String senha = editTextsenha.getText().toString().trim();
            String nome = editTextnome.getText().toString().trim();
            parameterValue = "ssid="+ssid+"+senha="+senha+"+nome="+nome;
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
            editor.putString(PREF_NAME, nome);
            editor.commit(); // save name
        }
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
