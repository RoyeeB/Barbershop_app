package com.example.final_project_barbershop;

import static android.content.ContentValues.TAG;
import static com.example.final_project_barbershop.MyAccountActivity.database;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class BarberActivity extends AppCompatActivity {


    private Button signout_BTN_barber;
    private ListView appointments_list_view_barber;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_page);
        findView();
        clickedPageBarber();
        loadAppointmentsForAllUsers();
    }


        private void clickedPageBarber() {
            signout_BTN_barber.setOnClickListener(new View.OnClickListener() {  //יציאה מהמשתמש
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(BarberActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BarberActivity.this, HomePageActivity.class));
                }
            });
        }

        private void findView() {
            signout_BTN_barber = findViewById(R.id.signout_BTN_barber);
            appointments_list_view_barber = findViewById(R.id.appointments_list_view_barber);
        }

    public void loadAppointmentsForAllUsers() {
        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointments");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // עבור כל משתמש
                    for (DataSnapshot appointmentSnapshot : userSnapshot.getChildren()) {
                        // מובן של כל תור (appointment)
                        Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                        if (appointment != null) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("date", appointment.getDate());
                            map.put("time", appointment.getTime());
                            map.put("key", appointmentSnapshot.getKey()); // שמור את המפתח של Firebase
                            data.add(map);
                        }
                    }
                }

                // עדכון ה-Adapter עם הנתונים שנשלפו
                MyAdapter2 adapter = new MyAdapter2 (BarberActivity.this, data , BarberActivity.this);
                appointments_list_view_barber.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // טיפול בשגיאות כאן
                Log.e("FirebaseError", "Failed to read value.", databaseError.toException());
            }
        });
    }
    public  void cancelAppointment(String key , String date, String time) {
        DatabaseReference appointmentRef = database.getReference("Appointments").child("date").child("time");

        appointmentRef.removeValue().addOnCompleteListener(task -> { // מחיקה
            if (task.isSuccessful()) {
                appointmentRef.removeValue();
                Toast.makeText(BarberActivity.this, "Appointment canceled successfully.", Toast.LENGTH_SHORT).show();
                loadAppointmentsForAllUsers(); // טוען מחדש את הרשימה
            } else {
                Toast.makeText(BarberActivity.this, "Failed to cancel appointment.", Toast.LENGTH_SHORT).show();
            }
        });
    }




}

