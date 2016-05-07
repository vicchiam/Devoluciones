package com.pcs.vicchiam.devoluciones;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.pcs.vicchiam.devoluciones.fragments.PreferenciasFragment;

/**
 * Created by vicchiam on 06/05/2016.
 * Class that makes the preferences activity, I not use a PreferenceActivity because I need a toolbar,
 * I use a PreferenceFragment
 */
public class PreferenciasBarActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferencias_bar_activity);

        initializaUI();

        getFragmentManager().beginTransaction().replace(R.id.preferences_content,new PreferenciasFragment()).commit();
    }

    private void initializaUI(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.preferences_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
