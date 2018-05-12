package com.example.alexchar.studyorganizer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.alexchar.studyorganizer.adapters.PassedMarksAdapter;

import java.util.List;

public class PassedMarksFragment extends Fragment {
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    SubjectDatabase sDatabase;
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
        expandableListAdapter = new PassedMarksAdapter(list,this.getContext());
        expandableListView.setAdapter(expandableListAdapter);

    }

}
