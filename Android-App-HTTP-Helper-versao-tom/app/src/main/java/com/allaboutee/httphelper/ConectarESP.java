package com.allaboutee.httphelper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by bela_ on 29-Oct-15.
 */
public class ConectarESP {

    public static void conectar(final Context ctx) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                ConnectNetwork.getInstance().setContext(ctx);
                if (ConnectNetwork.getInstance().conectarRede("CITI-guest")){
                    Log.i("teste", "Conectou");
                    //Toast.makeText(ctx, "Conectou na rede", Toast.LENGTH_LONG);

                }
                else {
                    //Toast.makeText(ctx, "NÃO conectou na rede", Toast.LENGTH_LONG);
                    Log.i("teste", "NÃO Conectou");
                }
            }
        });
        t.start();
    }
}
