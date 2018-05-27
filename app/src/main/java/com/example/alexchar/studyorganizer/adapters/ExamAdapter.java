package com.example.alexchar.studyorganizer.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.activities.ExamActivity;
import com.example.alexchar.studyorganizer.database.SubjectDatabase;
import com.example.alexchar.studyorganizer.entities.Exam;
import com.example.alexchar.studyorganizer.fragments.ExamSetFragment;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {
    private Context context;
    private List<Exam> exams;
    private SubjectDatabase sDatabase;

    public ExamAdapter(List<Exam> exams) {
        this.exams = exams;
        this.exams = sortExamsByDate(exams);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_row, parent, false);
        context = parent.getContext();
        sDatabase = SubjectDatabase.getSubjectDatabase(context);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ExamAdapter.ViewHolder holder, int position) {
        int id = exams.get(position).getSubjectId();
        holder.subjectName.setText(sDatabase.subjectDao().findById(id).getSubjectName());
        holder.examRoom.setText("Αμφιθέατρο:" + exams.get(position).getExamRoom());

//        Switch the color of the importance indicator based on the difficulty of the exam
        switch (exams.get(position).getExamDifficulty()){
            case 0: holder.importance.setBackgroundColor(context.getResources().getColor(R.color.colorSuccess));
                break;
            case 1: holder.importance.setBackgroundColor(context.getResources().getColor(R.color.randomColor3));
                break;
            case 2: holder.importance.setBackgroundColor(context.getResources().getColor(R.color.colorFail));
                break;
            default: holder.importance.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
                break;
        }

        holder.examDay.setText(String.valueOf(exams.get(position).getExamDay()));
        holder.examMonth.setText(getMonthShortForm(position));
        String hour = (exams.get(position).getExamHour() < 10 ? "0" : "") + exams.get(position).getExamHour();
        String minute = (exams.get(position).getExamMinute() < 10 ? "0" : "") + exams.get(position).getExamMinute();
        holder.examTime.setText(hour + ":" + minute);

    }



    private String getMonthShortForm(int pos) {
        switch (exams.get(pos).getExamMonth()){
            case 0: return "JAN";
            case 1: return "FEB";
            case 2: return "MAR";
            case 3: return "APR";
            case 4: return "MAY";
            case 5: return "JUN";
            case 6: return "JUL";
            case 7: return "AUG";
            case 8: return "SEP";
            case 9: return "OCT";
            case 10: return "NOV";
            case 11: return "DEC";
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView subjectName, examRoom, examDay, examMonth,examTime;
        private View importance;

        public ViewHolder(final View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subject_name);
            examRoom = itemView.findViewById(R.id.subject_room);
            importance = itemView.findViewById(R.id.importance_status);
            examDay = itemView.findViewById(R.id.exam_day);
            examMonth = itemView.findViewById(R.id.exam_month);
            examTime = itemView.findViewById(R.id.exam_time);
        }
    }
    private List<Exam> sortExamsByDate(List<Exam> exams) {

        Collections.sort(exams, new Comparator<Exam>() {
            @Override
            public int compare(Exam exam1, Exam exam2) {
                if(exam1.getExamYear() != exam2.getExamYear()){
                    return exam1.getExamYear() - exam2.getExamYear();
                }else if(exam1.getExamMonth() != exam2.getExamMonth()){
                    return exam1.getExamMonth() - exam2.getExamMonth();
                }else if(exam1.getExamDay() != exam2.getExamDay()){
                    return exam1.getExamDay() - exam2.getExamDay();
                }else if(exam1.getExamHour() != exam2.getExamHour()){
                    return exam1.getExamHour() - exam2.getExamHour();
                }else if(exam1.getExamMinute() != exam2.getExamMinute()){
                    return exam1.getExamMinute() - exam2.getExamMinute();
                }
                else{
                    return 0;
                }

            }
        });
        return exams;
    }

    public List<Exam> getExams() {
        return exams;
    }
}
