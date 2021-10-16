package com.phuquytran_300303518.moneysaver.Activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phuquytran_300303518.moneysaver.Fragments.ArchievementFragment;
import com.phuquytran_300303518.moneysaver.Fragments.PlanFragment;
import com.phuquytran_300303518.moneysaver.Fragments.ReportFragment;
import com.phuquytran_300303518.moneysaver.Fragments.TransactionFragment;
import com.phuquytran_300303518.moneysaver.R;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TransactionFragment transactionFragment;
    PlanFragment planFragment;
    ReportFragment reportFragment;
    ArchievementFragment archievementFragment;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigatin_view);
        fragmentManager = getSupportFragmentManager();
        transactionFragment = new TransactionFragment(fragmentManager);
        planFragment = new PlanFragment();
        reportFragment = new ReportFragment();
        archievementFragment = new ArchievementFragment();

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
                case R.id.add:
                    Toast.makeText(this, "Add ", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.transaction);

    }



}