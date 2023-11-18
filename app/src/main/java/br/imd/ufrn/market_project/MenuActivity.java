package br.imd.ufrn.market_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnCadastrarProduto = findViewById(R.id.btnCadastrarProduto);
        Button btnListarProdutos = findViewById(R.id.btnListarProdutos);
        Button btnAlterarProduto = findViewById(R.id.btnAlterarProduto);
        Button btnDeletarProdutos = findViewById(R.id.btnDeletarProdutos);

        btnCadastrarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, CadastrarProdutoActivity.class));
            }
        });

        btnListarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, ListarProdutosActivity.class));
            }
        });

        btnAlterarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, AlterarProdutoActivity.class));
            }
        });

        btnDeletarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, DeletarProdutosActivity.class));
            }
        });
    }
}
