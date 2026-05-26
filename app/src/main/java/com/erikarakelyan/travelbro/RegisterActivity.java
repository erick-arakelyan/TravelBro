package com.erikarakelyan.travelbro;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private FirebaseAuth auth;

    // Регулярное выражение: Минимум 6 симв, 1 заглавная, 1 цифра, 1 спецсимвол
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.et_email_reg);
        etPassword = findViewById(R.id.et_password_reg);
        MaterialButton btnRegister = findViewById(R.id.btn_register);
        TextView tvToLogin = findViewById(R.id.tv_to_login);

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Введите Email", Toast.LENGTH_SHORT).show();
                return;
            }

            // ПРОВЕРКА ПАРОЛЯ
            if (!PASSWORD_PATTERN.matcher(password).matches()) {
                Toast.makeText(this, "Пароль слишком простой! Добавьте заглавную букву, цифру и спецсимвол.", Toast.LENGTH_LONG).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (auth.getCurrentUser() != null) {
                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(vTask -> {
                            Toast.makeText(this, "Успешно! Мы отправили письмо на почту для подтверждения.", Toast.LENGTH_LONG).show();
                            auth.signOut();
                            finish(); // Возврат на LoginActivity
                        });
                    }
                } else {
                    Toast.makeText(this, "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Возврат на логин при клике на текст
        tvToLogin.setOnClickListener(v -> finish());
    }
}