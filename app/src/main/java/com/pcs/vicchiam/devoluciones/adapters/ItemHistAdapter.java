package com.pcs.vicchiam.devoluciones.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.pcs.vicchiam.devoluciones.R;
import com.pcs.vicchiam.devoluciones.bbdd.Devolucion;

import java.util.List;

/**
 * Created by victor on 24/08/2016.
 */
public class ItemHistAdapter extends RecyclerView.Adapter<ItemHistAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(long id);
    }

    private Context contexto;
    private List<Devolucion> lista;

    private OnItemClickListener listener;

    public ItemHistAdapter(Context contexto, List<Devolucion> lista){
        this.contexto=contexto;
        this.lista=lista;
    }

    public void setListado(List<Devolucion> lista){
        this.lista=lista;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_hist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Devolucion d=lista.get(position);

        holder.hCodigo.setText(d.getCodigo());
        holder.hNombre.setText(d.getNombre());
        holder.hFecha.setText(d.getFecha());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener listener){
        this.listener=listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView hCodigo;
        public TextView hNombre;
        public TextView hFecha;

        public ViewHolder(View v) {
            super(v);
            hCodigo=(TextView) v.findViewById(R.id.hCodigo);
            hNombre=(TextView) v.findViewById(R.id.hNombre);
            hFecha=(TextView) v.findViewById(R.id.hFecha);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener!=null) {
                Devolucion d=lista.get(getPosition());
                listener.onItemClick(d.getId());
            }
        }
    }


}
