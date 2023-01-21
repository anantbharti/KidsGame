package com.example.kidsgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Map<TextView,TextView> viewMap = new HashMap<TextView,TextView>();
    TextView l1,l2,r1,r2;
    PaintView paintView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
        paintView.setViewMap(viewMap);

    }

    private void setUpViews() {
        l1 = findViewById(R.id.rc);
        l2 = findViewById(R.id.bc);
        r1 = findViewById(R.id.bs);
        r2 = findViewById(R.id.rs);
        paintView = findViewById(R.id.pv);
        viewMap.put(l1,r2);
        viewMap.put(l2,r1);
    }
}