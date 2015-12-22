package com.iothome;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ConfigConn extends MainActivity{

    private Button button_SET;
    private EditText text_esps, editTextsenha, editTextip, editTextgateway, editTextmask, editTextnome, editssid;
    private RadioButton radioesc;
    private RadioGroup radiotipo;
    List<String> list = new ArrayList<String>();

    //Variáveis do WifiScan
    List<ScanResult> scanList;

    public volatile Boolean flag;
    private Boolean teste = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_conn);

        //Devices Salvos
        sharedPreferences_dev = getSharedPreferences("Devices_salvos", Context.MODE_PRIVATE);

        //TextBoxes
        text_esps = (EditText)findViewById(R.id.text_esps);
        editssid = (EditText)findViewById(R.id.eg_ssid);
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

        //Completando esp e redes
        Intent intentconf = getIntent();
        text_esps.setText(intentconf.getStringExtra(AccessActivity.EXTRA_MESSAGE).replaceAll("\"", ""));
        text_esps.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == button_SET.getId()) {

            //Entrada de dados do dispositivo

            String ssid = editssid.getText().toString().trim();
            String esp = text_esps.getText().toString().trim();
            String senha = editTextsenha.getText().toString().trim();
            String gateway = editTextgateway.getText().toString().trim();
            String mask = editTextmask.getText().toString().trim();
            String ip = editTextip.getText().toString().trim();
            String nome_carinhoso = editTextnome.getText().toString().trim();

            if (esp.equals("") || ssid.equals("") || senha.equals("") || gateway.equals("") || mask.equals("") || ip.equals("") || nome_carinhoso.equals("")) {
                Toast.makeText(ConfigConn.this,
                        "Insira os dados faltantes",
                        Toast.LENGTH_LONG).show();
            } else if (RedesAcessiveis(ssid) != true) {
                Toast.makeText(ConfigConn.this,
                        "Insira corretamente o nome da rede",
                        Toast.LENGTH_LONG).show();
            }
            else{
                //Verifica tipo de dispositivo
                int selectedId = radiotipo.getCheckedRadioButtonId();
                radioesc = (RadioButton) findViewById(selectedId);
                String tipo = radioesc.getText().toString();

                //Atesta existencia do dispositivo
                boolean existe = false;

                //Cria json de dados
                JSONObject json = writeJSON(ssid, senha, gateway, mask, ip, nome_carinhoso, tipo);

                //Salva dados do device
                editor_dev = sharedPreferences_dev.edit();

                //1. Registra dados usando a chave IP para cada device
                if (sharedPreferences_dev.getString(ip, "").equals(null) || sharedPreferences_dev.getString(ip, "").equals("")) {
                    editor_dev.putString(ip, json.toString());
                } else {
                    editor_dev.remove(ip);
                    editor_dev.commit();
                    editor_dev.putString(ip, json.toString());
                    existe = true;
                }
                editor_dev.commit();

                //2. Registra todos IPs programados
                String devices_programados = "";

                if ((!sharedPreferences_dev.getString("lista_de_ips", "").equals("") && !sharedPreferences_dev.getString("lista_de_ips", "").equals(null)) && existe == false) {
                    //Caso ja tenha dispositivos configurados
                    devices_programados = sharedPreferences_dev.getString("lista_de_ips", "");
                    editor_dev.putString("lista_de_ips", devices_programados + "," + ip);
                } else if (existe == false) {
                    //Caso nao tenha dispositivos configurados
                    editor_dev.putString("lista_de_ips", ip);
                }
                editor_dev.commit();

                //Printa ips programados
                System.out.print("lista de ips " + sharedPreferences_dev.getString("lista_de_ips", "") + "\n");

                //Procura devices programados pelos ips
                devices_programados = sharedPreferences_dev.getString("lista_de_ips", "");
                String ips[] = devices_programados.split(Pattern.quote(","));

                //Printa devices programados
                for (int i = 0; ips.length > i; i++) {
                    System.out.print("dispositivos salvos " + sharedPreferences_dev.getString(ips[i], "") + "\n");
                }

                //3. Limpa registros
                //editor_dev.clear();
                //editor_dev.commit();

                //4. Envia dados pro esp
                sendSocket(json.toString(),"192.168.4.1");
                // /SSID=CITI-Terreo/SENHA=1cbe991a14/IP=192.168.1.95/MASCARA=255.255.255.0/GATEWAY=192.168.2.15/NOME=madrugs/TIPO=tomada/

                String sucesso = "";

                try {
                    sucesso = sharedPreferences.getString("resposta192.168.4.1", "");
                } catch (Exception e) {
                    Toast.makeText(ConfigConn.this,
                            "Aguardando envio",
                            Toast.LENGTH_LONG).show();
                    sucesso = "Aguardando envio...";
                }

                //Verifica se enviou e volta para a lista de devices
                if(sucesso.equals("falta: mais nada")){
                    editor = sharedPreferences.edit();
                    editor.remove("resposta192.168.4.1");
                    editor.commit();
                    Intent devices = new Intent(this, AccessActivity.class);
                    startActivity(devices);
                }
                else{
                    Toast.makeText(ConfigConn.this,
                            "Tente novamente.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // add items into spinner dynamically
    public boolean RedesAcessiveis(final String ssid){
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        ConfigConn.this.flag = false;
        Log.i("AQUIIIIIIIIIIIIII", "acessei");

        final WifiManager wifiManager =(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(new BroadcastReceiver() {
            @SuppressLint("UseValueOf")
            @Override
            public void onReceive(Context context, Intent intent) {
                Context tmpContext = getApplicationContext();

                WifiManager tmpManager = (WifiManager) tmpContext.getSystemService(Context.WIFI_SERVICE);

                if (!tmpManager.isWifiEnabled())
                    wifiManager.setWifiEnabled(true);

                scanList = wifiManager.getScanResults();
                arrayList.clear();

                for (int i = 0; i < scanList.size(); i++) {
                    list.add(scanList.get(i).SSID);

                    if (ssid.equals(scanList.get(i).SSID)) {
                        ConfigConn.this.flag = true;
                        Log.i("AQUIIIIIIIIIIIIII", scanList.get(i).SSID);
                        Log.i(ssid, scanList.get(i).SSID);
                    }
                }
            }
        }, filter);
        wifiManager.startScan();
        Log.i("AQUIIIIIIIIIIIIII", ConfigConn.this.flag.toString());
        return ConfigConn.this.flag;
    }

    public JSONObject writeJSON(String ssid, String senha, String gateway, String mask, String ip, String nome_carinhoso, String tipo) {
        JSONObject object = new JSONObject();
        try {
            //object.put("ESP", esp);
            object.put("SSID", ssid);
            object.put("SENHA", senha);
            object.put("IP", ip);
            object.put("MASCARA", mask);
            object.put("GATEWAY", gateway);
            object.put("NOME", nome_carinhoso);
            object.put("TIPO", tipo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(object);
        return (object);
    }

}
