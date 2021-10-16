package com.phuquytran_300303518.moneysaver.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.phuquytran_300303518.moneysaver.Adapters.TransactionAdapter;
import com.phuquytran_300303518.moneysaver.Entities.Transaction;
import com.phuquytran_300303518.moneysaver.R;

import java.util.ArrayList;
import java.util.List;

public class TransactionFragment extends Fragment {
    private RecyclerView rcv_transactions;
    TextView txtTransaction_inflow, txtTransaction_outflow;
    FloatingActionButton fabTransaction_addTransaction;
    List<Transaction> transactions;
    FragmentManager fragmentManager;
    public TransactionFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        //Add a dummy list to the transaction
        //Later on, get data from firebase
        transactions = new ArrayList<>();
        Transaction dummy1 = new Transaction("transaction1 ","income", 100.0);
        Transaction dummy2 = new Transaction("transaction2","expense", -100.0);
        transactions.add(dummy1);
        transactions.add(dummy2);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the transaction fragment view
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        txtTransaction_inflow = view.findViewById(R.id.txtTransaction_inFlow);
        txtTransaction_outflow = view.findViewById(R.id.txtTransaction_outFlow);
        fabTransaction_addTransaction = view.findViewById(R.id.fabTransaction_newTransaction);

        //Start the new Add Transaction Fragment
        fabTransaction_addTransaction.setOnClickListener(v -> {
            fragmentManager.beginTransaction().replace(R.id.nav_fragment, new AddTransactionFragment(fragmentManager)).commit();
        });

        txtTransaction_inflow.setText("+"+Double.toString(getInFlow()));
        txtTransaction_outflow.setText(Double.toString(getOutFlow()));

        //Set up the fragment, set layoutmanager and adapter
        rcv_transactions = view.findViewById(R.id.rcv_transactions);
        rcv_transactions.setHasFixedSize(true);
        TransactionAdapter transactionAdapter = new TransactionAdapter(transactions);
        rcv_transactions.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rcv_transactions.setAdapter(transactionAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    private double getInFlow(){
        double result = 0.0;
        if (transactions.size() > 0){
            for(int i = 0; i < transactions.size(); i++){
                Transaction transaction = transactions.get(i);
                if (transaction.getTransactionAmount() > 0){
                    result += transaction.getTransactionAmount();
                }
            }
        }
        return  result;
    }

    private double getOutFlow(){
        double result = 0.0;
        if (transactions.size() > 0){
            for(int i = 0; i < transactions.size(); i++){
                Transaction transaction = transactions.get(i);
                if (transaction.getTransactionAmount() < 0){
                    result += transaction.getTransactionAmount();
                }
            }
        }
        return  result;
    }
}