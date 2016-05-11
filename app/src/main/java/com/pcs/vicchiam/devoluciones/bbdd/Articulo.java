package com.pcs.vicchiam.devoluciones.bbdd;

import android.database.Cursor;

/**
 * Created by vicch on 11/05/2016.
 */
public class Articulo {

    private int codigo;
    private String nombre;
    private String umv;

    public Articulo(int codigo, String nombre, String umv){
        this.codigo=codigo;
        this.nombre=nombre;
        this.umv=umv;
    }

    public Articulo(Cursor cursor){
        this.codigo=cursor.getInt(cursor.getColumnIndex("codigo"));
        this.nombre=cursor.getString(cursor.getColumnIndex("nombre"));
        this.umv=cursor.getString(cursor.getColumnIndex("umv"));
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUmv() {
        return umv;
    }

    public void setUmv(String umv) {
        this.umv = umv;
    }
}
