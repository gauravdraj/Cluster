package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SettingsAbout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_about);
        setTitle("About");

        //Get UI elements
        TextView name = (TextView) findViewById(R.id.nameField);
        TextView email = (TextView) findViewById(R.id.emailField);

        //Set user info here
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        FirebaseFirestore.getInstance()
                .collection("users")
                .whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(task -> {

                    //Post name of the user
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        name.setText(documentSnapshot.getString("name"));
                    }
                });
    }
}