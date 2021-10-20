package com.phuquytran_300303518.moneysaver.Entities;

import com.phuquytran_300303518.moneysaver.Enum.TransactionType;

import java.util.UUID;

public class Transaction {
    private String transactionID;
    private String transactionTitle;
    private TransactionType type;
    private String transactionDescription;
    private Double transactionAmount;

//    private String walletID;
//    private String CategoryID;

    public Transaction(){

    }
    public Transaction(String transactionTitle, TransactionType type, Double transactionAmount ) {
        this.transactionID = UUID.randomUUID().toString();
        this.transactionTitle = transactionTitle;
        this.type = type;
        this.transactionAmount = transactionAmount;
        transactionDescription = "";
    }

    public Transaction(String transactionTitle, TransactionType type, Double transactionAmount, String transactionDescription) {
        this.transactionID = UUID.randomUUID().toString();
        this.transactionTitle = transactionTitle;
        this.type = type;
        this.transactionAmount = transactionAmount;
        this.transactionDescription = transactionDescription;
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
}
