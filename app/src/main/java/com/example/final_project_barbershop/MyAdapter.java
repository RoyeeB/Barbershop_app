package com.example.final_project_barbershop;

import static android.content.Intent.getIntent;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.time.ZoneId;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyAdapter extends SimpleAdapter {
    private List<HashMap<String, String>> data;
    private Context context;
    private MyAccountActivity activity;

    public MyAdapter(Context context, List<HashMap<String, String>> data, MyAccountActivity activity) {

            super(context, data, R.layout.activity_list_appointment,
                    new String[]{"date", "time"},
                    new int[]{R.id.list_item_date, R.id.list_item_time});
            this.context = context;
            this.data = data;
            this.activity = activity; // Save the activity reference
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            Button cancelButton = view.findViewById(R.id.cancel_BTN_list);
            Button payButton = view.findViewById(R.id.pay_BTN_list);


            String dateString = data.get(position).get("date");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd" , Locale.getDefault());

            try {
                Date appointmentDate = dateFormat.parse(dateString);
                Calendar currentCalendar = Calendar.getInstance();
                Calendar currentDate = Calendar.getInstance();
                currentCalendar.setTime(appointmentDate);



                if ((appointmentDate != null) && ((currentCalendar.before(currentDate)) || ( currentCalendar.equals(currentDate)))) {
                    // אם התור כבר עבר מסתיר את כפתור הביטול
                    cancelButton.setVisibility(View.INVISIBLE);
                } else {
                    // אם התור עתידי  תציג את כפתור הביטול והגדר את ה- OnClickListener
                    cancelButton.setVisibility(View.VISIBLE);
                    cancelButton.setOnClickListener(v -> {
                        HashMap<String, String> selectedAppointment = data.get(position);
                        String key = selectedAppointment.get("key");
                        String date = selectedAppointment.get("date");
                        String time = selectedAppointment.get("time");
                        activity.cancelAppointment(key, date, time);
                    });
                }

                payButton.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ToPayActivity.class);
                    context.startActivity(intent);
                });





            } catch (ParseException e) {
                e.printStackTrace();
                cancelButton.setVisibility(View.GONE);
            }

            return view;
        }


}






