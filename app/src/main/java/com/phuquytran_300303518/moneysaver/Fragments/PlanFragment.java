package com.phuquytran_300303518.moneysaver.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phuquytran_300303518.moneysaver.Activities.AddPlanPaymentActivity;
import com.phuquytran_300303518.moneysaver.Adapters.PlanPaymentAdapter;
import com.phuquytran_300303518.moneysaver.Entities.PlanPayment;
import com.phuquytran_300303518.moneysaver.R;

import java.util.ArrayList;
import java.util.List;

public class PlanFragment extends Fragment {
    FloatingActionButton btnNewPlanPayment;
    RecyclerView rcvPlanPayments;
    List<PlanPayment> planPayments;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    public PlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        planPayments = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(user.getUid());

        DatabaseReference planPaymentRef = databaseReference.child("plan_payments");
        planPaymentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                planPayments.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    PlanPayment planPayment = dataSnapshot.getValue(PlanPayment.class);
                    planPayments.add(planPayment);
                }
                 updateUI(view);
                };
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnNewPlanPayment = view.findViewById(R.id.fabPlanPayment_newPlan);
        btnNewPlanPayment.setOnClickListener(view1 -> {
            Intent addPlanPaymentIntent = new Intent(getContext(), AddPlanPaymentActivity.class);
            startActivity(addPlanPaymentIntent);
        });

        return view;
    }

    private void updateUI(View view){
        //Set up the fragment, set layoutmanager and adapter
        rcvPlanPayments = view.findViewById(R.id.rcv_planPayments);
        rcvPlanPayments.setHasFixedSize(true);
        PlanPaymentAdapter planPaymentAdapter = new PlanPaymentAdapter(planPayments);
        rcvPlanPayments.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rcvPlanPayments.setAdapter(planPaymentAdapter);
    }

}