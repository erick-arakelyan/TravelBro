package com.example.travelbro;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.*;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.VH> {

    private List<ForecastDay> data;

    public ForecastAdapter(List<ForecastDay> data) { this.data = data; }

    public void updateData(List<ForecastDay> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast_day, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        ForecastDay d = data.get(pos);
        h.tvDate.setText(d.date);
        h.tvEmoji.setText(d.emoji);
        h.tvDesc.setText(d.description);
        h.tvTemp.setText(d.tempMax + "° / " + d.tempMin + "°");
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvDate, tvEmoji, tvDesc, tvTemp;
        VH(View v) {
            super(v);
            tvDate  = v.findViewById(R.id.tv_forecast_date);
            tvEmoji = v.findViewById(R.id.tv_forecast_emoji);
            tvDesc  = v.findViewById(R.id.tv_forecast_desc);
            tvTemp  = v.findViewById(R.id.tv_forecast_temp);
        }
    }
}