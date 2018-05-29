package com.example.alexchar.studyorganizer.fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alexchar.studyorganizer.AlertReceiver;
import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.database.SubjectDatabase;
import com.example.alexchar.studyorganizer.database.TaskDatabase;
import com.example.alexchar.studyorganizer.activities.TaskActivity;
import com.example.alexchar.studyorganizer.entities.Subject;
import com.example.alexchar.studyorganizer.entities.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class TaskSetFragment extends Fragment {
    private Button cancel_button, save_button;
    private EditText datepicker_button, timepicker_button, datepicker, timepicker, title, notes;
    private SubjectDatabase sDatabase;
    private List<Subject> subjectList;
    private List<String> subjectNamesList = new ArrayList<>();
    private List<Integer> subjectIdsList = new ArrayList<>();
    private int selectedSubjectId, requestCode = 1, autocompletedSubjectId;
    private Calendar mCurrentDate;
    private int day = -1, month = -1, year = -1, hour = -1, minute = -1;
    private Task task = new Task(), editTaskObject;
    private TaskDatabase tDatabase;
    private ImageButton clear_date_button, clear_time_button;
    private AutoCompleteTextView autoCompleteTextView;

    public TaskSetFragment() {
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
        clear_date_button = mView.findViewById(R.id.clear_date_button);
        clear_date_button.setVisibility(View.INVISIBLE);
        clear_time_button = mView.findViewById(R.id.clear_time_button);
        clear_time_button.setVisibility(View.INVISIBLE);
        timepicker.setEnabled(false);
    }

    private void setFields(int editTaskId) {
        tDatabase = TaskDatabase.getTaskDatabase(getActivity());
        editTaskObject = tDatabase.taskDao().findById(editTaskId);

        title.setText(editTaskObject.getTaskName());
        notes.setText(editTaskObject.getTaskNotes());

        String setDate = ((editTaskObject.getTaskDueDay()) < 10 ? "0" : "") + editTaskObject.getTaskDueDay() + "/" + ((editTaskObject.getTaskDueMonth()) < 10 ? "0" : "") + editTaskObject.getTaskDueMonth() + "/" + ((editTaskObject.getTaskDueYear()) < 10 ? "0" : "") + editTaskObject.getTaskDueYear();
        String setTime = ((editTaskObject.getTaskDueHour()) < 10 ? "0" : "") + editTaskObject.getTaskDueHour() + ":" + ((editTaskObject.getTaskDueMinute()) < 10 ? "0" : "") + editTaskObject.getTaskDueMinute();

        if (editTaskObject.getTaskDueDay() != -1) {
            datepicker_button.setText(setDate);
        }

        if (editTaskObject.getTaskDueHour() != -1) {
            timepicker_button.setText(setTime);
            timepicker_button.setEnabled(true);
        }
        if (!datepicker_button.getText().toString().equals("Ημερομηνία")) {
            clear_date_button.setVisibility(View.VISIBLE);
            timepicker_button.setEnabled(true);
        }

        if (!timepicker_button.getText().toString().equals("Ώρα"))
            clear_time_button.setVisibility(View.VISIBLE);

        clear_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!datepicker_button.getText().toString().equals("Ημερομηνία"))
                    datepicker_button.setText("Ημερομηνία");
                clear_date_button.setVisibility(View.INVISIBLE);
            }
        });

        clear_time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timepicker_button.getText().toString().equals("Ώρα"))
                    timepicker_button.setText("Ώρα");
                clear_time_button.setVisibility(View.INVISIBLE);
            }
        });

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE,editTaskObject.getTaskDueDay());
        cal.set(Calendar.MONTH,editTaskObject.getTaskDueMonth());
        cal.set(Calendar.YEAR,editTaskObject.getTaskDueYear());

        save_button.setText("Αποθήκευση");
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!title.getText().toString().equals("") && ((datepicker.getText().toString().equals("Ημερομηνία") && timepicker_button.getText().toString().equals("Ώρα")) || (!datepicker.getText().toString().equals("Ημερομηνία") && !timepicker_button.getText().toString().equals("Ώρα")))) {
                    String name = title.getText().toString();
                    int sId = editTaskObject.getSubjectId();

                    int year = -1;
                    month = -1;
                    day = -1;
                    if (!datepicker_button.getText().toString().equals("Ημερομηνία")) {
                        String[] mDate = datepicker_button.getText().toString().split("/");
                        year = Integer.valueOf(mDate[2]);
                        month = Integer.valueOf(mDate[1]);
                        day = Integer.valueOf(mDate[0]);
                    }

                    int hour = -1, minute = -1;
                    if (!timepicker_button.getText().toString().equals("Ώρα")) {
                        String[] mTime = timepicker_button.getText().toString().split(":");
                        hour = Integer.valueOf(mTime[0]);
                        minute = Integer.valueOf(mTime[1]);
                    }

                    String mNotes = notes.getText().toString();
                    tDatabase.taskDao().update(name, sId, year, month, day, hour, minute, mNotes, editTaskObject.getTid());
                    if (!datepicker_button.getText().toString().equals("Ημερομηνία")) {
                        setAlarm(editTaskObject.getTid());
                    }else{
                        cancelAlarm(editTaskObject.getTid());
                    }

                    Intent intent = new Intent(getActivity(), TaskActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Το task ενημερώθηκε!", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getActivity());
                    }
                    String text = "Το πεδίο με τον τίτλο δεν μπορεί να είναι κενό.";
                    if (!datepicker.getText().toString().equals("Ημερομηνία") && timepicker_button.getText().toString().equals("Ώρα")) {
                        text = "Για τον καθορισμό της προθεσμίας είναι απαραίτητο να συμπληρώσετε την ώρα";
                    }
                    builder.setTitle("Επεξεργασία Task")
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

    private void setButtons(final View mView) {
        setAutoCompleteTextView(mView);
        setCurrentDate();
        setCurrentTime();

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

//        if datepicker and timepicker isnt pressed at all
        task.setTaskDueYear(-1);
        task.setTaskDueMonth(-1);
        task.setTaskDueDay(-1);
        task.setTaskDueMinute(-1);
        task.setTaskDueHour(-1);

//        DatePicker Button
        datepicker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker datePicker, int year, int month, int day) {
                        //                        Print the real month and not the index of it
                        month++;
                        task.setTaskDueYear(year);
                        task.setTaskDueMonth(month);
                        task.setTaskDueDay(day);
                        String setDate = ((day) < 10 ? "0" : "") + day + "/" + ((month) < 10 ? "0" : "") + month + "/" + ((year) < 10 ? "0" : "") + year;
                        datepicker.setText(setDate);
//                        If datepicker is set then show the button to clear the text
                        if (!datepicker.getText().toString().equals("Ημερομηνία")) {
                            clear_date_button.setVisibility(View.VISIBLE);
                            clear_date_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    datepicker.setText("Ημερομηνία");
                                    clear_date_button.setVisibility(View.INVISIBLE);
                                    task.setTaskDueYear(-1);
                                    task.setTaskDueMonth(-1);
                                    task.setTaskDueDay(-1);
                                }
                            });
                        }
//                        Only if the date is set then enable timepicker_button
                        timepicker.setEnabled(true);
                    }
                }, editTaskObject != null ? editTaskObject.getTaskDueYear() : year, editTaskObject != null ? editTaskObject.getTaskDueMonth() : month, editTaskObject != null ? editTaskObject.getTaskDueDay() : day);
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
//                        If datepicker is set then show the button to clear the text
                        if (!timepicker.getText().toString().equals("Ώρα")) {
                            clear_time_button.setVisibility(View.VISIBLE);
                            clear_time_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    timepicker.setText("Ώρα");
                                    clear_time_button.setVisibility(View.INVISIBLE);
                                    task.setTaskDueHour(-1);
                                    task.setTaskDueMinute(-1);
                                }
                            });
                        }
                    }
                }, editTaskObject != null ? editTaskObject.getTaskDueHour() : hour, editTaskObject != null ? editTaskObject.getTaskDueMinute() : minute, true);
                timePickerDialog.show();
            }
        });

//        SaveButton
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Check if the title is null
                if (!title.getText().toString().equals("") && ((datepicker.getText().toString().equals("Ημερομηνία") && timepicker_button.getText().toString().equals("Ώρα")) || (!datepicker.getText().toString().equals("Ημερομηνία") && !timepicker_button.getText().toString().equals("Ώρα")))) {
                    task.setTaskName(title.getText().toString());
                    task.setSubjectId(selectedSubjectId);
                    task.setTaskNotes(notes.getText().toString());
                    task.setSubjectId(autocompletedSubjectId);
                    int id = (int) tDatabase.taskDao().insert(task);
                    if(!datepicker.getText().toString().equals("Ημερομηνία"))
                        setAlarm(id);

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
                    if (!datepicker.getText().toString().equals("Ημερομηνία") && timepicker_button.getText().toString().equals("Ώρα")) {
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

    private void setAlarm(int reminderForTaskTid) {
        Calendar mCalendar = Calendar.getInstance();
        if (!datepicker_button.getText().toString().equals("Ημερομηνία")) {
            int mYear, mMonth, mDay, mHour, mMinute;

            String[] mDate = datepicker_button.getText().toString().split("/");
            mYear = Integer.valueOf(mDate[2]);
            mMonth = Integer.valueOf(mDate[1]) - 1;
            mDay = Integer.valueOf(mDate[0]);

            String[] mTime = timepicker_button.getText().toString().split(":");
            mHour = Integer.valueOf(mTime[0]);
            mMinute = Integer.valueOf(mTime[1]);
            mCalendar.set(Calendar.YEAR, mYear);
            mCalendar.set(Calendar.DATE, mDay);
            mCalendar.set(Calendar.MONTH, mMonth);
            mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
            mCalendar.set(Calendar.MINUTE, mMinute);
            mCalendar.set(Calendar.SECOND, 0);

            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), reminderForTaskTid, intent, 0);
            Log.d(TAG, "setAlarm: TID SET " + reminderForTaskTid );
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);

        }
    }

    private void cancelAlarm(int reminderForTaskTid) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), reminderForTaskTid, intent, 0);
        Log.d(TAG, "setAlarm: TID cancel " + reminderForTaskTid );
        alarmManager.cancel(pendingIntent);
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
                            autocompletedSubjectId = subjectIdsList.get(i);
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
                autocompletedSubjectId = subjectIdsList.get(index);
                Log.d(TAG, "ID SELECTED: " + selectedSubjectId);
            }
        });
    }
}
