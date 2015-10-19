package com.allaboutee.httphelper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AccessActivity extends ListESP {

    private TextView nome_escolhido;
    private Button button_ON,button_OFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        nome_escolhido= (TextView)findViewById(R.id.nome_escolhido);
        nome_escolhido.setText("lala");
        button_ON = (Button)findViewById(R.id.button_ON);
        button_OFF = (Button)findViewById(R.id.button_OFF);
        button_ON.setOnClickListener(this);
        button_OFF.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        String parameterValue;
        if (view.getId() == button_ON.getId()) {
            parameterValue = "ON";
        } else {
            parameterValue = "OFF";
        }
        String ipAddress = "";//sharedPreferences.getString(nome)
        String portNumber = "80";

        // execute HTTP request
        new HttpRequestAsyncTask(
                view.getContext(), "=" + parameterValue, ipAddress, ":" + portNumber, "/?pin"
        ).execute();
    }
}

