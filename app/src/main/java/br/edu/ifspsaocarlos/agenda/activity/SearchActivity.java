package br.edu.ifspsaocarlos.agenda.activity;


import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;


public class SearchActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        handleIntent(intent);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            setupRecylerView(query, Boolean.TRUE);
        }
    }

}
