package com.pcs.vicchiam.devoluciones.bbdd;

import android.database.Cursor;

/**
 * Created by vicchiam on 17/05/2016.
 * Class that represent a attach file
 */
public class Adjunto {

    private long id;
    private String path;

    public Adjunto(String path){
        this.id=0;
        this.path=path;
    }

    /**
     * Constructor that make a Attach file object from the database cursor
     * @param cursor database cursor
     */
    public Adjunto(Cursor cursor){
        this.id=cursor.getLong(cursor.getColumnIndex(DevolucionDB.COLS_ADJUNTO[0]));
        this.path=cursor.getString(cursor.getColumnIndex(DevolucionDB.COLS_ADJUNTO[1]));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
