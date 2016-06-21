package com.pcs.vicchiam.devoluciones.bbdd;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicchiam on 16/05/2016.
 * Make a devolution object
 */
public class Devolucion {

    private long id;
    private String codigo;
    private String nombre;
    private String observacion;
    private List<Linea> lineas;
    private List<Adjunto> adjuntos;

    /**
     * Constructor that create a empty objetc
     */
    public Devolucion(){
        this.lineas=new ArrayList<>();
        this.adjuntos=new ArrayList<>();
    }

    public Devolucion(String codigo, String nombre, String observacion) {
        this.id=0;
        this.codigo = codigo;
        this.nombre = nombre;
        this.observacion=observacion;
        this.lineas=new ArrayList<>();
        this.adjuntos=new ArrayList<>();
    }

    public Devolucion(Cursor cursor){
        this.id=cursor.getLong(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[0]));
        this.codigo=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[1]));
        this.nombre=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[2]));
        this.observacion=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[3]));
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<Linea> getLineas() {
        return lineas;
    }

    public void setLineas(List<Linea> lineas) {
        this.lineas = lineas;
    }

    public void setLinea(Linea linea){
        int pos=-1;
        for(int i=0;i<lineas.size() && linea.getId()!=0;i++){
            if(linea.getId()==lineas.get(i).getId()){
                pos=i;
                break;
            }
        }
        if(pos>=0)
            lineas.remove(pos);
        lineas.add(linea);
    }

    public void replace(Linea linea, int pos){
        lineas.remove(pos);
        lineas.add(pos,linea);
    }

    public Linea getLinea(long id){
        for(Linea l : lineas){
            if(l.getId()==id)
                return l;
        }
        return null;
    }

    public void removeLinea(int pos){
        lineas.remove(pos);
    }

    public List<Adjunto> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<Adjunto> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public void setAdjunto(Adjunto a){
        adjuntos.add(a);
    }

    public Adjunto getAdjunto(long id){
        for(Adjunto a : adjuntos){
            if(a.getId()==id){
                return a;
            }
        }
        return null;
    }

    public boolean tieneCambios(Devolucion devolucion){
        if(!this.getCodigo().equals(devolucion.getCodigo())) return true;
        if(!this.getNombre().equals(devolucion.getNombre())) return true;
        return false;
    }

}
