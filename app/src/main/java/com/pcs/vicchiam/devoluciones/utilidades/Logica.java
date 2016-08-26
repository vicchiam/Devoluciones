package com.pcs.vicchiam.devoluciones.utilidades;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.pcs.vicchiam.devoluciones.DevolucionActivity;
import com.pcs.vicchiam.devoluciones.MainActivity;
import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.bbdd.ArticuloDB;
import com.pcs.vicchiam.devoluciones.bbdd.ClienteDB;
import com.pcs.vicchiam.devoluciones.bbdd.DBAsyncTask;
import com.pcs.vicchiam.devoluciones.bbdd.Devolucion;
import com.pcs.vicchiam.devoluciones.bbdd.DevolucionDB;
import com.pcs.vicchiam.devoluciones.bbdd.TransporteBD;
import com.pcs.vicchiam.devoluciones.interfaces.RespuestaServidor;

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
    private DevolucionDB devolucionDB;
    private TransporteBD transporteBD;

    //private ProgressDialog progressDialog;

    /*
    public static Logica getInstance(Activity activity){
        if(instancia==null){
            instancia=new Logica(activity);
        }
        return instancia;
    }
    */

    /**
     * Constructor of class Logica
     * @param activity the parent activity
     */
    public Logica(Activity activity){
        this.activity =activity;
        //comprobarRecursos();
        inicializarDatabase();
        obtenerDatos(false);
    }

    /**
     * Create all objects tha represent a database tables
     */
    public void inicializarDatabase(){
        clienteDB=new ClienteDB(this.activity);
        articuloDB=new ArticuloDB(this.activity);
        devolucionDB=new DevolucionDB(this.activity);
        transporteBD=new TransporteBD(this.activity);
    }

    /**
     * Try get of the server all clients according to the frecuency preferences
     * @param forzar If is TRUE remove an insert all
     */
    public void obtenerDatos(boolean forzar){
        this.recurso=Utilidades.comprobarRecursos(activity);
        if(this.recurso==null){
            return;
        }

        //Check if date in preferences is greater than now
        String ahora = Utilidades.fechaCadena(new Date());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String ultimaActualizacion = prefs.getString("ultima_actualizacion", "01/01/1990");
        if(forzar){
            ultimaActualizacion="01/01/1990";
        }
        if (ahora.equals(ultimaActualizacion)) {
            return;
        }

        /*
        //If now is greater than date in preferences update the date of preferences
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString("ultima_actualizacion",ultimaActualizacion);
        editor.commit();
        */

        //Block connection and database access
        Utilidades.SEMAFORO.lock();

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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String fecha=prefs.getString("ultima_actualizacion", "01/01/1990");

        if(tipo==Utilidades.OBTENER_CLIENTES_NUEVO){
            fecha="01/01/1990";
        }

        //Update the customers
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("operacion","obtener_clientes_mod");
        hashMap.put("fecha",fecha);

        Conexion conn = new Conexion(this.activity, this, this.recurso, tipo);
        conn.execute(hashMap);
    }

    /**
     * Start process that get all articles
     * @param tipo
     */
    private void obtenerArticulos(int tipo){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String fecha=prefs.getString("ultima_actualizacion", "01/01/1990");

        if(tipo==Utilidades.OBTENER_ARTICULOS_NUEVO){
            fecha="01/01/1990";
        }

        //Update the customers
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("operacion","obtener_articulos_mod");
        hashMap.put("fecha",fecha);

        Conexion conn = new Conexion(this.activity, this, this.recurso,tipo);
        conn.execute(hashMap);
    }

    public void enviarDevolucion(){
        this.recurso=Utilidades.comprobarRecursos(activity);
        if(this.recurso==null){
            return;
        }

        String devol= Utilidades.devolucion.toJSON();

        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("operacion","guardar_devolucion");
        hashMap.put("datos",devol);

        Log.e("DATOS",devol);

        Conexion conn = new Conexion(this.activity, this, this.recurso,Utilidades.ENVIAR_DEVOLUCION);
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
                Utilidades.Alerts(activity,null, activity.getResources().getString(R.string.database_err),Utilidades.TIPO_ADVERTENCIA_NEUTRAL,null);
                Utilidades.SEMAFORO.unlock();
                break;
            }
            case Utilidades.ERROR_CONEXION:{
                Utilidades.Alerts(activity,null, activity.getResources().getString(R.string.conn_err),Utilidades.TIPO_ADVERTENCIA_NEUTRAL,null);
                Utilidades.SEMAFORO.unlock();
                break;
            }
            case Utilidades.OBTENER_CLIENTES_NUEVO:{
                DBAsyncTask dbAsyncTask=new DBAsyncTask(activity,this,Utilidades.FINALIZAR_CLIENTES_NUEVO);
                dbAsyncTask.guardar(respuesta);
                break;
            }
            case Utilidades.OBTENER_CLIENTES_ACTUALIZAR:{
                DBAsyncTask dbAsyncTask=new DBAsyncTask(activity,this, Utilidades.FINALIZAR_CLIENTES_ACTUALIZAR);
                dbAsyncTask.guardar(respuesta);
                break;
            }
            case Utilidades.OBTENER_ARTICULOS_NUEVO:{
                DBAsyncTask dbAsyncTask=new DBAsyncTask(activity,this, Utilidades.FINALIZAR_ARTICULOS_NUEVO);
                dbAsyncTask.guardar(respuesta);
                break;
            }
            case Utilidades.OBTENER_ARTICULOS_ACTUALIZAR:{
                DBAsyncTask dbAsyncTask=new DBAsyncTask(activity,this, Utilidades.FINALIZAR_ARTICULOS_ACTUALIZAR);
                dbAsyncTask.guardar(respuesta);
                break;
            }
            case Utilidades.FINALIZAR_CLIENTES_NUEVO:{
                MainActivity mActivity=(MainActivity)activity;
                Utilidades.crearSnackBar(mActivity.getCoordinatorLayout(), activity.getResources().getString(R.string.num_clientes,respuesta));
                obtenerArticulos(Utilidades.OBTENER_ARTICULOS_NUEVO);
                break;
            }
            case Utilidades.FINALIZAR_CLIENTES_ACTUALIZAR:{
                MainActivity mActivity=(MainActivity)activity;
                Utilidades.crearSnackBar(mActivity.getCoordinatorLayout(), activity.getResources().getString(R.string.num_clientes,respuesta));
                obtenerArticulos(Utilidades.OBTENER_ARTICULOS_ACTUALIZAR);
                break;
            }
            case Utilidades.FINALIZAR_ARTICULOS_NUEVO:
            case Utilidades.FINALIZAR_ARTICULOS_ACTUALIZAR:{
                MainActivity mActivity=(MainActivity)activity;
                Utilidades.crearSnackBar(mActivity.getCoordinatorLayout(), activity.getResources().getString(R.string.num_articulos,respuesta));

                String ahora = Utilidades.fechaCadena(new Date());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
                SharedPreferences.Editor editor=prefs.edit();
                editor.putString("ultima_actualizacion",ahora);
                //Variable that get if is a first time that app run or not
                editor.putBoolean("appInicializada",true);
                editor.commit();

                //obtenerDevolucionesTrasporte(Utilidades.OBTENER_DEVOLUCIONES_TRANSPORTE);
                Utilidades.SEMAFORO.unlock();
                break;
            }
            /*case Utilidades.OBTENER_DEVOLUCIONES_TRANSPORTE:{
                DBAsyncTask dbAsyncTask=new DBAsyncTask(activity,this, Utilidades.FINALIZAR_DEVOLUCIONES_TRANSPORTE);
                dbAsyncTask.guardar(respuesta);
                break;
            }
            case Utilidades.FINALIZAR_DEVOLUCIONES_TRANSPORTE:{
                MainActivity mActivity=(MainActivity)activity;
                mActivity.actualizar();
                break;
            }*/
            case Utilidades.ENVIAR_DEVOLUCION:{
                DevolucionActivity dActivity=(DevolucionActivity)activity;
                dActivity.finalizarDevolucion(respuesta);
            }
            default:{
                break;
            }
        }
    }

}
