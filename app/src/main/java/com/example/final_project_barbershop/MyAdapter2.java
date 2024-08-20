package com.example.final_project_barbershop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MyAdapter2  extends SimpleAdapter {

    private final Context context;
    private final List<HashMap<String, String>> data;
    private BarberActivity activity;
    private DatabaseReference appointmentsRef;


    public MyAdapter2(Context context, List<HashMap<String, String>> data, BarberActivity activity) {
        super(context, data, R.layout.activity_list_item_barber,
                new String[]{"date", "time"},
                new int[]{R.id.list_item_date_barber, R.id.list_item_time_barber});
        this.context = context;
        this.data = data;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_list_item_barber, parent, false);
        }

        // קבל את ה-HashMap הנוכחי
        HashMap<String, String> item = (HashMap<String, String>) getItem(position);

        // עדכון TextViews
        TextView list_item_date_barber = convertView.findViewById(R.id.list_item_date_barber);
        TextView list_item_time_barber = convertView.findViewById(R.id.list_item_time_barber);
        Button cancel_BTN_list_barber = convertView.findViewById(R.id.cancel_BTN_list_barber);
        HashMap<String, String> selectedAppointment = data.get(position);
        if (item != null) {
            list_item_date_barber.setText(item.get("date"));
            list_item_time_barber.setText(item.get("time"));

            // הגדרת פעולה עבור הכפתור
            cancel_BTN_list_barber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   cancel_BTN_list_barber.setOnClickListener(view -> {
                        HashMap<String, String> selectedAppointment = data.get(position);
                        String key = selectedAppointment.get("key");
                        String date = selectedAppointment.get("date");
                        String time = selectedAppointment.get("time");
                        activity.cancelAppointment(key, date, time);
                    });
                }
            });
        }

        return convertView;
    }

    private void cancelAppointment(String appointmentId) {
        appointmentsRef.child(appointmentId).removeValue();
        activity.loadAppointmentsForAllUsers();
    }
}
