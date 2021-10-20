package com.phuquytran_300303518.moneysaver.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    private static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(user.getUid());

//        transactions = new ArrayList<>();
        bottomNavigationView = findViewById(R.id.bottom_navigatin_view);
        fragmentManager = getSupportFragmentManager();
        transactionFragment = new TransactionFragment(fragmentManager);
        planFragment = new PlanFragment();
        reportFragment = new ReportFragment();
        archievementFragment = new AchievementFragment();

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
}