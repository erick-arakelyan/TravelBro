package com.erikarakelyan.travelbro;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Locale;

public class BookingFormActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPhone, etPassport;
    private TextView tvBirthDate, tvFlightInfo, tvPrice;
    private RadioGroup rgTitle;
    private Button btnNext;
    private String birthDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_form);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        etFirstName = findViewById(R.id.et_first_name);
        etLastName  = findViewById(R.id.et_last_name);
        etEmail     = findViewById(R.id.et_email);
        etPhone     = findViewById(R.id.et_phone);
        etPassport  = findViewById(R.id.et_passport);
        tvBirthDate = findViewById(R.id.tv_birth_date);
        tvFlightInfo = findViewById(R.id.tv_flight_info);
        tvPrice     = findViewById(R.id.tv_price);
        rgTitle     = findViewById(R.id.rg_title);
        btnNext     = findViewById(R.id.btn_next);

        // Показать инфо о рейсе
        DuffelOffer offer = FlightSearchHolder.offers.get(FlightSearchHolder.selectedOfferIndex);
        if (offer.slices != null && !offer.slices.isEmpty()) {
            DuffelOffer.Slice slice = offer.slices.get(0);
            if (slice.segments != null && !slice.segments.isEmpty()) {
                DuffelOffer.Segment seg = slice.segments.get(0);
                String from = seg.origin != null ? seg.origin.iataCode : "—";
                String to   = seg.destination != null ? seg.destination.iataCode : "—";
                String dep  = seg.departingAt != null && seg.departingAt.length() >= 16
                        ? seg.departingAt.substring(0, 16).replace("T", " ") : "—";
                tvFlightInfo.setText(from + " → " + to + "  |  " + dep);
            }
        }
        if (offer.totalAmount != null) {
            tvPrice.setText("Итого: " + offer.totalAmount + " " + offer.totalCurrency);
        }

        tvBirthDate.setOnClickListener(v -> showBirthDatePicker());

        btnNext.setOnClickListener(v -> {
            if (!validate()) return;

            int titleId = rgTitle.getCheckedRadioButtonId();
            RadioButton rb = findViewById(titleId);
            FlightSearchHolder.passengerTitle     = rb != null ? rb.getText().toString() : "Mr";
            FlightSearchHolder.passengerFirstName = etFirstName.getText().toString().trim();
            FlightSearchHolder.passengerLastName  = etLastName.getText().toString().trim();
            FlightSearchHolder.passengerEmail     = etEmail.getText().toString().trim();
            FlightSearchHolder.passengerPhone     = etPhone.getText().toString().trim();
            FlightSearchHolder.passengerPassport  = etPassport.getText().toString().trim();
            FlightSearchHolder.passengerBirthDate = birthDate;

            startActivity(new Intent(this, SeatSelectionActivity.class));
        });
    }

    private void showBirthDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -25);
        new DatePickerDialog(this, (view, year, month, day) -> {
            birthDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day);
            String disp = String.format(Locale.US, "%02d.%02d.%04d", day, month + 1, year);
            tvBirthDate.setText("🎂 " + disp);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean validate() {
        if (etFirstName.getText().toString().trim().isEmpty()) {
            etFirstName.setError("Введите имя"); return false;
        }
        if (etLastName.getText().toString().trim().isEmpty()) {
            etLastName.setError("Введите фамилию"); return false;
        }
        if (etEmail.getText().toString().trim().isEmpty()) {
            etEmail.setError("Введите email"); return false;
        }
        if (etPhone.getText().toString().trim().isEmpty()) {
            etPhone.setError("Введите телефон"); return false;
        }
        if (birthDate.isEmpty()) {
            Toast.makeText(this, "Выберите дату рождения", Toast.LENGTH_SHORT).show(); return false;
        }
        if (etPassport.getText().toString().trim().isEmpty()) {
            etPassport.setError("Введите номер паспорта"); return false;
        }
        return true;
    }
}