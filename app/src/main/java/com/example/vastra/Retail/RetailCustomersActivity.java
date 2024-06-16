package com.example.vastra.Retail;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vastra.Customer;
import com.example.vastra.Item;
import com.example.vastra.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RetailCustomersActivity extends AppCompatActivity {

    private EditText etCustomerName, etItemCount, etItemPrice;
    private Button btnAddCustomer, btnAddItem;
    private RecyclerView recyclerView;
    private RetailCustomerAdapter adapter;
    private ArrayList<Customer> customerList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private String selectedCustomerId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retail_customers);

        // Initialize views
        etCustomerName = findViewById(R.id.etCustomerName);
        etItemCount = findViewById(R.id.etItemCount);
        etItemPrice = findViewById(R.id.etItemPrice);
        btnAddCustomer = findViewById(R.id.btnAddCustomer);
        btnAddItem = findViewById(R.id.btnAddItem);
        recyclerView = findViewById(R.id.recyclerView);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RetailCustomerAdapter(customerList, this);
        recyclerView.setAdapter(adapter);

        // Set item click listener
        adapter.setOnItemClickListener(new RetailCustomerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String customerId) {
                selectedCustomerId = customerId;
                Toast.makeText(RetailCustomersActivity.this, "Selected Customer ID: " + customerId, Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("retail_customers");

        // Fetch customers from Firebase
        fetchCustomersFromFirebase();

        // Add new customer
        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerName = etCustomerName.getText().toString().trim();
                if (!customerName.isEmpty()) {
                    addCustomerToFirebase(customerName);
                } else {
                    Toast.makeText(RetailCustomersActivity.this, "Please enter a customer name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Add item to selected customer
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemCountStr = etItemCount.getText().toString().trim();
                String itemPriceStr = etItemPrice.getText().toString().trim();
                if (!itemCountStr.isEmpty() && !itemPriceStr.isEmpty()) {
                    int itemCount = Integer.parseInt(itemCountStr);
                    double itemPrice = Double.parseDouble(itemPriceStr);
                    // Assuming selectedCustomerId, itemCount, and itemPrice are properly defined and initialized
                    addItemToCustomer(selectedCustomerId, itemCount, itemPrice);

                } else {
                    Toast.makeText(RetailCustomersActivity.this, "Please enter item count and price", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchCustomersFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Customer customer = snapshot.getValue(Customer.class);
                    customerList.add(customer);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RetailCustomersActivity.this, "Failed to load customers", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCustomerToFirebase(String customerName) {
        String customerId = databaseReference.push().getKey();
        Customer customer = new Customer(customerId, customerName);
        databaseReference.child(customerId).setValue(customer)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RetailCustomersActivity.this, "Customer added", Toast.LENGTH_SHORT).show();
                    etCustomerName.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RetailCustomersActivity.this, "Failed to add customer", Toast.LENGTH_SHORT).show();
                });
    }

    private void addItemToCustomer(String customerId, int itemCount, double itemPrice) {
        for (Customer customer : customerList) {
            if (customer.getId().equals(customerId)) {
                String itemId = databaseReference.child(customerId).child("items").push().getKey();

                // Add the correct number of items based on the item count
                for (int i = 0; i < itemCount; i++) {
                    Item item = new Item(itemCount, itemPrice); // One item at a time
                    databaseReference.child(customerId).child("items").child(itemId).setValue(item)
                            .addOnSuccessListener(aVoid -> Toast.makeText(RetailCustomersActivity.this, "Item added successfully.", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(RetailCustomersActivity.this, "Failed to add item.", Toast.LENGTH_SHORT).show());
                }
                return;
            }
        }

        Toast.makeText(this, "Customer not found.", Toast.LENGTH_SHORT).show();
    }
}
