package com.iothome;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class AccessActivity extends MainActivity {

    private static final String TAG = "AccessActivity";

    private TextView txtestado, txtestado_2;
    private Button button_ON;
    private ImageButton button_int;

    Integer estado_int;
    String portNumber = "80";
    Boolean enviou = false;


    private static boolean busy = false;
    private static String last_action = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        //tomada
        txtestado = (TextView)findViewById(R.id.estado);
        button_ON = (Button)findViewById(R.id.button_onoff);

        //interruptor
        txtestado_2 = (TextView)findViewById(R.id.estado_2);

        button_int = (ImageButton)findViewById(R.id.button_interruptor);

        String parameterValue = "estado";
//        sendSocket(parameterValue);
//        while (Globals.getInstance().getData(1).equals("")) {}

        String estado = Globals.getInstance().getData(1);
        Log.v(TAG, "estado "+estado);
        if (estado.equals(null) || estado.equals("")){
            txtestado.setText("nada encontrado");
        }
        else{
            txtestado.setText(estado);
        }

        createUI("192.168.1.95");
        setButton(estado);
    }


    public void setButton(String state) {
        Log.v(TAG, "state: "+state);
        if (state.equals("ligado")) {
            button_ON.setText("off");
            button_ON.setEnabled(true);
        }
        else if (state.equals("desligado")) {
            button_ON.setText("on");
            button_ON.setEnabled(true);
        }
        else{
            button_ON.setText("Nenhum estado encontrado. Tente novamente.");
            button_ON.setEnabled(false);
        }

    }

    public void createUI(String ip) {
        if (ip.equals("192.168.1.95")) {
            button_int.setOnClickListener(this);
            estado_int = 1;

        }
        else if(ip.equals("192.168.1.96")) {
            //Nome do dispositivo e botoes
            button_ON.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {

        Log.v(TAG, "AccessActivity.last_actionbutton" + AccessActivity.last_action);

        if (!AccessActivity.busy) {

            if (view.getId() == button_ON.getId() && button_ON.isEnabled() && !AccessActivity.last_action.equals(button_ON.getText())) {
                Log.v(TAG, "parametro que sera enviado: "+button_ON.getText());

                AccessActivity.busy = true;

                button_ON.setEnabled(false);
                button_int.setEnabled(false);

                String parameterValue = "";
                parameterValue = button_ON.getText().toString();

                sendSocket(parameterValue);
                //enviou = enviarHTTP(parameterValue, view.getContext(), "192.168.1.96");

                if (parameterValue.equals("on")) {
                    AccessActivity.last_action = "on";
                    txtestado.setText("ligado");
                } else {
                    AccessActivity.last_action = "off";
                    txtestado.setText("desligado");
                }
                setButton(txtestado.getText().toString());

                AccessActivity.busy = false;
                button_int.setEnabled(true);

            }
            else if (view.getId() == button_int.getId() && button_int.isEnabled()) {

                Log.v(TAG, "acionando interruptor");
                Integer state_button_onoff;

                if (button_ON.isEnabled()){
                    state_button_onoff = 1;
                }
                else{
                    state_button_onoff = 0;
                }

                if(estado_int==1){
                    button_ON.setEnabled(false);
                    button_int.setEnabled(false);

                    Log.v(TAG, "mandando on");
                    button_int.setBackgroundResource(R.drawable.pos2);
                    //enviar on

                    estado_int=2;
                    button_int.setEnabled(true);
                    if(state_button_onoff==1){
                        button_ON.setEnabled(true);
                    }

                }
                else{
                    button_ON.setEnabled(false);
                    button_int.setEnabled(false);

                    Log.v(TAG, "mandando off");
                    button_int.setBackgroundResource(R.drawable.pos1);
                    //enviar off

                    estado_int=1;
                    button_int.setEnabled(true);
                    if(state_button_onoff==1){
                        button_ON.setEnabled(true);
                    }
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
            ).execute();//.get() para esperar
            Toast.makeText(AccessActivity.this,
                    "Comando enviado!",
                    Toast.LENGTH_LONG).show();
            button_ON.setEnabled(true);;
            AccessActivity.busy = false;
            setButton(txtestado.getText().toString());

            return true;
        }
        catch (Exception e) {
            Toast.makeText(AccessActivity.this,
                    "Comando não enviado! Tente novamente",
                    Toast.LENGTH_LONG).show();
            Log.v(TAG, "2-erro no envio: " + e);
            button_ON.setEnabled(true);
            AccessActivity.busy = false;
            setButton(txtestado.getText().toString());

            return false;
        }

    }
}
