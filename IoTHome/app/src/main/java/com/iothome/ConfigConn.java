package com.iothome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfigConn extends MainActivity{


    private Button button_SET;
    private EditText editTextSSID, editTextsenha, editTextip, editTextgateway, editTextmask, editTextnome;
    private RadioButton radioint, radiotom, radioesc;
    private RadioGroup radiotipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_conn);

        //Devices Salvos
        sharedPreferences_dev = getSharedPreferences("Devices_salvos", Context.MODE_PRIVATE);

        //TextBoxes
        editTextSSID = (EditText)findViewById(R.id.eg_ssid);
        editTextsenha = (EditText)findViewById(R.id.eg_senha);
        editTextip = (EditText)findViewById(R.id.eg_ip);
        editTextgateway = (EditText)findViewById(R.id.eg_gateway);
        editTextmask = (EditText)findViewById(R.id.eg_mask);
        editTextnome = (EditText)findViewById(R.id.eg_nome);
        editTextnome = (EditText)findViewById(R.id.eg_nome);
        radiotipo = (RadioGroup)findViewById(R.id.eg_tipo);

        //Botao Enviar
        button_SET = (Button)findViewById(R.id.button_SET);
        button_SET.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == button_SET.getId()) {

            String ssid = editTextSSID.getText().toString().trim();
            String senha = editTextsenha.getText().toString().trim();
            String gateway = editTextgateway.getText().toString().trim();
            String mask = editTextmask.getText().toString().trim();
            String ip = editTextip.getText().toString().trim();
            String nome_carinhoso = editTextnome.getText().toString().trim();

            String tipo = "";

            int selectedId = radiotipo.getCheckedRadioButtonId();
            radioesc = (RadioButton) findViewById(selectedId);

            System.out.println(selectedId);

            tipo = radioesc.getText().toString().trim();

            JSONObject json = writeJSON(ssid, senha, gateway, mask, ip, nome_carinhoso, tipo);
            // tete para pegar o json da string
            try {
                JSONObject obj = new JSONObject(json.toString());
                System.out.println("teste SSID: "+obj.getString("SSID"));
            } catch (Exception e){

            }

            editor_dev = sharedPreferences_dev.edit();

            editor_dev.putString(ip, json.toString());
            editor_dev.commit();

            String devices_programados = sharedPreferences_dev.getString("lista_de_ips","");

            editor_dev.putString("lista_de_ips", devices_programados + "," + ip);
            editor_dev.commit();

            Intent devices = new Intent(this, MainActivity.class);
            startActivity(devices);
        }
    }

    public JSONObject writeJSON(String ssid, String senha, String gateway, String mask, String ip, String nome_carinhoso, String tipo) {
        JSONObject object = new JSONObject();
        try {
            object.put("SSID", ssid);
            object.put("Senha", senha);
            object.put("gateway", gateway);
            object.put("mask", mask);
            object.put("ip", ip);
            object.put("apelido", nome_carinhoso);
            object.put("tipo", tipo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(object);
        return (object);
    }
}
