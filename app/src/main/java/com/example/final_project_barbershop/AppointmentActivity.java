package com.example.final_project_barbershop;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentActivity extends AppCompatActivity {
    private  CalendarView calendar;
    private ImageButton back_BTN_cal;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseUser currentUser;

    private Map<String, List<String>> availableTimesPerDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        auth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = auth.getCurrentUser();
        findView();
        clickedPageAppointment();
        availableTimesPerDay = new HashMap<>();
        initializeAvailableTimes();

    }
 // מביא את כל התורות הזמינים בכללי - ימים שלא עובדים פה זה שבת ושלישי
    private void initializeAvailableTimes() {
        List<String> weekdayTimes = generateAvailableTimes(10, 18);
        List<String> fridayTimes = generateAvailableTimes(10, 14);
        Calendar calendar = Calendar.getInstance();

        for (int day = 1; day <= 31; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            String date = String.format("2024-08-%02d", day);
            List<String> availableTimes = new ArrayList<>();

            switch (dayOfWeek) {
                case Calendar.SATURDAY:
                case Calendar.TUESDAY:
                    availableTimesPerDay.put(date, new ArrayList<>());
                    break;

                case Calendar.FRIDAY: // בשישי יש שעות נפרדות
                    availableTimes.addAll(fridayTimes);
                    break;

                default:
                    availableTimes.addAll(weekdayTimes);
                    break;
            }

            if (!availableTimes.isEmpty()) {
                DatabaseReference reference = database.getReference("Appointments").child(date);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot timeSnapshot : snapshot.getChildren()) {
                            String bookedTime = timeSnapshot.getKey();
                            if (availableTimes.contains(bookedTime)) {
                                availableTimes.remove(bookedTime);
                            }
                        }
                        availableTimesPerDay.put(date, availableTimes);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }


// מגדיר את כל השעות עבודה
    private List<String> generateAvailableTimes(int startHour, int endHour) {
        List<String> times = new ArrayList<>();
        int minutes = 0;

        for (int hour = startHour; hour < endHour; hour++) {
            for (int i = 0; i < 3; i++) {
                String time = String.format("%02d:%02d", hour, minutes);
                times.add(time);
                minutes += 20;
            }
            minutes = 0;
        }
        int lastHour = endHour ;
        int lastMinutes = 40;
        if (lastMinutes + 20 <= 60) {
            lastMinutes = 00 ;
            String additionalTime = String.format("%02d:%02d", lastHour, lastMinutes);
            times.add(additionalTime);
        }
        return times;
    }
// קביעת תור לספר
    private void bookAppointment(String date, String time) {
        String userId = currentUser.getUid();
        DatabaseReference reference = database.getReference("Appointments").child(date).child(time);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(AppointmentActivity.this, "This time slot is already booked.", Toast.LENGTH_SHORT).show();
                } else {
                    Appointment appointment = new Appointment(date, time, 0);
                    reference.setValue(appointment);
                    database.getReference("Users").child(userId).child("Appointments").push().setValue(appointment);
                    Toast.makeText(AppointmentActivity.this, "Appointment booked successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AppointmentActivity.this, MenuActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AppointmentActivity.this, "Failed to book appointment. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
 // מראה מה נשאר זמין
    private void showAvailableTimes(String date) {
        List<String> availableTimes = availableTimesPerDay.get(date);

        if (availableTimes == null || availableTimes.isEmpty()) {
            Toast.makeText(this, "No available times for this date", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] timesArray = availableTimes.toArray(new String[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Available Times for " + date);
        builder.setItems(timesArray, (dialog, which) -> {
            String selectedTime = timesArray[which];
            bookAppointment(date, selectedTime);
        });

        builder.show();
    }

   private void clickedPageAppointment() {
       back_BTN_cal.setOnClickListener(v -> startActivity( new Intent(AppointmentActivity.this , MenuActivity.class)));

        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(year, month, dayOfMonth);
            Calendar currentCalendar = Calendar.getInstance();
               if (selectedCalendar.before(currentCalendar)) {  // בודק שלא לחצו על תאריך שעבר כבר
                Toast.makeText(AppointmentActivity.this, "Cannot select a past date.", Toast.LENGTH_SHORT).show();
            } else {

                showAvailableTimes(selectedDate);
            }

        });
    }
    private void findView() {
        calendar = findViewById(R.id.calendar);
        back_BTN_cal = findViewById(R.id.back_BTN_cal);
    }




}

