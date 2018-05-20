package com.example.alexchar.studyorganizer.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.SubjectDatabase;
import com.example.alexchar.studyorganizer.TaskDatabase;
import com.example.alexchar.studyorganizer.activities.MarkActivity;
import com.example.alexchar.studyorganizer.activities.TaskActivity;
import com.example.alexchar.studyorganizer.entities.Subject;
import com.example.alexchar.studyorganizer.entities.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class setTaskFragment extends Fragment {
    private Button cancel_button, save_button;
    private EditText datepicker_button, timepicker_button, datepicker, timepicker, title, notes;
    private AutoCompleteTextView autoCompleteTextView;
    private SubjectDatabase sDatabase;
    private List<Subject> subjectList;
    private List<String> subjectNamesList = new ArrayList<>();
    private List<Integer> subjectIdsList = new ArrayList<>();
    private int selectedSubjectId;
    private Calendar mCurrentDate;
    private int day, month, year, hour, minute;
    private Task task = new Task();
    private TaskDatabase tDatabase;

    public setTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_task, container, false);
        tDatabase = TaskDatabase.getTaskDatabase(getActivity());
        setAutoCompleteTextView(view);
        setButtons(view);
        return view;
    }

    private void setButtons(final View viewFragment) {
        cancel_button = viewFragment.findViewById(R.id.cancel_button);
        save_button = viewFragment.findViewById(R.id.save_button);
        datepicker_button = viewFragment.findViewById(R.id.datepicker_button);
        timepicker_button = viewFragment.findViewById(R.id.timepicker_button);

        setCurrentDate();
        setCurrentTime();

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

//        DatePicker Button
        datepicker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        task.setTaskDueYear(year);
                        task.setTaskDueMonth(month);
                        task.setTaskDueDay(day);
//                        Print the real month and not the index of it
                        month++;
                        datepicker = viewFragment.findViewById(R.id.datepicker_button);
                        String setDate = ((day) < 10 ? "0" : "") + day + "/" + ((month) < 10 ? "0" : "") + month + "/" + ((year) < 10 ? "0" : "") + year;
                        datepicker.setText(setDate);
//                        Only if the date is set then enable timepicker_button
                        timepicker.setEnabled(true);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        timepicker = viewFragment.findViewById(R.id.timepicker_button);
        timepicker.setEnabled(false);

        timepicker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        task.setTaskDueHour(hour);
                        task.setTaskDueMinute(minute);
                        String setTime = ((hour) < 10 ? "0" : "") + hour + ":" + ((minute) < 10 ? "0" : "") + minute;
                        timepicker.setText(setTime);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

//        SaveButton
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = viewFragment.findViewById(R.id.set_title_input);
                notes = viewFragment.findViewById(R.id.notes);
//                Check if the title is null
                if (!title.getText().toString().equals("")) {
                    task.setTaskName(title.getText().toString());
                    task.setSubjectId(selectedSubjectId);
                    task.setTaskNotes(notes.getText().toString());
                    tDatabase.taskDao().insertAll(task);
                    Intent intent = new Intent(getActivity(), TaskActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Το task προστέθηκε!", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getActivity());
                    }
                    builder.setTitle("Δημιουργήστε Task")
                            .setMessage("Το πεδίο με τον τίτλο δεν μπορεί να είναι κενό.")
                            .setNeutralButton("Οκ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            }
        });
    }

    private void setCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    private void setCurrentDate() {
        mCurrentDate = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DATE);
//        Months indexes starting at 0
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);
    }

    private void setAutoCompleteTextView(View view) {
        autoCompleteTextView = view.findViewById(R.id.autocomplete_search);
        ImageView dropdownButton = view.findViewById(R.id.dropdown_icon);
        sDatabase = SubjectDatabase.getSubjectDatabase(getActivity());
        subjectList = sDatabase.subjectDao().getAll();


//        Getting the names of the subjects and adding them in to the array
        for (Subject subject : subjectList) {
            subjectNamesList.add(subject.getSubjectName());
//            Store ids of the subjects in same position in order to use this id to update the selected subject grade
            subjectIdsList.add(subject.getSid());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, subjectNamesList);
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
                if (!b) {
                    String typedSubject = autoCompleteTextView.getText().toString();

                    ListAdapter listAdapter = autoCompleteTextView.getAdapter();
                    for (int i = 0; i < listAdapter.getCount(); i++) {
                        String existingSubject = listAdapter.getItem(i).toString();
                        if (typedSubject.equals(existingSubject)) {
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
