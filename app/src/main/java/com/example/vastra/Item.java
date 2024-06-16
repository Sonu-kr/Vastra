package com.example.vastra;

public class Item {
    private String itemId;
    private int count;
    private double price;
    private String jobType;
    private String vendor; // Add this line

    public Item() {
        // Default constructor required for calls to DataSnapshot.getValue(Item.class)
    }

    public Item(String itemId, int count, double price, String jobType, String vendor) {
        this.itemId = itemId;
        this.count = count;
        this.price = price;
        this.jobType = jobType;
        this.vendor = vendor; // Add this line
    }

    public Item(int itemCount, double itemPrice) {
        this.count = itemCount;
        this.price = itemPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getVendor() { // Add this method
        return vendor;
    }

    public void setVendor(String vendor) { // Add this method
        this.vendor = vendor;
    }
}
