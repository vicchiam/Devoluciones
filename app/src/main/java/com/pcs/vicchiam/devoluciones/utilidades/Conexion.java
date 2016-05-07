package com.pcs.vicchiam.devoluciones.utilidades;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.interfaces.RespuestaServidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vicchiam on 05/05/2016.
 *
 * Class task is crete connection with HTTP method POST with the server and get the response, in background *
 */
public class Conexion extends AsyncTask<HashMap<String,String>,Void,String>{

    private Activity activity;
    private RespuestaServidor respuestaServidor;
    private String recurso;
    private int tipo;

    private ProgressDialog dialog;

    /**
     * Constructor of class Conexion
     * @param activity the parent activity
     * @param respuestaServidor the class thet get the response, this must implement RespuestaServidor
     * @param recurso the complete URL when the server get and response
     * @param tipo the type of call to the server
     */
    public Conexion(Activity activity, RespuestaServidor respuestaServidor, String recurso, int tipo){
        this.activity=activity;
        this.respuestaServidor=respuestaServidor;
        this.recurso=recurso;
        this.tipo=tipo;
        dialog = new ProgressDialog(this.activity);
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setTitle(activity.getResources().getString(R.string.progress_title));
        this.dialog.setMessage(activity.getResources().getString(R.string.progress_msj));
        this.dialog.show();
    }

    @Override
    protected String doInBackground(HashMap<String, String>... params) {
        HashMap<String,String> variables=params[0];
        String res="";
        try {
            res=post(variables);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return res;
    }

    @Override
    protected void onPostExecute(final String respuesta){
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        //The method of the interface RespuestaServidor, need type and response
        if(respuesta.equals("error")){
            this.respuestaServidor.respuesta(Utilidades.ERROR_CONEXION,"");
        }
        else {
            this.respuestaServidor.respuesta(this.tipo, respuesta);
        }
    }

    /**
     * Method that do the conection and recieve the response
     * @param variables the POST variables that send to the server
     * @return the response of the server
     * @throws IOException
     */
    public String post(HashMap<String,String> variables) throws IOException {
        //Make the connection
        URL url = new URL(this.recurso);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.connect();

        //Make the POST
        StringBuilder data = new StringBuilder();
        for (Map.Entry<String, String> variable : variables.entrySet()) {
            data.append("&");
            data.append(URLEncoder.encode(variable.getKey(), "UTF-8"));
            data.append("=");
            data.append(URLEncoder.encode(variable.getValue(), "UTF-8"));
        }

        //Send POST
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(data.toString());
        writer.flush();
        writer.close();
        os.close();

        //Get response
        String respuesta = "";
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = br.readLine()) != null) {
                respuesta += line;
            }
            br.close();
        }
        else{
            throw new IOException("Network error");
        }
        conn.disconnect();
        return respuesta;
    }

}
