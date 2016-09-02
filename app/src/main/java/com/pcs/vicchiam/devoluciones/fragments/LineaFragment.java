package com.pcs.vicchiam.devoluciones.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.pcs.vicchiam.devoluciones.DevolucionActivity;
import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.bbdd.Devolucion;
import com.pcs.vicchiam.devoluciones.bbdd.DevolucionDB;
import com.pcs.vicchiam.devoluciones.bbdd.Linea;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

import java.util.Calendar;

/**
 * Created by vicchiam on 09/05/2016.
 * Class that make a fragment that represent a line
 */
public class LineaFragment extends Fragment {

    private DevolucionActivity devolucionActivity;
    private EditText editCodigo, editNombre,editCantidad, editUmv, editLote;
    private TextView textCaducidad, textAccion, textMotivo;
    private ImageView imgCaducidad;
    private RelativeLayout layoutCaducidad, layoutAccion, layoutMotivo;
    private int position;
    private Linea linea;

    private DevolucionDB db;

    /**
     * Create instance on LineaFragment
     * @param arguments id of line
     * @return a Linefragment object
     */
    public static LineaFragment newInstance(Bundle arguments){
        LineaFragment lf=new LineaFragment();
        if(arguments != null){
            lf.setArguments(arguments);
        }
        return lf;
    }

    public LineaFragment(){}

    /**
     * When fragment is created get a id of line if id is 0 is a new line, other is a exitent line
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        position=-1;
        if(context instanceof DevolucionActivity){
            devolucionActivity =(DevolucionActivity) context;
        }
        if(getArguments()!=null && getArguments().containsKey("pos")){
            position=getArguments().getInt("pos");
        }
        if(position>=0){
            linea=Utilidades.devolucion.getLineas().get(position);
        }
        else{
            linea=new Linea();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.linea_fragment,container,false);

        db=new DevolucionDB(devolucionActivity);

        editCodigo=(EditText)view.findViewById(R.id.edit_codigo_art);
        editNombre=(EditText)view.findViewById(R.id.edit_descr_art);
        editCantidad=(EditText)view.findViewById(R.id.edit_cantidad_art);
        editUmv=(EditText)view.findViewById(R.id.edit_umv_art);
        editLote=(EditText)view.findViewById(R.id.edit_lote);
        textCaducidad=(TextView)view.findViewById(R.id.edit_caducidad);
        layoutCaducidad=(RelativeLayout)view.findViewById(R.id.layoutCaducidadArt);
        layoutCaducidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final int year = c.get(Calendar.YEAR);
                final int month = c.get(Calendar.MONTH);
                final int day = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(devolucionActivity, android.R.style.Theme_DeviceDefault_Dialog_MinWidth ,myDateListener, year+2, month, day).show();

            }
        });
        textAccion=(TextView)view.findViewById(R.id.texto_accion_art);
        layoutAccion=(RelativeLayout)view.findViewById(R.id.layoutAccionArt);
        layoutAccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items=getResources().getStringArray(R.array.acciones);

                new AlertDialog.Builder(devolucionActivity)
                        .setTitle(getResources().getString(R.string.linea_titulo_acciones))
                        .setItems(items,new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String seleccion=items[which];
                                if(which==0){
                                    textAccion.setText("");
                                }
                                else {
                                    textAccion.setText(seleccion);
                                }
                            }
                        })
                        .show();
            }
        });
        textMotivo=(TextView)view.findViewById(R.id.texto_motivo_art);
        layoutMotivo=(RelativeLayout)view.findViewById(R.id.layoutMotivoArt);
        layoutMotivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(devolucionActivity,android.R.style.Theme_DeviceDefault_Dialog_MinWidth)
                        .setTitle(getResources().getString(R.string.linea_titulo_motivo))
                        .setMessage(getResources().getString(R.string.linea_pregunta_motivo))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("SI",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                crearMotivo(true);
                            }
                        })
                        .setNegativeButton("NO",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                crearMotivo(false);
                            }
                        })
                        .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })

                        .show();
            }
        });

        llenarLinea();
        return view;
    }

    @Override
    public void onDetach() {
        devolucionActivity = null;
        super.onDetach();
    }

    /**
     * Start scan barcode activity
     */
    public void abrirBarcode(){
        IntentIntegrator integrator = new IntentIntegrator(devolucionActivity);
        integrator.initiateScan();
    }

    /**
     * put data in editext
     * @param codigo
     * @param nombre
     * @param umv
     */
    public void actualizarLinea(String codigo, String nombre, String umv){
        editCodigo.setText(codigo);
        editNombre.setText(nombre);
        editUmv.setText(umv);
    }

    /**
     * Puth data in editext
     * @param codigo
     * @param nombre
     * @param umv
     * @param lote
     * @param caducidad
     */
    public void actualizarLinea(String codigo, String nombre, String umv, String lote, String caducidad){
        actualizarLinea(codigo,nombre,umv);
        editLote.setText(lote);
        textCaducidad.setText(caducidad);
    }

    /**
     * Check data an save in a object line
     * @return can save or not
     */
    public boolean guardar(){
        this.linea=leerLinea();
        if(linea.getCodigo().equals("")){
            Utilidades.Alerts(devolucionActivity,null,getResources().getString(R.string.linea_e_codigo),Utilidades.TIPO_ADVERTENCIA_NEUTRAL,null);
            return false;
        }
        if(linea.getCantidad()==-1){
            Utilidades.Alerts(devolucionActivity,null,getResources().getString(R.string.linea_e_cantidad),Utilidades.TIPO_ADVERTENCIA_NEUTRAL,null);
            return false;
        }
        if(this.position==-1){
            Utilidades.devolucion.setLinea(linea, db);
        }
        else{
            Utilidades.devolucion.actualizarLinea(linea,position, db);
        }
        return true;
    }

    /**
     * When exit the fragment check if have some change if have a change, show a alert dialog
     */
    public void perderCambios(){
        Linea nueva=leerLinea();
        if(linea.tieneCambios(nueva)){
            Utilidades.Alerts(devolucionActivity,null,getResources().getString(R.string.descartar_cambios),Utilidades.TIPO_ADVERTENCIA_SI_NO, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    devolucionActivity.cambiarFragment(0);
                }
            });
        }
        else {
            devolucionActivity.cambiarFragment(0);
        }
    }

    /**
     * Read a line od devolution of the fragment widgets
     * @return a object line
     */
    public Linea leerLinea(){
        String codigo=editCodigo.getText().toString();
        String nombre=editNombre.getText().toString();
        String cantidadS=editCantidad.getText().toString();
        double cantidad=-1;
        try {
            cantidad = Double.parseDouble(cantidadS);
        }
        catch (NumberFormatException e){}
        String umv=editUmv.getText().toString();
        String lote=editLote.getText().toString();
        String fecha=textCaducidad.getText().toString();
        String accion=textAccion.getText().toString();
        String motivo=textMotivo.getText().toString();
        Linea res= new Linea(codigo,nombre,cantidad,umv,lote,fecha,accion,motivo);
        res.setId(linea.getId());
        return res;
    }

    /**
     * Fill all fileds with a existent line
     */
    private void llenarLinea(){
        editCodigo.setText(linea.getCodigo());
        editNombre.setText(linea.getNombre());
        String cantidad="";
        if(linea.getCantidad()>=0)
            cantidad=linea.getCantidad()+"";
        editCantidad.setText(cantidad);
        editUmv.setText(linea.getUmv());
        editLote.setText(linea.getLote());
        textCaducidad.setText(linea.getFecha());
        textAccion.setText(linea.getAccion());
        textMotivo.setText(linea.getMotivo());
    }

    /**
     * Catch the event acept in Datepicker and put date in textView
     */
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            month++;
            textCaducidad.setText(((day<10)?"0"+day:day)+"-"+((month<10)?"0"+month:month)+"-"+year);
        }
    };

    private void crearMotivo(final boolean calidad){

        final String[] items;
        if(calidad)
            items=getResources().getStringArray(R.array.calidad);
        else
            items=getResources().getStringArray(R.array.no_calidad);

        new AlertDialog.Builder(devolucionActivity)
                .setTitle(getResources().getString(R.string.linea_titulo_motivo))
                .setItems(items,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String seleccion=items[which];
                        if(!calidad && which==0){
                            textMotivo.setText("");
                        }
                        else {
                            textMotivo.setText(seleccion);
                        }
                    }
                })
                .show();
    }

}
