package com.pcs.vicchiam.devoluciones;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pcs.vicchiam.devoluciones.adapters.SearchSuggestionAdapter;
import com.pcs.vicchiam.devoluciones.bbdd.Articulo;
import com.pcs.vicchiam.devoluciones.bbdd.ArticuloDB;
import com.pcs.vicchiam.devoluciones.bbdd.ClienteDB;
import com.pcs.vicchiam.devoluciones.fragments.CabeceraFragment;
import com.pcs.vicchiam.devoluciones.fragments.LineaFragment;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

import java.util.List;

public class DevolucionActivity extends AppCompatActivity {

    private DevolucionActivity self;
    private SearchSuggestionAdapter searchAdapter;
    private SearchView searchView;
    private CabeceraFragment cabeceraFragment;
    private LineaFragment lineaFragment;
    private int actualFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion);
        this.self=this;

        cabeceraFragment=CabeceraFragment.newInstance(null);
        actualFragment=0;

        initializeUI();
    }

    private void initializeUI(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, cabeceraFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
        } else {
            if(actualFragment==0){
                finish();
            }
            else{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, cabeceraFragment).commit();
                actualFragment=0;
            }
           // super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                if(actualFragment==0){
                    finish();
                }
                else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, cabeceraFragment).commit();
                    actualFragment=0;
                }
                break;
            }
            case R.id.menu_save:{
                if(actualFragment==0){

                }
                else{

                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_devolucion, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
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

    public void abrirLinea(){
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
        }
        lineaFragment=LineaFragment.newInstance(null);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, lineaFragment).commit();
        actualFragment=1;
    }

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

        Log.e("BARCODE",empresa+"  "+codigo+"  "+lote+"   "+codigo_empresa);

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
