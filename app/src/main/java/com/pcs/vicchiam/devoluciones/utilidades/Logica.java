package com.pcs.vicchiam.devoluciones.utilidades;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.pcs.vicchiam.devoluciones.MainActivity;
import com.pcs.vicchiam.devoluciones.PreferenciasBarActivity;
import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.bbdd.ArticuloDB;
import com.pcs.vicchiam.devoluciones.bbdd.ClienteDB;
import com.pcs.vicchiam.devoluciones.bbdd.DBAsyncTask;
import com.pcs.vicchiam.devoluciones.bbdd.DevolucionDB;
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

    private MainActivity mainActivity;
    private String recurso;
    private ClienteDB clienteDB;
    private ArticuloDB articuloDB;
    private DevolucionDB devolucionDB;

    ProgressDialog progressDialog;

    public static Logica getInstance(MainActivity mainActivity){
        if(instancia==null){
            instancia=new Logica(mainActivity);
        }
        return instancia;
    }

    /**
     * Constructor of class Logica
     * @param mainActivity the parent activity
     */
    public Logica(MainActivity mainActivity){
        this.mainActivity=mainActivity;
        //comprobarRecursos();
        inicializarDatabase();
        obtenerDatos(false);
    }

    /**
     * Create all objects tha represent a database tables
     */
    public void inicializarDatabase(){
        clienteDB=new ClienteDB(this.mainActivity);
        articuloDB=new ArticuloDB(this.mainActivity);
        devolucionDB=new DevolucionDB(this.mainActivity);
    }

    /**
     * Check if the pref values are OK and if WIFI ssid is the same that on the preferences
     */
    public boolean comprobarRecursos(){
        //Get the preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        String miWifi=prefs.getString("ssid",mainActivity.getResources().getString(R.string.wifi_defecto));
        String servidor=prefs.getString("servidor",mainActivity.getResources().getString(R.string.servidor_defecto));
        String servicio=prefs.getString("servicio",mainActivity.getResources().getString(R.string.servicio_defecto));

        //Check if wifi preference is right
        if(miWifi.equals("")){
            Utilidades.Alerts(mainActivity,null,mainActivity.getResources().getString(R.string.no_wifi_pref),Utilidades.TIPO_ADVERTENCIA_CONFIGURACION,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent=new Intent(mainActivity, PreferenciasBarActivity.class);
                    mainActivity.startActivity(intent);
                }
            });
            return false;
        }
        //Check if the server preference is rigth
        if(servidor.equals("")){
            Utilidades.Alerts(mainActivity,null,mainActivity.getResources().getString(R.string.no_servidor_pref),Utilidades.TIPO_ADVERTENCIA_CONFIGURACION,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent=new Intent(mainActivity, PreferenciasBarActivity.class);
                    mainActivity.startActivity(intent);
                }
            });
            return false;
        }

        //Check if the service preference is right
        if(servicio.equals("")){
            Utilidades.Alerts(mainActivity,null,mainActivity.getResources().getString(R.string.no_servicio_pref),Utilidades.TIPO_ADVERTENCIA_CONFIGURACION,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent=new Intent(mainActivity, PreferenciasBarActivity.class);
                    mainActivity.startActivity(intent);
                }
            });
            return false;
        }

        //Check if the WIFI ssid is the same that on the preferences
        if(!Utilidades.Wifi(this.mainActivity,miWifi)) {
            Utilidades.Alerts(mainActivity, null, mainActivity.getResources().getString(R.string.no_wifi_conn, miWifi), Utilidades.TIPO_ADVERTENCIA_NEUTRAL, null);
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
        if(!comprobarRecursos()){
            return;
        }

        //Check if date in preferences is greater than now
        String ahora = Utilidades.fechaCadena(new Date());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        String ultimaActualizacion = prefs.getString("ultima_actualizacion", "01/01/1990");
        if(forzar){
            ultimaActualizacion="01/01/1990";
        }
        if (ahora.equals(ultimaActualizacion)) {
            return;
        }

        //If now is greater than date in preferences update the date of preferences
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString("ultima_actualizacion",ultimaActualizacion);
        editor.commit();

        if(forzar || ultimaActualizacion.equals("01/01/1990")) {
            obtenerClientes(Utilidades.OBTENER_CLIENTES_NUEVO);
        }
        else{
            obtenerClientes(Utilidades.OBTENER_CLIENTES_ACTUALIZAR);
        }
    }

    /**
     * Start process that get all customers
     * @param tipo
     */
    private void obtenerClientes(int tipo){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        String fecha=prefs.getString("ultima_actualizacion", "01/01/1990");

        //Update the customers
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("operacion","obtener_clientes_mod");
        hashMap.put("fecha",fecha);

        Conexion conn = new Conexion(this.mainActivity, this, this.recurso, tipo);
        conn.execute(hashMap);
    }

    /**
     * Start process that get all articles
     * @param tipo
     */
    private void obtenerArticulos(int tipo){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        String fecha=prefs.getString("ultima_actualizacion", "01/01/1990");

        //Update the customers
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("operacion","obtener_articulos_mod");
        hashMap.put("fecha",fecha);

        Conexion conn = new Conexion(this.mainActivity, this, this.recurso,tipo);
        conn.execute(hashMap);
    }

    /**
     * This function get all responses from Conecion and process these.
     * @param tipo
     * @param respuesta
     */
    @Override
    public void respuesta(int tipo,String respuesta) {
        switch (tipo){
            case Utilidades.ERROR_BASE_DATOS:{
                Utilidades.Alerts(mainActivity,null,mainActivity.getResources().getString(R.string.database_err),Utilidades.TIPO_ADVERTENCIA_NEUTRAL,null);
                break;
            }
            case Utilidades.ERROR_CONEXION:{
                Utilidades.Alerts(mainActivity,null,mainActivity.getResources().getString(R.string.conn_err),Utilidades.TIPO_ADVERTENCIA_NEUTRAL,null);
                break;
            }
            case Utilidades.OBTENER_CLIENTES_NUEVO:{
                DBAsyncTask dbAsyncTask=new DBAsyncTask(mainActivity,this,Utilidades.FINALIZAR_CLIENTES_NUEVO);
                dbAsyncTask.guardar(respuesta,true);
                break;
            }
            case Utilidades.OBTENER_CLIENTES_ACTUALIZAR:{
                DBAsyncTask dbAsyncTask=new DBAsyncTask(mainActivity,this, Utilidades.FINALIZAR_CLIENTES_ACTUALIZAR);
                dbAsyncTask.guardar(respuesta,false);
                break;
            }
            case Utilidades.OBTENER_ARTICULOS_NUEVO:{
                DBAsyncTask dbAsyncTask=new DBAsyncTask(mainActivity,this, Utilidades.FINALIZAR_ARTICULOS);
                dbAsyncTask.guardar(respuesta,true);
                break;
            }
            case Utilidades.OBTENER_ARTICULOS_ACTUALIZAR:{
                DBAsyncTask dbAsyncTask=new DBAsyncTask(mainActivity,this, Utilidades.FINALIZAR_ARTICULOS);
                dbAsyncTask.guardar(respuesta,false);
            }
            case Utilidades.FINALIZAR_CLIENTES_NUEVO:{
                Utilidades.crearSnackBar(mainActivity.getCoordinatorLayout(), mainActivity.getResources().getString(R.string.num_clientes,respuesta));
                obtenerArticulos(Utilidades.OBTENER_ARTICULOS_NUEVO);
                break;
            }
            case Utilidades.FINALIZAR_CLIENTES_ACTUALIZAR:{
                Utilidades.crearSnackBar(mainActivity.getCoordinatorLayout(), mainActivity.getResources().getString(R.string.num_clientes,respuesta));
                obtenerArticulos(Utilidades.OBTENER_ARTICULOS_ACTUALIZAR);
                break;
            }
            case Utilidades.FINALIZAR_ARTICULOS:{
                Utilidades.crearSnackBar(mainActivity.getCoordinatorLayout(), mainActivity.getResources().getString(R.string.num_articulos,respuesta));

                String ahora = Utilidades.fechaCadena(new Date());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);
                SharedPreferences.Editor editor=prefs.edit();
                editor.putString("ultima_actualizacion",ahora);
                editor.commit();
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
            Utilidades.crearSnackBar(mainActivity.getCoordinatorLayout(),"Clientes actualizados "+jsonArray.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
