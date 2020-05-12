package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    ListView lv;
    protected static final String API_URL = "https://api.rootnet.in/covid19-in/stats/latest";
    private DataAdapter dataAdapter;
    private MyViewModel myViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        lv = (ListView) findViewById(R.id.listview);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        myViewModel.getStates().observe(this, new Observer<List<States>>() {
            @Override
            public void onChanged(List<States> states) {
                // update UI
                dataAdapter = new DataAdapter(Main3Activity.this, (ArrayList<States>) states);

                lv.setAdapter(dataAdapter);

            }
        });
    }
}
