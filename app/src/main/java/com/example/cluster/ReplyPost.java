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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReplyPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_post);

        //Set Title
        Intent thisPage = getIntent();
        setTitle("Reply to " + thisPage.getStringExtra("title"));
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText(thisPage.getStringExtra("title"));
    }


    /**
     * Creates reply post for daily polling
     * @param view View Object
     */
    public void onClick(View view) {
        EditText reply = (EditText) findViewById(R.id.repsonseText);

        //Check if reply is valid
        if (reply.getText().toString().isEmpty())
            Toast.makeText(this, "No message written", Toast.LENGTH_LONG).show();
        else {
            //Connect to Firebase
            FirebaseFirestore.getInstance().collection("community")
                    .document(getIntent().getStringExtra("clusterID"))
                    .collection("posts")
                    .document(getIntent().getStringExtra("postID")).update("replies",
                    FieldValue.arrayUnion(reply.getText().toString())).addOnCompleteListener(task -> {
                        //If reply was able to be uploaded
                        if (task.isComplete()) {
                            Toast.makeText(ReplyPost.this, "Reply sent",
                                    Toast.LENGTH_LONG).show();

                            //Refresh last Activity and end
                            setResult(500);
                            finish();
                        }
                    });

        }
    }
}