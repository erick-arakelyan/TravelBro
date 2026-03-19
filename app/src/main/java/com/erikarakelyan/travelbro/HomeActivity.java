package com.erikarakelyan.travelbro;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * HomeActivity — Main screen of the Travel App.
 * Displays hero header, search bar, location selector,
 * service cards grid, and bottom navigation bar.
 */
public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvServiceCards;
    private ServiceCardAdapter cardAdapter;
    private BottomNavigationView bottomNav;
    private TextView tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make status bar transparent to blend with hero
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );

        setContentView(R.layout.activity_home);

        initViews();
        setupServiceCards();
        setupBottomNavigation();
        setupLocationSelector();
    }

    /** Binds all views from layout */
    private void initViews() {
        rvServiceCards = findViewById(R.id.rv_service_cards);
        bottomNav = findViewById(R.id.bottom_navigation);
        tvLocation = findViewById(R.id.tv_location);

        // Search bar click
        View searchBar = findViewById(R.id.search_bar);
        searchBar.setOnClickListener(v ->
                Toast.makeText(this, "Opening Search…", Toast.LENGTH_SHORT).show()
        );

        // Voice search click
        View ivMic = findViewById(R.id.iv_mic);
        ivMic.setOnClickListener(v ->
                Toast.makeText(this, "Voice Search…", Toast.LENGTH_SHORT).show()
        );
    }

    /** Builds the list of service cards and attaches the adapter */
    private void setupServiceCards() {
        List<ServiceCard> cards = buildServiceCards();

        // 2-column grid, last row spans full width for special cards
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // Offline Maps (index 8) and Active Traveler (index 9) in last row
                // Offline Maps takes 1 col, Active Traveler takes 1 col → normal
                return 1;
            }
        });

        rvServiceCards.setLayoutManager(layoutManager);
        cardAdapter = new ServiceCardAdapter(this, cards);
        rvServiceCards.setAdapter(cardAdapter);
        rvServiceCards.setNestedScrollingEnabled(false);
    }

    /** Creates all 10 service card data objects */
    private List<ServiceCard> buildServiceCards() {
        List<ServiceCard> list = new ArrayList<>();

        list.add(new ServiceCard(
                "Book Tickets",
                "Flights & Transport",
                R.drawable.ic_book_tickets,
                "#1A3A5C",
                "#2196F3"
        ));
        list.add(new ServiceCard(
                "Tour Packages",
                "Vacation Deals",
                R.drawable.ic_tour_packages,
                "#2D1B00",
                "#FF6F00"
        ));
        list.add(new ServiceCard(
                "Events & Festivals",
                "What's On Worldwide",
                R.drawable.ic_events,
                "#2D0B3D",
                "#9C27B0"
        ));
        list.add(new ServiceCard(
                "Destination Finder",
                "Personalized Suggestions",
                R.drawable.ic_destination,
                "#0A2A1A",
                "#4CAF50"
        ));
        list.add(new ServiceCard(
                "Travel Journal",
                "My Trip Diary",
                R.drawable.ic_journal,
                "#2D1500",
                "#FF5722"
        ));
        list.add(new ServiceCard(
                "Trip Planner",
                "Plan Your Route",
                R.drawable.ic_trip_planner,
                "#0A1E3A",
                "#03A9F4"
        ));
        list.add(new ServiceCard(
                "AI Guide",
                "Smart Travel Info",
                R.drawable.ic_ai_guide,
                "#001A2D",
                "#00BCD4"
        ));
        list.add(new ServiceCard(
                "Bonus Rewards",
                "Earn & Save Points",
                R.drawable.ic_rewards,
                "#2D1A00",
                "#FFC107"
        ));
        list.add(new ServiceCard(
                "Offline Maps",
                "Use Maps Without Internet",
                R.drawable.ic_offline_maps,
                "#0D1F35",
                "#29B6F6"
        ));
        list.add(new ServiceCard(
                "Active Traveler Status",
                "Collect Bonuses & Benefits",
                R.drawable.ic_active_traveler,
                "#1A2000",
                "#FFD700"
        ));

        return list;
    }

    /** Configures the BottomNavigationView with tab selection */
    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Already on home
                return true;
            } else if (id == R.id.nav_explore) {
                Toast.makeText(this, "Explore", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_rewards) {
                Toast.makeText(this, "Rewards", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_profile) {
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        // Select Home by default
        bottomNav.setSelectedItemId(R.id.nav_home);
    }

    /** Location chip click handler */
    private void setupLocationSelector() {
        View locationChip = findViewById(R.id.location_chip);
        locationChip.setOnClickListener(v ->
                Toast.makeText(this, "Change Location", Toast.LENGTH_SHORT).show()
        );
    }
}
