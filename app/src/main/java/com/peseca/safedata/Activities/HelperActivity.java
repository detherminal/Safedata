package com.peseca.safedata.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.peseca.safedata.R;

public class HelperActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                Toast.makeText(this, R.string.this_app_doesnt_support_large_screens, Toast.LENGTH_SHORT).show();
                finish();
                System.exit(0);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                break;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        boolean isVaultCreated = sharedPreferences.getBoolean("isVaultCreated", false);

        Handler delayer = new Handler();
        delayer.postDelayed(new Runnable() {
            public void run() {
                if (isVaultCreated) {
                    Intent goToMainActivity = new Intent(HelperActivity.this, MainActivity.class);
                    startActivity(goToMainActivity);
                } else {
                    Intent goToVaultCreation = new Intent(HelperActivity.this, VaultCreationActivity.class);
                    startActivity(goToVaultCreation);
                }
            }
        }, 1000);
    }
}