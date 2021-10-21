package com.phuquytran_300303518.moneysaver.Entities;

import com.phuquytran_300303518.moneysaver.Enum.TransactionType;

import java.util.UUID;

public class Transaction {
    private String transactionID;
    private String transactionTitle;
    private TransactionType type;
    private String transactionDescription;
    private Double transactionAmount;
    private TransactionDate date;
    private Category category;

    public Transaction(){

    }
    public Transaction(String transactionTitle, TransactionType type, Double transactionAmount, TransactionDate date ) {
        this.transactionID = UUID.randomUUID().toString();
        this.transactionTitle = transactionTitle;
        this.type = type;
        this.transactionAmount = transactionAmount;
        transactionDescription = "";
        this.date = date;
    }

    public Transaction(String transactionTitle, TransactionType type, Double transactionAmount, String transactionDescription, TransactionDate date, Category category) {
        this.transactionID = UUID.randomUUID().toString();
        this.transactionTitle = transactionTitle;
        this.type = type;
        this.transactionAmount = transactionAmount;
        this.transactionDescription = transactionDescription;
        this.date = date;
        this.category = category;
    }



    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionTitle() {
        return transactionTitle;
    }

    public void setTransactionTitle(String transactionTitle) {
        this.transactionTitle = transactionTitle;
    }
    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
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
}
