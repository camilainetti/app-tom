package com.allaboutee.httphelper_teste;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bela_ on 03-Nov-15.
 */
public class Globals {
    private static Globals instance;
    private static Globals ips;


    // Global variable
    private String resposta = "";
    private Boolean foi = false;
    Map<String, String> aMap = new HashMap<String, String>();

    // Restrict the constructor from being instantiated
    private Globals(){}

    public void setData(String d){
        this.resposta=d;
    }
    public void setData(Boolean d){
        this.foi=d;
    }
    public void setDatadisps(String ip, String value){
        this.aMap.put(ip, value);
    }

    public String getData(int n){
        if (n == 1) {
            return this.resposta;
        }
        if (n == 0) {
            return Boolean.toString(this.foi);
        }
        return "ERRO";
    }

    public String getDataip(String ip){
        //Recebe dados da rede
        //Molde dos dados: SSID=CITI-Terreo/SENHA=1cbe991a14/IP=192.168.1.95/MASCARA=255.255.255.0/GATEWAY=192.168.2.15/NOME=madrugs/
        String value = aMap.get(ip);



        return value;
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }

    public static synchronized Globals getips(){
        if(ips==null){
            ips=new Globals();
        }
        return ips;
    }
}
