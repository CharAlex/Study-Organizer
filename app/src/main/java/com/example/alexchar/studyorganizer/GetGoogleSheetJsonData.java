package com.example.alexchar.studyorganizer;

import android.widget.TextView;

import com.example.alexchar.studyorganizer.entities.Faculty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class GetGoogleSheetJsonData{
    private static final String TAG = "GetGoogleSheetJsonData";
    private String rawJsonData;
    TextView textView;

    public GetGoogleSheetJsonData(String rawJsonData, TextView textView) {
        this.rawJsonData = rawJsonData;
        this.textView = textView;
    }

    public void setJsonData(){
        Faculty mFaculty = new Faculty();
        try {
            JSONObject object = new JSONObject(rawJsonData);
            JSONArray jsonArray = object.getJSONArray("Sheet1");
            //ΚΑΠΟΙΟ FOR ΜΑΛΛΟΝ
            JSONObject faculty = jsonArray.getJSONObject(0);
            mFaculty.setfName(faculty.getString("ΣΧΟΛΗ"));
            mFaculty.setLink(faculty.getString("LINK"));
            mFaculty.setSemester(faculty.getString("ΕΞΑΜΗΝΟ"));
            mFaculty.setYear(faculty.getString("ΕΤΟΣ"));
            mFaculty.setScheduleLink(faculty.getString("ΠΡΟΓΡΑΜΜΑ_LINK"));
            textView.setText(mFaculty.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

}