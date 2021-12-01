package com.phuquytran_300303518.moneysaver.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.phuquytran_300303518.moneysaver.R;

public class NotificationActivity extends AppCompatActivity {
    Button btnNofifyReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        btnNofifyReminder = findViewById(R.id.btnNotifyReminder);

        btnNofifyReminder.setOnClickListener(view -> {
            Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}