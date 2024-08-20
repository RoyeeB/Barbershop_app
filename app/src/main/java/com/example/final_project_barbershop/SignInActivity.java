package com.example.final_project_barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignInActivity extends AppCompatActivity {
    private ImageButton back_BTN_signin;
    private MaterialButton signin_BTN;
    private AppCompatEditText email_EDT_signin;
    private AppCompatEditText password_EDT_signin;

    private FirebaseAuth auth;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth= FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_in);
        findView();
        clickedPageSignIn();

    }
// כפתורים במסך פתיחה - כניסה של משתמש
    private void clickedPageSignIn(){
        back_BTN_signin.setOnClickListener(v -> startActivity( new Intent(SignInActivity.this , HomePageActivity.class)));
        signin_BTN.setOnClickListener(v -> {
                    String email = email_EDT_signin.getText().toString();
                    String password = password_EDT_signin.getText().toString();
                    logigUser(email , password);
        });

    }
// בודק את הפרטים
    private void logigUser(String email, String password) {
        auth.signInWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(SignInActivity.this , "Login Successful!" , Toast.LENGTH_SHORT ).show();

                if (email.equals("admin@gmail.com"))
                startActivity( new Intent(SignInActivity.this , BarberActivity.class));
                else
                startActivity( new Intent(SignInActivity.this , MenuActivity.class));
            }
        });
    }


    private void findView() {
        back_BTN_signin = findViewById(R.id.back_BTN_signin);
        signin_BTN = findViewById(R.id.signin_BTN);
        email_EDT_signin = findViewById(R.id.email_EDT_signin);
        password_EDT_signin = findViewById(R.id.password_EDT_signin);


    }
}
