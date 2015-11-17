package com.iothome;

import android.content.Context;
import android.util.Log;

/**
 * Created by bela_ on 29-Oct-15.
 */
public class ConectarWIFI {

    public synchronized static boolean conectar(final Context ctx, final String wifi) {
        Thread t = new Thread(new Runnable() {
            @Override
            public synchronized void run() {

                ConnectNetwork.getInstance().setContext(ctx);
                if (ConnectNetwork.getInstance().conectarRede(wifi)){
                    Log.i("teste", "Conectou");
                    //Toast.makeText(ctx, "Conectou na rede", Toast.LENGTH_LONG);

                }
                else {
                    //Toast.makeText(ctx, "NÃO conectou na rede", Toast.LENGTH_LONG);
                    Log.i("teste", "NÃO Conectou");
                }
                notifyAll();
            }
        });
        t.start();
        try {
            t.wait();
        } catch (Exception e) {
            Log.i("e2: ", e.toString());
            return false;
        }
        return true;
    }
}
