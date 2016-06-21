package com.pcs.vicchiam.devoluciones.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicchiam on 11/05/2016.
 * Class that make an hand all operations of the articles in SQLite database
 */
public class ArticuloDB extends SQLiteOpenHelper{

    public static final String DATABASE_NAME="Devoluciones.db";
    public static final String TABLE_NAME_ARTICULO="ARTICULO";

    public static final String[] COLS_ARTICULO={"codigo","articulo","umv"};
    public static final String[] COLS_TYPE_ARTICULO={"INTEGER PRIMARY KEY","TEXT","TEXT"};

    /**
     * Make a SQL create string
     * @return SQL create string
     */
    public static String CREATE_SQL(){
        String SQL="CREATE TABLE IF NOT EXISTS "+TABLE_NAME_ARTICULO+" (";
        for(int i=0;i<(COLS_ARTICULO.length-1);i++){
            SQL+=COLS_ARTICULO[i]+" "+COLS_TYPE_ARTICULO[i]+", ";
        }
        SQL+=COLS_ARTICULO[COLS_ARTICULO.length-1]+" "+COLS_TYPE_ARTICULO[COLS_ARTICULO.length-1]+")";
        return SQL;
    }

    /**
     * Make a drop SQL string for delete all table
     */
    public static final String DROP_SQL="DROP TABLE IF EXISTS "+TABLE_NAME_ARTICULO;

    public ArticuloDB(Context context){
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
     * Deleta and create table
     */
    public void truncate(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(DROP_SQL);
        onCreate(db);
    }

    /**
     * Get all articles
     * @return list of all articles
     */
    public List<Articulo> obtenerTodos(){
        List<Articulo> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_ARTICULO, null);
        return this.prepararListado(res);
    }

    /**
     * Search a article for your column and value
     * @param columna column of table
     * @param valor value to search
     * @return a list of articles that check the query
     */
    public List<Articulo> buscar(String columna, String valor){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_ARTICULO+" WHERE "+columna+"="+valor+"",null);
        return this.prepararListado(res);
    }

    /**
     * Function for return a autocomplet query
     * @param columna column to search
     * @param valor value to search
     * @return a list of articles that check this query
     */
    public Cursor autocompletar(String columna,String valor){
        SQLiteDatabase db=this.getReadableDatabase();
        String SQL="SELECT codigo as _id, codigo, articulo, umv FROM "+TABLE_NAME_ARTICULO+" WHERE LOWER("+columna+") LIKE '%"+valor+"%' order by codigo LIMIT 40";
        Cursor res=db.rawQuery(SQL,null);
        return  res;
    }

    /**
     * Inseert article in database
     * @param codigo
     * @param articulo
     * @param umv
     * @return
     */
    public Articulo insertar(int codigo,String articulo, String umv){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("articulo",articulo);
        cv.put("umv",umv);
        db.insert(TABLE_NAME_ARTICULO,null,cv);
        return new Articulo(codigo, articulo, umv);
    }

    /**
     * Insert or update article in database
     * @param codigo
     * @param articulo
     * @param umv
     */
    public void reemplazar(int codigo, String articulo, String umv){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("articulo",articulo);
        cv.put("umv",umv);
        db.replace(TABLE_NAME_ARTICULO,null,cv);
    }

    /**
     * Delete a article in database
     * @param codigo
     */
    public void eliminar(int codigo){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME_ARTICULO,"codigo="+codigo,null);
    }

    /**
     * Make a list of articles from a database cursor
     * @param cursor database cursor
     * @return list of articles
     */
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
