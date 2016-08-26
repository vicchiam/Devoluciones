package com.pcs.vicchiam.devoluciones.bbdd;

import android.database.Cursor;
import android.util.Log;

import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

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
    private String fecha;
    private long id_transporte;
    private long id_servidor;
    private List<Linea> lineas;
    private List<Adjunto> adjuntos;

    /**
     * Constructor that create a empty objetc
     */
    public Devolucion(){
        this.id=0;
        this.codigo="";
        this.nombre="";
        this.observacion="";
        this.fecha=Utilidades.hoy();
        this.id_transporte=0;
        this.id_servidor=0;
        this.lineas=new ArrayList<>();
        this.adjuntos=new ArrayList<>();
    }

    public Devolucion(String codigo, String nombre, String observacion) {
        this.id=0;
        this.codigo = codigo;
        this.nombre = nombre;
        this.observacion=observacion;
        this.fecha=Utilidades.hoy();
        this.id_transporte=0;
        this.id_servidor=0;
        this.lineas=new ArrayList<>();
        this.adjuntos=new ArrayList<>();
    }

    public Devolucion(Cursor cursor){
        this.id=cursor.getLong(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[0]));
        this.codigo=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[1]));
        this.nombre=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[2]));
        this.observacion=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[3]));
        this.fecha=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[4]));

        this.id_transporte=cursor.getLong(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[6]));
        this.id_servidor=cursor.getLong(cursor.getColumnIndex(DevolucionDB.COLS_DEVOLUCION[7]));
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public long getId_transporte() {
        return id_transporte;
    }

    public void setId_transporte(long id_transporte) {
        this.id_transporte = id_transporte;
    }

    public long getId_servidor() {
        return id_servidor;
    }

    public void setId_servidor(long id_servidor) {
        this.id_servidor = id_servidor;
    }

    public List<Linea> getLineas() {
        return lineas;
    }

    public void setLineas(List<Linea> lineas) {
        this.lineas = lineas;
    }

    public void setLinea(Linea linea, DevolucionDB db){
        int pos=-1;
        for(int i=0;i<lineas.size() && linea.getId()!=0;i++){
            if(linea.getId()==lineas.get(i).getId()){
                pos=i;
                break;
            }
        }
        if(pos>=0) {
            lineas.remove(pos);
        }
        lineas.add(linea);

        if(id>0) {
            if (linea.getId() == 0) {
                linea.agregar(db, id);
            } else {
                linea.modificar(db);
            }
        }

    }

    public void actualizarLinea(Linea linea, int pos, DevolucionDB db){
        if(id>0) {
            linea.modificar(db);
        }
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

    public void eliminarLinea(int pos, DevolucionDB db){
        if(lineas.get(pos).getId()>0){
            db.eliminarLinea(lineas.get(pos).getId());
        }
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
        if(id==0 && lineas.size()>0) return true;
        return false;
    }

    public void agregar(DevolucionDB db){
        long id=db.insertarDevolucion(codigo,nombre,observacion,id_transporte);
        for(Linea l : lineas){
            l.agregar(db,id);
        }
        this.id=id;
    }

    public void modificar(DevolucionDB db){
        db.remplazarDevolucion(id,codigo,nombre,observacion,id_transporte);
    }

    public void eliminar(DevolucionDB db){
        db.eliminarDevolucion(id);
    }

    @Override
    public String toString() {
        return "Devolucion{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", observacion='" + observacion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", id_transporte=" + id_transporte +
                ", id_servidor=" + id_servidor +
                ", lineas=" + lineas +
                ", adjuntos=" + adjuntos +
                '}';
    }

    public String toJSON(){
        String l="";
        for(int i=0;i<lineas.size();i++){
            if(i>0){
                l+=",";
            }
            l+=lineas.get(i).toJSON();
        }

        String res="{" +
                "\"codigo\":\""+codigo+"\","+
                "\"nombre\":\""+nombre+"\","+
                "\"observacion\":\""+observacion+"\","+
                "\"fecha\":\""+fecha+"\","+
                "\"id_transporte\":"+id_transporte+","+
                "\"id\":"+id_servidor+","+
                "\"lineas\":["+l+"]}";

        return res;

    }

}
