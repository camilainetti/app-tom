package com.iothome;

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
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int SERVERPORT = 9090;
    private static final String SERVER_IP = "192.168.1.179";
    private Socket socket;


    private TextView txtestado;
    int comluz = R.drawable.comluz;
    int semluz = R.drawable.semluz;

    ListView list;
    private ArrayAdapter<ItemListView> adapter;
    ArrayList<ItemListView> arrayList = new ArrayList<ItemListView>();

    private long lastClickTime = 0;

    private Button button_ONOFF, button_enter;
    private ImageButton button_int;
    String nome;
    String portNumber = "80";
    String estado = "";

    Boolean enviou = false;


    public final static String EXTRA_MESSAGE2 = "esp_config";

    // shared preferences objects used to save the IP address and port so that the user doesn't have to
    // type them next time he/she opens the app.
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< HEAD
        //Botões
        button_enter = (Button)findViewById(R.id.button_enter);
        button_enter.setOnClickListener(this);

        button_ONOFF = (Button)findViewById(R.id.button_onoff);
        button_int = (ImageButton)findViewById(R.id.button_interruptor);

        txtestado = (TextView)findViewById(R.id.estado);

        /*String parameterValue = "estado";

        sendSocket(parameterValue);
        enviarHTTP("estado",getApplicationContext(),"192.168.1.97");

        while (Globals.getInstance().getData(1).equals("")) {}
        estado = Globals.getInstance().getData(1);
        Log.v(TAG, "estado " + estado);
        txtestado.setText(estado);
        createUI("192.168.1.96");
        setButton(estado);
        //button_ONOFF.setText("OFF");

        sendSocket(parameterValue);
        while (Globals.getInstance().getData(1).equals("")) {}
        estado = Globals.getInstance().getData(1);
        Log.v(TAG, "estado 2 " + estado);
        createUI("192.168.1.95");
        button_int.setBackgroundResource(R.drawable.pos1);*/

=======
        String parameterValue = "estado";
        Log.v(TAG, "começou:" + SystemClock.elapsedRealtime());
        //sendSocket(parameterValue);
        enviarHTTP(parameterValue, this, "192.168.1.97");


        /*TableLayout table = (TableLayout) findViewById(R.id.tableforitems);
        for (int i = 0; i < 6; i++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            TextView nome_disp = new TextView(this);
            nome_disp.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            nome_disp.setText("tomada");
            nome_disp.setTextSize(20.0f);
            nome_disp.setGravity(Gravity.CENTER);
            tableRow.addView(nome_disp);
            Button b = new Button(this);
            b.setMinimumWidth(100);
            b.setMinimumHeight(200);
            b.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            b.setText("ON");
            tableRow.addView(b);
            table.addView(tableRow);*/
>>>>>>> i_teste
    }

    public void setButton(String state) {
        Log.v(TAG, "state: "+state);
        if (state.equals("ligado")) {
            button_ONOFF.setText("OFF");
        }
        else {
            button_ONOFF.setText("ON");
        }
    }

    public void createUI(String ip) {
        if(ip.equals("192.168.1.95")) {
            //Nome do dispositivo e botoes
            button_int.setOnClickListener(this);
        }
        if(ip.equals("192.168.1.96")) {
            //Nome do dispositivo e botoes
            button_ONOFF.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
<<<<<<< HEAD

        //Botão entrar: acessa dispositivos ligados à rede selecionada
        if (view.getId() == button_enter.getId()) {
            Intent intentwifi = new Intent(this, AccessActivity.class);
            startActivity(intentwifi);

=======
        Log.v(TAG, "SystemClock.elapsedRealtime():" +SystemClock.elapsedRealtime());
        Log.v(TAG, "lastClickTime:" +lastClickTime);
        if (SystemClock.elapsedRealtime() - lastClickTime > 2000) {
            lastClickTime = SystemClock.elapsedRealtime();
            String parameterValue;

            if (view.getId() == button_ONOFF.getId()) {
                Log.v(TAG, "aqui");
                button_int.setEnabled(false);
                button_ONOFF.setEnabled(false);
                //Rotinas botoes on e off
                if (button_ONOFF.getText().equals("ON")) {
                    parameterValue = "on";


                } else {
                    parameterValue = "off";

                }

                sendSocket(parameterValue);
                //enviou = enviarHTTP(parameterValue, view.getContext(), "192.168.1.96");

                if (parameterValue.equals("on")) {
                    txtestado.setText("ligado");
                } else {
                    txtestado.setText("desligado");
                }
                setButton(txtestado.getText().toString());
            }


            if (view.getId() == button_int.getId()) {

                if (button_int.getBackground().getIntrinsicHeight() == getResources().getDrawable(comluz).getIntrinsicHeight()) {
                    parameterValue = "off";
                } else {
                    parameterValue = "on";
                }

                sendSocket(parameterValue);

                if (parameterValue.equals("on")) {
                    button_int.setBackgroundResource(R.drawable.comluz);
                } else {
                    button_int.setBackgroundResource(R.drawable.semluz);
                }
            }
>>>>>>> i_teste
        }
    }

    public String sendRequest(String parameterValue, String ipAddress, String portNumber, String parameterName) {
        String serverResponse;

        try {
            HttpClient httpclient = new DefaultHttpClient(); // create an HTTP client
            // define the URL e.g. http://myIpaddress:myport/?pin=13 (to toggle pin 13 for example)
            //URI website = new URI("http://"+ipAddress+":"+portNumber+"/?"+parameterName+"="+parameterValue);
            URI website = new URI("http://"+ipAddress+portNumber+parameterName+parameterValue);
            //URI website = new URI("http://"+"httpbin.org/ip");
            Log.v(TAG, "http://" + ipAddress + portNumber + parameterName + parameterValue);
            HttpGet getRequest = new HttpGet(); // create an HTTP GET object
            getRequest.setURI(website); // set the URL of the GET request
            Log.v(TAG, "dado pronto para enviar:" + SystemClock.elapsedRealtime());
            HttpResponse response = httpclient.execute(getRequest); // execute the request
            Log.v(TAG, "resposta recebida:" +SystemClock.elapsedRealtime());
            Log.v(TAG, "response::"+response.toString()+"::");
            // get the ip address server's reply
            InputStream content;
            content = response.getEntity().getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    content
            ));
            serverResponse = in.readLine();
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
            this.ipAddress = ipAddress;
            this.parameterValue = parameterValue;
            this.portNumber = portNumber;
            this.parameter = parameter;
            alertDialog = new AlertDialog.Builder(this.context)
                    .setTitle("Enviando comando!")
                    .setCancelable(true)
                    .create();
        }

        /**
         * Name: doInBackground
         * Description: Sends the request to the ip address
         */
        @Override
        protected Void doInBackground(Void... voids) {
            requestReply = sendRequest(parameterValue,ipAddress,portNumber, parameter);
            Log.v(TAG, "resposta 2:" +SystemClock.elapsedRealtime());
            Globals.getInstance().setData(requestReply);
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

            Log.v(TAG, "requestReply::" + requestReply + "::");
            if (parameterValue.equals("=on") || parameterValue.equals("=off")) {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
            Log.v(TAG, "aqui:" +SystemClock.elapsedRealtime());
        }

        /**
         * Name: onPreExecute
         * Description: This function is executed before the HTTP request is sent to ip address.
         * The function will set the dialog's message and display the dialog.
         */
        @Override
        protected void onPreExecute() {

            Log.v(TAG, "Enviando...::"+parameterValue+"::");
            if (parameterValue.equals("=on") || parameterValue.equals("=off")) {
                Log.v(TAG, "Enviando2...");
                alertDialog.setMessage("Aguarde!");
                if (!alertDialog.isShowing()) {
                    alertDialog.show();
                }
            }
        }

    }

    //Envia comando HTTP GET para o ESP
    public boolean enviarHTTP(String parameterValue, Context ctx, String ip) {

        String portNumber = "80";
        Log.v(TAG, "ip server:" + ip + "::" + "parameterValue" + parameterValue);

        // execute HTTP request
        try {
            new HttpRequestAsyncTask(
                    ctx, "=" + parameterValue, ip, ":" + portNumber, "/?pin"
            ).execute().get();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void sendSocket(final String data) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
<<<<<<< HEAD
                    Socket s = new Socket("192.168.1.128", 9090);
                    String answer = "";
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                    dos.writeUTF(data);

=======
                    socket = new Socket("192.168.1.97", 80);
                    String answer;
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    Log.v(TAG, "dado pronto para enviar:" +SystemClock.elapsedRealtime());
                    dos.writeBytes(data);
                    Log.v(TAG, "enviou:" + SystemClock.elapsedRealtime());
>>>>>>> i_teste
                    //read input stream
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader); // get the client message
                    bufferedReader.ready();
                    answer = bufferedReader.readLine();
                    Log.v(TAG, "recebeu resposta:" +SystemClock.elapsedRealtime());
                    Globals.getInstance().setData(answer);
                    Log.v(TAG, "answer::" + answer + "::");
                    bufferedReader.close();
                    inputStreamReader.close();
                    dos.close();
                    socket.close();
                    Log.v(TAG, "terminou:" + SystemClock.elapsedRealtime());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        t.start();
        if (data.equals("estado")) {
            while (t.isAlive()) {
            }
        }
        Log.v(TAG, "aqui::");
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
        DhcpInfo tmpDHCP;
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

<<<<<<< HEAD
    //Envia comando HTTP GET para o ESP
    public boolean enviarHTTP(String parameterValue, Context ctx, String ip) {

        String portNumber = "80";
        Log.v(TAG, "ip server:" + ip + "::" + "parameterValue" + parameterValue);

        // execute HTTP request
        try {
            new HttpRequestAsyncTask(
                    ctx, "=" + parameterValue, ip, ":" + portNumber, "/?pin"
            ).execute();//.get() para esperar


            return true;
        }
        catch (Exception e) {


            return false;
        }

=======
    private void findWifiNetwork(){
        //listESP = (ListView)findViewById(com.iothome.R.id.listWeb);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        final WifiManager wifiManager =
                (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(new BroadcastReceiver() {

            @SuppressLint("UseValueOf")
            @Override
            public void onReceive(Context context, Intent intent) {

                Context tmpContext = getApplicationContext();
                //adapter = new ArrayAdapter<String>(getApplicationContext(),
                //      R.layout.wifi_list_item,
                //    arrayList);

                //listESP.setAdapter(adapter);

                WifiManager tmpManager = (WifiManager) tmpContext.getSystemService(android.content.Context.WIFI_SERVICE);
                if (!tmpManager.isWifiEnabled())
                    wifiManager.setWifiEnabled(true);

                //scanList = wifiManager.getScanResults();
                //arrayList.clear();

                //for (int i = 0; i < scanList.size(); i++) {
                //  if (scanList.get(i).SSID.contains("")) {
                //    arrayList.add((scanList.get(i).SSID));
                //}
                //}
            }

        }, filter);

        wifiManager.startScan();
        //return listESP;
>>>>>>> i_teste
    }

}
