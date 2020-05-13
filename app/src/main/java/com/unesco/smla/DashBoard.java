package com.unesco.smla;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.smla.alerts.DialogBoxes;
import com.smla.content.ContentSettings;
import com.smla.database.DatabaseConn;
import com.smla.sessions.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static TextView student_name, student_status;
    public static ImageView student_image;

    public static String studentID="";
    public static String CLOC ="";
    public static String[] abc;

    public static String video="", coursetitle="", term="", lessonName="";

    public static float DSCORE=0;

    public static String lessonTitlex="", playLesson="";

    String[] lessonTitle;

    TextView timeSpent;
    TextView QZ;

    ContentSettings cs = new ContentSettings();

    Utilities utx = new Utilities();
    DialogBoxes dbox = new DialogBoxes();
    DatabaseConn db = new DatabaseConn(this);

    public static String FIRSTNAME, LASTNAME, FULLNAME, USERNAME, PASSWORD, STUDENTID="", DOB, SEX, STUDENTTYPE, ACCOUNTTYPE;
    public static byte[] STUDENTIMAGE;

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;

    TableLayout tableLayout;

    Dialog dialog;

    SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setLogo(R.mipmap.ic_launcher);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View nav_head = navigationView.getHeaderView(0);

        //Set name and image of user..

        student_name = (TextView)nav_head.findViewById(R.id.student_name);
        student_status = (TextView)nav_head.findViewById(R.id.student_status);

        student_image = (ImageView)nav_head.findViewById(R.id.studentImage);

        timeSpent = (TextView) findViewById(R.id.timeSpent);

        QZ = (TextView)findViewById(R.id.quiz_pass);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(DashBoard.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);

        }
        else{

        }




        //Show Profile..

        showProfile();

        //Table..

        tableLayout = (TableLayout) findViewById(R.id.tableViewDash);

        tableLayout.setColumnShrinkable(0, true);
        tableLayout.setColumnShrinkable(1, true);
        tableLayout.setColumnShrinkable(2, true);
        tableLayout.setColumnShrinkable(3, true);
        tableLayout.setColumnShrinkable(4, false);

        loadHistory();

        totalHoursSpent();

        //Quiz Passed..
        getSettings();
        int x = db.quizPassed(STUDENTID, DSCORE, ">=");
        System.out.println("XXXX ISSSS---"+x);


        QZ.setText(String.valueOf(x));

    }

    //Get settings..



    public void getSettings(){

        String a = db.getMySetting();

        if(a.equals("OK")){
            CLOC= DatabaseConn.CLOC;
            DSCORE = Float.parseFloat(DatabaseConn.SDSCORE);
        }
        else{
            CLOC="";
        }
    }

    //Get total Hours Spent..

    public void totalHoursSpent(){

        String xa = db.hoursSpent(STUDENTID, 0);

        if(xa.length()>0){

                timeSpent.setText(xa);
            }
        else{
            System.out.println("HOURS SPENT DATA NOT FOUND");
        }
    }

    //Load Data into Query...

    public void loadHistory(){

        //String QUERY = "SELECT DATE, COURSE, TERM, LESSON, ((julianday(EDATE) - julianday(DATE)) * 86400.0) "
              //  + "FROM COURSEVIEW WHERE STUDENTID=? AND ((julianday(EDATE) - julianday(DATE)) * 86400.0)>? ORDER BY DATE DESC LIMIT 13";

        String QUERY ="SELECT DATE, COURSE, TERM, LESSON, ((julianday(EDATE) - julianday(DATE)) * 86400.0) "
                + "FROM COURSEVIEW WHERE STUDENTID=? ORDER BY DATE DESC LIMIT 15";

        Cursor c = db.loadHistory(QUERY, STUDENTID);
        int x = c.getCount();

        System.out.print("LOAD HISTORY COUNT IS -"+x);

        if(x>0) {

            try {

                while(c.moveToNext()) {

                    String date1 = c.getString(0);
                    String course = c.getString(1);
                    String term = c.getString(2);
                    String lesson = c.getString(3);
                    int dur = c.getInt(4);
                    String duration = utx.getFormattedTime(dur);


                    Date dates = sdfIn.parse(date1);
                    String date = sdfOut.format(dates);

                    TableRow row = new TableRow(this);
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    String[] colText = {date + "", course, term, lesson, duration};

                    for (String text : colText) {
                        TextView tv = new TextView(this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        //tv.setTextSize(16);

                        tv.setPadding(5, 5, 5, 5);
                        tv.setText(text);
                        row.addView(tv);
                    }
                    tableLayout.addView(row);
                }

                c.close();
                db.close();

            } catch (Exception nn) {
                System.out.println("Error here NN -"+nn);

            }

        }
        else{
            System.out.println("NO DATA FOUND");

        }
    }

    //Get Profile Details...

    public void showProfile(){

        Cursor c = db.getProfile(LoginPage.uname, LoginPage.upass);

        int x = c.getCount();

        if(x==1){
            if(c.moveToFirst()){

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

                student_status.setText(STUDENTTYPE);
                student_image.setImageBitmap(utx.getImage(STUDENTIMAGE));

                //studentID = STUDENTID;

                SpannableString content = new SpannableString(FULLNAME);
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                student_name.setText(content);
            }
            c.close();
            db.close();
        }
        else{
            System.out.println("ERROR ACCESSING USER PROFILE");
        }

    }

    //Lesson Buttons Main method..

    public void lessonButtons(String title){

        ActivityCompat.requestPermissions(DashBoard.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);

        getSettings();

        String mLoc = CLOC+"/PINNED LESSONS/"+STUDENTID+".txt";

        System.out.println("MLOCATION IS -------"+mLoc);
        int a = cs.pinConfirm(mLoc);

        if(a==0){
            //We are fine..
            String lessonTitle1 = cs.findExist(mLoc, title);

             lessonTitle = lessonTitle1.split("#");

            System.out.println("LESSON TITLE IS "+lessonTitle[1]);

            if(STUDENTTYPE.equals("NON FORMAL")){
                playLesson = CLOC+"/NON FORMAL/"+lessonTitle[1];
            }else{
                playLesson = CLOC+"/"+lessonTitle[1];
            }


            if(playLesson.contains("EMPTY")){
                //do nothing...

                dbox.showMessageDialog(this,getString(R.string.dialog_title_error),
                        getString(R.string.unsavedLesson), R.drawable.error_icon);
            }

            else{

                System.out.println("PLAY LESSON IS -- "+playLesson);
                loadplayLesson();
            }


        }else{
            //Error..
            dbox.showMessageDialog(this,getString(R.string.dialog_title_error),
                    getString(R.string.unsavedLesson), R.drawable.error_icon);
        }
    }

    //Shortcut action buttons..

    public void lesson_1(View v){
        //Lesson 1 Action..
        lessonButtons("LESS1");
    }

    public void lesson_2(View v){

        lessonButtons("LESS2");

    }

    public void lesson_3(View v){
        lessonButtons("LESS3");
    }

    public void lesson_4(View v){
        lessonButtons("LESS4");
    }

    public void lesson_5(View v){
        lessonButtons("LESS5");
    }

    public void lesson_6(View v){
        //Lesson 1 Action..
        lessonButtons("LESS6");
    }

    public void editProfile(View v){

        System.out.println("PLEASE TAKE SEVERAL SEATSSSSSS");
        Intent i=new Intent(DashBoard.this, EditProfile.class);
        startActivity(i);
    }

    //Play lesson Video..

    public void loadplayLesson(){

        ViewLesson.VLOC="DASH";

        String lesson_1="";

        System.out.println("STUDENT TYPE- "+STUDENTTYPE);

        if(STUDENTTYPE.equals("NON FORMAL")){

            NonFormalVideo.LOC = "DASH";

            video = "file:"+playLesson+".mp4";

            coursetitle = "NON-FORMAL";
            lessonName =lessonTitle[1];
            term = lessonName.split("/")[0];
            lesson_1 = lessonTitle[1];

            System.out.println("LESSON TITLE 0::: "+lessonTitle[0]);

            db.accessLesson(DashBoard.STUDENTID, coursetitle, term, lessonName.split("/")[1]);

            Toast.makeText(DashBoard.this, "You are now accessing "+lesson_1, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(DashBoard.this, NonFormalVideo.class);
            startActivity(i);

        }else{

            lesson_1 = lessonTitle[1];
            abc = lesson_1.split("/");

            video = "file:"+playLesson+"/index.html";
            coursetitle = DashBoard.abc[1];
            term = DashBoard.abc[0];
            lessonName =DashBoard.abc[2];

            db.accessLesson(DashBoard.STUDENTID, coursetitle, term, lessonName);

            Toast.makeText(DashBoard.this, "You are now accessing "+lesson_1, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(DashBoard.this, ViewLesson.class);
            startActivity(i);
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.exitTitle))
                    .setMessage(getString(R.string.confirmExit))
                    .setIcon(R.drawable.confirm_icon)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(DashBoard.this, "You are logged out", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(DashBoard.this, LoginPage.class);
                            startActivity(i);
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            if(ACCOUNTTYPE.equals("STUDENT")||ACCOUNTTYPE.equals("LEARNER")){
                dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.access_denied), R.drawable.error_icon);
            }else{
                Intent i=new Intent(DashBoard.this, Settings.class);
                startActivity(i);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.courses) {

            if(ACCOUNTTYPE.equals("ADMINISTRATOR")){

                dialog = new Dialog(this);
                dialog.setContentView(R.layout.student_type);
                dialog.setTitle("Title...");

                Button formal = (Button) dialog.findViewById(R.id.formal);
                Button nonformal = (Button) dialog.findViewById(R.id.nonFormal);

                formal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Show Formal page
                        dialog.dismiss();
                        Intent i=new Intent(DashBoard.this, FormalCourses.class);
                        startActivity(i);

                    }
                });

                nonformal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Show NonFormal page
                        Intent i=new Intent(DashBoard.this, NonFormalOption.class);
                        startActivity(i);
                    }
                });


                dialog.show();

            }else{
                if(STUDENTTYPE.equals("NON FORMAL")){
                    //Show Non Formal
                    String activity = FULLNAME+" Opened the courses page.";
                    db.insertLog(STUDENTID, activity, FULLNAME);

                    Intent i=new Intent(DashBoard.this, NonFormalOption.class);
                    startActivity(i);

                }
                else{
                    String activity = FULLNAME+" Opened the courses page.";
                    db.insertLog(STUDENTID, activity, FULLNAME);

                    Intent i=new Intent(DashBoard.this, FormalCourses.class);
                    startActivity(i);
                }

                //Show student side..
            }
            // Handle the course
        } else if (id == R.id.quiz) {

            if(ACCOUNTTYPE.equals("FACILITATOR")||ACCOUNTTYPE.equals("LEARNER")) {
                dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.access_denied), R.drawable.error_icon);
            }
            else{
                Intent i = new Intent(DashBoard.this, QuizPage.class);
                startActivity(i);
            }

        } else if (id == R.id.achieve) {

            if(ACCOUNTTYPE.equals("STUDENT")||ACCOUNTTYPE.equals("LEARNER")){
                Intent i=new Intent(DashBoard.this, Achievements.class);
                startActivity(i);
            }else{

                Intent i=new Intent(DashBoard.this, SearchStudent.class);
                startActivity(i);
            }


        } else if (id == R.id.log) {
            if(ACCOUNTTYPE.equals("STUDENT")||ACCOUNTTYPE.equals("LEARNER")){
                dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.access_denied), R.drawable.error_icon);
            }else{
                Intent i=new Intent(DashBoard.this, FindLog.class);
                startActivity(i);
            }

        } else if (id == R.id.setting) {

            if(ACCOUNTTYPE.equals("STUDENT")||ACCOUNTTYPE.equals("LEARNER")){
                dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.access_denied), R.drawable.error_icon);
            }else{
                Intent i=new Intent(DashBoard.this, Settings.class);
                startActivity(i);
            }



        } else if (id == R.id.sync_data) {

            Intent i = new Intent(DashBoard.this, SyncData.class);
            startActivity(i);

        }
        else if(id ==R.id.library){
            //dbox.showMessageDialog(this, getString(R.string.info), "Module is under construction", R.drawable.error_icon);

            Intent i=new Intent(DashBoard.this, LibraryPage.class);
            startActivity(i);

        }
        else if (id == R.id.logout) {

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.exitTitle))
                    .setMessage(getString(R.string.confirmExit))
                    .setIcon(R.drawable.confirm_icon)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(DashBoard.this, "You are logged out", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(DashBoard.this, LoginPage.class);
                            startActivity(i);
                        }})
                    .setNegativeButton(android.R.string.no, null).show();

        }

        else if (id==R.id.profile){

            if(ACCOUNTTYPE.equals("STUDENT")||ACCOUNTTYPE.equals("LEARNER")){
                dbox.showMessageDialog(this, getString(R.string.dialog_title_error), getString(R.string.access_denied), R.drawable.error_icon);
            }else{
                Intent i=new Intent(DashBoard.this, ProfilePage.class);
                startActivity(i);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
