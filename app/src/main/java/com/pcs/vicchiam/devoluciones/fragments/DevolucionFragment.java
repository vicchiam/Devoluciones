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
import android.widget.EditText;
import android.widget.ListView;

import com.pcs.vicchiam.devoluciones.DevolucionActivity;
import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.adapters.ItemLineaAdapter;
import com.pcs.vicchiam.devoluciones.bbdd.Devolucion;
import com.pcs.vicchiam.devoluciones.bbdd.DevolucionDB;
import com.pcs.vicchiam.devoluciones.bbdd.Linea;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

/**
 * Created by vicchiam on 07/05/2016.
 * Class that make a fragment with the data of a devolution
 */
public class DevolucionFragment extends Fragment {

    private DevolucionActivity devolucionActivity;
    private EditText editCodigo, editRazon;
    private ItemLineaAdapter itemLineaAdapter;
    private ListView listView;

    private DevolucionDB db;

    /**
     * Create a object Cabecera frgament
     * @param arguments id of the devolution
     * @return a object DevolucionFragment
     */
    public static DevolucionFragment newInstance(Bundle arguments){
        DevolucionFragment cf=new DevolucionFragment();
        if(arguments != null){
            cf.setArguments(arguments);
        }
        return cf;
    }

    public DevolucionFragment(){}

    /**
     * When fragment is created get id of the devolution
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DevolucionActivity){
            devolucionActivity =(DevolucionActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.devolucion_fragment,container,false);

        db = new DevolucionDB(this.devolucionActivity);

        editCodigo=(EditText) view.findViewById(R.id.edit_codigo);
        editRazon=(EditText) view.findViewById(R.id.edit_razon);

        editCodigo.setText(Utilidades.devolucion.getCodigo());
        editRazon.setText(Utilidades.devolucion.getNombre());

        listView=(ListView) view.findViewById(R.id.list_lineas);
        itemLineaAdapter=new ItemLineaAdapter(devolucionActivity, Utilidades.devolucion.getLineas());
        listView.setAdapter(itemLineaAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                bundle.putInt("pos",position);
                devolucionActivity.abrirLinea(bundle);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(devolucionActivity)
                        .setTitle("Borrar")
                        .setMessage("¿Seguro que quieres borrar?")
                        .setPositiveButton("Borrar", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utilidades.devolucion.eliminarLinea(position,db);
                                refresh();
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

        llenarDevolucion();

        return view;
    }

    @Override
    public void onDetach() {
        devolucionActivity = null;
        super.onDetach();
    }

    /**
     * When exit the fragment check if have some change if have a change, show a alert dialog
     */
    public void perderCambios(){
        Devolucion nueva=leerDevolucion();
        if(Utilidades.devolucion.tieneCambios(nueva)){
            Utilidades.Alerts(devolucionActivity,null,getResources().getString(R.string.descartar_cambios),Utilidades.TIPO_ADVERTENCIA_SI_NO, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    devolucionActivity.finish();
                }
            });
        }
        else {
            devolucionActivity.finish();
        }
    }

    public void llenarDevolucion(){
        editCodigo.setText(Utilidades.devolucion.getCodigo());
        editRazon.setText(Utilidades.devolucion.getNombre());
        refresh();
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

    public Devolucion leerDevolucion(){
        Devolucion devolucion=new Devolucion();
        devolucion.setCodigo(editCodigo.getText().toString());
        devolucion.setNombre(editRazon.getText().toString());
        return devolucion;
    }

    /**
     * Save in a object Devolucion the data of the editText
     */
    public boolean guardar() {

        String codigo = editCodigo.getText().toString();
        String nombre = editRazon.getText().toString();

        if (codigo.equals("")){
            Utilidades.Alerts(devolucionActivity, null, getResources().getString(R.string.devol_e_codigo), Utilidades.TIPO_ADVERTENCIA_NEUTRAL, null);
            return false;
        }
        if(Utilidades.devolucion.getLineas().size()==0){
            Utilidades.Alerts(devolucionActivity, null, getResources().getString(R.string.devol_e_linea), Utilidades.TIPO_ADVERTENCIA_NEUTRAL, null);
            return false;
        }

        Utilidades.devolucion.setCodigo(codigo);
        Utilidades.devolucion.setNombre(nombre);
        if(Utilidades.devolucion.getId()==0){
            Utilidades.devolucion.agregar(db);
        }
        else if(Utilidades.devolucion.getId()>0){
            Utilidades.devolucion.modificar(db);
        }
        return true;
    }

    /**
     * Refresh a listview of lines
     */
    public void refresh(){
        listView.invalidateViews();
    }

}
