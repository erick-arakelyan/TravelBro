package com.erikarakelyan.travelbro;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.fullLogoImg);

        // Сердцебиение (пульсация)
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.9f, 1.05f, 0.95f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.9f, 1.05f, 0.95f);

        ObjectAnimator pulse = ObjectAnimator.ofPropertyValuesHolder(logo, scaleX, scaleY);
        pulse.setDuration(800);
        pulse.setInterpolator(new AccelerateDecelerateInterpolator());
        pulse.setRepeatCount(ObjectAnimator.INFINITE);
        pulse.setRepeatMode(ObjectAnimator.RESTART);
        pulse.start();

        // Через 3 секунды переход на Login
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 3000);
    }
}