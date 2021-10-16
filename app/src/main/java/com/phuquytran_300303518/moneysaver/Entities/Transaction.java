package com.phuquytran_300303518.moneysaver.Entities;

public class Transaction {
    private String transactionID;
    private String transactionTitle;
    private String type;
    private String transactionDescription;
    private Double transactionAmount;
    private String walletID;
    private String CategoryID;

    public Transaction(){
        this.transactionTitle = "";
        this.type = "";
        this.transactionAmount = 0.0;
        transactionDescription = "";
    }
    public Transaction(String transactionTitle, String type, Double transactionAmount ) {
        this.transactionTitle = transactionTitle;
        this.type = type;
        this.transactionAmount = transactionAmount;
        transactionDescription = "";
    }

    public Transaction(String transactionTitle, String type, Double transactionAmount, String transactionDescription) {
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
    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public String getWalletID() {
        return walletID;
    }

    public void setWalletID(String walletID) {
        this.walletID = walletID;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }
}
