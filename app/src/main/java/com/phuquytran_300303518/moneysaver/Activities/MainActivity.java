package com.phuquytran_300303518.moneysaver.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phuquytran_300303518.moneysaver.Entities.Transaction;
import com.phuquytran_300303518.moneysaver.Fragments.AchievementFragment;
import com.phuquytran_300303518.moneysaver.Fragments.PlanFragment;
import com.phuquytran_300303518.moneysaver.Fragments.ReportFragment;
import com.phuquytran_300303518.moneysaver.Fragments.TransactionFragment;
import com.phuquytran_300303518.moneysaver.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TransactionFragment transactionFragment;
    PlanFragment planFragment;
    ReportFragment reportFragment;
    AchievementFragment archievementFragment;
    FragmentManager fragmentManager;
    List<Transaction> transactions;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    TextView txtUsername;

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
        txtUsername = navHeader.findViewById(R.id.nav_header_textView);
        setUpNavigation();
        txtUsername.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        bottomNavigationView = findViewById(R.id.bottom_navigatin_view);
        fragmentManager = getSupportFragmentManager();
        transactionFragment = new TransactionFragment(fragmentManager);
        planFragment = new PlanFragment();
        reportFragment = new ReportFragment();
        archievementFragment = new AchievementFragment();

//        toolbar = findViewById(R.id.toolbar);

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
                case R.id.archievement:
                    fragmentManager.beginTransaction().replace(R.id.nav_fragment, archievementFragment).commit();
                    return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.transaction);
//                View view = bottomNavigationView.findViewById(R.id.transaction);//                view.performClick();
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

                    break;
                case R.id.navItem_settings:

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
}