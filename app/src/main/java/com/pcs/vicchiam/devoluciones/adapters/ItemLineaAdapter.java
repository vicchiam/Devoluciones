package com.pcs.vicchiam.devoluciones.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pcs.vicchiam.devoluciones.R;
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
            rowView = inflater.inflate(R.layout.list_item_linea, parent, false);
        }

        TextView tCodigo = (TextView) rowView.findViewById(R.id.tCodigo);
        TextView tNombre=(TextView) rowView.findViewById(R.id.tNombre);
        TextView tCantidad=(TextView) rowView.findViewById(R.id.tCantidad);
        TextView tLote=(TextView) rowView.findViewById(R.id.tLote);

        Linea linea=this.lineas.get(position);
        tCodigo.setText(linea.getCodigo());
        tNombre.setText(linea.getNombre());
        tCantidad.setText(linea.getCantidad()+" "+linea.getUmv());
        tLote.setText(linea.getLote());

        return rowView;
    }

    public void setList(List<Linea> list){
        this.lineas=list;
    }

}
