package com.phuquytran_300303518.moneysaver.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuquytran_300303518.moneysaver.Entities.Transaction;
import com.phuquytran_300303518.moneysaver.Enum.TransactionType;
import com.phuquytran_300303518.moneysaver.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactions;

    public TransactionAdapter(List<Transaction> transactions){
        this.transactions = transactions;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.txtTransactionTitle.setText(transaction.getTransactionTitle());
        if (transaction.getType() == TransactionType.INCOME){
            holder.txtTransactionAmount.setTextColor(Color.GREEN);
            holder.txtTransactionAmount.setText("+"+transaction.getTransactionAmount());
        }
        else{
            holder.txtTransactionAmount.setTextColor(Color.RED);
            holder.txtTransactionAmount.setText("-"+transaction.getTransactionAmount());
        }

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTransactionTitle, txtTransactionAmount;

        public ViewHolder(@NonNull View v) {
            super(v);
            txtTransactionTitle = v.findViewById(R.id.item_transactionTitle);
            txtTransactionAmount = v.findViewById(R.id.item_transactionAmount);
        }
    }
}
