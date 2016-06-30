package com.pcs.vicchiam.devoluciones.bbdd;

import android.database.Cursor;
import android.util.Log;

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
    private String accion;
    private String motivo;

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
        this.accion = "";
        this.motivo = "";
    }

    public Linea(String codigo, String nombre, double cantidad, String umv, String lote, String fecha, String accion, String motivo) {
        this.id=0;
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.umv = umv;
        this.lote = lote;
        this.fecha = fecha;
        this.accion=accion;
        this.motivo=motivo;
    }

    public Linea(Cursor cursor){
        this.id=cursor.getLong(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[0]));
        this.codigo=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[1]));
        this.nombre=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[2]));
        this.cantidad=cursor.getDouble(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[3]));
        this.umv=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[4]));
        this.lote=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[5]));
        this.fecha=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[6]));
        this.accion=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[7]));
        this.motivo=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_LINEA[8]));
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

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public boolean tieneCambios(Linea nueva){
        if(this.getId()!=nueva.getId()) return true;
        if(!this.getCodigo().equals(nueva.getCodigo())) return true;
        if(!this.getNombre().equals(nueva.getNombre())) return true;
        if(this.getCantidad()!=nueva.getCantidad()) return true;
        if(!this.getUmv().equals(nueva.getUmv())) return true;
        if(!this.getLote().equals(nueva.getLote())) return true;
        if(!this.getFecha().equals(nueva.getFecha())) return true;
        if(!this.getAccion().equals(nueva.getAccion())) return true;
        if(!this.getMotivo().equals(nueva.getMotivo())) return true;
        return false;
    }

    public void agregar(DevolucionDB db, long id_devol){
        db.insertarLinea(codigo,nombre,cantidad,umv,lote,fecha,accion,motivo,id_devol);
    }

    public void modificar(DevolucionDB db){
        db.remplazarLinea(id,codigo,nombre,cantidad,umv,lote,fecha,accion,motivo);
    }

    public void eliminar(DevolucionDB db){
        db.eliminarLinea(id);
    }

}
