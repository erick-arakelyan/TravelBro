package com.example.travelbro;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OfflineMapActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
        android.widget.Button btnNotify = findViewById(R.id.btn_notify);
        if (btnNotify != null) btnNotify.setOnClickListener(v ->
            Toast.makeText(this, "Мы уведомим вас!", Toast.LENGTH_SHORT).show());
    }
}
