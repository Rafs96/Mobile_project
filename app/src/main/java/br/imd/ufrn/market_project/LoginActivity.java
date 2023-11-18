package br.imd.ufrn.market_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Verificar se o login e senha estão corretos
        if (isValidLogin(username, password)) {
            // Login bem-sucedido, você pode redirecionar para a próxima atividade aqui
            Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();

            // Iniciar a nova atividade (MenuActivity neste caso)
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(intent);

            // Finalizar a LoginActivity para evitar que o usuário volte para esta tela
            finish();
        } else {
            // Exibir mensagem de erro
            Toast.makeText(this, "Login ou senha incorretos", Toast.LENGTH_SHORT).show();
        }
    }

    // Método fictício para validar o login
    private boolean isValidLogin(String username, String password) {
        // Aqui você deve implementar a lógica real de verificação do login
        // Neste exemplo, usaremos um nome de usuário e senha fixos
        return username.equals("admin") && password.equals("admin");
    }
}
