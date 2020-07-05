package com.example.marvelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    EditText mobile;
    TextView country_code;
    ProgressBar progressBar;
    String otp_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button=findViewById(R.id.request_button);
        mobile=findViewById(R.id.edit_text_mobile);
        country_code=findViewById(R.id.country_code);
        progressBar=findViewById(R.id.send_otp_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        DataBaseHelper dataBaseHelper=new DataBaseHelper(this);
        Cursor res=dataBaseHelper.get_All_Data();
        if(res.getCount()>0)
        {
            while(res.moveToNext())
            {
                String mobile_number=res.getString(2);
                Intent intent=new Intent(this,Profile.class);
                intent.putExtra("Mobile Number",mobile_number);
                startActivity(intent);
                finish();
            }
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_otp();
            }
        });
    }

            public void send_otp()
            {
                if(mobile.getText().toString().trim().isEmpty())
                {
                    mobile.setError("Required");
                    mobile.requestFocus();
                    return;
                }
                if(mobile.getText().toString().trim().length()<10)
                {
                    mobile.setError("Incorrect Number");
                    mobile.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                String number=country_code.getText().toString().trim()+mobile.getText().toString().trim();
                Toast.makeText(this,number,Toast.LENGTH_SHORT).show();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        number,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                        mCallbacks);        // OnVerificationStateChangedCallbacks

            }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            otp_code=s;
            Toast.makeText(getApplicationContext(),"Otp Send", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), VerifyOtp.class);
            intent.putExtra("code", otp_code);
            intent.putExtra("Mobile Number",mobile.getText().toString().trim());
            progressBar.setVisibility(View.INVISIBLE);
            startActivity(intent);
            finish();
        }
    };
}