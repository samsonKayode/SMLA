package com.unesco.smla;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.smla.alerts.DialogBoxes;
import com.smla.database.DatabaseConn;
import com.smla.sessions.MyCustomWebViewClient;
import com.smla.sessions.MyJavaScriptInterface;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class ViewLesson extends AppCompatActivity {

    DialogBoxes box = new DialogBoxes();
    DatabaseConn db = new DatabaseConn(this);

    WebView webView;
    public static String LESSONNAME="";

    String video="";

    public static String term="", coursetitle="", lessonName="";

    public static String VLOC="";

    Button startquiz;

    public static String CL="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lesson);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_lesson_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setD
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_play_circle_filled_white_24dp);

        startquiz = (Button) findViewById(R.id.startQuiz);

        Timer buttonTimer = new Timer();

        String as = getString(R.string.nowPlaying);
        as = "I AM IN CONTROL";

        toolbar.setTitle(as);

        startquiz.setEnabled(false);

        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        startquiz.setEnabled(true);
                        startquiz.setBackgroundColor(getResources().getColor(R.color.green));
                    }
                });
            }
        }, 120000);

        if(VLOC.equals("COURSELIST")){

            String[] x = CourseList.LESSONNAME.split("/");
            LESSONNAME = x[1];

            video = "file:"+CourseList.COURSELOCATION+"/"+LESSONNAME+"/index.html";

            term= FormalCourses.term;
            coursetitle = FormalCourses.courseTitle;
            lessonName = CourseList.LNAME[1];

        }
           if(VLOC.equals("DASH"))
            {

            video = DashBoard.video;

                coursetitle = DashBoard.coursetitle;
                term = DashBoard.term;
                lessonName =DashBoard.lessonName;
        }

        if(VLOC.equals("NONFORMAL")){

            video = "file:"+NonFormalList.COURSELOCATION+"/"+NonFormalList.LNAME[1]+"/index.html";
            coursetitle = "NON-FORMAL";
            term = "N-A";
            lessonName =NonFormalList.LNAME[1];
        }


        webView = (WebView)findViewById(R.id.webView);

        final MyJavaScriptInterface myJavaScriptInterface
                = new MyJavaScriptInterface(this);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(video);


    }

    public void start_Quiz( View v){

        String loc = CourseList.COURSELOCATION2+"/"+ViewLesson.LESSONNAME+".txt";

        System.out.println("QUIZ LOCATION IS ::: "+loc);

        File f = new File(loc);

        if(f.exists()){

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.exitTitle))
                    .setMessage(getString(R.string.confirmLessonQuiz))
                    .setIcon(R.drawable.confirm_icon)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(ViewLesson.this, "You have left the lesson", Toast.LENGTH_SHORT).show();
                            CL = term+" - "+coursetitle+" - "+lessonName;
                            String activity = DashBoard.FULLNAME+" exited lesson "+ CL;
                            db.insertLog(DashBoard.STUDENTID, activity, DashBoard.FULLNAME);
                            //Update Lesson Time...
                            updateTime();

                            //String CL2 = term+" - "+coursetitle+" - "+lessonName;
                            String activity2 = DashBoard.FULLNAME+" Accessed Quiz "+CL;
                            db.insertLog(DashBoard.STUDENTID, activity2, DashBoard.FULLNAME);

                            webView.destroy();

                            if(VLOC.equals("COURSELIST")){
                                //CourseList.LESSONNAME="";

                                StartQuiz.WHERE="LESSON";

                                Intent i = new Intent(ViewLesson.this, StartQuiz.class);
                                startActivity(i);

                            }

                            if(VLOC.equals("DASH")){

                                StartQuiz.WHERE="LESSON";

                                Intent i = new Intent(ViewLesson.this, StartQuiz.class);
                                startActivity(i);
                            }

                        }})
                    .setNegativeButton(android.R.string.no, null).show();

        }else{
            box.showMessageDialog(this, getString(R.string.info),
                    getString(R.string.noQuiz), R.drawable.info_icon);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.exitTitle))
                .setMessage(getString(R.string.confirmExitLesson))
                .setIcon(R.drawable.confirm_icon)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(ViewLesson.this, "You have left the lesson", Toast.LENGTH_SHORT).show();
                        CL = term+" - "+coursetitle+" - "+lessonName;
                        String activity = DashBoard.FULLNAME+" exited lesson "+ CL;
                        db.insertLog(DashBoard.STUDENTID, activity, DashBoard.FULLNAME);
                        //Update Lesson Time...
                        updateTime();

                        webView.destroy();

                        //webView.onPause();

                        if(VLOC.equals("COURSELIST")){
                            CourseList.LESSONNAME="";

                            finish();
                        }if(VLOC.equals("DASH")){

                            Intent i = new Intent(ViewLesson.this, DashBoard.class);
                            startActivity(i);
                        }
                        if(VLOC.equals("NONFORMAL")){
                            NonFormalList.LESSONNAME="";
                            Intent i = new Intent(ViewLesson.this, NonFormalList.class);
                            startActivity(i);

                        }

                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }


    public void updateTime(){

        Cursor c = db.selectTime(DashBoard.STUDENTID, coursetitle, term,lessonName);

        int CID=0;

        int x = c.getCount();

        if(x>0){
            while(c.moveToNext()){

                String a = c.getString(0);
                CID = Integer.parseInt(a);

                System.out.println("CID FOR LESSON IS "+CID);

            }
            c.close();
            db.close();

            db.updateTime(DashBoard.STUDENTID, CID);


        }else{
            System.out.println("NO LESSON TO UPDATE... COUNT IS "+x);
        }


    }

}
