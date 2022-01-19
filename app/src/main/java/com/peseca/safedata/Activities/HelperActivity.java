package com.peseca.safedata.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.peseca.safedata.NewVersionActivity;
import com.peseca.safedata.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HelperActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public boolean isNewerVersionAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        isNewerVersionAvailable = sharedPreferences.getBoolean("isNewerVersionAvailable", false);
        boolean isVaultCreated = sharedPreferences.getBoolean("isVaultCreated", false);

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

        Handler delayer = new Handler();
        delayer.postDelayed(new Runnable() {
            public void run() {
                if (isNewerVersionAvailable) {
                    goToNewVersionActivity();
                } else {
                    isThereANewVersion();
                    if (isNewerVersionAvailable) {
                        goToNewVersionActivity();
                    } else {
                        if (isVaultCreated) {
                            Intent goToMainActivity = new Intent(HelperActivity.this, MainActivity.class);
                            startActivity(goToMainActivity);
                        } else {
                            Intent goToVaultCreation = new Intent(HelperActivity.this, VaultCreationActivity.class);
                            startActivity(goToVaultCreation);
                        }
                    }
                }
            }
        }, 1000);
    }

    public void isThereANewVersion() {
        String currentMonth = (new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()));
        String currentYear = (new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        int currentYearAndMonth = Integer.parseInt(currentYear+currentMonth);
        if (currentYearAndMonth > 202208){
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isNewerVersionAvailable", true);
            editor.apply();
            isNewerVersionAvailable = true;
        } else {
            isNewerVersionAvailable = false;
        }
    }

    public void goToNewVersionActivity() {
        Intent goToNewVersionActivity = new Intent(this, NewVersionActivity.class);
        startActivity(goToNewVersionActivity);
    }
}