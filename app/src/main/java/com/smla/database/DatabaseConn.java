package com.smla.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.smla.sessions.Utilities;
import com.unesco.smla.FindLog;

import java.io.File;

/**
 * Created by SAMSON KAYODE on 08-Sep-2017.
 */

public class DatabaseConn extends SQLiteOpenHelper {

    public static String DATABASE_NAME="SMLA.db";
    private static int DATABASE_VERSION = 1;

    private static final String LOGCAT = null;

    //User Data..

    public static String FIRSTNAME, LASTNAME, FULLNAME, USERNAME, PASSWORD, STUDENTID, DOB, SEX, STUDENTTYPE, ACCOUNTTYPE;
    public static byte[] STUDENTIMAGE;

    public static String CLOC, CLANG, SERVER, SDSCORE;
    public static Float DSCORE;

    Utilities utx = new Utilities();

    //End of User Data..


    public DatabaseConn(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Q1 = "CREATE TABLE IF NOT EXISTS COURSEVIEW( STUDENTID TEXT, COURSE TEXT, TERM TEXT, "
                + "LESSON TEXT, DATE DATETIME DEFAULT CURRENT_TIMESTAMP, EDATE DATETIME DEFAULT CURRENT_TIMESTAMP, CID INTEGER PRIMARY KEY AUTOINCREMENT, SYNC TEXT DEFAULT 'NO')";

        String Q2 = " CREATE TABLE IF NOT EXISTS LOGS(ID TEXT, DATE DATETIME DEFAULT CURRENT_TIMESTAMP, ACTIVITY TEXT, NAME TEXT, SYNC TEXT DEFAULT 'NO')";

        String Q3 = " CREATE TABLE IF NOT EXISTS QUIZ (STUDENTID TEXT, COURSE TEXT, TERM TEXT, LESSON TEXT, "
                + "SCORE FLOAT, DATE DATETIME DEFAULT CURRENT_TIMESTAMP, QID INTEGER PRIMARY KEY AUTOINCREMENT, SYNC TEXT DEFAULT 'NO')";

        String Q4 = " CREATE TABLE IF NOT EXISTS SETTINGS (C_LOC TEXT, LANGUAGE TEXT, SCORE FLOAT, SERVER TEXT);";

        String Q5 = " CREATE TABLE IF NOT EXISTS SYNC (DATE DATETIME DEFAULT CURRENT_TIMESTAMP);";

        String Q6 = " CREATE TABLE IF NOT EXISTS PROFILES(USERNAME TEXT, PASSWORD TEXT, FIRSTNAME TEXT, LASTNAME TEXT, "
                + "DOB DATETIME, DOR DATETIME DEFAULT CURRENT_TIMESTAMP, SEX TEXT, ACCOUNT_TYPE TEXT, SID INTEGER PRIMARY KEY AUTOINCREMENT, U_IMAGE BLOB, STYPE TEXT, STUDENTID TEXT, SYNC TEXT DEFAULT 'NO')";

        db.execSQL(Q1);
        db.execSQL(Q2);
        db.execSQL(Q3);
        db.execSQL(Q4);
        db.execSQL(Q5);
        db.execSQL(Q6);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS COURSEVIEW");
        db.execSQL("DROP TABLE IF EXISTS LOGS");
        db.execSQL("DROP TABLE IF EXISTS QUIZ");
        db.execSQL("DROP TABLE IF EXISTS SETTINGS");
        db.execSQL("DROP TABLE IF EXISTS SYNC");
        db.execSQL("DROP TABLE IF EXISTS PROFILES");

        onCreate(db);

    }

    public void populateTable() {

        ContentValues values = new ContentValues();
        values.put("C_LOC", "/storage/emulated/0/COMPLETE SMLA CONTENT");
        values.put("LANGUAGE", "EN");
        values.put("SCORE", 80);
        values.put("SERVER", "192.168.0.1#10898");

        ContentValues p_values = new ContentValues();
        p_values.put("USERNAME", "UNESCO");
        p_values.put("PASSWORD", "UNE$C0");
        p_values.put("FIRSTNAME", "UNESCO");
        p_values.put("LASTNAME", "ADMIN");
        p_values.put("DOB", "01-01-1960");
        p_values.put("SEX", "F");
        p_values.put("ACCOUNT_TYPE", "ADMINISTRATOR");
        p_values.put("U_IMAGE", "0X");
        p_values.put("STUDENTID", "UNSC0001");


        SQLiteDatabase db = getWritableDatabase();
        db.insert("SETTINGS", null, values);
        db.insert("PROFILES", null, p_values);

        System.out.print("Tables Populated");

        db.close();

    }

    public Cursor getSettings() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM SETTINGS", null);

        return res;
    }

    //Validate Login..

    /*

    public int validateLogin(String username, String password){
        int r = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try
        {
            Cursor c = null;
            c = db.rawQuery("select * from PROFILES where username=? and password=?", new String[]{username, password});

            if(c.moveToFirst())

            {
                USERNAME = c.getString(c.getColumnIndex("USERNAME"));
                PASSWORD = c.getString(c.getColumnIndex("PASSWORD"));
                FIRSTNAME = c.getString(c.getColumnIndex("FIRSTNAME"));
                LASTNAME = c.getString(c.getColumnIndex("LASTNAME"));
                DOB = c.getString(c.getColumnIndex("DOB"));
                SEX = c.getString(c.getColumnIndex("SEX"));
                STUDENTTYPE = c.getString(c.getColumnIndex("STYPE"));
                ACCOUNTTYPE = c.getString(c.getColumnIndex("ACCOUNT_TYPE"));
                STUDENTIMAGE = c.getBlob(c.getColumnIndex("U_IMAGE"));
                STUDENTID = c.getString(c.getColumnIndex("STUDENTID"));

                FULLNAME = FIRSTNAME +" "+ LASTNAME;


                System.out.println("DATA IS ");
                System.out.println("--------------");
                System.out.println(STUDENTID+" "+FULLNAME+" "+DOB+" "+PASSWORD);

                r = 1;
            }
            else{
                    System.out.println("USER NOT FOUND") ;
                r=0;
            }

            c.close();
            db.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            r=-1;

            System.out.println("Error verifying user "+e);
        }

        return r;

    }

    */

    public Cursor validateLogin(String username, String password) {
        int r = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        c = db.rawQuery("select * from PROFILES where username=? and password=?", new String[]{username, password});

        return c;
    }

    public void insertLog(String studentID, String activity, String name){

        try {

            ContentValues cv = new ContentValues();

            cv.put("ID", studentID);
            cv.put("ACTIVITY", activity);
            cv.put("NAME", name);

            SQLiteDatabase db = this.getWritableDatabase();
            long a = db.insert("LOGS", null, cv);
            db.close();

            if (a > 0) {
                System.out.println("LOGIN Log Saved " + a);
            } else {
                System.out.println("LOGIN LOG NOT  SAVED " + a);
            }
        }
        catch(Exception nn){
            System.out.println("ERROR SAVING LOGS "+nn);
        }


    }

    public void accessLesson(String studentID, String course, String term, String lesson){

        ContentValues cv = new ContentValues();

        cv.put("STUDENTID", studentID);
        cv.put("COURSE", course);
        cv.put("TERM", term);
        cv.put("LESSON", lesson);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("COURSEVIEW", null, cv);

        db.close();
        System.out.println("Course View Saved");

    }


    public String insertProfile(String fname, String lname, String uname, String upass,
                             String dob, String sex, String atype, byte[] img, String stype, String studentID){

        String x = "";

        try{

            ContentValues values = new ContentValues();

            values.put("FIRSTNAME", fname);
            values.put("LASTNAME", lname);
            values.put("USERNAME", uname);
            values.put("PASSWORD", upass);
            values.put("DOB", dob);
            values.put("SEX", sex);
            values.put("ACCOUNT_TYPE", atype);
            values.put("U_IMAGE", img);
            values.put("STYPE", stype);
            values.put("STUDENTID", studentID);


            SQLiteDatabase db = getWritableDatabase();
           long ax = db.insert("PROFILES", null, values);

            if(ax<0){
                System.out.print("DATA NOT INSERTED INTO TABLE...  AX IS "+String.valueOf(ax));
                x = "DATA NOT INSERTED INTO TABLE...  AX IS "+String.valueOf(ax);
            }
            else{
                System.out.print("Data inserted into profiles table");
                x="GOOD";
            }
            db.close();

        }
        catch(SQLException nn){
            System.out.println("ERROR INSERTING NEW PROFILE..."+nn);
            nn.printStackTrace();
            x=nn.getMessage();
            Log.d(LOGCAT, nn.getMessage());
        }


        return x;

    }

    //Insert Quiz..

    public String insertQuiz(String studentID, String course, String term, String lesson, double score){
        String a ="";

        try{
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("STUDENTID", studentID);
            cv.put("COURSE", course);
            cv.put("TERM", term);
            cv.put("LESSON", lesson);
            cv.put("SCORE", score);

            long ax = db.insert("QUIZ", null, cv);

            if(ax>0){
                System.out.println("QUIZ DATA INSERTED");
                a = "OK";
            }else{
                System.out.println("QUIZ DATA NOT INSERTED");
            }
            db.close();
        }
        catch(Exception nn){
            System.out.println("Error saving Quiz Data --"+nn);
            a = "ERROR HERE -"+nn.getMessage();
        }

        return a;
    }

    //update settings table..

    public String updateSettings(String language, float score, String content, String server){
        String a = "";
        try{

            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("LANGUAGE", language);
            cv.put("SCORE", score);
            cv.put("C_LOC", content);
            cv.put("SERVER", server);

            long v = db.update("SETTINGS", cv, null, null);

            a = "OK";

            db.close();

        }
        catch(Exception nn){

            a = nn.getMessage();
        }

        return a;
    }

    public String getMySetting(){

        String a="";

        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            Cursor c = null;
            c = db.rawQuery("select C_LOC, LANGUAGE, SCORE, SERVER from SETTINGS", null);

            if(c.moveToFirst())

            {
                CLOC = c.getString(c.getColumnIndex("C_LOC"));
                CLANG = c.getString(c.getColumnIndex("LANGUAGE"));
                SDSCORE = c.getString(c.getColumnIndex("SCORE"));
                SERVER = c.getString(c.getColumnIndex("SERVER"));

                a = "OK";

                System.out.println(CLOC+" " +CLANG+ SDSCORE+ SERVER);
            }
            a = "OK";

            c.close();
            db.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            a="ERROR "+e.getMessage();

            System.out.println("Error verifying user "+e);
        }

        return a;
    }

    //Get user profile...

    public Cursor getProfile(String username, String password){
        int r = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c=null;
                c = db.rawQuery("select * from PROFILES where username=? and password=?", new String[]{username, password});

        return c;

    }

    //Update user profile..

    public String updateProfile(String fname, String lname, String uname, String upass,
                              String dob, String sex, String atype, byte[] img,
                             String stype, String studentID, String lUser, String lPassword) {

        String x="";

        try {

            ContentValues values = new ContentValues();

            values.put("FIRSTNAME", fname);
            values.put("LASTNAME", lname);
            values.put("USERNAME", uname);
            values.put("PASSWORD", upass);
            values.put("DOB", dob);
            values.put("SEX", sex);
            values.put("ACCOUNT_TYPE", atype);
            values.put("U_IMAGE", img);
            values.put("STYPE", stype);
            values.put("STUDENTID", studentID);


            SQLiteDatabase db = getWritableDatabase();
            long a =db.update("PROFILES",values,"username=? and password=?", new String[]{lUser, lPassword});

            if(a<=0){
                x = "NIL";
            }else{
             x = "OK";
                System.out.println("LONG A IS "+a);
            }
            db.close();

        } catch (Exception nn) {

            System.out.println("Error Updating data "+nn);
            x="NIL";
        }
        return x;
    }

    //Load history..

    public Cursor loadHistory(String QUERY, String studentID) {

        Cursor c = null;

        try {

            SQLiteDatabase db = this.getReadableDatabase();

            c = db.rawQuery(QUERY, new String[]{studentID});

            System.out.println("CURSOR COUNT IN DB CONN IS ---" + c.getCount());

            db.close();
        } catch (Exception nn) {
            System.out.println("Exception HEREEEEEEEEEEEEEEEEE-------------" + nn);
        }
        return c;

    }

    //Time spent..

    public String hoursSpent(String studentID, int secs) {

        Cursor c=null;
        int res = 0;
        String resx="";
        try {

            String QUERY = "SELECT sum( ((julianday(EDATE) - julianday(DATE)) * 86400.0)) from COURSEVIEW WHERE STUDENTID=?";

            SQLiteDatabase db = this.getReadableDatabase();
            c = db.rawQuery(QUERY, new String[]{studentID});

            int x = c.getCount();
            if(x>0){

                System.out.println("CURRRRSOR COUNT IS "+x);

                while(c.moveToNext()) {
                    int a = c.getInt(0);
                    resx = utx.getFormattedTime(a);
                }
            }
            else{
                System.out.println("HOURS SPENT DATA NOT FOUND");
            }
            c.close();
            db.close();

        } catch (Exception nn) {
            System.out.println("Error retireving hours spent "+nn);
        }

        return resx;
    }

    public Cursor selectTime(String studentID,  String course, String term, String lesson) {

        Cursor c=null;

        try {
            String QUERY = "SELECT CID FROM COURSEVIEW WHERE STUDENTID=? AND COURSE=? AND TERM=? AND LESSON=? ORDER BY CID DESC LIMIT 1";

            SQLiteDatabase db = this.getReadableDatabase();

            c = db.rawQuery(QUERY, new String[]{studentID, course, term, lesson});


            //db.close();

        } catch (Exception nn) {

            System.out.println("ERROR HERE...."+nn);

        }

        return c;
    }

    public void updateTime(String studentID, int CID ) {

        String x = "";
        Cursor c = null;
        try {

            String QUERY = "UPDATE COURSEVIEW SET EDATE =CURRENT_TIMESTAMP WHERE STUDENTID=? AND CID=?";

            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL(QUERY, new String[]{studentID, String.valueOf(CID)});

            //db.close();

        } catch (Exception nn) {
            System.out.println("Error updating time.." + nn);
            x = "EXCEPTION " + nn.getMessage();
        }

    }

    public Cursor exportLog(){
        Cursor c=null;


        return c;
    }

    public Cursor findLogs(String sdate, String edate){
        Cursor c=null;
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            c= db.rawQuery(FindLog.QUERY, new String[]{FindLog.date_s, FindLog.date_e});

            System.out.println("QUERY IS -"+FindLog.QUERY);

            //db.close();
        }
        catch(Exception nn){
            System.out.println("Error retrieving data..."+nn);
        }

        return c;
    }


    public int quizPassed(String studentID, float score, String sign){
        int x =0;

        String QUERY = "SELECT DISTINCT COURSE, TERM, LESSON FROM QUIZ WHERE SCORE"+sign+"? AND STUDENTID=?";
        try{
            SQLiteDatabase db= this.getReadableDatabase();
            Cursor c = db.rawQuery(QUERY, new String[]{String.valueOf(score), studentID});
            int cx = c.getCount();
            if(cx>0){
                System.out.println("CX DATA IS -"+cx);

                while (c.moveToNext()){
                    x++;
                    System.out.println("COURSE -"+c.getString(0));
                    System.out.println("LESSON -"+c.getString(1));
                }
            }
            else{
                System.out.println("NO DATA FOUND "+cx);
            }
            c.close();
            db.close();
        }
        catch(Exception nn){
            System.out.println("Error loading data "+nn);
        }
        return x;
    }

    //Load Quiz History...

    public Cursor loadQuizHistory(String studentID) {

        Cursor c = null;

        try {
            String QUERY = "SELECT DATE, COURSE, TERM, LESSON, SCORE FROM QUIZ WHERE STUDENTID=? ORDER BY DATE DESC";

            SQLiteDatabase db = this.getReadableDatabase();
            c = db.rawQuery(QUERY, new String[]{studentID});

            int cx = c.getCount();
            if(cx>0){
                System.out.println("RECORDS FOUND "+cx);
            }else{
                System.out.println("NO RECORD FOUND");
            }

        } catch (Exception nn) {

            System.out.println("Error retrieving Quiz Data..."+nn);
        }

        return c;
    }

    //Lessons accessed..

    public Cursor lessonAccessed(String studentID, String course, String term, String lesson){
        Cursor c=null;
        String QUERY = "SELECT * FROM COURSEVIEW WHERE STUDENTID=? AND COURSE=? AND TERM=? AND LESSON=?";
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            c= db.rawQuery(QUERY, new String[]{studentID, course, term, lesson});
        }
        catch(Exception nn){
            System.out.println("Error retrieving data..."+nn);
        }

        return c;
    }

    public Cursor avaProfile(String name){
        Cursor c=null;
        String QUERY = "SELECT FIRSTNAME, LASTNAME, STUDENTID, STYPE, U_IMAGE " +
                "FROM PROFILES WHERE FIRSTNAME LIKE '%"+name+"%' OR LASTNAME LIKE '%"+name+"%'";
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            c= db.rawQuery(QUERY, null);
        }
        catch(Exception nn){
            System.out.println("Error retrieving data..."+nn);
        }

        return c;
    }

    public Cursor courseView(){
        Cursor c=null;
        String QUERY = "SELECT STUDENTID, COURSE, TERM, LESSON, DATE, EDATE FROM COURSEVIEW WHERE SYNC='NO'";
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            c= db.rawQuery(QUERY, null);
        }
        catch(Exception nn){
            System.out.println("Error retrieving data..."+nn);
        }

        return c;
    }

    public Cursor quizData(){
        Cursor c=null;
        String QUERY = "SELECT STUDENTID, COURSE, TERM, LESSON, SCORE, DATE FROM QUIZ WHERE SYNC='NO'";
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            c= db.rawQuery(QUERY, null);
        }
        catch(Exception nn){
            System.out.println("Error retrieving quiz data..."+nn);
        }

        return c;
    }

    public Cursor logData(){
        Cursor c=null;
        String QUERY = "SELECT ID, DATE, ACTIVITY, NAME FROM LOGS WHERE SYNC='NO'";
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            c= db.rawQuery(QUERY, null);
        }
        catch(Exception nn){
            System.out.println("Error retrieving quiz data..."+nn);
        }

        return c;
    }

    public Cursor profileData(){
        Cursor c=null;

        String QUERY = "SELECT USERNAME, PASSWORD, FIRSTNAME, LASTNAME, DOB, DOR, SEX, ACCOUNT_TYPE, U_IMAGE, STYPE, STUDENTID FROM PROFILES WHERE SYNC='NO'";
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            c= db.rawQuery(QUERY, null);
        }
        catch(Exception nn){
            System.out.println("Error retrieving profile data..."+nn);
        }

        return c;
    }

    public String completeSync(SQLiteDatabase db){

        String str="";

        String Q1 = "UPDATE PROFILES SET SYNC='YES'";
        String Q2 = "UPDATE LOGS SET SYNC='YES'";
        String Q3 = "UPDATE QUIZ SET SYNC='YES'";
        String Q4 = "UPDATE COURSEVIEW SET SYNC='YES'";

        try{
            db.execSQL(Q1);
            db.execSQL(Q2);
            db.execSQL(Q3);
            db.execSQL(Q4);

            str = " SYNCHRONIZATION COMPLETE SUCCESSFULLY !!!#SUCCESS";
        }
        catch(Exception ex){

            System.out.println("Error completing sync..."+ex);

            str = "ERROR COMPLETING SYNCHRONIZATION#ERROR";
        }
        return str;
    }

    public int deleteProfile(String name, String studentID){
        int str =0;

        String QUERY = "DELETE FROM PROFILES WHERE USERNAME=? AND STUDENTID=?";
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c= db.rawQuery(QUERY, new String[]{name, studentID});

            str = c.getCount();

            System.out.println("DELETE X iS::: "+str);

        }
        catch(Exception nn){
            System.out.println("Error deleting profile data..."+nn);
            str=0;
        }

        return str;
    }

    //Load all Profiles..

    public Cursor getAllProfile(){
        int r = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c=null;
        c = db.rawQuery("select FIRSTNAME, LASTNAME, USERNAME, PASSWORD, ACCOUNT_TYPE, U_IMAGE, STUDENTID from PROFILES WHERE ACCOUNT_TYPE!='ADMINISTRATOR' ORDER BY USERNAME ASC", new String[]{});

        return c;
    }

    //Load all profiles..

    public void profileList(){
        int r = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try
        {
            Cursor c = null;
            c = db.rawQuery("select * from PROFILES", null);

            int xx = c.getCount();

            System.out.println("X is WHAT ---"+xx);

            System.out.println("DATA IS ");
            while(c.moveToNext())

            {
                USERNAME = c.getString(c.getColumnIndex("USERNAME"));
                PASSWORD = c.getString(c.getColumnIndex("PASSWORD"));
                FIRSTNAME = c.getString(c.getColumnIndex("FIRSTNAME"));
                LASTNAME = c.getString(c.getColumnIndex("LASTNAME"));
                DOB = c.getString(c.getColumnIndex("DOB"));
                SEX = c.getString(c.getColumnIndex("SEX"));
                STUDENTTYPE = c.getString(c.getColumnIndex("STYPE"));
                ACCOUNTTYPE = c.getString(c.getColumnIndex("ACCOUNT_TYPE"));
                STUDENTIMAGE = c.getBlob(c.getColumnIndex("U_IMAGE"));
                STUDENTID = c.getString(c.getColumnIndex("STUDENTID"));

                FULLNAME = FIRSTNAME +" "+ LASTNAME;


                System.out.println("--------------");
                System.out.println("Username: "+USERNAME+". PASSWORD- "+PASSWORD+". FIRSTNAME -"+FIRSTNAME+". LASTNAME-"+LASTNAME+". DOB- "+DOB+". SEX- "+SEX+". STUDENTTYPE- "+STUDENTTYPE+". ACCOUNTTYPE-"+ACCOUNTTYPE+". STUDENT ID -"+STUDENTID);

                r = 1;
            }

    }
        catch(Exception e)
    {
        e.printStackTrace();

        System.out.println("Error printing profiles "+e);
    }

}

}
