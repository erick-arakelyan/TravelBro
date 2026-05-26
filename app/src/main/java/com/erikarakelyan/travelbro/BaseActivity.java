package com.erikarakelyan.travelbro;

import android.content.Intent;
import android.content.res.Configuration;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {

    protected void setupBottomNav(BottomNavigationView bottomNav, int selectedId) {
        bottomNav.setSelectedItemId(selectedId);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedId) return true;
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
            } else if (id == R.id.nav_tickets) {
                startActivity(new Intent(this, BookTicketsActivity.class));
            } else if (id == R.id.nav_tours) {
                startActivity(new Intent(this, WeatherCurrencyActivity.class));
            } else if (id == R.id.nav_events) {
                startActivity(new Intent(this, EventsActivity.class));
            } else if (id == R.id.nav_countries) {
                startActivity(new Intent(this, CountriesActivity.class));
            }
            return true;
        });
    }

    protected void switchLanguage(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
