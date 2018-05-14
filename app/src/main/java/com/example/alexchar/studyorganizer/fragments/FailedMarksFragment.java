package com.example.alexchar.studyorganizer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.SubjectDatabase;
import com.example.alexchar.studyorganizer.entities.Subject;
import com.example.alexchar.studyorganizer.adapters.FailedMarksAdapter;

import java.util.List;

public class FailedMarksFragment extends Fragment {
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    SubjectDatabase sDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.marks_fragment,container,false);
        View listItemView = inflater.inflate(R.layout.passed_mark_list_item,container,false);
        sDatabase = SubjectDatabase.getSubjectDatabase(getActivity());
        //sDatabase.subjectDao().deleteAll();
        setExpandableListView(view);
        setFailedTotal(view, listItemView);

        return view;
    }

    private void setFailedTotal(View view, View listItemView) {
        int count = 0;
        sDatabase = SubjectDatabase.getSubjectDatabase(getContext());
        List<Subject> subjects = sDatabase.subjectDao().getAll();
        for(Subject i: subjects){
            if(i.getSubjectGrade() < 5 ){
                count++;
            }
        }
        TextView failed_marks = view.findViewById(R.id.marks_total);
        failed_marks.setText("Χρωστούμενα: " + count);
        failed_marks.setBackgroundColor(getActivity().getResources().getColor(R.color.colorFail));
    }

    private void setExpandableListView(View view){
        //Get all the subject from database
        List<Subject> list = sDatabase.subjectDao().getAll();
        expandableListView =  view.findViewById(R.id.marks_list);
        expandableListAdapter = new FailedMarksAdapter(list,this.getContext());
        expandableListView.setAdapter(expandableListAdapter);

    }

}
