package com.pcs.vicchiam.devoluciones;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by vicch on 05/05/2016.
 */
public class Utilidades {

    public static void Alerts(Activity padre, String titulo, String mensaje, int tipo){
        switch (tipo){
            case 0:{
                new AlertDialog.Builder(padre)
                        .setTitle(titulo)
                        .setMessage(mensaje)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            }
            default:{

            }
        }
    }

    public static boolean Wifi(Activity padre){
        ConnectivityManager cm = (ConnectivityManager) padre.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wm=(WifiManager) padre.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wm.getConnectionInfo();
                if (wifiInfo.getSupplicantState()== SupplicantState.COMPLETED) {
                    String ssid = wifiInfo.getSSID();
                    if(ssid.equals("\"PCS\"") || ssid.equals("PCS") || ssid.equals("pcs")){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
