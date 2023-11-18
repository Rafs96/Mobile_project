package br.imd.ufrn.market_project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AlterarProdutoActivity extends AppCompatActivity {

    private SQLiteHelper dbHelper;
    private ArrayList<String> codigosENomesProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_produto);

        dbHelper = new SQLiteHelper(this);

        ListView listViewProdutos = findViewById(R.id.listViewProdutosAlterar);

        Button btnVoltar = findViewById(R.id.btnVoltar1);

        btnVoltar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Ler os dados dos produtos do SQLite (Código e Nome)
        codigosENomesProdutos = lerCodigosENomesProdutosDoSQLite();

        // Configurar o adapter para exibir os Códigos e Nomes dos produtos no ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, codigosENomesProdutos);
        listViewProdutos.setAdapter(adapter);

        // Configurar o listener de clique nos itens da lista
        listViewProdutos.setOnItemClickListener((adapterView, view, position, id) -> {
            // Ao clicar em um item, abrir a tela de edição do produto
            abrirTelaEdicao(codigosENomesProdutos.get(position));
        });
    }

    private ArrayList<String> lerCodigosENomesProdutosDoSQLite() {
        ArrayList<String> codigosENomesProdutos = new ArrayList<>();

        // Abrir o banco de dados para leitura
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta para obter Códigos e Nomes dos produtos
        String[] projection = {"codigo", "nome"};
        Cursor cursor = db.query("produtos", projection, null, null, null, null, null);

        // Iterar sobre o cursor e adicionar os Códigos e Nomes à lista
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int codigo = cursor.getInt(cursor.getColumnIndexOrThrow("codigo"));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                String codigoENome = "Código: " + codigo + " - Nome: " + nome;
                codigosENomesProdutos.add(codigoENome);
            } while (cursor.moveToNext());

            // Fechar o cursor
            cursor.close();
        }

        // Fechar o banco de dados
        db.close();

        return codigosENomesProdutos;
    }

    private void abrirTelaEdicao(String codigoENomeProduto) {
        // Extrair o Código do produto da String no formato "Código: X - Nome: Y"
        int codigo = extrairCodigoProduto(codigoENomeProduto);

        // Iniciar a nova atividade (EdicaoProdutoActivity) passando o código do produto
        Intent intent = new Intent(AlterarProdutoActivity.this, EdicaoProdutoActivity.class);
        intent.putExtra("codigo_produto", codigo);
        startActivity(intent);
    }

    private int extrairCodigoProduto(String codigoENomeProduto) {
        // Extrair o Código do produto da String no formato "Código: X - Nome: Y"
        String[] parts = codigoENomeProduto.split(" ");
        String codigoPart = parts[1];
        return Integer.parseInt(codigoPart);
    }
}
