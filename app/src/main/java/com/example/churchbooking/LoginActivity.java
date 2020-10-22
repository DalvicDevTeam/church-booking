package com.example.churchbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText mPhone_number;
    private Button mButton;
    private TextInputLayout mTextInputLayout;
    private String mMobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mButton = findViewById(R.id.btn_login);
        mTextInputLayout = findViewById(R.id.textInputLayout);
        mPhone_number = findViewById(R.id.et_phone_number);


        mPhone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > mTextInputLayout.getCounterMaxLength())
                    mTextInputLayout.setError("Max phone number length is " + mTextInputLayout.getCounterMaxLength());
                else
                    mTextInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mMobile = mTextInputLayout.getEditText().getText().toString().trim();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                if(mMobile.isEmpty() || mMobile.length() <= 9){
                    mPhone_number.setError("Enter a valid mobile");
                    mPhone_number.requestFocus();
                    return;
                }
                Intent intent = new Intent(LoginActivity.this, LoginConfirmationActivity.class);
                intent.putExtra(LoginConfirmationActivity.INTENT_EXTRA_PHONE_NUMBER, mMobile);
                startActivity(intent);
            }
        });
    }


}