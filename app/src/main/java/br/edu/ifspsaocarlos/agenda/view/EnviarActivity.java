package br.edu.ifspsaocarlos.agenda.view;

/**
 * Created by Note on 04/07/2017.
 */

import android.app.Activity;
import android.os.Bundle;
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

import org.json.JSONObject;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.model.Contato;


public class EnviarActivity extends Activity implements View.OnClickListener {

    private Contato c;
    private Button btEnviar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);
        btEnviar = (Button) findViewById(R.id.bt_enviar);
        btEnviar.setOnClickListener(this);

        if (getIntent().hasExtra("contatoEnviar"))
        {
            this.c = (Contato) getIntent().getSerializableExtra("contatoEnviar");
            EditText etOrigem = (EditText) findViewById(R.id.et_origem_enviar);
            etOrigem.setText(String.valueOf(c.getId_origem()));
            EditText etDestino = (EditText) findViewById(R.id.et_destino_enviar);
            etDestino.setText(String.valueOf(c.getId()));

        }
    }

    public void onClick(View v) {

        //final EditText etOrigem = (EditText) findViewById(R.id.et_origem_enviar);
        //final EditText etDestino = (EditText) findViewById(R.id.et_destino_enviar);
        final EditText etAssunto = (EditText) findViewById(R.id.et_assunto_enviar);
        final EditText etCorpo = (EditText) findViewById(R.id.et_corpo_enviar);

        new Thread() {
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(EnviarActivity.this);
                String url = getString(R.string.url_base) + "/mensagem";
                String string = "{\"origem_id\":\"" + String.valueOf(c.getId_origem()) + "\"," +
                        "\"destino_id\":\"" + String.valueOf(c.getId()) + "\"," +
                        "\"assunto\":\"" + etAssunto.getText().toString() + "\"," +
                        "\"corpo\":\"" + etCorpo.getText().toString() + "\"}";
                try {
                    final JSONObject jsonBody = new JSONObject(string);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            jsonBody,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject s) {
                                    Toast.makeText(EnviarActivity.this, "Mensagem enviada!",
                                            Toast.LENGTH_SHORT).show();
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            etAssunto.setText("");
                                            etCorpo.setText("");
                                        }
                                    });
                                }
                            },
                            new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(EnviarActivity.this, "Erro no envio da mensagem!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    queue.add(jsonObjectRequest);
                } catch (Exception e) {
                    Log.e("SDM", "Erro no envio da mensagem");
                }
            }
        }.start();
    }
}

