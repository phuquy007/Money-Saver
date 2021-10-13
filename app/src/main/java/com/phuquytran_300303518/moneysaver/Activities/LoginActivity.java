package com.phuquytran_300303518.moneysaver.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import com.phuquytran_300303518.moneysaver.R;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private static final int GG_SIGN_IN = 3;
    private static final String TAG = "LoginActivity";
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

        //Initial Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        //Initialize facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

        goToRegister = findViewById(R.id.txtRegister);
        edtEmail = findViewById(R.id.login_edtUsername);
        edtPassword = findViewById(R.id.login_edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);


        //Go to register activity
        goToRegister.setOnClickListener(view ->  {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        );

        //on Button Login Click
        btnLogin.setOnClickListener(view -> {
            onLoginClick(view);
        });

        //on button Google Click
        btnGoogle.setOnClickListener(view ->{
            onGoogleLogin(view);
        });

        //on button Facebook Click
        btnFacebook.setOnClickListener(view -> {
            onFacebookLogin(view);
            loginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
        });
    }

    //Login with email and password through firebase authentication
    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                goToMain();
            }else{
                Toast.makeText(LoginActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //To to Main Activity
    public void goToMain(){
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    //Validate the email and password before login, add more later
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

    //on Google Login, start the Google Sign In Activity and get back the result
    public void onGoogleLogin(View v){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent googleSignInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent, GG_SIGN_IN);
    }

    //Get the result back from Google Sign in, If success, send the token to firebase login with token
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GG_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign in was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());
                firebaseAuthenticationWithToken(account.getIdToken());
            } catch (ApiException e){
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    //On Facebook Login, start the facebook login manager, if users sign in successful, get access token and pass to firebase login with token
    public void onFacebookLogin(View v){
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess: " + loginResult);
                Toast.makeText(LoginActivity.this, "facebook: onSuccess", Toast.LENGTH_SHORT).show();
                firebaseAuthenticationWithToken(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook: onCancel");
                Toast.makeText(LoginActivity.this, "on Cancel", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook: onError", error);
                Toast.makeText(LoginActivity.this, "On Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Firebase sign in with google, use the token from google to login
    private void firebaseAuthenticationWithToken(String idToken) {
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
}