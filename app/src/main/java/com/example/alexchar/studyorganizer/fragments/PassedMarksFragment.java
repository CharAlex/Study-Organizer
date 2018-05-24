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
import com.example.alexchar.studyorganizer.database.SubjectDatabase;
import com.example.alexchar.studyorganizer.entities.Subject;
import com.example.alexchar.studyorganizer.adapters.PassedMarksAdapter;

import java.util.List;

public class PassedMarksFragment extends Fragment {
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    SubjectDatabase sDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.marks_fragment,container,false);
        sDatabase = SubjectDatabase.getSubjectDatabase(getActivity());
        //sDatabase.subjectDao().deleteAll();
        setExpandableListView(view);
        setPassedTotal(view);

        return view;
    }

    private void setPassedTotal(View view) {
        int count = 0;
        sDatabase = SubjectDatabase.getSubjectDatabase(getContext());
        List<Subject> subjects = sDatabase.subjectDao().getAll();
        for(Subject i: subjects){
            if(i.getSubjectGrade() >= 5 ){
                count++;
            }
        }
        TextView passed_marks = view.findViewById(R.id.marks_total);
        passed_marks.setText("Περασμένα: " + count);
    }

    private void setExpandableListView(View view){
        //Get all the subject from database
        List<Subject> list = sDatabase.subjectDao().getAll();
        expandableListView =  view.findViewById(R.id.marks_list);
        expandableListAdapter = new PassedMarksAdapter(list,this.getContext());
        expandableListView.setAdapter(expandableListAdapter);

    }

}
