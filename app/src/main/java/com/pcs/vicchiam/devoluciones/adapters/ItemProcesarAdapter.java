package com.pcs.vicchiam.devoluciones.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.bbdd.Transporte;
import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

import java.util.List;

/**
 * Created by victor on 07/06/2016.
 */
public class ItemProcesarAdapter extends BaseAdapter{

    private Context context;

    private List<Transporte> lista;

    public ItemProcesarAdapter(Context context, List<Transporte> lista){
        this.context=context;
        this.lista=lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item_procesar, parent, false);
        }

        TextView pCodigo = (TextView) rowView.findViewById(R.id.pCodigo);
        TextView pNombre = (TextView) rowView.findViewById(R.id.pNombre);
        TextView pFecha = (TextView) rowView.findViewById(R.id.pFecha);

        pCodigo.setText(lista.get(position).getCliente());
        pNombre.setText(lista.get(position).getNombre());
        pFecha.setText(lista.get(position).getFecha());

        return rowView;
    }

    public void setListado(List<Transporte> lista){
        this.lista=lista;
    }

}
