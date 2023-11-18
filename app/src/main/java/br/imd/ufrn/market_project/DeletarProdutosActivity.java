package br.imd.ufrn.market_project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DeletarProdutosActivity extends AppCompatActivity {

    private SQLiteHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> codigosENomesProdutos;
    private final ArrayList<Integer> selectedCodigos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletar_produtos);

        dbHelper = new SQLiteHelper(this);

        ListView listViewProdutos = findViewById(R.id.listViewProdutosDeletar);

        Button btnVoltar = findViewById(R.id.btnVoltar2);

        btnVoltar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Ler os dados dos produtos do SQLite (Código e Nome)
        codigosENomesProdutos = lerCodigosENomesProdutosDoSQLite();

        // Configurar o adapter para exibir os Códigos e Nomes dos produtos no ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, codigosENomesProdutos);
        listViewProdutos.setAdapter(adapter);
        listViewProdutos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Configurar o listener de clique nos itens da lista
        listViewProdutos.setOnItemClickListener((adapterView, view, position, id) -> {
            // Ao clicar em um item, adicionar/remover da lista de seleção
            if (listViewProdutos.isItemChecked(position)) {
                selectedCodigos.add(position);
            } else {
                selectedCodigos.remove((Integer) position);
            }
        });

        // Configurar o botão de exclusão em massa
        Button btnExcluirEmMassa = findViewById(R.id.btnExcluirEmMassa);
        btnExcluirEmMassa.setOnClickListener(view -> excluirProdutosSelecionados());
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

    private void excluirProdutosSelecionados() {
        // Abrir o banco de dados para escrita
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (int position : selectedCodigos) {
            // Extrair o Código do produto
            int codigo = extrairCodigoProduto(codigosENomesProdutos.get(position));

            // Condição para a cláusula WHERE
            String selection = "codigo=?";
            String[] selectionArgs = {String.valueOf(codigo)};

            // Excluir o produto
            db.delete("produtos", selection, selectionArgs);
        }

        // Fechar o banco de dados
        db.close();

        // Limpar a seleção
        selectedCodigos.clear();

        // Atualizar a lista de produtos após a exclusão
        codigosENomesProdutos = lerCodigosENomesProdutosDoSQLite();
        adapter.clear();
        adapter.addAll(codigosENomesProdutos);
        adapter.notifyDataSetChanged();

        // Exibir mensagem de sucesso
        Toast.makeText(this, "Produtos excluídos com sucesso!", Toast.LENGTH_SHORT).show();
    }

    private int extrairCodigoProduto(String codigoENomeProduto) {
        // Extrair o Código do produto da String no formato "Código: X - Nome: Y"
        String[] parts = codigoENomeProduto.split(" ");
        String codigoPart = parts[1];
        return Integer.parseInt(codigoPart);
    }
}
