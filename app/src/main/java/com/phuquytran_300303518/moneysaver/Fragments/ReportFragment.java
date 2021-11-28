package com.phuquytran_300303518.moneysaver.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.components.Legend;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phuquytran_300303518.moneysaver.Entities.Category;
import com.phuquytran_300303518.moneysaver.Entities.CategoryList;
import com.phuquytran_300303518.moneysaver.Entities.Transaction;
import com.phuquytran_300303518.moneysaver.Enum.TransactionType;
import com.phuquytran_300303518.moneysaver.R;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportFragment extends Fragment {

    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    int[] colorClassArray = new int[]{Color.LTGRAY, Color.DKGRAY, Color.MAGENTA, Color.RED};

    List<Transaction> transactions;
    List<Transaction> filteredTransactions;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    TabLayout tabLayout;

    public ReportFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the report fragment view
        View view = inflater.inflate(R.layout.fragment_report, container, false);

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

                tabLayout = view.findViewById(R.id.reportTabLayout);
                filterTransactions(transactions, "Today");

                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        filterTransactions(transactions, tab.getText().toString());
                        updatePieChart(view);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        updatePieChart(view);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        filterTransactions(transactions, tab.getText().toString());
                        updatePieChart(view);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    // method to set up data values + label for the chart
    private void getEntries(List<Transaction> transactions) {
        pieEntries = new ArrayList<>();
        float food = 0;
        float transport = 0;
        float housing = 0;
        float others = 0;
        float expenses = Float.parseFloat(TransactionFragment.getOutFlow(transactions));

        if (transactions != null && !transactions.isEmpty()){
            for(int i = 0; i < transactions.size(); i++){
                Transaction transaction = transactions.get(i);
                switch (transaction.getCategory().getCategoryID()) {
                    case "mortgage":
                    case "rentals":
                        housing += transaction.getTransactionAmount();
                        break;
                    case "gas":
                    case "transport":
                        transport += transaction.getTransactionAmount();
                        break;
                    case "food":
                    case "beverage":
                        food += transaction.getTransactionAmount();
                        break;
                    case "income":
                        break;
                    default:
                        others += transaction.getTransactionAmount();
                }
            }
        }

        pieEntries.add(new PieEntry(food, "Foods"));
        pieEntries.add(new PieEntry(transport, "Transportation"));
        pieEntries.add(new PieEntry(housing, "Housing"));
        pieEntries.add(new PieEntry(others, "Others"));
    }

    private void updatePieChart(View view){
        // link with the pie chart in XML layout file
        pieChart = view.findViewById(R.id.pieChart);
        getEntries(filteredTransactions);

        // create Dataset object, and set colors of each pie slice
        pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(colorClassArray);

        // set up data for the pie chart
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        // customize the pie chart
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(20f);
        pieDataSet.setSliceSpace(5f);
        pieDataSet.setDrawValues(false); // remove values from chart

        // customize the legend
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(Color.BLUE);
        legend.setTextSize(20);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(30);
        legend.setXEntrySpace(50);
        legend.setFormToTextSpace(10);
        legend.setWordWrapEnabled(true);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setYEntrySpace(50);
        legend.setYOffset(10);

        // remove the description label + entry label from the chart
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);

        pieChart.invalidate();
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