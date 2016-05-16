package com.pcs.vicchiam.devoluciones.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicch on 16/05/2016.
 */
public class DevolucionDB extends SQLiteOpenHelper{

    public static final String DATABASE_NAME="Devoluciones.db";

    public static final String PRAGMA_SQL="PRAGMA foreign_keys = ON;";

    public static final String TABLE_NAME_DEVOLUCION="devolucion";
    public static final String TABLE_NAME_LINEA="linea";
    public static final String TABLE_NAME_ADJUNTO="adjunto";

    public static final String[] COLS_DEVOLUCION={"id","codigo","cliente","razon","accion","motivo"};
    public static final String[] COLS_TYPE_DEVOLUCION={"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","TEXT","TEXT","TEXT","TEXT"};

    public static final String[] COLS_LINEA={"id","codigo","descripcion","cantidad","umv","lote","caducidad","id_devolucion"};
    public static final String[] COLS_TYPE_LINEA={"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","TEXT","NUMBER","TEXT","TEXT","TEXT","INTEGER"};

    public static final String[] COLS_ADJUNTO={"id","ruta","id_devolucion"};
    public static final String[] COLS_TYPE_ADJUNTO={"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","INTEGER"};

    public static final String FOREIGN_KEY=" FOREIGN KEY(id_devolucion) REFERENCES devolucion(id) ";

    public static String CREATE_SQL_DEVOLUCION(){
        String SQL="CREATE TABLE IF NOT EXISTS "+TABLE_NAME_DEVOLUCION+" (";
        for(int i=0;i<(COLS_DEVOLUCION.length-1);i++){
            SQL+=COLS_DEVOLUCION[i]+" "+COLS_TYPE_DEVOLUCION[i]+", ";
        }
        SQL+=COLS_DEVOLUCION[COLS_DEVOLUCION.length-1]+" "+COLS_TYPE_DEVOLUCION[COLS_DEVOLUCION.length-1]+")";
        return SQL;
    }

    public static String CREATE_SQL_LINEA(){
        String SQL="CREATE TABLE IF NOT EXISTS "+TABLE_NAME_LINEA+" (";
        for(int i=0;i<(COLS_LINEA.length-1);i++){
            SQL+=COLS_LINEA[i]+" "+COLS_TYPE_LINEA[i]+", ";
        }
        SQL+=COLS_LINEA[COLS_LINEA.length-1]+" "+COLS_TYPE_LINEA[COLS_LINEA.length-1]+")";
        SQL+=FOREIGN_KEY;
        return SQL;
    }

    public static String CREATE_SQL_ADJUNTO(){
        String SQL="CREATE TABLE IF NOT EXISTS "+TABLE_NAME_ADJUNTO+" (";
        for(int i=0;i<(COLS_ADJUNTO.length-1);i++){
            SQL+=COLS_ADJUNTO[i]+" "+COLS_TYPE_ADJUNTO[i]+", ";
        }
        SQL+=COLS_ADJUNTO[COLS_ADJUNTO.length-1]+" "+COLS_TYPE_ADJUNTO[COLS_ADJUNTO.length-1]+")";
        SQL+=FOREIGN_KEY;
        return SQL;
    }

    public static final String DROP_SQL_DEVOLUCION="DROP TABLE IF EXISTS "+TABLE_NAME_DEVOLUCION;
    public static final String DROP_SQL_LINEA="DROP TABLE IF EXISTS "+TABLE_NAME_LINEA;
    public static final String DROP_SQL_ADJUNTO="DROP TABLE IF EXISTS "+TABLE_NAME_ADJUNTO;

    public DevolucionDB(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PRAGMA_SQL);
        db.execSQL(CREATE_SQL_DEVOLUCION());
        db.execSQL(CREATE_SQL_LINEA());
        db.execSQL(CREATE_SQL_ADJUNTO());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_SQL_ADJUNTO);
        db.execSQL(DROP_SQL_LINEA);
        db.execSQL(DROP_SQL_DEVOLUCION);
        onCreate(db);
    }

    public void truncate(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(DROP_SQL_ADJUNTO);
        db.execSQL(DROP_SQL_LINEA);
        db.execSQL(DROP_SQL_DEVOLUCION);
        onCreate(db);
    }

    /**DEVOLUCIONES********************************************************************************/

    public List<Devolucion> obtenerTodosDevoluciones(){
        List<Devolucion> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_DEVOLUCION, null);
        return this.prepararListadoDevoluciones(res);
    }

    public List<Devolucion> buscarDevolucion(String columna, String valor){
        List<Devolucion> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_DEVOLUCION+" WHERE "+columna+"="+valor+"",null);
        return this.prepararListadoDevoluciones(res);
    }

    public Cursor autocompletar(String columna, String valor){
        SQLiteDatabase db=this.getReadableDatabase();
        String SQL="SELECT id as _id, codigo, articulo, umv FROM "+TABLE_NAME_DEVOLUCION+" WHERE LOWER("+columna+") LIKE '%"+valor+"%' order by id LIMIT 40";
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_DEVOLUCION+" WHERE "+columna+"="+valor+"",null);
        return res;
    }

    public Devolucion insertar(String codigo, String razon, String accion, String motivo){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("razon",razon);
        cv.put("accion",accion);
        cv.put("motivo",motivo);
        long id=db.insert(TABLE_NAME_DEVOLUCION,null,cv);
        Devolucion dev=new Devolucion(codigo, razon, accion, motivo);
        dev.setId(id);
        return dev;
    }

    public Devolucion remplazar(String codigo, String razon, String accion, String motivo){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("razon",razon);
        cv.put("accion",accion);
        cv.put("motivo",motivo);
        db.replace(TABLE_NAME_DEVOLUCION,null,cv);
        return new Devolucion(codigo, razon, accion, motivo);
    }

    public void eliminar(long id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME_DEVOLUCION,"id="+id,null);
    }

    private List<Devolucion> prepararListadoDevoluciones(Cursor cursor){
        List<Devolucion> list=new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            list.add(new Devolucion(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /**********************************************************************************************/

    /**LINEAS********************************************************************************/
    /*
    public List<Linea> obtenerTodosLineas(){
        List<Devolucion> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_LINEA, null);
        return this.prepararListadoLineas(res);
    }

    public List<Devolucion> buscarDevolucion(String columna, String valor){
        List<Devolucion> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_DEVOLUCION+" WHERE "+columna+"="+valor+"",null);
        return this.prepararListadoDevoluciones(res);
    }

    public Cursor autocompletar(String columna, String valor){
        SQLiteDatabase db=this.getReadableDatabase();
        String SQL="SELECT id as _id, codigo, articulo, umv FROM "+TABLE_NAME_DEVOLUCION+" WHERE LOWER("+columna+") LIKE '%"+valor+"%' order by id LIMIT 40";
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_DEVOLUCION+" WHERE "+columna+"="+valor+"",null);
        return res;
    }

    public Devolucion insertar(String codigo, String razon, String accion, String motivo){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("razon",razon);
        cv.put("accion",accion);
        cv.put("motivo",motivo);
        long id=db.insert(TABLE_NAME_DEVOLUCION,null,cv);
        Devolucion dev=new Devolucion(codigo, razon, accion, motivo);
        dev.setId(id);
        return dev;
    }

    public Devolucion remplazar(String codigo, String razon, String accion, String motivo){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("razon",razon);
        cv.put("accion",accion);
        cv.put("motivo",motivo);
        db.replace(TABLE_NAME_DEVOLUCION,null,cv);
        return new Devolucion(codigo, razon, accion, motivo);
    }

    public void eliminar(long id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME_DEVOLUCION,"id="+id,null);
    }

    private List<Linea> prepararListadoLineas(Cursor cursor){
        List<Linea> list=new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            list.add(new Linea(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    */
    /**********************************************************************************************/


}
