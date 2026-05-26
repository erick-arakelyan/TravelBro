package com.erikarakelyan.travelbro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Профиль
        View profileBtn = findViewById(R.id.btn_profile);
        if (profileBtn != null) {
            profileBtn.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        }

        // ПОИСК: Исправленная логика.
        // Вместо TextWatcher используем OnEditorActionListener или просто клик,
        // чтобы не спамить открытиями Activity при каждой букве.
        EditText etSearch = findViewById(R.id.et_search);
        if (etSearch != null) {
            etSearch.setFocusable(false); // Делаем поле "кнопкой", чтобы открыть экран поиска
            etSearch.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, SearchResultsActivity.class);
                startActivity(intent);
            });
        }

        View searchLayout = findViewById(R.id.layout_search);
        if (searchLayout != null) {
            searchLayout.setOnClickListener(v -> startActivity(new Intent(this, SearchResultsActivity.class)));
        }

        // Карточки быстрого доступа
        setupClickCard(R.id.card_tickets, BookTicketsActivity.class);
        setupClickCard(R.id.card_tours, WeatherCurrencyActivity.class);
        setupClickCard(R.id.card_events, EventsActivity.class);
        setupClickCard(R.id.card_ai_guide, AiGuideActivity.class);
        setupClickCard(R.id.card_diary, DiaryActivity.class);
        setupClickCard(R.id.card_plan, PlanActivity.class);
        setupClickCard(R.id.card_world_map, WorldMapActivity.class);

        // Список популярных направлений
        RecyclerView rvDestinations = findViewById(R.id.rv_destinations);
        if (rvDestinations != null) {
            rvDestinations.setLayoutManager(new LinearLayoutManager(this));
            // Используем наш обновленный адаптер
            DestinationsAdapter adapter = new DestinationsAdapter(this);
            rvDestinations.setAdapter(adapter);
            // Метод теперь реализован в адаптере
            adapter.loadPopularDestinations();
        }

        // Нижняя навигация
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            setupBottomNav(bottomNav, R.id.nav_home);
        }
    }

    // Вспомогательный метод для чистоты кода
    private void setupClickCard(int id, Class<?> activityClass) {
        View card = findViewById(id);
        if (card != null) {
            card.setOnClickListener(v -> startActivity(new Intent(this, activityClass)));
        }
    }
}