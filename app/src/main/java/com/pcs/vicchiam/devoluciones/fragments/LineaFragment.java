package com.pcs.vicchiam.devoluciones.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.pcs.vicchiam.devoluciones.DevolucionActivity;
import com.pcs.vicchiam.devoluciones.R;

/**
 * Created by vicch on 09/05/2016.
 */
public class LineaFragment extends Fragment {

    private DevolucionActivity activity;

    public static LineaFragment newInstance(Bundle arguments){
        LineaFragment lf=new LineaFragment();
        if(arguments != null){
            lf.setArguments(arguments);
        }
        return lf;
    }

    public LineaFragment(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DevolucionActivity){
            activity=(DevolucionActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.linea_fragment,container,false);
        return view;
    }

    @Override
    public void onDetach() {
        activity = null;
        super.onDetach();
    }

}
