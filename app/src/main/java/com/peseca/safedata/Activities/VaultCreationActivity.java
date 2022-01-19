package com.peseca.safedata.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.peseca.safedata.R;

public class VaultCreationActivity extends AppCompatActivity {

    String main_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_creation);

        Button continue_button = findViewById(R.id.vault_creation_1_button);
        EditText vault_creation_edittext = findViewById(R.id.vault_creation_edittext);

        continue_button.setOnClickListener(view -> {
            main_password = vault_creation_edittext.getText().toString();
            if (main_password.isEmpty()) {
                Toast.makeText(this, R.string.password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            } else {
                Intent goToVaultCreationWarning = new Intent(VaultCreationActivity.this, VaultCreationWarningActivity.class);
                goToVaultCreationWarning.putExtra("main_password", main_password);
                startActivity(goToVaultCreationWarning);
            }
        });
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
            Toast.makeText(VaultCreationActivity.this,R.string.press_again_to_exit,Toast.LENGTH_SHORT).show();
        }
    }
}