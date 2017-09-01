package br.edu.ifspsaocarlos.agenda.view;

/**
 * Created by Note on 03/07/2017.
 */


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class NovoContatoActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mensagemDaNotificacao = getIntent().getStringExtra("mensagem_da_notificacao");
        if (mensagemDaNotificacao != null) {
            Toast.makeText(this, mensagemDaNotificacao, Toast.LENGTH_SHORT).show();
        }
        TextView tvNovoContato = new TextView(this);
        tvNovoContato.setText("Informações do novo contato");
        setContentView(tvNovoContato);
    }
}
