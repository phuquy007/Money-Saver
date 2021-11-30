package com.phuquytran_300303518.moneysaver.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phuquytran_300303518.moneysaver.Activities.AddPlanPaymentActivity;
import com.phuquytran_300303518.moneysaver.Activities.RecommendActivity;
import com.phuquytran_300303518.moneysaver.Adapters.TransactionAdapter;
import com.phuquytran_300303518.moneysaver.Entities.Transaction;
import com.phuquytran_300303518.moneysaver.Enum.TransactionType;
import com.phuquytran_300303518.moneysaver.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionFragment extends Fragment {
    private static final String TAG = "Transaction Fragment";
    private RecyclerView rcv_transactions;
    TextView txtTransaction_inflow, txtTransaction_outflow;
    FloatingActionButton fabTransaction_addTransaction;
    ImageButton btnDelete;
    TabLayout tabLayout;
    Button btnHints;

    List<Transaction> transactions;
    List<Transaction> filteredTransactions;
    FragmentManager fragmentManager;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    public TransactionFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
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

        transactions = new ArrayList<>();
        filteredTransactions = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(user.getUid());

        DatabaseReference transactionRef = databaseReference.child("transactions");
        transactionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactions.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Transaction transaction = dataSnapshot.getValue(Transaction.class);
                    transactions.add(transaction);
                }

                txtTransaction_inflow = view.findViewById(R.id.txtTransaction_inFlow);
                txtTransaction_outflow = view.findViewById(R.id.txtTransaction_outFlow);
                fabTransaction_addTransaction = view.findViewById(R.id.fabTransaction_newTransaction);
                btnDelete = view.findViewById(R.id.item_transactionDelete);
                tabLayout = view.findViewById(R.id.transactionTabLayout);
                filterTransactions(transactions, "Today");
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        filterTransactions(transactions, tab.getText().toString());
                        txtTransaction_inflow.setText("+" + getInFlow(filteredTransactions));
                        txtTransaction_outflow.setText("-" + getOutFlow(filteredTransactions));

                        updateUI(view);

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        updateUI(view);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        filterTransactions(transactions, tab.getText().toString());
                        txtTransaction_inflow.setText("+" + getInFlow(filteredTransactions));
                        txtTransaction_outflow.setText("-" + getOutFlow(filteredTransactions));

                        updateUI(view);
                    }
                });

                //Start the new Add Transaction Fragment
                fabTransaction_addTransaction.setOnClickListener(v -> {
                    fragmentManager.beginTransaction().replace(R.id.nav_fragment, new AddTransactionFragment(fragmentManager)).addToBackStack(null).commit();
                });

                txtTransaction_inflow.setText("+" + getInFlow(filteredTransactions));
                txtTransaction_outflow.setText("-" + getOutFlow(filteredTransactions));

                updateUI(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: Fail");
            }
        });

        btnHints = view.findViewById(R.id.btnHints);
        btnHints.setOnClickListener(view1 -> {
            Intent hintsIntent = new Intent(getContext(), RecommendActivity.class);
            startActivity(hintsIntent);
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void updateUI(View view){
        //Set up the fragment, set layoutmanager and adapter
        rcv_transactions = view.findViewById(R.id.rcv_transactions);
        rcv_transactions.setHasFixedSize(true);
        TransactionAdapter transactionAdapter = new TransactionAdapter(filteredTransactions, view.getContext());
        rcv_transactions.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rcv_transactions.setAdapter(transactionAdapter);
    }

    protected static String getInFlow(List<Transaction> transactions){
        double result = 0.0;
        if (transactions != null && !transactions.isEmpty()){
            for(int i = 0; i < transactions.size(); i++){
                Transaction transaction = transactions.get(i);
                if (transaction.getType() == TransactionType.INCOME){
                    result += transaction.getTransactionAmount();
                }
            }
        }
        return String.format("%.2f", result);
    }

    protected static String getOutFlow(List<Transaction> transactions){
        double result = 0.0;
        if (transactions != null && !transactions.isEmpty()){
            for(int i = 0; i < transactions.size(); i++){
                Transaction transaction = transactions.get(i);
                if (transaction.getType() == TransactionType.EXPENSE){
                    result += transaction.getTransactionAmount();
                }
            }
        }
        return String.format("%.2f", result);
    }

    public void filterTransactions(List<Transaction> transactions, String filter){

        filteredTransactions.clear();
        Date today = Calendar.getInstance().getTime();
        int year = today.getYear()+1900;
        int month = today.getMonth() + 1;
        int day = today.getDate();
        int thisWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
//        Toast.makeText(getContext(), "Day: " + day + " Week: "+ thisWeek + " Month: " + month + " Year: " + year, Toast.LENGTH_SHORT).show();
        switch (filter){
            case "Today":
                for (Transaction transaction: transactions) {
                    if (year == transaction.getDate().getYear() && month == transaction.getDate().getMonth() && day == transaction.getDate().getDay()) {
                        filteredTransactions.add(transaction);
                    }
                }
                break;
            case "This Week":
                for (Transaction transaction: transactions){
                    if(year == transaction.getDate().getYear() && thisWeek == transaction.getDate().getWeek()){
                        filteredTransactions.add(transaction);
                    }
                }
                break;
            case "This Month":
                for(Transaction transaction: transactions){
                    if(year == transaction.getDate().getYear() && month == transaction.getDate().getMonth()){
                        filteredTransactions.add(transaction);
                    }
                }
        }
    }
}