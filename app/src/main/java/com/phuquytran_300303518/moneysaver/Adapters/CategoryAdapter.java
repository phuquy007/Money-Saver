package com.phuquytran_300303518.moneysaver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuquytran_300303518.moneysaver.Entities.Category;
import com.phuquytran_300303518.moneysaver.Interfaces.OnCategoryClickListener;
import com.phuquytran_300303518.moneysaver.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categories;
    OnCategoryClickListener onCategoryClickListener;
    Context context;

    public CategoryAdapter(List<Category> categories, Context context, OnCategoryClickListener onCategoryClickListener){
        this.categories = categories;
        this.context = context;
        this.onCategoryClickListener = onCategoryClickListener;
    }
    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);

        holder.imgLogo.setImageResource(category.getLogo());
        holder.txtCategoryName.setText(category.getCategoryName());

        holder.itemView.setOnClickListener(view -> {
            onCategoryClickListener.onCategoryClick(category);
        });
    }

    @Override
    public int getItemCount() {
        if (categories != null && !categories.isEmpty())
            return categories.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgLogo;
        TextView txtCategoryName;

        public ViewHolder(@NonNull View v) {
            super(v);
            imgLogo = v.findViewById(R.id.item_category_img);
            txtCategoryName = v.findViewById(R.id.item_category_Name);
        }
    }


}
