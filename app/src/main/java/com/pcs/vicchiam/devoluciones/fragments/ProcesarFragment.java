package com.pcs.vicchiam.devoluciones.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.pcs.vicchiam.devoluciones.MainActivity;
import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.adapters.ItemProcesarAdapter;
import com.pcs.vicchiam.devoluciones.bbdd.DBAsyncTask;
import com.pcs.vicchiam.devoluciones.bbdd.Transporte;
import com.pcs.vicchiam.devoluciones.bbdd.TransporteBD;
import com.pcs.vicchiam.devoluciones.interfaces.RespuestaServidor;
import com.pcs.vicchiam.devoluciones.utilidades.Conexion;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by vicch on 31/05/2016.
 */
public class ProcesarFragment extends Fragment implements RespuestaServidor {

    public static final String ARG_NUM_PAGE = "ARG_NUM_PAGE";
    private int posPage;

    private MainActivity mainActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemProcesarAdapter itemProcesarAdapter;
    private ListView listView;

    private TransporteBD transporteBD;

    public static ProcesarFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_NUM_PAGE, page);
        ProcesarFragment pFragment = new ProcesarFragment();
        pFragment.setArguments(args);
        return pFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            mainActivity =(MainActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posPage = getArguments().getInt(ARG_NUM_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_page1, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_procesar);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actualizar();
            }
        });

        List<Transporte> lista=new ArrayList<>();
        itemProcesarAdapter = new ItemProcesarAdapter(this.getContext(), lista);
        listView = (ListView) view.findViewById(R.id.list_procesar);
        listView.setAdapter(itemProcesarAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Transporte t=(Transporte) itemProcesarAdapter.getItem(position);
                mainActivity.abrirDevolucion(t.getCliente(),t.getNombre(),t.getId());
            }

        });

        obtenerDatos();

        return view;
    }

    private void obtenerDatos(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        if(prefs.getBoolean("appInicializada",false) && !Utilidades.SEMAFORO.isLock()){
            if(transporteBD==null){
                transporteBD=new TransporteBD(this.getContext());
            }
            try {
                List<Transporte> lista = transporteBD.obtenerTransporte();
                itemProcesarAdapter.setListado(lista);
                listView.invalidateViews();
            }
            catch(Exception e){

            }
        }
    }

    private void actualizar(){
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("operacion","obtener_transportes");

        String recurso= Utilidades.comprobarRecursos(this.mainActivity);

        if(recurso==null){
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        Conexion conn = new Conexion(mainActivity, this, recurso, Utilidades.OBTENER_DEVOLUCIONES_TRANSPORTE_LISTADO);
        conn.execute(hashMap);
    }

    public void actualizarListado(){
        if(listView!=null) {
            obtenerDatos();
        }
    }

    @Override
    public void respuesta(int tipo, String respuesta) {
        Log.e("TRANS",respuesta);
        switch (tipo) {
            case Utilidades.ERROR_BASE_DATOS: {
                Utilidades.Alerts(mainActivity, null, mainActivity.getResources().getString(R.string.database_err), Utilidades.TIPO_ADVERTENCIA_NEUTRAL, null);
                swipeRefreshLayout.setRefreshing(false);
                break;
            }
            case Utilidades.ERROR_CONEXION: {
                Utilidades.Alerts(mainActivity, null, mainActivity.getResources().getString(R.string.conn_err), Utilidades.TIPO_ADVERTENCIA_NEUTRAL, null);
                swipeRefreshLayout.setRefreshing(false);
                break;
            }
            case Utilidades.OBTENER_DEVOLUCIONES_TRANSPORTE_LISTADO:{
                DBAsyncTask dbAsyncTask=new DBAsyncTask(mainActivity,this, Utilidades.FINALIZAR_DEVOLUCIONES_TRANSPORTE_LISTADO);
                dbAsyncTask.guardar(respuesta);
                break;
            }
            case Utilidades.FINALIZAR_DEVOLUCIONES_TRANSPORTE_LISTADO:{
                actualizarListado();
                swipeRefreshLayout.setRefreshing(false);
                break;
            }
        }
    }

}
