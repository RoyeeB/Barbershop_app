package com.example.final_project_barbershop;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class ToPayActivity extends AppCompatActivity {

    private AppCompatImageButton back_BTN_topay;
    private AppCompatTextView price_LBL_topay;
    private Button pay_BTN_now;
    private FirebaseAuth auth;
    static FirebaseUser currentUser;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topay);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        findView();
        clickedPageToPay();


    }


    // בודק  אם יש אפשרות לשלם או לא ( אם קיים אמצעי תשלום לאותו משתמש - פונקציה אחרת) אם כן נותן לו לשלם אם לא מחזיר שגיאה
    private void clickedPageToPay() {
        back_BTN_topay.setOnClickListener(v -> startActivity(new Intent(ToPayActivity.this, MyAccountActivity.class)));
        pay_BTN_now.setOnClickListener(v -> {
            checkPM(new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(Boolean result) {
                    if (result) {
                        Toast.makeText(ToPayActivity.this, "Successfully Paid", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ToPayActivity.this, MenuActivity.class));
                    } else {
                        Toast.makeText(ToPayActivity.this, "Faild Paid", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }



// בודק בדאטא בייס אם יש אמצעי תשלום למשתמש
    private void checkPM(OnCompleteListener<Boolean> onCompleteListener) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                final AtomicBoolean exists = new AtomicBoolean(false); // שימוש ב-AtomicBoolean

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = currentUser.getUid();
                DatabaseReference paymentMethodRef = databaseReference.child("Users").child(userId).child("PaymentMethods");

                final CountDownLatch latch = new CountDownLatch(1);
                paymentMethodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        exists.set(dataSnapshot.exists());
                        latch.countDown();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        latch.countDown();
                    }
                });

                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return exists.get();
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    Toast.makeText(ToPayActivity.this, "Successfully Paid", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ToPayActivity.this, MenuActivity.class));
                } else {
                    Toast.makeText(ToPayActivity.this, "Failed to Pay", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }



//כפתורים
    private void findView() {
        back_BTN_topay = findViewById(R.id.back_BTN_topay);
        price_LBL_topay = findViewById(R.id.price_LBL_topay);
        pay_BTN_now = findViewById(R.id.pay_BTN_now);

    }
}

