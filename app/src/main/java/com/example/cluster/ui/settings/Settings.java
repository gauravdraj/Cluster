package com.example.cluster.ui.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cluster.SettingsAbout;
import com.example.cluster.SettingsChangeName;
import com.example.cluster.SettingsChangePassword;
import com.example.cluster.SettingsViewCluster;

import com.example.cluster.R;
import com.example.cluster.ui.settings.SettingsViewModel;


import java.util.Set;

public class Settings extends Fragment {

    private SettingsViewModel settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        //Possible settings
        String[] settings = {"About", "Change Name", "Change Password"};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, settings);

        //Setting adapter
        ListView listView = (ListView) root.findViewById(R.id.lstSettings);
        listView.setAdapter(adapter);

        //Setting items to be clickable
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <? > arg0, View view, int position, long id) {
                switch(position) {
                    // about
                    case 0:
                        Intent i1 = new Intent(getActivity(), SettingsAbout.class);
                        i1.putExtra("SettingsAbout", settings[position]);
                        startActivity(i1);
                        break;
                    // change name
                    case 1:
                        Intent i2 = new Intent(getActivity(), SettingsChangeName.class);
                        i2.putExtra("SettingsUserSwitch", settings[position]);
                        startActivity(i2);
                        break;
                    // change password
                    case 2:
                        Intent i3 = new Intent(getActivity(), SettingsChangePassword.class);
                        i3.putExtra("SettingsPasswordSwitch", settings[position]);
                        startActivity(i3);
                        break;
                }
            }

        });
        return root;
    }
}