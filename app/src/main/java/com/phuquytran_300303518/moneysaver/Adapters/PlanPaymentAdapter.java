package com.phuquytran_300303518.moneysaver.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuquytran_300303518.moneysaver.Entities.PlanPayment;
import com.phuquytran_300303518.moneysaver.Enum.TransactionType;
import com.phuquytran_300303518.moneysaver.R;

import java.util.List;

public class PlanPaymentAdapter extends RecyclerView.Adapter<PlanPaymentAdapter.ViewHolder> {
    List<PlanPayment> planPayments;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle, txtCategory, txtRepeatType, txtAmount, txtDate;
        ImageView imgCategoryIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtPlanPayment_Name);
            txtCategory = itemView.findViewById(R.id.txtPlanPayment_Category);
            txtRepeatType = itemView.findViewById(R.id.txtPlanPayment_Repeat);
            txtAmount = itemView.findViewById(R.id.txtPlanPayment_Amount);
            txtDate = itemView.findViewById(R.id.txtPlanPayment_Date);
            imgCategoryIcon = itemView.findViewById(R.id.imgPlanPayment_categoryImg);
        }
    }

    public PlanPaymentAdapter(List<PlanPayment> planPayments) {
        this.planPayments = planPayments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_payment, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanPaymentAdapter.ViewHolder holder, int position) {
        PlanPayment planPayment = planPayments.get(position);

        holder.txtTitle.setText(planPayment.getTitle());
        holder.txtCategory.setText(planPayment.getCategory().getCategoryName());
        holder.txtRepeatType.setText(planPayment.getRepeatType().toString() + " payment");
        holder.txtDate.setText(planPayment.getDate().getDay() + "/" + planPayment.getDate().getMonth() + "/" + planPayment.getDate().getYear());
        if (planPayment.getType() == TransactionType.INCOME){
            holder.txtAmount.setTextColor(Color.GREEN);
            holder.txtAmount.setText("+"+planPayment.getAmount());
        }
        else{
            holder.txtAmount.setTextColor(Color.RED);
            holder.txtAmount.setText("-"+planPayment.getAmount());
        }
        holder.imgCategoryIcon.setImageResource(planPayment.getCategory().getLogo());
    }

    @Override
    public int getItemCount() {
        if (planPayments != null && !planPayments.isEmpty())
            return planPayments.size();
        return 0;
    }


}
