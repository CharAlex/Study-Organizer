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
        return view;
    }

    private void setExpandableListView(View view){
        //Get all the subject from database
        List<Subject> list = sDatabase.subjectDao().getAll();
        expandableListView =  view.findViewById(R.id.passed_marks_list);
        expandableListAdapter = new MarksAdapter(list,this.getContext());
        expandableListView.setAdapter(expandableListAdapter);

    }

}
