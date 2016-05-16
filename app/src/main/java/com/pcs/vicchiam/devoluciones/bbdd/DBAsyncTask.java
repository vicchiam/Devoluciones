package com.pcs.vicchiam.devoluciones.bbdd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.interfaces.RespuestaServidor;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vicch on 11/05/2016.
 */
public class DBAsyncTask {

    private Activity activity;
    private RespuestaServidor respuestaServidor;
    private ClienteDB clienteDB;
    private ArticuloDB articuloDB;
    private boolean forzar;
    private int tipo;

    public DBAsyncTask(Activity activity, RespuestaServidor respuestaServidor, int tipo){
        this.activity=activity;
        this.respuestaServidor=respuestaServidor;
        this.tipo=tipo;
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
            int count=-1;
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
                    if(forzar){
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
            } catch (JSONException e) {
                e.printStackTrace();
                count=-1;
            }

            return count;
        }

        @Override
        protected void onPostExecute(final Integer respuesta) {
            if (dialog.isShowing()) {
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
