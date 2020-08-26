package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateNewUser extends AppCompatActivity {

    //UI Elements
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText confirmPasswordEditText;
    private Button createUserButton;

    //Firebase References
    private FirebaseDatabase database;
    private DatabaseReference nDatabase;
    private FirebaseAuth mAuth;
    private static final String USER = "user";
    private static final String TAG = "CreateNewUser";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);

        //Linking UI elements
        emailEditText = findViewById(R.id.new_email_field);
        passwordEditText = findViewById(R.id.new_password_field);
        nameEditText = findViewById(R.id.new_name_field);
        confirmPasswordEditText = findViewById(R.id.confirm_password_field);
        createUserButton = findViewById(R.id.submit_new_user);

        //Getting firebase references
        database = FirebaseDatabase.getInstance();
        nDatabase = database.getReference(USER);
        mAuth = FirebaseAuth.getInstance();

        //When button is clicked
        createUserButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String name = nameEditText.getText().toString();
            String confirm_password = confirmPasswordEditText.getText().toString();

            //Check if fields are empty
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) ||
            TextUtils.isEmpty(confirm_password)) {
                Toast.makeText(getApplicationContext(), "Enter name, email, password, and confirmation",
                        Toast.LENGTH_LONG).show();
                return;
            }
            user = new User(email, password, name);
            onClickFormatNewUser(email, password);
        });
    }

    /**
     * Add user info to Firebase Auth
     * @param email user's email
     * @param password user's password
     */
    public void onClickFormatNewUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "createUserWithEmail:success");
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
            }
            else {
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                Toast.makeText(CreateNewUser.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Sets up UI with Firebase user
     * @param currentUser Firebase user
     */
    public void updateUI(FirebaseUser currentUser) {
        String keyId = nDatabase.push().getKey();
        nDatabase.child(keyId).setValue(user);
        finish();
    }
}