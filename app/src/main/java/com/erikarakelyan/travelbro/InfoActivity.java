package com.erikarakelyan.travelbro;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoActivity extends BaseSectionActivity {

    private static final String[][] INFO_ITEMS = {
            {"🛂", "Visa Requirements", "Check entry requirements for 190+ countries based on your passport."},
            {"💉", "Health & Vaccinations", "Stay safe with recommended vaccinations and health advisories."},
            {"💱", "Currency & Exchange", "Live exchange rates and tips on getting the best deals."},
            {"🌐", "Language Guide", "Essential phrases in 50+ languages with audio pronunciation."},
            {"⚡", "Power & Plugs", "Plug types and voltage info for every destination."},
            {"🌡️", "Climate Guide", "Best times to visit and what to pack for any weather."},
            {"📱", "SIM & Data", "Local SIM options and roaming tips for staying connected."},
            {"🚑", "Emergency Contacts", "Local emergency numbers and nearest hospitals worldwide."},
    };

    @Override
    protected int getLayoutResId() { return R.layout.activity_info; }

    @Override
    protected void onSectionCreated(Bundle savedInstanceState) {
        LinearLayout container = findViewById(R.id.info_container);
        if (container == null) return;
        for (int i = 0; i < INFO_ITEMS.length; i++) {
            View card = getLayoutInflater().inflate(R.layout.item_info, container, false);
            ((TextView) card.findViewById(R.id.info_icon)).setText(INFO_ITEMS[i][0]);
            ((TextView) card.findViewById(R.id.info_title)).setText(INFO_ITEMS[i][1]);
            ((TextView) card.findViewById(R.id.info_desc)).setText(INFO_ITEMS[i][2]);
            final int idx = i;
            card.setAlpha(0f); card.setTranslationY(30f);
            card.postDelayed(() -> card.animate().alpha(1f).translationY(0f)
                    .setDuration(350).setInterpolator(new android.view.animation.DecelerateInterpolator()).start(),
                    200 + idx * 80L);
            container.addView(card);
        }
    }
}
