package com.iothome;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by bela_ on 19-Nov-15.
 */
public class ItemListView {
    private TextView nome_dispositivo;
    private Button button;
    private ImageButton interruptor;

    public ItemListView(Context ctx, TextView nome, Button button) {
        nome_dispositivo = nome;
        this.button = button;
    }

    public ItemListView(TextView nome, ImageButton interruptor) {
        nome_dispositivo = nome;
        this.interruptor = interruptor;

    }
}
