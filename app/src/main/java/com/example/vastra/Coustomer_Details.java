package com.example.vastra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Coustomer_Details extends AppCompatActivity {
    private Spinner transactionTypeSpinner;
    private TextView vendor, total, Counter;
    private EditText designEditText, diamond, price;
    private Button calculateTotal, submitData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coustomer_details);

        // Initialize views
        transactionTypeSpinner = findViewById(R.id.tarnsactionType);
        vendor = findViewById(R.id.Vendor);
        Counter = findViewById(R.id.Counter);
        designEditText = findViewById(R.id.etDesign);
        diamond = findViewById(R.id.diamonds);
        price = findViewById(R.id.price);
        total = findViewById(R.id.total);
        calculateTotal = findViewById(R.id.calculateTotal);
        submitData = findViewById(R.id.submitData);

        // Receive the name of the button clicked from the previous activity
        Intent intent = getIntent();
        if (intent != null) {
            String buttonName = intent.getStringExtra("COUSTOMER_NAME");
            vendor.setText(buttonName);
            if (buttonName != null) {
                Toast.makeText(this, "Clicked Button Name: " + buttonName, Toast.LENGTH_SHORT).show();
            }
        }

        // Populate the spinner with transaction types
        List<TransactionType> transactionTypes = new ArrayList<>();
        transactionTypes.add(new TransactionType("Job Work", "Job"));
        transactionTypes.add(new TransactionType("Receive", "Receive"));

        TransactionTypeAdapter adapter = new TransactionTypeAdapter(this, transactionTypes);
        transactionTypeSpinner.setAdapter(adapter);

        // Fetch the counter value from Firebase
        String customerName = vendor.getText().toString();
        DatabaseReference counterRef = FirebaseDatabase.getInstance().getReference("buttons").child(customerName).child("counter");
        counterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String counterValue = snapshot.getValue(String.class);
                    if (counterValue != null) {
                        Counter.setText(counterValue);

                        try {
                            int count = Integer.parseInt(counterValue);
                            if (count > 0) {
                                Counter.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                            } else {
                                Counter.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(Coustomer_Details.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                            Counter.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        }
                    }
                } else {
                    // Handle the case where the counter value is not present in the database
                    Counter.setText("0");
                    Counter.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Coustomer_Details.this, "Failed to fetch counter value", Toast.LENGTH_SHORT).show();
            }
        });

        calculateTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noOfDiamond = diamond.getText().toString();
                String priceOfEachDiamond = price.getText().toString();

                if (!noOfDiamond.isEmpty() && !priceOfEachDiamond.isEmpty()) {
                    try {
                        int dDiamond = Integer.parseInt(noOfDiamond);
                        double price = Double.parseDouble(priceOfEachDiamond);
                        double total_price = price * dDiamond;
                        total.setText(String.valueOf(total_price));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        total.setText("Invalid Input");
                    }
                } else {
                    total.setText("Please enter values");
                }
            }
        });

        // Handle the submission of data
        submitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String transactionType = ((TransactionType) transactionTypeSpinner.getSelectedItem()).getTitle();
                String customerName = vendor.getText().toString();
                String design = designEditText.getText().toString();
                String diamondCount = diamond.getText().toString();
                String priceValue = price.getText().toString();

                if (transactionType.equals("Receive")) {
                    showReceiveDialog(customerName, design, diamondCount, priceValue);
                } else if (transactionType.equals("Job Work")) {
                    showJobWorkDialog(customerName, design, diamondCount, priceValue);
                }
            }
        });
    }

    private boolean isDialogShowing = false;

    private void showJobWorkDialog(String customerName, String design, String diamondCount, String priceValue) {
        if (isDialogShowing) {
            return;
        }
        isDialogShowing = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Job Work");

        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String jobWorkCountStr = input.getText().toString();
                if (!jobWorkCountStr.isEmpty()) {
                    int jobWorkCount = Integer.parseInt(jobWorkCountStr);
                    int currentCount = Integer.parseInt(Counter.getText().toString());
                    int newCount = currentCount + jobWorkCount;

                    // Update the counter value
                    Counter.setText(String.valueOf(newCount));

                    // Change the counter color based on the new value
                    if (newCount > 0) {
                        Counter.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                    } else {
                        Counter.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    }

                    // Save data to Firebase
                    saveDataToFirebase(customerName, design, diamondCount, priceValue, newCount);
                } else {
                    Toast.makeText(Coustomer_Details.this, "Please enter the number of items for Job Work", Toast.LENGTH_SHORT).show();
                }
                isDialogShowing = false;
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                isDialogShowing = false;
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isDialogShowing = false;
            }
        });

        builder.show();
    }

    private void showReceiveDialog(String customerName, String design, String diamondCount, String priceValue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Receive Items");

        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int receivedCount = Integer.parseInt(input.getText().toString());
                int currentCount = Integer.parseInt(Counter.getText().toString());
                int newCount = currentCount - receivedCount;

                // Update the counter value
                Counter.setText(String.valueOf(newCount));

                // Change the counter color based on the new value
                if (newCount > 0) {
                    Counter.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                } else {
                    Counter.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }

                // Save data to Firebase
                saveDataToFirebase(customerName, design, diamondCount, priceValue, newCount);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void saveDataToFirebase(String customerName, String design, String diamondCount, String priceValue, int newCounterValue) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("buttons").child(customerName);

        // Create a Map to hold the data
        Map<String, Object> data = new HashMap<>();
        data.put("design", design);
        data.put("diamondCount", diamondCount);
        data.put("priceValue", priceValue);
        data.put("counter", String.valueOf(newCounterValue));

        // Update the data for the customer
        databaseReference.updateChildren(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
