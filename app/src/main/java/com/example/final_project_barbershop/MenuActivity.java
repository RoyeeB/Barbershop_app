package com.example.final_project_barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity {

    private AppCompatTextView title_LBL_menu;
    private MaterialButton schedule_BTN_menu;
    private MaterialButton address_BTN_menu;
    private MaterialButton paymentmethod_BTN_menu;
    private MaterialButton pricelist_BTN_menu;
    private MaterialButton signout_BTN_menu;
    private MaterialButton myacc_BTN;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseUser currentUser;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        auth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = auth.getCurrentUser();
        findView();
        clickedPageRegister();
        updateTitle();


    }
    private void clickedPageRegister(){
        schedule_BTN_menu.setOnClickListener(v -> startActivity( new Intent(MenuActivity.this , AppointmentActivity.class)));
       address_BTN_menu.setOnClickListener((v -> startActivity(new Intent(MenuActivity.this , AddressActivity.class))));
       paymentmethod_BTN_menu.setOnClickListener((v -> startActivity(new Intent(MenuActivity.this , PaymentMethodActivity.class))));
       pricelist_BTN_menu.setOnClickListener((v -> startActivity(new Intent(MenuActivity.this , PriceListActivity.class))));
        myacc_BTN.setOnClickListener((v -> startActivity(new Intent(MenuActivity.this , MyAccountActivity.class))));
        signout_BTN_menu.setOnClickListener((v -> startActivity(new Intent(MenuActivity.this , HomePageActivity.class))));

       signout_BTN_menu.setOnClickListener(new View.OnClickListener() {  //יציאה מהמשתמש
           @Override
           public void onClick(View v) {
               FirebaseAuth.getInstance().signOut();
               Toast.makeText(MenuActivity.this , "Logged out!" , Toast.LENGTH_SHORT ).show();
               startActivity(new Intent(MenuActivity.this , HomePageActivity.class));
           }
       });


    }



    private void findView() {
        schedule_BTN_menu = findViewById(R.id.schedule_BTN);
        address_BTN_menu = findViewById(R.id.address_BTN);
        paymentmethod_BTN_menu = findViewById(R.id.paymentmethod_BTN);
        pricelist_BTN_menu = findViewById(R.id.pricelist_BTN);
        signout_BTN_menu = findViewById(R.id.signout_BTN);
        title_LBL_menu = findViewById(R.id.title_LBL_menu);
        myacc_BTN = findViewById(R.id.myacc_BTN);


    }

    public void updateTitle(){ // מעדכן את הכותרת של העמוד לפי השם של היוזר

        String userId = currentUser.getUid();
        DatabaseReference reference = database.getReference("Users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    title_LBL_menu.setText("Hello " + name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
