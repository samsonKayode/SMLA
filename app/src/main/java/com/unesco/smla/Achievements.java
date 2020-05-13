package com.unesco.smla;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.smla.content.ContentSettings;
import com.smla.database.DatabaseConn;
import com.smla.sessions.MyCustomAdapter;
import com.smla.sessions.Utilities;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Achievements extends AppCompatActivity {

    DatabaseConn db = new DatabaseConn(this);
    Utilities utx = new Utilities();

    DecimalFormat df = new DecimalFormat("###,###,###.#");

    SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");

    int history_count=0, lessons_completed_count=0, quiz_attempts=0;

    String STUDENTID="";

    TextView timeSpent, lessonsCompleted, availableQuizzes,
            completedQuizzes, lessonsAccessed, incompleteLessons, attempts, fails, passes;

    TableLayout courseHistory, quizHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ach_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_stars_white_24dp);

        timeSpent = (TextView)findViewById(R.id.timeSPent);
        lessonsAccessed = (TextView)findViewById(R.id.lessons_accessed);
        lessonsCompleted = (TextView)findViewById(R.id.lessons_completed);
        availableQuizzes = (TextView) findViewById(R.id.available_quizzes);

        incompleteLessons = (TextView)findViewById(R.id.incomplete_lesson);

        completedQuizzes = (TextView)findViewById(R.id.completed_quizzes);
        fails = (TextView)findViewById(R.id.fails);
        passes = (TextView) findViewById(R.id.passes);
        attempts = (TextView)findViewById(R.id.attempts);


        courseHistory = (TableLayout) findViewById(R.id.course_history);
        courseHistory.setColumnShrinkable(0, true);
        courseHistory.setColumnShrinkable(1, true);
        courseHistory.setColumnShrinkable(2, true);
        courseHistory.setColumnShrinkable(3, true);
        courseHistory.setColumnShrinkable(4, false);

        quizHistory = (TableLayout) findViewById(R.id.quiz_history);

        quizHistory.setColumnShrinkable(0, true);
        quizHistory.setColumnShrinkable(1, true);
        quizHistory.setColumnShrinkable(2, true);
        quizHistory.setColumnShrinkable(3, true);
        quizHistory.setColumnShrinkable(4, false);

        if(DashBoard.ACCOUNTTYPE.equals("STUDENT")||DashBoard.ACCOUNTTYPE.equals("LEARNER")){

            STUDENTID = DashBoard.STUDENTID;
        }else{
            STUDENTID = MyCustomAdapter.STID;
        }




        //TotalHours..
        String xa = db.hoursSpent(STUDENTID, 0);



        if(xa.length()>0){
            timeSpent.setText(xa);
        }else{

        }


        ContentSettings.resx=0;
        int x = ContentSettings.countQuizFile(DashBoard.CLOC+"/QUIZZES");
        availableQuizzes.setText(String.valueOf(x));


        //List of Lessons accessed..
        loadHistory();

        //Total Lessons accessed.

        lessonsAccessed.setText(String.valueOf(history_count));

        //Lessons Completed..
        completedLessons();
        lessonsCompleted.setText(String.valueOf(lessons_completed_count));

        incompleteLessons.setText(String.valueOf(history_count-lessons_completed_count));

        int qPassed = db.quizPassed(STUDENTID, DashBoard.DSCORE, ">=");

        System.out.println("QUIZ PASSEDDDDDD------- "+qPassed);

        passes.setText(String.valueOf(qPassed));

        int qfailed = db.quizPassed(STUDENTID, DashBoard.DSCORE, "<");
        fails.setText(String.valueOf(qfailed));

        int cmp = db.quizPassed(STUDENTID, 0, ">=");
        completedQuizzes.setText(String.valueOf(cmp));

        //Quiz attempts..

        loadQuiz();

        attempts.setText(String.valueOf(quiz_attempts));


    }

    //Lessons Completed..

    public void completedLessons() {

        String QUERY = "SELECT DATE, COURSE, TERM, LESSON, ((julianday(EDATE) - julianday(DATE)) * 86400.0) "
                + "FROM COURSEVIEW WHERE STUDENTID=? AND ((julianday(EDATE) - julianday(DATE)) * 86400.0)>120 ORDER BY DATE DESC";

        Cursor c = db.loadHistory(QUERY, STUDENTID);

        lessons_completed_count = c.getCount();

    }

    //Load Course History..

    public void loadHistory(){

        String QUERY = "SELECT DATE, COURSE, TERM, LESSON, ((julianday(EDATE) - julianday(DATE)) * 86400.0) "
                + "FROM COURSEVIEW WHERE STUDENTID=? ORDER BY DATE DESC";

        //System.out.println("QUERY:::- "+QUERY);
        //System.out.println("STUDENT ID:::- "+STUDENTID);

        Cursor c = db.loadHistory(QUERY, STUDENTID);


        history_count = c.getCount();

        if(history_count>0) {

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
                    courseHistory.addView(row);
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

    //Load Quiz History

    public void loadQuiz(){

        Cursor c = db.loadQuizHistory(STUDENTID);
        quiz_attempts = c.getCount();

        if(quiz_attempts>0) {

            try {

                while(c.moveToNext()) {

                    String date1 = c.getString(0);
                    String course = c.getString(1);
                    String term = c.getString(2);
                    String lesson = c.getString(3);
                    float score = c.getFloat(4);

                    Date dates = sdfIn.parse(date1);
                    String date = sdfOut.format(dates);

                    TableRow row = new TableRow(this);
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    String[] colText = {date + "", course, term, lesson, df.format(score)};

                    for (String text : colText) {
                        TextView tv = new TextView(this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        //tv.setTextSize(16);

                        tv.setPadding(5, 5, 5, 5);
                        tv.setText(text);
                        row.addView(tv);
                    }
                    quizHistory.addView(row);
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

}
