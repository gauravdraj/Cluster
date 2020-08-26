package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.ResultSet;
import java.util.List;

public class PostPage extends AppCompatActivity {

    //Relevant strings
    String clusterID;
    String postID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);
        Intent thisPage = getIntent();
        setTitle(thisPage.getStringExtra("title"));

        //UI element and Firebase references from previous screen
        TextView title = (TextView) findViewById(R.id.titleView);
        title.setText(thisPage.getStringExtra("title"));
        ListView listView = (ListView) findViewById(R.id.replyList);
        clusterID = thisPage.getStringExtra("clusterID");
        postID = thisPage.getStringExtra("postID");
        TextView questionView = findViewById(R.id.contextView);

        //Connect to firebase
        FirebaseFirestore.getInstance().collection("community").document(clusterID)
                .collection("posts").document(postID).get()
                .addOnCompleteListener(task -> {

                    //Check if query is complete
                    if (task.isComplete()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        List<String> replies = (List<String>) documentSnapshot.get("replies");
                        String question = documentSnapshot.getString("question");

                        if (replies != null && question != null) {

                            //Create custom list and update list
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(PostPage.this,
                                    android.R.layout.simple_list_item_1, replies);
                            listView.setAdapter(adapter);

                            questionView.setText(question);
                        }
                    }
                });
    }

    /**
     * Navigates to the next screen to create a reply
     * @param view View Object
     */
    public void createReply(View view) {
        //Create reply activity and pass relevant info
        Intent nextPage = new Intent(this, ReplyPost.class);
        nextPage.putExtra("clusterID", clusterID);
        nextPage.putExtra("postID", postID);
        nextPage.putExtra("title", getIntent().getStringExtra("title"));
        startActivity(nextPage);
    }

    /**
     * After post is created, update UI
     * @param requestCode code from previous screen
     * @param resultCode not used, implemented by @Override
     * @param data not used, implemented by @Override
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500)
            this.recreate();
    }

}