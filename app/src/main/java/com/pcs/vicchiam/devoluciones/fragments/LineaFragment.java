package com.pcs.vicchiam.devoluciones.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.zxing.integration.android.IntentIntegrator;
import com.pcs.vicchiam.devoluciones.DevolucionActivity;
import com.pcs.vicchiam.devoluciones.R;

/**
 * Created by vicch on 09/05/2016.
 */
public class LineaFragment extends Fragment {

    private DevolucionActivity self;
    private EditText editCodigo, editNombre, editUmv;
    private ImageButton imgButtonBarcode;

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
            self=(DevolucionActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.linea_fragment,container,false);
        editCodigo=(EditText)view.findViewById(R.id.edit_codigo_art);
        editNombre=(EditText)view.findViewById(R.id.edit_descr_art);
        editUmv=(EditText)view.findViewById(R.id.edit_umv_art);
        imgButtonBarcode=(ImageButton)view.findViewById(R.id.imgbtn_barcode_art);
        imgButtonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(self);
                integrator.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                integrator.initiateScan();
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        self = null;
        super.onDetach();
    }

    public void actualizarLinea(String codigo, String nombre, String umv){
        editCodigo.setText(codigo);
        editNombre.setText(nombre);
        editUmv.setText(umv);
    }

}
