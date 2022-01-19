package com.peseca.safedata.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.peseca.safedata.Crypt.AES256;
import com.peseca.safedata.Database.AppDatabase;
import com.peseca.safedata.Database.Password;
import com.peseca.safedata.Database.PasswordDAO;
import com.peseca.safedata.R;

public class VaultActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public String passwordMain;
    public String main_salt;
    public ListView password_listview;
    public Button goToAddPwd;

    AppDatabase db;
    PasswordDAO dao;
    Cursor csr;
    SimpleCursorAdapter sca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault);

        password_listview = findViewById(R.id.listview_password);
        goToAddPwd = findViewById(R.id.goToPasswordAddActivity);

        // Getting Main Password (Important!)
        passwordMain = getIntent().getStringExtra("main_password");

        // Sending Password
        Intent sendPasswordfromVault = new Intent(this, AddPasswordActivity.class);
        sendPasswordfromVault.putExtra("passwordMain", passwordMain);

        //Getting Salt
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        main_salt = sharedPreferences.getString("main_salt", null);

        db = AppDatabase.getInstance(this);
        dao = db.getDao();


        goToAddPwd.setOnClickListener(view -> startActivity(sendPasswordfromVault));
    }

    /* As it says setup or refresh the ListView */
    private void setUpOrRefreshListView() {
        csr = getCursor();
        if (sca == null) {

            sca = new SimpleCursorAdapter(this, R.layout.custom_listview_layout,csr, new String[]{"app_name","hashed_password"}, new int[]{R.id.customListView_AppName,R.id.customListView_HashedPassword}, 0
            );
            password_listview.setAdapter(sca);
            /* BONUS handle Long Click of an Item in the ListView
                in this case just Toast info
            */
            password_listview.setOnItemClickListener((adapterView, view, i, l) -> {
                @SuppressLint("Range") String app_name_private = csr.getString(csr.getColumnIndex("app_name"));
                @SuppressLint("Range") String hash_private = csr.getString(csr.getColumnIndex("hashed_password"));

                Intent goToDetailsActivity = new Intent(VaultActivity.this, DetailsActivity.class);
                goToDetailsActivity.putExtra("hash_private", hash_private);
                goToDetailsActivity.putExtra("app_name_private", app_name_private);
                goToDetailsActivity.putExtra("uid", l);
                goToDetailsActivity.putExtra("passwordMain", passwordMain);
                startActivity(goToDetailsActivity);
            });
        } else {
            sca.swapCursor(csr);
        }

    }

    /* Gets all the passwords using getAllPassswords and creates a MatrixCursor */
    @SuppressLint("NewApi")
    private Cursor getCursor() {
        /* NOTE that as Cursor Adapters required a column named _ID (as per BaseColumns._ID)
            the first column has been renamed accordingly

         */
        MatrixCursor mxcsr = new MatrixCursor(new String[]{BaseColumns._ID, "app_name", "hashed_password"}, 0);
        for (Password p: dao.getAllPasswords()) {
            // Decrypting Password Array
            p.hashed_password = AES256.decrypt(p.hashed_password, passwordMain, main_salt, getApplicationContext());
            mxcsr.addRow(new Object[]{p.uid,p.app_name,p.hashed_password});
        }
        return mxcsr;
    }

    /* If resuming this activity then refresh the ListView */
    @Override
    protected void onResume() {
        super.onResume();
        setUpOrRefreshListView();
    }

    /* if the activity is destroyed then close the Cursor */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        csr.close();
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
            Toast.makeText(VaultActivity.this,R.string.press_again_to_exit,Toast.LENGTH_SHORT).show();
        }
    }
}