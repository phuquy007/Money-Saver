package com.phuquytran_300303518.moneysaver.Entities;

public class Category {
    private String categoryID;
    private String categoryName;
    private int logo;
    private String categoryDescription;

    public  Category(){

    }

    public Category(String categoryID, String categoryName, int logo) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.logo = logo;
        this.categoryDescription = "";
    }
    public Category(String categoryID, String categoryName, int logo, String categoryDescription) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.logo = logo;
        this.categoryDescription = categoryDescription;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
}
