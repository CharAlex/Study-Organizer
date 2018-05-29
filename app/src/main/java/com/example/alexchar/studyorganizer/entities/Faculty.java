package com.example.alexchar.studyorganizer.entities;

public class Faculty {

    private String fName;
    private String link;
    private String semester;
    private String year;
    private String scheduleLink;

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setScheduleLink(String scheduleLink) {
        this.scheduleLink = scheduleLink;
    }

    public String getfName() {
        return fName;
    }

    public String getLink() {
        return link;
    }

    public String getSemester() {
        return semester;
    }

    public String getYear() {
        return year;
    }

    public String getScheduleLink() {
        return scheduleLink;
    }

    @Override
    public String toString() {
        return "Σχολή: " + fName + "\n" +
                "Σύνδεσμος: " + link + "\n" +
                "Εξάμηνο: " + semester + "\n" +
                "Έτος: " + year + "\n" +
                "Ωρολόγιο Πρόγραμμα: " + scheduleLink;
    }
}
