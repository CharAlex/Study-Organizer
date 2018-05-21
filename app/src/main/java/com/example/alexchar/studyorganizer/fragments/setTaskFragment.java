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
    private SubjectDatabase sDatabase;
    private List<Subject> subjectList;
    private List<String> subjectNamesList = new ArrayList<>();
    private List<Integer> subjectIdsList = new ArrayList<>();
    private int selectedSubjectId;
    private Calendar mCurrentDate;
    private int day, month, year, hour, minute;
    private Task task = new Task(), editTaskObject;
    private TaskDatabase tDatabase;

    public setTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_task, container, false);
        tDatabase = TaskDatabase.getTaskDatabase(getActivity());
        setViews(view);
        setButtons(view);
        Bundle bundle = this.getArguments();
//        If the bundle is not null it means that the adapter parsed this taskId to edit the task
        if (bundle != null) {
            int editTaskId = bundle.getInt("taskId", 1);
            setFields(editTaskId);
        }
        return view;
    }

    private void setViews(View mView) {
        notes = mView.findViewById(R.id.notes);
        title = mView.findViewById(R.id.set_title_input);
        notes = mView.findViewById(R.id.notes);
        cancel_button = mView.findViewById(R.id.cancel_button);
        save_button = mView.findViewById(R.id.save_button);
        datepicker_button = mView.findViewById(R.id.datepicker_button);
        timepicker_button = mView.findViewById(R.id.timepicker_button);
        datepicker = datepicker_button;
        timepicker = timepicker_button;
        timepicker.setEnabled(false);
    }

    private void setFields(int editTaskId) {
        tDatabase = TaskDatabase.getTaskDatabase(getActivity());
        editTaskObject = tDatabase.taskDao().findById(editTaskId);

        title.setText(editTaskObject.getTaskName());
        notes.setText(editTaskObject.getTaskNotes());

        String setDate = ((editTaskObject.getTaskDueDay()) < 10 ? "0" : "") + editTaskObject.getTaskDueDay() + "/" + ((editTaskObject.getTaskDueMonth()) < 10 ? "0" : "") + editTaskObject.getTaskDueMonth() + "/" + ((editTaskObject.getTaskDueYear()) < 10 ? "0" : "") + editTaskObject.getTaskDueYear();
        String setTime = ((editTaskObject.getTaskDueHour()) < 10 ? "0" : "") + editTaskObject.getTaskDueHour() + ":" + ((editTaskObject.getTaskDueMinute()) < 10 ? "0" : "") + editTaskObject.getTaskDueMinute();

        if(editTaskObject.getTaskDueDay() != 0 ){
            datepicker_button.setText(setDate);
        }
        if(editTaskObject.getTaskDueHour() != 0 ){
            timepicker_button.setText(setTime);
            timepicker_button.setEnabled(true);
        }
        save_button.setText("Αποθήκευση");
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!title.getText().toString().equals("")) {
                    String name = title.getText().toString();
                    int sId = editTaskObject.getSubjectId();
                    String[] mDate = datepicker_button.getText().toString().split("/");
                    int year = Integer.valueOf(mDate[2]);
                    int month = Integer.valueOf(mDate[1]);
                    int day = Integer.valueOf(mDate[0]);
                    String[] mTime = timepicker_button.getText().toString().split(":");
                    int hour = Integer.valueOf(mTime[0]);
                    int minute = Integer.valueOf(mTime[0]);
                    String mNotes = notes.getText().toString();
                    tDatabase.taskDao().update(name,sId,year,month,day,hour,minute,mNotes,editTaskObject.getTid());
                    Intent intent = new Intent(getActivity(), TaskActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Το task ενημερώθηκε!", Toast.LENGTH_LONG).show();
                }
                else {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getActivity());
                    }
                    builder.setTitle("Επεργασία Task")
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

    private void setButtons(final View mView) {
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
                        String setDate = ((day) < 10 ? "0" : "") + day + "/" + ((month) < 10 ? "0" : "") + month + "/" + ((year) < 10 ? "0" : "") + year;
                        datepicker.setText(setDate);
//                        Only if the date is set then enable timepicker_button
                        timepicker.setEnabled(true);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

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
//                Check if the title is null
                if (!title.getText().toString().equals("") && ((datepicker.getText().toString().equals("Ημερομηνία") && timepicker_button.getText().toString().equals("Ώρα")) || (!datepicker.getText().toString().equals("Ημερομηνία") && !timepicker_button.getText().toString().equals("Ώρα")))){
                    task.setTaskName(title.getText().toString());
                    task.setSubjectId(selectedSubjectId);
                    task.setTaskNotes(notes.getText().toString());
                    tDatabase.taskDao().insertAll(task);
                    Intent intent = new Intent(getActivity(), TaskActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Το task προστέθηκε!", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getActivity());
                    }
                    String text = "Το πεδίο με τον τίτλο δεν μπορεί να είναι κενό.";
                    if(!datepicker.getText().toString().equals("Ημερομηνία") && timepicker_button.getText().toString().equals("Ώρα")){
                        text = "Για τον καθορισμό της προθεσμίας είναι απαραίτητο να συμπληρώσετε την ώρα";
                    }
                    builder.setTitle("Δημιουργήστε Task")
                            .setMessage(text)
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


}
