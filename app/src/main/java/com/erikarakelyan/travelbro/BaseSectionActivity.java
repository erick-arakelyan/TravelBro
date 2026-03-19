package com.erikarakelyan.travelbro;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public abstract class BaseSectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

        String sectionTitle = getIntent().getStringExtra("section_title");
        String sectionDescription = getIntent().getStringExtra("section_description");

        TextView titleView = findViewById(R.id.section_title);
        TextView descView = findViewById(R.id.section_description);
        if (titleView != null && sectionTitle != null) titleView.setText(sectionTitle);
        if (descView != null && sectionDescription != null) descView.setText(sectionDescription);

        View contentRoot = findViewById(R.id.content_root);
        if (contentRoot != null) {
            contentRoot.setAlpha(0f);
            contentRoot.setTranslationY(40f);
            contentRoot.animate()
                    .alpha(1f).translationY(0f)
                    .setDuration(500).setStartDelay(100)
                    .setInterpolator(new android.view.animation.DecelerateInterpolator(2f))
                    .start();
        }

        onSectionCreated(savedInstanceState);
    }

    protected abstract int getLayoutResId();

    protected void onSectionCreated(Bundle savedInstanceState) {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_out_down);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_down);
    }
}
