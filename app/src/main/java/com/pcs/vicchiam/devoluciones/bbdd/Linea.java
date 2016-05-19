package com.pcs.vicchiam.devoluciones.bbdd;

import android.database.Cursor;

/**
 * Created by vicchiam on 16/05/2016.
 * Class that represent a line of devolution
 */
public class Linea {

    private long id;
    private String codigo;
    private String nombre;
    private double cantidad;
    private String umv;
    private String lote;
    private String fecha;

    /**
     * Empty constructor for a temporal lines
     */
    public Linea(){
        this.id=0;
        this.codigo = "";
        this.nombre = "";
        this.cantidad = -1;
        this.umv = "";
        this.lote = "";
        this.fecha = "";
    }

    public Linea(String codigo, String nombre, double cantidad, String umv, String lote, String fecha) {
        this.id=0;
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.umv = umv;
        this.lote = lote;
        this.fecha = fecha;
    }

    public Linea(Cursor cursor){
        this.id=cursor.getLong(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[0]));
        this.codigo=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[1]));
        this.nombre=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[2]));
        this.cantidad=cursor.getDouble(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[3]));
        this.umv=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[4]));
        this.lote=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[5]));
        this.fecha=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[6]));
    }

    public long getId(){ return id; }

    public void setId(long id){ this.id=id; }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getUmv() {
        return umv;
    }

    public void setUmv(String umv) {
        this.umv = umv;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Linea linea = (Linea) o;

        if (id != linea.id) return false;
        if (Double.compare(linea.cantidad, cantidad) != 0) return false;
        if (codigo != null ? !codigo.equals(linea.codigo) : linea.codigo != null) return false;
        if (nombre != null ? !nombre.equals(linea.nombre) : linea.nombre != null) return false;
        if (umv != null ? !umv.equals(linea.umv) : linea.umv != null) return false;
        if (lote != null ? !lote.equals(linea.lote) : linea.lote != null) return false;
        return fecha != null ? fecha.equals(linea.fecha) : linea.fecha == null;

    }

    public boolean tieneCambios(Linea nueva){
        if(this.getId()!=nueva.getId()) return true;
        if(!this.getCodigo().equals(nueva.getCodigo())) return true;
        if(!this.getNombre().equals(nueva.getNombre())) return true;
        if(this.getCantidad()!=nueva.getCantidad()) return true;
        if(!this.getUmv().equals(nueva.getUmv())) return true;
        if(!this.getLote().equals(nueva.getLote())) return true;
        if(!this.getFecha().equals(nueva.getFecha())) return true;
        return false;
    }

}
