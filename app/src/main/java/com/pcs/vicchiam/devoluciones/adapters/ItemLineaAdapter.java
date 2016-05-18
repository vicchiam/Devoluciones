package com.pcs.vicchiam.devoluciones.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.bbdd.Devolucion;
import com.pcs.vicchiam.devoluciones.bbdd.Linea;

import java.util.List;

/**
 * Created by vicchiam on 17/05/2016.
 * Class for show a item of listview that represent a line
 */
public class ItemLineaAdapter extends BaseAdapter {

    private Context context;
    private List<Linea> lineas;

    public ItemLineaAdapter(Context context, List<Linea> lineas){
        this.context=context;
        this.lineas=lineas;
    }

    @Override
    public int getCount() {
        return lineas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.lineas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item_devol, parent, false);
        }

        ImageView ivItem = (ImageView) rowView.findViewById(R.id.ivItem);
        TextView textView = (TextView) rowView.findViewById(R.id.tvTitle);

        Linea linea=this.lineas.get(position);
        textView.setText(linea.getNombre());


        return rowView;
    }

}
