package com.example.vastra.JobWork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vastra.R;


public class JobWork extends AppCompatActivity {

    private Button btnAddJob;
    private Button btnAddVendor;
    private Button btnJobList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_work);

        // Initialize buttons
        btnAddJob = findViewById(R.id.btnAddJob);
        btnAddVendor = findViewById(R.id.btnAddVendor);
        btnJobList = findViewById(R.id.btnJobList);

        // Set click listeners
        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobWork.this, AddJob.class);
                startActivity(intent);
            }
        });

        btnAddVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobWork.this, AddVEndor.class);
                startActivity(intent);
            }
        });

        btnJobList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobWork.this, ShowCustomersActivity.class);
                startActivity(intent);
            }
        });
    }
}
