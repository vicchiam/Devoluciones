package com.pcs.vicchiam.devoluciones.bbdd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by vicch on 11/05/2016.
 */
public class DBAsyncTask {

    private Activity activity;
    private ClienteDB clienteDB;
    private ArticuloDB articuloDB;
    private boolean forzar;

    public DBAsyncTask(Activity activity){
        this.activity=activity;
        clienteDB=new ClienteDB(activity);
        articuloDB=new ArticuloDB(activity);
        this.forzar=false;
    }

    public void guardar(String json, boolean forzar){
        MyAsyncTask mat=new MyAsyncTask(activity);
        this.forzar=forzar;
        mat.execute(json);
    }

    private class MyAsyncTask extends AsyncTask<String,Void,Integer> {

        private Activity activity;
        private ProgressDialog dialog;


        private MyAsyncTask(Activity activity) {
            this.activity = activity;
            this.dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setTitle(activity.getResources().getString(R.string.progress_title));
            this.dialog.setMessage(activity.getResources().getString(R.string.progress_msj_save));
            this.dialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            String json=params[0];
            int count=0;
            try {
                JSONObject jsonObject=new JSONObject(json);
                if(jsonObject.has("clientes")){
                    if(forzar){
                        clienteDB.truncate();
                    }
                    JSONArray jsonArray=jsonObject.getJSONArray("clientes");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObj=jsonArray.getJSONObject(i);
                        int codigo=jObj.getInt("CODIGO");
                        String nombre=jObj.getString("NOMBRE");
                        clienteDB.reemplazar(codigo,nombre);
                    }
                    count=jsonArray.length();
                }
                if(jsonObject.has("articulos")){
                    if(forzar){
                        articuloDB.truncate();
                    }
                    JSONArray jsonArray=jsonObject.getJSONArray("articulos");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObj=jsonArray.getJSONObject(i);
                        int codigo=jObj.getInt("CODIGO");
                        String nombre=jObj.getString("NOMBRE");
                        String umv=jObj.getString("UMV");
                        articuloDB.reemplazar(codigo, nombre, umv);
                    }
                    count=jsonArray.length();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                count=1;
            }

            return count;
        }

        @Override
        protected void onPostExecute(final Integer respuesta) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(respuesta>=0) {
                Utilidades.crearSnackBar(activity, "Datos actualizados " + respuesta);
            }
            else{
                Utilidades.Alerts(activity,null,"Error al recibir los datos",Utilidades.TIPO_ADVERTENCIA_NEUTRAL,null);
            }
        }
    }




}
