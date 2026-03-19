package com.erikarakelyan.travelbro;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity — entry hub after login.
 * Immediately redirects to HomeActivity (the main UI screen).
 * Keeping this class allows LoginActivity to always navigate here
 * without coupling it directly to HomeActivity.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Delegate straight to HomeActivity
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
