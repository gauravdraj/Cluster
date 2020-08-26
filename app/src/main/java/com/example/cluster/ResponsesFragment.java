package com.example.cluster;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ResponsesFragment extends Fragment {

    //UI elements and fields
    ListView listViewLive;
    String clusterID;

    public ResponsesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_responses, container, false);

        //Set title
        Intent thisScreen = getActivity().getIntent();
        clusterID = thisScreen.getStringExtra("ID");

        //Link UI elements
        ListView listViewDaily = (ListView) root.findViewById(R.id.dailyList);
        listViewLive = (ListView) root.findViewById(R.id.liveList);

        //Connect to firebase
        FirebaseFirestore.getInstance().collection("clusters").document(clusterID)
                .collection("dailypolls").get()
                .addOnCompleteListener(task -> {

                    //Get all daily polls and ID from the cluster
                    ArrayList<String> dailyPolls = new ArrayList<>();
                    ArrayList<String> dailyPollID = new ArrayList<>();
                    if (task.isComplete()) {

                        //Loop through the queries
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            String dailyTitle = documentSnapshot.getString("question");
                            String dailyID = documentSnapshot.getId();
                            if (dailyTitle != null) {
                                dailyPolls.add(dailyTitle);
                                dailyPollID.add(dailyID);
                            }
                        }

                        //Link listViews
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                android.R.layout.simple_list_item_1, dailyPolls);
                        listViewDaily.setAdapter(adapter);

                        listViewDaily.setOnItemClickListener((parent, view, position, id) -> {
                            //Create new activity and pass relevant info
                            Intent nextPage = new Intent(getActivity(), DailyPollingResults.class);
                            nextPage.putExtra("title", dailyPolls.get(position));
                            nextPage.putExtra("pollID", dailyPollID.get(position));
                            nextPage.putExtra("clusterID", clusterID);
                            startActivity(nextPage);
                        });
                    }
                });

        //Updates live responses list every 3 seconds
        new Thread(() -> {
            while (true) {
                FirebaseFirestore.getInstance().collection("clusters").document(clusterID)
                        .collection("livepolls").document("live").get()
                        .addOnCompleteListener(task -> {
                            if (task.isComplete()) {
                                ArrayList<String> livePolls = new ArrayList<>();
                                DocumentSnapshot snapshot = task.getResult();

                                //Get all the replies
                                List<String> replies = (List<String>) snapshot.get("replies");
                                if (replies != null || replies.isEmpty()) {
                                    for (String reply : replies) {
                                        if (reply != null) {
                                            livePolls.add(reply);
                                        }
                                    }
                                    try {
                                        //Post it in the listView
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                                android.R.layout.simple_list_item_1, livePolls);
                                        listViewLive.setAdapter(adapter);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        });
                android.os.SystemClock.sleep(3000); //Try after 3 seconds again
            }
        }).start();
        return root;
    }



}
