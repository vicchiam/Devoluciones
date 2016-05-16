package com.pcs.vicchiam.devoluciones.bbdd;

/**
 * Created by vicch on 16/05/2016.
 */
public class Linea {

    private long id;
    private String codigo;
    private String nombre;
    private double cantidad;
    private String umv;
    private String lote;
    private String fecha;

    public Linea(String codigo, String nombre, double cantidad, String umv, String lote, String fecha) {
        this.id=0;
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.umv = umv;
        this.lote = lote;
        this.fecha = fecha;
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
}
