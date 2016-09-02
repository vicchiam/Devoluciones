package com.pcs.vicchiam.devoluciones;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.zxing.common.StringUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.Util;
import com.pcs.vicchiam.devoluciones.adapters.SearchSuggestionAdapter;
import com.pcs.vicchiam.devoluciones.bbdd.Articulo;
import com.pcs.vicchiam.devoluciones.bbdd.ArticuloDB;
import com.pcs.vicchiam.devoluciones.bbdd.ClienteDB;
import com.pcs.vicchiam.devoluciones.bbdd.Devolucion;
import com.pcs.vicchiam.devoluciones.bbdd.DevolucionDB;
import com.pcs.vicchiam.devoluciones.bbdd.TransporteBD;
import com.pcs.vicchiam.devoluciones.fragments.AdjuntoFragment;
import com.pcs.vicchiam.devoluciones.fragments.DevolucionFragment;
import com.pcs.vicchiam.devoluciones.fragments.LineaFragment;
import com.pcs.vicchiam.devoluciones.fragments.ObsFragment;
import com.pcs.vicchiam.devoluciones.utilidades.Logica;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

import java.io.File;
import java.util.List;

/**
 * Created by vicchiam on 01/05/2016.
 * Class that make a activity that contains a devolutions and lines
 */
public class DevolucionActivity extends AppCompatActivity {

    private static final String ID="ID";
    private static final String ACTUAL_FRAGMENT="ACTUAL_FRAGMENT";
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int WRITE_EXTERNAL_STORAGE = 1;

    private DevolucionActivity self;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;
    private Menu menu;
    private SearchSuggestionAdapter searchAdapter;
    private SearchView searchView;
    private DevolucionFragment devolucionFragment;
    private LineaFragment lineaFragment;
    private ObsFragment obsFragment;
    private AdjuntoFragment adjuntoFragment;
    private int actualFragment;
    private long id;

    private boolean cambio_obs;
    private boolean cambio_adj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion);
        this.self=this;

        if(savedInstanceState==null) {

            devolucionFragment = DevolucionFragment.newInstance(null);

            actualFragment = 0;

            id = 0;
            Intent i = getIntent();
            if (i.getExtras().containsKey("id")) {
                id = i.getExtras().getLong("id");
            }

            if(id==0) {

                Utilidades.devolucion = new Devolucion();

                Utilidades.devolucion.setId(id);

                if (i.getExtras().containsKey("codigo")) {
                    Utilidades.devolucion.setCodigo(i.getExtras().getString("codigo"));
                }
                if (i.getExtras().containsKey("nombre")) {
                    Utilidades.devolucion.setNombre(i.getExtras().getString("nombre"));
                }
                if (i.getExtras().containsKey("id_trasporte")) {
                    long id_trasporte = i.getExtras().getLong("id_trasporte");
                    Utilidades.devolucion.setId_transporte(id_trasporte);
                }
            }
            else{

                DevolucionDB db=new DevolucionDB(this);

                Utilidades.devolucion=db.buscarDevolucionId(id);
                Utilidades.devolucion.setLineas(db.obtenerTodosLineas(id));

            }

        }
        else{

            this.id=savedInstanceState.getLong(ID);

            this.actualFragment=savedInstanceState.getInt(ACTUAL_FRAGMENT);

            //Utilidades.devolucion = new Devolucion();
            //Utilidades.devolucion.setId(id);

            this.devolucionFragment=Utilidades.devolFrag;
            Utilidades.devolFrag=null;

            this.lineaFragment=Utilidades.lineaFrag;
            Utilidades.lineaFrag=null;

        }

        initializeUI();

        this.cambio_obs=false;

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

        if(actualFragment==1){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, lineaFragment).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, devolucionFragment).commit();
        }

    }

    /**
     *Catch when the back button pressed
     */
    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
        } else {
            switch (actualFragment){
                case 0:{
                    devolucionFragment.perderCambios();
                    break;
                }
                case 1:{
                    lineaFragment.perderCambios();
                    break;
                }
                case 2:{
                    cambiarFragment(0);
                    break;
                }
                case 3:{
                    cambiarFragment(0);
                    break;
                }
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
                switch (actualFragment){
                    case 0:{
                        devolucionFragment.perderCambios();
                        break;
                    }
                    case 1:{
                        lineaFragment.perderCambios();
                        break;
                    }
                    case 2:{
                        cambiarFragment(0);
                        break;
                    }
                    case 3:{
                        cambiarFragment(0);
                        break;
                    }
                }
                break;
            }
            case R.id.menu_nuevo_devol:{
                if(actualFragment==0){
                    abrirLinea(null);
                }
                else if(actualFragment==1){
                    lineaFragment.abrirBarcode();
                }
                else if(actualFragment==2){
                    obsFragment.borrar();
                }
                else if(actualFragment==3){
                    hacerFoto();
                }
                break;
            }
            case R.id.menu_obs_devol:{
                cambiarFragment(2);
                break;
            }
            case R.id.menu_adj_devol:{
                cambiarFragment(3);
                break;
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
                if(actualFragment==0) {
                    String codigo = cursor.getString(cursor.getColumnIndex(ClienteDB.COLS_CLIENTE[0]));
                    String nombre = cursor.getString(cursor.getColumnIndex(ClienteDB.COLS_CLIENTE[1]));
                    devolucionFragment.actualizarCabecera(codigo, nombre);
                }
                else if(actualFragment==1){
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
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap foto = (Bitmap) extras.get("data");

                Uri tempUri = Utilidades.getImageUri(getApplicationContext(), foto);
                File file = new File(Utilidades.getRealPathFromURI(getApplicationContext(),tempUri));

                if(adjuntoFragment!=null){
                    adjuntoFragment.anyadirImagen(file.getAbsolutePath());
                }


            }
        }
        else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {
                    String codigoBarras = result.getContents();
                    procesarCodigoBarras(codigoBarras);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putLong(ID,this.id);
        savedInstanceState.putInt(ACTUAL_FRAGMENT,this.actualFragment);

        Utilidades.devolFrag=devolucionFragment;
        if(lineaFragment!=null) {
            Utilidades.lineaFrag = lineaFragment;
        }

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
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
        else if(actualFragment==1){
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
    public void cambiarFragment(int num){
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
        }
        switch (num) {
            case 0: {
                getSupportActionBar().setTitle(R.string.devol_title);
                fab.setVisibility(View.VISIBLE);
                menu.getItem(0).setVisible(true);
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_add_white_24dp));
                menu.getItem(2).setVisible(true);
                menu.getItem(3).setVisible(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, devolucionFragment).commit();
                break;
            }
            case 1: {
                getSupportActionBar().setTitle(R.string.linea_title);
                fab.setVisibility(View.VISIBLE);
                menu.getItem(0).setVisible(true);
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_monochrome_photos_white_24dp));
                menu.getItem(2).setVisible(false);
                menu.getItem(3).setVisible(false);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, lineaFragment).commit();
                break;
            }
            case 2: {
                getSupportActionBar().setTitle(R.string.devol_obs);
                fab.setVisibility(View.VISIBLE);
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_delete_white_24dp));
                menu.getItem(2).setVisible(false);
                menu.getItem(3).setVisible(false);
                if(obsFragment==null) {
                    obsFragment = ObsFragment.newInstance(null);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, obsFragment).commit();
                break;
            }
            case 3: {
                getSupportActionBar().setTitle(R.string.devol_adj);
                fab.setVisibility(View.GONE);
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_photo_camera_white_24dp));
                menu.getItem(2).setVisible(false);
                menu.getItem(3).setVisible(false);
                if(adjuntoFragment==null){
                    adjuntoFragment=AdjuntoFragment.newInstance(null);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, adjuntoFragment).commit();
                break;
            }
            default:{
                break;
            }
        }
        actualFragment=num;

    }

    /**
     * Save the actual fragment
     */
    private void guardar(){
        //Save a data
        if(actualFragment==0){
            if(devolucionFragment.guardar()){
                Logica l=new Logica(this);
                l.enviarDevolucion();
            }
        }
        else if(actualFragment==1){
            if(lineaFragment.guardar()){
                Utilidades.crearSnackBar(this.coordinatorLayout,getResources().getString(R.string.linea_guardada));
                cambiarFragment(0);
                devolucionFragment.refresh();
            }
        }
        else if(actualFragment==2){
            if(obsFragment.guardar()){
                Utilidades.crearSnackBar(this.coordinatorLayout,getResources().getString(R.string.devol_obs_guardada));
                cambiarFragment(0);
            }
        }

        //Hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);
    }

    public void finalizarDevolucion(String respuesta){
        Log.e("Respuesta",respuesta);
        if(Utilidades.esNumero(respuesta)){
            Long id_servidor=Long.parseLong(respuesta);
            DevolucionDB db=new DevolucionDB(this);
            db.agregarDevolucionIdServidor(Utilidades.devolucion.getId(),id_servidor);
            Utilidades.ESTADO_DEVOLUCION=1;
        }
        else{
            Utilidades.ESTADO_DEVOLUCION=2;
        }
        Utilidades.devolucion=null;
        setResult(RESULT_OK);
        finish();
    }

    /**
     * Create a new lineaFragment
     */
    public void abrirLinea(Bundle bundle){
        lineaFragment=LineaFragment.newInstance(bundle);
        cambiarFragment(1);
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

    public void hacerFoto(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            // Callback onRequestPermissionsResult interceptado na Activity MainActivity
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
        }
        else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }

    public void ponerCambioObservacion(){
        this.cambio_obs=true;
    }

    public boolean hayCambioObservcion(){
        return this.cambio_obs;
    }


}
