package com.example.cluster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateNewPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);
        setTitle("Create New Post");
    }

    /**
     * Function when submit button is click
     * @param view View object
     */
    public void onSubmit(View view) {
        String title = ((EditText) findViewById(R.id.titleView)).getText().toString();
        String context = ((EditText) findViewById(R.id.detailsView)).getText().toString();

        //Create data fields
        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("question", context);
        data.put("replies", new ArrayList<>());

        //Add to firebase
        FirebaseFirestore.getInstance().collection("community")
                .document(getIntent().getStringExtra("clusterID"))
                .collection("posts").add(data);
        setResult(500);
        finish();
    }
}