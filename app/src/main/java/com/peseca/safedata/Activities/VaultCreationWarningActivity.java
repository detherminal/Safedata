package com.peseca.safedata.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.peseca.safedata.Crypt.BCrypt;
import com.peseca.safedata.R;

import java.util.Random;

public class VaultCreationWarningActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_creation_warning);

        Button warning_continue_button = findViewById(R.id.warning_continue_button);
        Intent sendPasswordfromVaultCreationActivity = getIntent();
        String main_password = sendPasswordfromVaultCreationActivity.getStringExtra("main_password");

        warning_continue_button.setOnClickListener(view -> {
            String hashed_password = BCrypt.hashpw(main_password, BCrypt.gensalt());
            String main_salt = getRandomString(8);

            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("main_salt", main_salt);
            editor.putString("hashedKey", hashed_password);
            editor.putBoolean("isVaultCreated", true);
            editor.apply();

            Intent goToVault = new Intent(this, VaultActivity.class);
            goToVault.putExtra("main_password", main_password);
            startActivity(goToVault);
        });
    }

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        Intent goToLastActivity = new Intent(this, VaultCreationActivity.class);
        startActivity(goToLastActivity);
    }
}

