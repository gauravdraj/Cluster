package com.example.cluster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class LivePoll extends AppCompatActivity {


    //UI elements and Firebase info
    Handler handler = new Handler();
    Fragment fragment;
    static int display;
    String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_poll);
        setTitle("Live Poll");
        display = R.id.fragmentContainer;
        fragment = new WaitingFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment).commit();

    }

    /**
     * This is used after the view is created
     */
    @Override
    protected void onStart() {
        super.onStart();

        handler.postDelayed(new Runnable() {

            /**
             * Runs background thread to get live question
             */
            @Override
            public void run() {
                //Get correct cluster
                FirebaseFirestore.getInstance().collection("clusters")
                        .document(getIntent().getStringExtra("ID"))
                        .collection("livepolls")
                        .document("live").get().addOnCompleteListener(task -> {

                    //Get fragment manager
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                            .beginTransaction();

                    //Check if query is complete
                    if (task.isComplete()) {
                        DocumentSnapshot snapshot = task.getResult();
                        String question = snapshot.getString("question");
                        List<String> emails = (List<String>) snapshot.get("emails");
                        boolean answered = false;

                        //Loop to check if use already answered the question
                        for (String s : emails)
                            if (s.equals(userEmail))
                                answered = true;

                        //User did not answer
                        if (!(question.isEmpty()) && !answered) {
                            try {
                                //Go to the next fragment
                                //Add relevant info to fragment
                                Bundle bundle = new Bundle();
                                QuestionFragment questionFragment = new QuestionFragment();
                                questionFragment.setArguments(bundle);
                                bundle.putString("ID", getIntent().getStringExtra("ID"));

                                //Post the fragment
                                fragmentTransaction.replace(R.id.fragmentContainer,
                                        questionFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else
                            handler.postDelayed(this, 1000);
                    }
                });
            }

        }, 1000); //Run thread every 1 sec
    }
}