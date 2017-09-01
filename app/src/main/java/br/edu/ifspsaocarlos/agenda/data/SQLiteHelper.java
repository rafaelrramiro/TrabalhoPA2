package br.edu.ifspsaocarlos.agenda.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "trabalho.dbo";
    public static final String DATABASE_TABLE = "contatos";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "nome";
    public static final String KEY_APELIDO = "apelido";
    public static final String KEY_MSG = "msg";
    public static final String KEY_IDORIGEM = "id_origem";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_CREATE = "CREATE TABLE "+ DATABASE_TABLE +" (" +
            KEY_ID  +  " INTEGER PRIMARY KEY, " +
            KEY_NAME + " TEXT NOT NULL, " +
            KEY_APELIDO + " TEXT, "  +
            KEY_MSG + " INTEGER, " +
            KEY_IDORIGEM + " INTEGER);";

    public static String DATABASE_UPDATE = "";

    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

            switch (oldVersion) {
                case 1: // código para atualizar da versão 1 para 2

                case 2:

                case 3: // código para atualizar da versão 3 para 4

            }

    }
}

