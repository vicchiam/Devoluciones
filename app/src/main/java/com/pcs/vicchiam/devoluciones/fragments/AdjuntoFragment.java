package com.pcs.vicchiam.devoluciones.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.pcs.vicchiam.devoluciones.DevolucionActivity;
import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.adapters.GridAdjAdapter;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

import java.util.ArrayList;
import java.util.List;


public class AdjuntoFragment extends Fragment {

    private DevolucionActivity devolucionActivity;

    private GridView gridView;
    private GridAdjAdapter gridAdapter;

    public static AdjuntoFragment newInstance(Bundle arguments){
        AdjuntoFragment af=new AdjuntoFragment();
        if(arguments!=null){
            af.setArguments(arguments);
        }
        return af;
    }

    public AdjuntoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.adjunto_fragment, container, false);

        gridView=(GridView)view.findViewById(R.id.gridAdj);
        gridAdapter=new GridAdjAdapter(devolucionActivity);
        gridView.setAdapter(gridAdapter);


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DevolucionActivity){
            devolucionActivity =(DevolucionActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void anyadirImagen(String imgPath){
        if(gridAdapter.getCount()>=9){
            Utilidades.Alerts(devolucionActivity,null,getResources().getString(R.string.devol_adj_tamanyo),Utilidades.TIPO_ADVERTENCIA_NEUTRAL,null);
        }
        else {
            gridAdapter.anyadirImagen(imgPath);
        }
    }

}
