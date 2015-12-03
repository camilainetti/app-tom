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

import java.util.regex.Pattern;

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

            //Entrada de dados do dispositivo
            String ssid = editTextSSID.getText().toString().trim();
            String senha = editTextsenha.getText().toString().trim();
            String gateway = editTextgateway.getText().toString().trim();
            String mask = editTextmask.getText().toString().trim();
            String ip = editTextip.getText().toString().trim();
            String nome_carinhoso = editTextnome.getText().toString().trim();

            //Verifica tipo de dispositivo
            int selectedId = radiotipo.getCheckedRadioButtonId();
            radioesc = (RadioButton) findViewById(selectedId);
            String tipo = radioesc.getText().toString();

            //Cria json de dados
            JSONObject json = writeJSON(ssid, senha, gateway, mask, ip, nome_carinhoso, tipo);

            //Salva dados do device
            editor_dev = sharedPreferences_dev.edit();

            //1. Registra dados usando a chave IP para cada device
            editor_dev.putString(ip, json.toString());
            editor_dev.commit();

            //2. Registra todos IPs programados
            String devices_programados = "";

            if (!sharedPreferences_dev.getString("lista_de_ips","").equals("") || !sharedPreferences_dev.getString("lista_de_ips","").equals(null)){
                //Caso ja tenha dispositivos configurados
                devices_programados = sharedPreferences_dev.getString("lista_de_ips","");
                editor_dev.putString("lista_de_ips", devices_programados + "," + ip);
            }
            else{
                //Caso nao tenha dispositivos configurados
                editor_dev.putString("lista_de_ips", ip);
            }
            editor_dev.commit();

            //Printa ips programados
            System.out.print("lista de ips " + sharedPreferences_dev.getString("lista_de_ips", ""));

            //Procura devices programados pelos ips
            devices_programados = sharedPreferences_dev.getString("lista_de_ips","");
            String ips[] = devices_programados.split(Pattern.quote(","));

            //Printa devices programados
            for (int i=0; ips.length>i; i++){
                System.out.print("dispositivos salvos " + sharedPreferences_dev.getString(ips[i], ""));
            }

            //3. Limpa registros
            //editor_dev.clear();
            //editor_dev.commit();

            //Volta para a lista de devices
            Intent devices = new Intent(this, AccessActivity.class);
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
