package com.example.travelbro;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EventItem> list = new ArrayList<>();
    private final Context context;

    public EventAdapter(Context context) {
        this.context = context;
    }

    public void setEvents(List<EventItem> l) {
        this.list = (l != null) ? l : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        EventItem item = list.get(pos);
        if (item == null) return;

        h.tvTitle.setText(item.title != null ? item.title : "");
        h.tvInfo.setText((item.date != null ? item.date : "") + " | " +
                (item.city != null ? item.city : "") + ", " +
                (item.location != null ? item.location : ""));
        h.tvEmoji.setText(item.emoji != null ? item.emoji : "🎫");

        h.itemView.setOnClickListener(v -> {
            String query = Uri.encode("tickets " + item.title + " " + item.city);
            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/search?q=" + query));
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvInfo, tvEmoji;

        public ViewHolder(@NonNull View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tv_event_title);
            tvInfo = v.findViewById(R.id.tv_event_info);
            tvEmoji = v.findViewById(R.id.tv_event_emoji);
        }
    }
}