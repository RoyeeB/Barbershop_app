package com.example.final_project_barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private AppCompatEditText name_EDT_register;
    private AppCompatEditText email_EDT_register;
    private AppCompatEditText password_EDT_register;
    private AppCompatEditText phone_EDT_register;
    private ImageButton back_BTN_register;
    private MaterialButton signup_BTN_register;

    private FirebaseAuth auth;
    FirebaseDatabase database;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        findView();
        clickedPageRegister();
    }


    private void clickedPageRegister(){
        back_BTN_register.setOnClickListener(v -> startActivity( new Intent(RegisterActivity.this , HomePageActivity.class)));
        signup_BTN_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // מעביר את כל מה שהתקבל לסטרינג ושולח לבדיקה ומשם יוצר את המשתמש
                String email = email_EDT_register.getText().toString();
                String password = password_EDT_register.getText().toString();
                String name = name_EDT_register.getText().toString();
                String phone = phone_EDT_register.getText().toString();
                checkAll(name , email , phone , password);
            }
        });
    }


    private void findView() {
        back_BTN_register = findViewById(R.id.back_BTN_register);
        email_EDT_register = findViewById(R.id.email_EDT_register);
        password_EDT_register = findViewById(R.id.password_EDT_register);
        phone_EDT_register = findViewById(R.id.phone_EDT_register);
        name_EDT_register = findViewById(R.id.name_EDT_register);
        signup_BTN_register = findViewById(R.id.signup_BTN_register);

    }
// בודק האם יש רק ספרות
    public boolean isNumeric(String input) {
        return input.matches("\\d+");
    }
// בודק שיש רק אותיות ורווח
    public boolean isAlphabetic(String input) {
        return input.matches("[a-zA-Z ]+");
    }

    // רישום לאפליקציה
    private void registerUser(String email, String password, String phone, String name) {
        auth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    User user = new User (name , email , phone , password );
                    String id=task.getResult().getUser().getUid();
                    database.getReference().child("Users").child(id).setValue(user);
                    Toast.makeText(RegisterActivity.this, "Registring user successful!", Toast.LENGTH_SHORT).show();
                    startActivity( new Intent(RegisterActivity.this , SignInActivity.class));
                } else
                    Toast.makeText(RegisterActivity.this, "Registring user failed!!", Toast.LENGTH_SHORT).show();

            }
        });


    }
// בודק את כל מה שהתקבל על מנת להירשם
    public void checkAll (String name , String email , String phone , String password){
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(name) ){
            Toast.makeText(RegisterActivity.this , "Empty credentials!" , Toast.LENGTH_SHORT).show();
        } else if (password.length()<6){
            Toast.makeText(RegisterActivity.this , "Password too short!" , Toast.LENGTH_SHORT).show();
        }  else if (phone.length() != 10) {
            Toast.makeText(RegisterActivity.this, "Phone number is wrong!", Toast.LENGTH_SHORT).show();
        } else if (isNumeric(phone) == false) {
            Toast.makeText(RegisterActivity.this, "Phone cant include characters!", Toast.LENGTH_SHORT).show();
        }else if (isAlphabetic(name) == false) {
            Toast.makeText(RegisterActivity.this, "Name include only characters!", Toast.LENGTH_SHORT).show();
        }
        else {
            registerUser (email , password , phone , name);
        }
    }



}
