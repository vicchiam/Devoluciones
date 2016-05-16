package com.pcs.vicchiam.devoluciones.bbdd;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicch on 16/05/2016.
 */
public class Devolucion {

    private long id;
    private String codigo;
    private String nombre;
    private String accion;
    private String motivo;
    private List<Linea> lineas;
    private List<String> adjuntos;

    public Devolucion(String codigo, String nombre, String accion, String motivo) {
        this.id=0;
        this.codigo = codigo;
        this.nombre = nombre;
        this.accion = accion;
        this.motivo = motivo;
        this.lineas=new ArrayList<>();
        this.adjuntos=new ArrayList<>();
    }

    public Devolucion(Cursor cursor){
        this.id=cursor.getLong(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[0]));
        this.codigo=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[1]));
        this.nombre=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[2]));
        this.accion=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[3]));
        this.motivo=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[4]));
        this.lineas=new ArrayList<>();
        this.adjuntos=new ArrayList<>();
    }

    public long getId(){ return id; }

    public void setId(long id) { this.id=id; }

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

    public List<Linea> getLineas() {
        return lineas;
    }

    public void setLineas(List<Linea> lineas) {
        this.lineas = lineas;
    }

    public List<String> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<String> adjuntos) {
        this.adjuntos = adjuntos;
    }
}
