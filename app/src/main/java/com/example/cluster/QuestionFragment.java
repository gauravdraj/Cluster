package com.example.cluster;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(String param1, String param2) {
        QuestionFragment fragment = new QuestionFragment();
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
        View root = inflater.inflate(R.layout.fragment_question, container, false);
        Bundle bundle = this.getArguments();

        //Connect to firebase
        FirebaseFirestore.getInstance().collection("clusters")
                .document(bundle.getString("ID")).collection("livepolls")
                .document("live").get().addOnCompleteListener((OnCompleteListener<DocumentSnapshot>)
                task -> {
                    //If task is complete
                    if (task.isComplete()) {
                        //Question has been asked
                        TextView question = (TextView) root.findViewById(R.id.questionView);
                        question.setText(task.getResult().getString("question"));
                    }
                });

        //Link UI elements
        Button comfirm = (Button) root.findViewById(R.id.submitButton);
        EditText replyView = (EditText) root.findViewById(R.id.responseView);
        Fragment fragment = this;
        comfirm.setOnClickListener(v -> {

            //Get Info from Firebase and TextView element
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String reply = replyView.getText().toString();

            //Add user email
            FirebaseFirestore.getInstance().collection("clusters")
                    .document(bundle.getString("ID")).collection("livepolls")
                    .document("live")
                    .update("emails", FieldValue.arrayUnion(email));

            //Add user's reply
            FirebaseFirestore.getInstance().collection("clusters")
                    .document(bundle.getString("ID")).collection("livepolls")
                    .document("live")
                    .update("replies", FieldValue.arrayUnion(reply));

            //End fragment
            getActivity().getSupportFragmentManager().beginTransaction().replace(LivePoll.display,
                    new WaitingFragment()).commit();
        });

        return root;
    }
}