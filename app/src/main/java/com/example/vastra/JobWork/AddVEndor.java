package com.example.vastra.JobWork;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vastra.Coustomer_Details;
import com.example.vastra.Coustomers;
import com.example.vastra.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddVEndor extends AppCompatActivity {

    private LinearLayout container;
    private Button btnAddVendor;
    private EditText etVendorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor);

        container = findViewById(R.id.container);
        btnAddVendor = findViewById(R.id.btnAddVendor);
        etVendorName = findViewById(R.id.etVendorName);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("buttons");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                container.removeAllViews();  // Clear previous buttons to avoid duplication
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the customer name from the key of each child
                    String customerName = snapshot.getKey();
                    createButton(customerName); // Call a method to create button dynamically
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        btnAddVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coustomerName = etVendorName.getText().toString();
                if (!coustomerName.isEmpty()) {
                    // Add the customer name to the Firebase database
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("vendors").child(coustomerName);
                    myRef.setValue(coustomerName); // Set the value with the same name as the key
                    // Create the button with the same name
                    createButton(coustomerName);
                    etVendorName.setText(""); // Clear the input field after adding
                } else {
                    // Show a toast message if the input field is empty
                    Toast.makeText(AddVEndor.this, "Please enter a customer name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createButton(String buttonText) {
        Button button = new Button(this);
        button.setText(buttonText);
        // Set background color
        button.setBackgroundResource(R.drawable.button_background); // Change 'button_background' to the resource ID of your background drawable

        // Set text color
        button.setTextColor(getResources().getColor(R.color.white)); // Change 'button_text_color' to the resource ID of your text color

        // Set text size
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); // Change '16' to the desired text size in SP

        // Set padding
        int paddingInDp = (int) getResources().getDimension(R.dimen.button_padding); // Change 'button_padding' to the resource ID of your padding dimension
        // int paddingInDp = getResources().getDimensionPixelSize(R.dimen.button_padding);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(paddingInDp, paddingInDp, paddingInDp, paddingInDp);
        button.setLayoutParams(params);


        // Add button to the container
        container.addView(button);

        // Set click listener on the dynamically created button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the text (name) of the clicked button
                String buttonText = ((Button) view).getText().toString();
                // Create an Intent to start Coustomer_Details activity
                Intent gotoCoustomerDetail = new Intent(view.getContext(), Coustomer_Details.class);
                // Put the name of the clicked button as an extra with the intent
                gotoCoustomerDetail.putExtra("COUSTOMER_NAME", buttonText);

                // Start the Coustomer_Details activity with the intent
                startActivity(gotoCoustomerDetail);
            }
        });
    }
}