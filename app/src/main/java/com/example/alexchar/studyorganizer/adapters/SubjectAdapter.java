package com.example.alexchar.studyorganizer.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alexchar.studyorganizer.EditSubject;
import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.Subject;
import com.example.alexchar.studyorganizer.TagViewHolder;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder>  {

    private List<Subject> subjects;
    private Context context;
    private static final String TAG = "SubjectAdapter";
    //SubjectDatabase sDatabase;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public SubjectAdapter(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_row,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.subjectName.setText(subjects.get(position).getSubjectName());
        holder.subjectType.setText("Τύπος : " +subjects.get(position).getSubjectType());
        holder.subjectSemester.setText("Εξάμηνο: " +subjects.get(position).getSubjectSemester());
        holder.subjectTeacher.setText("Καθηγητής: " +subjects.get(position).getSubjectTeacher());
        holder.subjectPoints.setText("Διδ. Μονάδες: " +String.valueOf(subjects.get(position).getSubjectPoints()));
        holder.subjectHours.setText("Ώρες διδ.: " +String.valueOf(subjects.get(position).getSubjectHours()));
        holder.subjectRoom.setText("Αμφιθέατρο/αίθουσα: " +subjects.get(position).getSubjectRoom());

        //Create object to pass the info to SubjectActivity class
        TagViewHolder tg = new TagViewHolder(subjects,position,subjects.get(position).getSid());
        holder.itemView.setTag(tg);


        //Change background color for every 2nd item
        if(position%2 == 0){
            holder.rootView.setBackgroundResource(R.color.backgroundAccent);
        }else{
            holder.green_bar.setBackgroundResource(R.color.colorPrimary);
        }

        //Make click listener to each row item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start new EditSubject activity
                parseSubjectId(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        TextView subjectType;
        TextView subjectSemester;
        TextView subjectTeacher;
        TextView subjectPoints;
        TextView subjectHours;
        TextView subjectRoom;
        LinearLayout rootView;
        View green_bar;
        public ViewHolder(final View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subject_name);
            subjectType =  itemView.findViewById(R.id.subject_type);
            subjectSemester =  itemView.findViewById(R.id.subject_semester);
            subjectTeacher =  itemView.findViewById(R.id.subject_teacher);
            subjectPoints =  itemView.findViewById(R.id.subject_points);
            subjectHours =  itemView.findViewById(R.id.subject_hours);
            subjectRoom =  itemView.findViewById(R.id.subject_room);
            rootView = itemView.findViewById(R.id.rootView);
            green_bar = itemView.findViewById(R.id.left_bar);
        }
    }
    public void parseSubjectId(int position){
        //parse this id to next activity
        int subjectId = subjects.get(position).getSid();
        Intent intent = new Intent(context,EditSubject.class);
        intent.putExtra("subject id", subjectId);
        //Start the new EditSubject Activity
        context.startActivity(intent);
    }


}
