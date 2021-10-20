package com.phuquytran_300303518.moneysaver.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.phuquytran_300303518.moneysaver.R;

public class RegisterActivity extends AppCompatActivity {
    TextView txtGoToLogin;
    EditText rEmail, rPassword, rConfirmPassword;
    Button btnRegister;
    ProgressBar loadingProgressBar;

    FirebaseAuth mAuth;

    final static String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        txtGoToLogin = findViewById(R.id.txtBackToLogin);
        rEmail = findViewById(R.id.register_edtUsername);
        rPassword = findViewById(R.id.register_edtPassword);
        rConfirmPassword = findViewById(R.id.register_edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        loadingProgressBar = findViewById(R.id.loading);

        btnRegister.setOnClickListener(view -> {
            onRegisterClick(view);
        });

        txtGoToLogin.setOnClickListener(view -> {
            goToLogin();
        });
    }

    //on Register login, Check the input then create a new user
    public void onRegisterClick(View v){
        String email = rEmail.getText().toString();
        String password = rPassword.getText().toString();
        String confirmPassword = rConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            rEmail.setError("Email is required");
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            rPassword.setError("Password is required");
            return;
        }
        if (TextUtils.isEmpty(confirmPassword))
        {
            rConfirmPassword.setError("Confirm Password is required");
            return;
        }
        if (password.length() < 6) {
            rPassword.setError("Password must be more than 6 characters");
            return;
        }

        loadingProgressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful())
            {
                //Sign up successfully
                Log.i(TAG, "onComplete: successfully");
                Toast.makeText(RegisterActivity.this, "User Created Successfully!", Toast.LENGTH_SHORT).show();
                goToLogin();

            }else{
                //Sign up fail
                Log.w(TAG, "onComplete: Failed", task.getException());
                Toast.makeText(RegisterActivity.this, "RegisterActivity failed", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void goToLogin(){
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }
}