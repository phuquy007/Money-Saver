package com.phuquytran_300303518.moneysaver.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.phuquytran_300303518.moneysaver.Entities.Category;
import com.phuquytran_300303518.moneysaver.Entities.CategoryList;
import com.phuquytran_300303518.moneysaver.Entities.PlanPayment;
import com.phuquytran_300303518.moneysaver.Entities.TransactionDate;
import com.phuquytran_300303518.moneysaver.Enum.RepeatType;
import com.phuquytran_300303518.moneysaver.Enum.TransactionType;
import com.phuquytran_300303518.moneysaver.R;

import java.util.Calendar;
import java.util.UUID;

public class AddPlanPaymentActivity extends AppCompatActivity {
    EditText edtTitle, edtCategory, edtAmount, edtDate, edtDescription;
    Spinner spnTransactionType, spnRepeatType;
    Button btnAdd, btnCancel;

    static final int REQUEST_CODE = 2;
    static final String INCOME = "Income";
    static final String MONTHLY = "monthly";
    static final String PLAN_PAYMENT = "plan_payments";
    Category category;
    int transactionDay, transactionWeek, transactionMonth, transactionYear;
    String[] transactionTypes;
    String[] repeatTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan_payment);

        transactionTypes = new String[]{"Income", "Expense"};
        repeatTypes = new String[]{"One Time", "Monthly"};

        edtTitle = findViewById(R.id.edtAddPlanPayment_Title);
        edtCategory = findViewById(R.id.edtAddPlanPayment_Category);
        edtAmount = findViewById(R.id.edtAddPlanPayment_Amount);
        edtDate = findViewById(R.id.edtAddPlanPayment_Date);
        edtDescription = findViewById(R.id.edtAddPlanPayment_Description);
        spnTransactionType = findViewById(R.id.spnAddPlanPayment_TransactionType);
        spnRepeatType = findViewById(R.id.spnAddPlanPayment_RepeatType);
        btnAdd = findViewById(R.id.btnAddPlanPayment_Add);
        btnCancel = findViewById(R.id.btnAddPlanPayment_Cancel);

        edtCategory.setOnClickListener(v -> {
            Intent selectCategory = new Intent(AddPlanPaymentActivity.this, CategoryActivity.class);
            startActivityForResult(selectCategory, REQUEST_CODE);
        });

        edtDate.setOnClickListener(view1 -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(this, (view2, year1, monthOfYear, dayOfMonth) -> {
                calendar.set(year1, monthOfYear, dayOfMonth);
                monthOfYear += 1;
                int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                transactionDay = dayOfMonth;
                transactionWeek = weekOfYear;
                transactionMonth = monthOfYear;
                transactionYear = year1;
                edtDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year1);
            }, year, month, day);
            datePicker.show();
        });

        ArrayAdapter<String> transactionTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, transactionTypes);
        spnTransactionType.setAdapter(transactionTypeAdapter);

        ArrayAdapter<String> repeatTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, repeatTypes);
        spnRepeatType.setAdapter(repeatTypeAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPlanPayment();

//                Intent mainIntent = new Intent(AddPlanPaymentActivity.this, MainActivity.class);
                finish();
//                startActivity(mainIntent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(AddPlanPaymentActivity.this, MainActivity.class);
                finish();
                startActivity(mainIntent);
            }
        });
    }

    public void addNewPlanPayment(){
        PlanPayment planPayment = new PlanPayment();
        planPayment.setId(UUID.randomUUID().toString());
        planPayment.setTitle(edtTitle.getText().toString());
        String transactionType = spnTransactionType.getSelectedItem().toString();
        planPayment.setType(transactionType.compareTo(INCOME)==0? TransactionType.INCOME:TransactionType.EXPENSE);
        planPayment.setCategory(category != null?category:CategoryList.getCategory("food"));
        planPayment.setAmount(Double.valueOf(edtAmount.getText().toString()));
        TransactionDate transactionDate = new TransactionDate(transactionDay, transactionWeek, transactionMonth, transactionYear);
        planPayment.setDate(transactionDate);
        planPayment.setDescription(edtDescription.getText().toString());
        String repeatType = spnRepeatType.getSelectedItem().toString();
        planPayment.setRepeatType(repeatType.compareTo(MONTHLY)==0?RepeatType.MONTHLY: RepeatType.ONE_TIME);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child(PLAN_PAYMENT).child(planPayment.getId()).setValue(planPayment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String categoryID = data.getStringExtra("categoryID");
                category = CategoryList.getCategory(categoryID);
                edtCategory.setText(category.getCategoryName());
            }
        }
    }
}