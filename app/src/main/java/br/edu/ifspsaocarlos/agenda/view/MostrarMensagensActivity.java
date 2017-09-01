package br.edu.ifspsaocarlos.agenda.view;

/**
 * Created by Note on 04/07/2017.
 */


import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;

public class MostrarMensagensActivity extends ListActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> corpoMensagens = getIntent().getStringArrayListExtra("mensagens");
        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                corpoMensagens);
        setListAdapter(listAdapter);
    }
}