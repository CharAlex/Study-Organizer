package com.example.alexchar.studyorganizer.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.SubjectDatabase;
import com.example.alexchar.studyorganizer.entities.Subject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class setTaskFragment extends Fragment {
    private Button cancel_button,save_button;
    private AutoCompleteTextView autoCompleteTextView;
    private SubjectDatabase sDatabase;
    private List<Subject> subjectList;
    private List<String> subjectNamesList = new ArrayList<>();
    private List<Integer> subjectIdsList = new ArrayList<>();
    private int selectedSubjectId;

    public setTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_task, container, false);
        setAutoCompleteTextView(view);
        setButtons(view);
        return view;
    }
    private void setButtons(View view) {
        cancel_button = view.findViewById(R.id.cancel_button);
        save_button = view.findViewById(R.id.save_button);
//        datepicker_button = view.findViewById(R.id.datepicker_button);
//        timepicker_button = view.findViewById(R.id.timepicker_button);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

//        datepicker_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    private void setAutoCompleteTextView(View view) {
        autoCompleteTextView = view.findViewById(R.id.autocomplete_search);
        ImageView dropdownButton = view.findViewById(R.id.dropdown_icon);
        sDatabase = SubjectDatabase.getSubjectDatabase(getActivity());
        subjectList = sDatabase.subjectDao().getAll();


//        Getting the names of the subjects and adding them in to the array
        for (Subject subject: subjectList){
            subjectNamesList.add(subject.getSubjectName());
//            Store ids of the subjects in same position in order to use this id to update the selected subject grade
            subjectIdsList.add(subject.getSid());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,subjectNamesList);
        autoCompleteTextView.setAdapter(adapter);

//        When the user presses at least one button the autocompletetextview searches the array
        autoCompleteTextView.setThreshold(1);

//        Setting up the dropwdown button for displaying all the subjects
        dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showDropDown();
            }
        });

//        Make sure that user select the name from the existing name stored in subjectNameList
        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    String typedSubject = autoCompleteTextView.getText().toString();

                    ListAdapter listAdapter = autoCompleteTextView.getAdapter();
                    for(int i = 0; i < listAdapter.getCount(); i++) {
                        String existingSubject = listAdapter.getItem(i).toString();
                        if(typedSubject.equals(existingSubject)) {
                            return;
                        }
                    }
//                    if focused changed and the the typed subject isnt equal to any existing subject then set the text to null
                    autoCompleteTextView.setText("");
                }
            }
        });

//        get the id of the subject that was selected from the autocompletetextview
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index = subjectNamesList.indexOf(autoCompleteTextView.getText().toString());
                selectedSubjectId = subjectIdsList.get(index);
                Log.d(TAG, "ID SELECTED: " + selectedSubjectId);
            }
        });
    }
}
