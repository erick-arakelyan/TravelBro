package com.erikarakelyan.travelbro;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import okhttp3.*;

public class WeatherCurrencyActivity extends AppCompatActivity {

    private static final String WEATHER_KEY  = "Example";
    private static final String CURRENCY_KEY = "Examplegi";

    private TextView tabWeather, tabCurrency;
    private View panelWeather, panelCurrency;

    // Погода — текущая
    private AutoCompleteTextView etCity;
    private Button btnGetWeather;
    private ProgressBar pbWeather;
    private LinearLayout llWeatherResult;
    private TextView tvWeatherCity, tvWeatherTemp, tvWeatherDesc,
            tvWeatherHumidity, tvWeatherWind, tvWeatherFeels, tvWeatherIcon;

    // Погода — прогноз
    private RecyclerView rvForecast;
    private CardView cardForecast;
    private ForecastAdapter forecastAdapter;

    // Валюты
    private AutoCompleteTextView etFromCurrency, etToCurrency;
    private EditText etAmount;
    private Button btnConvert;
    private ProgressBar pbCurrency;
    private LinearLayout llCurrencyResult;
    private TextView tvCurrencyResult, tvCurrencyRate;

    private final OkHttpClient client = new OkHttpClient();

    private static final List<String> CITIES = Arrays.asList(
            "Ереван","Москва","Санкт-Петербург","Тбилиси","Баку","Стамбул",
            "Анталья","Дубай","Лондон","Париж","Берлин","Рим","Барселона",
            "Амстердам","Прага","Вена","Варшава","Афины","Бангкок","Сингапур",
            "Токио","Дели","Гоа","Бали","Пхукет","Хургада","Нью-Йорк",
            "Лос-Анджелес","Алматы","Ташкент","Бишкек","Минск","Сочи","Казань"
    );

    private static final List<String> CURRENCIES = Arrays.asList(
            "USD","EUR","RUB","AMD","GBP","TRY","AED","THB","JPY","CNY",
            "KZT","GEL","AZN","UZS","BYN","UAH","CHF","SEK","NOK","CAD",
            "AUD","INR","SGD","HKD","KRW","EGP","MXN","BRL","PLN","CZK"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_currency);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        tabWeather    = findViewById(R.id.tab_weather);
        tabCurrency   = findViewById(R.id.tab_currency);
        panelWeather  = findViewById(R.id.panel_weather);
        panelCurrency = findViewById(R.id.panel_currency);

        // Текущая погода
        etCity            = findViewById(R.id.et_city);
        btnGetWeather     = findViewById(R.id.btn_get_weather);
        pbWeather         = findViewById(R.id.pb_weather);
        llWeatherResult   = findViewById(R.id.ll_weather_result);
        tvWeatherCity     = findViewById(R.id.tv_weather_city);
        tvWeatherTemp     = findViewById(R.id.tv_weather_temp);
        tvWeatherDesc     = findViewById(R.id.tv_weather_desc);
        tvWeatherHumidity = findViewById(R.id.tv_weather_humidity);
        tvWeatherWind     = findViewById(R.id.tv_weather_wind);
        tvWeatherFeels    = findViewById(R.id.tv_weather_feels);
        tvWeatherIcon     = findViewById(R.id.tv_weather_icon);

        // Прогноз
        rvForecast    = findViewById(R.id.rv_forecast);
        cardForecast  = findViewById(R.id.card_forecast);
        rvForecast.setLayoutManager(new LinearLayoutManager(this));
        forecastAdapter = new ForecastAdapter(new ArrayList<>());
        rvForecast.setAdapter(forecastAdapter);

        // Валюты
        etFromCurrency   = findViewById(R.id.et_from_currency);
        etToCurrency     = findViewById(R.id.et_to_currency);
        etAmount         = findViewById(R.id.et_amount);
        btnConvert       = findViewById(R.id.btn_convert);
        pbCurrency       = findViewById(R.id.pb_currency);
        llCurrencyResult = findViewById(R.id.ll_currency_result);
        tvCurrencyResult = findViewById(R.id.tv_currency_result);
        tvCurrencyRate   = findViewById(R.id.tv_currency_rate);

        setupAutocomplete(etCity, CITIES);
        setupAutocomplete(etFromCurrency, CURRENCIES);
        setupAutocomplete(etToCurrency, CURRENCIES);

        etFromCurrency.setText("USD");
        etToCurrency.setText("AMD");
        etAmount.setText("100");

        tabWeather.setOnClickListener(v -> showTab(true));
        tabCurrency.setOnClickListener(v -> showTab(false));

        btnGetWeather.setOnClickListener(v -> fetchWeatherAndForecast());
        btnConvert.setOnClickListener(v -> fetchCurrency());
    }

    private void showTab(boolean isWeather) {
        if (isWeather) {
            tabWeather.setBackgroundResource(R.drawable.bg_tab_active_light);
            tabWeather.setTextColor(0xFF111214);
            tabCurrency.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            tabCurrency.setTextColor(0xFF6B7280);
            panelWeather.setVisibility(View.VISIBLE);
            panelCurrency.setVisibility(View.GONE);
        } else {
            tabCurrency.setBackgroundResource(R.drawable.bg_tab_active_light);
            tabCurrency.setTextColor(0xFF111214);
            tabWeather.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            tabWeather.setTextColor(0xFF6B7280);
            panelCurrency.setVisibility(View.VISIBLE);
            panelWeather.setVisibility(View.GONE);
        }
    }

    private void setupAutocomplete(AutoCompleteTextView actv, List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, items);
        actv.setAdapter(adapter);
        actv.setThreshold(1);
    }

    // ── ПОГОДА: текущая + прогноз ────────────────────────────────────────────

    private void fetchWeatherAndForecast() {
        String city = etCity.getText().toString().trim();
        if (city.isEmpty()) {
            Toast.makeText(this, "Введите город", Toast.LENGTH_SHORT).show();
            return;
        }

        btnGetWeather.setEnabled(false);
        pbWeather.setVisibility(View.VISIBLE);
        llWeatherResult.setVisibility(View.GONE);
        rvForecast.setVisibility(View.GONE);
        cardForecast.setVisibility(View.GONE); // скрываем карточку прогноза

        fetchCurrentWeather(city);
        fetchForecast(city);
    }

    private void fetchCurrentWeather(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather"
                + "?q=" + city
                + "&appid=" + WEATHER_KEY
                + "&units=metric"
                + "&lang=ru";

        client.newCall(new Request.Builder().url(url).build())
                .enqueue(new okhttp3.Callback() {
                    @Override public void onFailure(okhttp3.Call call, IOException e) {
                        runOnUiThread(() -> {
                            pbWeather.setVisibility(View.GONE);
                            btnGetWeather.setEnabled(true);
                            Toast.makeText(WeatherCurrencyActivity.this,
                                    "Ошибка сети", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override public void onResponse(okhttp3.Call call, okhttp3.Response response)
                            throws IOException {
                        String json = response.body().string();
                        runOnUiThread(() -> {
                            pbWeather.setVisibility(View.GONE);
                            btnGetWeather.setEnabled(true);
                            try {
                                JSONObject obj = new JSONObject(json);
                                if (obj.has("cod") && obj.getInt("cod") != 200) {
                                    Toast.makeText(WeatherCurrencyActivity.this,
                                            "Город не найден", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String cityName  = obj.getString("name");
                                double temp      = obj.getJSONObject("main").getDouble("temp");
                                double feelsLike = obj.getJSONObject("main").getDouble("feels_like");
                                int humidity     = obj.getJSONObject("main").getInt("humidity");
                                double windSpeed = obj.getJSONObject("wind").getDouble("speed");
                                String desc      = obj.getJSONArray("weather")
                                        .getJSONObject(0).getString("description");
                                String iconCode  = obj.getJSONArray("weather")
                                        .getJSONObject(0).getString("icon");

                                tvWeatherCity.setText(cityName);
                                tvWeatherTemp.setText(String.format(Locale.US, "%.0f°C", temp));
                                tvWeatherDesc.setText(capitalize(desc));
                                tvWeatherHumidity.setText("💧 Влажность: " + humidity + "%");
                                tvWeatherWind.setText("💨 Ветер: " + windSpeed + " м/с");
                                tvWeatherFeels.setText("🌡 Ощущается: "
                                        + String.format(Locale.US, "%.0f°C", feelsLike));
                                tvWeatherIcon.setText(getWeatherEmoji(iconCode));

                                llWeatherResult.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                Toast.makeText(WeatherCurrencyActivity.this,
                                        "Ошибка данных", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void fetchForecast(String city) {
        String url = "https://api.openweathermap.org/data/2.5/forecast"
                + "?q=" + city
                + "&appid=" + WEATHER_KEY
                + "&units=metric"
                + "&lang=ru"
                + "&cnt=40";

        client.newCall(new Request.Builder().url(url).build())
                .enqueue(new okhttp3.Callback() {
                    @Override public void onFailure(okhttp3.Call call, IOException e) { /* игнорируем */ }

                    @Override public void onResponse(okhttp3.Call call, okhttp3.Response response)
                            throws IOException {
                        String json = response.body().string();
                        runOnUiThread(() -> {
                            try {
                                JSONObject obj = new JSONObject(json);
                                JSONArray list = obj.getJSONArray("list");

                                Map<String, List<Double>> tempsByDay = new LinkedHashMap<>();
                                Map<String, String> descByDay = new LinkedHashMap<>();
                                Map<String, String> iconByDay = new LinkedHashMap<>();

                                SimpleDateFormat sdfIn  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                SimpleDateFormat sdfKey = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                SimpleDateFormat sdfOut = new SimpleDateFormat("EEE, d MMM", new Locale("ru"));

                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject item = list.getJSONObject(i);
                                    String dtTxt = item.getString("dt_txt");
                                    Date date = sdfIn.parse(dtTxt);
                                    String key = sdfKey.format(date);

                                    double t = item.getJSONObject("main").getDouble("temp");
                                    tempsByDay.computeIfAbsent(key, k -> new ArrayList<>()).add(t);

                                    if (dtTxt.contains("12:00:00") || !descByDay.containsKey(key)) {
                                        descByDay.put(key, item.getJSONArray("weather")
                                                .getJSONObject(0).getString("description"));
                                        iconByDay.put(key, item.getJSONArray("weather")
                                                .getJSONObject(0).getString("icon"));
                                    }
                                }

                                List<ForecastDay> days = new ArrayList<>();
                                String today = sdfKey.format(new Date());

                                for (Map.Entry<String, List<Double>> entry : tempsByDay.entrySet()) {
                                    String key = entry.getKey();
                                    if (key.equals(today)) continue;

                                    List<Double> temps = entry.getValue();
                                    double min = Collections.min(temps);
                                    double max = Collections.max(temps);

                                    Date d = sdfKey.parse(key);
                                    String label = sdfOut.format(d);

                                    days.add(new ForecastDay(
                                            label,
                                            getWeatherEmoji(iconByDay.getOrDefault(key, "01d")),
                                            capitalize(descByDay.getOrDefault(key, "")),
                                            (int) Math.round(max),
                                            (int) Math.round(min)
                                    ));
                                }

                                forecastAdapter.updateData(days);
                                rvForecast.setVisibility(View.VISIBLE);
                                cardForecast.setVisibility(View.VISIBLE); // показываем карточку

                            } catch (Exception e) {
                                // Прогноз не загрузился — не критично
                            }
                        });
                    }
                });
    }

    private String getWeatherEmoji(String icon) {
        if (icon.startsWith("01")) return "☀️";
        if (icon.startsWith("02")) return "⛅";
        if (icon.startsWith("03") || icon.startsWith("04")) return "☁️";
        if (icon.startsWith("09") || icon.startsWith("10")) return "🌧️";
        if (icon.startsWith("11")) return "⛈️";
        if (icon.startsWith("13")) return "❄️";
        if (icon.startsWith("50")) return "🌫️";
        return "🌤️";
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    // ── ВАЛЮТЫ ──────────────────────────────────────────────────────────────

    private void fetchCurrency() {
        String from   = etFromCurrency.getText().toString().trim().toUpperCase();
        String to     = etToCurrency.getText().toString().trim().toUpperCase();
        String amtStr = etAmount.getText().toString().trim();

        if (from.isEmpty() || to.isEmpty() || amtStr.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try { amount = Double.parseDouble(amtStr); }
        catch (Exception e) {
            Toast.makeText(this, "Введите корректную сумму", Toast.LENGTH_SHORT).show();
            return;
        }

        btnConvert.setEnabled(false);
        pbCurrency.setVisibility(View.VISIBLE);
        llCurrencyResult.setVisibility(View.GONE);

        String url = "https://v6.exchangerate-api.com/v6/" + CURRENCY_KEY
                + "/pair/" + from + "/" + to + "/" + amount;

        client.newCall(new Request.Builder().url(url).build())
                .enqueue(new okhttp3.Callback() {
                    @Override public void onFailure(okhttp3.Call call, IOException e) {
                        runOnUiThread(() -> {
                            pbCurrency.setVisibility(View.GONE);
                            btnConvert.setEnabled(true);
                            Toast.makeText(WeatherCurrencyActivity.this,
                                    "Ошибка сети", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override public void onResponse(okhttp3.Call call, okhttp3.Response response)
                            throws IOException {
                        String json = response.body().string();
                        runOnUiThread(() -> {
                            pbCurrency.setVisibility(View.GONE);
                            btnConvert.setEnabled(true);
                            try {
                                JSONObject obj = new JSONObject(json);
                                if (!"success".equals(obj.getString("result"))) {
                                    Toast.makeText(WeatherCurrencyActivity.this,
                                            "Валюта не найдена", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                double converted = obj.getDouble("conversion_result");
                                double rate      = obj.getDouble("conversion_rate");

                                tvCurrencyResult.setText(String.format(Locale.US,
                                        "%.2f %s = %.2f %s", amount, from, converted, to));
                                tvCurrencyRate.setText(String.format(Locale.US,
                                        "1 %s = %.4f %s", from, rate, to));

                                llCurrencyResult.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                Toast.makeText(WeatherCurrencyActivity.this,
                                        "Ошибка данных", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
}