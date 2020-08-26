package com.example.cluster;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<String> {

    //Field for the list
    private final Activity context;
    private final ArrayList<String> clusterNames;
    private final boolean clusterView;

    /**
     * Constructor for Custom list
     * @param context Activity context
     * @param clusterNames Names of all the clusters
     * @param clusterView Showing in community or not
     */
    public MyListAdapter(Activity context, ArrayList<String> clusterNames, boolean clusterView) {
        super(context, R.layout.row, clusterNames);

        this.context = context;
        this.clusterNames = clusterNames;
        this.clusterView = clusterView;
    }

    /**
     * Inflates list with specific fields
     * @param position List position
     * @param view View Object
     * @param parent ViewGroup that the view object is in
     * @return view of the custom list
     */
    public View getView(int position, View view, ViewGroup parent) {

        //Linking UI elements
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.row, null, true);

        TextView cluster = (TextView) rowView.findViewById(R.id.clusterName);

        //Checking if any strings have "(Owner)" written on them
        String clusterTitle = "";
        if (clusterNames.get(position).contains("(Owner)")) {
            for (int i = 0; i < clusterNames.get(position).length(); i++) {
                if (clusterNames.get(position).charAt(i) == '(') {
                    clusterTitle = clusterNames.get(position).substring(0, i);
                    break;
                }
            }
            cluster.setText(clusterTitle);
        }
        else
            cluster.setText(clusterNames.get(position));

        //Setting member status of the cluster
        TextView memberStatus = (TextView) rowView.findViewById(R.id.memberStatus);
        if (clusterView) {
            if (clusterNames.get(position).contains("(Owner)"))
                memberStatus.setText("Owner");
            else
                memberStatus.setText("Member");
        }
        else {
            memberStatus.setText("Community Open");
        }
        return rowView;
    }
}
