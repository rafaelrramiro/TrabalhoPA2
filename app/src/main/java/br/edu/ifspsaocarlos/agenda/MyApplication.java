package br.edu.ifspsaocarlos.agenda;

import android.app.Application;

/**
 * Created by Note on 04/07/2017.
 */

public class MyApplication extends Application {

    static String idOrigem;

    public static String getIdOrigem() {
        return idOrigem;
    }

    public void setIdOrigem(String idOrigem) {
        this.idOrigem = idOrigem;
    }
}
