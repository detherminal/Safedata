package com.peseca.safedata.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.peseca.safedata.Crypt.AES256;
import com.peseca.safedata.Database.AppDatabase;
import com.peseca.safedata.Database.Password;
import com.peseca.safedata.R;

public class EditActivity extends AppCompatActivity {

    String app_name_str, hash_str, passwordMain;
    Long uid;
    EditText app_name_edit, hash_edit;
    Button edit_save_button;

    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        app_name_edit = findViewById(R.id.app_name_edit);
        hash_edit = findViewById(R.id.hash_edit);
        edit_save_button = findViewById(R.id.edit_save_button);

        Intent getIntent = getIntent();
        app_name_str = getIntent.getStringExtra("app_name_private");
        hash_str = getIntent.getStringExtra("hash_private");
        uid = getIntent.getLongExtra("uid", 1000000);
        passwordMain = getIntent.getStringExtra("main_password");

        app_name_edit.setText(app_name_str);
        hash_edit.setText(hash_str);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String main_salt = sharedPreferences.getString("main_salt", null);

        AppDatabase db  = com.peseca.safedata.Database.AppDatabase.getInstance(this.getApplicationContext());

        edit_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String final_app_name_str = app_name_edit.getText().toString();
                String final_hash_str = hash_edit.getText().toString();
                String hashed_pwd = AES256.encrypt(final_hash_str, passwordMain, main_salt, getApplicationContext());

                Password password = new Password();
                password.app_name = final_app_name_str;
                password.hashed_password = hashed_pwd;

                db.getDao().insertUser(password);

                db.getDao().deleteByUserId(uid);

                Intent goToVault = new Intent(EditActivity.this, VaultActivity.class);
                goToVault.putExtra("main_password", passwordMain);
                startActivity(goToVault);

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent goToVault = new Intent(EditActivity.this, VaultActivity.class);
        goToVault.putExtra("main_password", passwordMain);
        startActivity(goToVault);
    }
}