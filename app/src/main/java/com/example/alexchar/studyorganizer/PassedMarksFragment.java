package com.example.alexchar.studyorganizer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.alexchar.studyorganizer.adapters.MarksAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PassedMarksFragment extends Fragment {
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    SubjectDatabase sDatabase;
    FloatingActionButton floatingActionButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.passed_marks_fragment,container,false);
        sDatabase = SubjectDatabase.getSubjectDatabase(getActivity());
        //sDatabase.subjectDao().deleteAll();
        setExpandableListView(view);
        floatingButtonAction(view);
        return view;
    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("1o Εξάμηνο");
        listDataHeader.add("2");
        listDataHeader.add("3");
        listDataHeader.add("Εξάμηνο");

        List<String> edmtDev = new ArrayList<>();
        edmtDev.add("Γραφικά 5");
        edmtDev.add("Προγραμματισμός διαδικτύου 10");

        List<String> androidStudio = new ArrayList<>();
        androidStudio.add("Expandable ListView");
        androidStudio.add("Google Map");
        androidStudio.add("Chat Application");
        androidStudio.add("Firebase ");

        List<String> xamarin = new ArrayList<>();
        xamarin.add("Xamarin Expandable ListView");
        xamarin.add("Xamarin Google Map");
        xamarin.add("Xamarin Chat Application");
        xamarin.add("Xamarin Firebase ");

        List<String> uwp = new ArrayList<>();
        uwp.add("UWP Expandable ListView");
        uwp.add("UWP Google Map");
        uwp.add("UWP Chat Application");
        uwp.add("UWP Firebase ");

        listHash.put(listDataHeader.get(0),edmtDev);
        listHash.put(listDataHeader.get(1),androidStudio);
        listHash.put(listDataHeader.get(2),xamarin);
        listHash.put(listDataHeader.get(3),uwp);

    }

    private void floatingButtonAction(View view) {
        floatingActionButton = view.findViewById(R.id.add_mark_flbutton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment();
            }
        });
    }

    private void setExpandableListView(View view){
        //Get all the subject from database
        List<Subject> list = sDatabase.subjectDao().getAll();
        expandableListView =  view.findViewById(R.id.passed_marks_list);
        expandableListAdapter = new MarksAdapter(list,this.getContext());
        expandableListView.setAdapter(expandableListAdapter);

    }
    private void openFragment(){
        SetMarkFragment fragment = new SetMarkFragment();
        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction().add(R.id.mark_fragment_container, fragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
