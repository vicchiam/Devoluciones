package com.pcs.vicchiam.devoluciones;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.pcs.vicchiam.devoluciones.adapters.SearchSuggestionAdapter;
import com.pcs.vicchiam.devoluciones.bbdd.ClienteDB;
import com.pcs.vicchiam.devoluciones.fragments.CabeceraFragment;
import com.pcs.vicchiam.devoluciones.fragments.LineaFragment;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

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
                if(newText.length()>=3){
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

                }
                searchView.onActionViewCollapsed();
                return true;
            }
        });

        return true;
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


}
