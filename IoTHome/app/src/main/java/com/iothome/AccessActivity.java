package com.iothome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class AccessActivity extends MainActivity {

    private static final String TAG = "AccessActivity";

    private ListView listDevices;
    private ImageButton button_devices, button_confs, button_other;

    private static boolean busy = false;
    private static String last_action = "";

    private ArrayAdapter<String> adapter_devices;
    ArrayList<String> arrayList_d = new ArrayList<String>();

    //teste
    private Button button;
    private ImageButton interruptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        //lista de devices
        listDevices = (ListView)findViewById(R.id.listDevices);
        listDevices.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                String value = (String)adapter.getItemAtPosition(position);
                System.out.print(value);

            }
        });

        //menu inferior
        button_devices = (ImageButton)findViewById(R.id.button_devices);
        button_devices.setOnClickListener(this);
        button_confs = (ImageButton)findViewById(R.id.button_confs);
        button_confs.setOnClickListener(this);
        button_other = (ImageButton)findViewById(R.id.button_other);
        button_other.setOnClickListener(this);

        sharedPreferences_dev = getSharedPreferences("Devices_salvos", Context.MODE_PRIVATE);

        adapter_devices = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.wifi_list_item,
                arrayList_d);

        listDevices.setAdapter(adapter_devices);

        //Procura devices programados pelos ips
        String devices = sharedPreferences_dev.getString("lista_de_ips", "");
        String ips[] = devices.split(Pattern.quote(","));

        //Printa devices programados
        if(ips.length==1){
            if(ips[0].equals("") || ips[0].equals(null)){
                System.out.print("Nenhum device associado");
            }
            else{
                for (int i=0; ips.length>i; i++){
                    String details[] = sharedPreferences_dev.getString(ips[i], "").split(Pattern.quote(","));
                    //System.out.print("ips: "+ips[i]+"\n");
                    //System.out.print("quantidade de ips: "+ips.length+"\n");

                    for (int j=0; details.length>j; j++){
                        System.out.print(details[j]+"\n");
                        String infos[] = details[j].split(Pattern.quote(":"));
                        infos[1] = infos[1].replaceAll("\"", "");
                        if (infos[1].contains("}")){
                            infos[1] = infos[1].replace("}", "");
                        }
                        details[j] = infos[1];
                    }
                    //arrayList_d.add(sharedPreferences_dev.getString(ips[i], ""));
                    arrayList_d.add(details[5]+"\n"+details[3]+"\n"+"IP Associado: "+details[6]);
                    if (details[3].equals("Interruptor")){
                        //add switch
                    }
                    else{
                        //add button
                    }
                }
            }
        }

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == button_confs.getId()) {
            String rede = ConnectNetwork.getInstance().verificaRede(getApplicationContext());
            if(!rede.equals("0esp")){
                Intent intentconf = new Intent(this, ConfigConn.class);
                intentconf.putExtra(EXTRA_MESSAGE, rede);
                startActivity(intentconf);
            }
            Toast.makeText(AccessActivity.this,
                    "Conecte-se a uma rede ESP para configurar seu dispositivo."+"\n\n"+"(Note que se seu ESP já tiver sido configurado, " +
                            "é necessário reiniciá-lo antes de conectar-se a ele).",
                    Toast.LENGTH_LONG).show();
        }

        else if (view.getId() == button_devices.getId()) {
            Toast.makeText(AccessActivity.this,
                    "Dispositivos sendo exibidos",
                    Toast.LENGTH_LONG).show();
        }
    }
}
