package com.pcs.vicchiam.devoluciones.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicchiam on 16/05/2016.
 * Class that make all operations of Devolution, Line, and attachment to database
 */
public class DevolucionDB extends SQLiteOpenHelper{

    public static final String DATABASE_NAME="Devoluciones.db";

    public static final String PRAGMA_SQL="PRAGMA foreign_keys = ON;";

    public static final String TABLE_NAME_DEVOLUCION="devolucion";
    public static final String TABLE_NAME_LINEA="linea";
    public static final String TABLE_NAME_ADJUNTO="adjunto";

    public static final String[] COLS_DEVOLUCION={"id","codigo","cliente","razon","accion","motivo"};
    public static final String[] COLS_TYPE_DEVOLUCION={"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","TEXT","TEXT","TEXT","TEXT"};

    public static final String[] COLS_LINEA={"id","codigo","descripcion","cantidad","umv","lote","caducidad","accion","motivo","id_devolucion"};
    public static final String[] COLS_TYPE_LINEA={"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","TEXT","NUMBER","TEXT","TEXT","TEXT","INTEGER"};

    public static final String[] COLS_ADJUNTO={"id","path","id_devolucion"};
    public static final String[] COLS_TYPE_ADJUNTO={"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","INTEGER"};

    public static final String FOREIGN_KEY=" FOREIGN KEY(id_devolucion) REFERENCES devolucion(id) ";

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
        SQL+=COLS_LINEA[COLS_LINEA.length-1]+" "+COLS_TYPE_LINEA[COLS_LINEA.length-1]+")";
        SQL+=FOREIGN_KEY;
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
        SQL+=COLS_ADJUNTO[COLS_ADJUNTO.length-1]+" "+COLS_TYPE_ADJUNTO[COLS_ADJUNTO.length-1]+")";
        SQL+=FOREIGN_KEY;
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
    public List<Devolucion> obtenerTodosDevoluciones(){
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
     * @param accion
     * @param motivo
     * @return
     */
    public Devolucion insertarDevolucion(String codigo, String razon, String accion, String motivo){
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

    /**
     * Insert or update a devolution in database
     * @param codigo
     * @param razon
     * @param accion
     * @param motivo
     * @return
     */
    public Devolucion remplazarDevolucion(String codigo, String razon, String accion, String motivo){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("razon",razon);
        cv.put("accion",accion);
        cv.put("motivo",motivo);
        db.replace(TABLE_NAME_DEVOLUCION,null,cv);
        return new Devolucion(codigo, razon, accion, motivo);
    }

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
    public Linea insertarLinea(String codigo, String descripcion, double cantidad, String umv, String lote, String caducidad, String accion, String motivo, long id_devolucion){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("razon",descripcion);
        cv.put("cantidad",cantidad);
        cv.put("umv",umv);
        cv.put("lote",lote);
        cv.put("caducidad",caducidad);
        cv.put("accion",accion);
        cv.put("motivo",motivo);
        cv.put("id_devolucion",id_devolucion);
        long id=db.insert(TABLE_NAME_LINEA,null,cv);
        Linea linea=new Linea(codigo,descripcion,cantidad,umv,lote,caducidad,accion,motivo);
        linea.setId(id);
        return linea;
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
     * @param id_devolucion
     * @return
     */
    public Linea remplazarLinea(String codigo, String descripcion, double cantidad, String umv, String lote, String caducidad, String accion, String motivo, long id_devolucion){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("codigo",codigo);
        cv.put("razon",descripcion);
        cv.put("cantidad",cantidad);
        cv.put("umv",umv);
        cv.put("lote",lote);
        cv.put("caducidad",caducidad);
        cv.put("accion",accion);
        cv.put("motivo",motivo);
        cv.put("id_devolucion",id_devolucion);
        db.replace(TABLE_NAME_LINEA,null,cv);
        return new Linea(codigo,descripcion,cantidad,umv,lote,caducidad,accion,motivo);
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
