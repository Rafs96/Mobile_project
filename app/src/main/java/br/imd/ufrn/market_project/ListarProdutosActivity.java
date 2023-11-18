// ListarProdutosActivity.java

package br.imd.ufrn.market_project;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ListarProdutosActivity extends AppCompatActivity {

    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_produtos);

        dbHelper = new SQLiteHelper(this);

        ListView listViewProdutos = findViewById(R.id.listViewProdutos);
        //Botão de voltar e função para voltar para a pagina anterior
        Button btnVoltar = findViewById(R.id.btnVoltar4);

        btnVoltar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Ler os dados dos produtos do SQLite
        ArrayList<String> nomesProdutos = lerNomesProdutosDoSQLite();

        // Configurar o adapter para exibir os nomes dos produtos no ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nomesProdutos);
        listViewProdutos.setAdapter(adapter);

        // Configurar o listener de clique nos itens da lista
        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Ao clicar em um item, obter os detalhes do produto e exibir um AlertDialog
                exibirDetalhesProduto(nomesProdutos.get(position));
            }
        });
    }

    private ArrayList<String> lerNomesProdutosDoSQLite() {
        ArrayList<String> nomesProdutos = new ArrayList<>();

        // Abrir o banco de dados para leitura
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta para obter todos os nomes dos produtos
        String[] projection = {"nome"};
        Cursor cursor = db.query("produtos", projection, null, null, null, null, null);

        // Iterar sobre o cursor e adicionar os nomes à lista
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                nomesProdutos.add(nome);
            } while (cursor.moveToNext());

            // Fechar o cursor
            cursor.close();
        }

        // Fechar o banco de dados
        db.close();

        return nomesProdutos;
    }

    private void exibirDetalhesProduto(String nomeProduto) {
        // Abrir o banco de dados para leitura
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta para obter todos os detalhes do produto com base no nome
        String[] projection = {"codigo", "descricao", "estoque"};
        String selection = "nome=?";
        String[] selectionArgs = {nomeProduto};
        Cursor cursor = db.query("produtos", projection, selection, selectionArgs, null, null, null);

        // Verificar se há resultados
        if (cursor != null && cursor.moveToFirst()) {
            // Obter os detalhes do produto
            String codigo = cursor.getString(cursor.getColumnIndexOrThrow("codigo"));
            String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
            int estoque = cursor.getInt(cursor.getColumnIndexOrThrow("estoque"));

            // Fechar o cursor
            cursor.close();

            // Fechar o banco de dados
            db.close();

            // Construir a mensagem com detalhes do produto
            String mensagemDetalhes = "Nome: " + nomeProduto +
                    "\nCódigo: " + codigo +
                    "\nDescrição: " + descricao +
                    "\nEstoque: " + estoque;

            // Criar e exibir um AlertDialog com os detalhes do produto
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Detalhes do Produto");
            builder.setMessage(mensagemDetalhes);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Ação ao clicar no botão OK
                }
            });
            builder.show();
        }
    }
}
