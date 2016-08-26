package com.pcs.vicchiam.devoluciones.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pcs.vicchiam.devoluciones.MainActivity;
import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.adapters.ItemEnviarAdapter;
import com.pcs.vicchiam.devoluciones.bbdd.Devolucion;
import com.pcs.vicchiam.devoluciones.bbdd.DevolucionDB;
import com.pcs.vicchiam.devoluciones.interfaces.RespuestaServidor;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 09/06/2016.
 */
public class EnviarFragment extends Fragment implements RespuestaServidor {

    public static final String ARG_NUM_PAGE = "ARG_NUM_PAGE";
    private int posPage;

    private MainActivity mainActivity;
    private ItemEnviarAdapter itemEnviarAdapter;
    private ListView listView;

    private DevolucionDB db;

    public static EnviarFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_NUM_PAGE, page);
        EnviarFragment eFragment = new EnviarFragment();
        eFragment.setArguments(args);
        return eFragment;
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
        View view = inflater.inflate(R.layout.main_fragment_page2, container, false);

        db=new DevolucionDB(this.getContext());

        List<Devolucion> lista=db.obtenerDevolucionesNoEnviadas();
        itemEnviarAdapter=new ItemEnviarAdapter(this.getContext(),lista);
        listView=(ListView) view.findViewById(R.id.list_enviar);
        listView.setAdapter(itemEnviarAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long _id) {
                long id=itemEnviarAdapter.getListado().get(position).getId();
                mainActivity.abrirDevolucion(id);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(mainActivity)
                        .setTitle("Borrar")
                        .setMessage("¿Seguro que quieres borrar?")
                        .setPositiveButton("Borrar", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                long id=itemEnviarAdapter.getListado().get(position).getId();
                                db.eliminarDevolucion(id);
                                Utilidades.devolucion=null;
                                actualizarListado();
                            }
                        })
                        .setNeutralButton("Cancelar",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
            }
        });

        return view;
    }

    @Override
    public void respuesta(int tipo, String respuesta) {

    }

    public void actualizarListado(){
        if(listView!=null){
            List<Devolucion> lista=db.obtenerDevolucionesNoEnviadas();
            itemEnviarAdapter.setListado(lista);
            listView.invalidateViews();
        }
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            actualizarListado();
        }
    }

}
