package com.example.cluster.ui.clusters;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cluster.ClusterSortContainer;
import com.example.cluster.Lobby;
import com.example.cluster.MyListAdapter;
import com.example.cluster.R;
import com.example.cluster.StudentViewCluster;
import com.example.cluster.TeacherView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.concurrent.ExecutionException;

public class ClustersFragment extends Fragment {

    private ClustersViewModel clustersViewModel;

    //User email
    private String userEmail;
    //Listview in xml page
    private ListView listView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        clustersViewModel =
                ViewModelProviders.of(this).get(ClustersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_clusters, container, false);

        listView = (ListView) root.findViewById(R.id.lstMain);
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //Start Async task after settings elements
        new MyTask().execute();


        return root;
    }

    /**
     * Sorts clusters
     */
    class SortAlphabetical implements Comparator<ClusterSortContainer> {
        public int compare(ClusterSortContainer a, ClusterSortContainer b) {
            String lower_a = (a.getClusterName()).toLowerCase();
            String lower_b = (b.getClusterName()).toLowerCase();
            return lower_a.compareTo(lower_b);
        }
    }

    /**
     * AsyncTask that gets relevant info from Firebase
     */
    private class MyTask extends AsyncTask<Void, Void, Void> {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseFirestore.getInstance()
                    .collection("clusters")
                    .get()
                    .addOnCompleteListener(task -> {

                        ArrayList<String> list = new ArrayList<>();
                        ArrayList<String> clusterID = new ArrayList<>();

                        //Check if task worked
                        if (task.isComplete()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                //Look if user owns the cluster
                                String clusterName = documentSnapshot.getString("name");
                                String ownerEmail = documentSnapshot.getString("ownerEmail");
                                List<String> members = (List<String>) documentSnapshot.get("members");
                                boolean isMember = false;

                                //First check if its a owner
                                if (ownerEmail.equals(userEmail)) {
                                    list.add(clusterName + " (Owner)");
                                    clusterID.add(documentSnapshot.getId());
                                    continue;
                                }

                                //Then member
                                for (String m : members) {
                                    if (m.equals(userEmail)) {
                                        isMember = true;
                                        clusterID.add(documentSnapshot.getId());
                                        break;
                                    }
                                }

                                //If member add to list, otherwise don't add it
                                if (isMember)
                                    list.add(clusterName);
                            }

                            //Sort list to show owner then members clusters
                            this.sortList(list, clusterID);

                            //Custom Listview adapter
                            MyListAdapter adapter = new MyListAdapter(getActivity(), list, true);

                            listView.setAdapter(adapter);

                            //Set what each item does
                            listView.setOnItemClickListener((parent, view, position, id) -> {

                                //This is where we can choose what view works
                                Intent i;
                                if (list.get(position).contains("Owner")) {
                                    i =  new Intent(getActivity(), TeacherView.class);
                                } else {
                                    i = new Intent(getActivity(), StudentViewCluster.class);
                                }
                                //Send relevant info to get page
                                i.putExtra("Cluster", list.get(position));
                                i.putExtra("ID", clusterID.get(position));
                                startActivity(i);
                            });
                        }
                    });
            return null;
        }

        /**
         * Custom sorting algorithm that sort by status and alphabetically
         * while keeping the ids in the same position
         * @param list of names of the clusters
         * @param clusterID that are associated with the cluster
         */
        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void sortList(List<String> list, List<String> clusterID) {

            List<ClusterSortContainer> sorter = new ArrayList<>();
            for (int i = 0; i < clusterID.size(); i++) {
                sorter.add(new ClusterSortContainer(list.get(i), clusterID.get(i)));
            }

            // Partition, then sort each
            List<ClusterSortContainer> owners = sorter.stream().filter(s -> (s.getClusterName()).endsWith("(Owner)")).collect(Collectors.toList());
            List<ClusterSortContainer> members = sorter.stream().filter(s-> !((s.getClusterName()).endsWith("(Owner)"))).collect(Collectors.toList());

            Comparator<ClusterSortContainer> sortAlphabetical = new SortAlphabetical();
            owners.sort(sortAlphabetical);
            members.sort(sortAlphabetical);

            list.clear();
            clusterID.clear();

            owners.forEach(y->list.add(y.getClusterName()));
            owners.forEach(z->clusterID.add(z.getClusterID()));
            members.forEach(a->list.add(a.getClusterName()));
            members.forEach(b->clusterID.add(b.getClusterID()));
        }
    }

}