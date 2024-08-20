package com.example.final_project_barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddressActivity extends AppCompatActivity {
    private ImageButton back_BTN_address;


    private static final String TAG = "AddressActivity";
    private MapsFragment2 fragmentMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        findView();
        clickedPageRegister();

        fragmentMap = new MapsFragment2();
        getSupportFragmentManager().beginTransaction().add(R.id.frameMap, fragmentMap).commit();
    }

    CallBackList callBack_list = new CallBackList() {
        @Override
        public void zoom(double lat, double lon) {
            try {
                GoogleMap gm = fragmentMap.getmMap();
                if (gm != null) {
                    LatLng point = new LatLng(lon, lat);
                    gm.addMarker(new MarkerOptions()
                            .position(point)
                            .title("    "));
                    gm.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13.0f));
                } else {
                    Log.e(TAG, "GoogleMap object is null");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in zoom method: ", e);
            }
        }

    };

    private void clickedPageRegister() {
        back_BTN_address.setOnClickListener(v -> startActivity( new Intent(AddressActivity.this , MenuActivity.class)));    }

    private void findView() {
        back_BTN_address = findViewById(R.id.back_BTN_address);
    }


}


