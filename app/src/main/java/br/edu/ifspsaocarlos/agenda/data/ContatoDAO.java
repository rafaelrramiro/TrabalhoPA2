package br.edu.ifspsaocarlos.agenda.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.agenda.model.Contato;


public class ContatoDAO {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public ContatoDAO(Context context) {
        this.dbHelper=new SQLiteHelper(context);
    }

    public  List<Contato> buscaContato(String id, Boolean search)
    {
        database=dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor;

        String[] cols = new String[] {SQLiteHelper.KEY_ID, SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_APELIDO,
                SQLiteHelper.KEY_MSG, SQLiteHelper.KEY_IDORIGEM};
        String where = null;
        String[] argWhere = null;

        if (id != null) {

            if (search) {
                where = SQLiteHelper.KEY_APELIDO + " like ?'%' or" + SQLiteHelper.KEY_NAME + " like ?'%'";
                argWhere = new String[]{id, id};
            } else {
                where = SQLiteHelper.KEY_ID + " = ?";
                argWhere = new String[]{id};
            }
        }

        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                null, null, SQLiteHelper.KEY_ID);

       if (cursor != null)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Contato contato = new Contato();
                contato.setId(cursor.getInt(0));
                contato.setNome(cursor.getString(1));
                contato.setApelido(cursor.getString(2));
                contato.setMsg(cursor.getInt(3));
                contato.setId_origem(cursor.getInt(4));
                contatos.add(contato);
                cursor.moveToNext();
            }
            cursor.close();
        }
        database.close();
        return contatos;
    }

    public void atualizaContato(Contato c) {
        database=dbHelper.getWritableDatabase();
        ContentValues updateValues = new ContentValues();
        updateValues.put(SQLiteHelper.KEY_NAME, c.getNome());
        updateValues.put(SQLiteHelper.KEY_APELIDO, c.getApelido());
        updateValues.put(SQLiteHelper.KEY_MSG, c.getMsg());
        updateValues.put(SQLiteHelper.KEY_IDORIGEM, c.getId_origem());
        database.update(SQLiteHelper.DATABASE_TABLE, updateValues, SQLiteHelper.KEY_ID + "="
                + c.getId(), null);
        database.close();
    }


    public void insereContato(Contato c) {
        database=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_ID, c.getId());
        values.put(SQLiteHelper.KEY_NAME, c.getNome());
        values.put(SQLiteHelper.KEY_APELIDO, c.getApelido());
        values.put(SQLiteHelper.KEY_MSG, c.getMsg());
        values.put(SQLiteHelper.KEY_IDORIGEM, c.getId_origem());
        database.insert(SQLiteHelper.DATABASE_TABLE, null, values);
        database.close();
    }

    public void apagaContato(Contato c)
    {
        database=dbHelper.getWritableDatabase();
        database.delete(SQLiteHelper.DATABASE_TABLE, SQLiteHelper.KEY_ID + "="
                + c.getId(), null);
        database.close();
    }
}
