package com.pcs.vicchiam.devoluciones.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pcs.vicchiam.devoluciones.R;

/**
 * Created by vicch on 07/05/2016.
 */
public class CabeceraFragment extends Fragment {



    public CabeceraFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.devolucion_cabecera_fragment,container,false);

        return view;
    }

}
