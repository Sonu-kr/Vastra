package com.example.vastra.JobWork;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vastra.Customer;
import com.example.vastra.CustomerAdapter;
import com.example.vastra.Item;
import com.example.vastra.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShowCustomersActivity extends AppCompatActivity {
    private Spinner jobTypeSpinner, vendorSpinner;
    private RecyclerView customerRecyclerView;
    private CustomerAdapter customerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_customers);

        jobTypeSpinner = findViewById(R.id.jobTypeSpinner);
        vendorSpinner = findViewById(R.id.vendorSpinner);
        customerRecyclerView = findViewById(R.id.customerRecyclerView);
        customerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        customerAdapter = new CustomerAdapter();
        customerRecyclerView.setAdapter(customerAdapter);

        loadItemsFromFirebase();
        setupSpinnerListeners();
    }

    private void loadItemsFromFirebase() {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("items");
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> itemList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null) {
                        itemList.add(item);
                    }
                }
                updateSpinners(itemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void updateSpinners(List<Item> itemList) {
        Set<String> jobTypes = new HashSet<>();
        Set<String> vendors = new HashSet<>();

        for (Item item : itemList) {
            if (item.getJobType() != null) {
                jobTypes.add(item.getJobType());
            }
            if (item.getVendor() != null) {
                vendors.add(item.getVendor());
            }
        }

        List<String> jobTypeList = new ArrayList<>(jobTypes);
        jobTypeList.add(0, "All");

        List<String> vendorList = new ArrayList<>(vendors);
        vendorList.add(0, "All");

        ArrayAdapter<String> jobTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jobTypeList);
        jobTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobTypeSpinner.setAdapter(jobTypeAdapter);

        ArrayAdapter<String> vendorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vendorList);
        vendorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendorSpinner.setAdapter(vendorAdapter);
    }

    private void setupSpinnerListeners() {
        jobTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterJobList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        vendorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterJobList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void filterJobList() {
        String selectedJobType = jobTypeSpinner.getSelectedItem().toString();
        String selectedVendor = vendorSpinner.getSelectedItem().toString();

        DatabaseReference customersRef = FirebaseDatabase.getInstance().getReference("customers");
        customersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Customer> customerList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Customer customer = snapshot.getValue(Customer.class);
                    if (customer != null) {
                        boolean matchesJobType = selectedJobType.equals("All") || customerHasJobType(customer, selectedJobType);
                        boolean matchesVendor = selectedVendor.equals("All") || customerHasVendor(customer, selectedVendor);

                        if (matchesJobType && matchesVendor) {
                            customerList.add(customer);
                        }
                    }
                }
                customerAdapter.setCustomers(customerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private boolean customerHasJobType(Customer customer, String jobType) {
        for (Item item : customer.getItems().values()) {
            if (jobType.equals(item.getJobType())) {
                return true;
            }
        }
        return false;
    }

    private boolean customerHasVendor(Customer customer, String vendor) {
        for (Item item : customer.getItems().values()) {
            if (vendor.equals(item.getVendor())) {
                return true;
            }
        }
        return false;
    }
}
