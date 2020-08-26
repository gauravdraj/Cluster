package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SettingsChangePassword extends AppCompatActivity {

    //UI elements
    private EditText newPassword;
    private Button reset;

    //Firebase references
    private FirebaseAuth fAuth;
    private String userId;

    private static final String TAG = "SettingsChangePassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_change_password);

        //Link UI elements
        newPassword = findViewById(R.id.newPasswordField);
        reset = findViewById(R.id.submitButton);
        fAuth = FirebaseAuth.getInstance();
        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getEmail();

        //Set title
        Intent intent = getIntent();
        String title = intent.getStringExtra("SettingsPasswordSwitch");
        setTitle(title);

        //If button is clicked
        reset.setOnClickListener(v -> {
            //Set new password in firebase
            String password_string = newPassword.getText().toString();
            if (TextUtils.isEmpty(password_string)) {
                Toast.makeText(getApplicationContext(), "Enter new password",
                        Toast.LENGTH_LONG).show();
            }
            else {
                onClickUpdatePassword(password_string, v);
            }
        });
    }

    /**
     * Updates Firebase with new password
     * @param nPassword new Password
     * @param v View Object
     */
    public void onClickUpdatePassword(String nPassword, View v) {

        // Reauthenticate the user with their old password
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(userId, nPassword);
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        //Get UI elements
                        final EditText reenterPassword = new EditText(v.getContext());
                        final AlertDialog.Builder reLoginDialog = new AlertDialog.Builder(v.getContext());

                        reLoginDialog.setTitle("Enter new password");
                        reLoginDialog.setMessage("Enter your new password- must be 6+ characters");
                        reLoginDialog.setView(reenterPassword);

                        reLoginDialog.setPositiveButton("Submit", (dialog, which) -> {
                            String reenteredPassword = reenterPassword.getText().toString();
                            // Now set new password
                            user.updatePassword(reenteredPassword)
                                    .addOnCompleteListener(task1 -> { //If firebase works
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(SettingsChangePassword.this,
                                                    "Password reset Successful",
                                                    Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }) //Firebase returns issues
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(SettingsChangePassword.this,
                                                "Password reset failed", Toast.LENGTH_SHORT)
                                                .show();
                                        finish();
                                    });
                        });
                        reLoginDialog.setNegativeButton("Cancel", (dialog, which) -> finish());

                        AlertDialog reLogin = reLoginDialog.create();
                        reLogin.show();
                    }
                }) //Invalid password
                .addOnFailureListener(e -> Toast.makeText(SettingsChangePassword.this,
                        "Wrong password", Toast.LENGTH_SHORT).show());
    }
}