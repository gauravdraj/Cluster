package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.w3c.dom.Text;

public class SettingsChangeName extends AppCompatActivity {

    private EditText nameField;
    private Button nameButton;

    private static final String TAG = "SettingsChangeName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_change_name);

        //Get UI elements
        nameField = findViewById(R.id.newNameField);
        nameButton = findViewById(R.id.changeNameButton);

        //Set title
        Intent intent = getIntent();
        String title = intent.getStringExtra("SettingsNameSwitch");
        setTitle(title);

        nameButton.setOnClickListener(v -> {

            //Change name
            String newNameString = nameField.getText().toString();
            if (TextUtils.isEmpty(newNameString)) {
                Toast.makeText(getApplicationContext(), "Enter new name", Toast.LENGTH_LONG).show();
            }
            else {
                onClickUpdateName(newNameString);
            }
        });
    }

    /**
     * Connect to firebase and change name
     * @param nName new name of the user
     */
    public void onClickUpdateName(String nName) {
        //Connect to firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nName)
                .build();
        //Update profile
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Name updated Successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }) //If update fails
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(),
                        "Name update failed", Toast.LENGTH_LONG).show());
    }

}