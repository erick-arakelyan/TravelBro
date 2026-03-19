package com.erikarakelyan.travelbro;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class PlanActivity extends BaseSectionActivity {

    private List<String> itinerary = new ArrayList<>();
    private TextView itineraryView;
    private int dayCount = 0;

    private static final String[] ACTIVITIES = {
            "🏛️ Visit local museum", "🍜 Street food tour", "🌅 Sunrise hike",
            "🏖️ Beach day", "🎭 Cultural show", "🛒 Market shopping",
    };

    @Override
    protected int getLayoutResId() { return R.layout.activity_plan; }

    @Override
    protected void onSectionCreated(Bundle savedInstanceState) {
        itineraryView = findViewById(R.id.itinerary_text);

        Button addDayBtn = findViewById(R.id.btn_add_day);
        if (addDayBtn != null) {
            addDayBtn.setOnClickListener(v -> {
                dayCount++;
                String activity = ACTIVITIES[(int)(Math.random() * ACTIVITIES.length)];
                itinerary.add("📅 Day " + dayCount + ": " + activity);
                updateItineraryView();
                v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(80)
                        .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(120).start()).start();
            });
        }
        Button clearBtn = findViewById(R.id.btn_clear);
        if (clearBtn != null) {
            clearBtn.setOnClickListener(v -> {
                itinerary.clear(); dayCount = 0; updateItineraryView();
                Toast.makeText(this, "Itinerary cleared", Toast.LENGTH_SHORT).show();
            });
        }
        Button shareBtn = findViewById(R.id.btn_share);
        if (shareBtn != null) {
            shareBtn.setOnClickListener(v -> {
                if (itinerary.isEmpty()) Toast.makeText(this, "Add days to your plan first!", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "Sharing itinerary...", Toast.LENGTH_SHORT).show();
            });
        }
        updateItineraryView();
    }

    private void updateItineraryView() {
        if (itineraryView == null) return;
        if (itinerary.isEmpty()) {
            itineraryView.setText("Tap '+ Add Day' to start building your trip plan ✨");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String item : itinerary) sb.append(item).append("\n\n");
            itineraryView.setText(sb.toString().trim());
        }
    }
}
