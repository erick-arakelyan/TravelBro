package com.erikarakelyan.travelbro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.*;

import java.io.*;
import java.net.*;
import java.util.Locale;
import java.util.Scanner;

public class AiGuideActivity extends AppCompatActivity {

    private static final String GEMINI_API_KEY = "Example";
    private static final int CAMERA_REQUEST = 1;
    private static final int CAMERA_PERM_REQUEST = 100;

    private final String COLOR_ACCENT = "#4A90E2";
    private final String COLOR_BOT_MSG = "#FFFFFF";
    private final String COLOR_USER_MSG = "#4A90E2";

    private LinearLayout chatContainer;
    private ScrollView scrollView;
    private EditText etInput;
    private ProgressBar progressBar;
    private TextToSpeech tts;
    private boolean ttsReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_guide);

        setupViews();

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("ru"));
                ttsReady = true;
            }
        });

        addBotMessage("Привет! Я AI гид по путешествиям. Задай мне любой вопрос или сфотографируй что-нибудь!");
    }

    private void setupViews() {
        chatContainer = findViewById(R.id.chat_container);
        scrollView = findViewById(R.id.scroll_chat);
        etInput = findViewById(R.id.et_input);
        progressBar = findViewById(R.id.progress_bar);

        ImageButton btnSend = findViewById(R.id.btn_send);
        ImageButton btnCamera = findViewById(R.id.btn_camera);
        ImageButton btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());
        btnSend.setOnClickListener(v -> sendMessage());
        btnCamera.setOnClickListener(v -> openCamera());
    }

    @Override
    protected void onDestroy() {
        if (tts != null) { tts.stop(); tts.shutdown(); }
        super.onDestroy();
    }

    private void sendMessage() {
        String text = etInput.getText().toString().trim();
        if (text.isEmpty()) return;
        etInput.setText("");
        addUserMessage(text);
        showLoading(true);

        new Thread(() -> {
            try {
                String answer = cleanMarkdown(askGeminiText(text));
                runOnUiThread(() -> { showLoading(false); addBotMessage(answer); });
            } catch (Exception e) {
                runOnUiThread(() -> { showLoading(false); addBotMessage("Ошибка: " + e.getMessage()); });
            }
        }).start();
    }

    private String cleanMarkdown(String text) {
        if (text == null) return "";
        text = text.replaceAll("\\*\\*([^*]+)\\*\\*", "$1");
        text = text.replaceAll("\\*([^*]+)\\*", "$1");
        text = text.replaceAll("(?m)^#{1,6}\\s*", "");
        text = text.replaceAll("```[\\w]*\\n?", "");
        text = text.replaceAll("```", "");
        text = text.replaceAll("`([^`]+)`", "$1");
        return text.trim();
    }

    private String askGeminiText(String text) throws Exception {
        URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + GEMINI_API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject sysPart = new JSONObject();
        sysPart.put("text", "Ты AI гид по путешествиям. Отвечай на русском языке. Не используй markdown форматирование. Пиши обычным текстом.");
        JSONArray sysParts = new JSONArray(); sysParts.put(sysPart);
        JSONObject systemInstruction = new JSONObject();
        systemInstruction.put("parts", sysParts);

        JSONObject part = new JSONObject(); part.put("text", text);
        JSONArray parts = new JSONArray(); parts.put(part);
        JSONObject msg = new JSONObject(); msg.put("parts", parts);
        JSONArray contents = new JSONArray(); contents.put(msg);

        JSONObject body = new JSONObject();
        body.put("system_instruction", systemInstruction);
        body.put("contents", contents);

        OutputStream os = conn.getOutputStream();
        os.write(body.toString().getBytes("UTF-8"));
        os.close();
        return parseGeminiResponse(conn);
    }

    private String askGeminiImage(byte[] imageBytes) throws Exception {
        URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + GEMINI_API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(20000);
        conn.setReadTimeout(20000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

        JSONObject textPart = new JSONObject();
        textPart.put("text", "Ты AI гид. Опиши фото обычным текстом. Если это достопримечательность — расскажи о ней подробно.");

        JSONObject imageData = new JSONObject();
        imageData.put("mime_type", "image/jpeg");
        imageData.put("data", base64Image);
        JSONObject imagePart = new JSONObject();
        imagePart.put("inline_data", imageData);

        JSONArray parts = new JSONArray(); parts.put(textPart); parts.put(imagePart);
        JSONObject msg = new JSONObject(); msg.put("parts", parts);
        JSONArray contents = new JSONArray(); contents.put(msg);
        JSONObject body = new JSONObject(); body.put("contents", contents);

        OutputStream os = conn.getOutputStream();
        os.write(body.toString().getBytes("UTF-8"));
        os.close();
        return parseGeminiResponse(conn);
    }

    private String parseGeminiResponse(HttpURLConnection conn) throws Exception {
        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();
        Scanner sc = new Scanner(is, "UTF-8");
        StringBuilder sb = new StringBuilder();
        while (sc.hasNextLine()) sb.append(sc.nextLine());
        sc.close();
        if (responseCode < 200 || responseCode >= 300) throw new Exception("HTTP " + responseCode + ": " + sb);
        JSONObject json = new JSONObject(sb.toString());
        return json.getJSONArray("candidates").getJSONObject(0)
                .getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text");
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_REQUEST);
        } else {
            launchCamera();
        }
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST);
        } else {
            Toast.makeText(this, "Камера недоступна", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) (extras != null ? extras.get("data") : null);
            if (photo == null) return;

            addUserMessage("📷 Фото отправлено, анализирую...");
            showLoading(true);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            byte[] imageBytes = stream.toByteArray();

            new Thread(() -> {
                try {
                    String answer = cleanMarkdown(askGeminiImage(imageBytes));
                    runOnUiThread(() -> { showLoading(false); addBotMessage(answer); });
                } catch (Exception e) {
                    runOnUiThread(() -> { showLoading(false); addBotMessage("Ошибка: " + e.getMessage()); });
                }
            }).start();
        }
    }

    private void showLoading(boolean show) {
        if (progressBar != null) progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void addUserMessage(String text) {
        runOnUiThread(() -> {
            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.END);
            row.setPadding(60, 12, 0, 12);

            GradientDrawable bg = new GradientDrawable();
            bg.setColor(Color.parseColor(COLOR_USER_MSG));
            bg.setCornerRadii(new float[]{40, 40, 10, 10, 40, 40, 40, 40});

            TextView tv = new TextView(this);
            tv.setText(text);
            tv.setTextColor(Color.WHITE);
            tv.setBackground(bg);
            tv.setPadding(40, 24, 40, 24);
            tv.setTextSize(15);
            tv.setElevation(2);

            row.addView(tv);
            chatContainer.addView(row);
            scrollToBottom();
        });
    }

    private void addBotMessage(String text) {
        runOnUiThread(() -> {
            LinearLayout wrapper = new LinearLayout(this);
            wrapper.setOrientation(LinearLayout.VERTICAL);
            wrapper.setGravity(Gravity.START);
            wrapper.setPadding(0, 12, 60, 12);

            GradientDrawable bg = new GradientDrawable();
            bg.setColor(Color.parseColor(COLOR_BOT_MSG));
            bg.setCornerRadii(new float[]{10, 10, 40, 40, 40, 40, 40, 40});

            TextView tv = new TextView(this);
            tv.setText(text);
            tv.setTextColor(Color.parseColor("#2D3135"));
            tv.setBackground(bg);
            tv.setPadding(40, 24, 40, 24);
            tv.setTextSize(15);
            tv.setElevation(2);

            // Кнопки управления звуком
            LinearLayout audioControls = new LinearLayout(this);
            audioControls.setOrientation(LinearLayout.HORIZONTAL);
            audioControls.setPadding(10, 8, 0, 0);

            Button btnSpeak = new Button(this, null, android.R.attr.borderlessButtonStyle);
            btnSpeak.setText("🔊 Слушать");
            btnSpeak.setAllCaps(false);
            btnSpeak.setTextSize(12);
            btnSpeak.setTextColor(Color.parseColor(COLOR_ACCENT));

            Button btnReset = new Button(this, null, android.R.attr.borderlessButtonStyle);
            btnReset.setText("🔄 Сначала");
            btnReset.setAllCaps(false);
            btnReset.setTextSize(12);
            btnReset.setTextColor(Color.parseColor("#99A3AD"));
            btnReset.setVisibility(View.GONE);

            btnSpeak.setOnClickListener(v -> {
                if (!ttsReady) return;
                if (tts.isSpeaking()) {
                    tts.stop();
                    btnSpeak.setText("▶️ Продолжить");
                    btnReset.setVisibility(View.VISIBLE);
                } else {
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "msg_id");
                    btnSpeak.setText("⏹ Стоп");
                    btnReset.setVisibility(View.VISIBLE);
                }
            });

            btnReset.setOnClickListener(v -> {
                tts.stop();
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "msg_id");
                btnSpeak.setText("⏹ Стоп");
            });

            audioControls.addView(btnSpeak);
            audioControls.addView(btnReset);

            wrapper.addView(tv);
            wrapper.addView(audioControls);
            chatContainer.addView(wrapper);
            scrollToBottom();
        });
    }

    private void scrollToBottom() {
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }
}