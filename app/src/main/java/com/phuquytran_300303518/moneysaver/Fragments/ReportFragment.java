package com.phuquytran_300303518.moneysaver.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.components.Legend;
import com.phuquytran_300303518.moneysaver.R;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;

public class ReportFragment extends Fragment {

    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    ArrayList PieEntryLabels;
    int[] colorClassArray = new int[]{Color.LTGRAY, Color.DKGRAY, Color.MAGENTA, Color.RED};

    public ReportFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the transaction fragment view
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        // link with the pie chart in XML layout file
        pieChart = view.findViewById(R.id.pieChart);
        getEntries();

        // create Dataset object, and set colors of each pie slice
        pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(colorClassArray);

        // set up data for the pie chart
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        // customize the pie chart
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);
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

        // Inflate the layout for this fragment
        return view;
    }

    // method to set up data values + label for the chart
    private void getEntries() {
        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(0.30f, "Foods"));
        pieEntries.add(new PieEntry(0.20f, "Transportation"));
        pieEntries.add(new PieEntry(0.25f, "Renting"));
        pieEntries.add(new PieEntry(0.25f, "Clothes"));
    }
}