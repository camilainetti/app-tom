package com.iothome;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class ConfigConn extends MainActivity{


    private Button button_SET;
    private EditText editTextSSID, editTextsenha, editTextip, editTextgateway, editTextmask, editTextnome;

    private static final String TAG = "ConfigConn";

    public String nome_carinhoso;
    String nomeWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Log.v(TAG, "::");

        Intent intent_nomeWifi = getIntent();
        nomeWifi = intent_nomeWifi.getStringExtra(EXTRA_MESSAGE2);
        Log.v(TAG, "nome Wifi:" + nomeWifi + "::");

        ConectarWIFI.conectar(getApplicationContext(), nomeWifi);
        */
        setContentView(R.layout.activity_config_conn);


        //TextBoxes
        editTextSSID = (EditText)findViewById(R.id.eg_ssid);
        editTextsenha = (EditText)findViewById(R.id.eg_senha);
        editTextip = (EditText)findViewById(R.id.eg_ip);
        editTextgateway = (EditText)findViewById(R.id.eg_gateway);
        editTextmask = (EditText)findViewById(R.id.eg_mask);
        editTextnome = (EditText)findViewById(R.id.eg_nome);

        //Botao Enviar
        button_SET = (Button)findViewById(R.id.button_SET);
        button_SET.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        if (view.getId() == button_SET.getId()) {

            //Recebe nome do dispositivo selecionado na tela principal
            /*Intent intent_nomeWifi = getIntent();
            nomeWifi = intent_nomeWifi.getStringExtra(EXTRA_MESSAGE2);
            */

            //String parameterValue;
            String ssid = editTextSSID.getText().toString().trim();
            String senha = editTextsenha.getText().toString().trim();
            String gateway = editTextgateway.getText().toString().trim();
            String mask = editTextmask.getText().toString().trim();
            String ip = editTextip.getText().toString().trim();
            nome_carinhoso = editTextnome.getText().toString().trim();

            //parameterValue = "SSID="+ssid+"/SENHA="+senha+"/";
            //parameterValue = "SSID="+ssid+"/SENHA="+senha+"/IP="+ip+"/MASCARA="+mask+"/GATEWAY="+gateway+"/NOME="+nome_carinhoso+"/";
            ///SSID=CITI-Terreo/SENHA=1cbe991a14/IP=192.168.1.95/MASCARA=255.255.255.0/GATEWAY=192.168.2.15/NOME=madrugs/

            /*String ipAddress = "192.168.4.1";
            String portNumber = "80";

            // execute HTTP request
            new HttpRequestAsyncTask(
                    view.getContext(), parameterValue, ipAddress, ":" + portNumber, "/"
            ).execute();

        // execute HTTP request
//            if (ssid.length() > 0 && senha.length() > 0) {
//                new HttpRequestAsyncTask(
//                        view.getContext(), "="+parameterValue, ipAddress, ":"+portNumber, "/?"
//                ).execute();
//            }

            //Registra configuracoes
            editor = sharedPreferences.edit();
            Log.v(TAG, "nome:" + nomeWifi + "::");
            nomeWifi = nomeWifi.replaceAll("\"", "");
            Log.v(TAG, "nome:" + nomeWifi + "::");

            editor.putString(nomeWifi, nome_carinhoso);
            editor.commit();

            editor.putString(nome_carinhoso, ip);
            editor.commit();

            editor.putString(nomeWifi + "getHomessid", ssid);
            editor.commit();

            editor.putString(nome_carinhoso + "getSSID", nomeWifi);
            editor.commit();

            //Registra parametros na globals
            Globals.getips().setDatadisps(ip,parameterValue);
            String Dados = Globals.getips().getDataip(ip);
            System.out.println("Dados rede "+Dados);

            //Volta para tela inicial e envia nome escolhido para dispositivo
            Intent intentESP = new Intent(this, MainActivity.class);
            intentESP.putExtra(EXTRA_MESSAGE2, nome_carinhoso);
            startActivity(intentESP);

            //Desconecta
            */

            JSONObject student1 = new JSONObject();
            try {
                student1.put("id", "3");
                student1.put("name", "NAME OF STUDENT");
                student1.put("year", "3rd");
                student1.put("curriculum", "Arts");
                student1.put("birthday", "5/5/1993");

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            JSONObject student2 = new JSONObject();
            try {
                student2.put("id", "2");
                student2.put("name", "NAME OF STUDENT2");
                student2.put("year", "4rd");
                student2.put("curriculum", "scicence");
                student2.put("birthday", "5/5/1993");

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            JSONArray jsonArray = new JSONArray();

            jsonArray.put(student1);
            jsonArray.put(student2);

            JSONObject studentsObj = new JSONObject();

            String jsonStr = studentsObj.toString();

            System.out.println("criado"+jsonStr);

        }

    }
}
