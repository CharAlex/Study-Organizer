package com.example.alexchar.studyorganizer.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.adapters.ExamAdapter;
import com.example.alexchar.studyorganizer.database.ExamDatabase;
import com.example.alexchar.studyorganizer.entities.Exam;
import com.example.alexchar.studyorganizer.fragments.ExamSetFragment;

import java.util.List;

public class ExamActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ExamAdapter examAdapter;
    private FloatingActionButton floatingActionButton;
    private List<Exam> exams;
    private ExamDatabase eDatabase;
    private ImageView deleteAll;
    private LinearLayout noExamIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        setExamList();
        setRecyclerView();
        setButtons();
        swipeToDelete();
    }

    private void setExamList() {
        eDatabase = ExamDatabase.getExamDatabase(getApplicationContext());
        exams = eDatabase.examtDao().getAll();
    }

    private void setButtons() {
        floatingButtonAction();
        deleteAll();
        setNoExamIcon();
    }

    private void deleteAll() {
        deleteAll = findViewById(R.id.delete_all);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eDatabase.examtDao().countExams() > 0 ){
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(ExamActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(ExamActivity.this);
                    }
                    builder.setTitle("Είστε σίγουρος;")
                            .setMessage("Η διαγραφή της εξεταστικής δεν μπορεί να αναιρεθεί!")
                            .setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    eDatabase.examtDao().deleteAll();
                                    Intent intent = new Intent(ExamActivity.this, ExamActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Διαγράφηκαν όλες οι εξετάσεις!", Toast.LENGTH_LONG).show();
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
                }else{
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(ExamActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(ExamActivity.this);
                    }
                    builder.setTitle("Δεν υπάρχει αποθηκευμένη εξέταση")
                            .setNeutralButton("Οκ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.stat_notify_error)
                            .show();
                }

            }

        });
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.exam_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        examAdapter = new ExamAdapter(exams);
        recyclerView.setAdapter(examAdapter);
    }

    private void floatingButtonAction() {
        floatingActionButton = findViewById(R.id.add_task_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment();
            }
        });
    }

    private void openFragment(){
        ExamSetFragment fragment = new ExamSetFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction().add(R.id.fragment_container, fragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
        floatingActionButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        floatingActionButton = findViewById(R.id.add_task_button);
        floatingActionButton.setVisibility(View.VISIBLE);
    }

    private void swipeToDelete() {
        //Drag and Drop items feature
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
                builder.setMessage("Θέλετε σίγουρα να διαγράψετε το task;");
                builder.setPositiveButton("Διαγραφή", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        List<Exam> exams = examAdapter.getExams();
                        int examId = exams.get(position).getEid();
                        examAdapter.notifyItemRemoved(position);
                        //Remove item from database too
                        Exam exam = eDatabase.examtDao().findById(examId);
                        eDatabase.examtDao().delete(exam);
                        //Delete subject from list
                        exams.remove(position);
                        examAdapter.notifyItemRangeChanged(position,exams.size());
                        setNoExamIcon();
                    }
                }).setNegativeButton("ΟΧΙ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        examAdapter.notifyItemRemoved(position+1);
                        examAdapter.notifyItemRangeChanged(position,examAdapter.getItemCount());
                        return;
                    }
                }).show();

            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setNoExamIcon() {
        noExamIcon = findViewById(R.id.no_exam_icon);
        if(examAdapter.getItemCount() > 0){
            noExamIcon.setVisibility(View.GONE);
        }else{
            noExamIcon.setVisibility(View.VISIBLE);
        }
    }
}
