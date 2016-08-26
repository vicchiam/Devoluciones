package com.pcs.vicchiam.devoluciones.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.bbdd.Devolucion;

import java.util.List;

/**
 * Created by victor on 22/08/2016.
 */
public class ItemEnviarAdapter extends BaseAdapter {

    private Context context;

    private List<Devolucion> lista;

    public ItemEnviarAdapter(Context context, List<Devolucion> lista){
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
            rowView = inflater.inflate(R.layout.list_item_enviar, parent, false);
        }

        TextView eCodigo = (TextView) rowView.findViewById(R.id.eCodigo);
        TextView eNombre = (TextView) rowView.findViewById(R.id.eNombre);
        TextView eFecha = (TextView) rowView.findViewById(R.id.eFecha);

        eCodigo.setText(lista.get(position).getCodigo());
        eNombre.setText(lista.get(position).getNombre());
        eFecha.setText(lista.get(position).getFecha());

        return rowView;
    }

    public List<Devolucion> getListado(){
        return this.lista;
    }

    public void setListado(List<Devolucion> lista){
        this.lista=lista;
    }

}
