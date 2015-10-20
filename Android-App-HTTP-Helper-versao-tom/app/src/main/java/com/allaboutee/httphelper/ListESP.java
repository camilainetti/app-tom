package com.allaboutee.httphelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.ArrayList;

public class ListESP extends Activity implements View.OnClickListener {

    private static final String TAG = "ListESP";
    public final static String PREF_NAME = "PREF_NAME";
    public final static String PREF_IP = "PREF_IP_ADDRESS";
    public final static String PREF_PORT = "PREF_PORT_NUMBER";
    public String rede;

    // declare buttons and text inputs
    private Button button_find, button_config, button_access, button_teste;
    //private EditText editTextIPAddress, editTextPortNumber;
    private ListView listWeb;
    private ArrayAdapter<String> adapter;
    ArrayList<String> arrayList = new ArrayList<String>();
    public final static String EXTRA_MESSAGE = "list_esp_wifi_name";
    Intent intentwifi = new Intent(this, AccessActivity.class);

    // shared preferences objects used to save the IP address and port so that the user doesn't have to
    // type them next time he/she opens the app.
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    List<ScanResult> scanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_esp);

        sharedPreferences = getSharedPreferences("HTTP_HELPER_PREFS", Context.MODE_PRIVATE);

        button_find = (Button)findViewById(R.id.button_find);
        button_find.setOnClickListener(this);

        button_config = (Button)findViewById(R.id.button_config);
        button_config.setOnClickListener(this);

        button_teste = (Button)findViewById(R.id.button_teste);
        button_teste.setOnClickListener(this);

        listWeb = (ListView)findViewById(R.id.listWeb);
        listWeb.setItemsCanFocus(true);
        listWeb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Integer i = (int) (long) id;
                System.out.println(arrayList.get(i));
                rede = arrayList.get(i);
                intentwifi.putExtra(EXTRA_MESSAGE, rede);
            }

        });

        button_access = (Button)findViewById(R.id.button_access);
        button_access.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //Botão procurar: abre rotina de busca e exibicao de rede
        if (view.getId() == button_find.getId())
            findWifiNetwork();

        //Botão configurar: configura o esp ao qual o dispositivo está conectado
        if (view.getId() == button_config.getId()) {
            connWifiNetwork(rede);
            Intent intent = new Intent(this, ConfigConn.class);
            startActivity(intent);
        }

        if (view.getId() == button_access.getId()) {
            startActivity(intentwifi);
        }

        if (view.getId() == button_teste.getId()) {
            Intent intent2 = new Intent(this, HomeActivity.class);
            startActivity(intent2);
        }
    }

    public String sendRequest(String parameterValue, String ipAddress, String portNumber, String parameterName) {
        String serverResponse = "ERROR";

        try {

            HttpClient httpclient = new DefaultHttpClient(); // create an HTTP client
            // define the URL e.g. http://myIpaddress:myport/?pin=13 (to toggle pin 13 for example)
            //URI website = new URI("http://"+ipAddress+":"+portNumber+"/?"+parameterName+"="+parameterValue);
            URI website = new URI("http://"+ipAddress+portNumber+parameterName+parameterValue);
            Log.v(TAG, "http://"+ipAddress+portNumber+parameterName+parameterValue);
            HttpGet getRequest = new HttpGet(); // create an HTTP GET object
            getRequest.setURI(website); // set the URL of the GET request
            HttpResponse response = httpclient.execute(getRequest); // execute the request
            // get the ip address server's reply
            InputStream content = null;
            content = response.getEntity().getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    content
            ));
            serverResponse = in.readLine();
            //serverResponse = in.readLine();
            // Close the connection
            content.close();
        } catch (ClientProtocolException e) {
            // HTTP error
            serverResponse = e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            // IO error
            serverResponse = e.getMessage();
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // URL syntax error
            serverResponse = e.getMessage();
            e.printStackTrace();
        }
        // return the server's reply/response text
        return serverResponse;
    }

    public class HttpRequestAsyncTask extends AsyncTask<Void, Void, Void> {

        // declare variables needed
        private String requestReply,ipAddress, portNumber;
        private Context context;
        private AlertDialog alertDialog;
        private String parameter;
        private String parameterValue;

        /**
         * Description: The asyncTask class constructor. Assigns the values used in its other methods.
         * @param context the application context, needed to create the dialog
         * @param parameterValue the pin number to toggle
         * @param ipAddress the ip address to send the request to
         * @param portNumber the port number of the ip address
         */
        public HttpRequestAsyncTask(Context context, String parameterValue, String ipAddress, String portNumber, String parameter)
        {
            this.context = context;

            alertDialog = new AlertDialog.Builder(this.context)
                    .setTitle("HTTP Response From IP Address:")
                    .setCancelable(true)
                    .create();

            this.ipAddress = ipAddress;
            this.parameterValue = parameterValue;
            this.portNumber = portNumber;
            this.parameter = parameter;
        }

        /**
         * Name: doInBackground
         * Description: Sends the request to the ip address
         * @param voids
         * @return
         */
        @Override
        protected Void doInBackground(Void... voids) {
            alertDialog.setMessage("Data sent, waiting for reply from server...");
            if(!alertDialog.isShowing())
            {
                alertDialog.show();
            }
            requestReply = sendRequest(parameterValue,ipAddress,portNumber, parameter);
            return null;
        }

        /**
         * Name: onPostExecute
         * Description: This function is executed after the HTTP request returns from the ip address.
         * The function sets the dialog's message with the reply text from the server and display the dialog
         * if it's not displayed already (in case it was closed by accident);
         * @param aVoid void parameter
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            alertDialog.setMessage(requestReply);
            if(!alertDialog.isShowing())
            {
                alertDialog.show(); // show dialog
            }
            final WifiManager wifiManager =
                    (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if(wifiManager.getConnectionInfo().getSSID().contains("ESP")){
                wifiManager.disconnect();
            }
        }

        /**
         * Name: onPreExecute
         * Description: This function is executed before the HTTP request is sent to ip address.
         * The function will set the dialog's message and display the dialog.
         */
        @Override
        protected void onPreExecute() {
            alertDialog.setMessage("Sending data to server, please wait...");
            if(!alertDialog.isShowing())
            {
                alertDialog.show();
            }
        }

    }

    private void findWifiNetwork(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        final WifiManager wifiManager =
                (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(new BroadcastReceiver() {

            @SuppressLint("UseValueOf")
            @Override
            public void onReceive(Context context, Intent intent) {

                Context tmpContext = getApplicationContext();

                adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.wifi_list_item,
                        arrayList);

                listWeb.setAdapter(adapter);

                WifiManager tmpManager = (WifiManager) tmpContext.getSystemService(android.content.Context.WIFI_SERVICE);

                if (!tmpManager.isWifiEnabled())
                    wifiManager.setWifiEnabled(true);

                scanList = wifiManager.getScanResults();
                arrayList.clear();

                for (int i = 0; i < scanList.size(); i++) {

                    if (scanList.get(i).SSID.contains("CITI")) {
                        arrayList.add((scanList.get(i).SSID));
                    }
                }
            }

        },filter);

        wifiManager.startScan();

    }

    private void connWifiNetwork(String rede){
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        final String nome_rede = rede;
        final WifiManager wifiManager =
                (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(new BroadcastReceiver() {
            @SuppressLint("UseValueOf")
            @Override
            public void onReceive(Context context, Intent intent) {
                //sb = new StringBuilder();
                Context tmpContext = getApplicationContext();
                WifiManager tmpManager =
                        (WifiManager) tmpContext.getSystemService(android.content.Context.WIFI_SERVICE);
                if (!tmpManager.isWifiEnabled())
                    wifiManager.setWifiEnabled(true);

                scanList = wifiManager.getScanResults();

                for (int i = 0; i < scanList.size(); i++) {

                    if (scanList.get(i).SSID.equals(nome_rede)) {
                        System.out.println("Nome da rede:" + nome_rede);
                        WifiConfiguration config = new WifiConfiguration();
                        config.SSID = "\"" + scanList.get(i).SSID + "\"";
                        config.BSSID = scanList.get(i).BSSID;
                        //config.priority = 0;
                        config.preSharedKey = "\"" + "welcomeCITI" + "\"";
                        config.status = WifiConfiguration.Status.ENABLED;
                        int id = wifiManager.addNetwork(config);
                        wifiManager.enableNetwork(id, true);
                        //wifiManager.saveConfiguration();
                    }
                }
             }

        },filter);
        wifiManager.startScan();

    }

    public String getInfoWifi(int iInformationType){
        // Get context variable
        Context tmpContext = getApplicationContext();
        //getContextApplication();
        //And WIFI manager object
        WifiManager tmpManager =
                (WifiManager)tmpContext.getSystemService(android.content.Context.WIFI_SERVICE);
        //Init variable to store current network information for processing
        WifiInfo tmpInfo = tmpManager.getConnectionInfo();
        //Init variable to store DHCP information
        DhcpInfo tmpDHCP = null;
        if (tmpInfo != null)
        {
            switch(iInformationType)
            {
                // BSSID
                case 1:
                    if (tmpInfo.getBSSID() != null)
                        return tmpInfo.getBSSID();
                    else
                        return "";
                    // SSID
                case 2:
                    if (tmpInfo.getSSID() != null)
                        return tmpInfo.getSSID();
                    else
                        return "";
                    // MAC Address
                case 3:
                    if(tmpInfo.getMacAddress() != null)
                        return tmpInfo.getMacAddress();
                    else
                        return "";
                    // IP Address
                case 4:
                    tmpDHCP = tmpManager.getDhcpInfo();
                    if(tmpDHCP != null)
                        return String.valueOf(tmpDHCP.ipAddress);
                    else
                        return "";
                    // Gateway
                case 5:
                    tmpDHCP = tmpManager.getDhcpInfo();
                    if(tmpDHCP != null)
                        return String.valueOf(tmpDHCP.gateway);
                    else
                        return "";
                    // Net Mask
                case 6:
                    tmpDHCP = tmpManager.getDhcpInfo();
                    if(tmpDHCP != null)
                        return String.valueOf(tmpDHCP.netmask);
                    else
                        return "";
                default :
                    return "";
            }
        }
        else {
            return "";
        }

    }
}