package com.pcs.vicchiam.devoluciones.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pcs.vicchiam.devoluciones.utilidades.Utilidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicchiam on 16/05/2016.
 * Class that make all operations of Devolution, Line, and attachment to database
 */
public class DevolucionDB extends SQLiteOpenHelper{

    public static final String DATABASE_NAME="Devoluciones.db";

    public static final String PRAGMA_SQL="PRAGMA foreign_keys = ON";

    public static final String TABLE_NAME_DEVOLUCION="devolucion";
    public static final String TABLE_NAME_LINEA="linea";
    public static final String TABLE_NAME_ADJUNTO="adjunto";

    public static final String[] COLS_DEVOLUCION={"id","codigo","razon","observacion","fecha","tipo","id_transporte","id_servidor"};
    public static final String[] COLS_TYPE_DEVOLUCION={"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","TEXT","TEXT","TEXT","INTEGER","INTEGER","INTEGER"};

    public static final String[] COLS_LINEA={"id","codigo","descripcion","cantidad","umv","lote","caducidad","accion","motivo","id_devolucion"};
    public static final String[] COLS_TYPE_LINEA={"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","TEXT","NUMBER","TEXT","TEXT","TEXT","TEXT","TEXT","INTEGER"};

    public static final String[] COLS_ADJUNTO={"id","path","id_devolucion"};
    public static final String[] COLS_TYPE_ADJUNTO={"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","INTEGER"};

    public static final String FOREIGN_KEY_LINEA=" FOREIGN KEY(id_devolucion) REFERENCES devolucion(id) ON DELETE CASCADE";

    /**
     * Make a create Devolution table SQL
     * @return
     */
    public static String CREATE_SQL_DEVOLUCION(){
        String SQL="CREATE TABLE IF NOT EXISTS "+TABLE_NAME_DEVOLUCION+" (";
        for(int i=0;i<(COLS_DEVOLUCION.length-1);i++){
            SQL+=COLS_DEVOLUCION[i]+" "+COLS_TYPE_DEVOLUCION[i]+", ";
        }
        SQL+=COLS_DEVOLUCION[COLS_DEVOLUCION.length-1]+" "+COLS_TYPE_DEVOLUCION[COLS_DEVOLUCION.length-1]+")";
        return SQL;
    }

    /**
     * Make a create Line table SQL
     * @return
     */
    public static String CREATE_SQL_LINEA(){
        String SQL="CREATE TABLE IF NOT EXISTS "+TABLE_NAME_LINEA+" (";
        for(int i=0;i<(COLS_LINEA.length-1);i++){
            SQL+=COLS_LINEA[i]+" "+COLS_TYPE_LINEA[i]+", ";
        }
        SQL+=COLS_LINEA[COLS_LINEA.length-1]+" "+COLS_TYPE_LINEA[COLS_LINEA.length-1];
        SQL+=","+FOREIGN_KEY_LINEA+")";
        return SQL;
    }

    /**
     * Make a create attachment table SQL
     * @return
     */
    public static String CREATE_SQL_ADJUNTO(){
        String SQL="CREATE TABLE IF NOT EXISTS "+TABLE_NAME_ADJUNTO+" (";
        for(int i=0;i<(COLS_ADJUNTO.length-1);i++){
            SQL+=COLS_ADJUNTO[i]+" "+COLS_TYPE_ADJUNTO[i]+", ";
        }
        SQL+=COLS_ADJUNTO[COLS_ADJUNTO.length-1]+" "+COLS_TYPE_ADJUNTO[COLS_ADJUNTO.length-1];
        SQL+=","+FOREIGN_KEY_LINEA+")";
        return SQL;
    }

    /**
     * Make a delete all table SQL
     */
    public static final String DROP_SQL_DEVOLUCION="DROP TABLE IF EXISTS "+TABLE_NAME_DEVOLUCION;
    public static final String DROP_SQL_LINEA="DROP TABLE IF EXISTS "+TABLE_NAME_LINEA;
    public static final String DROP_SQL_ADJUNTO="DROP TABLE IF EXISTS "+TABLE_NAME_ADJUNTO;

    public DevolucionDB(Context context){
        super(context,DATABASE_NAME,null,1);
        onCreate(getWritableDatabase());
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

    /**
     * Delete and create Devolution, Line and attachment table
     */
    public void truncate(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(DROP_SQL_ADJUNTO);
        db.execSQL(DROP_SQL_LINEA);
        db.execSQL(DROP_SQL_DEVOLUCION);
        onCreate(db);
    }

    /**DEVOLUCIONES********************************************************************************/

    /**
     * Get all devolutions
     * @return list of devolutions
     */
    public List<Devolucion> obtenerTodasDevoluciones(){
        List<Devolucion> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_DEVOLUCION, null);
        return this.prepararListadoDevoluciones(res);
    }

    /**
     * Search a devolutions that check a column and value
     * @param columna Column to search
     * @param valor value to search
     * @return a list o devolutions that check the query
     */
    public List<Devolucion> buscarDevolucion(String columna, String valor){
        List<Devolucion> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_DEVOLUCION+" WHERE "+columna+"="+valor+"",null);
        return this.prepararListadoDevoluciones(res);
    }

    /**
     * Get devolution for your ID
     * @param id the ID to search
     * @return a line with these ID
     */
    public Devolucion buscarDevolucionId(long id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_DEVOLUCION+" WHERE id="+id+"",null);
        cursor.moveToFirst();
        return new Devolucion(cursor);
    }

    public  List<Devolucion> obtenerDevolucionesNoEnviadas(){
        List<Devolucion> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_DEVOLUCION+" WHERE id_servidor=0",null);
        return this.prepararListadoDevoluciones(res);
    }

    public  List<Devolucion> obtenerDevolucionesEnviadas(){
        List<Devolucion> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_DEVOLUCION+" WHERE id_servidor<>0",null);
        return this.prepararListadoDevoluciones(res);
    }

    /**
     * Make a data of the autocomplet widget
     * @param columna column that search
     * @param valor value that search
     * @return a list of devolutions that check with the query
     */
    public Cursor autocompletar(String columna, String valor){
        SQLiteDatabase db=this.getReadableDatabase();
        String SQL="SELECT id as _id, codigo, articulo, umv FROM "+TABLE_NAME_DEVOLUCION+" WHERE LOWER("+columna+") LIKE '%"+valor+"%' order by id LIMIT 40";
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_DEVOLUCION+" WHERE "+columna+"="+valor+"",null);
        return res;
    }

    /**
     * Insert a devolution in the databasae
     * @param codigo
     * @param razon
     * @param observacion
     * @return
     */
    public long insertarDevolucion(String codigo, String razon, String observacion, long id_trans){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("razon",razon);
        cv.put("observacion",observacion);
        cv.put("fecha",Utilidades.hoy());
        cv.put("id_transporte",id_trans);
        cv.put("id_servidor",0);
        long id=db.insert(TABLE_NAME_DEVOLUCION,null,cv);
        //Devolucion dev=new Devolucion(codigo, razon, observacion);
        //dev.setId(id);
        return id;
    }

    /**
     * Insert or update a devolution in database
     * @param codigo
     * @param razon
     * @param observacion
     * @return
     */
    public long remplazarDevolucion(long id, String codigo, String razon, String observacion, long id_trans){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("razon",razon);
        cv.put("observacion",observacion);
        cv.put("fecha",Utilidades.hoy());
        cv.put("id_transporte",id_trans);
        db.update(TABLE_NAME_DEVOLUCION,cv,"id="+id,null);
        return id;
    }

    public void agregarDevolucionIdServidor(long id, long id_servidor){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("id_servidor",id_servidor);
        db.update(TABLE_NAME_DEVOLUCION,cv,"id="+id,null);
        db.close();
    }

    /*
    public boolean existeTrasporte(long id_transporte){
        List<Devolucion> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_DEVOLUCION+" WHERE id_transporte="+id_transporte+"",null);
        cursor.moveToFirst();
        Log.e("ESTA",cursor.getCount()+"");
        int res=cursor.getCount();
        cursor.close();
        return (res>0);
    }
    */

    /**
     * Delete a devolution in dtatabse
     * @param id ID to the devolution
     */
    public void eliminarDevolucion(long id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME_DEVOLUCION,"id="+id,null);
    }

    /**
     * Prepare a list of devolutions from the database cursor
     * @param cursor
     * @return
     */
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

    /**
     * Search a lines of check the ID of devolution
     * @param id ID of devolution to search
     * @return a Line with these ID
     */
    public List<Linea> obtenerTodosLineas(long id){
        List<Devolucion> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_LINEA+" WHERE id_devolucion="+id, null);
        return this.prepararListadoLineas(res);
    }

    /**
     * Search a line for ID
     * @param id a ID of line that want find
     * @return a line with these ID
     */
    public Linea buscarLinea(long id){
        List<Linea> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_LINEA+" WHERE id="+id+"",null);
        cursor.moveToFirst();
        return new Linea(cursor);
    }

    /**+
     * Insert a line in database
     * @param codigo
     * @param descripcion
     * @param cantidad
     * @param umv
     * @param lote
     * @param caducidad
     * @param accion
     * @param motivo
     * @param id_devolucion
     * @return
     */
    public long insertarLinea(String codigo, String descripcion, double cantidad, String umv, String lote, String caducidad, String accion, String motivo, long id_devolucion){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("descripcion",descripcion);
        cv.put("cantidad",cantidad);
        cv.put("umv",umv);
        cv.put("lote",lote);
        cv.put("caducidad",caducidad);
        cv.put("accion",accion);
        cv.put("motivo",motivo);
        cv.put("id_devolucion",id_devolucion);
        long id=db.insert(TABLE_NAME_LINEA,null,cv);
        //Linea linea=new Linea(codigo,descripcion,cantidad,umv,lote,caducidad,accion,motivo);
        //linea.setId(id);
        return id;
    }

    /**
     * Insert or replace a line in dtabase
     * @param codigo
     * @param descripcion
     * @param cantidad
     * @param umv
     * @param lote
     * @param caducidad
     * @param accion
     * @param motivo
     * @return
     */
    public long remplazarLinea(long id,String codigo, String descripcion, double cantidad, String umv, String lote, String caducidad, String accion, String motivo){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("descripcion",descripcion);
        cv.put("cantidad",cantidad);
        cv.put("umv",umv);
        cv.put("lote",lote);
        cv.put("caducidad",caducidad);
        cv.put("accion",accion);
        cv.put("motivo",motivo);
        db.replace(TABLE_NAME_LINEA,null,cv);
        db.update(TABLE_NAME_LINEA,cv,"id="+id,null);
        return id;
    }

    /**
     * Deleta line in database
     * @param id
     */
    public void eliminarLinea(long id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME_LINEA,"id="+id,null);
    }

    /**
     * Get a list of lines
     * @param cursor
     * @return
     */
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

    /**********************************************************************************************/

    /**ADJUNTOS********************************************************************************/

    /**
     * Get all attachment files from a ID devolution
     * @param id the Devolution ID
     * @return the attach that check these ID
     */
    public List<Adjunto> obtenerTodosAdjuntos(long id){
        List<Devolucion> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME_ADJUNTO+" WHERE id_devolucion="+id , null);
        return this.prepararListadoAdjuntos(res);
    }

    /**
     * Search attach for your ID
     * @param id the attach ID
     * @return the attach that check these ID
     */
    public Adjunto buscarAdjuntoId(long id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME_ADJUNTO+" WHERE id="+id+"",null);
        cursor.moveToFirst();
        return new Adjunto(cursor);
    }

    /**
     * Insert attachment in database
     * @param path a path of attachment
     * @param id_devolucion id of parent devolution
     * @return
     */
    public Adjunto insertarAdjunto(String path, long id_devolucion){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("path",path);
        cv.put("id_devolucion",id_devolucion);
        long id=db.insert(TABLE_NAME_ADJUNTO,null,cv);
        Adjunto adj=new Adjunto(path);
        adj.setId(id);
        return adj;
    }

    /**
     * Delete attachment from database
     * @param id
     */
    public void eliminarAdjunto(long id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME_ADJUNTO,"id="+id,null);
    }

    /**
     * prepare a list of attachments from a database cursor
     * @param cursor
     * @return
     */
    private List<Adjunto> prepararListadoAdjuntos(Cursor cursor){
        List<Adjunto> list=new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            list.add(new Adjunto(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /**********************************************************************************************/




}
