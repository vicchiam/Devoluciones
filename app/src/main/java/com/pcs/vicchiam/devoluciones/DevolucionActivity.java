package com.pcs.vicchiam.devoluciones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.pcs.vicchiam.devoluciones.fragments.CabeceraFragment;

public class DevolucionActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private DevolucionActivity self;

    private CabeceraFragment cabeceraFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion);
        this.self=this;

        cabeceraFragment=new CabeceraFragment();

        initializeUI();
    }

    private void initializeUI(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        viewPager=(ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private class PageAdapter extends FragmentPagerAdapter {

        public final int NUM_PAGES=1;

        public PageAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return cabeceraFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
