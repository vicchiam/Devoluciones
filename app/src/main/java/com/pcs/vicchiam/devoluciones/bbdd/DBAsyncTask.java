package com.pcs.vicchiam.devoluciones.bbdd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.interfaces.RespuestaServidor;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vicchiam on 11/05/2016.
 * Make a class to make operation to database in async mode
 */
public class DBAsyncTask {

    private Activity activity;
    private RespuestaServidor respuestaServidor;
    private ClienteDB clienteDB;
    private ArticuloDB articuloDB;
    private TransporteBD transporteBD;
    private DevolucionDB devolucionDB;
    private int tipo;

    /**
     * Constructor
     * @param activity Parent activity
     * @param respuestaServidor Object that to recieve a response of task
     * @param tipo Type of operation
     */
    public DBAsyncTask(Activity activity, RespuestaServidor respuestaServidor, int tipo){
        this.activity=activity;
        this.respuestaServidor=respuestaServidor;
        this.tipo=tipo;
        clienteDB=new ClienteDB(activity);
        articuloDB=new ArticuloDB(activity);
        devolucionDB=new DevolucionDB(activity);
        transporteBD=new TransporteBD(activity);
    }

    /**
     * Methot that start a process of save data in database
     * @param json
     */
    public void guardar(String json){
        MyAsyncTask mat=new MyAsyncTask(activity);
        mat.execute(json);
    }

    /**
     * Class that make a async task
     */
    private class MyAsyncTask extends AsyncTask<String,Void,Integer> {

        private Activity activity;
        private ProgressDialog dialog;


        private MyAsyncTask(Activity activity) {
            this.activity = activity;
            this.dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            if(tipo!=Utilidades.FINALIZAR_DEVOLUCIONES_TRANSPORTE_LISTADO) {
                this.dialog.setTitle(activity.getResources().getString(R.string.progress_title));
                this.dialog.setMessage(activity.getResources().getString(R.string.progress_msj_save));
                this.dialog.show();
            }
        }

        /**
         * Save data in a database
         * @param params A string json
         * @return A number of results
         */
        @Override
        protected Integer doInBackground(String... params) {
            String json=params[0];
            int count=-1;
            try {
                JSONObject jsonObject=new JSONObject(json);
                if(jsonObject.has("clientes")){
                    if(tipo==Utilidades.FINALIZAR_CLIENTES_NUEVO){
                        clienteDB.truncate();
                    }
                    JSONArray jsonArray=jsonObject.getJSONArray("clientes");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObj=jsonArray.getJSONObject(i);
                        int codigo=jObj.getInt("CODIGO");
                        String nombre=jObj.getString("NOMBRE");
                        String estado=jObj.getString("ESTADO");
                        if(estado.equals("A")) {
                            clienteDB.reemplazar(codigo, nombre);
                        }
                        else if(estado.equals("P")){
                            clienteDB.eliminar(codigo);
                        }
                    }
                    count=jsonArray.length();
                }
                if(jsonObject.has("articulos")){
                    if(tipo==Utilidades.FINALIZAR_ARTICULOS_NUEVO){
                        articuloDB.truncate();
                    }
                    JSONArray jsonArray=jsonObject.getJSONArray("articulos");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObj=jsonArray.getJSONObject(i);
                        int codigo=jObj.getInt("CODIGO");
                        String nombre=jObj.getString("NOMBRE");
                        String umv=jObj.getString("UMV");
                        String estado=jObj.getString("ESTADO");
                        if(estado.equals("A")) {
                            articuloDB.reemplazar(codigo, nombre, umv);
                        }
                        else  if(estado.equals("P")){
                            articuloDB.eliminar(codigo);
                        }
                    }
                    count=jsonArray.length();
                }
                if(jsonObject.has("devoluciones")){
                    transporteBD.truncate();
                    JSONArray jsonArray=jsonObject.getJSONArray("devoluciones");
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        long id=jObj.getLong("id");
                        String cliente=jObj.getString("cliente");
                        String nombre=jObj.getString("nombre");
                        String fecha=jObj.getString("fecha");
                        String obs=jObj.getString("obs");
                        String obs_cal=jObj.getString("com_calidad");
                        String observacion=obs+"CALIDAD:"+obs_cal;
                        try {
                            if (!devolucionDB.existeTrasporte(id)) {
                                transporteBD.insertarTransporte(id, cliente, nombre, fecha, observacion);
                            }
                        }
                        catch (Exception e){
                            Log.e("EXC",e.getMessage());
                        }
                    }
                    count=jsonArray.length();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                count=-1;
            }
            return count;
        }

        /**
         * Get the final result and pass to the objecr RespuestaServidor
         * @param respuesta
         */
        @Override
        protected void onPostExecute(final Integer respuesta) {
            if (tipo!=Utilidades.FINALIZAR_DEVOLUCIONES_TRANSPORTE_LISTADO && dialog.isShowing()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
            if(respuesta>=0) {
                respuestaServidor.respuesta(tipo,respuesta+"");
            }
            else{
                respuestaServidor.respuesta(Utilidades.ERROR_BASE_DATOS,"");
            }
        }
    }

}
