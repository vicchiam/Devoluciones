package com.pcs.vicchiam.devoluciones;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
    }

    /*INICIALIZAR UI********************************************************************************/

    private void initializeUI(){
        setupToolbar();
        setupNavigationView();
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_navigation_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupNavigationView(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(this);
    }

    /*MENU*******************************************************************************************/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
            case R.id.navigation_item_1:
                break;
            case R.id.navigation_item_2:
                break;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    /*MIS METODOS**********************************************************************************/






}
