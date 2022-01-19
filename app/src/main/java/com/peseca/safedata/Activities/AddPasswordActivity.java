package com.peseca.safedata.Activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.peseca.safedata.Crypt.AES256;
import com.peseca.safedata.Database.AppDatabase;
import com.peseca.safedata.Database.Password;
import com.peseca.safedata.R;

public class AddPasswordActivity extends AppCompatActivity {

    EditText input_password, input_app_name;
    Button add_password;
    String main_salt;
    String passwordMain;

    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        input_password = findViewById(R.id.input_password);
        input_app_name = findViewById(R.id.input_app_name);
        add_password = findViewById(R.id.add_password);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        main_salt = sharedPreferences.getString("main_salt", null);

        Intent sendPasswordfromVault = getIntent();
        passwordMain = sendPasswordfromVault.getStringExtra("passwordMain");


        add_password.setOnClickListener(view -> {
            String app_name = input_app_name.getText().toString();
            String not_hashed_password = input_password.getText().toString();
            AppDatabase db  = com.peseca.safedata.Database.AppDatabase.getInstance(this.getApplicationContext());
            String hashed_pwd = AES256.encrypt(not_hashed_password, passwordMain, main_salt, getApplicationContext());

            Password password = new Password();
            password.app_name = app_name;
            password.hashed_password = hashed_pwd;
            db.getDao().insertUser(password);

            Intent goToVault = new Intent(this, VaultActivity.class);
            goToVault.putExtra("main_password", passwordMain);
            startActivity(goToVault);

            finish();
        });
    }

    @Override
    public void onBackPressed() {
        Intent goToVault = new Intent(this, VaultActivity.class);
        goToVault.putExtra("main_password", passwordMain);
        startActivity(goToVault);
    }
}