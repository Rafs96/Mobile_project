package br.imd.ufrn.market_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "market_database";
    private static final int DATABASE_VERSION = 1;

    // Tabela de produtos
    private static final String CREATE_PRODUTOS_TABLE =
            "CREATE TABLE produtos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "codigo TEXT UNIQUE," +
                    "nome TEXT," +
                    "descricao TEXT," +
                    "estoque TEXT)";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação da tabela de produtos
        db.execSQL(CREATE_PRODUTOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualizações futuras do banco de dados
        // Não estamos tratando atualizações neste exemplo
    }

    public void inserirProduto(String codigo, String nome, String descricao, String estoque) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("codigo", codigo);
        values.put("nome", nome);
        values.put("descricao", descricao);
        values.put("estoque", estoque);

        db.insert("produtos", null, values);
        db.close();
    }

    public boolean verificarCodigoExistente(String codigo) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"id"};
        String selection = "codigo=?";
        String[] selectionArgs = {codigo};

        Cursor cursor = db.query("produtos", projection, selection, selectionArgs, null, null, null);

        boolean codigoExistente = cursor.moveToFirst();
        cursor.close();
        db.close();

        return codigoExistente;
    }

    public int obterUltimoCodigoInserido() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT MAX(codigo) FROM produtos";
        Cursor cursor = db.rawQuery(sql, null);

        int ultimoCodigo = 0;
        if (cursor.moveToFirst()) {
            ultimoCodigo = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return ultimoCodigo;
    }
}
