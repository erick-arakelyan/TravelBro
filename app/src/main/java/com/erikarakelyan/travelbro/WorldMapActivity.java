package com.erikarakelyan.travelbro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class WorldMapActivity extends AppCompatActivity {

    private WebView webView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_map);

        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        webView = findViewById(R.id.web_map);

        // Сначала проверяем разрешения, потом запускаем карту
        checkLocationPermissions();
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Запрашиваем разрешение у пользователя
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Если разрешение уже есть, запускаем карту
            setupWebView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupWebView();
            } else {
                Toast.makeText(this, "Доступ к карте ограничен без геолокации", Toast.LENGTH_SHORT).show();
                setupWebView(); // Все равно запускаем, но без GPS
            }
        }
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        // Этот клиент отвечает за всплывающее окно "Разрешить сайту доступ к гео" внутри WebView
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        String html = "<!DOCTYPE html><html><head><meta charset='utf-8'/>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no' />"
                + "<link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css'/>"
                + "<link rel='stylesheet' href='https://unpkg.com/leaflet-routing-machine@3.2.12/dist/leaflet-routing-machine.css' />"
                + "<style>html,body,#map{width:100%;height:100%;margin:0;padding:0;background:#F8F9FA;}"
                + ".leaflet-control-container .leaflet-routing-container { background: white; padding: 5px; border-radius: 8px; font-family: sans-serif; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }"
                + "</style></head><body>"
                + "<div id='map'></div>"
                + "<script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>"
                + "<script src='https://unpkg.com/leaflet-routing-machine@3.2.12/dist/leaflet-routing-machine.js'></script>"
                + "<script>"
                + "var map = L.map('map', {center:[40.18, 44.51], zoom:13});"
                + "L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);"
                + ""
                + "var control = L.Routing.control({"
                + "  waypoints: [],"
                + "  routeWhileDragging: true,"
                + "  show: true," // Показывает текстовую инструкцию маршрута
                + "  lineOptions: { styles: [{color: '#4A90E2', opacity: 0.8, weight: 6}] }"
                + "}).addTo(map);"
                + ""
                + "map.on('locationfound', function(e) {"
                + "  L.marker(e.latlng).addTo(map).bindPopup('Вы здесь').openPopup();"
                + "  map.setView(e.latlng, 15);"
                + "});"
                + "map.locate({setView: true, maxZoom: 16});"
                + ""
                + "var points = [];"
                + "map.on('click', function(e) {"
                + "  if (points.length >= 2) { points = []; control.setWaypoints([]); }"
                + "  points.push(e.latlng);"
                + "  L.marker(e.latlng).addTo(map);"
                + "  if (points.length === 2) { control.setWaypoints(points); }"
                + "});"
                + "</script></body></html>";

        webView.loadDataWithBaseURL("https://unpkg.com", html, "text/html", "UTF-8", null);
    }
}