package com.pcs.vicchiam.devoluciones.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.interfaces.RespuestaServidor;

/**
 * Created by victor on 09/06/2016.
 */
public class HistoricoFragment extends Fragment implements RespuestaServidor {

    public static final String ARG_NUM_PAGE = "ARG_NUM_PAGE";
    private int posPage;

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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_page3, container, false);
        return view;
    }

    @Override
    public void respuesta(int tipo, String respuesta) {

    }
}
