package com.phuquytran_300303518.moneysaver.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phuquytran_300303518.moneysaver.Fragments.ArchievementFragment;
import com.phuquytran_300303518.moneysaver.Fragments.PlanFragment;
import com.phuquytran_300303518.moneysaver.Fragments.ReportFragment;
import com.phuquytran_300303518.moneysaver.Fragments.TransactionFragment;
import com.phuquytran_300303518.moneysaver.R;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TransactionFragment transactionFragment = new TransactionFragment();
    PlanFragment planFragment = new PlanFragment();
    ReportFragment reportFragment = new ReportFragment();
    ArchievementFragment archievementFragment = new ArchievementFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigatin_view);


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.transaction:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, transactionFragment).commit();
                    return true;
                case R.id.plan:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, planFragment).commit();
                    return true;
                case R.id.report:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, reportFragment).commit();
                    return true;
                case R.id.archievement:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, archievementFragment).commit();
                    return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.transaction);

    }



}