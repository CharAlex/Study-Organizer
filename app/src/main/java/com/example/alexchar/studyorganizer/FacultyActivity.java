package com.example.alexchar.studyorganizer;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.alexchar.studyorganizer.GetRawData;
import com.example.alexchar.studyorganizer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FacultyActivity extends AppCompatActivity {

    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        GetRawData getRawData = new GetRawData(this);
        getRawData.execute("https://script.googleusercontent.com/macros/echo?user_content_key=JL6McMeVhNDvQLSl77wh8H39kvSnzcTkeXemWSNVVQUHyOqxAShgu8hhJtPLYByN7ReSmhn9gQPOlDw5uq_GxisP1TUPkAgVOJmA1Yb3SEsKFZqtv3DaNYcMrmhZHmUMWojr9NvTBuBLhyHCd5hHa1ZsYSbt7G4nMhEEDL32U4DxjO7V7yvmJPXJTBuCiTGh3rUPjpYM_V0PJJG7TIaKp26Sr_hdSgG4yUi8di08MCsRDTMOqN41GI6BSrALhK3lPOXQ4O7So7a347RP5TfrEcKiW3k6MDkf31SIMZH6H4k&lib=MbpKbbfePtAVndrs259dhPT7ROjQYJ8yx");
        Log.d(TAG, "onCreate: ends");
    }
    



    public void onDownloadComplete(String data, DownloadStatus status){
        if (status == DownloadStatus.OK){
            Log.d(TAG, "onDownloadComplete: data is " + data);
            //ΔΟΚΙΜΗ
            TextView view = (TextView) findViewById(R.id.finalJson);
            GetGoogleSheetJsonData getGoogleSheet = new GetGoogleSheetJsonData(data,view);
            getGoogleSheet.setJsonData();
        }else{
            //download or processing failed
            Log.e(TAG, "onDownloadComplete: failed with stauts " + status );
        }

    }
}











