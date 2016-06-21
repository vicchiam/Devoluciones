package com.pcs.vicchiam.devoluciones.bbdd;

import android.database.Cursor;

/**
 * Created by victor on 02/06/2016.
 */
public class Transporte {

    private long albaran;
    private String cliente;
    private String nombre;
    private String fecha;
    private String obs;

    public Transporte(long albaran, String cliente, String nombre, String fecha, String obs){
        this.albaran=albaran;
        this.cliente=cliente;
        this.nombre=nombre;
        this.fecha=fecha;
        this.obs=obs;
    }

    public Transporte(Cursor cursor){
        this.albaran=cursor.getLong(cursor.getColumnIndex(TransporteBD.COLS[0]));
        this.cliente=cursor.getString(cursor.getColumnIndex(TransporteBD.COLS[1]));
        this.nombre=cursor.getString(cursor.getColumnIndex(TransporteBD.COLS[2]));
        this.fecha=cursor.getString(cursor.getColumnIndex(TransporteBD.COLS[3]));
        this.obs=cursor.getString(cursor.getColumnIndex(TransporteBD.COLS[4]));
    }

    public long getId() {
        return albaran;
    }

    public void setId(long id) {
        this.albaran = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

}
