package com.example.travelbro;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DestinationDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_detail);

        String city = getIntent().getStringExtra("city");
        String description = getIntent().getStringExtra("description");

        TextView tvTitle = findViewById(R.id.tv_detail_title);
        TextView tvDesc = findViewById(R.id.tv_detail_description);

        if (tvTitle != null) tvTitle.setText(city);
        if (tvDesc != null) tvDesc.setText(description);

        // Кнопка назад
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
}