package com.example.alexchar.studyorganizer.activities;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.fragments.FailedMarksFragment;
import com.example.alexchar.studyorganizer.fragments.PassedMarksFragment;
import com.example.alexchar.studyorganizer.fragments.SetMarkFragment;

public class MarkActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private PassedMarksFragment tab1;
    private FailedMarksFragment tab2;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

//      Floating button setup
        floatingButtonAction();
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    tab1 = new PassedMarksFragment();
                    return tab1;
                case 1:
                    tab2 = new FailedMarksFragment();
                    return tab2;
                default: return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

    }

    private void floatingButtonAction() {
        floatingActionButton = findViewById(R.id.add_mark_flbutton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment();
            }
        });
    }
    private void openFragment(){
        SetMarkFragment fragment = new SetMarkFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        /*add_mark_container is the container that is below the viewpager in the xml file so it will
         appear on top of both tabs when fragment is opened*/
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction().add(R.id.add_mark_container, fragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
        floatingActionButton.setVisibility(View.GONE);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FloatingActionButton floatingActionButton = findViewById(R.id.add_mark_flbutton);
        floatingActionButton.setVisibility(View.VISIBLE);
    }
}
