package com.erikarakelyan.travelbro;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends BaseActivity {

    private static final int PICK_IMAGE = 101;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    // Header views
    private ImageView ivAvatar;
    private ImageView btnChangePhoto;
    private TextView tvUserName;
    private TextView tvEmail;
    private TextView tvTripsCount;

    // Stat cards
    private TextView tvStatTrips;
    private TextView tvStatCountries;
    private TextView tvStatRating;

    // Form fields
    private TextInputEditText etDisplayName;
    private TextInputEditText etBio;
    private TextInputEditText etPhone;
    private TextView tvEmailField;

    // Buttons
    private Button btnSave;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth    = FirebaseAuth.getInstance();
        db      = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        bindViews();
        populateFromAuth(user);
        loadFirestoreData(user);
        setupClickListeners(user);
        animateEntrance();
    }

    // ─────────────────────────────────────
    //  View binding
    // ─────────────────────────────────────
    private void bindViews() {
        ivAvatar       = findViewById(R.id.iv_avatar);
        btnChangePhoto = findViewById(R.id.btn_change_photo);
        tvUserName     = findViewById(R.id.tv_user_name);
        tvEmail        = findViewById(R.id.tv_email);
        tvTripsCount   = findViewById(R.id.tv_trips_count);

        tvStatTrips     = findViewById(R.id.tv_stat_trips);
        tvStatCountries = findViewById(R.id.tv_stat_countries);
        tvStatRating    = findViewById(R.id.tv_stat_rating);

        etDisplayName = findViewById(R.id.et_display_name);
        etBio         = findViewById(R.id.et_bio);
        etPhone       = findViewById(R.id.et_phone);
        tvEmailField  = findViewById(R.id.tv_email_field);

        btnSave   = findViewById(R.id.btn_save_profile);
        btnLogout = findViewById(R.id.btn_logout);
    }

    // ─────────────────────────────────────
    //  Populate from FirebaseAuth
    // ─────────────────────────────────────
    private void populateFromAuth(FirebaseUser user) {
        String email       = user.getEmail() != null ? user.getEmail() : "";
        String displayName = user.getDisplayName() != null
                ? user.getDisplayName() : "Путешественник";

        tvEmail.setText(email);
        tvUserName.setText(displayName);
        tvEmailField.setText(email);
        etDisplayName.setText(displayName);

        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .placeholder(R.drawable.ic_profile)
                    .transform(new CircleCrop())
                    .into(ivAvatar);
        }
    }

    // ─────────────────────────────────────
    //  Load from Firestore
    // ─────────────────────────────────────
    private void loadFirestoreData(FirebaseUser user) {
        DocumentReference ref = db.collection("users").document(user.getUid());
        ref.get().addOnSuccessListener(doc -> {
            if (!doc.exists()) return;

            String bio   = doc.getString("bio");
            String phone = doc.getString("phone");
            if (bio   != null) etBio.setText(bio);
            if (phone != null) etPhone.setText(phone);

            Long   tripsCount = doc.getLong("tripsCount");
            Long   countries  = doc.getLong("countries");
            Double rating     = doc.getDouble("rating");

            long trips = tripsCount != null ? tripsCount : 0;
            tvTripsCount.setText(trips + " поездок");
            tvStatTrips.setText(String.valueOf(trips));
            tvStatCountries.setText(countries != null ? String.valueOf(countries) : "0");
            tvStatRating.setText(rating != null ? String.format("%.1f", rating) : "—");

            // Animate counting up
            if (tripsCount != null) animateCount(tvStatTrips, tripsCount);
            if (countries  != null) animateCount(tvStatCountries, countries);
        });
    }

    // ─────────────────────────────────────
    //  Click listeners
    // ─────────────────────────────────────
    private void setupClickListeners(FirebaseUser user) {
        View btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        if (btnChangePhoto != null) {
            btnChangePhoto.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            });
        }

        DocumentReference ref = db.collection("users").document(user.getUid());
        btnSave.setOnClickListener(v -> {
            animateButtonPress(btnSave);
            saveProfile(user, ref);
        });

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    // ─────────────────────────────────────
    //  Save profile
    // ─────────────────────────────────────
    private void saveProfile(FirebaseUser user, DocumentReference ref) {
        String name  = getText(etDisplayName);
        String bio   = getText(etBio);
        String phone = getText(etPhone);

        UserProfileChangeRequest req = new UserProfileChangeRequest.Builder()
                .setDisplayName(name.isEmpty() ? user.getDisplayName() : name)
                .build();

        user.updateProfile(req).addOnCompleteListener(task -> {
            Map<String, Object> data = new HashMap<>();
            data.put("displayName", name);
            data.put("bio", bio);
            data.put("phone", phone);
            data.put("email", user.getEmail());

            ref.set(data, com.google.firebase.firestore.SetOptions.merge())
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Профиль сохранён ✓", Toast.LENGTH_SHORT).show();
                        if (!name.isEmpty()) tvUserName.setText(name);
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Ошибка: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show());
        });
    }

    // ─────────────────────────────────────
    //  Photo upload
    // ─────────────────────────────────────
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != PICK_IMAGE || resultCode != Activity.RESULT_OK || data == null) return;

        Uri imageUri = data.getData();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null || imageUri == null) return;

        StorageReference storageRef = storage.getReference()
                .child("avatars/" + user.getUid() + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(snap ->
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            UserProfileChangeRequest req = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(uri).build();
                            user.updateProfile(req);

                            db.collection("users").document(user.getUid())
                                    .update("photoUrl", uri.toString());

                            Glide.with(this)
                                    .load(uri)
                                    .transform(new CircleCrop())
                                    .into(ivAvatar);

                            Toast.makeText(this, "Фото обновлено ✓", Toast.LENGTH_SHORT).show();
                        }))
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Ошибка загрузки: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }

    // ─────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────

    private String getText(TextInputEditText et) {
        if (et == null || et.getText() == null) return "";
        return et.getText().toString().trim();
    }

    /** Staggered fade-in for stat cards */
    private void animateEntrance() {
        int[] ids = { R.id.card_trips, R.id.card_countries, R.id.card_rating };
        for (int i = 0; i < ids.length; i++) {
            View v = findViewById(ids[i]);
            if (v == null) continue;
            v.setAlpha(0f);
            v.setTranslationY(24f);
            v.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setStartDelay(120L * i + 200L)
                    .setDuration(350)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }
    }

    /**
     * Counts up from 0 to target and sets it on the TextView.
     * Uses ValueAnimator<Integer> — no ObjectAnimator.ofInt ambiguity.
     */
    private void animateCount(TextView tv, long target) {
        ValueAnimator anim = ValueAnimator.ofInt(0, (int) target);
        anim.setDuration(700);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.addUpdateListener(a -> tv.setText(String.valueOf((int) a.getAnimatedValue())));
        anim.start();
    }

    /** Subtle press scale on Save button */
    private void animateButtonPress(View v) {
        v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(80)
                .withEndAction(() ->
                        v.animate().scaleX(1f).scaleY(1f).setDuration(120).start())
                .start();
    }
}