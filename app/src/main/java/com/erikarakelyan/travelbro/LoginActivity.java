package com.erikarakelyan.travelbro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEdit, passwordEdit;
    private MaterialButton loginBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ссылка на элементы дизайна
        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        loginBtn = findViewById(R.id.loginBtn);

        // Инициализация Firebase
        mAuth = FirebaseAuth.getInstance();

        // LOGIN BUTTON
        loginBtn.setOnClickListener(v -> {
            String email = emailEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(LoginActivity.this, "Введите email и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            // Вход через Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Вход успешен!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Ошибка входа: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // REGISTER BUTTON
        findViewById(R.id.registerBtn).setOnClickListener(v -> {
            String email = emailEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(LoginActivity.this, "Введите email и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            // Создание аккаунта через Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Аккаунт создан!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Ошибка регистрации: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // GUEST BUTTON
        findViewById(R.id.guestBtn).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });
    }
}