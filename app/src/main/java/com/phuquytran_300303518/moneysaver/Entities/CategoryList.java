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
        Category clothes = new Category("cloth", "Clothes", R.drawable.cloth);
        Category beverage = new Category("beverage", "Beverage", R.drawable.drink);
        Category electricity = new Category("electricity", "Electricity Bill", R.drawable.electricity);
        Category gas = new Category("gas", "Gas Bill", R.drawable.gas);
        Category otherBills = new Category("otherBills", "Other Utility Bills", R.drawable.budget);
        Category gift = new Category("gift", "Gift", R.drawable.gift);
        Category fitness = new Category("fitness", "Fitness", R.drawable.gym);
        Category insurance = new Category("insurance", "Insurance", R.drawable.insurance);
        Category investment = new Category("investment", "Investment", R.drawable.investment);
        Category medical = new Category("medical", "Medical", R.drawable.medical);
        Category transport = new Category("transport", "Transportation", R.drawable.public_transport);
        Category mortgage = new Category("mortgage", "Mortgage", R.drawable.mortgage);
        Category others = new Category("others", "Other Expense", R.drawable.shopping_);
        Category travel = new Category("travel", "Travel", R.drawable.travel);
        Category rentals = new Category("rentals", "Rentals", R.drawable.mortgage);
        Category education = new Category("education", "Education", R.drawable.book);

        categories.add(entertainment);
        categories.add(food);
        categories.add(beverage);
        categories.add(income);
        categories.add(clothes);
        categories.add(electricity);
        categories.add(gas);
        categories.add(otherBills);
        categories.add(gift);
        categories.add(fitness);
        categories.add(insurance);
        categories.add(investment);
        categories.add(medical);
        categories.add(transport);
        categories.add(mortgage);
        categories.add(rentals);
        categories.add(travel);
        categories.add(education);
        categories.add(others);
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
