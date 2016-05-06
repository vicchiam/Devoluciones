package com.pcs.vicchiam.devoluciones;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vicch on 05/05/2016.
 */
public class Comunicacion extends AsyncTask<HashMap<String,String>,Void,String>{

    private final String RECURSO="http://172.16.0.173/Apps/android/devoluciones/repartidor.php";
    private MainActivity padre;

    private ProgressDialog dialog;

    public Comunicacion(MainActivity padre){
        this.padre=padre;
        dialog = new ProgressDialog(this.padre);
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Progress start");
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
        }
        return res;
    }

    @Override
    protected void onPostExecute(final String respuesta){
        Log.e("RESPUESTA",respuesta);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        this.padre.respuesta("eyy",respuesta);
    }

    public String post(HashMap<String,String> variables) throws IOException {
        //Hacemos la conexion
        URL url = new URL(RECURSO);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.connect();

        //Creo el post
        StringBuilder data = new StringBuilder();
        for (Map.Entry<String, String> variable : variables.entrySet()) {
            data.append("&");
            data.append(URLEncoder.encode(variable.getKey(), "UTF-8"));
            data.append("=");
            data.append(URLEncoder.encode(variable.getValue(), "UTF-8"));
        }

        //Envio el post
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(data.toString());
        writer.flush();
        writer.close();
        os.close();

        //Obtengo respuesta
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
        conn.disconnect();
        return respuesta;
    }

}
