package com.pcs.vicchiam.devoluciones.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;

import com.pcs.vicchiam.devoluciones.R;

/**
 * Created by vicch on 09/05/2016.
 */
public class SearchSuggestionAdapter extends CursorAdapter {

    private String col_name1, col_name2;

    public SearchSuggestionAdapter(Context context, Cursor cursor,String col1, String col2){
        super(context,cursor,0);
        this.col_name1=col1;
        this.col_name2=col2;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.search_suggestion_item,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView t1=(TextView)view.findViewById(R.id.search_suggestion_item_1);
        TextView t2=(TextView)view.findViewById(R.id.search_suggestion_item_2);

        t1.setText(cursor.getString(cursor.getColumnIndex(col_name1)));
        t2.setText(cursor.getString(cursor.getColumnIndex(col_name2)));
    }
}
