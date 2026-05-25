package com.example.travelbro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class FlightResultsActivity extends AppCompatActivity {

    private LinearLayout llResults;
    private ProgressBar progressBar;
    private TextView tvNoResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_results);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        llResults   = findViewById(R.id.ll_results);
        progressBar = findViewById(R.id.progress_bar);
        tvNoResults = findViewById(R.id.tv_no_results);

        List<DuffelOffer> offers = FlightSearchHolder.offers;
        progressBar.setVisibility(View.GONE);

        if (offers == null || offers.isEmpty()) {
            tvNoResults.setVisibility(View.VISIBLE);
        } else {
            tvNoResults.setVisibility(View.GONE);
            for (int i = 0; i < offers.size(); i++) {
                addOfferCard(offers.get(i), i);
            }
        }
    }

    private void addOfferCard(DuffelOffer offer, int index) {
        View card = LayoutInflater.from(this)
                .inflate(R.layout.item_flight_card, llResults, false);

        TextView tvAirline = card.findViewById(R.id.tv_airline);
        TextView tvRoute   = card.findViewById(R.id.tv_route);
        TextView tvTime    = card.findViewById(R.id.tv_time);
        TextView tvPrice   = card.findViewById(R.id.tv_price);
        TextView tvDuration = card.findViewById(R.id.tv_duration);
        TextView tvStops   = card.findViewById(R.id.tv_stops);
        Button btnBook     = card.findViewById(R.id.btn_book);

        if (offer.owner != null) tvAirline.setText("✈ " + offer.owner.name);

        if (offer.slices != null && !offer.slices.isEmpty()) {
            DuffelOffer.Slice slice = offer.slices.get(0);
            if (slice.segments != null && !slice.segments.isEmpty()) {
                DuffelOffer.Segment seg = slice.segments.get(0);

                String from = seg.origin != null ? seg.origin.iataCode : "—";
                String to   = seg.destination != null ? seg.destination.iataCode : "—";
                tvRoute.setText(from + "  →  " + to);

                String dep = seg.departingAt != null && seg.departingAt.length() >= 16
                        ? seg.departingAt.substring(11, 16) : "—";
                String arr = seg.arrivingAt != null && seg.arrivingAt.length() >= 16
                        ? seg.arrivingAt.substring(11, 16) : "—";
                tvTime.setText(dep + "  →  " + arr);

                // Пересадки
                int stops = slice.segments.size() - 1;
                tvStops.setText(stops == 0 ? "Прямой рейс" : "Пересадок: " + stops);

                // Длительность
                if (seg.duration != null) {
                    String dur = seg.duration.replace("PT", "").replace("H", "ч ").replace("M", "м");
                    tvDuration.setText("🕐 " + dur);
                } else {
                    tvDuration.setVisibility(View.GONE);
                }
            }
        }

        if (offer.totalAmount != null && offer.totalCurrency != null) {
            try {
                double amount = Double.parseDouble(offer.totalAmount);
                tvPrice.setText(String.format("%.0f %s", amount, offer.totalCurrency));
            } catch (Exception e) {
                tvPrice.setText(offer.totalAmount + " " + offer.totalCurrency);
            }
        }

        btnBook.setOnClickListener(v -> {
            FlightSearchHolder.selectedOfferIndex = index;
            startActivity(new Intent(this, BookingFormActivity.class));
        });

        llResults.addView(card);
    }
}