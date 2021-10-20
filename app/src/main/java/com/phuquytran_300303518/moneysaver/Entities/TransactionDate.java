package com.phuquytran_300303518.moneysaver.Entities;

public class TransactionDate {
    int day;
    int week;
    int month;
    int year;

    public TransactionDate(){

    }

    public TransactionDate(int day, int week, int month, int year) {
        this.day = day;
        this.week = week;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
