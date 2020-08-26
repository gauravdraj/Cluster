package com.example.cluster;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Community#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Community extends Fragment {


    private ListView listView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Community() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Community.
     */
    // TODO: Rename and change types and number of parameters
    public static Community newInstance(String param1, String param2) {
        Community fragment = new Community();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_community, container, false);
        listView = (ListView) root.findViewById(R.id.clusterList);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);


        //Get Firebase clusters and List them, only allow "Members"
        FirebaseFirestore.getInstance().collection("clusters")
                .get().addOnCompleteListener(task -> {
                    ArrayList<String> clusterNames = new ArrayList<>();
                    ArrayList<String> clusterIDs = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            List<String> members = (List<String>) documentSnapshot.get("members");
                            //See if its a member
                            for (String member : members) {
                                if (member.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                    clusterNames.add(documentSnapshot.getString("name"));
                                    clusterIDs.add(documentSnapshot.getId());
                                    break;
                                }
                            }

                            //Connect to custom listView
                            MyListAdapter adapter = new MyListAdapter(getActivity(), clusterNames,
                                    false);

                            listView.setAdapter(adapter);

                            //Create onListener
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    //Create new page with relevant info
                                    Intent i = new Intent(getActivity(), DiscussionBoard.class);
                                    i.putExtra("title", clusterNames.get(position));
                                    i.putExtra("clusterID", clusterIDs.get(position));
                                    startActivity(i);
                                }
                            });

                        }


                    }
                });
    }
}