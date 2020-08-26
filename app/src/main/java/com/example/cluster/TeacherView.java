package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class TeacherView extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view);

        //Updating strings to reflect UI
        Intent intent = getIntent();
        String name = intent.getStringExtra("Cluster");
        String title = "";
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '(') {
                title = name.substring(0, i);
                break;
            }
        }

        setTitle(title);

        //Fragment layout for teachers and UI elements
        TabLayout tabLayout = (TabLayout) findViewById(R.id.TeacherTabLayout);
        TabItem tabPrompt = (TabItem) findViewById(R.id.promptTab);
        TabItem tabResponses = (TabItem) findViewById(R.id.responsesTab);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        //Setting adapter
        TeacherViewAdapter adapter = new TeacherViewAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        //When a tab is clicked
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Go to that fragment
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}