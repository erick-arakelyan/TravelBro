package com.erikarakelyan.travelbro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CountriesActivity extends BaseSectionActivity {

    private static final String[][] COUNTRIES = {
            {"🇯🇵", "Japan", "Asia"},
            {"🇫🇷", "France", "Europe"},
            {"🇧🇷", "Brazil", "S. America"},
            {"🇮🇳", "India", "Asia"},
            {"🇦🇺", "Australia", "Oceania"},
            {"🇲🇽", "Mexico", "N. America"},
            {"🇿🇦", "South Africa", "Africa"},
            {"🇨🇦", "Canada", "N. America"},
            {"🇮🇹", "Italy", "Europe"},
            {"🇹🇭", "Thailand", "Asia"},
            {"🇳🇿", "New Zealand", "Oceania"},
            {"🇵🇹", "Portugal", "Europe"},
    };

    @Override
    protected int getLayoutResId() { return R.layout.activity_countries; }

    @Override
    protected void onSectionCreated(Bundle savedInstanceState) {
        RecyclerView rv = findViewById(R.id.countries_recycler);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(new CountriesAdapter());
    }

    private class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.VH> {
        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_country, p, false));
        }
        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            h.flag.setText(COUNTRIES[pos][0]);
            h.name.setText(COUNTRIES[pos][1]);
            h.region.setText(COUNTRIES[pos][2]);
            h.itemView.setAlpha(0f); h.itemView.setScaleX(0.85f); h.itemView.setScaleY(0.85f);
            h.itemView.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(350)
                    .setStartDelay(pos * 60L)
                    .setInterpolator(new android.view.animation.OvershootInterpolator(1.1f)).start();
            h.itemView.setOnClickListener(v ->
                v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100)
                        .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()).start());
        }
        @Override public int getItemCount() { return COUNTRIES.length; }
        class VH extends RecyclerView.ViewHolder {
            TextView flag, name, region;
            VH(View v) { super(v); flag=v.findViewById(R.id.country_flag); name=v.findViewById(R.id.country_name); region=v.findViewById(R.id.country_region); }
        }
    }
}
