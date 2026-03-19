package com.erikarakelyan.travelbro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EventsActivity extends BaseSectionActivity {

    private static final String[][] EVENTS = {
            {"Cherry Blossom Festival", "Tokyo, Japan", "Mar 25 – Apr 10", "🌸"},
            {"Carnival of Venice", "Venice, Italy", "Feb 8 – Feb 18", "🎭"},
            {"Oktoberfest", "Munich, Germany", "Sep 21 – Oct 6", "🍺"},
            {"Diwali Celebrations", "Jaipur, India", "Nov 1 – Nov 5", "🪔"},
            {"Mardi Gras", "New Orleans, USA", "Feb 13 – Mar 5", "🎉"},
            {"Songkran Water Festival", "Chiang Mai, Thailand", "Apr 13 – Apr 15", "💦"},
            {"La Tomatina", "Buñol, Spain", "Aug 28", "🍅"},
            {"Rio Carnival", "Rio de Janeiro, Brazil", "Feb 28 – Mar 5", "💃"},
    };

    @Override
    protected int getLayoutResId() { return R.layout.activity_events; }

    @Override
    protected void onSectionCreated(Bundle savedInstanceState) {
        RecyclerView rv = findViewById(R.id.events_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new EventsAdapter());
    }

    private class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.VH> {
        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_event, p, false));
        }
        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            h.emoji.setText(EVENTS[pos][3]); h.name.setText(EVENTS[pos][0]);
            h.location.setText(EVENTS[pos][1]); h.date.setText(EVENTS[pos][2]);
            h.itemView.setAlpha(0f); h.itemView.setTranslationX(-60f);
            h.itemView.animate().alpha(1f).translationX(0f).setDuration(400)
                    .setStartDelay(pos * 70L)
                    .setInterpolator(new android.view.animation.DecelerateInterpolator(2f)).start();
        }
        @Override public int getItemCount() { return EVENTS.length; }
        class VH extends RecyclerView.ViewHolder {
            TextView emoji, name, location, date;
            VH(View v) { super(v); emoji=v.findViewById(R.id.event_emoji); name=v.findViewById(R.id.event_name); location=v.findViewById(R.id.event_location); date=v.findViewById(R.id.event_date); }
        }
    }
}
