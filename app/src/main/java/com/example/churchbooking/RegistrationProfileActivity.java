package com.example.churchbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.churchbooking.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationProfileActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationProfile";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        final TextInputLayout textInputLayout = findViewById(R.id.textInputLayout_full_name);
        final TextInputEditText et_full_name = findViewById(R.id.et_full_name);
        getSupportActionBar().setTitle("Personal Info");


        Spinner sp_city_of_residence = findViewById(R.id.sp_city_of_residence);
        Spinner sp_church_membership = findViewById(R.id.sp_church_membership);
        Button btn_let_me_in = findViewById(R.id.btn_let_me_in);

        btn_let_me_in.setOnClickListener(new View.OnClickListener() {

            private String mUserName;
            private Intent mIntent;

            @Override
            public void onClick(View v) {
                mIntent = new Intent(RegistrationProfileActivity.this,HomeActivity.class);
                mUserName = et_full_name.getText().toString();

                User user = new User();
                user.setUserName(mUserName);
                user.setChurchMembershipId("");
                user.setProfilePic("");
                user.setUserId(mAuth.getCurrentUser().getUid());

                mDatabase.getReference()
                        .child(getString(R.string.db_node_users))
                        .child(mAuth.getCurrentUser().getUid())
                        .setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mIntent.putExtra(HomeActivity.INTENT_EXTRA_STRING, mUserName);
                        startActivity(mIntent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrationProfileActivity.this,"something went wrong",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: FAILED:" + e.getMessage());

                    }
                });

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
