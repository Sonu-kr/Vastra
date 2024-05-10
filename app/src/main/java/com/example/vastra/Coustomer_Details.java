package com.example.vastra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Coustomer_Details extends AppCompatActivity {
    private Spinner transactionTypeSpinner;
    private TextView vendor,total;
    private EditText designEditText,diamond,price;
    private Button calculateTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coustomer_details);

        // Initialize Spinners
        transactionTypeSpinner = findViewById(R.id.tarnsactionType);
        vendor = findViewById(R.id.Vendor);


        // Initialize EditText fields
        designEditText = findViewById(R.id.etDesign);
        diamond = findViewById(R.id.diamonds);
        price = findViewById(R.id.price);
        total = findViewById(R.id.total);
        calculateTotal = findViewById(R.id.calculateTotal);

        // Receive the name of the button clicked from the previous activity
        Intent intent = getIntent();
        if (intent != null) {
            String buttonName = intent.getStringExtra("COUSTOMER_NAME");
            vendor.setText(buttonName);
            if (buttonName != null) {
                // Use the received button name as needed
                Toast.makeText(this, "Clicked Button Name: " + buttonName, Toast.LENGTH_SHORT).show();
            }

        }
        // Populate the spinner with transaction types
        List<TransactionType> transactionTypes = new ArrayList<>();
        transactionTypes.add(new TransactionType("Job Work", "Job"));
        transactionTypes.add(new TransactionType("Receive", "Receive"));

        TransactionTypeAdapter adapter = new TransactionTypeAdapter(this, transactionTypes);
        transactionTypeSpinner.setAdapter(adapter);
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
                        // Handle the case where parsing fails
                        e.printStackTrace(); // Log the exception for debugging
                        total.setText("Invalid Input");
                    }
                } else {
                    // Handle the case where one or both fields are empty
                    total.setText("Please enter values");
                }
            }
        });



    }
}
