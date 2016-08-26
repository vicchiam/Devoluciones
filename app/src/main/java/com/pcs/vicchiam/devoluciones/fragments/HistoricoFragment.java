package com.pcs.vicchiam.devoluciones.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pcs.vicchiam.devoluciones.MainActivity;
import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.adapters.ItemHistAdapter;
import com.pcs.vicchiam.devoluciones.bbdd.Devolucion;
import com.pcs.vicchiam.devoluciones.bbdd.DevolucionDB;
import com.pcs.vicchiam.devoluciones.interfaces.RespuestaServidor;

import java.util.List;

/**
 * Created by victor on 09/06/2016.
 */
public class HistoricoFragment extends Fragment implements RespuestaServidor {

    public static final String ARG_NUM_PAGE = "ARG_NUM_PAGE";
    private int posPage;

    private MainActivity mainActivity;
    private ItemHistAdapter adapter;
    private RecyclerView recyclerView;

    private DevolucionDB db;

    public static HistoricoFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_NUM_PAGE, page);
        HistoricoFragment hFragment = new HistoricoFragment();
        hFragment.setArguments(args);
        return hFragment;
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
        View view = inflater.inflate(R.layout.main_fragment_page3, container, false);

        db=new DevolucionDB(this.getContext());
        List<Devolucion> list=db.obtenerDevolucionesEnviadas();
        adapter=new ItemHistAdapter(this.getContext(),list);
        adapter.SetOnItemClickListener(new ItemHistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long id) {
                mainActivity.abrirDevolucion(id);
            }
        });

        recyclerView=(RecyclerView) view.findViewById(R.id.list_hist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void respuesta(int tipo, String respuesta) {

    }

    public void actualizarListado(){
        adapter.setListado(db.obtenerDevolucionesEnviadas());
        adapter.notifyDataSetChanged();
    }

}
