package com.example.alexchar.studyorganizer;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus { IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK}

public class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";

    private DownloadStatus mDownloadStatus;
    private final FacultyActivity mCallBack;


    public GetRawData(FacultyActivity callback) {
        mDownloadStatus = DownloadStatus.IDLE;
        mCallBack = callback;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: parameter = " + s);
        if (mCallBack != null){
            mCallBack.onDownloadComplete(s, mDownloadStatus);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if(strings == null){
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }
        try{
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was " + response);

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            //Διαβάζω γραμμές αποθηκευμένες στον Buffer και στο τέλος τις κάνω return
            String line;
            while (null != (line = reader.readLine())){
                result.append(line).append("\n");
            }

            mDownloadStatus = DownloadStatus.OK;
            return result.toString();

        }catch (MalformedURLException e){
            Log.e(TAG, "doInBackground: " + e.getMessage());
        }catch (IOException e){
            Log.e(TAG, "doInBackground: IO Exception reading data: " + e.getMessage() );
        }catch(SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception. Needs permission? " + e.getMessage() );
        }finally {
            if (connection != null)
                connection.disconnect();
            if (reader != null){
                try{
                    reader.close();
                }catch (IOException e){
                    Log.e(TAG, "doInBackground: Error closing stream " + e.getMessage() );
                }
            }
        }

        //Αν έχω φτάσει εδώ και δεν έχει γίνει προηγουμένως κάποιον return σημαίνει ότι έχω πρόβλημα
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}



















