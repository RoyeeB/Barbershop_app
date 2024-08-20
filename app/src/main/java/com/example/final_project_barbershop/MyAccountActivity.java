package com.example.final_project_barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyAccountActivity extends AppCompatActivity {

    private AppCompatImageButton back_BTN_myaccount;
    private AppCompatTextView user_LBL_name;
    private AppCompatTextView user_LBL_email;
    private AppCompatTextView user_LBL_phone;
    private AppCompatTextView user_LBL_cardnumber;
    private AppCompatTextView user_LBL_countappointment;
    private AppCompatTextView user_LBL_points;
    private static ListView appointments_list_view;
    private FirebaseAuth auth;
    static FirebaseDatabase database;
    static FirebaseUser currentUser;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = auth.getCurrentUser();
        findView();
        clickedPageMyAccount();
        updateAll();
        loadAppointments();
        countAppointments();


    }


    private void clickedPageMyAccount() {
        back_BTN_myaccount.setOnClickListener(v -> startActivity(new Intent(MyAccountActivity.this, MenuActivity.class)));
    }

    private void findView() {
        user_LBL_name = findViewById(R.id.user_LBL_name);
        user_LBL_email = findViewById(R.id.user_LBL_email);
        user_LBL_phone = findViewById(R.id.user_LBL_phone);
        user_LBL_cardnumber = findViewById(R.id.user_LBL_cardnumber);
        back_BTN_myaccount = findViewById(R.id.back_BTN_myaccount);
        appointments_list_view = findViewById(R.id.appointments_list_view);
        user_LBL_countappointment = findViewById(R.id.user_LBL_countappointment);
        user_LBL_points = findViewById(R.id.user_LBL_points);

    }

    public void updateAll() { // מציג עבור המשתמש את כל הפרטים שלו

        String userId = currentUser.getUid();
        DatabaseReference reference = database.getReference("Users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String cardNum = snapshot.child("PaymentMethods").child("cardNumber").getValue(String.class);
                    user_LBL_name.setText("Name: " + name);
                    user_LBL_email.setText("Email: " + email);
                    user_LBL_phone.setText("Phone: " + phone);
                    if (cardNum != null)
                        user_LBL_cardnumber.setText("Card Number: " + cardNum);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void loadAppointments() { // טוען את התורות של המשתמש
        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        String userId = currentUser.getUid();
        DatabaseReference reference = database.getReference("Users").child(userId).child("Appointments");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                    if (appointment != null) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("date", appointment.getDate());
                        map.put("time", appointment.getTime());
                        map.put("key", appointmentSnapshot.getKey()); // Save the Firebase key
                        data.add(map);
                    }
                }

                // Create MyAdapter with reference to MyAccountActivity
                MyAdapter adapter = new MyAdapter(MyAccountActivity.this, data, MyAccountActivity.this);
                appointments_list_view.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyAccountActivity.this, "Failed to load appointments.", Toast.LENGTH_SHORT).show();
            }
        });
    }




 // ביטול של תור
    public  void cancelAppointment(String key, String date, String time) {
        String userId = currentUser.getUid();
        DatabaseReference userRef = database.getReference("Users").child(userId).child("Appointments").child(key);
        DatabaseReference appointmentRef = database.getReference("Appointments").child(date).child(time);

        userRef.removeValue().addOnCompleteListener(task -> { // מחיקה
            if (task.isSuccessful()) {
                appointmentRef.removeValue();
                Toast.makeText(MyAccountActivity.this, "Appointment canceled successfully.", Toast.LENGTH_SHORT).show();
                loadAppointments(); // טוען מחדש את הרשימה
            } else {
                Toast.makeText(MyAccountActivity.this, "Failed to cancel appointment.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void countAppointments() {  // סופר את התורות שעברו וגם מחשב את הנקודות למשתמש
        String userId = currentUser.getUid();
        DatabaseReference userAppointmentsRef = database.getReference("Users").child(userId).child("Appointments");
        DatabaseReference userAppointmentCountRef = database.getReference("Users").child(userId).child("appointmentCount");
        DatabaseReference userAppointmentPointRef = database.getReference("Users").child(userId).child("points");
        userAppointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int pastAppointmentCount = 0;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar currentCalendar = Calendar.getInstance();
                Date currentDate = currentCalendar.getTime();

                for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                    String dateString = appointmentSnapshot.child("date").getValue(String.class);
                    try {
                        Date appointmentDate = dateFormat.parse(dateString);
                        if (appointmentDate != null && appointmentDate.before(currentDate)) {
                            pastAppointmentCount++;
                        }
                    } catch (ParseException e) {
                        Log.e("DateParseError", "Failed to parse date.", e);
                    }
                }
                userAppointmentPointRef.setValue(pastAppointmentCount*50);
                userAppointmentCountRef.setValue(pastAppointmentCount);
                user_LBL_countappointment.setText("Number of past appointments: " + pastAppointmentCount);
                user_LBL_points.setText("Your points :" + pastAppointmentCount*50 );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Failed to load appointments count.", error.toException());
            }
        });
    }

    }
