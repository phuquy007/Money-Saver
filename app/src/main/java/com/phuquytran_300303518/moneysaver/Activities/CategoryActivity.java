package com.phuquytran_300303518.moneysaver.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuquytran_300303518.moneysaver.Adapters.CategoryAdapter;
import com.phuquytran_300303518.moneysaver.Entities.Category;
import com.phuquytran_300303518.moneysaver.Entities.CategoryList;
import com.phuquytran_300303518.moneysaver.Interfaces.OnCategoryClickListener;
import com.phuquytran_300303518.moneysaver.R;

public class CategoryActivity extends AppCompatActivity implements OnCategoryClickListener {
    OnCategoryClickListener onCategoryClickListener;
    RecyclerView rcvCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        onCategoryClickListener = new OnCategoryClickListener() {
            @Override
            public void onCategoryClick(Category category) {

            }
        };
        rcvCategories = findViewById(R.id.rcvCategories);

        rcvCategories.setHasFixedSize(true);
        CategoryAdapter categoryAdapter = new CategoryAdapter(CategoryList.getInstance(), this, this);
        rcvCategories.setLayoutManager(new LinearLayoutManager(this));
        rcvCategories.setAdapter(categoryAdapter);
    }

    @Override
    public void onCategoryClick(Category category) {
        Intent intent = new Intent();
        intent.putExtra("categoryID", category.getCategoryID());
        setResult(RESULT_OK, intent);
        finish();
    }
}