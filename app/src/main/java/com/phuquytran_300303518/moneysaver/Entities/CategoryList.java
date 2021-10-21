package com.phuquytran_300303518.moneysaver.Entities;

import com.phuquytran_300303518.moneysaver.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryList {
    private static List<Category> categories;

    private CategoryList(){
        categories = new ArrayList<>();
        Category entertainment = new Category("entertainment", "Entertainment", R.drawable.entertainment);
        Category food = new Category("food", "Food", R.drawable.food);
        Category income = new Category("income", "Income", R.drawable.income);

        categories.add(entertainment);
        categories.add(food);
        categories.add(income);
    }

    public static List<Category> getInstance(){
        if (categories == null){
            new CategoryList();
        }
        return categories;
    }

    public static Category getCategory(String categoryID) {
        for(Category category: categories){
            if (category.getCategoryID().compareTo(categoryID) == 0){
                return category;
            }
        }
        return new Category();
    }
}
