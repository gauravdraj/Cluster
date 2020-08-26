package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReplyDailyPoll extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_daily_poll);

        //Set Title
        Intent thisScreen = getIntent();
        setTitle(thisScreen.getStringExtra("title"));
    }

    /**
     * Submit button function
     * @param view View Object
     */
    public void onSubmit(View view) {
        //Get reply from EditView
        String reply = ((EditText) findViewById(R.id.replyField)).getText().toString();

        if (!(reply.isEmpty())) {
            //Connect to firebase
            FirebaseFirestore.getInstance().collection("clusters")
                    .document(getIntent().getStringExtra("clusterID"))
                    .collection("dailypolls")
                    .document(getIntent().getStringExtra("pollID")).update("replies",
                    FieldValue.arrayUnion(reply)).addOnCompleteListener(task -> {
                        //If the reply was able to be uploaded
                        if (task.isComplete()) {
                            Toast.makeText(this, "Reply to " + getIntent().getStringExtra("title")
                                    + " Successful", Toast.LENGTH_LONG).show();
                        }
                    });

            //End Activity
            finish();
        }
    }
}