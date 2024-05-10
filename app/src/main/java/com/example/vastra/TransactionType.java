package com.example.vastra;
public class TransactionType {
    private String title;
    private String type;

    public TransactionType(String title, String type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return title; // Display title in the spinner
    }
}
