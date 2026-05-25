package com.example.travelbro;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiaryActivity extends AppCompatActivity {

    private LinearLayout diaryContainer;
    private SharedPreferences prefs;
    private static final String PREFS_KEY = "diary_entries";
    private static final int PICK_IMAGE_REQUEST = 200;

    private String pendingImageBase64 = null;
    private ImageView dialogPreview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        prefs = getSharedPreferences("TravelBro", MODE_PRIVATE);
        diaryContainer = findViewById(R.id.diary_container);

        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        FloatingActionButton fab = findViewById(R.id.fab_add);
        if (fab != null) fab.setOnClickListener(v -> showAddDialog());

        loadEntries();
    }

    private void showAddDialog() {
        pendingImageBase64 = null;
        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 32, 48, 16);

        EditText etTitle = new EditText(this);
        etTitle.setHint(getString(R.string.diary_hint_title));
        etTitle.setTextSize(16);
        layout.addView(etTitle);

        EditText etText = new EditText(this);
        etText.setHint(getString(R.string.diary_hint_text));
        etText.setTextSize(14);
        etText.setMinLines(3);
        etText.setInputType(android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(0, 16, 0, 0);
        etText.setLayoutParams(textParams);
        layout.addView(etText);

        TextView tvRatingLabel = new TextView(this);
        tvRatingLabel.setText("Оценка поездки:");
        tvRatingLabel.setTextSize(14);
        tvRatingLabel.setTextColor(Color.parseColor("#6B7280"));
        LinearLayout.LayoutParams ratingLabelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ratingLabelParams.setMargins(0, 20, 0, 8);
        tvRatingLabel.setLayoutParams(ratingLabelParams);
        layout.addView(tvRatingLabel);

        int[] selectedRating = {5};
        TextView tvRatingValue = new TextView(this);
        tvRatingValue.setText("⭐ " + selectedRating[0] + " / 10");
        tvRatingValue.setTextSize(16);
        tvRatingValue.setTextColor(Color.parseColor("#F59E0B"));
        tvRatingValue.setTypeface(null, Typeface.BOLD);
        layout.addView(tvRatingValue);

        SeekBar seekBar = new SeekBar(this);
        seekBar.setMax(9);
        seekBar.setProgress(4);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
                selectedRating[0] = progress + 1;
                tvRatingValue.setText("⭐ " + selectedRating[0] + " / 10");
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });
        layout.addView(seekBar);

        dialogPreview = new ImageView(this);
        dialogPreview.setVisibility(View.GONE);
        dialogPreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 300);
        imgParams.setMargins(0, 8, 0, 8);
        dialogPreview.setLayoutParams(imgParams);
        layout.addView(dialogPreview);

        Button btnAddPhoto = new Button(this);
        btnAddPhoto.setText("📷 Прикрепить фото");
        btnAddPhoto.setTextColor(Color.WHITE);
        btnAddPhoto.setBackgroundColor(Color.parseColor("#1A73E8"));
        layout.addView(btnAddPhoto);
        btnAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        scrollView.addView(layout);

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.diary_add))
                .setView(scrollView)
                .setPositiveButton(getString(R.string.diary_save), (d, w) -> {
                    String title = etTitle.getText().toString().trim();
                    String text = etText.getText().toString().trim();
                    if (!title.isEmpty()) saveEntry(title, text, selectedRating[0], pendingImageBase64);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream is = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                int maxSize = 800;
                float scale = Math.min((float) maxSize / bitmap.getWidth(), (float) maxSize / bitmap.getHeight());
                bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*scale), (int)(bitmap.getHeight()*scale), true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                pendingImageBase64 = Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);
                if (dialogPreview != null) {
                    dialogPreview.setImageBitmap(bitmap);
                    dialogPreview.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private void saveEntry(String title, String text, int rating, String imageBase64) {
        try {
            String existing = prefs.getString(PREFS_KEY, "[]");
            JSONArray arr = new JSONArray(existing);
            JSONObject entry = new JSONObject();
            entry.put("title", title);
            entry.put("text", text);
            entry.put("rating", rating);
            entry.put("image", imageBase64 != null ? imageBase64 : "");
            entry.put("date", new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));
            arr.put(entry);
            prefs.edit().putString(PREFS_KEY, arr.toString()).apply();
            loadEntries();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void deleteEntry(int index) {
        try {
            String existing = prefs.getString(PREFS_KEY, "[]");
            JSONArray arr = new JSONArray(existing);
            JSONArray newArr = new JSONArray();
            for (int i = 0; i < arr.length(); i++) {
                if (i != index) newArr.put(arr.get(i));
            }
            prefs.edit().putString(PREFS_KEY, newArr.toString()).apply();
            loadEntries();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadEntries() {
        diaryContainer.removeAllViews();
        try {
            String existing = prefs.getString(PREFS_KEY, "[]");
            JSONArray arr = new JSONArray(existing);
            if (arr.length() == 0) {
                TextView tv = new TextView(this);
                tv.setText(getString(R.string.diary_empty));
                tv.setGravity(Gravity.CENTER);
                tv.setPadding(0, 100, 0, 0);
                diaryContainer.addView(tv);
                return;
            }
            for (int i = arr.length() - 1; i >= 0; i--) {
                JSONObject entry = arr.getJSONObject(i);
                addEntryCard(entry.getString("title"), entry.getString("text"),
                        entry.getString("date"), entry.optInt("rating", 0),
                        entry.optString("image", ""), i);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void addEntryCard(String title, String text, String date, int rating, String imageBase64, int index) {
        float density = getResources().getDisplayMetrics().density;

        HorizontalScrollView swiper = new HorizontalScrollView(this);
        swiper.setHorizontalScrollBarEnabled(false);
        swiper.setOverScrollMode(View.OVER_SCROLL_NEVER);
        LinearLayout.LayoutParams swiperParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        swiperParams.setMargins(0, 0, 0, (int)(16 * density));
        swiper.setLayoutParams(swiperParams);

        LinearLayout scrollContent = new LinearLayout(this);
        scrollContent.setOrientation(LinearLayout.HORIZONTAL);

        // ── Кнопка удаления ──────────────────────────────────
        int deleteWidth = (int)(80 * density);
        FrameLayout deleteBtn = new FrameLayout(this);
        deleteBtn.setLayoutParams(new LinearLayout.LayoutParams(
                deleteWidth, LinearLayout.LayoutParams.MATCH_PARENT));

        GradientDrawable deleteBg = new GradientDrawable();
        deleteBg.setColor(Color.parseColor("#EF4444"));
        deleteBg.setCornerRadii(new float[]{
                16*density, 16*density,
                0, 0,
                0, 0,
                16*density, 16*density
        });
        deleteBtn.setBackground(deleteBg);

        LinearLayout deleteBtnInner = new LinearLayout(this);
        deleteBtnInner.setOrientation(LinearLayout.VERTICAL);
        deleteBtnInner.setGravity(Gravity.CENTER);
        deleteBtnInner.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        TextView deleteEmoji = new TextView(this);
        deleteEmoji.setText("🗑");
        deleteEmoji.setTextSize(22);
        deleteEmoji.setGravity(Gravity.CENTER);

        TextView deleteLabel = new TextView(this);
        deleteLabel.setText("Удалить");
        deleteLabel.setTextSize(10);
        deleteLabel.setTextColor(Color.WHITE);
        deleteLabel.setGravity(Gravity.CENTER);
        deleteLabel.setTypeface(null, Typeface.BOLD);

        deleteBtnInner.addView(deleteEmoji);
        deleteBtnInner.addView(deleteLabel);
        deleteBtn.addView(deleteBtnInner);

        deleteBtn.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Удалить запись?")
                .setMessage("Это действие нельзя отменить.")
                .setPositiveButton("Удалить", (d, w) -> deleteEntry(index))
                .setNegativeButton("Отмена", null)
                .show());
        // ─────────────────────────────────────────────────────

        // Карточка
        CardView card = new CardView(this);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int cardWidth = screenWidth - (int)(32 * density);
        card.setLayoutParams(new LinearLayout.LayoutParams(cardWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
        card.setRadius((int)(16 * density));
        card.setCardElevation(4);
        card.setCardBackgroundColor(Color.WHITE);

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);

        View stripe = new View(this);
        stripe.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, (int)(4 * density)));
        stripe.setBackgroundColor(Color.parseColor("#EC4899"));
        inner.addView(stripe);

        if (!imageBase64.isEmpty()) {
            try {
                byte[] decodedBytes = Base64.decode(imageBase64, Base64.NO_WRAP);
                Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                ImageView iv = new ImageView(this);
                iv.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, (int)(220 * density)));
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setImageBitmap(bmp);
                inner.addView(iv);
            } catch (Exception ignored) {}
        }

        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setPadding((int)(40*density), (int)(24*density), (int)(40*density), (int)(24*density));

        TextView tvTitle = new TextView(this);
        tvTitle.setText("📍 " + title);
        tvTitle.setTextSize(16);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setTextColor(Color.parseColor("#1A1A2E"));
        contentLayout.addView(tvTitle);

        TextView tvDate = new TextView(this);
        tvDate.setText(date);
        tvDate.setTextSize(12);
        tvDate.setTextColor(Color.parseColor("#9CA3AF"));
        contentLayout.addView(tvDate);

        if (rating > 0) {
            TextView tvRating = new TextView(this);
            StringBuilder stars = new StringBuilder();
            for (int i = 0; i < rating; i++) stars.append("⭐");
            tvRating.setText(stars.toString() + " " + rating + "/10");
            tvRating.setTextSize(13);
            tvRating.setTextColor(Color.parseColor("#F59E0B"));
            contentLayout.addView(tvRating);
        }

        TextView tvText = new TextView(this);
        tvText.setText(text);
        tvText.setTextSize(14);
        tvText.setTextColor(Color.parseColor("#6B7280"));
        contentLayout.addView(tvText);

        inner.addView(contentLayout);
        card.addView(inner);

        scrollContent.addView(deleteBtn);
        scrollContent.addView(card);
        swiper.addView(scrollContent);

        swiper.post(() -> swiper.scrollTo(deleteWidth, 0));

        diaryContainer.addView(swiper);
    }
}