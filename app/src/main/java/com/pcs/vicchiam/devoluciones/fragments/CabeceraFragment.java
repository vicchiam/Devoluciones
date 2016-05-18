package com.pcs.vicchiam.devoluciones.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.pcs.vicchiam.devoluciones.DevolucionActivity;
import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.adapters.ItemLineaAdapter;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

/**
 * Created by vicchiam on 07/05/2016.
 * Class that make a fragment with the data of a devolution
 */
public class CabeceraFragment extends Fragment {

    private DevolucionActivity activity;
    private EditText editCodigo, editRazon;
    private Button nuevaLinea;
    private ItemLineaAdapter itemLineaAdapter;
    private ListView listView;

    /**
     * Create a object Cabecera frgament
     * @param arguments id of the devolution
     * @return a object CabeceraFragment
     */
    public static CabeceraFragment newInstance(Bundle arguments){
        CabeceraFragment cf=new CabeceraFragment();
        if(arguments != null){
            cf.setArguments(arguments);
        }
        return cf;
    }

    public CabeceraFragment(){}

    /**
     * When fragment is created get id of the devolution
     * @param context
     */
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
                activity.abrirLineaNueva();
            }
        });
        listView=(ListView) view.findViewById(R.id.list_lineas);
        itemLineaAdapter=new ItemLineaAdapter(activity, Utilidades.devolucion.getLineas());
        listView.setAdapter(itemLineaAdapter);
        return view;
    }

    @Override
    public void onDetach() {
        activity = null;
        super.onDetach();
    }

    /**
     * Put data in the fragment edittext
     * @param codigo
     * @param razon
     */
    public void actualizarCabecera(String codigo, String razon){
        editCodigo.setText(codigo);
        editRazon.setText(razon);
    }

    /**
     * Save in a object Devolucion the data of the editText
     */
    public void guardar(){

    }

    /**
     * Refresh a listview of lines
     */
    public void refresh(){
        listView.invalidateViews();
    }

}
