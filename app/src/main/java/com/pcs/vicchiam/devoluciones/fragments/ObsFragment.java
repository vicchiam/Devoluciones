package com.pcs.vicchiam.devoluciones.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.pcs.vicchiam.devoluciones.DevolucionActivity;
import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.bbdd.DevolucionDB;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;


public class ObsFragment extends Fragment {

    private DevolucionActivity devolucionActivity;

    private EditText editObs;
    private String obs_ant;

    public static ObsFragment newInstance(Bundle arguments){
        ObsFragment of=new ObsFragment();
        if(arguments!=null){
            of.setArguments(arguments);
        }
        return of;
    }

    public ObsFragment() {

    }

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.obs_fragment, container, false);
        editObs=(EditText) view.findViewById(R.id.edit_obs);
        editObs.setText(Utilidades.devolucion.getObservacion());
        obs_ant=Utilidades.devolucion.getObservacion();
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

    public void borrar(){
        editObs.setText("");
    }

    public boolean guardar(){
        String obs=editObs.getText().toString();
        if(!obs.equals(this.obs_ant)) {
            Utilidades.devolucion.setObservacion(obs);
            devolucionActivity.ponerCambioObservacion();
        }
        return true;
    }

}
