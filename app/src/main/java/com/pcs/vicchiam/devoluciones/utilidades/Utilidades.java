package com.pcs.vicchiam.devoluciones.utilidades;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.bbdd.Devolucion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vicchiam on 05/05/2016.
 * Static clase to make the alerts and checks
 */
public class Utilidades {

    //Devolucion
    public static Devolucion devolucion;

    public static final int TIPO_ADVERTENCIA_NEUTRAL=0;
    public static final int TIPO_ADVERTENCIA_CONFIGURACION=1;
    public static final int TIPO_ADVERTENCIA_SI_NO=2;

    //Connection
    public static final int ERROR_BASE_DATOS=-2;
    public static final int ERROR_CONEXION=-1;
    public static final int OBTENER_CLIENTES_NUEVO=1;
    public static final int OBTENER_CLIENTES_ACTUALIZAR=2;
    public static final int OBTENER_ARTICULOS_NUEVO=3;
    public static final int OBTENER_ARTICULOS_ACTUALIZAR=4;
    public static final int FINALIZAR_CLIENTES_NUEVO=5;
    public static final int FINALIZAR_CLIENTES_ACTUALIZAR=6;
    public static final int FINALIZAR_ARTICULOS=7;


    //AsyncDatabaseType
    public static final int CLIENTE=0;
    public static final int ARTICULO=0;

    /**
     * Method that make the alerts
     * @param activity parent activity
     * @param titulo title of the window dialog
     * @param mensaje menssage that appear in the dialog
     * @param tipo type of the alert
     * @param onClickListener action that does a specific button
     */
    public static void Alerts(Activity activity, String titulo, String mensaje, int tipo, DialogInterface.OnClickListener onClickListener){
        String title=titulo;
        if(titulo==null && (tipo==TIPO_ADVERTENCIA_NEUTRAL || tipo==TIPO_ADVERTENCIA_CONFIGURACION)){
            title=activity.getResources().getString(R.string.advertencia);
        }

        switch (tipo){
            case TIPO_ADVERTENCIA_NEUTRAL:{
                new AlertDialog.Builder(activity)
                        .setTitle(title)
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
            case TIPO_ADVERTENCIA_CONFIGURACION:{
                new AlertDialog.Builder(activity)
                        .setTitle(title)
                        .setMessage(mensaje)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Ir",onClickListener)
                        .show();
                break;
            }
            case TIPO_ADVERTENCIA_SI_NO:{
                new AlertDialog.Builder(activity)
                        .setTitle(title)
                        .setMessage(mensaje)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Descartar",onClickListener)
                        .show();
                break;
            }
            default:{

            }
        }
    }

    /**
     * Make a snackbar
     * @param activity parent activity on this is show
     * @param mensaje the message that show
     */
    public static void crearSnackBar(Activity activity,String mensaje){
        View v=activity.findViewById(android.R.id.content);
        Snackbar.make(v,mensaje, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Check the WIFI connection and SSID
     * @param activity parent activity
     * @param miWifi name of ssid
     * @return if is connected to the preferences WIFI return TRUE
     */
    public static boolean Wifi(Activity activity, String miWifi){
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wm=(WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wm.getConnectionInfo();
                if (wifiInfo.getSupplicantState()== SupplicantState.COMPLETED) {
                    String ssid = wifiInfo.getSSID();
                    if(ssid.equals("\""+miWifi+"\"") || ssid.equals(miWifi) || ssid.toLowerCase().equals(miWifi.toLowerCase())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Methot that conevrt a String in a Date
     * @param text
     * @return
     */
    public static Date cadenaFecha(String text){
        SimpleDateFormat f=new SimpleDateFormat("dd-MM-yyyy");
        Date d=null;
        try{
            d=f.parse(text);
        }
        catch (ParseException e){
            Log.e("OBTENER_FECHA",e.getMessage());
        }
        return d;
    }

    /**
     * Convert date in string
     * @param d date to convert
     * @return a string with the date
     */
    public static String fechaCadena(Date d){
        SimpleDateFormat f=new SimpleDateFormat("dd/MM/yyyy");
        String s=null;
        s=f.format(d);
        return s;
    }

    /**
     * Check if a string is a number
     * @param text
     * @return
     */
    public static boolean esNumero(String text){
        for (char c : text.toCharArray())
        {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }



}
