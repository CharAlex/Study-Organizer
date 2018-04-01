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
import android.widget.TextView;

import com.example.alexchar.studyorganizer.adapters.MarksAdapter;

import java.util.List;

public class PassedMarksFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    SubjectDatabase sDatabase;
    FloatingActionButton floatingActionButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.passed_marks_fragment,container,false);
        sDatabase = SubjectDatabase.getSubjectDatabase(getActivity());
        //sDatabase.subjectDao().deleteAll();
        setRecyclerView(view);
        floatingButtonAction(view);
        return view;
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

    private void setRecyclerView(View view){
        //Get all the subject from database
        List<Subject> list = sDatabase.subjectDao().getAll();
        recyclerView = view.findViewById(R.id.passed_marks_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MarksAdapter(list);
        recyclerView.setAdapter(adapter);
    }
    private void openFragment(){
        SetMarkFragment fragment = new SetMarkFragment();
        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction().add(R.id.mark_fragment_container, fragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
