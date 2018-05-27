package com.example.alexchar.studyorganizer.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.activities.ExamActivity;
import com.example.alexchar.studyorganizer.database.ExamDatabase;
import com.example.alexchar.studyorganizer.database.SubjectDatabase;
import com.example.alexchar.studyorganizer.entities.Exam;
import com.example.alexchar.studyorganizer.entities.Subject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;


public class ExamSetFragment extends Fragment {
    private AutoCompleteTextView autoCompleteTextView;
    private TextInputEditText examRoom;
    private SubjectDatabase sDatabase;
    private List<Subject> subjectList;
    private List<String> subjectNamesList = new ArrayList<>();
    private List<Integer> subjectIdsList = new ArrayList<>();
    private Button cancelButton, saveButton;
    private EditText timepicker_button, datepicker_button;
    private ImageButton clear_time_button, clear_date_button;
    private int selectedSubjectId, hour = -1, minute = -1,year, month, day, selectedDay, selectedMonth, selectedYear, selectedMinute, selectedHour;
    private CheckBox checkBoxHard, checkBoxMedium,checkBoxEasy;
    private ExamDatabase eDatabase;

    public ExamSetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam_set, container, false);
        eDatabase = ExamDatabase.getExamDatabase(getContext());
        sDatabase = SubjectDatabase.getSubjectDatabase(getContext());
        setFields(view);
        setButtons(view);
        return view;
    }


    private void setFields(View mView) {
        cancelButton = mView.findViewById(R.id.cancel_button);
        saveButton = mView.findViewById(R.id.submit_button);
        timepicker_button = mView.findViewById(R.id.timepicker_button);
        datepicker_button = mView.findViewById(R.id.datepicker_button);
        clear_time_button = mView.findViewById(R.id.clear_time_button);
        clear_time_button.setVisibility(View.INVISIBLE);
        clear_date_button = mView.findViewById(R.id.clear_date_button);
        clear_date_button.setVisibility(View.INVISIBLE);
        checkBoxHard = mView.findViewById(R.id.checkBox_hard);
        checkBoxMedium = mView.findViewById(R.id.checkBox_medium);
        checkBoxEasy = mView.findViewById(R.id.checkBox_easy);
        examRoom = mView.findViewById(R.id.exam_room);
    }

    private void setButtons(View mView) {
        setCancelButton(cancelButton);
        setAutoCompleteTextView(mView);
        setCurrentTime();
        setTimePicker(mView);
        setCurrentDate();
        setDatePicker(mView);
        setCheckBoxActions();
        setSaveButton();
    }

    private void setCheckBoxActions() {
        checkBoxHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxMedium.setChecked(false);
                checkBoxEasy.setChecked(false);
            }
        });
        checkBoxMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxHard.setChecked(false);
                checkBoxEasy.setChecked(false);
            }
        });
        checkBoxEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxHard.setChecked(false);
                checkBoxMedium.setChecked(false);
            }
        });
    }

    private void setTimePicker(View mView) {
        timepicker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        selectedMinute = minute;
                        selectedHour = hour;

                        String setTime = ((hour) < 10 ? "0" : "") + hour + ":" + ((minute) < 10 ? "0" : "") + minute;
                        timepicker_button.setText(setTime);
//                        If datepicker is set then show the button to clear the text
                        if (!timepicker_button.getText().toString().equals("Ώρα")) {
                            clear_time_button.setVisibility(View.VISIBLE);
                            clear_time_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    timepicker_button.setText("Ώρα");
                                    clear_time_button.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });
    }

    private void setDatePicker(View mView){
        //        DatePicker Button
        datepicker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker datePicker, int year, int month, int day) {
                        selectedDay = day;
                        selectedMonth = month;
                        selectedYear = year;

//                        Print the real month and not the index of it
                        month++;
                        String setDate = ((day) < 10 ? "0" : "") + day + "/" + ((month) < 10 ? "0" : "") + month + "/" + ((year) < 10 ? "0" : "") + year;
                        datepicker_button.setText(setDate);
//                        If datepicker is set then show the button to clear the text
                        if (!datepicker_button.getText().toString().equals("Ημερομηνία")) {
                            clear_date_button.setVisibility(View.VISIBLE);
                            clear_date_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    datepicker_button.setText("Ημερομηνία");
                                    clear_date_button.setVisibility(View.INVISIBLE);
//                                    task.setTaskDueYear(-1);
//                                    task.setTaskDueMonth(-1);
//                                    task.setTaskDueDay(-1);
                                }
                            });
                        }
//                        Only if the date is set then enable timepicker_button
                        datepicker_button.setEnabled(true);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void setCancelButton(Button cancelButton){
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
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
                            selectedSubjectId = subjectIdsList.get(i);
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

    private void setCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DATE);
//        Months indexes starting at 0
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
    }

    private void setSaveButton() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!autoCompleteTextView.getText().toString().equals("") && !examRoom.getText().toString().equals("")
                        && !datepicker_button.getText().toString().equals("Ημερομηνία") && !timepicker_button.getText().toString().equals("Ώρα")){
                    //        First create Exam entity to store the input data and then add this entity to the database
                    Exam exam = new Exam();
                    exam.setSubjectId(selectedSubjectId);
                    exam.setExamDay(selectedDay);
                    exam.setExamMonth(selectedMonth);
                    exam.setExamYear(selectedYear);
                    exam.setExamMinute(selectedMinute);
                    exam.setExamHour(selectedHour);
//                0 = easy, 1 = medium, 2 = hard
                    exam.setExamDifficulty(getDifficulty());
                    exam.setExamRoom(examRoom.getText().toString());
                    eDatabase.examtDao().insert(exam);

                    Intent intent = new Intent(getActivity(), ExamActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Η εξέταση προστέθηκε!", Toast.LENGTH_LONG).show();
                }else {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getActivity());
                    }
                    String text = "Συμπλήρωστε όλα τα πεδία";

                    builder.setTitle("")
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

    private int getDifficulty() {
      if(checkBoxHard.isChecked()){
          return 2;
      }else if(checkBoxMedium.isChecked()){
          return 1;
      }else if(checkBoxEasy.isChecked()){
          return 0;
      }
//      If user doesnt select any difficulty then return -1 as flag
        return -1;
    }
}
