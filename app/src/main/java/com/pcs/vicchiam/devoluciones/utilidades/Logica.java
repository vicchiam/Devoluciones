package com.pcs.vicchiam.devoluciones.utilidades;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.pcs.vicchiam.devoluciones.PreferenciasBarActivity;
import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.bbdd.Articulo;
import com.pcs.vicchiam.devoluciones.bbdd.ArticuloDB;
import com.pcs.vicchiam.devoluciones.bbdd.ClienteDB;
import com.pcs.vicchiam.devoluciones.bbdd.DBAsyncTask;
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
    private ArticuloDB articuloDB;

    ProgressDialog progressDialog;

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
        //comprobarRecursos();
        inicializarDatabase();
        obtenerDatos(false);
    }

    public void inicializarDatabase(){
        clienteDB=new ClienteDB(this.activity);
        articuloDB=new ArticuloDB(this.activity);
    }

    /**
     * Check if the pref values are OK and if WIFI ssid is the same that on the preferences
     */
    public boolean comprobarRecursos(){
        //Get the preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String miWifi=prefs.getString("ssid",activity.getResources().getString(R.string.wifi_defecto));
        String servidor=prefs.getString("servidor",activity.getResources().getString(R.string.servidor_defecto));
        String servicio=prefs.getString("servicio",activity.getResources().getString(R.string.servicio_defecto));

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
     * @param forzar If is TRUE remove an insert all
     */
    public void obtenerDatos(boolean forzar){
        comprobarRecursos();

        //Get frecuency preference
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String frequencia=prefs.getString("frecuencia_actualizar_cliente",activity.getResources().getInteger(R.integer.actualizacion_defecto)+"");

        //Check if date in preferences is greater than now
        String ahora = Utilidades.fechaCadena(new Date());
        String ultimaActualizacion = prefs.getString("ultima_actualizacion", "01/01/1990");
        if(forzar){
            ultimaActualizacion="01/01/1990";
        }
        if (ahora.equals(ultimaActualizacion)) {
            return;
        }

        //If now is greater than date in preferences update the date of preferences
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString("ultima_actualizacion",ahora);
        editor.commit();

        obtenerClientes(ultimaActualizacion,forzar);
        obtenerArticulos(ultimaActualizacion,forzar);
    }

    private void obtenerClientes(String fecha, boolean forzar){
        //Update the customers
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("operacion","obtener_clientes_mod");
        hashMap.put("fecha",fecha);

        Conexion conn;
        if(forzar) {
            conn = new Conexion(this.activity, this, this.recurso, Utilidades.OBTENER_CLIENTES_NUEVO);
        }
        else{
            conn = new Conexion(this.activity, this, this.recurso, Utilidades.OBTENER_CLIENTES_ACTUALIZAR);
        }
        conn.execute(hashMap);
    }

    private void obtenerArticulos(String fecha, boolean forzar){
        //Update the customers
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("operacion","obtener_articulos_mod");
        hashMap.put("fecha",fecha);

        Conexion conn;
        if(forzar) {
            conn = new Conexion(this.activity, this, this.recurso, Utilidades.OBTENER_ARTICULOS_NUEVO);
        }
        else{
            conn = new Conexion(this.activity, this, this.recurso, Utilidades.OBTENER_ARTICULOS_ACTUALIZAR);
        }
        conn.execute(hashMap);
    }

    @Override
    public void respuesta(int tipo,String respuesta) {
        switch (tipo){
            case Utilidades.ERROR_CONEXION:{
                Utilidades.Alerts(activity,null,activity.getResources().getString(R.string.conn_err),Utilidades.TIPO_ADVERTENCIA_NEUTRAL,null);
            }
            case Utilidades.OBTENER_CLIENTES_NUEVO:{
                DBAsyncTask dbAsyncTask=new DBAsyncTask(activity);
                dbAsyncTask.guardar(respuesta,true);
                break;
            }
            case Utilidades.OBTENER_CLIENTES_ACTUALIZAR:{
                DBAsyncTask dbAsyncTask=new DBAsyncTask(activity);
                dbAsyncTask.guardar(respuesta,false);
                break;
            }
            case Utilidades.OBTENER_ARTICULOS_NUEVO:{
                DBAsyncTask dbAsyncTask=new DBAsyncTask(activity);
                dbAsyncTask.guardar(respuesta,true);
                break;
            }
            case Utilidades.OBTENER_ARTICULOS_ACTUALIZAR:{
                DBAsyncTask dbAsyncTask=new DBAsyncTask(activity);
                dbAsyncTask.guardar(respuesta,false);
            }
            default:{
                break;
            }
        }
    }

    /**
     * With a JSON of customers makes a database table of customers
     * @param jsonClientes
     * @param forzar If is TRUE remove an insert all
     */
    private void procesarClientes(String jsonClientes,boolean forzar){
        try {
            if (forzar){
                clienteDB.truncate();
            }
            JSONObject jsonObject=new JSONObject(jsonClientes);
            JSONArray jsonArray=jsonObject.getJSONArray("clientes");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jObj=jsonArray.getJSONObject(i);
                int codigo=jObj.getInt("CODIGO");
                String nombre=jObj.getString("NOMBRE");
                if(forzar) {
                    clienteDB.insertar(codigo, nombre);
                }
                else{
                    clienteDB.reemplazar(codigo,nombre);
                }
            }
            Utilidades.crearSnackBar(activity,"Clientes actualizados "+jsonArray.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
