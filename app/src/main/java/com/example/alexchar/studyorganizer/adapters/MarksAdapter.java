package com.example.alexchar.studyorganizer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.Subject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static android.content.ContentValues.TAG;


public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.ViewHolder> {

    private List<Subject> subjects;

    public MarksAdapter(List<Subject> subjects){
        this.subjects = subjects;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MarksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marks_row,parent,false);
        getMaxSemester();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MarksAdapter.ViewHolder holder, int position) {
        holder.subjectMark.setText(String.valueOf(getMaxSemester()));
//        holder.subjectMark.setText(subjects.get(position).getSubjectName());
//        holder.mark.setText(Integer.toString(subjects.get(position).getSubjectGrade()));

    }

    @Override
    public int getItemCount() {
        return getMaxSemester();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectMark;
        TextView mark;

        public ViewHolder(final View itemView) {
            super(itemView);
            subjectMark = itemView.findViewById(R.id.subject_mark_name);
//            mark = itemView.findViewById(R.id.subject_mark);
        }
    }

    //Getting the max semester that is submitted by the user.
    public int getMaxSemester(){
        int subjectMaxSemester = 0;

        for(Subject i : subjects ){
            if(Integer.parseInt(i.getSubjectSemester()) > subjectMaxSemester){
                subjectMaxSemester = Integer.parseInt(i.getSubjectSemester());
            }
        }
        Log.d(TAG, "getMaxSemester: " + subjectMaxSemester);
        return subjectMaxSemester;
    }
}
