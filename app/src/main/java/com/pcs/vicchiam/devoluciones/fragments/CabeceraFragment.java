package com.pcs.vicchiam.devoluciones.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pcs.vicchiam.devoluciones.DevolucionActivity;
import com.pcs.vicchiam.devoluciones.R;

/**
 * Created by vicch on 07/05/2016.
 */
public class CabeceraFragment extends Fragment {

    private DevolucionActivity activity;
    private EditText editCodigo, editRazon;
    private Button nuevaLinea;

    public static CabeceraFragment newInstance(Bundle arguments){
        CabeceraFragment cf=new CabeceraFragment();
        if(arguments != null){
            cf.setArguments(arguments);
        }
        return cf;
    }

    public CabeceraFragment(){}


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
        View view=inflater.inflate(R.layout.devolucion_fragment,container,false);
        editCodigo=(EditText) view.findViewById(R.id.edit_codigo);
        editRazon=(EditText) view.findViewById(R.id.edit_razon);
        nuevaLinea=(Button) view.findViewById(R.id.btn_nueva_linea);
        nuevaLinea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.abrirLinea();
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        activity = null;
        super.onDetach();
    }

    public void actualizarCabecera(String codigo, String razon){
        editCodigo.setText(codigo);
        editRazon.setText(razon);
    }

}
