package com.example.churchbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginConfirmationActivity extends AppCompatActivity {

    public static final String INTENT_EXTRA_PHONE_NUMBER = "PHONE_NUMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_confirmation);
        TextView tv_change_phone = findViewById(R.id.tv_change_phone_number);
        TextView tv_phone = findViewById(R.id.tv_phone);

        TextInputEditText et_sent_code = findViewById(R.id.et_sent_code);
        final TextInputLayout textInputLayout = findViewById(R.id.et_send_code_layout);
        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra(INTENT_EXTRA_PHONE_NUMBER);
        if (phoneNumber != null){
            tv_phone.setText(phoneNumber);

        }



        Button btn_continue = findViewById(R.id.btn_continue);

        //creating clickable text view
        String text = "Change Phone Number";
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(LoginConfirmationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        };
        spannableString.setSpan(clickableSpan,0,19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_change_phone.setText(spannableString);
        tv_change_phone.setMovementMethod(LinkMovementMethod.getInstance());


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginConfirmationActivity.this,RegistrationProfileActivity.class);
                startActivity(intent);
            }
        });
        et_sent_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > textInputLayout.getCounterMaxLength())
                    textInputLayout.setError("Max sms code is " + textInputLayout.getCounterMaxLength());
                else
                    textInputLayout.setError(null);
            }
        });

    }
}
