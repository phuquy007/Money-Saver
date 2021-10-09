package com.phuquytran_300303518.moneysaver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Script;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    TextView goToRegister;
    EditText edtEmail, edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        goToRegister = findViewById(R.id.txtRegister);
        edtEmail = findViewById(R.id.login_edtUsername);
        edtPassword = findViewById(R.id.login_edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        goToRegister.setOnClickListener(view ->  {
                Intent registerIntent = new Intent(Login.this, Register.class);
                startActivity(registerIntent);
            }
        );

        btnLogin.setOnClickListener(view -> {
            onLoginClick(view);
        });

    }
    public void login(String email, String password){
        try {
            AuthResult authResult = Tasks.await(mAuth.signInWithEmailAndPassword(email, password));
            FirebaseUser user = authResult.getUser();

            if (user == null){
                Toast.makeText(Login.this, "Username or Password are incorrect!", Toast.LENGTH_SHORT).show();
            }else{
                goToMain();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public void goToMain(){
        Intent mainIntent = new Intent(Login.this, MainActivity.class);
        startActivity(mainIntent);
    }

    public void onLoginClick(View v){
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            edtEmail.setError("Email is required");
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            edtPassword.setError("Password is required");
            return;
        }
        login(email, password);
    }
}