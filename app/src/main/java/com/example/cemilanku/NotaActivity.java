package com.example.cemilanku;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class NotaActivity extends AppCompatActivity {
    String username;
    SharedPreferences sharedpreferences;

    public static final String TAG_USERNAME = "username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);
    }

    public void beli(View view) {
        Intent sendToLoginandRegistration = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(sendToLoginandRegistration);
    }
}
