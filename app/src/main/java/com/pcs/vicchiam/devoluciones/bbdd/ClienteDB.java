package com.pcs.vicchiam.devoluciones.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicchiam on 06/05/2016.
 *
 * Class that makes the  customer database and your operations
 */
public class ClienteDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Devoluciones.db";
    public static final String TABLE_NAME_CLIENTE="CLIENTE";

    public static final String[] COLS_CLIENTE={"codigo","nombre"};
    public static final String[] COLS_TYPE_CLIENTE={"INTEGER PRIMARY KEY","TEXT"};

    /**
     * Craete a SQL for create table Customer
     * @return
     */
    public static String CREATE_SQL(){
        String SQL="CREATE TABLE IF NOT EXISTS "+TABLE_NAME_CLIENTE+" (";
        for(int i=0;i<(COLS_CLIENTE.length-1);i++){
            SQL+=COLS_CLIENTE[i]+" "+COLS_TYPE_CLIENTE[i]+", ";
        }
        SQL+=COLS_CLIENTE[COLS_CLIENTE.length-1]+" "+COLS_TYPE_CLIENTE[COLS_CLIENTE.length-1]+")";
        return SQL;
    }

    public static final String DROP_SQL="DROP TABLE IF EXISTS "+TABLE_NAME_CLIENTE;

    /**
     * Constructor of ClienteDB
     * @param context
     */
    public ClienteDB(Context context){
        super(context,DATABASE_NAME,null,1);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_SQL);
        onCreate(db);
    }

    /**
     * Remove the Customer table of the database
     */
    public void truncate(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(DROP_SQL);
        onCreate(db);
    }

    /**
     * Get all customers
     * @return
     */
    public List<Cliente> obtenerTodos(){
        List<Cliente> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_CLIENTE, null);
        return this.prepararListado(res);
    }

    /**
     * Find the specific customer
     * @param columna
     * @param valor
     * @return
     */
    public List<Cliente> buscar(String columna, String valor){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_CLIENTE+" WHERE ?=?",new String[]{columna,valor});
        return this.prepararListado(res);
    }

    /**
     * To autocompete SQL, this need a _id column
     * @param columna column to search
     * @param valor value to search
     * @return cursor with the results
     */
    public Cursor autocompletar(String columna,String valor){
        SQLiteDatabase db=this.getReadableDatabase();
        String SQL="SELECT codigo as _id, codigo, nombre FROM "+TABLE_NAME_CLIENTE+" WHERE LOWER("+columna+") LIKE '%"+valor+"%' order by codigo LIMIT 40";
        Cursor res=db.rawQuery(SQL,null);
        return  res;
    }

    /**
     * Insert a customer in database
     * @param codigo
     * @param nombre
     * @return
     */
    public Cliente insertar(int codigo,String nombre){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("nombre",nombre);
        db.insert(TABLE_NAME_CLIENTE,null,cv);
        return new Cliente(codigo, nombre);
    }

    /**
     * Insert or update a custome in database
     * @param codigo
     * @param nombre
     */
    public void reemplazar(int codigo, String nombre){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("nombre",nombre);
        db.replace(TABLE_NAME_CLIENTE,null,cv);
    }

    /**
     * Deleta a customer in database
     * @param codigo
     */
    public void eliminar(int codigo){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME_CLIENTE,"codigo="+codigo,null);
    }

    /**
     * Makes a list of customers
     * @param cursor
     * @return
     */
    private List<Cliente> prepararListado(Cursor cursor){
        List<Cliente> list=new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            list.add(new Cliente(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

}
