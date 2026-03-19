package com.erikarakelyan.travelbro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TicketsActivity extends BaseSectionActivity {

    private static final String[][] TICKETS = {
            {"✈️", "NYC → Tokyo", "Mar 15, 2025 · 14h 30m", "Business Class", "$1,240"},
            {"🚂", "Paris → Barcelona", "Mar 22, 2025 · 6h 25m", "1st Class", "$89"},
            {"🎫", "Louvre Museum", "Mar 23, 2025 · 10:00 AM", "Skip-the-Line", "$22"},
            {"🏟️", "Flamenco Show", "Mar 24, 2025 · 8:00 PM", "Front Row", "$65"},
            {"🚢", "Mediterranean Cruise", "Apr 1–8, 2025", "Cabin A42", "$1,899"},
    };

    @Override
    protected int getLayoutResId() { return R.layout.activity_tickets; }

    @Override
    protected void onSectionCreated(Bundle savedInstanceState) {
        RecyclerView rv = findViewById(R.id.tickets_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new TicketsAdapter());
    }

    private class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.VH> {
        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_ticket, p, false));
        }
        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            h.icon.setText(TICKETS[pos][0]); h.route.setText(TICKETS[pos][1]);
            h.datetime.setText(TICKETS[pos][2]); h.classType.setText(TICKETS[pos][3]); h.price.setText(TICKETS[pos][4]);
            h.itemView.setAlpha(0f); h.itemView.setScaleX(0.9f);
            h.itemView.animate().alpha(1f).scaleX(1f).setDuration(400).setStartDelay(pos * 90L).start();
        }
        @Override public int getItemCount() { return TICKETS.length; }
        class VH extends RecyclerView.ViewHolder {
            TextView icon, route, datetime, classType, price;
            VH(View v) { super(v); icon=v.findViewById(R.id.ticket_icon); route=v.findViewById(R.id.ticket_route); datetime=v.findViewById(R.id.ticket_datetime); classType=v.findViewById(R.id.ticket_class); price=v.findViewById(R.id.ticket_price); }
        }
    }
}
