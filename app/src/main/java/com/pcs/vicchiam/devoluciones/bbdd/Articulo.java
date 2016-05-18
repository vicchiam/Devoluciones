package com.pcs.vicchiam.devoluciones.bbdd;

import android.database.Cursor;

/**
 * Created by vicchiam on 11/05/2016.
 * Class that represent a Article
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

    /**
     * Constructor that make a Article from a database Cirsor
     * @param cursor database cursor
     */
    public Articulo(Cursor cursor){
        this.codigo=cursor.getInt(cursor.getColumnIndex(ArticuloDB.COLS_ARTICULO[0]));
        this.nombre=cursor.getString(cursor.getColumnIndex(ArticuloDB.COLS_ARTICULO[1]));
        this.umv=cursor.getString(cursor.getColumnIndex(ArticuloDB.COLS_ARTICULO[2]));
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
