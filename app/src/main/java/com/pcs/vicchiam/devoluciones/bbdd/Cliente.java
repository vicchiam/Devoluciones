package com.pcs.vicchiam.devoluciones.bbdd;

import android.database.Cursor;

/**
 * Created by vicchiam on 06/05/2016.
 *
 * Class that represents a customer object
 */
public class Cliente {

    private int codigo;
    private String nombre;

    /**
     * Constructor of Cliente
     * @param cursor database cursor
     */
    public Cliente(Cursor cursor){
        this.codigo=cursor.getInt(cursor.getColumnIndex("codigo"));
        this.nombre=cursor.getString(cursor.getColumnIndex("nombre"));
    }

    /**
     * Constructor of Cliente
     * @param codigo
     * @param nombre
     */
    public Cliente(int codigo, String nombre){
        this.codigo=codigo;
        this.nombre=nombre;
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

    @Override
    public String toString() {
        return "Cliente{" +
                "codigo=" + codigo +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
