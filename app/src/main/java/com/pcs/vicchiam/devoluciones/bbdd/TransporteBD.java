package com.pcs.vicchiam.devoluciones.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 02/06/2016.
 */
public class TransporteBD extends SQLiteOpenHelper{

    public static final String DATABASE_NAME="Devoluciones.db";

    public static final String TABLE_NAME="transporte";

    public static final String[] COLS={"id","cliente","nombre","fecha","obs"};

    public static final String[] COLS_TYPE={"INTEGER PRIMARY KEY","TEXT","TEXT","TEXT","TEXT"};

    /**
     * Make a create Transporte table SQL
     * @return
     */
    public static String CREATE_SQL(){
        String SQL="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (";
        for(int i=0;i<(COLS.length-1);i++){
            SQL+=COLS[i]+" "+COLS_TYPE[i]+", ";
        }
        SQL+=COLS[COLS.length-1]+" "+COLS_TYPE[COLS.length-1]+")";
        return SQL;
    }

    public static final String DROP_SQL="DROP TABLE IF EXISTS "+TABLE_NAME;


    public TransporteBD(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_SQL);
        db.execSQL(CREATE_SQL());
    }

    /**
     * Delete and create Transporte table
     */
    public void truncate(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(DROP_SQL);
        onCreate(db);
    }

    public List<Transporte> obtenerTransporte(){
        List<Transporte> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME+" ORDER BY fecha DESC",null);
        return prepararTransporte(res);
    }

    public Transporte insertarTransporte(long id, String cliente, String nombre, String fecha, String obs){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("id",id);
        cv.put("cliente",cliente);
        cv.put("nombre",nombre);
        cv.put("fecha",fecha);
        cv.put("obs",obs);
        db.insert(TABLE_NAME,null,cv);
        return new Transporte(id, cliente, nombre, fecha, obs);
    }

    /**
     * Prepare a list of transports from the database cursor
     * @param cursor
     * @return
     */
    public List<Transporte> prepararTransporte(Cursor cursor){
        List<Transporte> list= new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            list.add(new Transporte(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

}
