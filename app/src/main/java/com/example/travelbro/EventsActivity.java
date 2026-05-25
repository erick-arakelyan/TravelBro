package com.example.travelbro;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class EventsActivity extends AppCompatActivity {

    private WebView webView;
    private EditText etSearch;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        ImageButton btnBack = findViewById(R.id.btn_back);
        ImageButton btnSearch = findViewById(R.id.btn_search);
        etSearch = findViewById(R.id.et_search);
        progressBar = findViewById(R.id.progress_bar);
        webView = findViewById(R.id.web_view);

        // Кнопка назад
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Настройка WebView
        setupWebView();

        // Кнопка поиска
        btnSearch.setOnClickListener(v -> doSearch());

        // Поиск по нажатию Enter на клавиатуре
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                doSearch();
                return true;
            }
            return false;
        });

        // Загружаем Eventbrite по умолчанию
        webView.loadUrl("https://www.eventbrite.com/");
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUserAgentString(
                "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void doSearch() {
        String query = etSearch.getText().toString().trim();
        if (query.isEmpty()) return;

        // Скрываем клавиатуру
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

        // Формируем URL поиска Eventbrite
        String encodedQuery = android.net.Uri.encode(query);
        String url = "https://www.eventbrite.com/d/worldwide/" + encodedQuery + "/";
        webView.loadUrl(url);
    }

    // Кнопка "назад" внутри WebView
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}