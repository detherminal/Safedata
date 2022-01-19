package com.peseca.safedata.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.peseca.safedata.Database.AppDatabase;
import com.peseca.safedata.R;

public class DetailsActivity extends AppCompatActivity {

    TextView app_name, hash;
    Button delete_button, edit_button;
    String passwordMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        app_name = findViewById(R.id.app_name_details);
        hash = findViewById(R.id.hash_details);
        delete_button = findViewById(R.id.delete_button);
        edit_button = findViewById(R.id.edit_button);

        Intent getIntent = getIntent();
        String hash_priv = getIntent.getStringExtra("hash_private");
        String app_name_str = getIntent.getStringExtra("app_name_private");
        Long uid = getIntent.getLongExtra("uid", 1000000);
        hash.setText(hash_priv);
        app_name.setText(app_name_str);
        passwordMain = getIntent.getStringExtra("passwordMain");

        AppDatabase db  = com.peseca.safedata.Database.AppDatabase.getInstance(this.getApplicationContext());

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToEdit = new Intent(DetailsActivity.this, EditActivity.class);
                goToEdit.putExtra("main_password", passwordMain);
                goToEdit.putExtra("hash_private", hash_priv);
                goToEdit.putExtra("app_name_private", app_name_str);
                goToEdit.putExtra("uid", uid);
                startActivity(goToEdit);
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.getDao().deleteByUserId(uid);
                Toast.makeText(DetailsActivity.this,
                        R.string.deleted_password,
                        Toast.LENGTH_SHORT).show();
                Intent goToVault = new Intent(DetailsActivity.this, VaultActivity.class);
                goToVault.putExtra("main_password", passwordMain);
                startActivity(goToVault);

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent goToVault = new Intent(DetailsActivity.this, VaultActivity.class);
        goToVault.putExtra("main_password", passwordMain);
        startActivity(goToVault);
    }
}