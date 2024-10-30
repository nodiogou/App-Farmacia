package com.labschool.estoquefarma;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ProdutosActivity extends AppCompatActivity {

    private EditText editTextNome, editTextValor, editTextQuantidade, editTextCategoriaId, editTextUsuarioId;
    private Button buttonAdicionar, buttonListar;
    private ListView listViewProdutos;
    private FarmaciaDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);

        editTextNome = findViewById(R.id.editTextNome);
        editTextValor = findViewById(R.id.editTextValor);
        editTextQuantidade = findViewById(R.id.editTextQuantidade);
        editTextCategoriaId = findViewById(R.id.editTextCategoriaId);
        editTextUsuarioId = findViewById(R.id.editTextUsuarioId); // Novo campo
        buttonAdicionar = findViewById(R.id.buttonAdicionar);
        buttonListar = findViewById(R.id.buttonListar);
        listViewProdutos = findViewById(R.id.listViewProdutos);

        dbHelper = new FarmaciaDatabaseHelper(this);

        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editTextNome.getText().toString();
                double valorUnitario = Double.parseDouble(editTextValor.getText().toString());
                int quantidade = Integer.parseInt(editTextQuantidade.getText().toString());
                int categoriaId = Integer.parseInt(editTextCategoriaId.getText().toString());
                int usuarioId = Integer.parseInt(editTextUsuarioId.getText().toString()); // Obter ID do usuário

                if (dbHelper.inserirProduto(nome, valorUnitario, quantidade, categoriaId, usuarioId)) {
                    Toast.makeText(ProdutosActivity.this, "Produto adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProdutosActivity.this, "Erro ao adicionar produto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int categoriaId = Integer.parseInt(editTextCategoriaId.getText().toString());
                int usuarioId = Integer.parseInt(editTextUsuarioId.getText().toString()); // Obter ID do usuário

                Cursor cursor = dbHelper.listarProdutosPorCategoriaEUsuario(categoriaId, usuarioId);
                ArrayList<String> produtosList = new ArrayList<>();

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        String nome = cursor.getString(cursor.getColumnIndex("nome"));
                        double valorUnitario = cursor.getDouble(cursor.getColumnIndex("valorUnitario"));
                        int quantidade = cursor.getInt(cursor.getColumnIndex("quantidade"));
                        produtosList.add(nome + " - R$ " + valorUnitario + " - Quantidade: " + quantidade);
                    } while (cursor.moveToNext());
                    cursor.close();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProdutosActivity.this, android.R.layout.simple_list_item_1, produtosList);
                listViewProdutos.setAdapter(adapter);
                listViewProdutos.setVisibility(View.VISIBLE);
            }
        });
    }
}
