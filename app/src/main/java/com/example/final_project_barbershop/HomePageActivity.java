package com.example.final_project_barbershop;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class HomePageActivity extends AppCompatActivity {

    private MaterialButton register_BTN_menu;
    private MaterialButton signin_BTN_menu;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        findView();
        clickedPageMenu();
    }

    private void clickedPageMenu(){
        signin_BTN_menu.setOnClickListener(v -> startActivity( new Intent(HomePageActivity.this , SignInActivity.class)));
        register_BTN_menu.setOnClickListener((v -> startActivity(new Intent(HomePageActivity.this , RegisterActivity.class))));
    }


    private void findView() {
        signin_BTN_menu = findViewById(R.id.signin_BTN_menu);
        register_BTN_menu = findViewById(R.id.register_BTN_menu);

    }

}
