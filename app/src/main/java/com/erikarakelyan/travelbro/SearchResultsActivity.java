package com.erikarakelyan.travelbro;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchResultsActivity extends BaseActivity {

    private DestinationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rv_search_results);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DestinationsAdapter(this);
        rv.setAdapter(adapter);

        EditText etSearch = findViewById(R.id.et_search_results);

        String initialQuery = getIntent().getStringExtra("query");
        if (initialQuery != null && !initialQuery.isEmpty()) {
            etSearch.setText(initialQuery);
            etSearch.setSelection(initialQuery.length());
            adapter.searchDestinations(initialQuery);
        } else {
            adapter.loadPopularDestinations();
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    adapter.loadPopularDestinations();
                } else if (s.length() >= 2) {
                    adapter.searchDestinations(s.toString());
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }
}
