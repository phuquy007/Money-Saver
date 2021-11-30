package com.phuquytran_300303518.moneysaver.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phuquytran_300303518.moneysaver.Entities.Transaction;
import com.phuquytran_300303518.moneysaver.Enum.TransactionType;
import com.phuquytran_300303518.moneysaver.Fragments.TransactionFragment;
import com.phuquytran_300303518.moneysaver.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecommendActivity extends AppCompatActivity {
    List<Transaction> transactions;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    double currentIncome = 0, inputIncome = 0;
    double currentNecessity = 0, recommendedNecessity = 0;
    double currentPlay = 0, recommendedPlay = 0;
    double currentInvestment = 0, recommendedInvestment = 0;
    double currentEducation = 0, recommendedEducation = 0;
    double currentSaving = 0, recommendedSaving = 0;
    double currentGive = 0, recommendedGive = 0;

    EditText incomeEditTxt;
    TextView necessityPlan, playPlan, investmentPlan, educationPlan, savingPlan, givePlan;
    TextView necessityActual, playActual, investmentActual, educationActual, savingActual, giveActual;
    Button btnCalculate, btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        transactions = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(user.getUid());

        DatabaseReference transactionRef = databaseReference.child("transactions");
        transactionRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactions.clear();

                // add all transactions of this month to array
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Date today = Calendar.getInstance().getTime();
                    int year = today.getYear()+1900;
                    int month = today.getMonth() + 1;
                    Transaction transaction = dataSnapshot.getValue(Transaction.class);
                    if(year == transaction.getDate().getYear() && month == transaction.getDate().getMonth()) {
                        transactions.add(transaction);
                    }
                }

                // calculate the users' expenses this month based on the transactional data in database
                getCurrentExpenses(transactions);

                incomeEditTxt = findViewById(R.id.incomeEditText);

                necessityPlan = findViewById(R.id.necessityPlanValue);
                playPlan = findViewById(R.id.playPlanValue);
                investmentPlan = findViewById(R.id.investmentPlanValue);
                educationPlan = findViewById(R.id.educationPlanValue);
                savingPlan = findViewById(R.id.savingPlanValue);
                givePlan = findViewById(R.id.givePlanValue);

                necessityActual = findViewById(R.id.necessityActualValue);
                playActual = findViewById(R.id.playActualValue);
                investmentActual = findViewById(R.id.investmentActualValue);
                educationActual = findViewById(R.id.educationActualValue);
                savingActual = findViewById(R.id.savingActualValue);
                giveActual = findViewById(R.id.giveActualValue);

                // display all current expenses and the current income this month on the screen
                incomeEditTxt.setText(String.format("%.2f", currentIncome));
                necessityActual.setText(String.format("%.2f", currentNecessity) + "$");
                playActual.setText(String.format("%.2f", currentPlay) + "$");
                investmentActual.setText(String.format("%.2f", currentInvestment) + "$");
                educationActual.setText(String.format("%.2f", currentEducation) + "$");
                savingActual.setText(String.format("%.2f", currentSaving) + "$");
                giveActual.setText(String.format("%.2f", currentGive) + "$");

                // calculate 6 expenses in the recommended plan based on 6 jars system
                recommendedNecessity = currentIncome*0.55;
                recommendedPlay = currentIncome*0.10;
                recommendedInvestment = currentIncome*0.10;
                recommendedEducation = currentIncome*0.10;
                recommendedSaving = currentIncome*0.10;
                recommendedGive = currentIncome*0.05;

                // display all figures in the recommended financial plan
                necessityPlan.setText(String.format("%.2f", recommendedNecessity) + "$");
                playPlan.setText(String.format("%.2f", recommendedPlay) + "$");
                investmentPlan.setText(String.format("%.2f", recommendedInvestment) + "$");
                educationPlan.setText(String.format("%.2f", recommendedEducation) + "$");
                savingPlan.setText(String.format("%.2f", recommendedSaving) + "$");
                givePlan.setText(String.format("%.2f", recommendedGive) + "$");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnCalculate = findViewById(R.id.btnCalculate);
        btnCalculate.setOnClickListener(view -> {
            inputIncome = Double.parseDouble(incomeEditTxt.getText().toString());

            // calculate the plan again based on the input income entered by the user
            recommendedNecessity = inputIncome*0.55;
            recommendedPlay = inputIncome*0.10;
            recommendedInvestment = inputIncome*0.10;
            recommendedEducation = inputIncome*0.10;
            recommendedSaving = inputIncome*0.10;
            recommendedGive = inputIncome*0.05;

            // display all figures in the recommended financial plan
            necessityPlan.setText(String.format("%.2f", recommendedNecessity) + "$");
            playPlan.setText(String.format("%.2f", recommendedPlay) + "$");
            investmentPlan.setText(String.format("%.2f", recommendedInvestment) + "$");
            educationPlan.setText(String.format("%.2f", recommendedEducation) + "$");
            savingPlan.setText(String.format("%.2f", recommendedSaving) + "$");
            givePlan.setText(String.format("%.2f", recommendedGive) + "$");
        });

        btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(view -> {
            finish();
        });
    }

    private void getCurrentExpenses(List<Transaction> transactions){
        if (transactions != null && !transactions.isEmpty()){
            for(int i = 0; i < transactions.size(); i++){
                Transaction transaction = transactions.get(i);
                switch (transaction.getCategory().getCategoryID()) {
                    case "income":
                        currentIncome += transaction.getTransactionAmount();
                        break;
                    case "entertainment":
                    case "travel":
                        currentPlay += transaction.getTransactionAmount();
                        break;
                    case "investment":
                        currentInvestment += transaction.getTransactionAmount();
                        break;
                    case "education":
                        currentEducation += transaction.getTransactionAmount();
                        break;
                    case "gift":
                        currentGive += transaction.getTransactionAmount();
                        break;
                    default:
                        currentNecessity += transaction.getTransactionAmount();
                }

                currentSaving = currentIncome - currentNecessity - currentPlay - currentInvestment - currentEducation - currentGive;
            }
        }
    }
}