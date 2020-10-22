package com.example.churchbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginConfirmationActivity extends AppCompatActivity {

    public static final String INTENT_EXTRA_PHONE_NUMBER = "PHONE_NUMBER";
    private static final String TAG = "LoginConfirmation";
    private TextView mTv_phone;
    private TextInputEditText mEt_sent_code;
    private String mVerificationId;


    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressBar mProgressBar;
    private String mPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_confirmation);
        TextView tv_change_phone = findViewById(R.id.tv_change_phone_number);
        mTv_phone = findViewById(R.id.tv_phone);
        mProgressBar = findViewById(R.id.progressBar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mEt_sent_code = findViewById(R.id.et_sent_code);
        final TextInputLayout textInputLayout = findViewById(R.id.et_send_code_layout);


        Intent intent = getIntent();
        mPhoneNumber = intent.getStringExtra(INTENT_EXTRA_PHONE_NUMBER);
        if (mPhoneNumber != null){
            mTv_phone.setText(mPhoneNumber);
            startPhoneNumberVerification(mPhoneNumber);

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

        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mEt_sent_code.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6){
                    mEt_sent_code.setError("Enter valid code");
                    mEt_sent_code.requestFocus();
                    return;
                }
                //verifying the code entered manually
                verifyVerificationCode(code);

            }
        });


        mEt_sent_code.addTextChangedListener(new TextWatcher() {
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

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }




    }
    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks  mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
            // [START_EXCLUDE silent]
            mVerificationInProgress = false;

            // [END_EXCLUDE]


            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                mEt_sent_code.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(LoginConfirmationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;

        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }



    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(mPhoneNumber);
        }
        // [END_EXCLUDE]
    }
    // [END on_start_check_user]

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void startPhoneNumberVerification(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        mVerificationInProgress = true;
        showProgressBar();
    }

    // [START sign_in_with_phone]
    private void  signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            Intent intent = new Intent(LoginConfirmationActivity.this,RegistrationProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";


                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                message = "Invalid code entered...";
                                Toast.makeText(LoginConfirmationActivity.this,message,Toast.LENGTH_LONG);

                            }

                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumber;
        if (TextUtils.isEmpty(phoneNumber)) {
            //mBinding.fieldPhoneNumber.setError("Invalid phone number.");
            mEt_sent_code.setError("Invalid phone number");
            return false;
        }

        return true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }
    public void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
    }



}
