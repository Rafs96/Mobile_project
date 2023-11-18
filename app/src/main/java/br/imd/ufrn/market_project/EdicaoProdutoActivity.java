package br.imd.ufrn.market_project;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EdicaoProdutoActivity extends AppCompatActivity {

    private SQLiteHelper dbHelper;
    private int codigoProduto;

    private EditText editTextNome;
    private EditText editTextDescricao;
    private EditText editTextEstoque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicao_produto);

        dbHelper = new SQLiteHelper(this);

        // Obter o código do produto passado pela Intent
        codigoProduto = getIntent().getIntExtra("codigo_produto", -1);

        editTextNome = findViewById(R.id.editTextNomeEdicao);
        editTextDescricao = findViewById(R.id.editTextDescricaoEdicao);
        editTextEstoque = findViewById(R.id.editTextEstoqueEdicao);

        Button btnSalvarEdicao = findViewById(R.id.btnSalvarEdicao);
        btnSalvarEdicao.setOnClickListener(view -> salvarEdicaoProduto(
                editTextNome.getText().toString().trim(),
                editTextDescricao.getText().toString().trim(),
                editTextEstoque.getText().toString().trim()
        ));

        Button btnVoltar = findViewById(R.id.btnVoltar3);

        btnVoltar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        carregarDadosProduto();
    }

    private void carregarDadosProduto() {
        // Abrir o banco de dados para leitura
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta para obter os dados do produto pelo Código
        String[] projection = {"nome", "descricao", "estoque"};
        String selection = "codigo=?";
        String[] selectionArgs = {String.valueOf(codigoProduto)};
        Cursor cursor = db.query("produtos", projection, selection, selectionArgs, null, null, null);

        // Preencher os campos com os dados do produto
        if (cursor != null && cursor.moveToFirst()) {
            String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
            String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
            int estoque = cursor.getInt(cursor.getColumnIndexOrThrow("estoque"));

            editTextNome.setText(nome);
            editTextDescricao.setText(descricao);
            editTextEstoque.setText(String.valueOf(estoque));

            // Fechar o cursor
            cursor.close();
        }

        // Fechar o banco de dados
        db.close();
    }

    private void salvarEdicaoProduto(String nome, String descricao, String estoque) {
        // Verificar se todos os campos estão preenchidos
        if (nome.isEmpty() || descricao.isEmpty() || estoque.isEmpty()) {
            // Exibir TOAST de aviso
            Toast.makeText(this, R.string.campos_obrigatorios, Toast.LENGTH_SHORT).show();
            return;
        }

        // Abrir o banco de dados para escrita
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Atualizar os dados do produto
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("descricao", descricao);
        values.put("estoque", Integer.parseInt(estoque));

        // Condição para a cláusula WHERE
        String whereClause = "codigo=?";
        String[] whereArgs = {String.valueOf(codigoProduto)};

        // Atualizar o produto
        int result = db.update("produtos", values, whereClause, whereArgs);

        // Fechar o banco de dados
        db.close();

        // Exibir mensagem de sucesso ou falha
        if (result > 0) {
            Toast.makeText(this, "Produto atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Falha ao atualizar o produto.", Toast.LENGTH_SHORT).show();
        }

        // Fechar esta atividade
        finish();
    }
}
