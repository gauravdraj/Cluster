package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DiscussionBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_board);

        //Get info from previous page
        Intent intent = getIntent();
        setTitle(intent.getStringExtra("title"));
        String clusterID = intent.getStringExtra("clusterID");

        //ListView used in xml page
        ListView listView = (ListView) findViewById(R.id.displayList);

        //Connect to firebase
        FirebaseFirestore.getInstance().collection("community")
                .document(clusterID).collection("posts").get()
                .addOnCompleteListener(task -> {

                    //If query is complete
                    if (task.isComplete()) {
                        List<String> postsTitles = new ArrayList<>();
                        List<String> postID = new ArrayList<>();
                        //Loop through each query
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            postsTitles.add(documentSnapshot.getString("title"));
                            postID.add(documentSnapshot.getId());
                        }

                        //Link with ListView
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(DiscussionBoard.this,
                                android.R.layout.simple_list_item_1, postsTitles);
                        listView.setAdapter(adapter);

                        //Set clickable activity for each item
                        listView.setOnItemClickListener((parent, view, position, id) -> {
                            //Pass relevant info to next screen
                            Intent i = new Intent(DiscussionBoard.this, PostPage.class);
                            i.putExtra("clusterID", clusterID);
                            i.putExtra("postID", postID.get(position));
                            i.putExtra("title", postsTitles.get(position));
                            startActivity(i);
                        });
                    }
                });
    }

    /**
     * Function for button to create new post
     * @param view View object
     */
    public void onCreatePosts(View view) {
        //Go to next screen with activity
        Intent nextScreen = new Intent(this, CreateNewPost.class);
        nextScreen.putExtra("clusterID", getIntent().getStringExtra("clusterID"));
        startActivityForResult(nextScreen, 500);
    }

    /**
     * After user creates a new post, update UI
     * @param requestCode code sent by previous activity
     * @param resultCode not used, implemented by @Override
     * @param data Not used, implemented by @Override
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500)
            this.recreate();
    }

}