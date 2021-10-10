package com.phuquytran_300303518.moneysaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phuquytran_300303518.moneysaver.model.User;

public class Register extends AppCompatActivity {
    TextView txtGoToLogin;
    EditText rEmail, rPassword, rConfirmPassword;
    Button btnRegister;
    ProgressBar loadingProgressBar;

    FirebaseAuth mAuth;

    final static String TAG = "Register";

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
            rPassword.setError("Password mus be more than 6 characters");
            return;
        }

        loadingProgressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful())
            {
                //Sign up successfully
                Log.i(TAG, "onComplete: successfully");
                FirebaseUser user = mAuth.getCurrentUser();

                //Save to firebase Database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseRef = database.getReference("users").child(user.getUid());
                Toast.makeText(Register.this, "User Created Successfully!", Toast.LENGTH_SHORT).show();
                goToLogin();

            }else{
                //Sign up fail
                Log.w(TAG, "onComplete: Failed", task.getException());
                Toast.makeText(Register.this, "Register failed", Toast.LENGTH_SHORT).show();
                goToLogin();
            }
        });
    }
    public void goToLogin(){
        Intent loginIntent = new Intent(Register.this, Login.class);
        startActivity(loginIntent);
    }
}