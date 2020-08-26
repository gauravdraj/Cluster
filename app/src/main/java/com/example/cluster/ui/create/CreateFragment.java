package com.example.cluster.ui.create;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cluster.Lobby;
import com.example.cluster.MainActivity;
import com.example.cluster.R;
import com.example.cluster.ui.join.JoinViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateFragment extends Fragment {

    private CreateViewModel createViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        createViewModel =
                ViewModelProviders.of(this).get(CreateViewModel.class);
        View root = inflater.inflate(R.layout.fragment_create, container, false);


        //Button to create a new cluster
        Button button = root.findViewById(R.id.createButton);
        button.setOnClickListener(v -> {

            //Getting relevant info from UI elements
            String clusterName = ((EditText) root.findViewById(R.id.createField)).getText().toString();
            String code = ((EditText) root.findViewById(R.id.codeField)).getText().toString();

            //Check if fields are filled in
            if (code.isEmpty() || clusterName.isEmpty()) {
                sendInvalidAlert();
            }

            else {
                //Create data to store in firebase
                Map<String, Object> data = new HashMap<>();
                data.put("active", true);
                data.put("name", clusterName);
                data.put("joinCode", code);
                data.put("members", new ArrayList<>());
                data.put("meetings", new ArrayList<>());
                data.put("ownerEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                addToFireBase(data);
            }

        });

        return root;

    }

    /**
     * Creates cluster in firebase
     * Makes user "owner" of their own cluster
     * @param data that will be passed to store in firebase
     */
    private void addToFireBase(Map<String, Object> data) {
        FirebaseFirestore.getInstance().collection("clusters")
                .add(data).addOnSuccessListener(documentReference -> {
                    //Notify user that cluster was created
                    Toast.makeText(getContext(), "Cluster created", Toast.LENGTH_SHORT).show();
                    String ID = documentReference.getId();
                    createCommunity(ID);
                })
        .addOnFailureListener(e ->
                //If there is issues with firebase or internet
                Toast.makeText(getContext(), "Failed to create Cluster",
                Toast.LENGTH_SHORT).show());
    }

    /**
     * Creates community with the same cluster ID
     * @param ID of the cluster
     */
    private void createCommunity(String ID) {

        //Creating the community
        FirebaseFirestore.getInstance().collection("community").document(ID)
                .set(new HashMap<>());

        //Adding post collection in the community
        FirebaseFirestore.getInstance().collection("community").document(ID)
                .collection("posts");

        //Adding daily polling objects
        FirebaseFirestore.getInstance().collection("clusters").document(ID)
                .collection("dailypolls").add(new HashMap<>());

        //Adding live polling objects
        Map<String, Object> emptyData = new HashMap<>();
        emptyData.put("emails", new ArrayList<>());
        emptyData.put("replies", new ArrayList<>());
        emptyData.put("question", new String());

        FirebaseFirestore.getInstance().collection("clusters").document(ID)
                .collection("livepolls").document("live")
                .set(emptyData);
    }


    /**
     * Creates and Sends an Invalid Alert if logging in fails
     */
    private void sendInvalidAlert() {
        getActivity().runOnUiThread(() -> {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext(),
                    R.style.MyAlertDialogStyle).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Missing fields");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        });
    }
}