package com.erikarakelyan.travelbro;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class BookingConfirmActivity extends AppCompatActivity {

    private Button btnBook;
    private ProgressBar progressBar;
    private LinearLayout llSummary;
    private LinearLayout llSuccess;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirm);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        btnBook     = findViewById(R.id.btn_book_now);
        progressBar = findViewById(R.id.progress_bar);
        llSummary   = findViewById(R.id.ll_summary);
        llSuccess   = findViewById(R.id.ll_success);
        tvStatus    = findViewById(R.id.tv_status);

        showSummary();

        btnBook.setOnClickListener(v -> simulateBooking());
    }

    private void showSummary() {
        DuffelOffer offer = FlightSearchHolder.offers.get(FlightSearchHolder.selectedOfferIndex);

        TextView tvPassenger = findViewById(R.id.tv_passenger);
        TextView tvFlight    = findViewById(R.id.tv_flight);
        TextView tvSeat      = findViewById(R.id.tv_seat);
        TextView tvPrice     = findViewById(R.id.tv_total_price);
        TextView tvPassport  = findViewById(R.id.tv_passport);

        tvPassenger.setText(FlightSearchHolder.passengerTitle + " " +
                FlightSearchHolder.passengerFirstName + " " +
                FlightSearchHolder.passengerLastName);

        tvPassport.setText("Паспорт: " + FlightSearchHolder.passengerPassport);

        if (offer.slices != null && !offer.slices.isEmpty()) {
            DuffelOffer.Slice slice = offer.slices.get(0);
            if (slice.segments != null && !slice.segments.isEmpty()) {
                DuffelOffer.Segment seg = slice.segments.get(0);
                String from = seg.origin != null ? seg.origin.iataCode : "—";
                String to   = seg.destination != null ? seg.destination.iataCode : "—";
                String dep  = seg.departingAt != null && seg.departingAt.length() >= 16
                        ? seg.departingAt.substring(0, 16).replace("T", " ") : "—";
                tvFlight.setText(from + " → " + to + "\n" + dep);
                if (offer.owner != null) tvFlight.append("\n" + offer.owner.name);
            }
        }

        tvSeat.setText("Место: " + FlightSearchHolder.selectedSeat);

        if (offer.totalAmount != null) {
            tvPrice.setText("Итого: " + offer.totalAmount + " " + offer.totalCurrency);
        }
    }

    private void simulateBooking() {
        btnBook.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText("Обработка платежа...");
        tvStatus.setTextColor(getColor(android.R.color.darker_gray));

        new Handler().postDelayed(() -> {
            String fakeRef = generateBookingReference();
            FlightSearchHolder.bookingReference = fakeRef;
            showSuccess(fakeRef);
        }, 2500);
    }

    private String generateBookingReference() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private void showSuccess(String bookingRef) {
        progressBar.setVisibility(View.GONE);
        tvStatus.setVisibility(View.GONE);
        llSummary.setVisibility(View.GONE);
        btnBook.setVisibility(View.GONE);
        llSuccess.setVisibility(View.VISIBLE);

        TextView tvRef = findViewById(R.id.tv_booking_ref);
        TextView tvSucPass = findViewById(R.id.tv_success_passenger);
        TextView tvSucFlight = findViewById(R.id.tv_success_flight);

        tvRef.setText(bookingRef);
        tvSucPass.setText(FlightSearchHolder.passengerFirstName + " " + FlightSearchHolder.passengerLastName);

        TextView tvFlightSource = findViewById(R.id.tv_flight);
        tvSucFlight.setText(tvFlightSource.getText());
    }
}
