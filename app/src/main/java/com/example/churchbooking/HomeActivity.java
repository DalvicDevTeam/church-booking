package com.example.churchbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    public static final String INTENT_EXTRA_STRING = "USER_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView tv_userName = findViewById(R.id.tv_userName);
        SearchView searchView = findViewById(R.id.search_view);

        Intent intent = getIntent();
        String userName = intent.getStringExtra(INTENT_EXTRA_STRING);
        tv_userName.setText(userName);

    }
}
