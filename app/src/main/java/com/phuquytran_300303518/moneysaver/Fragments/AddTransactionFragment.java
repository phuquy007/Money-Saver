package com.phuquytran_300303518.moneysaver.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.phuquytran_300303518.moneysaver.Entities.Transaction;
import com.phuquytran_300303518.moneysaver.Enum.TransactionType;
import com.phuquytran_300303518.moneysaver.R;


public class AddTransactionFragment extends Fragment {
    FragmentManager fragmentManager;
    EditText edtTransactionTitle, edtTransactionAmount, edtTransactionDescription;
    Spinner spnTransactionType;
    Button btnAdd;

    public static final String INCOME = "Income";
    public static final String TRANSACTION = "transactions";

    //Dummy data, replace later
    String[] transactionTypes;

    public AddTransactionFragment() {

    }

    public AddTransactionFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public static AddTransactionFragment newInstance() {
        AddTransactionFragment fragment = new AddTransactionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionTypes = new String[]{"Income", "Expense"};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_transaction, container, false);

        edtTransactionTitle = view.findViewById(R.id.edtAddTransaction_Title);
        edtTransactionAmount = view.findViewById(R.id.edtAddTransaction_Amount);
        edtTransactionDescription = view.findViewById(R.id.edtAddTransaction_Description);
        spnTransactionType = view.findViewById(R.id.spnAddTransaction_Type);
        btnAdd = view.findViewById(R.id.btnAddTransaction_Add);

        btnAdd.setOnClickListener(v ->{
            addNewTransaction();
        });

        ArrayAdapter<String> transactionTypeAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, transactionTypes);
        spnTransactionType.setAdapter(transactionTypeAdapter);

        return view;
    }

    private void addNewTransaction(){
        if (TextUtils.isEmpty(edtTransactionTitle.getText())){
            edtTransactionTitle.setError("Please enter the title");
            return;
        }
        if (TextUtils.isEmpty(edtTransactionAmount.getText())){
            edtTransactionAmount.setError("Please enter the amount");
            return;
        }

        String title = edtTransactionTitle.getText().toString();
        double amount =  Double.parseDouble(edtTransactionAmount.getText().toString());
        String type = spnTransactionType.getSelectedItem().toString();
        String description = edtTransactionDescription.getText().toString();

        //Add to firebase later
        Transaction newTransaction = new Transaction(title, (type.compareTo(INCOME)==0? TransactionType.INCOME:TransactionType.EXPENSE), amount, description);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child(TRANSACTION).push().setValue(newTransaction);

        //Back to transaction Fragment
        fragmentManager.popBackStack();

    }
}