package com.example.cluster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;



public class StudentViewCluster extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_cluster);
        Intent intent = getIntent();
        String title = intent.getStringExtra("Cluster");
        setTitle(title);


        //Options to show
        String[] options = {"Daily Polls", "Live Polls"};

        //Link listView
        ListView listView = findViewById(R.id.listOptions);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                options));

        //Clickable liveView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent nextPage;
            if (position == 0) {
                //Daily polling
                nextPage = new Intent(StudentViewCluster.this, DailyPolls.class);
            }
            else {
                //Live polling
                nextPage = new Intent(StudentViewCluster.this, LivePoll.class);
            }
            //Pass relevant info
            nextPage.putExtra("ID", intent.getStringExtra("ID"));
            startActivity(nextPage);
        });
    }
}