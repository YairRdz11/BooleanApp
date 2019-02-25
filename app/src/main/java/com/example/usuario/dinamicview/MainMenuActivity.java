package com.example.usuario.dinamicview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void goStart(View view){
        Intent iStart = new Intent(this, MainActivity.class);
        startActivity(iStart);
    }

    public void goHelp(View view){
        Intent iHelp = new Intent(this, AyudaActivity.class);
        startActivity(iHelp);
    }

    public void goCredits(View view){
        Intent iCredits = new Intent(this, CreditsActivity.class);
        startActivity(iCredits);
    }
}
