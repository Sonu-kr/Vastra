package com.example.vastra.JobWork;
import com.example.vastra.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddJob extends AppCompatActivity {

    private Spinner vendorType, jobType, designType;
    private EditText etQuantity;
    private Button btnSubmit, btnCancel;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        vendorType = findViewById(R.id.vendorType);
        jobType = findViewById(R.id.jobType);
        designType = findViewById(R.id.designType);

        etQuantity = findViewById(R.id.etQuantity);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Load data into spinners
        loadSpinnerData("vendors", vendorType);
        loadSpinnerData("jobTypes", jobType);
        loadSpinnerData("designTypes", designType);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadSpinnerData(String node, Spinner spinner) {
        databaseReference.child(node).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> dataList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String value = dataSnapshot.getValue(String.class);
                    dataList.add(value);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddJob.this, android.R.layout.simple_spinner_item, dataList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddJob.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitData() {
        String vendor = vendorType.getSelectedItem().toString();
        String job = jobType.getSelectedItem().toString();
        String design = designType.getSelectedItem().toString();
        String quantity = etQuantity.getText().toString();

        if (vendor.isEmpty() || job.isEmpty() || design.isEmpty() || quantity.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference jobsRef = databaseReference.child("jobs").push();
        jobsRef.child("vendor").setValue(vendor);
        jobsRef.child("jobType").setValue(job);
        jobsRef.child("designType").setValue(design);
        jobsRef.child("quantity").setValue(quantity).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddJob.this, "Job added successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            } else {
                Toast.makeText(AddJob.this, "Failed to add job", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
