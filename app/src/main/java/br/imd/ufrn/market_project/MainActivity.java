package br.imd.ufrn.market_project;



import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verificar se o usuário já fez login
        if (!userIsLoggedIn()) {
            // Se o usuário não fez login, iniciar LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finalizar a MainActivity para evitar que o usuário volte para esta tela
        } else {
            // Se o usuário já fez login, iniciar MenuActivity
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            finish(); // Finalizar a MainActivity para evitar que o usuário volte para esta tela
        }
    }

    // Método fictício para verificar se o usuário já fez login
    private boolean userIsLoggedIn() {
        // Substitua isso com sua lógica de verificação de login real
        // Por exemplo, você pode verificar se há um token de autenticação válido, etc.
        return false;
    }
}
