package com.phuquytran_300303518.moneysaver.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phuquytran_300303518.moneysaver.Entities.Transaction;
import com.phuquytran_300303518.moneysaver.R;

import java.util.ArrayList;
import java.util.List;

public class AchievementFragment extends Fragment {
    TextView txtSaving, txtShoes, txtCar, txtHouse, txtPhone;
    double saving = 50000;

    List<Transaction> transactions;
    FirebaseDatabase database;
    DatabaseReference databaseReference;


    public AchievementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievement, container, false);

        transactions = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(user.getUid());

        DatabaseReference transactionRef = databaseReference.child("transactions");
        transactionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactions.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Transaction transaction = dataSnapshot.getValue(Transaction.class);
                    transactions.add(transaction);
                }

                saving = Double.parseDouble(TransactionFragment.getInFlow(transactions)) - Double.parseDouble(TransactionFragment.getOutFlow(transactions));
                txtSaving = view.findViewById(R.id.txtSaving);
                txtSaving.setText(String.format("%.2f",saving) + "$");

                txtShoes = view.findViewById(R.id.txtShoes);
                if (saving >= 200) {
                    txtShoes.setText("New shoes unlocked");
                } else {
                    txtShoes.setText("Unlocking " + ((int) (saving / 200.0 * 100)) + "% new shoes");
                }

                txtPhone = view.findViewById(R.id.txtPhone);
                if (saving >= 600) {
                    txtPhone.setText("New phone unlocked");
                } else {
                    txtPhone.setText("Unlocking " + ((int) (saving / 600.0 * 100)) + "% new phone");
                }

                txtCar = view.findViewById(R.id.txtCar);
                if (saving >= 50000) {
                    txtCar.setText("New car unlocked");
                } else {
                    txtCar.setText("Unlocking " + ((int) (saving / 50000.0 * 100)) + "% new car");
                }

                txtHouse = view.findViewById(R.id.txtHouse);
                if (saving >= 1000000) {
                    txtHouse.setText("New house unlocked");
                } else {
                    txtHouse.setText("Unlocking " + ((int) (saving / 1000000.0 * 100)) + "% new house");
                }
            }
                @Override
                public void onCancelled (@NonNull DatabaseError error){

                }
            });

        // Inflate the layout for this fragment
        return view;
    }


        }