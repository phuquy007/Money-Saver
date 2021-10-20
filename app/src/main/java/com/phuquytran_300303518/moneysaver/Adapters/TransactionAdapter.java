package com.phuquytran_300303518.moneysaver.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phuquytran_300303518.moneysaver.Entities.Transaction;
import com.phuquytran_300303518.moneysaver.Enum.TransactionType;
import com.phuquytran_300303518.moneysaver.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactions;
    private static final String TRANSACTION = "transactions";
    Context context;

    public TransactionAdapter(List<Transaction> transactions, Context context){
        this.transactions = transactions;
        this.context = context;
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

        holder.btnDelete.setOnClickListener(view -> {
            DialogInterface.OnClickListener dialogOnClickListener = (dialogInterface, i) -> {
                switch(i){
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteTransaction(transactions.get(position));
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Do you want to delete the Transaction?").setPositiveButton("Yes", dialogOnClickListener)
                    .setNegativeButton("No", dialogOnClickListener).show();
        });

    }

    @Override
    public int getItemCount() {
        if (transactions != null && !transactions.isEmpty())
            return transactions.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTransactionTitle, txtTransactionAmount;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View v) {
            super(v);
            txtTransactionTitle = v.findViewById(R.id.item_transactionTitle);
            txtTransactionAmount = v.findViewById(R.id.item_transactionAmount);
            btnDelete = v.findViewById(R.id.item_transactionDelete);
        }
    }

    public void deleteTransaction(Transaction deletedTransaction){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(user.getUid()).child(TRANSACTION);
        ref.child(deletedTransaction.getTransactionID()).removeValue();
        notifyDataSetChanged();
    }
}
