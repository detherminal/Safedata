package com.peseca.safedata.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.peseca.safedata.Crypt.BCrypt;
import com.peseca.safedata.R;

public class MainActivity extends AppCompatActivity {

    public EditText mainkey_input;
    public static final String SHARED_PREFS = "sharedPrefs";
    public boolean isVaultCreated;
    public Button main_button;
    public String main_password;
    public String hashed_key_real;
    public String main_salt;
    public ListView listview_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainkey_input = findViewById(R.id.mainkey_input_edittext);
        main_button = findViewById(R.id.main_page_button);
        listview_password = findViewById(R.id.listview_password);

        main_button.setOnClickListener(view -> {
            main_password = mainkey_input.getText().toString();
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            hashed_key_real = sharedPreferences.getString("hashedKey", null);


            boolean isSame = BCrypt.checkpw(main_password, hashed_key_real);

            if(main_password.isEmpty()) {
                Toast.makeText(this, R.string.password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            } else {
                if (isSame) {
                    goToVault();
                } else {
                    Toast.makeText(this, R.string.your_key_is_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mainkey_input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId != 0 || event.getAction() == KeyEvent.ACTION_DOWN) {
                main_password = mainkey_input.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                hashed_key_real = sharedPreferences.getString("hashedKey", "null");

                boolean isSame = BCrypt.checkpw(main_password, hashed_key_real);

                if(main_password.isEmpty()) {
                    Toast.makeText(this, R.string.password_cannot_be_empty, Toast.LENGTH_SHORT).show();
                } else {
                    if (isSame) {
                        goToVault();
                    } else {
                        Toast.makeText(this, R.string.your_key_is_wrong, Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            } else {
                return false;
            }
        });
    }

    public void savePreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().putBoolean("isVaultCreated", isVaultCreated);
    }

    public void getPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isVaultCreated = preferences.getBoolean("isVaultCreated", false);
    }

    public void goToVault() {
        Intent goToVault_intent = new Intent(this, VaultActivity.class);
        goToVault_intent.putExtra("main_password", main_password);
        startActivity(goToVault_intent);
    }

    int x = 0;

    @Override
    public void onBackPressed() {
        x = x + 1;
        if (x == 2) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            Toast.makeText(MainActivity.this, R.string.press_again_to_exit, Toast.LENGTH_SHORT).show();
        }
    }
}