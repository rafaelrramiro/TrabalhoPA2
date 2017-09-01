package br.edu.ifspsaocarlos.agenda.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.agenda.MyApplication;
import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.Contato;

/**
 * Created by Note on 04/07/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Contato c;
    protected ContatoDAO cDAO;
    private Button btLogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btLogar = (Button) findViewById(R.id.bt_enviar);
        btLogar.setOnClickListener(this);

        cDAO = new ContatoDAO(this);

    }

    public void onClick(View v) {

        if (v == btLogar) {

            final EditText ednome = (EditText) findViewById(R.id.ed_nome);
            final EditText edapelido = (EditText) findViewById(R.id.ed_apelido);

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = getString(R.string.url_base) + "/contato/" + String.valueOf(c.getId());
            String string = "{\"nome_completo\":\"" + ednome.getText().toString() + "\"," +
                    "\"apelido\":\"" + edapelido.getText().toString() + "\"}";

            // {"id":"6","nome_completo":"Sergio L. D. Júnior","apelido":"sergio"}

            try {
                final JSONObject jsonBody = new JSONObject(string);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonBody,
                        new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject s) {

                                List<Contato> contatos = new ArrayList<>();

                                JSONArray jsonArray;
                                try {
                                    jsonArray = s.getJSONArray("contatos");
                                    for (int indice = 0; indice < jsonArray.length(); indice++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(indice);

                                        c = new Contato();
                                        c.setId(jsonObject.getInt("id"));
                                        c.setNome(jsonObject.getString("nome_completo"));
                                        c.setApelido(jsonObject.getString("apelido"));

                                        contatos = cDAO.buscaContato(Long.toString(c.getId()), Boolean.FALSE);

                                        if (contatos != null && !contatos.isEmpty()) {

                                            ((MyApplication) LoginActivity.this.getApplication()).setIdOrigem(jsonObject.getString("id"));

                                        } else {

                                            cDAO.insereContato(c);
                                            ((MyApplication) LoginActivity.this.getApplication()).setIdOrigem(jsonObject.getString("id"));

                                        }

                                    }
                                } catch (JSONException je) {
                                    Log.e("SDM", "Erro na conversão de objeto JSON!");
                                    //Toast.makeText(DetalheActivity.this, "Erro na conversão de objeto JSON!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(LoginActivity.this, "Erro na atualização do Contato!", Toast.LENGTH_SHORT).show();
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

