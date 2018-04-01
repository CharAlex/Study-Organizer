package com.example.alexchar.studyorganizer;

import java.util.List;
//A class to parse subject list, position, id from activity to fragment
public class TagViewHolder {
    private List<Subject> subjects;
    private int position;
    private int id;

    public TagViewHolder(List<Subject> subjects, int position, int id) {
        this.subjects = subjects;
        this.position = position;
        this.id = id;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public int getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }
}
