package com.erikarakelyan.travelbro;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MapsActivity extends BaseSectionActivity {

    @Override
    protected int getLayoutResId() { return R.layout.activity_maps; }

    @Override
    protected void onSectionCreated(Bundle savedInstanceState) {
        int[] mapTypeIds = {R.id.btn_street, R.id.btn_satellite, R.id.btn_terrain, R.id.btn_transit};
        String[] mapTypes = {"Street", "Satellite", "Terrain", "Transit"};
        for (int i = 0; i < mapTypeIds.length; i++) {
            final String type = mapTypes[i];
            TextView btn = findViewById(mapTypeIds[i]);
            if (btn != null) btn.setOnClickListener(v ->
                    Toast.makeText(this, type + " view selected", Toast.LENGTH_SHORT).show());
        }
        int[] chipIds = {R.id.chip_hotels, R.id.chip_restaurants, R.id.chip_attractions, R.id.chip_airports};
        String[] chipLabels = {"🏨 Hotels", "🍽️ Restaurants", "🗺️ Attractions", "✈️ Airports"};
        for (int i = 0; i < chipIds.length; i++) {
            final String label = chipLabels[i];
            TextView chip = findViewById(chipIds[i]);
            if (chip != null) chip.setOnClickListener(v ->
                    Toast.makeText(this, "Finding " + label + " nearby...", Toast.LENGTH_SHORT).show());
        }
    }
}
