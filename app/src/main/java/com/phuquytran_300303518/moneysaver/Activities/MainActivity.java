package com.phuquytran_300303518.moneysaver.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.phuquytran_300303518.moneysaver.Entities.NotifyReceiver;
import com.phuquytran_300303518.moneysaver.Entities.Transaction;
import com.phuquytran_300303518.moneysaver.Fragments.AchievementFragment;
import com.phuquytran_300303518.moneysaver.Fragments.PlanFragment;
import com.phuquytran_300303518.moneysaver.Fragments.ReportFragment;
import com.phuquytran_300303518.moneysaver.Fragments.TransactionFragment;
import com.phuquytran_300303518.moneysaver.R;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TransactionFragment transactionFragment;
    PlanFragment planFragment;
    ReportFragment reportFragment;
    AchievementFragment achievementFragment;
    FragmentManager fragmentManager;
    List<Transaction> transactions;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    TextView txtUsername;
    TextView txtDisplayName;
    ImageView imgAvatar;

    String isReminder, reminderTime;

    private static final String TAG = "Main Activity";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(user.getUid());

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navView = findViewById(R.id.nav_view);
        View navHeader = navView.getHeaderView(0);
        txtDisplayName = navHeader.findViewById(R.id.txtDisplayName);
        imgAvatar = navHeader.findViewById(R.id.nav_header_imageView);
        txtUsername = navHeader.findViewById(R.id.txtDisplayName);
        setUpNavigation();
        txtUsername.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        bottomNavigationView = findViewById(R.id.bottom_navigatin_view);
        fragmentManager = getSupportFragmentManager();
        transactionFragment = new TransactionFragment(fragmentManager);
        planFragment = new PlanFragment();
        reportFragment = new ReportFragment();
        achievementFragment = new AchievementFragment();

//        toolbar = findViewById(R.id.toolbar);

        getUser();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.transaction:
                    fragmentManager.beginTransaction().replace(R.id.nav_fragment, transactionFragment).commit();
                    return true;
                case R.id.plan:
                    fragmentManager.beginTransaction().replace(R.id.nav_fragment, planFragment).commit();
                    return true;
                case R.id.report:
                    fragmentManager.beginTransaction().replace(R.id.nav_fragment, reportFragment).commit();
                    return true;
                case R.id.achievement:
                    fragmentManager.beginTransaction().replace(R.id.nav_fragment, achievementFragment).commit();
                    return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.transaction);
//                View view = bottomNavigationView.findViewById(R.id.transaction);//                view.performClick();

        checkReminder();
    }

    private void setUpNavigation() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        // Add more space to account for status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            toolbar.setOnApplyWindowInsetsListener((v, insets) -> {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();

                // If we used topMargin instead of leftMargin here, the topMargin will be increased
                // each time the keyboard is closed.
                layoutParams.topMargin = layoutParams.leftMargin + insets.getSystemWindowInsetTop();
                return insets;
            });
        }

        // Add the hamburger button
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);

        // Need to call this function to change the back icon into the hamburger icon
        toggle.syncState();

        // Add listener for pressing navigation items
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navItem_home:
                    goToMainActivity();
                    break;
                case R.id.navItem_profile:
                    goToProfileActivity();
                    break;
                case R.id.navItem_settings:
                    goToSettingsActivity();
                    break;
                case R.id.navItem_logOut:
                    logOut();
                    break;
            }
            return true;
        });
    }

    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        finish();
        startActivity(loginIntent);
    }

    public void goToMainActivity(){
        finish();
        startActivity(getIntent());
    }

    public void goToSettingsActivity(){
        Intent settingsActivity = new Intent(this, SettingsActivity.class);
        startActivity(settingsActivity);
    }

    public void goToProfileActivity(){
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        startActivity(profileIntent);
    }

    private void getUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            txtDisplayName.setText("Not logged in");
            return;
        }

        FirebaseDatabase.getInstance().getReference().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                txtDisplayName.setText(snapshot.child("firstName").getValue(String.class) + " " + snapshot.child("lastName").getValue(String.class));

                String avatarLink = snapshot.child("avatar").getValue(String.class);
                if (avatarLink == null)
                    return;

                StorageReference storageReference = FirebaseStorage.getInstance().getReference("avatars");
                Glide.with(MainActivity.this)
                        .load(storageReference.child(avatarLink))
                        .placeholder(R.drawable.placeholder_square)
                        .into(imgAvatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createNotification (int hour, int minute) {
        Log.d(TAG, "createNotification: " + hour);
        Log.d(TAG, "createNotification: " + minute);
        Intent myIntent = new Intent(MainActivity.this , NotifyReceiver.class ) ;
        AlarmManager alarmManager = (AlarmManager) this.getSystemService( ALARM_SERVICE ) ;
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (MainActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar. getInstance ();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP , calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pendingIntent);
        Log.d(TAG, "Done Notification: " );
    }

    public void checkReminder(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }

        FirebaseDatabase.getInstance().getReference().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    isReminder = snapshot.child("isReminder").getValue(String.class);
                    reminderTime = snapshot.child("reminderTime").getValue(String.class);

                    String[] temp = reminderTime.split(":");
                    int hour = Integer.valueOf(temp[0]);
                    int minute = Integer.valueOf(temp[1]);

                    if(isReminder.compareTo("Yes") == 0){
                        createNotification(hour, minute);
                    }else {

                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}