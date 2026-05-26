package com.erikarakelyan.travelbro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class TravelResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_result);

        String country = getIntent().getStringExtra("country");
        String city = getIntent().getStringExtra("city");
        String emoji = getIntent().getStringExtra("emoji");
        String description = getIntent().getStringExtra("description");
        String highlights = getIntent().getStringExtra("highlights");
        String bestTime = getIntent().getStringExtra("bestTime");
        int matchScore = getIntent().getIntExtra("matchScore", 85);

        // Bind views
        TextView tvEmoji = findViewById(R.id.tv_result_emoji);
        TextView tvCountry = findViewById(R.id.tv_result_country);
        TextView tvCity = findViewById(R.id.tv_result_city);
        TextView tvDescription = findViewById(R.id.tv_result_description);
        TextView tvHighlights = findViewById(R.id.tv_result_highlights);
        TextView tvBestTime = findViewById(R.id.tv_result_best_time);
        TextView tvMatchScore = findViewById(R.id.tv_match_score);
        ProgressBar progressMatch = findViewById(R.id.progress_match);

        if (tvEmoji != null) tvEmoji.setText(emoji);
        if (tvCountry != null) tvCountry.setText(country);
        if (tvCity != null) tvCity.setText(city);
        if (tvDescription != null) tvDescription.setText(description);
        if (tvHighlights != null) tvHighlights.setText(highlights);
        if (tvBestTime != null) tvBestTime.setText("🗓️  " + bestTime);
        if (tvMatchScore != null) tvMatchScore.setText(matchScore + "%");

        // Animate progress bar
        if (progressMatch != null) {
            progressMatch.setMax(100);
            progressMatch.setProgress(0);
            final int targetScore = Math.min(matchScore, 99);
            new Handler().postDelayed(() -> {
                progressMatch.animate(); // fallback
                final int[] current = {0};
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (current[0] < targetScore) {
                            current[0] += 2;
                            progressMatch.setProgress(current[0]);
                            new Handler().postDelayed(this, 16);
                        }
                    }
                };
                new Handler().post(runnable);
            }, 600);
        }

        // Animate cards in with delay
        animateResultIn();

        // Back button
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Try again
        MaterialButton btnRetry = findViewById(R.id.btn_retry);
        if (btnRetry != null) {
            btnRetry.setOnClickListener(v -> {
                finish();
                startActivity(new Intent(this, CountriesActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }

        // Share button
        MaterialButton btnShare = findViewById(R.id.btn_share);
        if (btnShare != null) {
            btnShare.setOnClickListener(v -> {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Мой идеальный маршрут: " + emoji + " " + city + ", " + country +
                    "\nСовпадение: " + matchScore + "% 🎯\n#TravelBro");
                startActivity(Intent.createChooser(shareIntent, "Поделиться"));
            });
        }
    }

    private void animateResultIn() {
        int[] viewIds = {
            R.id.card_main_result,
            R.id.card_match_score,
            R.id.card_description,
            R.id.card_highlights,
            R.id.card_best_time,
            R.id.ll_buttons
        };

        for (int i = 0; i < viewIds.length; i++) {
            View v = findViewById(viewIds[i]);
            if (v != null) {
                v.setAlpha(0f);
                v.setTranslationY(50f);
                v.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(400)
                    .setStartDelay(200 + i * 120L)
                    .start();
            }
        }
    }
}
