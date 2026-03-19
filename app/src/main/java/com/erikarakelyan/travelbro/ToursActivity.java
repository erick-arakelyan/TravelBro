package com.erikarakelyan.travelbro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ToursActivity extends BaseSectionActivity {

    private static final String[][] TOURS = {
            {"🗼", "Paris in a Day", "Paris, France", "8 hrs · 12 people max", "⭐ 4.9", "$89/person"},
            {"🏯", "Kyoto Temples Walk", "Kyoto, Japan", "6 hrs · 8 people max", "⭐ 4.8", "$65/person"},
            {"🐘", "Safari Adventure", "Serengeti, Tanzania", "Full day · 6 people max", "⭐ 5.0", "$299/person"},
            {"🍕", "Rome Food Tour", "Rome, Italy", "4 hrs · 10 people max", "⭐ 4.7", "$55/person"},
            {"🏔️", "Machu Picchu Trek", "Cusco, Peru", "3 days · 12 people max", "⭐ 4.9", "$450/person"},
            {"🌊", "Bali Snorkeling", "Bali, Indonesia", "5 hrs · 15 people max", "⭐ 4.6", "$45/person"},
    };

    @Override
    protected int getLayoutResId() { return R.layout.activity_tours; }

    @Override
    protected void onSectionCreated(Bundle savedInstanceState) {
        RecyclerView rv = findViewById(R.id.tours_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ToursAdapter());
    }

    private class ToursAdapter extends RecyclerView.Adapter<ToursAdapter.VH> {
        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_tour, p, false));
        }
        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            h.icon.setText(TOURS[pos][0]); h.name.setText(TOURS[pos][1]);
            h.location.setText(TOURS[pos][2]); h.duration.setText(TOURS[pos][3]);
            h.rating.setText(TOURS[pos][4]); h.price.setText(TOURS[pos][5]);
            h.bookBtn.setOnClickListener(v -> {
                Toast.makeText(ToursActivity.this, "Booking: " + TOURS[pos][1], Toast.LENGTH_SHORT).show();
                v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100)
                        .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()).start();
            });
            h.itemView.setAlpha(0f); h.itemView.setTranslationY(50f);
            h.itemView.animate().alpha(1f).translationY(0f).setDuration(400)
                    .setStartDelay(pos * 75L).setInterpolator(new android.view.animation.DecelerateInterpolator(2f)).start();
        }
        @Override public int getItemCount() { return TOURS.length; }
        class VH extends RecyclerView.ViewHolder {
            TextView icon, name, location, duration, rating, price, bookBtn;
            VH(View v) { super(v); icon=v.findViewById(R.id.tour_icon); name=v.findViewById(R.id.tour_name); location=v.findViewById(R.id.tour_location); duration=v.findViewById(R.id.tour_duration); rating=v.findViewById(R.id.tour_rating); price=v.findViewById(R.id.tour_price); bookBtn=v.findViewById(R.id.tour_book_btn); }
        }
    }
}
