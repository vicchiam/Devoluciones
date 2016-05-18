package com.pcs.vicchiam.devoluciones.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.pcs.vicchiam.devoluciones.DevolucionActivity;
import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.bbdd.Linea;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

import java.util.Calendar;

/**
 * Created by vicchiam on 09/05/2016.
 * Class that make a fragment that represent a line
 */
public class LineaFragment extends Fragment {

    private DevolucionActivity self;
    private EditText editCodigo, editNombre,editCantidad, editUmv, editLote;
    private ImageButton imgButtonBarcode;
    private TextView textCaducidad;
    private ImageView imgCaducidad;
    private RelativeLayout layoutCaducidad;
    private long id;
    private Linea linea;

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
        if(context instanceof DevolucionActivity){
            self=(DevolucionActivity) context;
        }
        if(getArguments()!=null && getArguments().containsKey("id")){
            id=getArguments().getLong("id");
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
        editCodigo=(EditText)view.findViewById(R.id.edit_codigo_art);
        editNombre=(EditText)view.findViewById(R.id.edit_descr_art);
        editCantidad=(EditText)view.findViewById(R.id.edit_cantidad_art);
        editUmv=(EditText)view.findViewById(R.id.edit_umv_art);
        editLote=(EditText)view.findViewById(R.id.edit_lote);
        textCaducidad=(TextView)view.findViewById(R.id.edit_caducidad);
        imgButtonBarcode=(ImageButton)view.findViewById(R.id.imgbtn_barcode_art);
        imgButtonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(self);
                integrator.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                integrator.initiateScan();
            }
        });
        layoutCaducidad=(RelativeLayout)view.findViewById(R.id.content_caducidad);
        layoutCaducidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final int year = c.get(Calendar.YEAR);
                final int month = c.get(Calendar.MONTH);
                final int day = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(self, myDateListener, year+2, month, day).show();
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        self = null;
        super.onDetach();
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
            Utilidades.Alerts(self,null,getResources().getString(R.string.linea_e_codigo),Utilidades.TIPO_ADVERTENCIA_NEUTRAL,null);
            return false;
        }
        if(linea.getCantidad()==-1){
            Utilidades.Alerts(self,null,getResources().getString(R.string.linea_e_cantidad),Utilidades.TIPO_ADVERTENCIA_NEUTRAL,null);
            return false;
        }
        if(this.id==0){
            Utilidades.devolucion.setLinea(linea);
        }
        return true;
    }

    /**
     * When exit the fragment check if have some change if have a chage, show a alert dialog
     */
    public void perderCambios(){
        Linea nueva=leerLinea();
        Log.e("EEEE",nueva.equals(this.linea)+"");
        if(!nueva.comprobarCambios(linea)){
            Utilidades.Alerts(self,null,getResources().getString(R.string.descartar_cambios),Utilidades.TIPO_ADVERTENCIA_SI_NO, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    self.volver();
                }
            });
        }
    }

    /**
     * Read a line od devolution of the fragment widgets
     * @return a object line
     */
    private Linea leerLinea(){
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
        return new Linea(codigo,nombre,cantidad,umv,lote,fecha);
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

}
