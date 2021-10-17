package com.phuquytran_300303518.moneysaver.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.phuquytran_300303518.moneysaver.R;

public class AchievementFragment extends Fragment {
    TextView txtSaving, txtShoes, txtCar, txtHouse, txtPhone;
    int saving = 50000;

    public AchievementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievement, container, false);

        txtSaving = view.findViewById(R.id.txtSaving);
        txtSaving.setText(saving + "$");

        txtShoes = view.findViewById(R.id.txtShoes);
        if (saving >= 200){
            txtShoes.setText("New shoes unlocked");
        } else {
            txtShoes.setText("Unlocking " + ((int)(saving/200.0*100)) + "% new shoes");
        }

        txtPhone = view.findViewById(R.id.txtPhone);
        if (saving >= 600){
            txtPhone.setText("New phone unlocked");
        } else {
            txtPhone.setText("Unlocking " + ((int)(saving/600.0*100))+ "% new phone");
        }

        txtCar = view.findViewById(R.id.txtCar);
        if (saving >= 50000){
            txtCar.setText("New car unlocked");
        } else {
            txtCar.setText("Unlocking " + ((int)(saving/50000.0*100)) + "% new car");
        }

        txtHouse = view.findViewById(R.id.txtHouse);
        if (saving >= 1000000){
            txtHouse.setText("New house unlocked");
        } else {
            txtHouse.setText("Unlocking " + ((int) (saving/1000000.0*100)) + "% new house");
        }

        // Inflate the layout for this fragment
        return view;
    }
}