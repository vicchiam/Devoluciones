package com.pcs.vicchiam.devoluciones.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.Calendar;

/**
 * Created by vicch on 09/05/2016.
 */
public class LineaFragment extends Fragment {

    private DevolucionActivity self;
    private EditText editCodigo, editNombre, editUmv, editLote;
    private ImageButton imgButtonBarcode;
    private TextView textCaducidad;
    private ImageView imgCaducidad;
    private RelativeLayout layoutCaducidad;

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

    public void actualizarLinea(String codigo, String nombre, String umv){
        editCodigo.setText(codigo);
        editNombre.setText(nombre);
        editUmv.setText(umv);
    }

    public void actualizarLinea(String codigo, String nombre, String umv, String lote, String caducidad){
        actualizarLinea(codigo,nombre,umv);
        editLote.setText(lote);
        textCaducidad.setText(caducidad);
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            month++;
            textCaducidad.setText(((day<10)?"0"+day:day)+"-"+((month<10)?"0"+month:month)+"-"+year);
        }
    };

}
