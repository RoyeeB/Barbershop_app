package com.example.final_project_barbershop;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PaymentMethodActivity extends AppCompatActivity {

    private ImageButton back_BTN_paymentmethod;
    private AppCompatEditText id_EDT_paymentmethod;
    private AppCompatEditText cardNumber_EDT_paymentmethod;
    private AppCompatEditText cvv_EDT_paymentmethod;
    private AppCompatEditText expiryDate_EDT_paymentmethod;
    private MaterialButton save_BTN_paymentmetod;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseUser currentUser;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        dateFormat = new SimpleDateFormat("MM/yy", Locale.getDefault());
        calendar = Calendar.getInstance();
        auth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = auth.getCurrentUser();
        findView();
        clickedPagePaymentmethod();



    }

    private void findView() {
        back_BTN_paymentmethod = findViewById(R.id.back_BTN_paymentmethod);
        id_EDT_paymentmethod = findViewById(R.id.id_EDT_paymentmethod);
        cardNumber_EDT_paymentmethod = findViewById(R.id.cardNumber_EDT_paymentmethod);
        cvv_EDT_paymentmethod = findViewById(R.id.cvv_EDT_paymentmethod);
        expiryDate_EDT_paymentmethod = findViewById(R.id.expiryDate_EDT_paymentmethod);
        save_BTN_paymentmetod = findViewById(R.id.save_BTN_paymentmetod);
    }

    private void clickedPagePaymentmethod() {
        back_BTN_paymentmethod.setOnClickListener(v -> startActivity( new Intent(PaymentMethodActivity.this , MenuActivity.class)));

        save_BTN_paymentmetod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // מכניס הכל לסטרינגים ומשם שול חהכל לבדיקה
                String id = id_EDT_paymentmethod.getText().toString();
                String cardNumber = cardNumber_EDT_paymentmethod.getText().toString();
                String cvv = cvv_EDT_paymentmethod.getText().toString();
                String expiryDate = expiryDate_EDT_paymentmethod.getText().toString();
                checkAll (id , cardNumber , cvv ,expiryDate);
            }
        });


    }

 // בדיקה של הפרמטרים
    private void checkAll(String id, String cardNumber, String cvv, String expiryDate) {
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(cardNumber) || TextUtils.isEmpty(cvv) || TextUtils.isEmpty(expiryDate) ) {
            Toast.makeText(PaymentMethodActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
        }if ((id.length() != 9) || (isNumeric(id) == false)){
            Toast.makeText(PaymentMethodActivity.this , "wrong id!" , Toast.LENGTH_SHORT).show();
        }else if ((cardNumber.length() != 16) || (isNumeric (cardNumber) == false)){
            Toast.makeText(PaymentMethodActivity.this , "wrong card number!" , Toast.LENGTH_SHORT).show();
        } else if ((cvv.length() != 3) || (isNumeric(cvv) == false)){
            Toast.makeText(PaymentMethodActivity.this , "wrong cvv!" , Toast.LENGTH_SHORT).show();
        } else if (!isExpiryDateValid(expiryDate)){
            Toast.makeText(PaymentMethodActivity.this , "wrong date!" , Toast.LENGTH_SHORT).show();
        }
        else {
            addPaymentMethod(id, cardNumber, cvv, expiryDate);
        }

    }

    public void addPaymentMethod(String id, String cardNumber, String cvv, String expiryDate){

        auth.updateCurrentUser(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    PaymentMethod PM = new PaymentMethod(cardNumber, expiryDate, cvv, id);
                    String userId = currentUser.getUid();
                    database.getReference("Users").child(userId).child("PaymentMethods").setValue(PM);
                    Toast.makeText(PaymentMethodActivity.this, "Payment method add successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PaymentMethodActivity.this, MenuActivity.class));
                }
                else
                    Toast.makeText(PaymentMethodActivity.this, "Payment method add failed!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    // בדיקה של התאריך תפוגה
    private boolean isExpiryDateValid(String dateString) {
        if (TextUtils.isEmpty(dateString)) {
            return false;
        }

        try {
            Date parsedDate = dateFormat.parse(dateString);
            if (parsedDate == null) {
                return false;
            }
            Calendar expiryDate = Calendar.getInstance();
            expiryDate.setTime(parsedDate);
            expiryDate.set(Calendar.DAY_OF_MONTH, expiryDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            Calendar currentDate = Calendar.getInstance();
            int currntMonth = currentDate.get(Calendar.MONTH);
            int currntYear = currentDate.get(Calendar.YEAR);

            int subMonth = Integer.parseInt(dateString.substring(0, 2));
            int subYear = Integer.parseInt(dateString.substring(3, 5)) + 2000;

            if (subYear < currntYear)
                return false;
             if ((subYear == currntYear) && (((subMonth < currntMonth) || (subMonth == currntMonth)))) {
                    return false;
            }
            return true;

        } catch (ParseException e) {
            return false;
        }
    }
// בודק שהוקלדו רק מספרים
    public boolean isNumeric(String input) {
        return input.matches("\\d+");
    }
}
