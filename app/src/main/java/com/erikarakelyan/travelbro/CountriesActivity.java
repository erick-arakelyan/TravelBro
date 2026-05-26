package com.erikarakelyan.travelbro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.HashMap;
import java.util.Map;

public class CountriesActivity extends AppCompatActivity {

    private int currentQuestion = 0;
    private Map<String, String> answers = new HashMap<>();

    // Questions data
    private String[] questionTitles = {
        "Какой климат вам нравится?",
        "Что вас привлекает больше всего?",
        "Какой бюджет вы планируете?",
        "Как вы предпочитаете путешествовать?",
        "Что для вас важнее всего в поездке?",
        "Какую кухню вы предпочитаете?",
        "Сколько времени займёт поездка?"
    };

    private String[] questionEmojis = {"🌤️", "🎯", "💰", "🧳", "✨", "🍽️", "📅"};

    private String[][] optionTexts = {
        {"☀️ Жаркий и солнечный", "❄️ Холодный и снежный", "🌿 Умеренный и мягкий", "🌧️ Прохладный и дождливый"},
        {"🏖️ Пляжи и море", "🏔️ Горы и природа", "🏛️ История и культура", "🎉 Ночная жизнь и тусовки"},
        {"💸 Эконом (до $50/день)", "💳 Средний ($50–150/день)", "💎 Премиум ($150–300/день)", "👑 Люкс (без ограничений)"},
        {"🎒 Самостоятельно", "👫 С парой/другом", "👨‍👩‍👧 С семьёй и детьми", "🧑‍🤝‍🧑 С группой друзей"},
        {"🌅 Отдых и расслабление", "🗺️ Приключения и активность", "📸 Фото и впечатления", "🛍️ Шопинг и гастрономия"},
        {"🍣 Азиатская", "🍕 Европейская", "🌮 Латинская", "🥙 Ближневосточная"},
        {"⚡ 3–5 дней", "📆 1–2 недели", "🗓️ 2–4 недели", "🌍 Месяц и больше"}
    };

    private String[][] optionValues = {
        {"hot", "cold", "mild", "rainy"},
        {"beach", "mountains", "culture", "nightlife"},
        {"budget", "mid", "premium", "luxury"},
        {"solo", "couple", "family", "group"},
        {"relax", "adventure", "photos", "shopping"},
        {"asian", "european", "latin", "middle"},
        {"short", "week", "twoweeks", "month"}
    };

    private String[] questionKeys = {"climate", "attraction", "budget", "travel", "priority", "cuisine", "duration"};

    // UI elements
    private TextView tvQuestionNumber;
    private TextView tvQuestionEmoji;
    private TextView tvQuestionTitle;
    private LinearLayout llOptions;
    private View progressBar;
    private View progressFill;
    private CardView[] optionCards = new CardView[4];
    private int selectedOptionIndex = -1;
    private com.google.android.material.button.MaterialButton btnNext;
    private LinearLayout llQuestionContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);

        setupViews();
        setupBackButton();
        displayQuestion(0);
    }

    private void setupViews() {
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvQuestionEmoji = findViewById(R.id.tv_question_emoji);
        tvQuestionTitle = findViewById(R.id.tv_question_title);
        llOptions = findViewById(R.id.ll_options);
        progressFill = findViewById(R.id.progress_fill);
        btnNext = findViewById(R.id.btn_next);
        llQuestionContainer = findViewById(R.id.ll_question_container);

        btnNext.setOnClickListener(v -> onNextClicked());
        btnNext.setEnabled(false);
        btnNext.setAlpha(0.5f);
    }

    private void setupBackButton() {
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (currentQuestion > 0) {
                    currentQuestion--;
                    selectedOptionIndex = -1;
                    displayQuestion(currentQuestion);
                } else {
                    finish();
                }
            });
        }
    }

    private void displayQuestion(int index) {
        selectedOptionIndex = -1;
        btnNext.setEnabled(false);
        btnNext.setAlpha(0.5f);

        // Update progress
        float progress = (float)(index + 1) / questionTitles.length;
        progressFill.post(() -> {
            android.view.ViewGroup.LayoutParams params = progressFill.getLayoutParams();
            params.width = (int)(progressFill.getRootView().getWidth() * progress);
            progressFill.setLayoutParams(params);
        });

        // Update question info
        tvQuestionNumber.setText((index + 1) + " / " + questionTitles.length);
        tvQuestionEmoji.setText(questionEmojis[index]);
        tvQuestionTitle.setText(questionTitles[index]);

        // Animate question in
        animateQuestionIn();

        // Build option cards
        llOptions.removeAllViews();
        for (int i = 0; i < optionTexts[index].length; i++) {
            View optionView = getLayoutInflater().inflate(R.layout.item_option_card, llOptions, false);
            TextView tvOption = optionView.findViewById(R.id.tv_option_text);
            tvOption.setText(optionTexts[index][i]);

            final int optionIndex = i;
            optionView.setOnClickListener(v -> selectOption(optionIndex));
            optionView.setTag(i);

            // Stagger animation
            optionView.setAlpha(0f);
            optionView.setTranslationX(80f);
            optionView.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(300)
                .setStartDelay(100 + i * 80L)
                .start();

            llOptions.addView(optionView);
        }
    }

    private void selectOption(int index) {
        selectedOptionIndex = index;

        // Update all cards visual state
        for (int i = 0; i < llOptions.getChildCount(); i++) {
            View card = llOptions.getChildAt(i);
            if (i == index) {
                card.setSelected(true);
                card.setBackgroundResource(R.drawable.bg_option_selected);
                TextView tv = card.findViewById(R.id.tv_option_text);
                tv.setTextColor(getColor(R.color.white));
                // Scale animation
                card.animate().scaleX(1.03f).scaleY(1.03f).setDuration(150).start();
            } else {
                card.setSelected(false);
                card.setBackgroundResource(R.drawable.bg_option_normal);
                TextView tv = card.findViewById(R.id.tv_option_text);
                tv.setTextColor(getColor(R.color.text_primary));
                card.animate().scaleX(1f).scaleY(1f).setDuration(150).start();
            }
        }

        btnNext.setEnabled(true);
        btnNext.animate().alpha(1f).setDuration(200).start();
    }

    private void onNextClicked() {
        if (selectedOptionIndex < 0) return;

        // Save answer
        answers.put(questionKeys[currentQuestion], optionValues[currentQuestion][selectedOptionIndex]);

        if (currentQuestion < questionTitles.length - 1) {
            currentQuestion++;
            displayQuestion(currentQuestion);
        } else {
            // Show results
            showResults();
        }
    }

    private void animateQuestionIn() {
        if (llQuestionContainer != null) {
            llQuestionContainer.setAlpha(0f);
            llQuestionContainer.setTranslationY(30f);
            llQuestionContainer.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(350)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
        }
    }

    private void showResults() {
        TravelDestination result = TravelMatcher.findBestMatch(answers);
        Intent intent = new Intent(this, TravelResultActivity.class);
        intent.putExtra("country", result.country);
        intent.putExtra("city", result.city);
        intent.putExtra("emoji", result.emoji);
        intent.putExtra("description", result.description);
        intent.putExtra("highlights", result.highlights);
        intent.putExtra("bestTime", result.bestTime);
        intent.putExtra("matchScore", result.matchScore);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
