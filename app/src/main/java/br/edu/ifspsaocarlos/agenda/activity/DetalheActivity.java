package br.edu.ifspsaocarlos.agenda.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.Contato;
import br.edu.ifspsaocarlos.agenda.view.EnviarActivity;
import br.edu.ifspsaocarlos.agenda.view.LerActivity;


public class DetalheActivity extends AppCompatActivity implements View.OnClickListener{

    private Contato c;
    private ContatoDAO cDAO;
    private Button btEnviar;
    private Button btVisualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        btEnviar = (Button) findViewById(R.id.bt_enviar);
        btEnviar.setOnClickListener(this);

        btVisualizar = (Button) findViewById(R.id.bt_visualizar);
        btVisualizar.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra("contato"))
        {
            this.c = (Contato) getIntent().getSerializableExtra("contato");
            EditText nameText = (EditText)findViewById(R.id.ed_nome);
            nameText.setText(c.getNome());
            EditText apelidoText = (EditText)findViewById(R.id.ed_apelido);
            apelidoText.setText(c.getApelido());

            int pos = c.getNome().indexOf(" ");
            if (pos == -1)
                pos = c.getNome().length();
            setTitle(c.getNome().substring(0, pos));
        }
        cDAO = new ContatoDAO(this);
    }

    public void onClick(View v) {

        //final EditText etOrigem = (EditText) findViewById(R.id.et_origem_ler);
        //final EditText etDestino = (EditText) findViewById(R.id.et_destino_ler);
        //final EditText etUltimaMensagem = (EditText) findViewById(R.id.et_ultima_mensagem_ler);

        if (v == btVisualizar) {

            Intent i = new Intent(getApplicationContext(), EnviarActivity.class);
            i.putExtra("contatoEnviar", c);
            startActivityForResult(i, 1);

        }

        if (v == btEnviar) {

            Intent i = new Intent(getApplicationContext(), LerActivity.class);
            i.putExtra("contatoLer", c);
            startActivityForResult(i, 2);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalhe, menu);
        if (!getIntent().hasExtra("contato"))
        {
            MenuItem item = menu.findItem(R.id.delContato);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salvarContato:
                salvar();
                return true;
            case R.id.delContato:
                apagar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void apagar()
    {
        cDAO.apagaContato(c);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.url_base) + "/contato/" + String.valueOf(c.getId());
        //String string = "{\"id\":\"" + String.valueOf(c.getId()) + "\"," +
        //        "\"nome_completo\":\"" + c.getNome().toString() + "\"," +
        //        "\"apelido\":\"" + c.getApelido().toString() + "\"}";

        try {
            //final JSONObject jsonBody = new JSONObject(string);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.DELETE,
                    url,
                    null, //jsonBody,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject s) {

                            JSONArray jsonArray;
                            try {
                                jsonArray = s.getJSONArray("contatos");
                                for (int indice = 0; indice < jsonArray.length(); indice++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(indice);

                                    c.setId(jsonObject.getInt("id"));

                                }
                            } catch (JSONException je) {
                                Toast.makeText(DetalheActivity.this, "Erro na conversão de objeto JSON!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(DetalheActivity.this, "Erro ao excluir o Contato!", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.e("SDM", "Erro na leitura do Contato");
        }

        Intent resultIntent = new Intent();
        setResult(3, resultIntent);
        finish();
    }

    public void salvar()
    {
        String name = ((EditText) findViewById(R.id.ed_nome)).getText().toString();
        String apelido = ((EditText) findViewById(R.id.ed_apelido)).getText().toString();

        if (c == null)
        {
            c = new Contato();
            c.setNome(name);
            c.setApelido(apelido);

            cDAO.insereContato(c);

        }
        else
        {
            c.setNome(name);
            c.setApelido(apelido);
            cDAO.atualizaContato(c);

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = getString(R.string.url_base) + "/contato/" + String.valueOf(c.getId());
            String string = "{\"id\":\"" + String.valueOf(c.getId()) + "\"," +
                    "\"nome_completo\":\"" + c.getNome().toString() + "\"," +
                    "\"apelido\":\"" + c.getApelido().toString() + "\"}";

            // {"id":"6","nome_completo":"Sergio L. D. Júnior","apelido":"sergio"}

            try {
                final JSONObject jsonBody = new JSONObject(string);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonBody,
                        new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject s) {

                                JSONArray jsonArray;
                                try {
                                    jsonArray = s.getJSONArray("contatos");
                                    for (int indice = 0; indice < jsonArray.length(); indice++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(indice);

                                        c.setId(jsonObject.getInt("id"));
                                        c.setNome(jsonObject.getString("nome_completo"));
                                        c.setApelido(jsonObject.getString("apelido"));

                                    }
                                } catch (JSONException je) {
                                    Log.e("SDM", "Erro na conversão de objeto JSON!");
                                    //Toast.makeText(DetalheActivity.this, "Erro na conversão de objeto JSON!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(DetalheActivity.this, "Erro na atualização do Contato!", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(jsonObjectRequest);
            } catch (Exception e) {
                Log.e("SDM", "Erro na leitura do Contato");
            }

        }
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

