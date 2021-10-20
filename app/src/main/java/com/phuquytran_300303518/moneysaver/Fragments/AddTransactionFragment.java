package com.phuquytran_300303518.moneysaver.Fragments;

import android.app.DatePickerDialog;
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
import com.phuquytran_300303518.moneysaver.Entities.TransactionDate;
import com.phuquytran_300303518.moneysaver.Enum.TransactionType;
import com.phuquytran_300303518.moneysaver.R;

import java.util.Calendar;


public class AddTransactionFragment extends Fragment {
    FragmentManager fragmentManager;
    EditText edtTransactionTitle, edtTransactionAmount, edtTransactionDescription, getEdtTransactionDate;
    Spinner spnTransactionType;
    Button btnAdd;
    int transactionDay, transactionWeek, transactionMonth, transactionYear;

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
        getEdtTransactionDate = view.findViewById(R.id.edtAddTransaction_Date);
        spnTransactionType = view.findViewById(R.id.spnAddTransaction_Type);
        btnAdd = view.findViewById(R.id.btnAddTransaction_Add);

        getEdtTransactionDate.setOnClickListener(view1 -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(getContext(), (view2, year1, monthOfYear, dayOfMonth) -> {
                calendar.set(year1, monthOfYear, dayOfMonth);
                monthOfYear += 1;
                int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                transactionDay = dayOfMonth;
                transactionWeek = weekOfYear;
                transactionMonth = monthOfYear;
                transactionYear = year1;
                getEdtTransactionDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year1 + " week: "+weekOfYear);

            }, year, month, day);
            datePicker.show();


        });

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
        if (TextUtils.isEmpty(getEdtTransactionDate.getText())){
            getEdtTransactionDate.setError("Please select the date");
            return;
        }

        String title = edtTransactionTitle.getText().toString();
        double amount =  Double.parseDouble(edtTransactionAmount.getText().toString());
        String type = spnTransactionType.getSelectedItem().toString();
        String description = edtTransactionDescription.getText().toString();

        TransactionDate transactionDate = new TransactionDate(transactionDay, transactionWeek, transactionMonth, transactionYear);
        //Add to firebase later
        Transaction newTransaction = new Transaction(title, (type.compareTo(INCOME)==0? TransactionType.INCOME:TransactionType.EXPENSE), amount, description, transactionDate);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child(TRANSACTION).child(newTransaction.getTransactionID()).setValue(newTransaction);

        //Back to transaction Fragment
        fragmentManager.popBackStack();

    }
}