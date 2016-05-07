package com.pcs.vicchiam.devoluciones.utilidades;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.pcs.vicchiam.devoluciones.PreferenciasBarActivity;
import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.bbdd.ClienteDB;
import com.pcs.vicchiam.devoluciones.interfaces.RespuestaServidor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by vicchiam on 06/05/2016.
 *
 * Class run the logic application
 */
public class Logica implements RespuestaServidor {

    private static Logica instancia;

    private Activity activity;
    private String recurso;
    private ClienteDB clienteDB;

    public static Logica getInstance(Activity activity){
        if(instancia==null){
            instancia=new Logica(activity);
        }
        return instancia;
    }

    /**
     * Constructor of class Logica
     * @param activity the parent activity
     */
    public Logica(Activity activity){
        this.activity=activity;
        comprobarRecursos();
        inicializarDatabase();
        obtenerClientes(true);
    }

    public void inicializarDatabase(){
        clienteDB=new ClienteDB(this.activity);
    }

    /**
     * Check if the pref values are OK and if WIFI ssid is the same that on the preferences
     */
    public boolean comprobarRecursos(){
        //Get the preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String miWifi=prefs.getString("ssid","");
        String servidor=prefs.getString("servidor","");
        String servicio=prefs.getString("servicio","");

        //Check if wifi preference is right
        if(miWifi.equals("")){
            Utilidades.Alerts(activity,null,activity.getResources().getString(R.string.no_wifi_pref),Utilidades.TIPO_ADVERTENCIA_CONFIGURACION,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent=new Intent(activity, PreferenciasBarActivity.class);
                    activity.startActivity(intent);
                }
            });
            return false;
        }
        //Check if the server preference is rigth
        if(servidor.equals("")){
            Utilidades.Alerts(activity,null,activity.getResources().getString(R.string.no_servidor_pref),Utilidades.TIPO_ADVERTENCIA_CONFIGURACION,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent=new Intent(activity, PreferenciasBarActivity.class);
                    activity.startActivity(intent);
                }
            });
            return false;
        }

        //Check if the service preference is right
        if(servicio.equals("")){
            Utilidades.Alerts(activity,null,activity.getResources().getString(R.string.no_servicio_pref),Utilidades.TIPO_ADVERTENCIA_CONFIGURACION,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent=new Intent(activity, PreferenciasBarActivity.class);
                    activity.startActivity(intent);
                }
            });
            return false;
        }

        //Check if the WIFI ssid is the same that on the preferences
        if(!Utilidades.Wifi(this.activity,miWifi)) {
            Utilidades.Alerts(activity, null, activity.getResources().getString(R.string.no_wifi_conn, miWifi), Utilidades.TIPO_ADVERTENCIA_NEUTRAL, null);
            return false;
        }

        //Put the recurso value
        this.recurso="http://"+servidor+"/"+servicio;

        //All is rigth return TRUE
        return true;
    }

    /**
     * Try get of the server all clients according to the frecuency preferences
     * @param comprobar If is TRUE check date preferences else always get customers
     */
    public void obtenerClientes(boolean comprobar){
        //Get frecuency preference
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String frequencia=prefs.getString("frecuencia_actualizar_cliente","0");

        //Check if date in preferences is greater than now
        String ahora = Utilidades.fechaCadena(new Date());
        if(comprobar) {
            String ultimaActualizacion = prefs.getString("ultima_actualizacion", "01-01-2000");
            if (!frequencia.equals("0") && ahora.equals(ultimaActualizacion)) {
                return;
            }
        }

        //If now is greater than date in preferences update the date of preferences
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString("ultima_actualizacion",ahora);
        editor.commit();

        //Update the customers
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("operacion","obtener_clientes");
        Conexion conn=new Conexion(this.activity,this,this.recurso,Utilidades.OBTENER_CLIENTES);
        conn.execute(hashMap);
    }

    @Override
    public void respuesta(int tipo,String respuesta) {
        switch (tipo){
            case Utilidades.ERROR_CONEXION:{
                Utilidades.Alerts(activity,null,activity.getResources().getString(R.string.conn_err),Utilidades.TIPO_ADVERTENCIA_NEUTRAL,null);
            }
            case Utilidades.OBTENER_CLIENTES:{
                procesarClientes(respuesta);
                break;
            }
            default:{
                break;
            }
        }
    }

    /**
     * With a JSON of customers makes a database table of customers
     * @param jsonClientes
     */
    private void procesarClientes(String jsonClientes){
        try {
            clienteDB.truncate();
            JSONObject jsonObject=new JSONObject(jsonClientes);
            JSONArray jsonArray=jsonObject.getJSONArray("clientes");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jObj=jsonArray.getJSONObject(i);
                int codigo=jObj.getInt("CODIGO");
                String nombre=jObj.getString("NOMBRE");
                clienteDB.insertar(codigo,nombre);
            }
            Log.e("COUNT",jsonArray.length()+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
