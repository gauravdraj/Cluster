package com.example.cluster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PromptFragment extends Fragment {

    public PromptFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_prompt, container, false);

        //Get relevant info from previous page
        Intent thisScreen = getActivity().getIntent();
        String clusterID = thisScreen.getStringExtra("ID");

        //Link UI elements
        EditText questionText = (EditText) root.findViewById(R.id.questionView);
        Button submit = (Button) root.findViewById(R.id.submitButton);
        Switch type = (Switch) root.findViewById(R.id.switchType);

        //Function for submitting a question
        submit.setOnClickListener(v -> {


            boolean live = type.isChecked();

            //If switch is checked
            if (live) {
                //Add question to live polling
                FirebaseFirestore.getInstance().collection("clusters")
                        .document(clusterID).collection("livepolls")
                        .document("live").update("question",
                        questionText.getText().toString());
            }
            else {
                //Add question to daily polling
                Map<String, Object> data = new HashMap<>();
                data.put("question", questionText.getText().toString());
                data.put("replies", new ArrayList<>());

                //Add to firebase
                FirebaseFirestore.getInstance().collection("clusters")
                        .document(clusterID).collection("dailypolls")
                        .add(data);
            }
            Toast.makeText(getContext(), "Question Posted", Toast.LENGTH_LONG).show();
        });

        return root;
    }
}
