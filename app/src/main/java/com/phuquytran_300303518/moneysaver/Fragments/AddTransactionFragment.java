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

import com.phuquytran_300303518.moneysaver.Entities.Transaction;
import com.phuquytran_300303518.moneysaver.R;

public class AddTransactionFragment extends Fragment {
    FragmentManager fragmentManager;
    EditText edtTransactionTitle, edtTransactionAmount, edtTransactionDescription;
    Spinner spnTransactionType;
    Button btnAdd;

    //Dummy data, replace later
    String[] transactionTypes;

    public AddTransactionFragment() {
        //dummy type, replace later

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
        Transaction newTransaction = new Transaction(title, type, amount, description);

        //Back to transaction Fragment
        fragmentManager.beginTransaction().replace(R.id.nav_fragment, new TransactionFragment(fragmentManager)).commit();
    }
}