package br.edu.ifspsaocarlos.agenda.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.agenda.MyApplication;
import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.adapter.ContatoAdapter;
import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.Contato;

public class BaseActivity extends AppCompatActivity {

    protected ContatoDAO cDAO;
    private RecyclerView recyclerView;

    protected List<Contato> contatos = new ArrayList<Contato>();
    private SearchView searchView;

    private ContatoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cDAO = new ContatoDAO(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerView.addItemDecoration(new DividerItemDecoration(BaseActivity.this, DividerItemDecoration.VERTICAL));

        RequestQueue queue = Volley.newRequestQueue(BaseActivity.this);
        String url = getString(R.string.url_base) + "/contato";
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject s) {

                            List<Contato> contatos = new ArrayList<>();

                            JSONArray jsonArray;
                            try {
                                jsonArray = s.getJSONArray("contatos");
                                for (int indice = 0; indice < jsonArray.length(); indice++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(indice);

                                    contatos = cDAO.buscaContato(jsonObject.getString("id"), Boolean.FALSE);

                                    Contato contato = new Contato();
                                    contato.setId(jsonObject.getInt("id"));
                                    contato.setNome(jsonObject.getString("nome_completo"));
                                    contato.setApelido(jsonObject.getString("apelido"));

                                    if (contatos != null && !contatos.isEmpty()) {

                                        cDAO.atualizaContato(contato);

                                    } else {

                                        cDAO.insereContato(contato);

                                    }

                                    contatos.add(contato);

                                }
                            } catch (JSONException je) {
                                Toast.makeText(BaseActivity.this, "Erro na conversão de objeto JSON!",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(BaseActivity.this, "Erro na recuperação das mensagens!",
                            Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.e("SDM", "Erro na leitura de mensagens");
        }

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Intent i = new Intent(getApplicationContext(), DetalheActivity.class);
        //        startActivityForResult(i, 1);
        //    }
        //});

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.pesqContato).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(true);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1)
            if (resultCode == RESULT_OK)
               showSnackBar("Contato inserido");

        if (requestCode == 2) {
                if (resultCode == RESULT_OK)
                   showSnackBar("Informações do contato alteradas");
                if (resultCode == 3)
                   showSnackBar("Contato removido");

            }

        setupRecylerView(null, Boolean.FALSE);
    }

    public void showSnackBar(String msg) {
        CoordinatorLayout coordinatorlayout=(CoordinatorLayout) findViewById(R.id.coordlayout);
        Snackbar.make(coordinatorlayout, msg,
                Snackbar.LENGTH_LONG)
                .show();
    }


    protected void setupRecylerView(String idContato, Boolean search) {

        contatos = cDAO.buscaContato(idContato, search);

        adapter = new ContatoAdapter(contatos, this);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new ContatoAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Contato contato = contatos.get(position);
                Intent i = new Intent(getApplicationContext(), DetalheActivity.class);
                i.putExtra("contato", contato);
                startActivityForResult(i, 2);
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == ItemTouchHelper.RIGHT) {
                    Contato contato = contatos.get(viewHolder.getAdapterPosition());
                    cDAO.apagaContato(contato);
                    contatos.remove(viewHolder.getAdapterPosition());
                    recyclerView.getAdapter().notifyDataSetChanged();

                    showSnackBar("Contato removido");
                }
            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
