package com.example.alexchar.studyorganizer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.Subject;

import java.util.List;


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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MarksAdapter.ViewHolder holder, int position) {
        holder.subjectMark.setText(subjects.get(position).getSubjectName());
        holder.mark.setText(Integer.toString(subjects.get(position).getSubjectGrade()));

    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectMark;
        TextView mark;

        public ViewHolder(final View itemView) {
            super(itemView);
            subjectMark = itemView.findViewById(R.id.subject_mark_name);
            mark = itemView.findViewById(R.id.subject_mark);
        }
    }
}
