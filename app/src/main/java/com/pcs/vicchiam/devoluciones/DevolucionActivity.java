package com.pcs.vicchiam.devoluciones;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pcs.vicchiam.devoluciones.adapters.SearchSuggestionAdapter;
import com.pcs.vicchiam.devoluciones.bbdd.Articulo;
import com.pcs.vicchiam.devoluciones.bbdd.ArticuloDB;
import com.pcs.vicchiam.devoluciones.bbdd.ClienteDB;
import com.pcs.vicchiam.devoluciones.bbdd.Devolucion;
import com.pcs.vicchiam.devoluciones.fragments.CabeceraFragment;
import com.pcs.vicchiam.devoluciones.fragments.LineaFragment;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

import java.util.List;

/**
 * Created by vicchiam on 01/05/2016.
 * Class that make a activity that contains a devolutions and lines
 */
public class DevolucionActivity extends AppCompatActivity {

    private DevolucionActivity self;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;
    private Menu menu;
    private SearchSuggestionAdapter searchAdapter;
    private SearchView searchView;
    private CabeceraFragment cabeceraFragment;
    private LineaFragment lineaFragment;
    private int actualFragment;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion);
        this.self=this;

        cabeceraFragment=CabeceraFragment.newInstance(null);
        actualFragment=0;

        initializeUI();

        Utilidades.devolucion=new Devolucion();

        id=0;
    }

    /**
     * Inictialize a user interface
     */
    private void initializeUI(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        fab=(FloatingActionButton)findViewById(R.id.fab_devol);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });

        this.coordinatorLayout=(CoordinatorLayout)findViewById(R.id.clayout_devol);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, cabeceraFragment).commit();
    }

    /**
     *Catch when the back button pressed
     */
    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
        } else {
            if(actualFragment==0){
                finish();
            }
            else{
                lineaFragment.perderCambios();
            }
        }
    }

    /**
     * The actionbar options
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //When pressed a left button of the actionbar
            case android.R.id.home:{
                if(actualFragment==0){
                    finish();
                }
                else{
                    //If are in a fragmet line chek if have changes without save
                    lineaFragment.perderCambios();
                }
                break;
            }
            case R.id.menu_nuevo_devol:{
                if(actualFragment==0){
                    abrirLinea(null);
                }
                else{
                    lineaFragment.abrirBarcode();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_devolucion, menu);
        this.menu=menu;
        searchView = (SearchView) menu.findItem(R.id.menu_search_devol).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>=2){
                    updateSearchSuggestion(newText);
                }
                return true;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            /**
             * Event when the autocomplet suggestion is selected
             * @param position
             * @return
             */
            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor=(Cursor)searchAdapter.getItem(position);
                if(self.actualFragment==0) {
                    String codigo = cursor.getString(cursor.getColumnIndex(ClienteDB.COLS_CLIENTE[0]));
                    String nombre = cursor.getString(cursor.getColumnIndex(ClienteDB.COLS_CLIENTE[1]));
                    cabeceraFragment.actualizarCabecera(codigo, nombre);
                }
                else{
                    String codigo = cursor.getString(cursor.getColumnIndex(ArticuloDB.COLS_ARTICULO[0]));
                    String nombre = cursor.getString(cursor.getColumnIndex(ArticuloDB.COLS_ARTICULO[1]));
                    String umv = cursor.getString(cursor.getColumnIndex(ArticuloDB.COLS_ARTICULO[2]));
                    lineaFragment.actualizarLinea(codigo, nombre, umv);
                }
                searchView.onActionViewCollapsed();
                return true;
            }
        });

        return true;
    }

    /**
     * Actions when return this activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                String codigoBarras=result.getContents();
                procesarCodigoBarras(codigoBarras);
            }
        }
    }

    /**
     * Get and show data to autocomplet widget
     * @param query
     */
    private void updateSearchSuggestion(String query){
        if(this.actualFragment==0) {
            String campo1= ClienteDB.COLS_CLIENTE[0];
            if(!Utilidades.esNumero(query)){
                campo1=ClienteDB.COLS_CLIENTE[1];
            }
            Cursor cursor = new ClienteDB(self).autocompletar(campo1, query);
            searchAdapter = new SearchSuggestionAdapter(self, cursor, ClienteDB.COLS_CLIENTE[0], ClienteDB.COLS_CLIENTE[1]);
            searchView.setSuggestionsAdapter(searchAdapter);
        }
        else{
            String campo1= ArticuloDB.COLS_ARTICULO[0];
            if(!Utilidades.esNumero(query)){
                campo1=ArticuloDB.COLS_ARTICULO[1];
            }
            Cursor cursor = new ArticuloDB(self).autocompletar(campo1,query);
            searchAdapter=new SearchSuggestionAdapter(self,cursor, ArticuloDB.COLS_ARTICULO[0],ArticuloDB.COLS_ARTICULO[1]);
            searchView.setSuggestionsAdapter(searchAdapter);
        }
    }

    /**
     * Change the fragments
     */
    public void cambiarFragment(){
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
        }
        if(this.actualFragment==0){
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_monochrome_photos_white_24dp));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, lineaFragment).commit();
            actualFragment=1;
        }
        else{
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_add_white_24dp));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, cabeceraFragment).commit();
            actualFragment=0;
        }
    }

    /**
     * Save the actual fragment
     */
    private void guardar(){
        //Save a data
        if(actualFragment==0){
            cabeceraFragment.guardar();
        }
        else{
            if(lineaFragment.guardar()){
                Utilidades.crearSnackBar(this.coordinatorLayout,getResources().getString(R.string.linea_guardada));
                cambiarFragment();
                cabeceraFragment.refresh();
            }
        }

        //Hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);
    }

    /**
     * Create a new lineaFragment
     */
    public void abrirLinea(Bundle bundle){
        lineaFragment=LineaFragment.newInstance(bundle);
        cambiarFragment();
    }

    /**
     * Extract data of barcode and ahow in the line fragment
     * @param codigoBarras
     */
    private void procesarCodigoBarras(String codigoBarras){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(self);
        boolean comporbar_barcode= prefs.getBoolean("check_barcode",false);
        String codigo_empresa=prefs.getString("codigo_empresa","");

        String p3=codigoBarras.substring(0,3);
        String empresa=codigoBarras.substring(3,10);
        String codigo=codigoBarras.substring(10,15);
        String p3_2=codigoBarras.substring(15,18);
        String fecha=codigoBarras.substring(18,24);
        String p3_3=codigoBarras.substring(24,26);
        String lote=codigoBarras.substring(26);

        if(comporbar_barcode && !empresa.equals(codigo_empresa)){
            Utilidades.Alerts(this,null,self.getResources().getString(R.string.error_barcode_company),Utilidades.TIPO_ADVERTENCIA_NEUTRAL,null);
            return;
        }

        ArticuloDB articuloDB=new ArticuloDB(self);
        List<Articulo> articulos=articuloDB.buscar(ArticuloDB.COLS_ARTICULO[0],codigo);
        if(articulos.size()>0) {
            Articulo articulo = articulos.get(0);

            String dia=fecha.substring(0,2);
            String mes=fecha.substring(2,4);
            String anyo=fecha.substring(4,6);

            String caducidad=dia+"-"+mes+"-20"+anyo;

            lineaFragment.actualizarLinea(codigo,articulo.getNombre(),articulo.getUmv(),lote,caducidad);

        }
    }


}
