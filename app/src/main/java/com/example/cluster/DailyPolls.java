package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

public class DailyPolls extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_polls);
        setTitle("Daily Polls");

        String clusterID = getIntent().getStringExtra("ID");

        ListView listView = (ListView) findViewById(R.id.dailyList);
        FirebaseFirestore.getInstance().collection("clusters")
                .document(clusterID).collection("dailypolls").get()
                .addOnCompleteListener(task -> {
                    //Get poll titles and ID's
                    ArrayList<String> polls = new ArrayList<>();
                    ArrayList<String> pollID = new ArrayList<>();
                    if (task.isComplete()) {
                        //Go through each query result
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            String title = documentSnapshot.getString("question");
                            String ID = documentSnapshot.getId();
                            if (title != null) {
                                polls.add(title);
                                pollID.add(ID);
                            }
                        }

                        //Set listView
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(DailyPolls.this,
                                android.R.layout.simple_list_item_1, polls);

                        listView.setAdapter(adapter);

                        //Set clickable activity for each item
                        listView.setOnItemClickListener((parent, view, position, id) -> {
                            //Create new activity and pass relevant info
                            Intent nextPage = new Intent(DailyPolls.this,
                                    ReplyDailyPoll.class);
                            nextPage.putExtra("clusterID", clusterID);
                            nextPage.putExtra("pollID", pollID.get(position));
                            nextPage.putExtra("title", polls.get(position));
                            startActivity(nextPage);
                        });
                    }
                });
    }
}