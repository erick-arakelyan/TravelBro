package com.erikarakelyan.travelbro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.*;

public class SeatSelectionActivity extends AppCompatActivity {

    private static final int ROWS = 30;
    private static final String[] COLS = {"A", "B", "C", "D", "E", "F"};
    private final Set<String> occupiedSeats = new HashSet<>(Arrays.asList(
            "1A","1B","1C","1D","2A","2F","5B","5C","7D","7E",
            "10A","10B","12C","12D","15A","15F","18B","18E","20C","22D"
    ));
    private String selectedSeat = "";
    private TextView tvSelectedSeat;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        tvSelectedSeat = findViewById(R.id.tv_selected_seat);
        btnConfirm     = findViewById(R.id.btn_confirm_seat);
        btnConfirm.setEnabled(false);

        buildSeatMap();

        btnConfirm.setOnClickListener(v -> {
            FlightSearchHolder.selectedSeat = selectedSeat;
            startActivity(new Intent(this, BookingConfirmActivity.class));
        });
    }

    private void buildSeatMap() {
        LinearLayout container = findViewById(R.id.ll_seat_container);

        // Шапка с буквами колонок
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setPadding(0, 0, 0, 8);
        addSeatView(header, "  ", Color.TRANSPARENT, false, false); // пустая ячейка для номера ряда
        for (int c = 0; c < COLS.length; c++) {
            if (c == 3) addSeatView(header, "   ", Color.TRANSPARENT, false, false); // проход
            TextView tv = new TextView(this);
            tv.setText(COLS[c]);
            tv.setTextColor(Color.parseColor("#6B7280"));
            tv.setTextSize(12);
            tv.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(48, 48);
            lp.setMargins(4, 0, 4, 0);
            tv.setLayoutParams(lp);
            header.addView(tv);
        }
        container.addView(header);

        // Ряды
        for (int row = 1; row <= ROWS; row++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setPadding(0, 4, 0, 4);

            // Номер ряда
            TextView tvRow = new TextView(this);
            tvRow.setText(String.valueOf(row));
            tvRow.setTextColor(Color.parseColor("#9CA3AF"));
            tvRow.setTextSize(11);
            tvRow.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams rowLp = new LinearLayout.LayoutParams(36, 48);
            tvRow.setLayoutParams(rowLp);
            rowLayout.addView(tvRow);

            for (int c = 0; c < COLS.length; c++) {
                if (c == 3) addSeatView(rowLayout, "   ", Color.TRANSPARENT, false, false);
                String seatId = row + COLS[c];
                boolean isOccupied = occupiedSeats.contains(seatId);
                boolean isFirst = row <= 3;

                int color = isOccupied ? Color.parseColor("#E5E7EB")
                        : isFirst ? Color.parseColor("#FDE68A")
                        : Color.parseColor("#DBEAFE");

                addSeatView(rowLayout, seatId, color, !isOccupied, isFirst);
            }
            container.addView(rowLayout);
        }
    }

    private void addSeatView(LinearLayout parent, String seatId,
                             int bgColor, boolean clickable, boolean isFirst) {
        CardView cv = new CardView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(48, 40);
        lp.setMargins(4, 2, 4, 2);
        cv.setLayoutParams(lp);
        cv.setRadius(8);
        cv.setCardElevation(clickable ? 2 : 0);
        cv.setCardBackgroundColor(bgColor);

        if (clickable) {
            cv.setOnClickListener(v -> {
                selectedSeat = seatId;
                tvSelectedSeat.setText("Выбрано место: " + seatId +
                        (isFirst ? " (Первый класс)" : ""));
                btnConfirm.setEnabled(true);
                buildSeatMap(); // перерисовать
                highlightSelected();
            });
        }

        cv.setTag(seatId);
        parent.addView(cv);
    }

    private void highlightSelected() {
        LinearLayout container = findViewById(R.id.ll_seat_container);
        for (int i = 0; i < container.getChildCount(); i++) {
            View row = container.getChildAt(i);
            if (row instanceof LinearLayout) {
                LinearLayout rowL = (LinearLayout) row;
                for (int j = 0; j < rowL.getChildCount(); j++) {
                    View v = rowL.getChildAt(j);
                    if (v instanceof CardView) {
                        CardView cv = (CardView) v;
                        Object tag = cv.getTag();
                        if (tag != null && tag.equals(selectedSeat)) {
                            cv.setCardBackgroundColor(Color.parseColor("#3B82F6"));
                        }
                    }
                }
            }
        }
    }
}
