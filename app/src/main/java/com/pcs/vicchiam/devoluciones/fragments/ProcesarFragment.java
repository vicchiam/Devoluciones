package com.pcs.vicchiam.devoluciones.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pcs.vicchiam.devoluciones.R;

/**
 * Created by vicch on 31/05/2016.
 */
public class ProcesarFragment extends Fragment{

    public static final String ARG_NUM_PAGE = "ARG_NUM_PAGE";
    private int posPage;

    public static ProcesarFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_NUM_PAGE, page);
        ProcesarFragment pFragment = new ProcesarFragment();
        pFragment.setArguments(args);
        return pFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posPage = getArguments().getInt(ARG_NUM_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_page1, container, false);
       // TextView textView = (TextView) view;
        //textView.setText("Fragment #" + posPage);
        return view;
    }
}
