package com.example.churchbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

public class LoginConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_confirmation);
        TextView tv_change_phone = findViewById(R.id.tv_change_phone_number);

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

    }
}
