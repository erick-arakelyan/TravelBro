package com.erikarakelyan.travelbro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MenuActivity extends BaseSectionActivity {

    private static final String[][] PACKAGES = {
            {"✈️ + 🏨", "Flight + Hotel", "All-inclusive bundles starting from $499", "Save up to 30%"},
            {"🚢", "Cruise Packages", "Caribbean & Mediterranean cruises", "7 nights from $899"},
            {"🏕️", "Adventure Tours", "Trekking, safaris & outdoor expeditions", "From $299/person"},
            {"🏙️", "City Breaks", "Weekend getaways to world capitals", "2 nights from $149"},
            {"🌴", "Beach Resorts", "Tropical paradise getaways", "All-inclusive from $599"},
            {"🎿", "Winter Sports", "Ski resorts across Europe & North America", "5 nights from $699"},
            {"🍷", "Food & Wine Tours", "Culinary experiences in top destinations", "From $199/day"},
            {"👨‍👩‍👧", "Family Packages", "Kid-friendly adventures for the whole family", "From $399/family"},
    };

    @Override
    protected int getLayoutResId() { return R.layout.activity_menu; }

    @Override
    protected void onSectionCreated(Bundle savedInstanceState) {
        RecyclerView rv = findViewById(R.id.menu_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new PackageAdapter());
    }

    private class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.VH> {
        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_package, p, false));
        }
        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            h.icon.setText(PACKAGES[pos][0]); h.title.setText(PACKAGES[pos][1]);
            h.desc.setText(PACKAGES[pos][2]); h.price.setText(PACKAGES[pos][3]);
            h.itemView.setAlpha(0f); h.itemView.setTranslationX(80f);
            h.itemView.animate().alpha(1f).translationX(0f).setDuration(400)
                    .setStartDelay(pos * 60L)
                    .setInterpolator(new android.view.animation.DecelerateInterpolator(2f)).start();
        }
        @Override public int getItemCount() { return PACKAGES.length; }
        class VH extends RecyclerView.ViewHolder {
            TextView icon, title, desc, price;
            VH(View v) { super(v); icon=v.findViewById(R.id.pkg_icon); title=v.findViewById(R.id.pkg_title); desc=v.findViewById(R.id.pkg_desc); price=v.findViewById(R.id.pkg_price); }
        }
    }
}
