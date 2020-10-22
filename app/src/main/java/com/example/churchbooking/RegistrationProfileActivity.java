package com.example.churchbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegistrationProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_profile);

        final TextInputLayout textInputLayout = findViewById(R.id.textInputLayout_full_name);
        final TextInputEditText et_full_name = findViewById(R.id.et_full_name);
        getSupportActionBar().setTitle("Personal Info");


        Spinner sp_city_of_residence = findViewById(R.id.sp_city_of_residence);
        Spinner sp_church_membership = findViewById(R.id.sp_church_membership);
        Button btn_let_me_in = findViewById(R.id.btn_let_me_in);

        btn_let_me_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationProfileActivity.this,HomeActivity.class);
                String userName = et_full_name.getText().toString();
                intent.putExtra(HomeActivity.INTENT_EXTRA_STRING,userName);
                startActivity(intent);
            }
        });

        et_full_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > textInputLayout.getCounterMaxLength())
                    textInputLayout.setError("Max character length is " + textInputLayout.getCounterMaxLength());
                else
                    textInputLayout.setError(null);
            }
        });
    }
}
