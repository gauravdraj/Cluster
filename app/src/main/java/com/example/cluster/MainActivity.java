package com.example.cluster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity {


    /**
     * LOGIN SCREEN
     * @param savedInstanceState
     */


    private FirebaseUser mAuth;
    private FirebaseUser user;
    private FirebaseAuth AuthUser;

    private ProgressDialog loginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AuthUser = FirebaseAuth.getInstance();
        setLoginDialog();

    }


    /**
     * Function when login button is clicked
     * @param v View object
     */
    public void onSignInClick(View v) {

        String email = ((EditText) findViewById(R.id.emailField)).getText().toString();
        String password = ((EditText) findViewById(R.id.PasswordField)).getText().toString();

        //Check fields
        if (email.isEmpty() || password.isEmpty()) {
            sendInvalidAlert();
            return;
        }

        loginDialog.show();
        AuthUser.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    //If Login is successful
                    if (task.isSuccessful()) {
                        user = AuthUser.getCurrentUser();
                        //Navigate to the next screen
                        Intent i = new Intent(MainActivity.this, Lobby.class);
                        startActivity(i);
                    }
                    //Show error message
                    else {
                        sendInvalidAlert();
                    }

                    loginDialog.dismiss();
                });

    }


    /**
     * Creates login Dialog
     */
    private void setLoginDialog() {
        loginDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        loginDialog.setTitle("Loading");
        loginDialog.setMessage("Please Wait... Logging in");
        loginDialog.setCancelable(false);
    }

    /**
     * Creates and Sends an Invalid Alert if logging in fails
     */
    private void sendInvalidAlert() {
        this.runOnUiThread(() -> {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this,
                    R.style.MyAlertDialogStyle).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Invalid username/password");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        });
    }


}