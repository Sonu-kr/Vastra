package com.example.vastra;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Coustomers extends AppCompatActivity {
    private LinearLayout container;
    private Button addCoustomer;
    private EditText etGetCoustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coustomers);
        container = findViewById(R.id.container);

        addCoustomer = findViewById(R.id.btnAddCoustomer);
        etGetCoustomer = findViewById(R.id.etGetName);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("buttons");
        System.out.println(myRef);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String buttonText = snapshot.getValue(String.class);
                    createButton(buttonText); // Call a method to create button dynamically
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        addCoustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String coustomerName = etGetCoustomer.getText().toString();
                // Add the customer name to the Firebase database
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("buttons");
                myRef.push().setValue(coustomerName);
                // Create the button with the same name
                createButton(coustomerName);

            }
        });
    }

    private void createButton(String buttonText) {
        Button button = new Button(this);
        button.setText(buttonText);

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
