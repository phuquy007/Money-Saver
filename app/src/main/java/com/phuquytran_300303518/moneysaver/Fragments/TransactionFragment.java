package com.phuquytran_300303518.moneysaver.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuquytran_300303518.moneysaver.Adapters.TransactionAdapter;
import com.phuquytran_300303518.moneysaver.Entities.Transaction;
import com.phuquytran_300303518.moneysaver.R;

import java.util.ArrayList;
import java.util.List;

public class TransactionFragment extends Fragment {
    private RecyclerView rcv_transactions;
    List<Transaction> transactions;
    public TransactionFragment() {

        //Add a dummy list to the transaction
        //Later on, get data from firebase
        transactions = new ArrayList<>();
        Transaction dummy1 = new Transaction("transaction1 ", 100.0);
        Transaction dummy2 = new Transaction("transaction2", -100.0);
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

        //Set up the fragment, set layoutmanager and adapter
        rcv_transactions = view.findViewById(R.id.rcv_transactions);
        rcv_transactions.setHasFixedSize(true);
        TransactionAdapter transactionAdapter = new TransactionAdapter(transactions);
        rcv_transactions.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rcv_transactions.setAdapter(transactionAdapter);

        // Inflate the layout for this fragment
        return view;
    }
}