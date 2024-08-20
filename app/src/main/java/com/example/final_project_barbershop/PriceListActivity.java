package com.example.final_project_barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class PriceListActivity extends AppCompatActivity {

    private ImageButton back_BTN_pricelist;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list);
        findView();
        clickedPageRegister();

        ListView priceListView = findViewById(R.id.price_list_view);

        String[] services = {
                "Haircut (Adults)",
                "Haircut (Children under 12)",
                "Beard Trim (Basic)",
                "Beard Trim (Full Grooming)",
                "Shave (Basic)",
                "Shave (Hot Towel)",
                "Hair Color (Full)",
                "Hair Color (Highlights)",
                "Hair Treatment",
                "Package: Haircut + Shave"
        };

        String[] prices = {
                "$20 / 1000 points",
                "$15 / 750 points",
                "$10 / 500 points",
                "$15 / 750 points",
                "$15 / 750 points",
                "$20 / 1000 points",
                "$50 / 2500 points",
                "$40 / 2000 points",
                "$30 / 1500 points",
                "$30 / 1500 points"
        };

        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        for (int i = 0; i < services.length; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("service", services[i]);
            map.put("price", prices[i]);
            data.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.activity_list_item,
                new String[]{"service", "price"},
                new int[]{R.id.list_item_text, R.id.list_item_price}
        );

        priceListView.setAdapter(adapter);
    }


    private void clickedPageRegister() {
        back_BTN_pricelist.setOnClickListener(v -> startActivity( new Intent(PriceListActivity.this , MenuActivity.class)));    }

    private void findView() {
        back_BTN_pricelist = findViewById(R.id.back_BTN_pricelist);
    }

    }


