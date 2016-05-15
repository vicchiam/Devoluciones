package com.pcs.vicchiam.devoluciones.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicch on 11/05/2016.
 */
public class ArticuloDB extends SQLiteOpenHelper{

    public static final String DATABASE_NAME="Devoluciones.db";
    public static final String TABLE_NAME_ARTICULO="ARTICULO";

    public static final String[] COLS_ARTICULO={"codigo","articulo","umv"};
    public static final String[] COLS_TYPE_ARTICULO={"INTEGER PRIMARY KEY","TEXT","TEXT"};

    public static String CREATE_SQL(){
        String SQL="CREATE TABLE IF NOT EXISTS "+TABLE_NAME_ARTICULO+" (";
        for(int i=0;i<(COLS_ARTICULO.length-1);i++){
            SQL+=COLS_ARTICULO[i]+" "+COLS_TYPE_ARTICULO[i]+", ";
        }
        SQL+=COLS_ARTICULO[COLS_ARTICULO.length-1]+" "+COLS_TYPE_ARTICULO[COLS_ARTICULO.length-1]+")";
        return SQL;
    }

    public static final String DROP_SQL="DROP TABLE IF EXISTS "+TABLE_NAME_ARTICULO;

    public ArticuloDB(Context context){
        super(context,DATABASE_NAME,null,1);
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

    public void truncate(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(DROP_SQL);
        onCreate(db);
    }

    public List<Articulo> obtenerTodos(){
        List<Cliente> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_ARTICULO, null);
        return this.prepararListado(res);
    }

    public List<Articulo> buscar(String columna, String valor){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_ARTICULO+" WHERE ?=?",new String[]{columna,valor});
        return this.prepararListado(res);
    }

    public Cursor autocompletar(String columna,String valor){
        SQLiteDatabase db=this.getReadableDatabase();
        String SQL="SELECT codigo as _id, codigo, articulo, umv FROM "+TABLE_NAME_ARTICULO+" WHERE LOWER("+columna+") LIKE '%"+valor+"%' order by codigo LIMIT 40";
        Cursor res=db.rawQuery(SQL,null);
        return  res;
    }

    public Articulo insertar(int codigo,String articulo, String umv){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("articulo",articulo);
        cv.put("umv",umv);
        db.insert(TABLE_NAME_ARTICULO,null,cv);
        return new Articulo(codigo, articulo, umv);
    }

    public void reemplazar(int codigo, String articulo, String umv){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("articulo",articulo);
        cv.put("umv",umv);
        db.replace(TABLE_NAME_ARTICULO,null,cv);
    }

    public void eliminar(int codigo){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME_ARTICULO,"codigo="+codigo,null);
    }


    private List<Articulo> prepararListado(Cursor cursor){
        List<Articulo> list=new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            list.add(new Articulo(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

}
