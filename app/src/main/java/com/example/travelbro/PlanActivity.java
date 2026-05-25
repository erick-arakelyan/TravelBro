package com.example.travelbro;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONObject;

public class PlanActivity extends AppCompatActivity {

    private LinearLayout planContainer;
    private SharedPreferences prefs;
    private static final String PREFS_KEY = "plan_steps_v2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        prefs = getSharedPreferences("TravelBro", MODE_PRIVATE);
        planContainer = findViewById(R.id.plan_container);

        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        FloatingActionButton fab = findViewById(R.id.fab_add);
        if (fab != null) fab.setOnClickListener(v -> showAddDialog());

        loadSteps();
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.plan_add_step));

        EditText et = new EditText(this);
        et.setHint(getString(R.string.plan_hint));

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(60, 40, 60, 10);
        container.addView(et);

        builder.setView(container);
        builder.setPositiveButton(getString(R.string.diary_save), (d, w) -> {
            String step = et.getText().toString().trim();
            if (!step.isEmpty()) saveStep(step);
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void saveStep(String text) {
        try {
            String existing = prefs.getString(PREFS_KEY, "[]");
            JSONArray arr = new JSONArray(existing);
            JSONObject stepObj = new JSONObject();
            stepObj.put("text", text);
            stepObj.put("isDone", false);
            arr.put(stepObj);
            prefs.edit().putString(PREFS_KEY, arr.toString()).apply();
            loadSteps();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void deleteStep(int index) {
        try {
            String existing = prefs.getString(PREFS_KEY, "[]");
            JSONArray arr = new JSONArray(existing);
            JSONArray newArr = new JSONArray();
            for (int i = 0; i < arr.length(); i++) {
                if (i != index) newArr.put(arr.get(i));
            }
            prefs.edit().putString(PREFS_KEY, newArr.toString()).apply();
            loadSteps();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void toggleStep(int index) {
        try {
            String existing = prefs.getString(PREFS_KEY, "[]");
            JSONArray arr = new JSONArray(existing);
            JSONObject obj = arr.getJSONObject(index);
            obj.put("isDone", !obj.getBoolean("isDone"));
            prefs.edit().putString(PREFS_KEY, arr.toString()).apply();
            loadSteps();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadSteps() {
        planContainer.removeAllViews();
        try {
            String existing = prefs.getString(PREFS_KEY, "[]");
            JSONArray arr = new JSONArray(existing);
            if (arr.length() == 0) {
                TextView tv = new TextView(this);
                tv.setText(getString(R.string.plan_empty));
                tv.setGravity(Gravity.CENTER);
                tv.setPadding(0, 100, 0, 0);
                planContainer.addView(tv);
                return;
            }
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                addStepCard(i + 1, obj.getString("text"), obj.getBoolean("isDone"), i);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void addStepCard(int stepNum, String text, boolean isDone, int index) {
        float density = getResources().getDisplayMetrics().density;

        HorizontalScrollView swiper = new HorizontalScrollView(this);
        swiper.setHorizontalScrollBarEnabled(false);
        swiper.setOverScrollMode(View.OVER_SCROLL_NEVER);
        LinearLayout.LayoutParams swiperParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        swiperParams.setMargins(0, 0, 0, (int)(12 * density));
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
                .setTitle("Удалить шаг?")
                .setMessage("Это действие нельзя отменить.")
                .setPositiveButton("Удалить", (d, w) -> deleteStep(index))
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
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setPadding((int)(32*density), (int)(28*density), (int)(32*density), (int)(28*density));
        inner.setGravity(Gravity.CENTER_VERTICAL);
        inner.setOnClickListener(v -> toggleStep(index));

        TextView tvNum = new TextView(this);
        tvNum.setText(String.valueOf(stepNum));
        tvNum.setTextSize(18);
        tvNum.setTypeface(null, Typeface.BOLD);
        tvNum.setTextColor(Color.WHITE);
        tvNum.setBackgroundColor(Color.parseColor("#0891B2"));
        tvNum.setGravity(Gravity.CENTER);
        tvNum.setPadding((int)(10*density), (int)(10*density), (int)(10*density), (int)(10*density));
        LinearLayout.LayoutParams np = new LinearLayout.LayoutParams((int)(45*density), (int)(45*density));
        np.setMargins(0, 0, (int)(24*density), 0);
        tvNum.setLayoutParams(np);

        TextView tvText = new TextView(this);
        tvText.setText(text);
        tvText.setTextSize(15);
        tvText.setTextColor(Color.parseColor("#1A1A2E"));
        tvText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        if (isDone) {
            tvText.setPaintFlags(tvText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        inner.addView(tvNum);
        inner.addView(tvText);

        if (isDone) {
            TextView tvCheck = new TextView(this);
            tvCheck.setText("✓");
            tvCheck.setTextColor(Color.parseColor("#10B981"));
            tvCheck.setTextSize(20);
            tvCheck.setTypeface(null, Typeface.BOLD);
            inner.addView(tvCheck);
        }

        card.addView(inner);
        scrollContent.addView(deleteBtn);
        scrollContent.addView(card);
        swiper.addView(scrollContent);

        swiper.post(() -> swiper.scrollTo(deleteWidth, 0));

        planContainer.addView(swiper);
    }
}