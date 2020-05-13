package com.smla.controller;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;

import com.smla.database.DatabaseConn;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by SAMSON KAYODE on 20-Dec-2017.
 */

public class SyncObjects extends AppCompatActivity {

    DatabaseConn db = new DatabaseConn(this);

    Cursor cvCursor;
    int cvCount;

    public String getCourseViewData(String file){

        JSONArray CourseViewArray = new JSONArray();
        JSONObject Root = new JSONObject();

        String str="";

        int sn=0;

        try{
            cvCursor = db.courseView();

            cvCount = cvCursor.getCount();
        }
        catch(Exception nn){
            System.out.println("EXCEPTION OCCURED HERE ---"+nn);
        }

        if(cvCount<=0){

            str = "NO UNSYNCHRONIZED DATA FOUND";

        }else{

            try {

                File f = new File(file);
                FileOutputStream fos = new FileOutputStream(f,true);
                PrintStream pStream = new PrintStream(fos);


                while(cvCursor.moveToNext()) {

                    JSONObject obj = new JSONObject();

                    sn++;
                    String ID = cvCursor.getString(cvCursor.getColumnIndex("STUDENTID"));
                    String COURSE = cvCursor.getString(cvCursor.getColumnIndex("COURSE"));
                    String TERM = cvCursor.getString(cvCursor.getColumnIndex("TERM"));
                    String LESSON = cvCursor.getString(cvCursor.getColumnIndex("LESSON"));
                    String DATE = cvCursor.getString(cvCursor.getColumnIndex("DATE"));
                    String EDATE = cvCursor.getString(cvCursor.getColumnIndex("EDATE"));

                    obj.put("STUDENTID", ID);
                    obj.put("COURSE", COURSE);
                    obj.put("TERM", TERM);
                    obj.put("LESSON", LESSON);
                    obj.put("DATE", DATE);
                    obj.put("EDATE", EDATE);

                    CourseViewArray.put(obj);

                    str = sn+" results of unsynchronized data scanned and saved....";
                }

                Root.put("CourseView", CourseViewArray);
                pStream.append(Root.toString()+"\n");
                pStream.close();

            }
            catch (Exception nn){
                System.out.println("EXCEPTION OCCURED...CourseView..."+nn);
                str = "ERROR COPYING COURSEVIEW DATA";
            }
        }

        return str;
    }

}
