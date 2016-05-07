package com.pcs.vicchiam.devoluciones.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.pcs.vicchiam.devoluciones.R;

/**
 * Created by vicchiam on 06/05/2016.
 *
 *Class that makes a PreferenceFragment
 */
public class PreferenciasFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);

        Context context=this.getActivity();
        context.setTheme(R.style.MyPreferenceTheme);

    }

}
