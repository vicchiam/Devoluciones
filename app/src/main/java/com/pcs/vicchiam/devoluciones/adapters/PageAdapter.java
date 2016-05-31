package com.pcs.vicchiam.devoluciones.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.fragments.ProcesarFragment;

/**
 * Created by vicch on 31/05/2016.
 */
public class PageAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT=3;
    private Context context;

    public PageAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        return ProcesarFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return context.getResources().getString(R.string.pagina1);
            case 1: return context.getResources().getString(R.string.pagina2);
            case 2: return context.getResources().getString(R.string.pagina3);
            default: return "";
        }
    }

}
