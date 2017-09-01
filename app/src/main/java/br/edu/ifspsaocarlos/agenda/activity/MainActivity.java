package br.edu.ifspsaocarlos.agenda.activity;

import android.content.Intent;
import android.os.Bundle;

import br.edu.ifspsaocarlos.agenda.MyApplication;

public class MainActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Intent login = new Intent(this, LoginActivity.class);
        //startActivity(login);
        setupRecylerView(MyApplication.getIdOrigem(), Boolean.FALSE);

    }


    @Override
    protected void onResume()
    {
        super.onResume();
        setupRecylerView(MyApplication.getIdOrigem(), Boolean.FALSE);

    }

}
