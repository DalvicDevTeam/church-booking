package com.example.churchbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button = findViewById(R.id.btn_login);
        final TextInputLayout textInputLayout = findViewById(R.id.et_full_name_layout);
        final TextInputEditText et_phone_number = findViewById(R.id.et_phone_number);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginConfirmationActivity.class);

                String phoneNumber = et_phone_number.getText().toString();
                if (phoneNumber!= null ){
                    intent.putExtra(LoginConfirmationActivity.INTENT_EXTRA_PHONE_NUMBER,phoneNumber);
                }

                startActivity(intent);
            }
        });
        et_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > textInputLayout.getCounterMaxLength())
                    textInputLayout.setError("Max phone number length is " + textInputLayout.getCounterMaxLength());
                else
                    textInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}