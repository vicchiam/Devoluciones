package com.pcs.vicchiam.devoluciones;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.pcs.vicchiam.devoluciones.adapters.PaginaAdapter;
import com.pcs.vicchiam.devoluciones.fragments.ProcesarFragment;
import com.pcs.vicchiam.devoluciones.utilidades.Logica;

/**
 * Created by vicchiam on 01/05/2016.
 * Class that make a main activity
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CoordinatorLayout coordinatorLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MainActivity self;

    private Logica logica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.self=this;
        initializeUI();
        iniApp();
    }

    /*INICIALIZAR UI********************************************************************************/

    /**
     * Initialize a user interface
     */
    private void initializeUI(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(self);

        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.clayout);

        tabLayout=(TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        PaginaAdapter pAdapter=new PaginaAdapter(getSupportFragmentManager(),MainActivity.this);
        viewPager.setAdapter(pAdapter);

        tabLayout.setupWithViewPager(viewPager);

    }

    /*MENU*******************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            }
            case R.id.menu_nuevo: {
                Intent intent=new Intent(self,DevolucionActivity.class);
                intent.putExtra("id",0);
                self.startActivity(intent);
                break;
            }
        }
        return true;
    }

    /**
     * Actions when touch a navigation bar
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if (menuItem.isChecked()) {
            menuItem.setChecked(false);
        } else {
            menuItem.setChecked(true);
        }
        //Closing drawer on item click
        drawerLayout.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.navigation_item_1:{
                break;
            }
            case R.id.navigation_item_2:{
                this.logica.obtenerDatos(true);
                break;
            }
            case R.id.navigation_item_3: {
                Intent intent = new Intent(self, PreferenciasBarActivity.class);
                startActivity(intent);
                break;
            }
        }
        return false;
    }

    /*OVERRIDES************************************************************************************/
    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(navigationView)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        else{
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    /*MIS METODOS**********************************************************************************/

    /**
     * Initialize the application
     */
    private void iniApp(){
        logica=Logica.getInstance(self);
    }

    /**
     * Get layout coordinator
     * @return
     */
    public CoordinatorLayout getCoordinatorLayout(){
        return this.coordinatorLayout;
    }

    /**
     * Update the viewpager fragments
     * @param pos
     */
    public void actualizar(int pos){
        PaginaAdapter adapter=(PaginaAdapter)viewPager.getAdapter();
        Fragment f=adapter.getItem(pos);
        if(f instanceof ProcesarFragment){
            ProcesarFragment pf=(ProcesarFragment)f;
            pf.actualizarListado();
        }
    }

    public void abrirDevolucion(String codigo, String nombre, long id){
        Intent intent=new Intent(self,DevolucionActivity.class);
        intent.putExtra("id",0);
        intent.putExtra("codigo",codigo);
        intent.putExtra("nombre",nombre);
        intent.putExtra("id_trasporte",id);
        self.startActivity(intent);
    }


}
