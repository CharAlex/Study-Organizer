package com.example.alexchar.studyorganizer.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.entities.Subject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;


public class FailedMarksAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Integer> listDataHeader = new ArrayList<>();;
    private HashMap<Integer,List<String>> listHashMap = new HashMap<>();
    private List<Subject> subjects;
    private int gradeFlag = 0;
    //    ToDo make the minimumGrade as an input for the user
    private int minimumGrade = 5;

    public FailedMarksAdapter(List<Subject> subjects, Context context){
        this.subjects = subjects;
        this.context = context;
        setData();
    }

    //    Set data from database to expandable list
    private void setData() {
//      Setting up the listDataHeader
        List<Integer> temp = new ArrayList<>();
        for(Subject i: subjects){
            if(i.getSubjectGrade() < minimumGrade){
//                Control to avoid having the same semester more than once
                if(!temp.contains(Integer.valueOf(i.getSubjectSemester())))
                    listDataHeader.add(Integer.valueOf(i.getSubjectSemester()));
                temp.add(Integer.valueOf(i.getSubjectSemester()));
            }
        }
//        Sort the listDataHeader
        Collections.sort(listDataHeader);

//      Setting up the listHashMap
//        Getting the subjects with the same semester grouped
        int position = 0;
//        HashMap initialize to store the lists(groups) of subjects in same semester. The number of lists that is stored is equal to the number of unique semesters.
        HashMap<String,List<String>> groupedSemesterList = new HashMap<>();
        for(int i=1; i <= listDataHeader.size(); i++){
            groupedSemesterList.put(String.valueOf(i), new ArrayList<String>());
        }

//        For each semester(distinct) in which the user is enrolled (the array of that semesters is sorted)
        for(Integer semester: listDataHeader){
//            For each subject of all subjects that user is enrolled.
            for(Subject i: subjects){
//                Controls if the distinct semester is equal to the semester that is accessed in the nested for loop.
                if(semester.equals(Integer.valueOf(i.getSubjectSemester())) && i.getSubjectGrade() < minimumGrade){
                    Log.d(TAG, "setData: " + String.valueOf(position+1) + groupedSemesterList.get(String.valueOf(position+1)));
//                    Get the list with the key position + 1 from the HashMap
                    groupedSemesterList.get(String.valueOf(position+1)).add(i.getSubjectName() + ": " + i.getSubjectGrade());
                }
            }
//            Reference the list from the HashMap groupedSemesterList to the HashMap listHashMap
            listHashMap.put(listDataHeader.get(position),groupedSemesterList.get(String.valueOf(position+1)));
            position++;
        }
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listHashMap.get(listDataHeader.get(i)).get(i1); //i = Group Item, i1 = ChildItem
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = String.valueOf(getGroup(i));
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.marks_list_group,null);
        }
        TextView listHeader = (TextView) view.findViewById(R.id.listHeader);
        listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText("Εξάμηνο " + headerTitle);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String child = (String) getChild(i,i1);
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.failed_mark_list_item,null);
        }
        TextView listChild =  view.findViewById(R.id.subject_name);
        listChild.setText(child);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
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
