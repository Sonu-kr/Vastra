package com.example.vastra;

import java.util.HashMap;
import java.util.Map;

public class Customer {
    private String id;
    private String name;
    private Map<String, Item> items;

    public Customer() {
        // Default constructor required for calls to DataSnapshot.getValue(Customer.class)
    }

    public Customer(String id, String name, Map<String, Item> items) {
        this.id = id;
        this.name = name;
        this.items = items != null ? items : new HashMap<>();
    }

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
        this.items = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public void setItems(Map<String, Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        this.items.put(item.getItemId(), item);
    }
}
