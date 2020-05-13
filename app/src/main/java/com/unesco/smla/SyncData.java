package com.unesco.smla;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smla.controller.Connecting;
import com.smla.database.DatabaseConn;
import com.smla.interfaces.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class SyncData extends AppCompatActivity implements AsyncResponse{

    LinearLayout layout;

    DatabaseConn db = new DatabaseConn(this);

    File f;

    JSONObject Root = new JSONObject();

    public static TextView t7;
    public static TextView t8;

    public static Button startSync;

    public static int AX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sync_data);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_cloud_upload_white_24dp);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layout = (LinearLayout) findViewById(R.id.syncLayout);

        startSync = (Button) findViewById(R.id.startSync);
    }

    public void startSync(View v) {

        startSync.setEnabled(false);
        startSync.setBackgroundResource(R.color.darkred);
        startSync.setText("PROCESSING.....");

        String myLoc = DatabaseConn.CLOC + "/SYNC/" + DashBoard.STUDENTID + ".txt";
        f = new File(myLoc);
        if (f.exists()) {
            f.delete();
            System.out.println("I DELETED");
        } else {
            System.out.println(" FILE NOT THERE ");
        }

        TextView t1 = new TextView(this);
        TextView t2 = new TextView(this);

        t1.setText("Starting Data Synchronization.........." + "\n");

        t1.setGravity(Gravity.CENTER);
        t1.setTextSize(20);

        t2.setText("___________________________________________________________" + "\n");
        t2.setGravity(Gravity.CENTER);

        layout.addView(t1);
        layout.addView(t2);

        //get Course View Data..

        String CV = getCourseViewData(myLoc);

        TextView t3 = new TextView(this);

        String[] CVx = CV.split("#");

        t3.setText(CVx[0]);

        System.out.println("CV --- " + CV);

        if (CVx[1].equals("SUCCESS")) {
            t3.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.correct_tick, 0, 0, 0);
        } else {
            t3.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.wrong_tick, 0, 0, 0);
        }
        t3.setPadding(5, 5, 5, 5);
        layout.addView(t3);

        TextView t4 = new TextView(this);

        String QZ = getQuizData(myLoc);
        String[] QZx = QZ.split("#");

        System.out.println("QZ --- " + QZ);

        t4.setText(QZx[0]);

        if (QZx[1].equals("SUCCESS")) {
            t4.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.correct_tick, 0, 0, 0);
        } else {
            t4.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.wrong_tick, 0, 0, 0);
        }
        t4.setPadding(5, 5, 5, 5);
        layout.addView(t4);

        //Log data..

        TextView t5 = new TextView(this);

        String LG = getLogData(myLoc);
        String[] LGx = LG.split("#");

        System.out.println("LG --- " + LG);

        t5.setText(LGx[0]);

        if (LGx[1].equals("SUCCESS")) {
            t5.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.correct_tick, 0, 0, 0);
        } else {
            t5.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.wrong_tick, 0, 0, 0);
        }
        t5.setPadding(5, 5, 5, 5);
        layout.addView(t5);

        //Profile Data..

        TextView t6 = new TextView(this);

        String PR = getProfileData(myLoc);
        String[] PRx = PR.split("#");

        System.out.println("PR --- " + PR);

        t6.setText(PRx[0]);

        if (PRx[1].equals("SUCCESS")) {
            t6.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.correct_tick, 0, 0, 0);
        } else {
            t6.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.wrong_tick, 0, 0, 0);
        }
        t6.setPadding(5, 5, 5, 5);
        layout.addView(t6);

        //CREATING CFILE...

        cFile(f.getAbsolutePath());

        String[] SADD = DatabaseConn.SERVER.split("#");

        String ipAddress = SADD[0];
        int portNo = Integer.parseInt(SADD[1]);

        TextView t71 = new TextView(this);

        layout.addView(t71);
        t71.setPadding(5, 5, 5, 5);

        t7 = new TextView(this);

        Connecting conn = new Connecting(ipAddress, portNo, myLoc);

        conn.delegate=this;
        conn.execute();

        t7.setPadding(5,5,5,5);
        layout.addView(t7);

        System.out.println("T7 TEXT IS --- "+t7.getText().toString());

        //Finish process..

        t8 = new TextView(this);
        t8.setPadding(5,5,5,5);
        layout.addView(t8);

    }

    public String getCourseViewData(String file) {

        JSONArray CourseViewArray = new JSONArray();
        //JSONObject Root = new JSONObject();

        String str = "";

        int sn = 0;

        Cursor cvCursor = db.courseView();

        int cvCount = cvCursor.getCount();

        if (cvCount <= 0) {

            str = "NO UNSYNCHRONIZED COURSE VIEW DATA FOUND#SUCCESS";

        } else {

            try {

                //File f = new File(file);
                //FileOutputStream fos = new FileOutputStream(f, true);
                //PrintStream pStream = new PrintStream(fos);


                while (cvCursor.moveToNext()) {

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

                    str = sn + " results of unsynchronized CourseView data scanned and saved for synchronization#SUCCESS";
                }

                Root.put("CourseView", CourseViewArray);
                //pStream.append(Root.toString() + "\n");
                //pStream.close();

            } catch (Exception nn) {
                System.out.println("EXCEPTION OCCURED...CourseView..." + nn);
                str = "ERROR COPYING COURSEVIEW DATA#ERROR";
            }
        }

        return str;
    }

    //Get Quiz Data..

    public String getQuizData(String file) {

        JSONArray CourseViewArray = new JSONArray();
        //JSONObject Root = new JSONObject();

        String str = "";

        int sn = 0;

        Cursor cvCursor = db.quizData();

        int cvCount = cvCursor.getCount();

        if (cvCount <= 0) {

            str = "NO UNSYNCHRONIZED QUIZ DATA FOUND#SUCCESS";

        } else {

            try {

                //File f = new File(file);
                //FileOutputStream fos = new FileOutputStream(f, true);
                //PrintStream pStream = new PrintStream(fos);

                while (cvCursor.moveToNext()) {

                    JSONObject obj = new JSONObject();

                    sn++;
                    String ID = cvCursor.getString(cvCursor.getColumnIndex("STUDENTID"));
                    String COURSE = cvCursor.getString(cvCursor.getColumnIndex("COURSE"));
                    String TERM = cvCursor.getString(cvCursor.getColumnIndex("TERM"));
                    String LESSON = cvCursor.getString(cvCursor.getColumnIndex("LESSON"));
                    Float SCORE = cvCursor.getFloat(cvCursor.getColumnIndex("SCORE"));
                    String DATE = cvCursor.getString(cvCursor.getColumnIndex("DATE"));

                    obj.put("STUDENTID", ID);
                    obj.put("COURSE", COURSE);
                    obj.put("TERM", TERM);
                    obj.put("LESSON", LESSON);
                    obj.put("SCORE", SCORE);
                    obj.put("DATE", DATE);

                    CourseViewArray.put(obj);

                    str = sn + " results of unsynchronized Quiz Data data scanned and saved for synchronization#SUCCESS";
                }

                Root.put("QuizResult", CourseViewArray);
                //pStream.append(Root.toString() + "\n");
                //pStream.close();

            } catch (Exception nn) {
                System.out.println("EXCEPTION OCCURED...QUIZ..." + nn);
                str = "ERROR COPYING QUIZ DATA#ERROR";
            }
        }
        return str;
    }

    public String getLogData(String file) {

        JSONArray CourseViewArray = new JSONArray();
        //JSONObject Root = new JSONObject();

        String str = "";

        int sn = 0;

        Cursor cvCursor = db.logData();

        int cvCount = cvCursor.getCount();

        if (cvCount <= 0) {

            str = "NO UNSYNCHRONIZED LOG DATA FOUND#SUCCESS";

        } else {

            try {

                //File f = new File(file);
                //FileOutputStream fos = new FileOutputStream(f, true);
                //PrintStream pStream = new PrintStream(fos);

                while (cvCursor.moveToNext()) {

                    JSONObject obj = new JSONObject();

                    sn++;
                    String ID = cvCursor.getString(cvCursor.getColumnIndex("ID"));
                    String DATE = cvCursor.getString(cvCursor.getColumnIndex("DATE"));
                    String ACTIVITY = cvCursor.getString(cvCursor.getColumnIndex("ACTIVITY"));
                    String NAME = cvCursor.getString(cvCursor.getColumnIndex("NAME"));

                    obj.put("STUDENTID", ID);
                    obj.put("DATE", DATE);
                    obj.put("ACTIVITY", ACTIVITY);
                    obj.put("NAME", NAME);

                    CourseViewArray.put(obj);

                    str = sn + " results of unsynchronized Log Data data scanned and saved for synchronization#SUCCESS";
                }

                Root.put("LogResult", CourseViewArray);
                //pStream.append(Root.toString() + "\n");
                //pStream.close();

            } catch (Exception nn) {
                System.out.println("EXCEPTION OCCURED...CourseView..." + nn);
                str = "ERROR COPYING LOG DATA#ERROR";
            }
        }
        return str;
    }

    public String getProfileData(String file) {

        JSONArray CourseViewArray = new JSONArray();
        //JSONObject Root = new JSONObject();

        String str = "";

        int sn = 0;

        Cursor cvCursor = db.profileData();

        int cvCount = cvCursor.getCount();

        if (cvCount <= 0) {

            str = "NO UNSYNCHRONIZED PROFILE DATA FOUND#SUCCESS";

        } else {

            try {

                //File f = new File(file);
                //FileOutputStream fos = new FileOutputStream(f, true);
                //PrintStream pStream = new PrintStream(fos);

                while (cvCursor.moveToNext()) {

                    JSONObject obj = new JSONObject();

                    sn++;

                    String username = cvCursor.getString(cvCursor.getColumnIndex("USERNAME"));
                    String password = cvCursor.getString(cvCursor.getColumnIndex("PASSWORD"));
                    String firstname = cvCursor.getString(cvCursor.getColumnIndex("FIRSTNAME"));
                    String lastname = cvCursor.getString(cvCursor.getColumnIndex("LASTNAME"));
                    String dob = cvCursor.getString(cvCursor.getColumnIndex("DOB"));
                    String dor = cvCursor.getString(cvCursor.getColumnIndex("DOR"));
                    String sex = cvCursor.getString(cvCursor.getColumnIndex("SEX"));
                    String account_type = cvCursor.getString(cvCursor.getColumnIndex("ACCOUNT_TYPE"));
                    byte[] bvv = cvCursor.getBlob(cvCursor.getColumnIndex("U_IMAGE"));
                    String stype = cvCursor.getString(cvCursor.getColumnIndex("STYPE"));
                    String studentID = cvCursor.getString(cvCursor.getColumnIndex("STUDENTID"));

                    String base64 = Base64.encodeToString(bvv, Base64.DEFAULT);


                    obj.put("USERNAME", username);
                    obj.put("PASSWORD", password);
                    obj.put("FIRSTNAME", firstname);
                    obj.put("LASTNAME", lastname);
                    obj.put("DOB", dob);
                    obj.put("DOR", dor);
                    obj.put("SEX", sex);
                    obj.put("ACCOUNT_TYPE", account_type);
                    obj.put("IMAGE", base64);
                    obj.put("STYPE", stype);
                    obj.put("STUDENTID", studentID);

                    CourseViewArray.put(obj);

                    str = sn + " results of unsynchronized Profile Data data scanned and saved for synchronization#SUCCESS";
                }

                Root.put("Profiles", CourseViewArray);
                //pStream.append(Root.toString() + "\n");
                //pStream.close();

                //cFile(file);

            } catch (Exception nn) {
                System.out.println("EXCEPTION OCCURED...Profile..." + nn);
                str = "ERROR COPYING PROFILE DATA#ERROR";
            }
        }

        return str;
    }

    //CREATE FILE..

    public void cFile(String file){

        try{
            File f = new File(file);
            FileOutputStream fos = new FileOutputStream(f,true);
            PrintStream pStream = new PrintStream(fos);

            pStream.append(Root.toString());

            pStream.close();

        }
        catch(Exception nn){
            System.out.println("ERROR CREATING FILE--"+nn);
        }

    }

    public  void finishUp(){

        String complete = db.completeSync(db.getWritableDatabase());
        String[] cmp = complete.split("#");

        if(cmp[1].equals("SUCCESS")){
            t8.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.correct_tick, 0, 0, 0);
        }else{
            t8.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.wrong_tick, 0, 0, 0);
        }
        t8.setText(cmp[0]);
    }

    @Override
    public void processFinish(String output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.

        if(output.equals("OK")){
            finishUp();
        }
        else{

        }
    }

}
