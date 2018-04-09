package com.example.alexchar.studyorganizer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexchar.studyorganizer.adapters.SubjectAdapter;

import java.util.List;

public class SubjectActivity extends AppCompatActivity implements SubjectInputFragment.OnFragmentInteractionListener{

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    FloatingActionButton fab;
    SubjectDatabase sDatabase;
    LinearLayout noSubjectsIcon;
    CardView subjectListCardview;
    private static final String TAG = "SubjectActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sDatabase = SubjectDatabase.getSubjectDatabase(getApplicationContext());
        //sDatabase.subjectDao().deleteAll();

        //Get all the subject from database
        List<Subject> list = sDatabase.subjectDao().getAll();


        //Set recycler view and parse the list with all the subject extracted from the database
        setRecyclerView(list);

        //Update subject Num
        updateSubjectNum();

        //remove all the subjects from database
        removeAllSubjects();

        //Show and hide no subjects icon
        setNoSubjectIcon();

        //Add swipe-delete feature
        swipeToDelete();

        fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment();
                fab.setVisibility(View.GONE);
            }
        });
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
                final TagViewHolder tg = (TagViewHolder) viewHolder.itemView.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(SubjectActivity.this);
                builder.setMessage("Θέλετε σίγουρα να διαγράψετε το μάθημα;");
                builder.setPositiveButton("Διαγραφή", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        adapter.notifyItemRemoved(tg.getPosition());
                        //Remove item from database too
                        int subjectId = tg.getSubjects().get(tg.getPosition()).getSid();
                        Subject subject = sDatabase.subjectDao().findById(subjectId);
                        sDatabase.subjectDao().delete(subject);
                        //Delete subject from list
                        tg.getSubjects().remove(tg.getPosition());
                        adapter.notifyItemRangeChanged(tg.getPosition(),tg.getSubjects().size());
                        //Update num of subjects and show icon if subjects.size == 0
                        if(tg.getSubjects().size() == 0){
                            noSubjectsIcon.setVisibility(View.VISIBLE);
                        }
                        updateSubjectNum();
                    }
                }).setNegativeButton("ΟΧΙ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.notifyItemRemoved(tg.getPosition()+1);
                        adapter.notifyItemRangeChanged(tg.getPosition(),adapter.getItemCount());
                        return;
                    }
                }).show();

            }
        }).attachToRecyclerView(recyclerView);
    }


    private void setNoSubjectIcon() {
        noSubjectsIcon = findViewById(R.id.no_subjects_icon);
        if(adapter.getItemCount() > 0){
            noSubjectsIcon.setVisibility(View.GONE);
        }else{
            noSubjectsIcon.setVisibility(View.VISIBLE);
            /*Set cardview height programmatically
            create params variable to parse match parent width and height
            also set margins
             */
            subjectListCardview = findViewById(R.id.subjectListCardview);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

            //Margin is 12dp but setMargins requires int so we call getDisplayMetrics to set the margin properly
            int dpValue = 12; // margin in dips
            float d = this.getResources().getDisplayMetrics().density;
            int margin = (int)(dpValue * d); // margin in pixels
            params.setMargins(margin,margin,margin,margin);

            subjectListCardview.setLayoutParams(params);

        }
    }


    private void updateSubjectNum() {
        TextView sNum = findViewById(R.id.subject_num);
        sNum.setText(String.valueOf(sDatabase.subjectDao().countSubjects()));
        if(sDatabase.subjectDao().countSubjects()> 0){
            sNum.setTextColor(Color.parseColor("#58be8a"));
        }else{
            sNum.setTextColor(Color.parseColor("#ffc60b"));
        }

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
    }

    public void openFragment(){
        SubjectInputFragment fragment = SubjectInputFragment.newInstance(null, null);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction().add(R.id.subject_container, fragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setRecyclerView(List<Subject> subjects){
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Custom subject adapter
        adapter = new SubjectAdapter(subjects);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    private void removeAllSubjects() {
        ImageView view = findViewById(R.id.delete_all_subjects);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sDatabase.subjectDao().countSubjects() > 0 ){
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(SubjectActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(SubjectActivity.this);
                    }
                    builder.setTitle("Είστε σίγουρος;")
                            .setMessage("Η διαγραφή όλων των μαθημάτων δεν μπορεί να αναιρεθεί!")
                            .setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    sDatabase.subjectDao().deleteAll();
                                    Intent intent = new Intent(SubjectActivity.this, SubjectActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Διαγράφηκαν όλα τα μαθήματα!", Toast.LENGTH_LONG).show();
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
                        builder = new AlertDialog.Builder(SubjectActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(SubjectActivity.this);
                    }
                    builder.setTitle("Δεν υπάρχει αποθηκευμένο μάθημα")
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

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}


