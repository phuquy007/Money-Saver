package com.phuquytran_300303518.moneysaver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class Login extends AppCompatActivity {
    private static final int GG_SIGN_IN = 3;
    private static final int FB_SIGN_IN = 2;
    private static final String TAG = "Login";
    private FirebaseAuth mAuth;
    TextView goToRegister;
    EditText edtEmail, edtPassword;
    Button btnLogin;
    ImageButton btnGoogle, btnFacebook;
    CallbackManager mCallbackManager;
    LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

        goToRegister = findViewById(R.id.txtRegister);
        edtEmail = findViewById(R.id.login_edtUsername);
        edtPassword = findViewById(R.id.login_edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);



        goToRegister.setOnClickListener(view ->  {
                Intent registerIntent = new Intent(Login.this, Register.class);
                startActivity(registerIntent);
            }
        );

        btnLogin.setOnClickListener(view -> {
            onLoginClick(view);
        });

        btnGoogle.setOnClickListener(view ->{
            onGoogleLogin(view);
        });

        btnFacebook.setOnClickListener(view -> {
            onFacebookLogin(view);
            loginManager.logInWithReadPermissions(Login.this, Arrays.asList("public_profile", "email"));
//            onFacebookLogin(view);
        });
    }

    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                Toast.makeText(Login.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                goToMain();
            }else{
                Toast.makeText(Login.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
            }
        });
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

    public void onGoogleLogin(View v){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent googleSignInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent, GG_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: Request Code: "+requestCode);
        Log.d(TAG, "onActivityResult: Result Code: "+resultCode);
        if (requestCode == GG_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign in was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e){
                Log.w(TAG, "Google sign in failed", e);
            }
        }
//        else if(requestCode == FB_SIGN_IN) {
//            mCallbackManager.onActivityResult(requestCode, resultCode, data);
//        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        goToMain();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }

    public void onFacebookLogin(View v){
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess: " + loginResult);
                Toast.makeText(Login.this, "facebook: onSuccess", Toast.LENGTH_SHORT).show();
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook: onCancel");
                Toast.makeText(Login.this, "on Cancel", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook: onError", error);
                Toast.makeText(Login.this, "On Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void firebaseAuthWithFacebook(AccessToken token){
        Log.d(TAG, "firebaseAuthWithFacebook: " + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "Sign In With Credential: success");
                    goToMain();
                }else{
                    Log.w(TAG, "Sign In With Credential: failure", task.getException());
                    Toast.makeText(Login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}