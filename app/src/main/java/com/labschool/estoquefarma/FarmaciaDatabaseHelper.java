package com.labschool.estoquefarma;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FarmaciaDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db";
    private static final int DATABASE_VERSION = 2;

    public FarmaciaDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUsuario = "CREATE TABLE usuario(id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, senha TEXT)";
        String createTableCategoria = "CREATE TABLE categoria(id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT)";
        String createTableProduto = "CREATE TABLE produto(id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, valorUnitario REAL, quantidade INTEGER, fkCategoria INTEGER, fkUsuario INTEGER, FOREIGN KEY(fkCategoria) REFERENCES categoria(id), FOREIGN KEY(fkUsuario) REFERENCES usuario(id))";

        db.execSQL(createTableUsuario);
        db.execSQL(createTableCategoria);
        db.execSQL(createTableProduto);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS categoria");
        db.execSQL("DROP TABLE IF EXISTS produto");
        onCreate(db);
    }

    // Métodos para gerenciar usuários
    public boolean inserirUsuario(String nome, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("senha", senha);

        long resultado = db.insert("usuario", null, values);
        db.close();
        return resultado != -1;
    }

    public Cursor listarUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM usuario", null);
    }

    public boolean verificarUsuario(String nome, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuario WHERE nome=? AND senha=?", new String[]{nome, senha});
        boolean usuarioValido = cursor.getCount() > 0;
        cursor.close();
        return usuarioValido;
    }

    // Métodos para gerenciar categorias
    public boolean inserirCategoria(String nome) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);

        long resultado = db.insert("categoria", null, values);
        db.close();
        return resultado != -1;
    }

    public Cursor listarCategorias() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM categoria", null);
    }

    // Métodos para gerenciar produtos
    public boolean inserirProduto(String nome, double valorUnitario, int quantidade, int fkCategoria, int fkUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("valorUnitario", valorUnitario);
        values.put("quantidade", quantidade);
        values.put("fkCategoria", fkCategoria);
        values.put("fkUsuario", fkUsuario);

        long resultado = db.insert("produto", null, values);
        db.close();
        return resultado != -1;
    }

    public Cursor listarProdutosPorCategoriaEUsuario(int fkCategoria, int fkUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM produto WHERE fkCategoria=? AND fkUsuario=?", new String[]{String.valueOf(fkCategoria), String.valueOf(fkUsuario)});
        return cursor;
    }

    public Cursor listarProdutos() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM produto", null);
        return cursor;
    }

}
