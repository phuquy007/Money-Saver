package com.phuquytran_300303518.moneysaver.Entities;

import com.phuquytran_300303518.moneysaver.Enum.RepeatType;
import com.phuquytran_300303518.moneysaver.Enum.TransactionType;

public class PlanPayment {
    String id;
    String title;
    TransactionType type;
    String description;
    TransactionDate date;
    Category category;
    RepeatType repeatType;
    double amount;

    public PlanPayment(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionDate getDate() {
        return date;
    }

    public void setDate(TransactionDate date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public RepeatType getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(RepeatType repeatType) {
        this.repeatType = repeatType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
