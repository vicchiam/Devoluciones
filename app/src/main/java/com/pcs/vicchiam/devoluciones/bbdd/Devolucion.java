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
    private String accion;
    private String motivo;
    private List<Linea> lineas;
    private List<Adjunto> adjuntos;

    /**
     * Constructor that create a empty objetc
     */
    public Devolucion(){
        this.lineas=new ArrayList<>();
        this.adjuntos=new ArrayList<>();
    }

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

    public List<Adjunto> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<Adjunto> adjuntos) {
        this.adjuntos = adjuntos;
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

    public void setAdjunto(Adjunto a){
        adjuntos.add(a);
    }

    public Linea getLinea(long id){
        for(Linea l : lineas){
            if(l.getId()==id)
                return l;
        }
        return null;
    }

    public Adjunto getAdjunto(long id){
        for(Adjunto a : adjuntos){
            if(a.getId()==id){
                return a;
            }
        }
        return null;
    }

}
