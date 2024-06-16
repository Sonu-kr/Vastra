package com.example.vastra.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vastra.Coustomers;
import com.example.vastra.JobWork.JobWork;
import com.example.vastra.R;
import com.example.vastra.Retail.RetailCustomersActivity;

public class WorkType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_type);

        // Reference the buttons using their IDs
        Button btnRetail = findViewById(R.id.btnRetail);
        Button btnJobWork = findViewById(R.id.btnJobWork);
        Button btnDesign = findViewById(R.id.btnDesign);

        // Set click listeners for the buttons
        btnRetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button 1 click
                Intent intent = new Intent(WorkType.this, RetailCustomersActivity.class); // Replace Activity1 with the actual target activity
                startActivity(intent);
            }
        });

        btnJobWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button 2 click
                Intent intent = new Intent(WorkType.this, JobWork.class); // Replace Activity2 with the actual target activity
                startActivity(intent);
            }
        });

        btnDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button 3 click
                Intent intent = new Intent(WorkType.this, Coustomers.class); // Replace Activity3 with the actual target activity
                startActivity(intent);
            }
        });
    }
}
