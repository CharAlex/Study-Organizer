package com.example.alexchar.studyorganizer.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.database.SubjectDatabase;
import com.example.alexchar.studyorganizer.entities.Subject;

public class EditSubjectActivity extends AppCompatActivity {
    SubjectDatabase sDatabase;
    int id_parsed;
    TextView greenTitle;
    TextView subjectName, subjectTeacher, subjectPoints, subjectHours, subjectRoom, seekBarText;
    RadioButton radioButtonOb, radioButtonCh;
    Button cancelButton, deleteButton, saveButton;
    SeekBar seekBar;
    Subject retrievedSubject;
    private static final String TAG = "EditSubjectActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);
        //link to subject database
        sDatabase = SubjectDatabase.getSubjectDatabase(getApplicationContext());
        //get extra parsed from each item clicked in adapter class
        Intent intent = getIntent();
        id_parsed = intent.getIntExtra("subject id", 0);

        retrievedSubject = sDatabase.subjectDao().findById(id_parsed);

        //set title based on the subject selected
        setBarTitle(id_parsed);
        //find all views
        findViews();

        //Set retrieved Data
        setRetrievedData();




        //Calling method to set TextView value of the seekBar Input
        setSemesterSeekBar(seekBar, seekBarText);
        //Cancel button action
        cancelButtonAction();
        //Delete button action
        deleteButtonAction();
        //Save button action
        saveButtonAction();




    }

    private void saveButtonAction() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(subjectName.getText().length() > 0){
                    String radio;
                    if(radioButtonCh.isChecked()){
                        radio = "Επιλογής";
                    }else{
                        radio = "Υποχρεωτικό";
                    }
                    sDatabase.subjectDao().update(subjectName.getText().toString(), radio, seekBarText.getText().toString(), subjectTeacher.getText().toString(),
                            subjectPoints.getText().toString(),subjectHours.getText().toString(), subjectRoom.getText().toString(), retrievedSubject.getSid());
                    Intent intent = new Intent(EditSubjectActivity.this, SubjectActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Οι αλλαγές αποθηκεύτηκαν!", Toast.LENGTH_LONG).show();
                }
                else{
                    //If user removes name completely
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(EditSubjectActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(EditSubjectActivity.this);
                    }
                    builder.setTitle("Προσθέστε Μάθημα")
                            .setMessage("Το πεδίο με το όνομα του μαθήματος δεν μπορεί να είναι κενό.")
                            .setNeutralButton("Οκ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
    }

    private void deleteButtonAction() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(EditSubjectActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(EditSubjectActivity.this);
                }
                builder.setTitle("Θέλετε σίγουρα να διαγράψετε το μάθημα " + retrievedSubject.getSubjectName())
                        .setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sDatabase.subjectDao().delete(retrievedSubject);
                                Intent intent = new Intent(EditSubjectActivity.this, SubjectActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Το μάθημα διαγράφηκε!", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Οχι", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_delete)
                        .show();
            }
        });
    }

    private void setRetrievedData() {
        //Get Subject from Id
        subjectName.setText(retrievedSubject.getSubjectName());
        subjectTeacher.setText(retrievedSubject.getSubjectTeacher());
        subjectRoom.setText(retrievedSubject.getSubjectRoom());
        subjectHours.setText(retrievedSubject.getSubjectHours());
        subjectPoints.setText(retrievedSubject.getSubjectPoints());
        if(retrievedSubject.getSubjectType().equals("Υποχρεωτικό")){
            radioButtonOb.setChecked(true);
        }else{
            radioButtonCh.setChecked(true);
        }
        seekBar.setProgress(Integer.valueOf(retrievedSubject.getSubjectSemester())-1);
        seekBarText.setText(String.valueOf(seekBar.getProgress()+1));
        //On progress Change, change the num of the seekbar textview
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarText.setText(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void findViews() {
        subjectName = findViewById(R.id.fSubject_name);
        subjectTeacher = findViewById(R.id.fSubject_teacher);
        subjectPoints = findViewById(R.id.fSubject_points);
        subjectHours = findViewById(R.id.fSubject_hours);
        subjectRoom = findViewById(R.id.fSubject_room);
        radioButtonOb = findViewById(R.id.radio_obligatory);
        radioButtonCh = findViewById(R.id.radio_choose);
        seekBar = findViewById(R.id.semester_seekbar);
        greenTitle = findViewById(R.id.green_title);
        greenTitle.setText("Επεξεργασία Μαθήματος:");
        cancelButton = findViewById(R.id.cancel_button);
        seekBar = findViewById(R.id.semester_seekbar);
        seekBarText = findViewById(R.id.semester_bar);
        deleteButton = findViewById(R.id.delete_button);
        saveButton = findViewById(R.id.save_button);

    }

    private void cancelButtonAction() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setBarTitle(int id) {
        TextView textView = findViewById(R.id.edit_title);
        Subject subject = sDatabase.subjectDao().findById(id);
        textView.setText(subject.getSubjectName());
    }

    public void setSemesterSeekBar(SeekBar seekBar, final TextView textView){

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                textView.setText(String.valueOf(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
