package br.imd.ufrn.market_project;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class CadastrarProdutoActivity extends AppCompatActivity {

    private SQLiteHelper dbHelper;
    private EditText editTextCodigo, editTextNome, editTextDescricao, editTextEstoque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_produto);

        dbHelper = new SQLiteHelper(this);

        editTextCodigo = findViewById(R.id.editTextCodigo);
        editTextNome = findViewById(R.id.editTextNome);
        editTextDescricao = findViewById(R.id.editTextDescricao);
        editTextEstoque = findViewById(R.id.editTextEstoque);

        Button btnCadastrar = findViewById(R.id.btnCadastrar);

        // Substituir o OnClickListener por uma expressão lambda
        btnCadastrar.setOnClickListener(view -> cadastrarProduto(
                editTextCodigo.getText().toString().trim(),
                editTextNome.getText().toString().trim(),
                editTextDescricao.getText().toString().trim(),
                editTextEstoque.getText().toString().trim()
        ));

        Button btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void cadastrarProduto(String codigo, String nome, String descricao, String estoque) {
        if (codigo.isEmpty() || nome.isEmpty() || descricao.isEmpty() || estoque.isEmpty()) {
            Toast.makeText(this, R.string.campos_obrigatorios, Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.verificarCodigoExistente(codigo)) {
            Toast.makeText(this, R.string.codigo_ja_cadastrado, Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper.inserirProduto(codigo, nome, descricao, estoque);

        Toast.makeText(this, R.string.cadastro_sucesso, Toast.LENGTH_SHORT).show();
        finish();
    }
}
