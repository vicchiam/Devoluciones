package com.pcs.vicchiam.devoluciones.utilidades;

/**
 * Created by victor on 09/06/2016.
 */
public class Semaforo {

    private int a;

    public Semaforo(){
        a=0;
    }

    public synchronized void lock(){
        a=1;
    }

    public synchronized void unlock(){
        a=0;
    }

    public boolean isLock(){
        return a==1;
    }

}
