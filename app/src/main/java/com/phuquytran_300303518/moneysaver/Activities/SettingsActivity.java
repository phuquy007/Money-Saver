package com.phuquytran_300303518.moneysaver.Activities;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phuquytran_300303518.moneysaver.R;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {
    Switch swtReminder, swtLimit;
    TextView txtReminder, txtLimit;
    String isReminder, isLimit;
    String reminderTime, limitAmount;
    Button btnSettings_Save;

    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        swtReminder = findViewById(R.id.swtReminder);
        swtLimit = findViewById(R.id.swtLimit);
        txtReminder = findViewById(R.id.txtReminder);
        txtLimit = findViewById(R.id.txtLimit);

        btnSettings_Save = findViewById(R.id.btnSettings_Save);

        getReminderAndLimit();

        swtReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    txtReminder.setVisibility(View.VISIBLE);
                }else{
                    txtReminder.setVisibility(View.INVISIBLE);
                }
            }
        });

        txtReminder.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            timePickerDialog = new TimePickerDialog(SettingsActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int sHour, int sMinute) {
                            txtReminder.setText(sHour + ":" + sMinute);

                            mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("isReminder").setValue("Yes");
                            mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("reminderTime").setValue(sHour + ":" + sMinute);
                        }
                    }, hour, minutes, true);
            timePickerDialog.show();
        });

        swtLimit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    txtLimit.setVisibility(View.VISIBLE);
                }else{
                    txtLimit.setVisibility(View.INVISIBLE);

                }
            }
        });

        txtLimit.setOnClickListener(view -> {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.new_limit_dialog, null);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("New Value");
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            final EditText newLimit = alertLayout.findViewById(R.id.edt_newLimit);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newStringValue = newLimit.getText().toString();
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("isLimit").setValue("Yes");
                    mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("limitAmount").setValue(newStringValue);
                    txtLimit.setText(newStringValue+"$");
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();
        });


        btnSettings_Save.setOnClickListener(view -> {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("isReminder").setValue(swtReminder.isChecked()?"Yes":"No");
            mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("isLimit").setValue(swtLimit.isChecked()?"Yes":"No");
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    public void getReminderAndLimit(){
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

                    if(isReminder.compareTo("Yes") == 0){
                        swtReminder.setChecked(true);
                        txtReminder.setVisibility(View.VISIBLE);
                        txtReminder.setText(reminderTime);
                    }else {
                        swtReminder.setChecked(false);
                        txtReminder.setVisibility(View.INVISIBLE);
                    }
                }catch (Exception e){
                    swtReminder.setChecked(false);
                    txtReminder.setVisibility(View.INVISIBLE);
                }

                try {
                    isLimit = snapshot.child("isLimit").getValue(String.class);
                    limitAmount = snapshot.child("limitAmount").getValue(String.class);

                    if(isLimit.compareTo("Yes") == 0){
                        swtLimit.setChecked(true);
                        txtLimit.setVisibility(View.VISIBLE);
                        txtLimit.setText(limitAmount+"$");
                    }else {
                        swtLimit.setChecked(false);
                        txtLimit.setVisibility(View.INVISIBLE);
                    }
                }catch(Exception e){
                    swtLimit.setChecked(false);
                    txtLimit.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}